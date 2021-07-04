package net.neoremind.mycode.argorithm.other;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class SendEmails2 {

    private static final String contents = "Hello\nHow are you doing?\nI am good.\n Thank you\n";

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

    void process(String line) {
        System.out.println("--" + line + "--");
        target.add(line);
        // send a mail line == email-id.
    }

    // Start of candidate editable code //

    /**
     * if first worker
     * process from beginning
     * doProcess
     * other worker
     * boolean nextLineExceedBoundary = process from next new line
     * if (nextLineExceedBoundary) {
     * return;
     * }
     * doProcess
     * <p>
     * edge case
     * 1. when boundary is \n? let next worker process.
     * <p>
     * Hello\nHow are you doing?\nI am good.\n Thank you \n
     * |  |      |      |        |        |        |      ..remaining
     * 0  1      2      3        4        5        6
     * <p>
     * 0: Hello
     * 1: How are you doing?
     * 2: null
     * 3: I am good
     * 4: null
     * 5: Thank you
     * 6: null
     * <p>
     * <p>
     * Hello\nHow are you doing?\nI am good.\n Thank you \n
     * |                             |
     * 0                            1
     * <p>
     * 0: Hello  How are you doing? I am good.
     * 1:  Thank you
     * <p>
     * void doProcess() {
     * <p>
     * }
     */
    void worker(int fileOffset, int length) {
        // Case 1: fileContents = "Hello"; // no new line, because usually they forget for last line.
        // Case 2: fileContents = "Hello\nWorld";
        //
        // Hello\nHow are you doing?\n
        // Hello\nHow are you d
        //

        System.out.println(String.format("===> %d %d %s", fileOffset, Math.min(Length(), fileOffset + length),
                contents.substring(fileOffset, Math.min(Length(), fileOffset + length))).replace("\n", "\\n"));

        int limit = fileOffset + length;
        if (fileOffset != 0) {
            fileOffset = findNextNewLineOffset(fileOffset, fileOffset + length);
            if (fileOffset == -1) {
                return;
            }
            fileOffset++;
        }

        List<String> mails = new ArrayList<>();

        // find within limit
        int maxLength = Math.min(Length(), limit);
        StringBuilder str = new StringBuilder();
        while (fileOffset < maxLength) {
            int fetchSize = Math.min(BATCH_SIZE, maxLength - fileOffset);
            String temp = Range(fileOffset, fetchSize);
            for (int i = 0; i < temp.length(); i++) {
                if (temp.charAt(i) == '\n') {
                    mails.add(str.toString());
                    str.setLength(0);
                } else {
                    str.append(temp.charAt(i));
                }
            }
            fileOffset += fetchSize;
        }

        // try extend
        int nextNewLineOffset = findNextNewLineOffset(fileOffset, Length());
        String temp;
        if (nextNewLineOffset == -1) {
            temp = Range(fileOffset, Length() - fileOffset);
        } else {
            temp = Range(fileOffset, nextNewLineOffset - fileOffset);
        }
        str.append(temp);
        mails.add(str.toString());

        for (String email : mails) {
            if (email.length() == 0) {
                continue;
            }
            process(email);
        }
    }

    /**
     * Return position of next new line
     */
    private int findNextNewLineOffset(int fileOffset, int limit) {
        int maxLength = Length();
        int offset = fileOffset;
        while (offset < maxLength) {
            int fetchSize = Math.min(BATCH_SIZE, maxLength - offset);
            String temp = Range(offset, fetchSize);
            int newLineIndex = temp.indexOf("\n");
            if (offset + newLineIndex >= limit) {
                return -1;
            }
            if (newLineIndex >= 0) {
                return offset + newLineIndex;
            }
            offset += fetchSize;
        }
        return -1;
    }

    public int BATCH_SIZE = 50;

    void master() {
        int length = Length();
        int eachWorkerLength = Length() / numOfWorkers;
        if (eachWorkerLength == 0) {
            throw new IllegalArgumentException("too many workers");
        }
        for (int i = 0; i < numOfWorkers; i++) {
            int offset = i * eachWorkerLength;
            worker(offset, eachWorkerLength);
        }
        // the last remaining
        if (length % numOfWorkers != 0) {
            worker(numOfWorkers * eachWorkerLength, Length() - numOfWorkers * eachWorkerLength);
        }
    }

    private int numOfWorkers;

    public static void main(String[] args) {
        // * interviewer edit */
        SendEmails2 xu = new SendEmails2();
        xu.numOfWorkers = 4;
        xu.master();
        // * interviewer edit ends */
    }


    // unit test

    private static final List<String> target = new ArrayList<>();

    @org.junit.Test
    public void test() {
        for (int i = 1; i < 50; i++) {
            for (int j = 1; j < 40; j++) {
                System.out.println("=========== batch size = " + i + ", workers = " + j);
                BATCH_SIZE = i;
                target.clear();
                SendEmails2 xu = new SendEmails2();
                xu.numOfWorkers = j;
                xu.master();
                Assert.assertEquals(4, target.size());
                Assert.assertEquals("Hello", target.get(0));
                Assert.assertEquals("How are you doing?", target.get(1));
                Assert.assertEquals("I am good.", target.get(2));
                Assert.assertEquals(" Thank you", target.get(3));
            }
        }

    }

}
