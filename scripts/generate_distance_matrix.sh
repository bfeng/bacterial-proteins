#!/usr/bin/env bash

jar="../target/bacterial-proteins-jar-with-dependencies.jar"
sourceFile=fusion2.15k_sampled.data
dataFile=../data/${sourceFile}.csv
points=9556
dimension=9556
outFile=../data/distance-matrix_${sourceFile}.bin

mpirun -n 32 java -cp ${jar} edu.indiana.sice.spidal.apps.CSVFileProcessor -input ${dataFile} -points ${points} -dim ${dimension} -output ${outFile}
