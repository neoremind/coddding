# JAVA并发编程实战笔记

[code listing](http://jcip.net/listings.html)

[英文版电子书](http://www.periodicooficial.oaxaca.gob.mx/files/2011/05/EXT02-2011-05-19.pdf)

## 第一章 简介

### 1.1 并发简史

早期计算机的分时系统，每个进程就是一个虚拟冯诺依曼机的，按指令顺序串行执行即可。

为了满足这些诉求，提出了线程的概念：

* 提高资源利用率
* 公平性（Time Slicing）
* 便利性（多任务处理）

这样一个进程就可以有多个控制流了，线程共享进程的资源，包括fd等。

所以线程被称为轻量级的进程，大多数现代操作系统也是按照线程为基本单元来调度的，例如`LockSupport.park(Thread)`就是一个用`Unsafe`的API来告诉操作系统挂起该线程的。

那么并发问题的本质就是，这些线程可能会访问堆上的共享变量，所以下文所说的一切都是提供了各种数据共享机制的，防止出现“不测”。

### 1.2 线程的优势

1. 发挥多处理器优势。
2. 建模简单，你写个Servlet和RMI完全不关心底层如何使用Socket，一个时刻多少个并发多来。
3. 异步事件简化处理，非阻塞I/O包NIO。
4. 响应更加灵敏。

### 1.3 线程带来的风险

- 安全性问题

```
public class UnsafeSequence {
  private int value;
  public int getNext() {
    return value++;
  }
}
```

++非原子性，看看字节码。

```
0: aload_0
1: dup
2: getfield      #2                  // Field index:J
5: dup2_x1
6: lconst_1
7: ladd
8: putfield      #2                  // Field index:J
11: lreturn
```

这种叫做竞态条件（Race Condition），多个线程共享相同的内存地址空间，线程交替运行，他们可能访问了或者修改其他线程正在使用的变量，或者已经被别的线程改过的变量，因此结果一般都偏小。

那么为啥JVM不让这顺序执行呢，天下多太平？无论编译器、运行时还是操作系统，都存在各种缓存以及指令重排序的特性，为提供更高的性能优化，如果都串行了，比如加了`sychronized`那么势必会抑制这种性能的提升。

- 活跃性问题

  死锁、饥饿、活锁

- 性能问题

  刚刚明明说了线程可以提高性能，但是怎么又降低了？降低的现象可能是服务响应时间长、吞吐量低、资源消耗过多，伸缩性（啥叫做伸缩性？？？）较差。

  这是因为线程本身存在运行开销，挂起、唤醒线程需要频繁的上下文切换（context switch），CPU花了不少时间在线程调度上而不是线程运行商，因此共享数据并为了安全，使用同步机制的时候，会抑制某些编译器优化，使内存缓冲区数据吴萧，以及增加内存总线的同步数量等等。这些额外的开销都属于性能问题。

### 1.4 线程无处不在

Servlet的安全性是“蔓延”开来的，或者框架需要一个回调，回调函数不安全都会引起线程安全问题。



## 第二章 线程安全性Thread Safety

- 思考为什么要保证线程安全？

线程安全保障的是对象，取决于是否被多个线程访问。安全意味着采用一些**同步机制来协同对象可变状态的访问**，如果不能协同就会导致数据破坏或者“不测”。

Java主要用`synchronized`做同步，这是一种独占的加锁方式，其他还包括volatile、显式锁（explicit lock）等。

- 如果出现线程安全问题，要怎么解决？

1）不在线程间共享（比如改成局部变量）

2）将状态改为不可变变量

3）访问状态变量时使用同步

### 2.1 什么是线程安全性

当多个线程访问某个类时，不管是

1）运行时环境采用何种调度方式，

2）或者这些线程如何交替执行，

3）并在在主调代码中不需要任何额外的同步或者协同，

这个类能**始终**表现出**正确**的行为。

示例：一个无状态的Servlet，无状态对象一定是线程安全的。

### 2.2原子性

```
public class UnsafeCountingFactorizer extends HttpServlet {
	private long count = 0;

	public long getCount() {
		return count;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		count++;
	}
}
```

这很明显存在Race condition，使得结果不可靠。

这种问题的本质是基于“先检查后执行（check-then-act）”操作，这个check之后可能就失效了，下一步动作肯定不对。

再来一个例子，

```
public class LazyInitRace {
	private ExpensiveObject expensiveObject = null;

	public ExpensiveObject getInstance() {
		if (null == expensiveObject) {
			expensiveObject = new ExpensiveObject();
		}
		return expensiveObject;
	}

	private static class ExpensiveObject {
	}
}
```

这个单例明显存在线程安全问题，我们有一种双重检测加锁+volatile的方式可以解决。

```
class SingletonWithDoubleCheckedLockingUnsafeEdition {
    private static volatile SingletonWithDoubleCheckedLockingUnsafeEdition INSTANCE = null;
    private static final Object LOCK = new Object();

    public static SingletonWithDoubleCheckedLockingUnsafeEdition getInstance() {
        if (INSTANCE == null) {
            synchronized(LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new SingletonWithDoubleCheckedLockingUnsafeEdition();
                }
            }
        }
        return INSTANCE;
    }

    private SingletonWithDoubleCheckedLockingUnsafeEdition() {
    }
}
```

要规避这种Race condition，就需要复合这些操作，让他们变成原子方式执行，修改这些变量的时候，别的线程不能使用这个变量。

比如使用J.U.C下面的atomic类。

```
public class UnsafeCountingFactorizer extends HttpServlet {
	private AtomicLong count = new AtomicLong(0);

	public long getCount() {
		return count.get();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		count.incrementAndGet();
	}
}
```

### 2.3 加锁机制

看下面的例子，虽然单个操作都是原子的，但是加起来不对，多个变量之间彼此不是互相独立的。

```
public class UnsafeCachingFactorizer extends HttpServlet {
	private final AtomicReference<BigInteger> lastNumber = new AtomicReference<BigInteger>();
	private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<BigInteger[]>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BigInteger value = new BigInteger(req.getParameter("number"));
		if (value.equals(lastNumber.get())) {
			resp.addHeader("Factors", Arrays.asList(lastFactors.get())
					.toString());
		} else {
			BigInteger[] factors = calculateFactor(value);
			lastNumber.set(value);
			lastFactors.set(factors);
			resp.addHeader("Factors", Arrays.asList(factors).toString());
		}
	}
}
```

我们的原则是要保持状态的一致性，就需要单个原子操作中更新所有的状态变量。所以我们可以使用**同步代码块**，Java的内置锁相当于一种互斥锁，只有一个线程可以持有锁，释放后别的线程才可以进入执行。

上面的例子最简单doGet方法上加个锁就行。

这里注意一个类里面不同的方法都有`synchronized`，由于内置锁是**可重入**的，所以内部没事。

### 2.4 用锁来保护状态

锁可以使其保护的代码路径以串行的方式来访问。

前面的例子中，计数器和延迟初始化单例都是通过原子操作避免Race condition。如果复合操作需要持有一个锁，那么以为这访问这个变量的所有位置都需要使用同步，例如最经典的Vector的例子

```
if (!vector.contains(element)) {
  vector.add(element);
}
```

这明显不能保证原子性。

### 2.5 活跃性和性能

上面的缓存最近一次因数分解的例子，在方法上加同步会严重影响性能，因此可以采用同步代码块降低锁的粒度。



## 第三章 对象的共享Sharing Objects

和第二章的关系，第二章介绍了如何通过同步的方法避免多个线程同一时刻访问相同的数据，而本章介绍如何共享和发布对象，从而使他们能够安全地被多个线程同时访问。

这两章加一起是构建J.U.C类库的基础。

### 3.1 可见性

一般一想到同步，就会想到原子性或者“临界区（Critical section）”，其实还有另外一方面就是可见性（visibility）。我们要做到防止修改的同时，另外一个线程写入，也得确保修改后，别的线程能够看到刚刚发生的变化。

为了确保多个线程之间对内存写入操作的可见性，必须使用同步机制。

下面的例子。

```
public class NotVisibility {
	private static boolean ready;
	private static int number;

	private static class ReaderThread extends Thread {
		@Override
		public void run() {
			while (!ready) {
				Thread.yield();
			}
			System.out.println(number);
		}
	}

	public static void main(String[] args) {
		new ReaderThread().start();
		number = 42;
		ready = true;
	}
}
```

输出可能是0或者无限循环下去。

1）如果输出是0，那么出现了指令重排序，read=true先执行，然后number=42后执行，因为可见性问题，reader读不到最新的42，而是默认的0。根据Javadoc虽然yield能让出CPU，单这个JNI的调用完全没有任何保障，所以你等不到number赋值为42了也是有可能的。

2）如果无限循环，是由于编译器优化，将while(!ready)优化为if(!ready) while(true)则无限下去。

为什么存在可见性问题？因为各种速度不一致，CPU>寄存器>L1/2/2 Cache>RAM，而JVM是和RAM打交道的，多个线程你写了我怎么知道你写了？我怎么看的见最新的值？这样就需要编译器、Runtime和操作系统能够配合工作好，给程序员一个能够预测的确定的状态来满足线程安全性。这其实就是所谓的内存可见性要有保障。一般情况都不需要串行或者特别强的可见性，除非需要线程安全保障，如果要求这个强一致性，那么JVM的性能开销就太大了，完全没必要。

Java语言规范规定了JVM线程内部维持顺序化语义，但是没规定指令必须严格按照这个“显然”的顺序，而指令重排序使得JVM能够根据处理器的特性（CPU的多级缓存系统、多核处理器等）适当的重新排序机器指令，使机器指令更符合CPU的执行特点，最大限度的发挥机器的性能。说白了就是为了性能考虑。

包括以下三种：
1）编译器生成指令的次序，可以不同于源代码所暗示的“显然”版本。
2）处理器可以乱序或者并行的执行指令。
3）缓存会改变写入提交到主内存的变量的次序。

所以，总结一句话，**只要数据在多个线程间共享，就需要使用正确的同步**。

下面的例子是一个非线程安全的可变整数类，因为value在多线程下可见性无法保障。解决的办法就是get和set方法上都加锁。

```	
public class MutableInteger {
    private int value;
 
    public int get() {
        return value;
    }
 
    public void set(int value) {
        this.value = value;
    }
}
```

另外JVM规范在涉及到64位操作的时候线程、同步等的方面的规定是委托给Java语言规范定义的，而Java语言规范里的相关定义就在[链接](http://docs.oracle.com/javase/specs/jls/se7/html/jls-17.html#jls-17.7)中。

* 实现对普通long与double的读写不要求是原子的（但如果实现为原子操作也OK）
* 实现对volatile long与volatile double的读写必须是原子的（没有选择余地）

> 加锁的含义不仅仅局限于互斥行为，还包括内存可见性。为了确保所有的线程都能看到共享变量的最新值，所有执行读操作或者写操作的线程都必须在同一个锁上同步。

下面说volatile，可以看做和synchronized有一样的特性，都是互斥和可见性，在某些情况下比锁更加方便，比锁更轻量，他的作用实际就是解决刚刚背景提到的两个问题。

* 禁止重排序：这个保证对volatile变量的操作时按照指令的出现顺序执行的。
* 不会被缓存在寄存器中（只有拥有线程可见）或者其他对CPU不可见的地方，每次总是从主存中读取volatile变量的结果。也就是在happens-before法则中，对一个valatile变量的写操作后，其后的任何读操作理解可见此写操作的结果。

还是用MutableInteger作例子，如果value为volatile，那么可以同样保障可见性，但是没有线程的阻塞，不会加锁，所以更轻量。

一般来说如下的代码在多线程下为了可以正确执行，需要加入volatile修饰。

```
volatile boolean asleep;
...
    while(!asleep) 
        dosmth();
```

然而volatile并不能保证原子性，加锁原子性和可见性都可以保证，所以说加锁是一种更强同步机制。

这里注意一下情况才可以用volatile：

* 对变量的写入不依赖变量的当前值，或者能保证单线程更新变量。
* 变量不会与其他变量一起纳入不变性条件中。
* 在访问变量时不需要加锁。

### 3.2 发布与逸出

发布了一个secret集合，这个内部的状态会被破坏，第一难以维护，第二破坏线程的安全性。**这也就是封装的好处，能够对正确性分析变得可能，并使得无意中破坏的设计约束变得更难**。

```
class Secrets {
    public static Set<Secret> knownSecrets;
 
    public void initialize() {
        knownSecrets = new HashSet<Secret>();
    }
}
 
class Secret {
}
```

下面的例子，虽然对象immutable，但是引用逃逸了。

```
class UnsafeStates {
    private String[] states = new String[]{
        "AK", "AL" /*...*/
    };
 
    public String[] getStates() {
        return states;
    }
}
```

下面的例子更加隐秘，非显式的逃逸了，this引用在构造函数中逃逸。当内部的EventListener发布时，外部封装的ThisEascape也逃逸了。因为EventListener根本可能没有初始化好。

```
public class ThisEscape {
    public ThisEscape(EventSource source) {
        source.registerListener(new EventListener() {
            public void onEvent(Event e) {
                doSomething(e);
            }
        });
    }
 
    void doSomething(Event e) {
    }
 
 
    interface EventSource {
        void registerListener(EventListener e);
    }
 
    interface EventListener {
        void onEvent(Event e);
    }
 
    interface Event {
    }
}
```

解决这个问题，可以用一个公共的工厂方法解决。

```
public class SafeListener {
    private final EventListener listener;
 
    private SafeListener() {
        listener = new EventListener() {
            public void onEvent(Event e) {
                doSomething(e);
            }
        };
    }
 
    public static SafeListener newInstance(EventSource source) {
        SafeListener safe = new SafeListener();
        source.registerListener(safe.listener);
        return safe;
    }
 
    ...
}
```

### 3.3 线程封闭Thread confinement

* ad-hoc封闭，维护线程封闭完全由程序承担，危险禁止使用。
* 栈封闭
* ThreadLocal类

```
public class ConnectionDispenser {
    static String DB_URL = "jdbc:mysql://localhost/mydatabase";
 
    private ThreadLocal<Connection> connectionHolder
            = new ThreadLocal<Connection>() {
                public Connection initialValue() {
                    try {
                        return DriverManager.getConnection(DB_URL);
                    } catch (SQLException e) {
                        throw new RuntimeException("Unable to acquire Connection, e");
                    }
                };
            };
 
    public Connection getConnection() {
        return connectionHolder.get();
    }
}
```

### 3.4 不变性

可不变对象一定是线程安全的。

不可变对象，他们只有一种状态，并且该状态是由构造函数来控制的。

上面的那个Secretes例子，如果是“不可信代码”使用不当，就会改变状态，引起线程安全问题。所以说“不可变对象”和“不可变的对象引用”是两种概念。

final很重要，他确保初始化过程的安全性，这是Java内存模型（JMM）规定的。

可不变对象很好的一个例子，这在guava类库中非常常见。

```
public class OneValueCache {
    private final BigInteger lastNumber;
    private final BigInteger[] lastFactors;
 
    public OneValueCache(BigInteger i,
                         BigInteger[] factors) {
        lastNumber = i;
        lastFactors = Arrays.copyOf(factors, factors.length);
    }
 
    public BigInteger[] getFactors(BigInteger i) {
        if (lastNumber == null || !lastNumber.equals(i))
            return null;
        else
            return Arrays.copyOf(lastFactors, lastFactors.length);
    }
}
```

### 3.5 安全发布

上几个小节讲的都是如何确保对象不被发布，例如封闭等。下面重点说如何发布线程安全的对象。

下面的对象如何不加同步机制，可能会发生一个线程初始化完，另外一个线程不可见认为为空又初始化，或者是一个旧值，因此调用函数的时候可能会报错。这都是未被正确发布的对象。

```
public class Holder {
    private int n;
 
    public Holder(int n) {
        this.n = n;
    }
 
    public void assertSanity() {
        if (n != n)
            throw new AssertionError("This statement is false.");
    }
}
```

Java内存模型为不可变对象提供了一些特殊的初始化安全保证，比如final，比如Holder用如下代码构建。

```
public static Holder holder = new Holder(42);
```

一些常用的安全发布的模式如下：

* 在静态构造函数中初始化一个对象引用
* 对象引用保存到volatile或者AtomicReference中
* 对象的引用保存到某个正确构造对象的final类型域中
* 对象的引用保存在一个由锁保护的域中

还有一些安全发布融入例如HashTable/syncMap/concurrentMap, vector/copyOnWriteListSet/syncSet(List), blockingQueue/concurrentLinkedQueue



## 第四章 对象的组合Composing Objects

### 4.1 设计线程安全的类

### 4.2 实例封闭

例如ArrayList和HashMap都提供了Collections.synchronizedList及其类似方法，这种装饰器Decorator模式，能将接口中的每个方法都实现为同步方法。

另外在做锁的时候，一般都内置锁，比如new Object()。

### 4.3 线程安全性委托

例如可以通过封装ConcurrentMap来打到线程安全的目的。这要确保委托是正确的足以保护类的安全。

### 4.4 在现有的安全类中添加功能

* 扩展类

```
@ThreadSafe
public class BetterVector <E> extends Vector<E> {
    // When extending a serializable class, you should redefine serialVersionUID
    static final long serialVersionUID = -3963416950630760754L;
 
    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !contains(x);
        if (absent)
            add(x);
        return absent;
    }
}
```

这种方法比较脆弱，同步策略分散在多个类中单独维护，比如Vector变了实现，这里面就失效了。

* 客户端加锁

```
@NotThreadSafe
class BadListHelper <E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());
 
    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !list.contains(x);
        if (absent)
            list.add(x);
        return absent;
    }
}
 
@ThreadSafe
class GoodListHelper <E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());
 
    public boolean putIfAbsent(E x) {
        synchronized (list) {
            boolean absent = !list.contains(x);
            if (absent)
                list.add(x);
            return absent;
        }
    }
}
```

非常经典的例子，本来是想做到安全，但是不是用的同一把锁。正确的如下：

```
@NotThreadSafe
class BadListHelper <E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());
 
    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !list.contains(x);
        if (absent)
            list.add(x);
        return absent;
    }
}
 
@ThreadSafe
class GoodListHelper <E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());
 
    public boolean putIfAbsent(E x) {
        synchronized (list) {
            boolean absent = !list.contains(x);
            if (absent)
                list.add(x);
            return absent;
        }
    }
}
```

- 组合

```
@ThreadSafe
public class ImprovedList<T> implements List<T> {
    private final List<T> list;
 
    /**
     * PRE: list argument is thread-safe.
     */
    public ImprovedList(List<T> list) { this.list = list; }
 
    public synchronized boolean putIfAbsent(T x) {
        boolean contains = list.contains(x);
        if (contains)
            list.add(x);
        return !contains;
    }
    //...
}
```

完全不暴露list出去，额外一层加锁。

### 4.5 将同步策略文档化



## 第五章 基础构建模块Building blocks

### 5.1 同步容器类

#### 5.1.1 同步容器类的问题

包括Vector和Hashtable，二者都是早期JDK的一部分，此外还包括封装类，比如Collections.synchronizedXXX等工厂方法创建的，他们的实现的方式都是把状态封装起来，并对每一个公有方法都进行同步，每次只有一个线程可以访问容器的状态，比如只有一个线程可以add，别人无法add，必须等待这个线程退出完成。

Hashtable的问题是如线程 1 使用 put 进行添加元素,线程 2 不但不能使用 put 方法添加元素,并且也不能使用 get 方法来获取元素,所以竞争越激烈效率越低。

但是同步容器类的问题是，虽然单个方法都是安全的，但是复合操作确不是，例如

* 迭代
* 若没有则添加

等等。例如下面的代码

```
public class UnsafeVectorHelpers {
    public static Object getLast(Vector list) {
        int lastIndex = list.size() - 1;
        return list.get(lastIndex);
    }
 
    public static void deleteLast(Vector list) {
        int lastIndex = list.size() - 1;
        list.remove(lastIndex);
    }
}
```

试想A线程执行getLast，B线程执行deleteLast，A看到size=10，那么get(9)，而B在A get之前remove了9，那么直接会抛出ArrayIndexOutOfBoundsException。所以客户端也是需要加锁的，而且必须在list上加锁，在方法上加不是同一把锁，如第三章所讲的，没用，所以改造的代码如下：

```
public class SafeVectorHelpers {
    public static Object getLast(Vector list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            return list.get(lastIndex);
        }
    }
 
    public static void deleteLast(Vector list) {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            list.remove(lastIndex);
        }
    }
}
```

同样迭代的时候也可能有危险，

```
for (int i = 0; i < vector.size(); i++) 
    dosmth(vector.get(i));
```

改造后可以加锁，虽然解决了不可靠问题，但是要牺牲一些伸缩性，降低并发。

```
synchronized(vector) {
  for (int i = 0; i < vector.size(); i++) 
    dosmth(vector.get(i));
}
```

#### 5.1.2 迭代器与ConcurrentModificationException

在迭代的时候，如果有别的线程修改了容器，那么JDK会采用fail-fast策略，抛出ConcurrentModificationException。例如下面的代码在多线程环境下其实是不安全的。

```
List<Widget> widgetList
 = Collections.synchronizedList(new ArrayList<Widget>());
...
// May throw ConcurrentModificationException
for (Widget w : widgetList)
 doSomething(w); 
```

for-each只是个语法糖罢了，实际还是用Iterator来实现，可以看看JDK的源代码在AbstractList#Itr类，很简单，有个判断checkForComodification();会抛出异常。

```
public E next() {
            checkForComodification();
            try {
                int i = cursor;
                E next = get(i);
                lastRet = i;
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }
```

#### 5.1.3 异常迭代器

有的时候比如在toString方法里面打印了list，而在多线程环境中有并发的读写的情况下，也会抛出这个异常。

### 5.2 并发容器

上面说的同步容器的代价就是严重降低并发性，存在竞争时吞吐量下降严重。

使用这一节说的并发容器可以极大的提高伸缩性并降低风险。

ConcurrentLinkedQueue、ConcurrentHashMap、ConcurrentSkipListMap...

ConcurrentHashMap使用一种粒度更细的加锁机制来实现更大程度的共享，叫做分段锁（Lock Striping），任务线程可以并发的读写互不阻塞，同时允许一定数量的写入线程并发。性能提升非常多，只是比单线程的HashMap损失一点点性能。

下面的摘自网络电子书java_concurrent.pdf。

HashTable 容器在竞争激烈的并发环境下表现出效率低下的原因是所有访问 HashTable 的线程 都必须竞争同一把锁,那假如容器里有多把锁,每一把锁用于锁容器其中一部分数据,那么当多线 程访问容器里不同数据段的数据时,线程间就不会存在锁竞争,从而可以有效的􏰀高并发访问效率, 这就是 ConcurrentHashMap 所使用的锁分段技术,首先将数据分成一段一段的存储,然后给每一段 数据配一把锁,当一个线程占用锁访问其中一个段数据的时候,其他段的数据也能被其他线程访问。

ConcurrentHashMap 是由 Segment 数组结构和 HashEntry 数组结构组成。Segment 是一种可重入锁 ReentrantLock,在 ConcurrentHashMap 里扮演锁的角色,HashEntry 则用于存储键值对数据。 一个 ConcurrentHashMap 里包含一个 Segment 数组,Segment 的结构和 HashMap 类似,是一种数 组和链表结构, 一个 Segment 里包含一个 HashEntry 数组,每个 HashEntry 是一个链表结构的元 素, 每个 Segment 守护者一个 HashEntry 数组里的元素,当对 HashEntry 数组的数据进行修改时, 必须首先获得它对应的 Segment 锁。

get操作的高效之处在于不需要加锁,原因是它的get方法里将要使用的共享变量都定义成 volatile,如用于统计当前Segement大小的count字段和用于存储值的HashEntry的 value。定义成volatile的变量,能够在线程之间保持可见性,能够被多线程同时读,并且保证不会读到过期的值,但是只能被单线程写(有一种情况 可以被多线程写,就是写入的值不依赖于原值),在get 操作里只需要读不需要写共享变量count和value,所以可以不用加锁。只所以不会读到过期的值,是根据JMM内存模型的 happens-before原则,对volatile字段的写入操作先于读操作,即使两个线程同时修改和获取 volatile变量,get操作也能拿到最新的值,这是用volatile替换锁的经典应用场景。

下面的源代码摘自JDK7。

```
 static final class Segment<K,V> extends ReentrantLock implements Serializable {
        transient volatile HashEntry<K,V>[] table;
        /**
         * The total number of mutative operations in this segment.
         * Even though this may overflows 32 bits, it provides
         * sufficient accuracy for stability checks in CHM isEmpty()
         * and size() methods.  Accessed only either within locks or
         * among other volatile reads that maintain visibility.
         */
        transient int modCount;
        ...
 
 static final class HashEntry<K,V> {
        final int hash;
        final K key;
        volatile V value;
        volatile HashEntry<K,V> next;
```

另外ConcurrentHashMap不会抛出ConcurrentModificationException。

ConcurrentHashMap还提供了一系列额外的原子操作putIfAbsent, removeIfEqual, and replaceIfEqual等等。

CopyOnWriteArrayList每次修改时候都会复制一遍底层的数组，当迭代的操作远远大于修改的时候，才可以使用“写入时复制”容器。

### 5.3 阻塞队列和生产者-消费者模式 

摘自http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/BlockingQueue.html

|             | *Throws exception*                       | *Special value*                          | *Blocks*                                 | *Times out*                              |
| ----------- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- |
| **Insert**  | [`add(e)`](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/BlockingQueue.html#add(E))不能容纳抛出异常 | [`offer(e)`](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/BlockingQueue.html#offer(E)) 不能容纳返回false | [`put(e)`](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/BlockingQueue.html#put(E)) | [`offer(e, time, unit)`](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/BlockingQueue.html#offer(E,%20long,%20java.util.concurrent.TimeUnit)) |
| **Remove**  | [`remove()`](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/BlockingQueue.html#remove(java.lang.Object)) | [`poll()`](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/BlockingQueue.html#poll(long,%20java.util.concurrent.TimeUnit)) | [`take()`](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/BlockingQueue.html#take()) | [`poll(time, unit)`](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/BlockingQueue.html#poll(long,%20java.util.concurrent.TimeUnit)) |
| **Examine** | [`element()`](http://docs.oracle.com/javase/7/docs/api/java/util/Queue.html#element()) | [`peek()`](http://docs.oracle.com/javase/7/docs/api/java/util/Queue.html#peek()) | *not applicable*                         | *not applicable*                         |

举例，

```
class Producer implements Runnable {
   private final BlockingQueue queue;
   Producer(BlockingQueue q) { queue = q; }
   public void run() {
     try {
       while (true) { queue.put(produce()); }
     } catch (InterruptedException ex) { ... handle ...}
   }
   Object produce() { ... }
 }

 class Consumer implements Runnable {
   private final BlockingQueue queue;
   Consumer(BlockingQueue q) { queue = q; }
   public void run() {
     try {
       while (true) { consume(queue.take()); }
     } catch (InterruptedException ex) { ... handle ...}
   }
   void consume(Object x) { ... }
 }

 class Setup {
   void main() {
     BlockingQueue q = new SomeQueueImplementation();
     Producer p = new Producer(q);
     Consumer c1 = new Consumer(q);
     Consumer c2 = new Consumer(q);
     new Thread(p).start();
     new Thread(c1).start();
     new Thread(c2).start();
   }
 }
```

J.U.C下面提供了多种支持并发的队列，包括LinkedBlockingQueue、ArrayBlockingQueue都是FIFO队列，PriorityBlockingQueue是优先队列，特殊的SynchronouseQueue。

同时JAVA6之后还提供了双端队列，Deque（发音“deck”）和BlockingDequeue。有些时候它可以支持密取，叫做Work Stealing，也就是从队头取元素出来。