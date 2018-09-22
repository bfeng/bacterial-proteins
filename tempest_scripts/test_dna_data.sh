#!/usr/bin/env bash

nprocs=144
hosts="t-004,t-005,t-006,t-008,t-009,t-010"

run(){
    local case=$(printf "%02d" ${1})
    local cp=${2}
    local conf=${3}
    if [ -f $conf ]; then
        printf "[%s]: %s %s\n" $case $cp $conf
        echo "mpirun -oversubscribe --mca btl_tcp_if_include enp24s0f0 -host ${hosts} -np ${nprocs} java -Xms1g -Xmx4g -cp ${cp} edu.indiana.soic.spidal.damds.Program -c ${conf} -n ${nprocs} -t 1 | tee ../output/${case}-summary.txt"
        #rm *.bin
    fi
    sleep 10
}

configs=(
./dna-configs/01-config.properties
./dna-configs/03-config.properties
./dna-configs/05-config.properties
./dna-configs/07-config.properties
./dna-configs/09-config.properties
)

cps=(
$HOME/.m2/repository/edu/indiana/soic/spidal/damds/1.1/damds-1.1-jar-with-dependencies.jar
$HOME/.m2/repository/edu/indiana/soic/spidal/damds/2.0/damds-2.0-jar-with-dependencies.jar
)

declare -i counter
counter=0
for conf in ${configs[*]}; do
    for cp in ${cps[*]}; do
        let "counter += 1"
        run $counter $cp $conf
    done
done
