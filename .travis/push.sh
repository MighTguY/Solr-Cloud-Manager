#!/bin/sh

echo "git push"
git config --global user.email "travis@travis-ci.org"
git config --global user.name "Travis CI"
git config --global push.default current
git checkout ${TRAVIS_BRANCH}
echo "https://mightguy:$1@github.com/mightguy/Solr-Cloud-Manager.git"
git push "https://mightguy:$1@github.com/mightguy/Solr-Cloud-Manager.git"