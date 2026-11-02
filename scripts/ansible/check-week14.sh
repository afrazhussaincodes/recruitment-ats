#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/../.."

echo "=== Syntax check ==="
ansible-playbook -i ansible/inventory.ini ansible/week14.yml --syntax-check

echo
echo "=== Provision and validate ==="
ansible-playbook -i ansible/inventory.ini ansible/week14.yml

echo
echo "=== Repeatability run ==="
ansible-playbook -i ansible/inventory.ini ansible/week14.yml

echo
echo "=== Candidate health ==="
docker exec recruitment-ats-week14 sh -c 'wget -qO- http://127.0.0.1:8080/actuator/health'

echo
echo "=== Production health ==="
docker exec recruitment-ats sh -c 'wget -qO- http://127.0.0.1:8080/actuator/health'

echo
echo "Week 14 verification completed."
