package utils.loggers;

import pcell.types.ProcessingUnit;
import utils.Log;

public abstract class LogManager {
    ProcessingUnit cell;

    abstract public void pushLog(Log log);

    abstract public void flush();
}
