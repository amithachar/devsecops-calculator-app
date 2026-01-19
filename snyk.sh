#!/bin/bash
curl -Lo snyk https://github.com/snyk/snyk/releases/latest/download/snyk-linux
chmod 777 snyk
mv snyk /usr/local/bin/
snyk --version