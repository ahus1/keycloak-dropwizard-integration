#!/usr/bin/env bash
set -x
set -e
mkdir -p $HOME/pages/tutorial
BASEDIR=$(dirname $0)
cd $BASEDIR
bundle install
asciidoctor -r asciidoctor-diagram tutorial.adoc -d book -D output | tee asciidoctor.log
# if there are warnings the build should be marked as unsuccessful
grep WARNING asciidoctor.log && exit 1
exit 0
