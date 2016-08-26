# JAVA并发编程实战笔记

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



## 第二章 线程安全性

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



