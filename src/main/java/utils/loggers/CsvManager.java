package utils.loggers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import pcell.types.ProcessingCell;
import utils.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;

public class CsvManager extends LogManager {

    final Integer MAX_SIZE = 1000;
    Queue<Log> logs;
    private File file;

//    public CsvManager(ProcessingCell cell) {
//        this.cell = cell;
//        file = new File("temp.csv");
//        logs = new LinkedList<>();
//    }

    public CsvManager(ProcessingCell cell, Path logPath) {
        this.cell = cell;
        this.file = logPath.toFile();
        this.file.getParentFile().mkdirs();
        try {
            this.file.createNewFile();
            FileWriter fileWriter = new FileWriter(this.file);
            CSVPrinter printer = CSVFormat.RFC4180.print(fileWriter);
            Log.csvHeadertoPrinter(printer);
            printer.close();
        } catch (IOException e) {
            System.err.println("Unable to build LogManager, execution will continue...");
        }
        logs = new LinkedList<>();
    }

    @Override
    public void pushLog(Log log) {
        if (maxSizeReach()) {
            try {
                writeLogsToFile();
            } catch (IOException e) {
                System.err.println("Error occurred while writing csv");
                e.printStackTrace();
            }
        }
        logs.add(log);
    }

    @Override
    public void flush() {
        try {
            writeLogsToFile();
        } catch (IOException e) {
            System.err.println("Error occurred while writing csv");
            e.printStackTrace();
        }
    }

    private void writeLogsToFile() throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);
        CSVPrinter printer = CSVFormat.RFC4180.print(fileWriter);
//        Log.csvHeadertoPrinter(printer);
        for (Log log : logs) {
            log.csvToPrinter(printer);
        }
        logs.clear();
        printer.close();
    }

    private boolean maxSizeReach() {
        return logs.size() >= MAX_SIZE;
    }


}
