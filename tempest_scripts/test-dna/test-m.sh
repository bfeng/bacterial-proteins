#!/usr/bin/env bash
conf=config-m.properties
cp=$HOME/.m2/repository/edu/indiana/soic/spidal/damds/2.0/damds-2.0-jar-with-dependencies.jar
nprocs=4

mpirun -np ${nprocs} java -Xms1g -Xmx4g -cp ${cp} edu.indiana.soic.spidal.damds.Program -c ${conf} -n ${nprocs} -t 1 | tee output/m-damds-summary.txt
rm *.bin
