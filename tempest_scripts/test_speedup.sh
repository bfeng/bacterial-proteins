#!/usr/bin/env bash

cp=$HOME/.m2/repository/edu/indiana/soic/spidal/damds/1.1/damds-1.1-jar-with-dependencies.jar

confFile=./damds-config.properties

procs=(48 96 144 192 240 288 336 384 432 480 528 576)

run_damds(){
    echo "mpirun -oversubscribe --mca btl_tcp_if_include enp24s0f0 -host t-004,t-005,t-006,t-008,t-009,t-010 -np ${1} java -Xms1g -Xmx4g -cp ${cp} edu.indiana.soic.spidal.damds.Program -c ${confFile} -n ${1} -t 1 | tee summary-${1}.txt"
    echo "rm *.bin"
}

for proc in ${procs[*]}; do
    run_damds ${proc}
done
