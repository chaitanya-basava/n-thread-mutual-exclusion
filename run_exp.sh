project_dir="$1"
num_runs="$2"
algo_num="$3"

for ((  n = 2;  n < 13;  n++ ));
do
  bash launcher.sh "$project_dir" "$num_runs" "$algo_num" "$n"
  echo "completed tests for num_threads = $n"
done
