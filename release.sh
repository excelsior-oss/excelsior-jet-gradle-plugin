#!/usr/bin/env bash
# Performs release of current version into maven central
./gradlew -x test clean release closeAndPromoteRepository