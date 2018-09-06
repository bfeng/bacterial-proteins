#!/usr/bin/env bash

#datafile="../data/data.tsv"
#datafile="../data/fusion2.15k_sampled.tsv"
#datafile="/share/jproject/bfeng/100K-sample.txt"
datafile="/N/u/bfeng/1M-fusion2.tsv"

n=$(awk -F ' ' '{print $1,$2}' ${datafile} | tr " " "\n" | sort -nu | wc -w)
printf "Number of unique points: %s\n" ${n}

min=$(awk -F ' ' '{print $3}' ${datafile} | sort -g | head -n 1)
printf "Minimum score: %s\n" ${min}

max=$(awk -F ' ' '{print $3}' ${datafile} | sort -gr | head -n 1)
printf "Maximum score: %s\n" ${max}

range=$(bc <<< "${max/$'\r'/} - ${min/$'\r'/}")
printf "Score range: %s\n" ${range}

