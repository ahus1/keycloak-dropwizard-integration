#!/usr/bin/env bash
set -e
set -x
echo -e "Starting to update gh-pages\n"

#using token to clone gh-pages branch
git clone --quiet --branch=gh-pages https://${GITHUB_ACTOR}:${GITHUB_TOKEN}@github.com/${GITHUB_REPOSITORY}.git gh-pages

git config --global user.email "alexander.schwartz@gmx.net"
git config --global user.name "GitHub Action"

#go into directory and copy data we're interested in to that directory
set -e
cd gh-pages
cp index.html ../tutorial/output
rm -rf ./*
cp -Rf ../tutorial/output/* .

#add, commit and push files
git add -f .

if [ ! -z "$(git status --porcelain)" ]; then
  git commit -m "GitHub build $GITHUB_RUN_ID pushed to gh-pages"
else
  echo no changes
fi

git push -fq origin gh-pages

echo -e "Done publishing to gh-pages.\n"
