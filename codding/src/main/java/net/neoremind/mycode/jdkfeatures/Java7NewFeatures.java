package net.neoremind.mycode.jdkfeatures;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

/**
 * As with every new Java version, Java 7 introduced some new features. The new features are definitely worth the
 * change and as developers, we must be happy as we can be more efficient. There are many features introduced in Java
 * 7 but we will look at some of them.
 * <ul>
 * <li>1. Improved Type reference for Generic Instance creation</li>
 * <li>2. Allows Strings to be used in ‘case’ switch statements.</li>
 * <li>3. Try with resources and custom Autocloseable Class</li>
 * <li>4. Using underscore characters in numeric values.</li>
 * <li>5. Multi-catch Exceptions</li>
 * <li>6. Binary literals</li>
 * <li>7. Safe Varargs</li>
 * <li>8. New file system API (NIO 2.0)</li>
 * <li>9. Fork and Join</li>
 * </ul>
 * <p/>
 * See more at:http://www.javabeat.net/java-7-new-features/#sthash.215LhoXp.dpuf
 * <p/>
 * http://stackoverflow.com/questions/213958/new-features-in-java-7
 * <p/>
 * http://radar.oreilly.com/2011/09/java7-features.html
 *
 * @author zhangxu
 */
public class Java7NewFeatures {

    /**
     * we can safely ignore the right hand type, as Java will automatically infer the type from the left hand side. -
     * See more at: http://www.javabeat.net/java-7-new-features/#sthash.215LhoXp.dpuf
     */
    @Test
    public void testDiamondOperator() {
        List<String> list = new ArrayList<>();
        list.add("123");
        list.add("abc");
        System.out.println(list);

        Map<String, List<String>> map = new TreeMap<>();

        List<? extends String> list2 = new ArrayList<>();
        System.out.println(list2);

        // list.addAll(new ArrayList<>());  // no
    }

    /**
     * Java 7 officially supports using Strings in switch case statements
     */
    @Test
    public void testStringInSwitchStatement() {
        String name = "Addo Quaye";
        switch (name) {
            case "Addo Quaye":
                System.out.println("He is the SRC president");
                break;
            case "Boateng Nicolas":
                System.out.println("He is the SU president");
                break;
            default:
                System.out.println("He is no body!");
                break;
        }
    }

    /**
     * Automatic resource management aka Try with resources. That is a kind of code simplification.
     * <p/>
     * A resource is as an object that must be closed after the program is finished with it. The try-with-resources
     * statement ensures that each resource is closed at the end of the statement. Any object that implements java
     * .lang.AutoCloseable, which includes all objects which implement java.io.Closeable, can be used as a resource.
     *
     * @see AutoCloseable
     */
    @Test
    public void testTryWithResources() throws IOException {
        // old way, manually free up the resource inside the finally block.
        /*
        Prior to Java SE 7, you can use a finally block to ensure that a resource is closed regardless of whether the
         try statement completes normally or abruptly.
         */
        String filename = "src/main/resources/log4j.properties";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));
            String lines;
            while ((lines = br.readLine()) != null) {
                System.out.println(lines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // new way, new syntax
        /*
        Because the BufferedReader instance is declared in a try-with-resource statement, it will be closed
        regardless of whether the try statement completes normally or abruptly (as a result of the method
        BufferedReader.readLine throwing an IOException).
         */
        try (BufferedReader buff = new BufferedReader(
                new FileReader(filename))) {
            String lines;
            while ((lines = buff.readLine()) != null) {
                System.out.println(lines);
            }
        }
    }

    @Test
    public void testUnderscoreCharacterInNumericValues() {
        int million = 1_000_000;
        double amount = 60.50_40_30;
        System.out.println(million);
        System.out.println(amount);
    }

    /**
     * we can handle multiple exceptions as part of the same catch block. It basically does the OR condition check
     * and we can have as many exceptions as need and handle it the same way.
     */
    @Test(expected = NumberFormatException.class)
    public void testMultipleExceptionInASingleCatchBlock() {
        try {
            Integer.parseInt("xx");
            String x = null;
            x.charAt(0);
        } catch (NullPointerException | NumberFormatException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * From Java 7, (byte,short,int,long) can also be expressed in binary number system. We just need to add a prefix
     * “0b” or “OB” before the number.
     */
    @Test
    public void testBinaryLiterals() {
        int binInt1 = 0b100;
        System.out.println(binInt1);
        Long binLong = 0B100000000000L;
        System.out.println(binLong);
    }

    /**
     * Improved Compiler Warnings and Errors When Using Non-Reifiable Formal Parameters with Varargs Methods
     *
     * @param lists
     */
    @SuppressWarnings({"unchecked", "varargs"})
    public static void printAll(List<String>... lists) {
        for (List<String> list : lists) {
            System.out.println(list);
        }
    }

    @SafeVarargs
    public static void printAll2(List<String>... lists) {
        for (List<String> list : lists) {
            System.out.println(list);
        }
    }

    /**
     * New file system API (NIO 2.0)
     */
    @Test
    public void testFileIO() throws IOException, InterruptedException {
        Path path = Paths.get("src/main/resources/log4j.properties");
        System.out.println("Number of Nodes:" + path.getNameCount());
        System.out.println("File Name:" + path.getFileName());
        System.out.println("File Root:" + path.getRoot());
        System.out.println("File Parent:" + path.getParent());

        // Files.delete(path);
        // Files.deleteIfExists(path);

        WatchService watchService = FileSystems.getDefault().newWatchService();
        Paths.get("/Users/baidu/work").register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);
        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                System.out.println(event.context() + "发生了" + event.kind() + "事件");
            }
            if (!key.reset()) {
                break;
            }
        }
    }

    @Test
    public void testForkAndJoin() {

    }

}
