#!/usr/bin/env bash

jar="../target/bacterial-proteins-jar-with-dependencies.jar"

dataFile=../data/data.csv
points=50
dimension=3
outFile=../data/distance-matrix.bin
outFileText=../data/distance-matrix1.csv

mpirun -n 2 java -cp ${jar} edu.indiana.sice.spidal.apps.CSVFileProcessor -input ${dataFile} -points ${points} -dim ${dimension} -output ${outFile} -output_csv ${outFileText}