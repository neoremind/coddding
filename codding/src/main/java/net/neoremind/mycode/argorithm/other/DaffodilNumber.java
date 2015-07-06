package net.neoremind.mycode.argorithm.other;

/**
 * ClassName: DaffodilNumber <br/>
 * Function: 求水仙花数。
 * <p/>
 * The daffodil number is one of the famous interesting numbers in the mathematical world. A daffodil number is a
 * three-digit number whose value is equal to the sum of cubes of each digit. For example. 153 is a daffodil as 153 =
 * pow(1,3) + pow(5,3) + pow(3,3).
 * 
 * @author Zhang Xu
 */
public class DaffodilNumber {

    public static void main(String[] args) {
        for (int i = 100; i < 1000; i++) {
            if (isDaffodilNumber(i)) {
                System.out.println(i);
            }
        }
    }

    private static boolean isDaffodilNumber(int num) {
        if (num > 999 || num < 100) {
            return false;
        }
        int a = num / 100;
        int b = (num / 10) % 10;
        int c = num % 10;
        if (num == (Math.pow(a, 3) + Math.pow(b, 3) + Math.pow(c, 3))) {
            return true;
        }
        return false;
    }

}
