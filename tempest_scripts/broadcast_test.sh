#!/usr/bin/env bash

run_test() {
    local nprocs=${1}
    local vsize=${2}
    local sparsity=${3}
    printf "%s\t%s\t%s\t" ${nprocs} ${vsize} ${sparsity}
    mpirun -n ${nprocs} java -cp target/mpi-common-io-2.0-SNAPSHOT-uber.jar edu.indiana.sice.dscspidal.mpicommonio.examples.SparseVectorBroadcastExample ${vsize} 10 ${sparsity}
}

sparse=(
0
0.4
0.6
0.8
0.9
0.92
0.94
0.96
0.98
0.99
)

vector=(
10000000
50000000
100000000
500000000
)

procs=(
72
144
)

for np in ${procs[*]} ; do
    for vec in ${vector[*]} ; do
        for sp in ${sparse[*]} ; do
            run_test ${np} ${vec} ${sp}
        done
    done
done

printf "\nDone\n"
