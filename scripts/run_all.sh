#!/usr/bin/env bash

./generate_adajacency_matrix.sh

./generate_distance_matrix.sh

./run_damds.sh

./format_data.sh ../output/damds-points.txt

rm *.bin
