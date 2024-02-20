# n-thread-mutual-exclusion

This contains the implementations of `Filter Lock`, `Tournament Tree`, `Shared Filter Lock`
and `Lamport's Bakery` mutual exclusion algorithms for n threads.

## Requirements
1. java@1.8.0_341
2. maven@3.9.4

## Steps to compile and run the project

This code works by generating an executable jar file of the project.

It uses maven for managing the build and packages and hence is a requirement for compiling and running the application smoothly.

1. `cd` into the project's root directory
2. Execute the `launcher.sh` script to run the project for the algorithm of your choice
```
bash launcher.sh <project_path> <number of runs each> <algorithm Id> <number of threads>
```

To run all the experiments execute the `run_exp.sh` script as following
```
bash run_exp.sh <project_path> <number of runs each> <algorithm Id>
```

**Example:**
```
bash launcher.sh "/Users/chaitanyabasava/IdeaProjects/n-thread-mutual-exclusion" 50 4 10
```

**Algorithm number:**
1. Filter Lock
2. Shared memory Filter Lock
3. Tournament Tree
4. Lamport's Bakery
