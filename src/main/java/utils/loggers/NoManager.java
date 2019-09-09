package utils.loggers;

import pcell.types.ProcessingUnit;
import utils.Log;

public class NoManager extends LogManager {

    public NoManager(ProcessingUnit cell) {
        this.cell = cell;
    }

    @Override
    public void pushLog(Log log) {

    }

    @Override
    public void flush() {

    }
}
