#!/usr/bin/env bash

jar="../target/bacterial-proteins-jar-with-dependencies.jar"
dataFile=/share/project2/FG546/bfeng/score-matrix.csv
points=810278
dimension=810278
outFile=/share/project2/FG546/bfeng/distance-matrix.bin
outWeight=/share/project2/FG546/bfeng/weight-matrix.bin

mpirun -mca btl ^openib java -cp ${jar} edu.indiana.sice.spidal.apps.CSVFileProcessor -input ${dataFile} -points ${points} -dim ${dimension} -output ${outFile} -output_weight ${outWeight}
