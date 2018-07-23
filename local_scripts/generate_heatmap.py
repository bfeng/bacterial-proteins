import math
import numpy as np
import pandas as pd
from sklearn.metrics.pairwise import euclidean_distances
import matplotlib

# configure backend here
from matplotlib import gridspec
matplotlib.use('Agg')

import matplotlib.pyplot as plt
import seaborn as sns


def plot_heatmap(df):
    # ax.set_title("Heatmap")
    # ax.set_xlabel("Point")
    # ax.set_ylabel("Point")
    # cmap = sns.light_palette("red", as_cmap=True)
    cmap = sns.diverging_palette(240, 10, sep=20, as_cmap=True)
    ax = sns.heatmap(df,
                     # annot=True, fmt="d",
                     # xticklabels=True, yticklabels=True,
                     # ax=ax,
                     # cbar=False,
                     # cbar_ax=axcb,
                     cmap=cmap,
                     # cmap="coolwarm",
                     square=True,
                     # center=0,
                     cbar_kws={"shrink": .5},
                     mask=df == 0
                     )
    ax.invert_yaxis()


def plot_kde(X, Y):
    sns.kdeplot(
            X,
            Y,
            cmap="Reds",
            shade=True,
            shade_lowest=False)


def get_location(v, minV, maxV, ntick):
    tick = (maxV - minV) / ntick
    loc = math.floor((v - minV) / tick)
    if (loc >= ntick):
        loc = ntick - 1
    return loc


def get_density_df(X, Y, nrow, ncol):
    dens = np.zeros(shape=(nrow, ncol), dtype=int)
    minX = min(X)
    maxX = math.ceil(max(X))
    stepX = (maxX - minX) / ncol
    print(minX, maxX, stepX)
    minY = min(Y)
    maxY = math.ceil(max(Y))
    stepY = (maxY - minY) / nrow
    print(minY, maxY, stepY)
    for idx in range(len(X)):
        x = X[idx]
        y = Y[idx]
        j = get_location(x, minX, maxX, ncol)
        i = get_location(y, minY, maxY, nrow)
        # print(x, y, i, j)
        dens[i, j] = dens[i, j] + 1
    df = pd.DataFrame(dens,
            index=map("{0:1.2f}".format,
                np.arange(minY, maxY, stepY, dtype=float)),
            columns=map("{0:1.1f}".format,
                np.arange(minX, maxX, stepX, dtype=float)))
    return df


def main():
    sns.set()
    input_file = "../data/distance-matrix.9556.csv"
    damds_out = "../data/damds-points.9556.txt"
    distance_df = pd.read_csv(input_file, header=None)
    damds_df = pd.read_csv(damds_out, usecols=[1, 2, 3], header=None, delim_whitespace=True)
    # damds_df = damds_df.head()
    mds_dist_df = euclidean_distances(damds_df, damds_df)
    # print(mds_dist_df)
    fig = plt.figure(figsize=(10, 10))
    fig.suptitle(
        "Heatmap of density\n"
        "Distance formula: (1.0 / score - 1.0 / max) * min * max / (max - min)\n"
        "Dataset: " + input_file + "\n"
        "Dataset: " + damds_out)

    X = np.matrix(mds_dist_df).getA1()
    Y = np.matrix(distance_df).getA1()
    print(X)
    print(Y)
    print("X size:", len(X))
    print("Y size:", len(Y))
    plot_kde(X, Y)
    # dens = get_density_df(X, Y, 100, 100)
    # print(dens)
    # print("Sum:")
    # print(dens.values.sum())
    # plt.plot(X, Y, 'o')

    # plot_heatmap(distance_df, ax_heatmap, ax_cb)
    # plot_heatmap(mds_dist_df, ax_heatmap, ax_cb)
    # plot_heatmap(dens)

    # fig.tight_layout()
    # fig.subplots_adjust(top=0.88, bottom=0.08)
    plt.savefig(input_file + "-heatmap.png", dpi=400)


if __name__=="__main__":
    main()
