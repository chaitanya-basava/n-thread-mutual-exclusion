package utd.multicore.cs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utd.multicore.Actor;

public interface CriticalSection {
    void execute();

    String getDetails();
}
