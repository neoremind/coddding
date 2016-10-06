package net.neoremind.mycode.argorithm.hackerrank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * @author zhangxu
 */
public class HostPaginate {

    String[] paginate(int num, String[] results) {
        if (num < 1) {
            throw new IllegalArgumentException();
        }
        if (results == null || results.length == 0) {
            return new String[] {""};
        }
        List<Host> hosts = Arrays.stream(results).map(r -> new Host(r)).collect(Collectors.toList());
        HostPool hostPool = new HostPool(hosts);

        Host dummy = new Host("0,D,D,D");
        List<Host> res = new ArrayList<>(results.length);

        int pageNum = getPages(results.length, num);
        Set<Integer> hostIdSet = new HashSet<>(5);
        int count = 0;
        for (int i = 0; i < pageNum; i++) {
            List<Host> singlePageListing = new ArrayList<>(num);
            while (count < results.length && singlePageListing.size() < num) {
                Host host = hostPool.get();
                if (!hostPool.exhausted() && hostIdSet.contains(host.getHostId())) {
                    hostPool.returnBack(host);
                    continue;
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

    int getPages(int resultSize, int num) {
        return (int) Math.ceil((double) resultSize / num);
    }

    class Dummy extends Host {
        public Dummy(String result) {
            super(result);
        }

        public String getContent() {
            return "";
        }
    }

    class Host {

        private int hostId;

        private String content;

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
        System.out.println(Arrays.toString(res));
    }

}
