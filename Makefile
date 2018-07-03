package:
	mvn clean package

install:
	mvn clean install

gendis:
	cd scripts; bash generate_distance_matrix.sh; cd ..
