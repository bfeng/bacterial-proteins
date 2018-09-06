#!/bin/bash

#SBATCH -N 1                 # Number of nodes
#SBATCH --tasks-per-node=32

date

rm /share/project2/FG546/bfeng/work/*

./generate_score_matrix.sh

./generate_distance_matrix.sh

./run_damds.sh

./format_data.sh ../output/damds-points.txt

rm *.bin

#./run_dapwc.sh

#./merge_and_format.sh ../output/damds-points.txt ../output/cluster-M10-C10txt

date
