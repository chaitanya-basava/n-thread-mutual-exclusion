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
    public static final int SLEEP = 0;
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

        try {
            Class<?> clazz = ExclusionType.getClassByType(algoId);
            logger.info("Exclusion algorithm: " + clazz);
            Exclusion exclusion = (Exclusion) clazz.getDeclaredConstructor(int.class).newInstance(N);
            CriticalSection criticalSection = new CounterCriticalSection();

            for(int i = 0; i < N; i++) {
                actors[i] = new Actor(i, C, exclusion, criticalSection);
                actors[i].start();
            }
            for(int i = 0; i < N; i++) actors[i].join();

            logger.info("Final value of counter: " + criticalSection.getDetails());
            logger.info("Expected value of counter: " + (N * C));
        } catch (InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException |
                 NoSuchMethodException |
                 IllegalArgumentException |
                 InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
