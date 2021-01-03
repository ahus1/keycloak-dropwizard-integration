#!/usr/bin/env bash
set -e
echo -e "Starting to update gh-pages\n"

#using token to clone gh-pages branch
git clone --quiet --branch=gh-pages https://${GITHUB_ACTOR}:${GITHUB_TOKEN}@github.com/${GITHUB_REPOSITORY}.git gh-pages

git config --global user.email "alexander.schwartz@gmx.net"
git config --global user.name "GitHub Action"

#go into diractory and copy data we're interested in to that directory
cd gh-pages
cp -Rf $HOME/pages/* .

#add, commit and push files
git add -f .
git diff --quiet || git commit -m "GitHub build $GITHUB_RUN_ID pushed to gh-pages"
git push -fq origin gh-pages

echo -e "Done publishing to gh-pages.\n"
