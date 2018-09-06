#!/usr/bin/env bash

datafile="../data/24520-points-filtered.tsv"

n=$(awk -F ' ' '{print $1,$2}' ${datafile} | tr " " "\n" | sort | uniq | wc -w)
min=$(awk -F ' ' '{print $3}' ${datafile} | sort -g | head -n 1)
max=$(awk -F ' ' '{print $3}' ${datafile} | sort -gr | head -n 1)
range=$(bc <<< "${max/$'\r'/} - ${min/$'\r'/}")

printf "Number of unique points: %s\n" ${n}
printf "Minimum score: %s\n" ${min}
printf "Maximum score: %s\n" ${max}
printf "Score range: %s\n" ${range}
