library(igraph)
library(readr)

# link_file = "~/DSC-SPIDAL/bacterial-proteins/data/data.tsv"
link_file = "~/DSC-SPIDAL/bacterial-proteins/data/fusion2.15k_sampled.tsv"
# link_file = "~/DSC-SPIDAL/bacterial-proteins/data/1M-fusion2.tsv"
links <- read_tsv(link_file, 
                  cols(
                    col_integer(),
                    col_integer(),
                    col_double()
                  ),
                  col_names = c("start", "end", "score"))

full_graph <- graph_from_data_frame(links, directed = F)