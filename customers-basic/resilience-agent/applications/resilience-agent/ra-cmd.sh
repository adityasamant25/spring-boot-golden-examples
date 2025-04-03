#!/bin/sh

cd $(readlink -f "$0" | xargs dirname | xargs dirname | xargs dirname)

if [ -z "$JAVA_HOME" ]; then
  echo "Loading environment variables from setEnv.sh"
  . ./setEnv.sh
fi

$JAVA_HOME/bin/java -jar applications/resilience-agent/lib/runtime.jar "$@"