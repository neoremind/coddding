package net.neoremind.mycode;

import com.google.common.collect.Lists;

import org.apache.commons.io.FileUtils;
import org.xerial.snappy.BitShuffle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class HelloWorld {

//    static String  addr  = "\"am-cdnbackup cdn智能调度备库，业务接口人：斯敬\", \"am-xngfwflow 云防火墙产品开发项目，主要是流量图和流量分析的场景, 每天预计会写入100亿行的数据, 需要存30天, 支持毫秒级别的查询，业务联系人: 希纯\", \"am-uf63edbsc6t8809i6\", \"am-guxiaoadb20190717\", \"am-forcontent 业务接口人：王子恺(倦尘)\", \"am-xlifeeryf1\", \"am-xlifeeryf0\", \"am-lazadagamecenter01\", \"am-youku_olap\", \"am-msc 铜牛\", \"am-ads_rex4s\", \"am-uf616nsr583fq9630\", \"am-marketingplatform\", \"am-uf64j6rl733356883 贪玩\", \"am-uf6558827t3o332fv\", \"am-auge 奥格\", \"am-uf6d65qme7549wxbk 八维通 新购\", \"am-uf6ys1a11y5k16019\", \"am-uf6v3t857mxgiw18t\", \"am-uf650x5goggp85e9a\", \"am-uf62d6p1vyt85u558\", \"am-uf6k4cf19elqkrd10\", \"am-uf63958r788d102sx 康众大数据\", \"am-uf60o1hs800e443vh\", \"am-uf6zex4s706zd90d2\", \"am-uf6oxhm5lr9gdqc66\", \"am-uf64x49qtc29905qe\", \"am-uf61wzg12012bs6ab\", \"am-uf68iu788h6a643dz\", \"am-uf643400m38c67q7t\", \"am-1hh300vv3uh643c48\", \"am-uf66b42t9d2oi6604\", \"am-uf69t85x1gh8632q5\", \"am-uf61b7yb2qsx6ny87\", \"am-alidns\", \"am-uf6vwp82r938yic82\", \"am-uf6g83t57bmx67v80\", \"am-uf6iv85k11hqa28u3\", \"am-cdncm67436603 @照捷 @赤芒 @木俩\", \"am-uf68yl9jz9o066f50\", \"am-uf692n1va27oz65kr 谊品弘科技\", \"am-1hh79m5d69hc5b98i\", \"am-uf6li1r3do8m49rpq\", \"am-1hhom8kufgw5aibls\", \"am-uf64v4o9yq19zxj53\", \"am-rediseyelog\", \"am-uf61c84q4vyi763e9\", \"am-uf616t91sz2vx96ui\", \"am-uf69s7a14idbibtfe\", \"am-1hhq45r1b70rgw8ys\", \"am-uf6386e9btajmaodx\", \"am-uf6d5532j73e66o4z\", \"am-uf6bz4wan2phx6037\", \"am-s16ins001 淘宝联盟-大存储OSS-32节点-步癫\", \"am-uf6b6i8l3v6h6804j\", \"am-uf6v0zw6l5p8ty668\", \"am-uf6xo2zi8l0n188rk\", \"am-uf670371385i8kmmp\", \"am-uf67joh0jkjz23jml\", \"am-uf61uy1ge096x56z6\", \"am-uf6y1s96459r66001 上海龙邦速运有限公司C32\", \"am-uf60b22v059du182u\", \"am-uf614kd44pfl27xd5\", \"am-s16forgaode003 高德大存储\", \"am-uf61pugfc90e222qe\", \"am-uf68fy95q55z42o4j\", \"am-uf623g994kf34uyl3\", \"am-uf6f4gr0260jhx184\", \"am-uf60023z1rz1y477n\", \"am-uf6wz9c2y17f4039c\", \"am-uf67r958g9wf24zed\", \"am-uf6eros1fghc218ka\", \"am-uf6s72tbqeh4fmig9 供应链同城零售-作战指挥室\", \"am-uf61fmj01h7d6unp1\", \"am-uf639tc12sf27j83x\", \"am-uf6au22lr430d8m88\", \"am-uf6yv1y82s6607014\", \"am-uf6gw0457af88d228\", \"am-uf673qinmklwjgr00\", \"am-uf61927j69c5z4172\", \"am-uf601p31zbj8bop6g\", \"am-uf6jz16904b2nhe39\", \"am-uf6ke0cj4n9w17q55\", \"am-uf6653q4tf64co4dq\", \"am-uf6d3k083s4218c9a\", \"am-uf68q1rcolm9m42ub\", \"am-uf6e591lz1d89g7xo\", \"am-uf6ewx9lac96ct134\", \"am-uf63t0u69upr6882h\", \"am-uf6988t87957n4m2t\", \"am-uf605pj0i3uvthza4\", \"am-1hh2m58926u08m3t4\", \"am-uf6nx6qy29929ug66\", \"am-uf6zm5vm4q04e8xrj\", \"am-uf62i4iuu840y92oj\", \"am-uf66wspfmt9w70o73\", \"am-uf67243w3r4a16bs0\", \"am-uf60wuw67gvzownbi\", \"am-uf6ks0g6t1e7ba390\", \"am-uf61ud7ud66715uq8\", \"am-uf6pm37v23fspx68d\", \"am-uf6m176506al1pcp2 昌投网络\", \"am-uf65cl076es608o65\", \"am-uf600f48o3i152ces\", \"am-uf6xt8wijck660tq1\", \"am-uf6au11pxuwh61lq4\", \"am-uf69i6n5lc8m9cpbg\", \"am-uf6hpwru05cx4627q\", \"am-uf6jy2x0ajws279hk\", \"am-uf6o0k4g348064pcf\", \"am-uf61o5sk551nkx962\", \"am-uf6v79d5656tcjwz0\", \"am-uf6v6ro0t5c2012rc\", \"am-uf6ylw548h07276ci\", \"am-uf6uvnbkawxsy13t3\", \"am-uf6c83krnpdvz5r6m\", \"am-uf64vz364yg29500c\", \"am-uf63160616co6819n\", \"am-uf6418f77qo4p84a4\", \"am-uf6e63d8232yc8yj5\", \"am-uf695472soc90xr50\", \"am-uf64p7shaq091pswm\", \"am-uf6qf2i44uh280014\", \"am-uf6754x04j82x9209\", \"am-uf6t9hiv0z2pt71k3\", \"am-uf6b3y46i2gw9e1b0\", \"am-uf6pyasp8d0tgx84f\", \"am-uf6s78i8q7w5p8304\", \"am-uf6l442o322l1unm8\""；


    public static void main(String[] args) throws IOException {

        List<String> lines = FileUtils.readLines(new File("/Users/xu/Desktop/failed.addr"));

        for (String line : lines) {
            String[] ss = line.split(",");
            for (String s : ss) {
                System.out.println(s.replace(" ", "").replace("\"", ""));
            }
//            System.out.println(line);
        }

        Pattern pattern = Pattern.compile(".*", Pattern.DOTALL);
        String ddd = "\n123";
        System.out.println(pattern.matcher("ab").matches());
        System.out.println(pattern.matcher("").matches());
        System.out.println(pattern.matcher("\n123").matches());

        List<byte[]> blocks = new ArrayList<>();
        blocks.add(null);
        blocks.add(new byte[1]);
        for (byte[] block : blocks) {
            System.out.println(block.length);
        }
        System.out.println(1 << 20);
        for (int i = 0; i < 640; i++) {
            System.out.println("_mm256_stream_si256( dst + " + i + ",     ((volatile const __m256i *)src)[" + i + "] );");
        }
//        byte a = (byte)0xFF;
//        byte a2 = (byte)0x01;
//        int b = -10;
//        int c = a + b;
//        System.out.println((int) a2);
//        int c2 = a2 + b;
//
//        a += 1;
//        System.out.println(c);
//        System.out.println(c2);
//        System.out.println(a);

        byte a = -128;
        int b = 1;
        int c = a + b;
        System.out.println(c); // -127

        byte a2 = 127;
        a2 += 1;
        System.out.println(a2); // 溢出了，0x7F + 1 = 0xFF = -128

        int x = 0x90; // java这种字面常亮，不遵循符号位扩展原则，高位全部补0
        System.out.println(x);
        System.out.println("符号位扩展后提升为int，高位全部补充了1，而右边的int高位是0，自然不相等，(byte)0x90 == 0x90:" + ((byte)0x90 == x)); // false
        System.out.println("正确的做法:" + (((byte)0x90 & 0xFF) == x)); // true

//
//        int[] data = new int[] {1, 3, 34, 43, 34};
//        byte[] shuffledData = BitShuffle.bitShuffle(data);
//        int[] result = BitShuffle.bitUnShuffleIntArray(shuffledData);

        System.out.println("d");
        List<Integer> list = Lists.newArrayList(0, 1, 2, 3, 4, 5);
        List<Integer> list1 = Lists.newArrayList();
        for (int i = 3; i < list.size(); i++) {
            list1.add(list.get(i));
        }
        System.out.println(list1);
        List<Integer> list2 = Lists.newArrayList();
        list2.addAll(list.subList(3, list.size()));
        System.out.println(list2);

        System.out.println("hello world");
        System.out.println(1 << 31);

        Random ran = new Random(100);
        for (int i = 0; i < 10; i++) {
            System.out.println(Math.abs(ran.nextInt()) % 100);
        }

        int start = 1;
        int end = 8;
        System.out.println((start + end) / 2);
        System.out.println((start + end) >>> 1);
        System.out.println((start + end) << 1);

        System.out.println((Integer.MAX_VALUE + 1) & (1 << 31)); //-2147483648
        System.out.println((Integer.MAX_VALUE) & (1 << 31)); //0
        System.out.println((Integer.MIN_VALUE >> 31) & 1); //1
        System.out.println((Integer.MIN_VALUE >> 32) & 1); //0
        System.out.println((Integer.MIN_VALUE >>> 31) & 1); //1
        System.out.println((Integer.MIN_VALUE >>> 32) & 1); //0
    }

}
