package net.neoremind.mycode.guava.reflection;

import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.reflect.TypeToken;

import junit.framework.TestCase;

/**
 * @author zhangxu
 */
public class TypeTokenTest extends TestCase {

    public void testRawTypeIsCorrect() {
        TypeToken<List<String>> token = new TypeToken<List<String>>() {
        };
        System.out.println(token);
        assertEquals(List.class, token.getRawType());
    }

    public void testTypeIsCorrect() {
        TypeToken<List<String>> token = new TypeToken<List<String>>() {
        };
        System.out.println(token.getType());
        assertEquals(StringList.class.getGenericInterfaces()[0], token.getType());
    }

    public void testGetClass() {
        TypeToken<List> token = TypeToken.of(List.class);
        System.out.println(token);
        assertEquals(new TypeToken<List>() {
        }, token);
    }

    public void testGetType() {
        TypeToken<?> t = TypeToken.of(StringList.class.getGenericInterfaces()[0]);
        System.out.println(t);
        assertEquals(new TypeToken<List<String>>() {
        }, t);
    }

    public void testGenericArrayType() {
        TypeToken<List<String>[]> token = new TypeToken<List<String>[]>() {
        };
        assertEquals(List[].class, token.getRawType());
        System.out.println(token.getType());
        System.out.println(token.getTypes());
    }

    public <T> void testGenericVariableTypeArrays() {
        assertEquals("T[]", new TypeToken<T[]>() {
        }.toString());
    }

    public void testResolveType() throws Exception {
        Method getFromList = List.class.getMethod("get", int.class);
        TypeToken<?> returnType = new TypeToken<List<String>>() {
        }
                .resolveType(getFromList.getGenericReturnType());
        System.out.println(returnType);
        assertEquals(String.class, returnType.getType());
    }

    public void testResolveType2() throws Exception {
        Method getKey = StringList.class.getMethod("getKey");
        TypeToken<?> returnType = new TypeToken<List<String>>() {
        }
                .resolveType(getKey.getGenericReturnType());
        System.out.println(returnType);
        assertEquals(Integer.class, returnType.getType());
    }

    public void testResolveType3() throws Exception {
        List<Field> fs = getInheritedFields(Person.class);
        TypeToken<?> returnType = TypeToken.of(fs.get(0).getType());
        System.out.println(returnType);
        assertEquals(Number.class, returnType.getType());
    }

    class StringList extends ArrayList<String> implements List<String>, Key<Integer> {

        @Override
        public Integer getKey() {
            return 100;
        }
    }

    public static List<Field> getInheritedFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

}

class Person extends AbstractKeyable<Integer> implements Key<Integer> {
    @Override
    public Integer getKey() {
        return id;
    }
}

abstract class AbstractKeyable<T extends Number> implements Key<T> {
    protected T id;
}

interface Key<T extends Number> {

    T getKey();
}
