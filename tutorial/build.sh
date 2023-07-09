#!/usr/bin/env bash
set -x
set -e pipefail
mkdir -p $HOME/pages/tutorial
BASEDIR=$(dirname $0)
cd $BASEDIR
bundle install
asciidoctor --failure-level=WARN -r asciidoctor-diagram tutorial.adoc -d book -D output
cp index.html output
