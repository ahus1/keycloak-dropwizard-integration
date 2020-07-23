#!/bin/bash
set -e
set -x
if [ "$TRAVIS_BRANCH" = "master" ] && [ "$TRAVIS_REPO_SLUG" = "ahus1/keycloak-dropwizard-integration" ] && [ "$TRAVIS_PULL_REQUEST" = "false" ]; then
    ./tutorial/update-gh-pages.sh
    mvn deploy --settings travissettings.xml -DskipTests=true -B
fi
