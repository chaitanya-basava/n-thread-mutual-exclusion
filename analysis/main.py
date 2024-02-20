import glob
import pandas as pd
import matplotlib.pyplot as plt


files = glob.glob("../tests/*-[0-9]*.csv")

id_algo = {
    1: "FilterLockExclusion",
    2: "SharedFilterLockExclusion",
    3: "TournamentTreeExclusion",
    4: "BakeryExclusion"
}

results = {}

for file in files:
    algo_num, num_threads = map(int, file.split("/")[-1].split('.')[0].split('-'))
    data = pd.read_csv(file, header=None, names=['throughput', 'avg_TAT', 'avg_latency'])

    averages = data.mean().tolist()

    if algo_num not in results:
        results[algo_num] = {}
    results[algo_num][num_threads] = averages

metrics = ['throughput', 'avg_TAT', 'avg_latency']
for i, metric in enumerate(metrics):
    plt.figure(i)
    for algo in results:
        threads = sorted(results[algo].keys())
        values = [results[algo][t][i] for t in threads]
        plt.plot(threads, values, label=f'{id_algo[algo]}')
    plt.xlabel('Number of Threads')
    plt.ylabel(metric)
    plt.title(f'{metric} vs Number of Threads')
    plt.legend()
    plt.grid(True)

    plt.savefig(f"./charts/{metric}.png")
