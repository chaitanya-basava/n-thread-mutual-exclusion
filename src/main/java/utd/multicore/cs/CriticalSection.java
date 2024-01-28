package utd.multicore.cs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface CriticalSection {
    int execute();

    String getDetails();
}
