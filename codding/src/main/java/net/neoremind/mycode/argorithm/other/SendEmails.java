package net.neoremind.mycode.argorithm.other;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SendEmails {

    String contents = "Hello\nHow are you doing?\nI am good.\n Thank you!!\n";

    // Provided by the file hosting service:
    int Length() {

        // Downloads file size information from server, something like reading
        // Content-Length header, but refuses other parts of the file, or uses some
        // special method like HEAD to get the file size.

        // Mock implementation:
        return contents.length();
    }

    // Provided by the file hosting service:
    // Expects 0 <= start <= start + length <= fileSize
    // Undefined behaviour if indexes are out of range.
    // free after using.
    String Range(int offset, int length) {
        return contents.substring(offset, offset + length);
    }

    List<String> actual = new ArrayList<>();

    void process(String line) {
        System.out.println("--" + line + "--");
        actual.add(line);
        // send a mail line == email-id.
    }

    public List<String> getActual() {
        return actual;
    }

    // ------ your implementation -------

    void worker(int offset, int len) {
       // System.out.println("worker " + offset + " " + len + " " + contents.substring(offset, Math.min(offset + len, Length())));
        int startOffset = offset;
        if (offset != 0) {
            int newLineOffset = findNextNewLineOffset(offset);
            if (newLineOffset == -1) {
                // read to the end
                return;
            }
            startOffset = newLineOffset + 1;
        }

        if (startOffset >= offset + len) {
            return;
        }

        StringBuilder str = new StringBuilder();
        int maxOffset = Math.min(Length(), offset + len);
        while (startOffset < maxOffset) {
            step = Math.min(step, maxOffset - startOffset);
            String temp = Range(startOffset, step);
            str.append(temp);
            startOffset += step;
        }

        int maxLength = Length();
        int tailOffset = startOffset;
        while (tailOffset < maxLength) {
            step = Math.min(step, maxLength - tailOffset);
            String temp = Range(tailOffset, step);
            int indexOfNewLine = temp.indexOf("\n");
            if (indexOfNewLine >= 0) {
                str.append(temp.substring(0, indexOfNewLine + 1));
                break;
            }
            tailOffset += step;
            str.append(temp);
        }

        String[] array = str.toString().split("\n");
        for (String a : array) {
            if (a.length() == 0) {
                continue;
            }
            process(a);
        }
    }

    /**
     * a b c \n d e f
     * /\
     * |
     * return here
     * <p>
     * case1:
     * offset              len
     * -----o--------
     * <p>
     * case2:
     * offset              len
     * --------------
     * <p>
     * case3:
     * offset              len
     * ---------------------o-------
     * <p>
     * case4:
     * offset               len
     * ------------------------------o---
     */
    private int findNextNewLineOffset(int offset) {
        if (offset == 0) {
            return offset;
        }
        int maxLength = Length();
        while (offset < maxLength) {
            step = Math.min(step, maxLength - offset);
            String temp = Range(offset, step);
            int indexOfNewLine = temp.indexOf("\n");
            if (indexOfNewLine >= 0) {
                return offset + indexOfNewLine;
            }
            offset += step;
        }
        return -1;
    }

    void master(int numOfWorkers) {
        int length = Length();
        int eachWorkerLength = length / numOfWorkers;
        int lastWorkerLength = length % numOfWorkers;
        for (int i = 0; i < numOfWorkers; i++) {
            worker(i * eachWorkerLength, eachWorkerLength);
        }
        if (lastWorkerLength > 0) {
            worker(numOfWorkers * eachWorkerLength, eachWorkerLength);
        }
    }

    // api read length
    private int step = 100;

    @Test
    public void testSimple() {
        System.out.println(Length());
        master(4);
    }

    @Test
    public void testComplex() {
        this.contents = genRandom(1000, 10);
        this.actual.clear();
        master(4);
        String[] cc = contents.split("\n");
        assertThat(actual.size(), is(cc.length));
        for (int i = 0; i < cc.length; i++) {
            assertThat(actual.get(i).equals(cc[i]), is(true));
        }
    }

    private Random r = new Random();

    private String genRandom(int len, int newLineNum) {
        String res = RandomStringUtils.randomAlphanumeric(len);
        char[] x = res.toCharArray();
        for (int i = 0; i < newLineNum; i++) {
            x[r.nextInt(len)] = '\n';
        }
        return new String(x);
    }
}
