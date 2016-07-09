package net.neoremind.mycode.bytecode.bytebuddy;

import java.util.Arrays;
import java.util.List;

/**
 * @author zhangxu
 */
public class MemoryDatabase {

    public List<String> load(String info) {
        return Arrays.asList(info + ": foo", info + ": bar");
    }

}
