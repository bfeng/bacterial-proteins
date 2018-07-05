#!/usr/bin/env bash

jar="../target/bacterial-proteins-jar-with-dependencies.jar"
dataFile=../data/symmetric-adajacency-matrix.csv
points=9556
dimension=9556
outFile=../output/distance-matrix.bin

mpirun -mca btl ^openib java -cp ${jar} edu.indiana.sice.spidal.apps.CSVFileProcessor -input ${dataFile} -points ${points} -dim ${dimension} -output ${outFile}
