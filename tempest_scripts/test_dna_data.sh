#!/usr/bin/env bash

nprocs=144
hosts="t-004,t-005,t-006,t-008,t-009,t-010"

run(){
    local case=$(printf "%02d" ${1})
    local cp=${2}
    local conf=${3}
    if [ -f $conf ] && [ -f $cp ]; then
        printf "[%s]: %s %s\n" $case $cp $conf
        mpirun -oversubscribe --mca btl_tcp_if_include enp24s0f0 -host ${hosts} -np ${nprocs} java -Xms1g -Xmx4g -cp ${cp} edu.indiana.soic.spidal.damds.Program -c ${conf} -n ${nprocs} -t 1 | tee ../output/${case}-summary.txt
        rm *.bin
    fi
    sleep 1
}

configs=(
./dna-configs/17-config.properties
./dna-configs/18-config.properties
./dna-configs/19-config.properties
./dna-configs/20-config.properties
)

cps=(
$HOME/.m2/repository/edu/indiana/soic/spidal/damds/1.1/damds-1.1-jar-with-dependencies.jar
$HOME/.m2/repository/edu/indiana/soic/spidal/damds/2.0/damds-2.0-jar-with-dependencies.jar
)

declare -i counter
counter=16
for conf in ${configs[*]}; do
    let "counter += 1"
    if [ $(( counter%2 )) -ne 0 ]; then
        cp=${cps[0]}
    else
        cp=${cps[1]}
    fi
    run $counter $cp $conf
done
