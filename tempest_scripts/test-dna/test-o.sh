conf=config-o.properties
cp=$HOME/.m2/repository/edu/indiana/soic/spidal/damds/1.1/damds-1.1-jar-with-dependencies.jar
nprocs=4

mpirun -np ${nprocs} java -Xms1g -Xmx4g -cp ${cp} edu.indiana.soic.spidal.damds.Program -c ${conf} -n ${nprocs} -t 1 | tee output/o-damds-summary.txt
rm *.bin
