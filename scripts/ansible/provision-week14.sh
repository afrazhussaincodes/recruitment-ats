#!/usr/bin/env bash
set -euo pipefail

APP_CONTAINER="recruitment-ats"
TEST_CONTAINER="recruitment-ats-week14"
TEST_PORT="8082"

if ! docker inspect "$APP_CONTAINER" >/dev/null 2>&1; then
  echo "ERROR: Existing ATS container was not found."
  exit 1
fi

IMAGE_ID="$(docker inspect --format '{{.Image}}' "$APP_CONTAINER")"

if docker inspect "$TEST_CONTAINER" >/dev/null 2>&1; then
  RUNNING="$(docker inspect --format '{{.State.Running}}' "$TEST_CONTAINER")"

  if [[ "$RUNNING" != "true" ]]; then
    docker start "$TEST_CONTAINER" >/dev/null
    echo "CANDIDATE_STARTED"
  else
    echo "CANDIDATE_ALREADY_EXISTS"
  fi
else
  mapfile -t APP_ENV < <(
    docker inspect --format '{{range .Config.Env}}{{println .}}{{end}}' "$APP_CONTAINER"
  )

  ENV_ARGS=()
  for item in "${APP_ENV[@]}"; do
    [[ -n "$item" ]] && ENV_ARGS+=(--env "$item")
  done

  docker run -d \
    --name "$TEST_CONTAINER" \
    --restart unless-stopped \
    -p "${TEST_PORT}:8080" \
    "${ENV_ARGS[@]}" \
    "$IMAGE_ID" >/dev/null

  echo "CANDIDATE_CREATED"
fi

echo "Candidate container status:"
docker ps --filter "name=^/${TEST_CONTAINER}$" \
  --format 'table {{.Names}}\t{{.Status}}\t{{.Ports}}'

echo "Candidate image: $IMAGE_ID"
