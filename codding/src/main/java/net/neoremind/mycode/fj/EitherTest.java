package net.neoremind.mycode.fj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fj.P1;
import fj.data.Either;

/**
 * http://www.ibm.com/developerworks/cn/java/j-ft13/
 *
 * @author zhangxu
 */
public class EitherTest {

    public static Either<Exception, String> uppercase2Lowercase(String s) {
        if (!s.matches("[A-Z]+")) {
            return Either.left(new Exception("Invalid Upper case"));
        } else {
            return Either.right(s.toLowerCase());
        }
    }

    @Test
    public void parsing_success() {
        Either<Exception, String> result = uppercase2Lowercase("XLII");
        assertEquals("xlii", result.right().value());
    }

    @Test
    public void parsing_failure() {
        Either<Exception, String> result = uppercase2Lowercase("f0OO");
        assertEquals("Invalid Upper case", result.left().value().getMessage());
    }

    public static P1<Either<Exception, String>> uppercase2LowercaseLazy(final String s) {
        if (!s.matches("[A-Z]+")) {
            return new P1<Either<Exception, String>>() {
                public Either<Exception, String> _1() {
                    return Either.left(new Exception("Invalid Upper case"));
                }
            };
        } else {
            return new P1<Either<Exception, String>>() {
                public Either<Exception, String> _1() {
                    return Either.right(s.toLowerCase());
                }
            };
        }
    }

    @Test
    public void parse_lazy() {
        P1<Either<Exception, String>> result = uppercase2LowercaseLazy("XLII");
        assertEquals("xlii", result._1().right().value());
    }

    @Test
    public void parse_lazy_exception() {
        P1<Either<Exception, String>> result = uppercase2LowercaseLazy("FOppO");
        assertTrue(result._1().isLeft());
        assertEquals("Invalid Upper case", result._1().left().value().getMessage());
    }
}
