library(plot3D)
library(readr)

score_matrix_file = "~/DSC-SPIDAL/bacterial-proteins/data/fusion2.15k_sampled-score-matrix.csv"
damds_points_file = "~/DSC-SPIDAL/bacterial-proteins/results/9556-bromberg/damds-points.txt"
out_points_file = "~/DSC-SPIDAL/bacterial-proteins/results/9556-bromberg/damds-points.hist-cls-7.txt"
# score_matrix_file = "~/DSC-SPIDAL/bacterial-proteins/data/score-matrix.csv"
# damds_points_file = "~/DSC-SPIDAL/bacterial-proteins/results/799-bromberg/damds-points.txt"

score_matrix <- read_csv(score_matrix_file,
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

damds_points <- read.delim(damds_points_file, header=FALSE)

# damds_points <- head(damds_points)

tick = ceiling(max(hx) / 5)
cls <- floor(hx / tick) + 1
for (i in 1:N) {
    if(cls[i]>1) {
        cls[i] = cls[i] + 2
    } else {
        #print(c(i, cls[i], hx[i]))
        if(hx[i] >= 5) {
            cls[i] = cls[i] + 1
        }
        if (hx[i] >= 11) {
            cls[i] = cls[i] + 1
            print(c(i, cls[i], hx[i]))
        }
    }
}

damds_points$V5 <- cls

damds_points <- cbind(damds_points, V6=damds_points$V5)

points3D(damds_points$V2, damds_points$V3, damds_points$V4, cex=.1, col = damds_points$V5)

write_tsv(damds_points, out_points_file, col_names = F)
