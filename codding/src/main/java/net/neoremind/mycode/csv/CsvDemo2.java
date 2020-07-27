package net.neoremind.mycode.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xu.zx
 */
public class CsvDemo2 {

  private static final String[] HEADER = {""};

  public static void main(String[] args) {
    CSVFormat csvFormat = CSVFormat.newFormat(',')
        .withRecordSeparator("\n")
        .withNullString("NULL");
        // .withQuote('"')
        //.withQuoteMode(QuoteMode.ALL_NON_NULL);
    int start = 0;
    int fileNum = 10;
    int linePerFile = 50;
    for (int i = 0; i < fileNum; i++) {
      try (Writer out = new FileWriter("/tmp/ttt1_" + i + ".csv");
           CSVPrinter printer = new CSVPrinter(out, csvFormat)) {
        List<Object> records = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
          records.clear();
          records.add(start + (i * linePerFile + j));
          records.add("a" + (i * linePerFile + j));
          records.add(20201020);
          printer.printRecord(records);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}
