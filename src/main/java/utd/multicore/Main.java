package utd.multicore;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utd.multicore.cs.CounterCriticalSection;
import utd.multicore.cs.CriticalSection;
import utd.multicore.exclusion.Exclusion;
import utd.multicore.exclusion.ExclusionType;

import java.lang.reflect.InvocationTargetException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static CommandLine parseArgs(String[] args) {
        Options options = new Options();

        Option protocolOption = new Option("a", "algo", true, "algo to use (int)");
        protocolOption.setRequired(true);
        options.addOption(protocolOption);

        Option numThreadsOption = new Option("n", "numThreads", true, "num of threads");
        numThreadsOption.setRequired(true);
        options.addOption(numThreadsOption);

        Option csCountOption = new Option("c", "csCount", true, "num of cs requests");
        csCountOption.setRequired(true);
        options.addOption(csCountOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }
        return null;
    }

    public static void main(String[] args) {
        CommandLine cmd = Main.parseArgs(args);
        // String logPath = cmd.getOptionValue("log_file_path");
        int algoId = Integer.parseInt(cmd.getOptionValue("algo"));
        int N = Integer.parseInt(cmd.getOptionValue("numThreads"));
        int C = Integer.parseInt(cmd.getOptionValue("csCount"));
        Actor[] actors = new Actor[N];
        Thread[] actorThreads = new Thread[N];

        try {
            Class<?> clazz = ExclusionType.getClassByType(algoId);
            logger.info("Exclusion algorithm: " + clazz);
            Exclusion exclusion = (Exclusion) clazz.getDeclaredConstructor(int.class).newInstance(N);
            CriticalSection criticalSection = new CounterCriticalSection();

            long actorStartMillis = System.currentTimeMillis();
            for(int i = 0; i < N; i++) {
                Actor actor = new Actor(i, C, exclusion, criticalSection);
                actors[i] = actor;
                actorThreads[i] = new Thread(actors[i]);
                actorThreads[i].start();
            }
            for(int i = 0; i < N; i++) actorThreads[i].join();
            long actorEndMillis = System.currentTimeMillis();

            logger.info("Final value of counter: " + criticalSection.getDetails());
            logger.info("Expected value of counter: " + (N * C));

            double diff = (actorEndMillis - actorStartMillis) * 1.0;
            double throughput = Double.parseDouble(criticalSection.getDetails()) / diff;
            logger.info("System Throughput: " + throughput);

            double avgTAT = 0, avgLatency = 0;
            for(Actor actor: actors) {
                for(int i = 0; i < C; i++) {
                    avgLatency += actor.getLatencyAtI(i);
                    avgTAT += actor.getTurnaroundTimeAtI(i);
                }
            }
            avgTAT /= Double.parseDouble(criticalSection.getDetails());
            avgLatency /= Double.parseDouble(criticalSection.getDetails());

            logger.info("Latency: " + avgLatency);
            logger.info("Turn Around Time: " + avgTAT);

        } catch (InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException |
                 NoSuchMethodException |
                 IllegalArgumentException |
                 InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
