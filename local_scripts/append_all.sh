#!/usr/bin/env bash
for (( i = 1; i <= 22; i++ )); do
    case=$(printf "%02d" $i)
    bash ~/DSC-SPIDAL/bacterial-proteins/local_scripts/append_clusters.sh ${case}-damds-points.txt ~/DSC-SPIDAL/bacterial-proteins/results/DNA-data/from-pulasthi/region_2_with_dust.webplotviz > ${case}-damds-points.clusters.txt
done
