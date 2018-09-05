#!/usr/bin/env bash


cp=$HOME/.m2/repository/edu/indiana/soic/spidal/damds/1.1/damds-1.1-jar-with-dependencies.jar

confFile=./dna-damds-config.properties

mpirun -n 2 java -cp ${cp} edu.indiana.soic.spidal.damds.Program -c ${confFile} -n 2 -t 1 | tee summary.txt
rm *.bin