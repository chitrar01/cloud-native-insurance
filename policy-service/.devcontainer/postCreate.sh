#!/usr/bin/env bash
set -euo pipefail

# Point system 'java' & 'javac' to 21 explicitly
if command -v update-alternatives >/dev/null 2>&1; then
  if [ -x /usr/lib/jvm/msopenjdk-21/bin/java ]; then
    sudo update-alternatives --set java /usr/lib/jvm/msopenjdk-21/bin/java || true
  fi
  if [ -x /usr/lib/jvm/msopenjdk-21/bin/javac ]; then
    sudo update-alternatives --set javac /usr/lib/jvm/msopenjdk-21/bin/javac || true
  fi
fi

# Export JAVA_HOME for future shells
echo 'export JAVA_HOME=/usr/lib/jvm/msopenjdk-21' | sudo tee /etc/profile.d/java_home.sh >/dev/null

# Ensure Maven wrapper exists
if [ -d "policy-service" ]; then
  (cd policy-service && mvn -N -q wrapper || true)
fi

echo "✅ Java configured:"
java -version
echo "JAVA_HOME=$JAVA_HOME"
