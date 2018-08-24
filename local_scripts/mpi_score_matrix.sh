#!/usr/bin/env bash

cd ..
mvn package

cd local_scripts

time mpirun -n 2 java -cp ../target/bacterial-proteins-jar-with-dependencies.jar \
        edu.indiana.sice.spidal.apps.MPIScoreMatrixGenerator \
        ../data/data.tsv \
        ../output