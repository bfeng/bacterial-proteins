library(readr)

# hist_file = "~/DSC-SPIDAL/bacterial-proteins/data/9556-histogram-data.csv"
# hist_file = "~/DSC-SPIDAL/bacterial-proteins/data/810278-histogram-data.csv"
# hist_file = "~/DSC-SPIDAL/bacterial-proteins/data/13227349-hist-data.csv"
# hist_file = "~/DSC-SPIDAL/bacterial-proteins/data/hist-all.csv"
hist_file = "~/DSC-SPIDAL/bacterial-proteins/data/new-hist-all.csv"
# hist_file = "~/DSC-SPIDAL/bacterial-proteins/data/3M-hist-data.csv"
point_links = read_csv(hist_file,
                       cols(
                         X1 = col_integer(),
                         X2 = col_integer()
                       ),
                      col_names = FALSE)

hx <- point_links$X2

# hist(hx, xlab="Links", main="Histogram of Links",
     # xaxt='n',
     # xlim=c(0, 2000),
     # breaks = c(0, 100, Inf)
     # )

my_breaks <- seq.int(0, 5000, by=100)

output <- cut(hx, breaks = c(my_breaks, 7500, 10000, Inf), dig.lab=5)

# hist(as.numeric(output))
par(mar=c(7, 6.8, 4.1, 2.1))
barplot(table(output), space = 0.1, las=2, 
        # cex.axis =.9, cex.names = .9,
        border=T,
        xpd=F,
        ylim=c(0, 3500000),
        main="Distribution of Links",
        ylab="",
        xlab='')
mtext(text="Links", side=1, line=5.5)
mtext(text="Frequency", side=2, line=5.5)
