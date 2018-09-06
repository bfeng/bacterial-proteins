#!/usr/bin/env bash

cd ..; mvn package; cd local_scripts;


jar="../target/bacterial-proteins-jar-with-dependencies.jar"

time mpirun -n 4 java -cp ${jar} edu.indiana.sice.spidal.apps.SparseDistanceMatrixGenerator
