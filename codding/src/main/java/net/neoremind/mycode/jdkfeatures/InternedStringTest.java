package net.neoremind.mycode.jdkfeatures;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * ① 使用字符串直接量时会在常量池创建对象，当然必须是常量折叠之后的。
 * ② 使用new String()时，new产生的字符串对象是位于堆中，而不是常量池中。
 * ③ JDK7之后intern()发生过变化，现在如果常量池中不存在这个对像，不会复制到常量池中，而是简单的使用堆中已有字符串对象。
 * ④ JDK7以前的intern()不是这样子的，以前会在常量池中创建一个新的对象，你可以将你的代码，在JDK6中测试一下，结果应该会不同。
 * <p>
 * <p>
 * 本来API里那句话就没有保证过str.intern() == str。那不是它的本意。JDK/JVM的组合选择如何实现就有自由了。
 * 为什么JRockit与新的JDK7里的HotSpot会返回true其实很简单：它们的string pool并不拷贝输进来intern()的java.lang
 * .String实例，只是在池里记录了每组内容相同的String实例首个被intern的那个实例的引用。
 * <p>
 * http://rednaxelafx.iteye.com/blog/774673
 * http://www.wtoutiao.com/a/1023451.html
 * http://silencegg.iteye.com/blog/2069267
 * http://www.iteye.com/topic/1112592?page=3#2216483
 *
 * @author zhangxu
 */
public class InternedStringTest {

    /**
     * <tt>s</tt>并没有在JVM加载时候在字符串常量池中新建"1"。
     * 因为<tt>s</tt>是一个指向堆的引用。
     * 调用<code>s.intern()</code>后，发现字符串常量池没有"1"，则新建了一个，并返回引用，但是没有赋值任何局部变量。
     * 新建一个s2，指向字符串常量池中的"1"。
     * 由于s指向堆，而s2指向字符串常量池中的"1"，因此内存地址不一样。
     * <p>
     * 编译后的字节码如下：
     * <pre>
     * public void testInternedString1();
     * Code:
     * Stack=3, Locals=3, Args_size=1
     * 0:	new	#2; //class java/lang/String
     * 3:	dup
     * 4:	ldc	#3; //String 1
     * 6:	invokespecial	#4; //Method java/lang/String."<init>":(Ljava/lang/String;)V
     * 9:	astore_1
     * 10:	aload_1
     * 11:	invokevirtual	#5; //Method java/lang/String.intern:()Ljava/lang/String;
     * 14:	pop
     * 15:	ldc	#3; //String 1
     * 17:	astore_2
     * 18:	aload_2
     * 19:	aload_1
     * 20:	if_acmpne	27
     * 23:	iconst_1
     * 24:	goto	28
     * 27:	iconst_0
     * 28:	invokestatic	#6; //Method java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
     * 31:	iconst_0
     * 32:	invokestatic	#6; //Method java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
     * 35:	invokestatic	#7; //Method org/hamcrest/Matchers.is:(Ljava/lang/Object;)Lorg/hamcrest/Matcher;
     * 38:	invokestatic	#8; //Method org/junit/Assert.assertThat:(Ljava/lang/Object;Lorg/hamcrest/Matcher;)V
     * 41:	return
     * </pre>
     */
    @Test
    public void testInternedString1() {
        String s = new String("1");
        s.intern();
        String s2 = "1";
        assertThat(s2 == s, Matchers.is(false));
    }

    /**
     * <tt>s</tt>并没有在JVM加载时候在字符串常量池中新建"11"，而是存储"1"，
     * 而是由编译器作为新建的stringbuilder对象，并且
     * 在运行时自动执行toString()方法，因此<tt>s</tt>是一个指向堆的引用。
     * 调用<code>s.intern()</code>后，发现字符串常量池没有"11"，则新建了一个，并返回引用，但是没有赋值任何局部变量。
     * 新建一个s2，关键点是jdk7中常量池不在Perm区域了，这块做了调整。
     * 常量池中不需要再存储一份对象了，可以直接存储堆中的引用。这份引用指向s引用的对象。
     * 也就是说引用地址是相同的，因此内存地址不一样。
     * <p>
     * <p>
     * 编译后的字节码如下：
     * <pre>
     * public void testInternedString2();
     * Code:
     * Stack=4, Locals=3, Args_size=1
     * 0:	new	#9; //class java/lang/StringBuilder
     * 3:	dup
     * 4:	invokespecial	#10; //Method java/lang/StringBuilder."<init>":()V
     * 7:	new	#2; //class java/lang/String
     * 10:	dup
     * 11:	ldc	#3; //String 1
     * 13:	invokespecial	#4; //Method java/lang/String."<init>":(Ljava/lang/String;)V
     * 16:	invokevirtual	#11; //Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
     * 19:	new	#2; //class java/lang/String
     * 22:	dup
     * 23:	ldc	#3; //String 1
     * 25:	invokespecial	#4; //Method java/lang/String."<init>":(Ljava/lang/String;)V
     * 28:	invokevirtual	#11; //Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
     * 31:	invokevirtual	#12; //Method java/lang/StringBuilder.toString:()Ljava/lang/String;
     * 34:	astore_1
     * 35:	aload_1
     * 36:	invokevirtual	#5; //Method java/lang/String.intern:()Ljava/lang/String;
     * 39:	pop
     * 40:	ldc	#13; //String 11
     * 42:	astore_2
     * 43:	aload_2
     * 44:	aload_1
     * 45:	if_acmpne	52
     * 48:	iconst_1
     * 49:	goto	53
     * 52:	iconst_0
     * 53:	invokestatic	#6; //Method java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
     * 56:	iconst_1
     * 57:	invokestatic	#6; //Method java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
     * 60:	invokestatic	#7; //Method org/hamcrest/Matchers.is:(Ljava/lang/Object;)Lorg/hamcrest/Matcher;
     * 63:	invokestatic	#8; //Method org/junit/Assert.assertThat:(Ljava/lang/Object;Lorg/hamcrest/Matcher;)V
     * 66:	return
     * </pre>
     */
    @Test
    public void testInternedString2() {
        String s = new String("1") + new String("1");
        s.intern();
        String s2 = "11";
        assertThat(s2 == s, Matchers.is(true));
    }

    @Test
    public void testInternedString3() {
        String s = new String("1") + new String("1");
        String s2 = "11";
        s.intern();
        assertThat(s2 == s, Matchers.is(false));
    }

    /**
     * https://www.zhihu.com/question/36908414
     */
    @Test
    public void pushPool() {
        String a = "a";
        String param = "b" + a;   // param指向堆，编译器变成了stringbuilder.tostring
        assertThat("ba" == param.intern(), Matchers.is(true));  // ba存在字符串常量池中，intern后返回的就是常量池，所以相同
        assertThat(param == "ba", Matchers.is(false));  // 指向堆和指向常量池不一样
    }

    @Test
    public void pushPool2() {
        String a = "a";
        String param = "b" + a;  // param指向堆，编译器变成了stringbuilder.tostring
        assertThat(param.intern() == "ba", Matchers.is(true));  // 先intern了，发现字符串常量池没有，则新建了一个字符串常量池的引用，指向堆。JDK7的做法。
        assertThat(param == "ba", Matchers.is(true));  // 引入自然相同
    }

    /**
     * https://www.zhihu.com/question/32672669
     */
    @Test
    public void testJavaString() {
        String str1 = new StringBuilder("计算机").append("软件").toString();
        assertThat(str1.intern() == str1, Matchers.is(false));  // 初始化的时候压根没有，自然复用对象

        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2.intern() == str2);
        assertThat(str2.intern() == str2, Matchers.is(false));  // 初始化的时候字符串常量池默认有了java，所以intern的对过相关，但是str2指向的堆，所以不同
    }

}
