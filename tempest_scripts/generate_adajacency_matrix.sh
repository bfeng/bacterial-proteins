#!/usr/bin/env bash

java -cp ../target/bacterial-proteins-jar-with-dependencies.jar \
        edu.indiana.sice.spidal.apps.DataConverter \
        ../data/fusion2.15k_sampled.tsv \
        ../data/symmetric-adajacency-matrix.csv
