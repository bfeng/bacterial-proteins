awk '{printf "%s\t%s\n", $5, $6}' $2 > ._clusters.txt

awk '{getline clusters < "._clusters.txt"; printf "%s\t%s\t%s\t%s\t%s\n", $1, $2, $3, $4, clusters}' $1

rm ._clusters.txt

