package utils.loggers;

import pcell.types.ProcessingCell;
import utils.Log;

public abstract class LogManager {
    ProcessingCell cell;

    abstract public void pushLog(Log log);

    abstract public void flush();
}
