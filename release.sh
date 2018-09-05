#!/bin/bash

echo "Release in progress....."
echo 'current version = ' $1
echo 'bumping to = ' $2
gradle release -Prelease.useAutomaticVersion=true -Prelease.releaseVersion=$1 -Prelease.newVersion=$2
echo "Successfully bumped version to $2"