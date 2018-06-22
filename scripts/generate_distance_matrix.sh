#!/usr/bin/env bash

jar="../target/bacterial-proteins-jar-with-dependencies.jar"

dataFile=../data/data.csv
points=50
dimension=3
outFile=../data/distance-matrix.bin

mpirun -n 2 java -cp ${jar} edu.indiana.sice.spidal.apps.CSVFileProcessor -input ${dataFile} -points ${points} -dim ${dimension} -output ${outFile}