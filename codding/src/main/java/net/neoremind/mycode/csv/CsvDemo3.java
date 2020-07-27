package net.neoremind.mycode.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xu.zx
 */
public class CsvDemo3 {

  private static final String[] HEADER = {""};

  public static void main(String[] args) {
    try (FileWriter out = new FileWriter("/tmp/ttt1.sql")) {
      out.write("insert into ttt1_distributed values ");
      for (int i = 1; i <= 10; i++) {
        out.write("(");
        out.write(i);
        out.write(",");
        out.write("ali" + i);
        out.write(",");
        out.write(2020);
        out.write(")");
        if (i != 10) {
          out.append(",");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
