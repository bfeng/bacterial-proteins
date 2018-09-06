library(readr)
score_matrix <- read_csv("~/DSC-SPIDAL/bacterial-proteins/data/fusion2.15k_sampled-score-matrix.csv", 
                         cols(
                           .default = col_double()
                         ),
                         col_names = FALSE)
N <- dim(score_matrix)[1]
x <- seq(1, N)
hx <- rep(0, N)

for (i in 1:N) {
  hx[i] = sum(score_matrix[i,] != 0)
  print(i)
}

# plot(hx, type='h' ,xlab="Node Index", ylab="Count of Links")

hist(hx, xlab="Links", main="Histogram of Links", breaks = 100)
