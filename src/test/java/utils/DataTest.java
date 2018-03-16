package utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataTest {

    @Test
    public void toCsv() throws IOException{
        Path dir = Paths.get("results", "test_files");
        Path file = dir.resolve("boston.csv");
//        Path file = Paths.get(dir, "boston.csv");
        Data data = ProblemReader.read_csv(file,true);
        data.toCsv(dir.resolve("boston_test_write.csv"));
        file = dir.resolve("boston_wo_index.csv");
        data = ProblemReader.read_csv(file,false);
        data.toCsv(dir.resolve("boston_wo_index_write2.csv"));

    }
}