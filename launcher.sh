project_dir="$1"
num_runs="$2"
algo_num="$3"
n="$4"

if [[ $project_dir == *".jar" ]]; then
  jar_path="$project_dir"
else
  mvn -f "$project_dir" clean package
  jar_path="$project_dir/target/n_thread_mutual_exclusion_algorithms-1.0-SNAPSHOT-jar-with-dependencies.jar"
fi

mkdir "./tests/"

rm "./tests/$algo_num-$n.txt"

for ((  i = 0;  i < num_runs;  i++ )); do
  java -jar "$jar_path" utd.multicore.Main -a "$algo_num" -n "$n"
done

# bash launcher.sh "./" 10 1 2
