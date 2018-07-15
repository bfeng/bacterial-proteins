#!/usr/bin/env bash

rm ../output/*

cd ../
mvn clean package

cd ./local_scripts

./generate_adajacency_matrix.sh

./generate_distance_matrix.sh

#./run_damds.sh
#
#./format_data.sh ../output/damds-points.txt
#
#rm *.bin

#./run_dapwc.sh

#./merge_and_format.sh ../output/damds-points.txt ../output/cluster-M10-C3txt
