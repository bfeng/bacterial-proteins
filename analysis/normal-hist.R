library(readr)

hist_file = "/Users/bfeng/Desktop/hist-results/part-00000"

point_links = read_csv(hist_file,
                       cols(X1 = col_integer(),
                            X2 = col_integer()),
                       col_names = FALSE)

hx <- point_links$X2

hist(
  hx,
  xlab = "Links",
  main = "Histogram of Links",
  breaks = 10
)
