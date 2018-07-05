# Bacterial Protein Clustering and Visualization

## Prerequisite

1. open-mpi

2. Install `mpi.jar`
    ```bash
    mvn install:install-file -DcreateChecksum=true -Dpackaging=jar -Dfile=<path-to>/mpi.jar -DgroupId=ompi -DartifactId=ompijavabinding -Dversion=<version>
    ```

3. DSC-SPIDAL algorithms

## Compile and Install

1. clone this project
2. run `mvn install`

## Run with the sample data on localhost (this will run both DAMDS and DAPWC algorithms)

```bash
cd local_scripts
./run_all.sh
```

## Run on Cluter Tempest
```bash
cd tempest_scripts
sbatch slurm_job.sh
```

## Visualize

Upload the output file `damds-points.txt.formatted` to the WebPlotViz
