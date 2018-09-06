import random

N = 1000

for i in range(1, N + 1):
    for j in range(i + 1, N + 1):
        # if random.uniform(0, 1) >= 0.9:
        #     print(i, j, random.uniform(0, 1), sep='\t')
        if i < 100 and j < 50:
            print(i, j, random.uniform(0, 1), sep='\t')
        elif 200 < i < 250 and 300 < j < 400:
            print(i, j, random.uniform(0, 1), sep='\t')
        elif 300 < i < 400 and 420 < j < 600:
            print(i, j, random.uniform(0, 1), sep='\t')
        elif 400 < i < 410 and 500 < j < 560:
            print(i, j, random.uniform(0, 1), sep='\t')
        elif 500 < i < 530 and 720 < j < 810:
            print(i, j, random.uniform(0, 1), sep='\t')
        elif 600 < i < 780 and 830 < j < 900:
            print(i, j, random.uniform(0, 1), sep='\t')
        elif 700 < i < 730 and 620 < j < 810:
            print(i, j, random.uniform(0, 1), sep='\t')
        elif 800 < i < 880 and 530 < j < 550:
            print(i, j, random.uniform(0, 1), sep='\t')
        elif i > 980 and j > 950:
            print(i, j, random.uniform(0, 1), sep='\t')
        else:
            print(i, j, 0, sep='\t')
