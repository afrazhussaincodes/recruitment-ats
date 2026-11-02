#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/../.."

echo "=== Ansible syntax validation ==="
ansible-playbook -i ansible/inventory.ini ansible/playbook.yml --syntax-check

echo
echo "=== Configuration verification ==="
ansible-playbook -i ansible/inventory.ini ansible/playbook.yml

echo
echo "=== Repeatability verification ==="
ansible-playbook -i ansible/inventory.ini ansible/playbook.yml

echo
echo "Week 13 Ansible checks completed."
