package net.neoremind.mycode;

// You have a server that hosts a long file, for example:
// it could a file containg email-ids, separated by new line.
// process - sending email to each of email-ids.

public class Test {

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
        // send a mail line == email-id.
    }

    // Start of candidate editable code //

    void worker(int fileOffset, int length) {
        System.out.println("**************");
        System.out.println(" ==> " + fileOffset + " " + length + " " + Range(fileOffset, length));
        System.out.println("**************");
        // Case 1: fileContents = "Hello"; // no new line, because usually they forget for last line.
        // Case 2: fileContents = "Hello\nWorld";
        //
        // Hello\nHow are you doing?\n
        // Hello\nHow are you d
        //

        if (fileOffset != 0) {
            fileOffset = findNonOverlapFileOffset(fileOffset);
        }
        if (fileOffset == -1) {
            return;
        }

        int maxLength = Length();
        length = Math.min(length, maxLength - fileOffset);
        String content = Range(fileOffset, length);
        StringBuilder str = new StringBuilder(content);

        int step = 100;
        int startOffset = fileOffset + length;
        int newLineIndex = 0;

        // * interviewer note */
        // shouldn't this be startOffset < maxLength ?
        // * interviewer note ends */
        // the largest startOffset should maxLength - 1
        while (startOffset < maxLength) {
            step = Math.min(step, maxLength - startOffset);
            String temp = Range(startOffset, step);
            newLineIndex = temp.indexOf("\n");
            if (newLineIndex > 0) {
                str.append(temp.substring(0, newLineIndex + 1));
                break;
            } else {
                str.append(temp);
            }
            startOffset += step;
        }

        String[] emails = str.toString().split("\n");
        for (String email: emails) {
            process(email);
            // handle
        }
    }

    private int findNonOverlapFileOffset(int fileOffset) {
        int maxLength = Length();

        int step = 100;
        int startOffset = fileOffset;
        int newLineIndex = 0;

        // * interviewer note */
        // shouldn't this be startOffset < maxLength ?
        // * interviewer note ends */
        // the largest startOffset should maxLength - 1
        while (startOffset < maxLength) {
            step = Math.min(step, maxLength - startOffset);
            String temp = Range(startOffset, step);
            newLineIndex = temp.indexOf("\n");
            if (newLineIndex > 0) {
                return startOffset + newLineIndex;
            }
            startOffset += step;
        }
        return -1;
    }

    void master() {
        int length = Length();
        int eachWorkerLength = length / numOfWorkers;
        // edge case here.
        for (int i = 0; i < numOfWorkers; i++) {
            int offset = i * eachWorkerLength;
            worker(offset, eachWorkerLength);
        }
    }

    private int numOfWorkers;

    public static void main(String[] args) {
        // * interviewer edit */
        Test xu = new Test();
        xu.numOfWorkers = 4;
        xu.master();
        // * interviewer edit ends */
    }
}

