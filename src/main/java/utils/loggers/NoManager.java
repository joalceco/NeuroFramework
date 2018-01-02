package utils.loggers;

import pcell.types.ProcessingCell;
import utils.Log;

public class NoManager extends LogManager {

    public NoManager(ProcessingCell cell) {
        this.cell = cell;
    }

    @Override
    public void pushLog(Log log) {

    }

    @Override
    public void flush() {

    }
}
