#!/usr/bin/env bash
# Taken from and modified http://sleepycoders.blogspot.de/2013/03/sharing-travis-ci-generated-files.html
if [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
  echo -e "Starting to update gh-pages\n"

  #go to home and setup git
  cd $HOME
  git config --global user.email "alexander.schwartz@gmx.net"
  git config --global user.name "Travis CI"

  #using token clone gh-pages branch
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/ahus1/keycloak-dropwizard-integration.git  gh-pages > /dev/null

  #go into diractory and copy data we're interested in to that directory
  cd gh-pages
  cp -Rf $HOME/pages/* .

  #add, commit and push files
  git add -f .
  git commit -m "Travis build $TRAVIS_BUILD_NUMBER pushed to gh-pages"
  git push -fq origin gh-pages > /dev/null

  echo -e "Done publishing to gh-pages.\n"
fi
