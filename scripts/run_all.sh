#!/usr/bin/env bash

./generate_distance_matrix.sh

./run_damds.sh

./format_data.sh ../data/damds-points.txt