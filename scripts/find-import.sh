#!/bin/sh
find . -name "*.jar" -print0 | xargs --null -n 1 jar tf | fzf | tr '/' '.'
