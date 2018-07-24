#!/usr/bin/env bash

java -cp ../target/bacterial-proteins-jar-with-dependencies.jar \
        edu.indiana.sice.spidal.apps.DataConverter \
        ../data/data.tsv \
        ../output/score-matrix.csv