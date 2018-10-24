library(igraph)
library(readr)

# link_file = "~/DSC-SPIDAL/bacterial-proteins/data/data.tsv"
link_file = "~/DSC-SPIDAL/bacterial-proteins/data/fusion2.15k_sampled.tsv"
# link_file = "~/DSC-SPIDAL/bacterial-proteins/data/fusion2.15k_filtered.tsv"
# link_file = "~/DSC-SPIDAL/bacterial-proteins/data/1M-fusion2.tsv"
links <- read_tsv(link_file, 
                                  cols(
                                    col_integer(),
                                    col_integer(),
                                    col_double()
                                  ),
                  col_names = c("start", "end", "score"))

g1 <- graph_from_data_frame(links, directed = F)

E(g1)$weight <- links$score

png(filename="graph-9556.png", 
    type="cairo",
    units="in", 
    width=5, 
    height=5, 
    pointsize=12, 
    res=300)

# plot(g1, vertex.label=NA, vertex.size=1, edge.width=E(g1)$weight)
plot(g1, vertex.label=NA, vertex.size=.5, edge.width=.4)

# dist <- degree_distribution(g1, cumulative = T, mode="all")

# clp <- cluster_label_prop(g1)
# plot(clp, g1, vertex.label=NA, vertex.size=2, edge.width=.4)

# ceb <- cluster_edge_betweenness(g1)
# imc <- cluster_infomap(g1)
# plot(g1,
#      col = membership(imc),
#      mark.groups = communities(imc),
#      vertex.label=NA, vertex.size=1, edge.width=E(g1)$weight)

dev.off()