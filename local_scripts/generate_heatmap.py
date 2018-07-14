import numpy as np
import pandas as pd
import matplotlib

# configure backend here
matplotlib.use('Agg')

import matplotlib.pyplot as plt
import seaborn as sns

sns.set()

input_file = "../output/distance-matrix.799.csv"

df = pd.read_csv(input_file, header=None)

f, ax = plt.subplots(figsize=(13, 13))
h = sns.heatmap(df,
                annot=False, fmt="1.1f",
                ax=ax,
                # cmap="coolwarm_r",
                square=True,
                mask=df == 1)
h.invert_yaxis()
plt.savefig(input_file + "-heatmap.png")

f, ax = plt.subplots(figsize=(13, 13))
hist_value = df.values.flatten()
hist_value = hist_value[hist_value != 1]
sns.distplot(hist_value, hist=True, bins=100, kde=False, rug=False, ax=ax)
plt.savefig(input_file + "-histgram.png")

# f, ax = plt.subplots(figsize=(13, 13))
# sns.distplot(hist_value, hist=True, bins=100, kde=False, rug=False, vertical=True)
# plt.savefig(input_file + "-histgram-v.png")


f, ax = plt.subplots(figsize=(13, 13))
xaxis = range(len(df.columns))
xprojection = np.zeros(len(df.columns))

for col in df.columns:
    xprojection[col] = (sum(list(map((lambda x: 0 if x == 1 else 1), df[col]))))
# plt.fill_between(xaxis, xprojection, color='darkblue')
sns.barplot(x='Point', y='Count', data=pd.DataFrame({'Point': xaxis, 'Count': xprojection}), ax=ax, color='darkblue')
plt.savefig(input_file + "-xprojection.png")

f, ax = plt.subplots(figsize=(13, 13))
sns.barplot(x='Count', y='Point', data=pd.DataFrame({'Point': xaxis, 'Count': xprojection}), ax=ax, color='darkblue',
            orient='h')
plt.savefig(input_file + "-yprojection.png")
