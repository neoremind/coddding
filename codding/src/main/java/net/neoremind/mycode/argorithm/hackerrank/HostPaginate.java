package net.neoremind.mycode.argorithm.hackerrank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Paginate test
 * <p>
 * Confidential
 *
 * @author zhangxu
 */
public class HostPaginate {

    /**
     * The method to paginate results
     *
     * @param num     items per page
     * @param results results list
     *
     * @return results by page
     */
    String[] paginate(int num, String[] results) {
        if (num < 1) {
            throw new IllegalArgumentException();
        }
        if (results == null || results.length == 0) {
            return new String[] {""};
        }
        List<Host> hosts = Arrays.stream(results).map(r -> new Host(r)).collect(Collectors.toList());
        HostPool hostPool = new HostPool(hosts);

        Dummy dummy = new Dummy("0,D,D,D");
        List<Host> res = new ArrayList<>(results.length);

        int pageNum = getPages(results.length, num);
        Set<Integer> hostIdSet = new HashSet<>(5);
        int count = 0;
        for (int i = 0; i < pageNum; i++) {
            List<Host> singlePageListing = new ArrayList<>(num);
            boolean shouldPad = false;
            while (count < results.length && singlePageListing.size() < num) {
                Host host = hostPool.get();
                if (!shouldPad && hostIdSet.contains(host.getHostId())) {
                    hostPool.returnBack(host);
                    if (!hostPool.exhausted()) {
                        continue;
                    } else {
                        shouldPad = true;
                        hostPool.revert();
                        host = hostPool.get();
                    }
                }
                singlePageListing.add(host);
                hostIdSet.add(host.getHostId());
                count++;
            }
            singlePageListing.add(dummy);
            res.addAll(singlePageListing);
            hostIdSet.clear();
            hostPool.revert();
        }

        return res.stream().map(h -> h.getContent()).toArray(String[]::new);
    }

    /**
     * The host pool used by the client to get host out and return host back when duplication occurs.
     * <p>
     * First construct the <code>HostPool</code> by the given host list, the class maintains a {@link LinkedList}
     * data structured field called <tt>mainList</tt> to store all the list. Then call <code>get()</code> to get host
     * one by one from the pool, when duplication occurs in this `batch` which is also known as the current page, the
     * client should return the host back to the pool for later use. When returning the specific host back,
     * <code>HostPool</code> leverage another {@link LinkedList} called <tt>backupList</tt> also can be seen as an
     * FIFO queue to store the duplicated hosts during this batch. When client used up the hosts from the
     * <tt>mainList</tt> which can be checking by calling <code>exhausted</code> method, then client should call
     * <code>revert</code> method to poll each elements from <tt>backupList</tt> and offer back to the
     * <tt>mainList</tt>.
     * <p>
     * An code template looks like below,
     * <pre>
     *     while (condition) {
     *         Host host = hostPool.get();
     *         if (duplication occurs or some other criteria meets) {
     *             hostPool.returnBack(host);
     *             if (!hostPool.exhausted()) {
     *                 continue;
     *             } else {
     *                 hostPool.revert();
     *                 host = hostPool.get();
     *             }
     *         }
     *         process host
     *         mark the host has been processed during this batch
     *     }
     * </pre>
     */
    class HostPool {

        LinkedList<Host> mainList = new LinkedList<>();

        LinkedList<Host> backupList = new LinkedList<>();

        public HostPool(List<Host> mainList) {
            this.mainList.addAll(mainList);
        }

        public boolean exhausted() {
            return mainList.isEmpty();
        }

        public Host get() {
            if (mainList.isEmpty()) {
                return backupList.pollFirst();
            }
            return mainList.pollFirst();
        }

        public void returnBack(Host host) {
            backupList.offer(host);
        }

        public void revert() {
            while (!backupList.isEmpty()) {
                mainList.addFirst(backupList.pollLast());
            }
        }
    }

    /**
     * Returns the page number according to the total result size and numbers of items per page
     *
     * @param resultSize result size
     * @param num        numbers of items per page
     *
     * @return page size
     */
    int getPages(int resultSize, int num) {
        return (int) Math.ceil((double) resultSize / num);
    }

    /**
     * The dummy class for generating empty string after each page
     */
    class Dummy extends Host {
        public Dummy(String result) {
            super(result);
        }

        @Override
        public String getContent() {
            return "";
        }
    }

    /**
     * The host object from the input string.
     * <p>
     * Note: Rather than extracting all fields from the string,
     * only need to pay attention to the host id which is used for distinction purpose,
     * so the left is stored at <code>content</code> originally.
     */
    class Host {

        /**
         * Host id
         */
        private int hostId;

        /**
         * Full content, like 1,2,300,A
         */
        private String content;

        /**
         * Default constructor
         *
         * @param result input string
         */
        public Host(String result) {
            if (result == null || result.length() == 0) {
                throw new IllegalArgumentException();
            }
            String[] fields = result.split(",");
            if (fields.length != 4) {
                throw new IllegalArgumentException();
            }
            this.hostId = Integer.parseInt(fields[0]);
            this.content = result;
        }

        @Override
        public String toString() {
            return "Host{" +
                    "hostId=" + hostId +
                    ", content='" + content + '\'' +
                    '}';
        }

        public int getHostId() {
            return hostId;
        }

        public void setHostId(int hostId) {
            this.hostId = hostId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    @Test
    public void test() {
        String[] results = new String[] {
                "1,2,300,A",
                "4,2,300,B",
                "20,2,300,C",
                "6,2,300,D",
                "6,2,300,E",
                "1,2,300,F",
                "6,2,300,G",
                "7,2,300,H",
                "6,2,300,I",
                "2,2,300,M",
                "2,2,300,N",
                "3,2,300,X",
                "2,2,300,Y",
        };
        String[] res = paginate(5, results);
        Arrays.stream(res).forEach(System.out::println);

        results = new String[] {
                "1,2,300,A",
                "4,2,300,B",
                "4,2,300,C",
                "4,2,300,D",
                "4,2,300,E",
                "4,2,300,F",
                "4,2,300,G",
                "7,2,300,H",
                "6,2,300,I",
                "2,2,300,M",
                "2,2,300,N",
                "3,2,300,X",
                "2,2,300,Y",
        };
        res = paginate(5, results);
        Arrays.stream(res).forEach(System.out::println);
    }

}
