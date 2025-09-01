#!/usr/bin/env bash
set -euo pipefail

# Ensure Maven wrapper exists (so CI & local are consistent)
if [ -d "policy-service" ]; then
  cd policy-service
  mvn -N -q wrapper || true
  cd -
fi

echo "✅ Dev container ready. Use: cd policy-service && ./mvnw spring-boot:run"
