#!/usr/bin/env bash

#time java -cp ../target/bacterial-proteins-jar-with-dependencies.jar \
#        edu.indiana.sice.spidal.apps.DataConverter \
#        ../data/data.tsv \
#        ../output/score-matrix.csv

#time java -cp ../target/bacterial-proteins-jar-with-dependencies.jar \
        #edu.indiana.sice.spidal.apps.DataConverter \
        #../data/fusion2.15k_sampled.tsv \
        #../output/fusion2.15k_sampled-score-matrix.csv

#time java -cp ../target/bacterial-proteins-jar-with-dependencies.jar \
#        edu.indiana.sice.spidal.apps.ScoreMatrixGenerator \
#        ../data/data.tsv \
#        ../output/score-matrix-new.csv

#time java -cp ../target/bacterial-proteins-jar-with-dependencies.jar \
        #edu.indiana.sice.spidal.apps.ScoreMatrixGenerator \
        #../data/fusion2.15k_sampled.tsv \
        #../output/fusion2.15k_sampled-score-matrix-new.csv

#time java -cp ../target/bacterial-proteins-jar-with-dependencies.jar \
        #edu.indiana.sice.spidal.apps.ScoreMatrixGenerator \
        #../data/3M-points.tsv \
        #../output/3M-points-score-matrix-new.csv

#time java -cp ../target/bacterial-proteins-jar-with-dependencies.jar \
        #edu.indiana.sice.spidal.apps.ScoreMatrixGenerator \
        #../data/1M-points.tsv \
        #../output/1M-points-score-matrix

#time java -cp ../target/bacterial-proteins-jar-with-dependencies.jar \
#        edu.indiana.sice.spidal.apps.ScoreMatrixGenerator \
#        ../data/500K-points.tsv \
#        ../output/500K-points-score-matrix

time java -cp ../target/bacterial-proteins-jar-with-dependencies.jar \
        edu.indiana.sice.spidal.apps.ScoreMatrixGenerator \
        ../data/100K-points.tsv \
        ../output/100K-points-score-matrix-new.csv
