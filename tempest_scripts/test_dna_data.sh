#!/usr/bin/env bash


cp=$HOME/.m2/repository/edu/indiana/soic/spidal/damds/1.1/damds-1.1-jar-with-dependencies.jar

confFile=./dna-damds-config.properties

nprocs=144

mpirun -oversubscribe --mca btl_tcp_if_include enp24s0f0 -host t-004,t-004,t-006,t-007,t-008,t-009 -np ${nprocs} java -Xms1g -Xmx4g -cp ${cp} edu.indiana.soic.spidal.damds.Program -c ${confFile} -n ${nprocs} -t 1 | tee summary.txt
rm *.bin
