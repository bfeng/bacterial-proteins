#!/usr/bin/env bash

count_weight(){
    local matrix=${1}
    if [ -f ${matrix} ]; then
        java -cp target/bacterial-proteins-jar-with-dependencies.jar edu.indiana.sice.spidal.apps.CountZeroWeights ${matrix}
        sleep 1
    else
        echo "${matrix} not found!"
    fi
}

if [ -p /dev/stdin ]; then
    while IFS= read line; do
        echo "File: ${line}"
        count_weight ${line}
    done
fi