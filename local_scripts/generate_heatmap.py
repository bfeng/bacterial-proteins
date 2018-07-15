import numpy as np
import pandas as pd
import matplotlib

# configure backend here
from matplotlib import gridspec

matplotlib.use('Agg')

import matplotlib.pyplot as plt
import seaborn as sns

sns.set()

input_file = "../output/distance-matrix.9556.csv"
df = pd.read_csv(input_file, header=None)


def plot_heatmap(df, ax, axcb):
    ax.set_title("Heatmap")
    ax.set_xlabel("Point")
    ax.set_ylabel("Point")
    ax = sns.heatmap(df,
                     annot=False, fmt="1.1f",
                     # xticklabels=50, yticklabels=50,
                     ax=ax,
                     # cbar=False,
                     cbar_ax=axcb,
                     # cmap="coolwarm_r",
                     # square=True,
                     # center=0.5,
                     cbar_kws={"shrink": .5},
                     mask=df == 1)
    ax.invert_yaxis()


def plot_histogram(df, ax):
    ax.set_title("Histogram of distance values")
    ax.set_xlabel("Distance")
    ax.set_ylabel("Count")
    ax.set_xlim(0, 1)
    hist_value = df.values.flatten()
    hist_value = hist_value[hist_value != 1]
    sns.distplot(hist_value, hist=True, bins=100, kde=False, rug=False, ax=ax)


def plot_projection(df, ax_x, ax_y):
    size = len(df.columns)
    points = range(size)
    counts = np.zeros(len(df.columns))

    for col in df.columns:
        counts[col] = (sum(list(map((lambda x: 0 if x == 1 else 1), df[col]))))

    local_df = pd.DataFrame({'Point': points, 'Count': counts})
    # num_bins = 160
    # plt.hist(local_df['Count'], bins=num_bins)
    # sns.barplot(x='Point', y='Count',
    #             data=local_df,
    #             ax=ax_x,
    #             color='darkblue')
    #
    # sns.barplot(x='Count', y='Point',
    #             data=local_df,
    #             ax=ax_y,
    #             color='darkblue',
    #             orient='h')
    # ax_y.invert_yaxis()

    ax_x.set_xlabel('Point')
    ax_x.set_ylabel('Count')
    ax_x.set_title("Histogram of point distances")
    ax_x.bar(local_df['Point'],
             local_df['Count'],
             width=1.0, color='darkblue',
             align='edge')
    ax_x.set_xlim(0, size)
    plt.setp(ax_x.get_xticklabels(), rotation=90)

    ax_y.set_title("Histogram (rotated) of point distances")
    ax_y.set_xlabel('Count')
    ax_y.set_ylabel('Point')
    ax_y.barh(local_df['Point'],
              local_df['Count'],
              height=1.0,
              color='darkblue',
              align='edge')
    ax_y.set_ylim(0, size)
    # plt.setp(ax_y.get_yticklabels(), rotation=90)


# fig = plt.figure(figsize=(26, 25))
fig = plt.figure(figsize=(10, 10))
# fig.suptitle("Heatmap with x y axis projection and distance histgram\nDataset: " + input_file, fontsize=16)
fig.suptitle(
    "Heatmap with x y axis projection and distance histgram\n"
    "Distance formula:(1.0 / score - 1.0 / max) * min * max / (max - min)\n"
    "Dataset: " + input_file)
# plt.title("Heatmap with x y axis projection and distance histgram\nDataset: " + input_file)
# ax_yprojection = plt.subplot2grid((2, 3), (0, 0))
# ax_heatmap = plt.subplot2grid((2, 3), (0, 1))
# ax_cb = plt.subplot2grid((2, 3), (0, 2))
# ax_histogram = plt.subplot2grid((2, 3), (1, 0))
# ax_xprojection = plt.subplot2grid((2, 3), (1, 1))

spec = gridspec.GridSpec(ncols=3, nrows=2, width_ratios=[1, 1, 0.08], height_ratios=[1, 1])
ax_xprojection = fig.add_subplot(spec[1, 1])
ax_yprojection = fig.add_subplot(spec[0, 0])
ax_heatmap = fig.add_subplot(spec[0, 1], sharex=ax_xprojection, sharey=ax_yprojection)
ax_cb = fig.add_subplot(spec[0, 2])
ax_histogram = fig.add_subplot(spec[1, 0])

plot_heatmap(df, ax_heatmap, ax_cb)
plot_histogram(df, ax_histogram)
plot_projection(df, ax_xprojection, ax_yprojection)

# ax_heatmap.get_shared_x_axes().join(ax_heatmap, ax_xprojection)

fig.tight_layout()
fig.subplots_adjust(top=0.88)
plt.savefig(input_file + "-assembled.png", dpi=400)
