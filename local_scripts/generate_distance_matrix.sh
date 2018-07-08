#!/usr/bin/env bash

jar="../target/bacterial-proteins-jar-with-dependencies.jar"
dataFile=../data/symmetric-adajacency-matrix.csv
points=799
dimension=799
outFile=../output/distance-matrix.bin

mpirun -n 2 java -cp ${jar} edu.indiana.sice.spidal.apps.CSVFileProcessor -input ${dataFile} -points ${points} -dim ${dimension} -output ${outFile} -output_csv ${outFile}.csv
