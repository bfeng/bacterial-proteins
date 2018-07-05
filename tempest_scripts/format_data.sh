#!/usr/bin/env bash

output=$1".formatted"
sed 's/\t/ /g' $1 > ${output}