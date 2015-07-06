package net.neoremind.mycode.argorithm.other;

/**
 * ClassName: PrintNum <br/>
 * Function: 尝试用递归打印一个数字从高位到低位 <br/>
 * 这里注意base case是num >= 10，而making progress的情形是需要<B>不断的逼近base case</B>，因此才可以退出
 * 
 * @author Zhang Xu
 */
public class PrintNumberDigit {

    public static void main(String[] args) {
        int num = 76843;
        printOut(num);
    }

    /**
     * 递归打印 运行栈如下：
     * 
     * <pre>
     * 
     * printOut(7); // 执行到此直接打印退出
     * printOut(76); // 取模打印最后一个数字
     * printOut(768);
     * printOut(7684);
     * printOut(76843);
     * 
     * <pre/>
     * 
     * @param num
     */
    private static void printOut(int num) {
        if (num >= 10) {
            printOut(num / 10);
        }
        System.out.println(num % 10);
    }

}
