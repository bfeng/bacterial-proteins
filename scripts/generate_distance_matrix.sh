#!/usr/bin/env bash

jar="../target/bacterial-proteins-jar-with-dependencies.jar"

dataFile=../data/fusion2.15k_sampled.data.csv
points=9556
dimension=9556
outFile=../data/distance-matrix.bin

mpirun -n 2 java -cp ${jar} edu.indiana.sice.spidal.apps.CSVFileProcessor -input ${dataFile} -points ${points} -dim ${dimension} -output ${outFile}