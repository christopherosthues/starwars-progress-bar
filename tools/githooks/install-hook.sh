#!/bin/zsh
echo Installing pre-commit hook
if [[ $(uname -s) == MINGW* ]];
then
  echo Downloading ktlint jar file
  curl -sSLO https://github.com/pinterest/ktlint/releases/download/0.47.0/ktlint && chmod a+x ktlint && mv ktlint ../../ktlint.jar
fi
echo Copying pre-commit hook to .git/hooks/pre-commit
cp pre-commit ../../.git/hooks/pre-commit
echo pre-commit hook installed
