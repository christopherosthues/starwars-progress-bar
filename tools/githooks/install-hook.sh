#!/bin/bash
echo Installing pre-commit hook
if [[ $(uname -s) == MINGW* ]];
then
  echo Downloading ktlint jar file and installing it in the root directory of the project
  curl -sSLO https://github.com/pinterest/ktlint/releases/download/0.47.1/ktlint && chmod a+x ktlint && mv ktlint ../../ktlint.jar
else
  echo Downloading ktlint and installing it to /usr/local/bin/
  curl -sSLO https://github.com/pinterest/ktlint/releases/download/0.47.1/ktlint && chmod a+x ktlint && sudo mv ktlint /usr/local/bin/
fi
echo Copying pre-commit hook to .git/hooks/pre-commit
cp pre-commit ../../.git/hooks/pre-commit
echo pre-commit hook installed
