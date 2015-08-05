set -x
set -e
mkdir -p $HOME/pages/tutorial
BASEDIR=$(dirname $0)
cd $BASEDIR
asciidoctor -r asciidoctor-diagram tutorial.adoc -d book -D $HOME/pages
