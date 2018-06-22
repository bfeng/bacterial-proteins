package:
	mvn clean package

install:
	mvn clean install

gendis:
	cd scripts; sh generate_distance_matrix.sh; cd ..
