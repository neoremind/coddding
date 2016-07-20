package net.neoremind.mycode.guava.base;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.google.common.base.Enums;

/**
 * @author zhangxu
 */
public class EnumsTest {

    /**
     * 模板元素使用类型
     * <ul>
     * <li>0:使用了产品库变量</li>
     * <li>1:用户纯文字</li>
     * <li>2:用户图片</li>
     * <li>3:图文小图标</li>
     * </ul>
     */
    private enum ElementUseType {
        WILDCARD(0), TEXT(1), IMAGE(2), TUWEN_ICON(3);

        private ElementUseType(final int useType) {
            this.useType = useType;
        }

        private int useType;

        public int getUseType() {
            return useType;
        }

        public void setUseType(int useType) {
            this.useType = useType;
        }
    }

    @Test
    public void testGetIfPresent() {
        assertThat(Enums.getIfPresent(ElementUseType.class, "WILDCARD").isPresent(), is(true));
        assertThat(Enums.getIfPresent(ElementUseType.class, "XXX").isPresent(), is(false));
    }

}
