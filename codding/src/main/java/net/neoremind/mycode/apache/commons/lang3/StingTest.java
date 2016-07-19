package net.neoremind.mycode.apache.commons.lang3;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.junit.Test;

/**
 * 1）缩短字符
 * 2）打印heading
 * 3）折行
 * <p>
 * 自己开发类库时候非常实用
 *
 * @author zhangxu
 */
public class StingTest {

    @Test
    public void testAbbreviate() {
        String str = "abcedfghijklmn";
        System.out.println(StringUtils.abbreviate(str, 5));
    }

    @Test
    public void testRepeatAndCenter() {
        String stars = StringUtils.repeat("*", 20);
        String centered = StringUtils.center(" hello ", 20, "*");
        String heading = StringUtils.join(new Object[] {stars, centered, stars}, "\n");
        System.out.println(heading);
    }

    @Test
    public void testWrap() {
        String str = "abcedfghijklmn";
        System.out.println(WordUtils.wrap(str, 10, "\n", true));
    }

}
