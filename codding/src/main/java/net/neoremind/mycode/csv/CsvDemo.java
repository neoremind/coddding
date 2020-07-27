package net.neoremind.mycode.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.EnumUtils;

import java.io.IOException;

/**
 * @author xu.zx
 */
public class CsvDemo {

  public static final CSVFormat FIELD_FORMAT_QUOTED;

  static {
    // delimiter is not used
    FIELD_FORMAT_QUOTED = CSVFormat.newFormat(',')
        .withNullString("NULL")
        .withQuote('"')
        .withQuoteMode(QuoteMode.ALL_NON_NULL);
  }

  /**
   * Use {@link StringBuilder} to build string out of an array.
   * <p>
   * Sometimes by reusing StringBuilder, we can avoid creating many StringBuilder and good to garbage collection.
   *
   * @param a         array
   * @param b         reusable StringBuilder
   * @param delimiter delimiter
   * @param quote     whether to quote value
   * @param newLine   if this is a new line, if true, write slash n at the end
   * @return array string
   */
  public static String arrayToString(Object[] a, StringBuilder b, String delimiter, boolean quote, boolean newLine) {
    if (a == null) {
      return "null";
    }
    // clean StringBuilder
    b.delete(0, b.length());
    for (int i = 0; i < a.length; i++) {
      if (quote) {
        try {
          FIELD_FORMAT_QUOTED.print(a[i], b, true);
        } catch (IOException e) {
          // should not happen as StringBuilder does not throw IOException
          throw new IllegalStateException(e);
        }
      } else {
        b.append(a[i]);
      }
      b.append(delimiter);
    }
    if (b.length() > 0) {
      b.deleteCharAt(b.length() - 1);
    }
    if (newLine) {
      b.append("\n");
    }
    return b.toString();
  }

  private static String arrayToString(Object[] a, String delimiter) {
    return arrayToString(a, delimiter, false);
  }

  private static String arrayToString(Object[] a, String delimiter, boolean quote) {
    return arrayToString(a, new StringBuilder(), delimiter, quote, false);
  }

  public static void main(String[] args) {
    Object[] array = new Object[6];
    array[0] = 1;
    array[1] = "abc";
    array[2] = 3.4;
    array[3] = 6.6D;
    array[4] = 10000000L;
    System.out.println(arrayToString(array, ",", true));

    // org.apache.commons.lang3.EnumUtils
    QuoteMode quoteMode = EnumUtils.getEnum(QuoteMode.class, "NON_NUMEIC");
    System.out.println(quoteMode);

    for (int i = 0; i < 10; i++) {

    }
  }

}
