package net.neoremind.mycode.argorithm.other;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * http://stackoverflow.com/questions/5020317/in-java-given-an-ip-address-range-return-the-minimum-list-of-cidr
 * -blocks-that
 *
 * http://stackoverflow.com/questions/7148991/cidr-address-selection
 */
public class IpRange2CIDR {

    @Test
    public void test() {
        range2cidrlist("1.1.1.111", "1.1.1.120").forEach(System.out::println);
        System.out.println("========");
        range2cidrlist("1.1.1.56", "1.1.1.245").forEach(System.out::println);
        System.out.println("========");
        range2cidrlist2("1.1.1.111", "1.1.1.120").forEach(System.out::println);
        System.out.println("========");
        range2cidrlist2("1.1.1.56", "1.1.1.245").forEach(System.out::println);
    }

    public static List<String> range2cidrlist(String startIp, String endIp) {
        long start = ipToLong(startIp);
        long end = ipToLong(endIp);

        ArrayList<String> pairs = new ArrayList<>();
        while (end >= start) {
            byte maxsize = 32;
            while (maxsize > 0) {
                long mask = CIDR2MASK[maxsize - 1];
                long maskedBase = start & mask;

                if (maskedBase != start) {
                    break;
                }

                maxsize--;
            }
            double x = Math.log(end - start + 1) / Math.log(2);
            byte maxdiff = (byte) (32 - Math.floor(x));
            if (maxsize < maxdiff) {
                maxsize = maxdiff;
            }
            String ip = longToIP(start);
            pairs.add(ip + "/" + maxsize);
            start += Math.pow(2, (32 - maxsize));
        }
        return pairs;
    }

    /**
     * 思路很简单，
     * <p>
     * 1）首先把IP段转成long的区间，例如[1.1.1.111 -> 1.1.1.120]变成了[16843119, 16843128]
     * <p>
     * 2）把区间段看做[start, end]，从start开始，依次要累加他能reach到的最大的一个整区间，也就是尾巴全是1的一段。方法如下：
     * <p>
     * 2.1）找到start的最右边的一个bit，类似BIT数据结构的算法，start & -start即使，最右边的那个1的值，比如100.
     * 那么后缀就是32-2（2个0）=30.
     * <p>
     * 2.2）计算end-start+1剩下的这个区间段里有多少数字，是不是上面的那个区间段可以cover的是这个的子集，如果是
     * 那么就把start和上面的那个后缀组成一个结果加进去。
     * <p>
     * 如果剩下的很多，而上面2.1步骤那里面的很少，那么证明不好意思，到头了，需要减下，使用Long.highestOneBit(leftCount)这个方法，
     * 找最近的2的幂。
     */
    public static List<String> range2cidrlist2(String startIp, String endIp) {
        long start = ipToLong(startIp);
        long end = ipToLong(endIp);

        ArrayList<String> pairs = new ArrayList<>();
        while (end >= start) {
            byte maxsize = 32;
            long rightMostOneBit = start & -start;
            long tempRightMostOneBit = start & -start;
            while (tempRightMostOneBit != 1) {
                maxsize--;
                tempRightMostOneBit >>>= 1;
            }
            long leftCount = end - start + 1;
            if (leftCount < rightMostOneBit) {
                long nearest2N = Long.highestOneBit(leftCount);
                byte thisTimeMaxSize = 32;
                while (nearest2N != 1) {
                    thisTimeMaxSize--;
                    nearest2N >>>= 1;
                }
                maxsize = thisTimeMaxSize;
            }
            String ip = longToIP(start);
            pairs.add(ip + "/" + maxsize);
            start += Math.pow(2, (32 - maxsize));
        }
        return pairs;
    }

    public static final int[] CIDR2MASK = new int[] {0x00000000, 0x80000000,
            0xC0000000, 0xE0000000, 0xF0000000, 0xF8000000, 0xFC000000,
            0xFE000000, 0xFF000000, 0xFF800000, 0xFFC00000, 0xFFE00000,
            0xFFF00000, 0xFFF80000, 0xFFFC0000, 0xFFFE0000, 0xFFFF0000,
            0xFFFF8000, 0xFFFFC000, 0xFFFFE000, 0xFFFFF000, 0xFFFFF800,
            0xFFFFFC00, 0xFFFFFE00, 0xFFFFFF00, 0xFFFFFF80, 0xFFFFFFC0,
            0xFFFFFFE0, 0xFFFFFFF0, 0xFFFFFFF8, 0xFFFFFFFC, 0xFFFFFFFE,
            0xFFFFFFFF};

    private static long ipToLong(String strIP) {
        long[] ip = new long[4];
        String[] ipSec = strIP.split("\\.");
        for (int k = 0; k < 4; k++) {
            ip[k] = Long.valueOf(ipSec[k]);
        }

        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    private static String longToIP(long longIP) {
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf(longIP >>> 24));
        sb.append(".");
        sb.append(String.valueOf((longIP & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((longIP & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf(longIP & 0x000000FF));

        return sb.toString();
    }
}

