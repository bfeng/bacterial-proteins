#!/usr/bin/env bash

rm ../output/*

cd ../
mvn clean package

cd ./local_scripts

#./generate_score_matrix.sh

./generate_distance_matrix.sh

./run_damds.sh
./format_data.sh ../output/damds-points.txt
rm *.bin

cp ../output/damds-points.txt ../results/799-bromberg/
cp ../output/distance-matrix.bin ../results/799-bromberg/
./dump-matrix.sh

#./run_dapwc.sh

#./merge_and_format.sh ../output/damds-points.txt ../output/cluster-M10-C3txt
