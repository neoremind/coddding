# JAVA并发编程实战笔记

[code listing](http://jcip.net/listings.html)

[英文版电子书](http://www.periodicooficial.oaxaca.gob.mx/files/2011/05/EXT02-2011-05-19.pdf)

## 准备工作

### 0x00 线程状态

![](http://neoremind.com/wp-content/uploads/2016/08/state-machine-example-java-6-thread-states.png)

关于线程的状态如下，来自JDK7的Javadoc：

[`NEW`](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html#NEW)A thread that has not yet started is in this state.

[`RUNNABLE`](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html#RUNNABLE)A thread executing in the Java virtual machine is in this state.

[`BLOCKED`](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html#BLOCKED)A thread that is blocked waiting for a monitor lock is in this state.

[`WAITING`](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html#WAITING)A thread that is waiting indefinitely for another thread to perform a particular action is in this state.

[`TIMED_WAITING`](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html#TIMED_WAITING)A thread that is waiting for another thread to perform an action for up to a specified waiting time is in this state.

[`TERMINATED`](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html#TERMINATED)A thread that has exited is in this state.

一些常见的误区讲解：

1）WAIT和SLEEP方法区别：

sleep是TIMED_WAITING，wait不带计时参数是WAITING，带计时参数是TIMED_WAITING，Java的Thread的状态和OS中的线程状态并不是严格对应的，涉及OS底层是怎么定义。比如Solaris中sleep用的是poll实现也就是带计时参数的io阻塞，这个阻塞直接就是系统级别的阻塞，线程进入阻塞队列，java Thread的Blocked只是在等待内置锁时的状态，这些线程在阻塞的时候也不会占用CPU时间，但当时多个线程争用锁这个行为是一个耗时操作，而且线程调度一般依赖内核中的调度，因此还会发生用户态和内核态的切换，然后在加上线程自己的上下文切换（Context Switch，包括local data, program pointer 的重新设置等），所以在多线程争用的情况，拥有锁的线程进行一些耗时操作，会极大降低吞吐量（amdahl定律），如果在同步块中使用sleep就是一种糟糕的做法，它不会释放锁却阻止其他线程获得锁，所以应该仔细设计分析，将这样的操作移出同步块。

2）WAITING和BLOCKED状态区别：

* WATTING就是一个线程调用了 Object.wait() 就是在等待别的线程对该对象调用 Object.notify() or Object.notifyAll()或者LockSupport.unPark(Thread)。
* BLOCKED是指线程正在等待获取锁。
* BLOCKED 和WAITING 都是非活动线程的状态，他们都不会占用CPU资源，也就是说会挂起。WAITING 线程是已经分配到了CPU时间，但是需要等待事件发生所以主动释放了CPU，直到某些事件完成后调用了notify()唤醒, **也就是WAITTING线程是自己现在不想要CPU时间，但是**BLOCKED线程是想要的，但是BLOCKED线程没有获得锁，所以轮不到BLOCKED线程。

1. 每个线程都有一个interrupt status标志位，用于表明当前线程是否处于中断状态 
2. 一般调用Thread.interrupt()会有两种处理方式 
   遇到一个低优先级的block状态时，比如object.wait(),object.sleep(),object.join()。它会立马触发一个unblock解除阻塞，并throw一个InterruptedException。 
   其他情况，Thread.interrupt()仅仅只是更新了status标志位。然后你的工作线程通过Thread.isInterrrupted()进行检查，可以做相应的处理，比如也throw InterruptedException或者是清理状态，取消task等。 

3）Thread.interrupt()的作用是什么？

* 每个线程都有一个interrupt status标志位，用于表明当前线程是否处于中断状态 
* 一般调用Thread.interrupt()会有两种处理方式，遇到调用wait(), wait(long), or wait(long, int) methods of the Object class, or of the join(), join(long), join(long, int), sleep(long), or sleep(long, int), methods of this class。这些状态会挂起线程，这时候interrupt会throw一个InterruptedException。 
* 其他情况，Thread.interrupt()仅仅只是更新了status标志位。然后你的工作线程通过Thread.isInterrrupted()进行检查，可以做相应的处理，比如也throw InterruptedException或者是清理状态，任务cancle， 数据清理等。 

4）LockSupport.park()和unpark()，与object.wait()和notify()的区别？ 

* 面向的主体不一样。LockSuport主要是针对Thread进进行阻塞处理，可以指定阻塞队列的目标对象，每次可以指定具体的线程唤醒。Object.wait()是以对象为纬度，阻塞当前的线程和唤醒单个(随机)或者所有线程。 
* 实现机制不同。虽然LockSuport可以指定monitor的object对象，但和object.wait()，两者的阻塞队列并不交叉。object.notifyAll()不能唤醒LockSupport的阻塞Thread。

5）关于wait和notify/notifyAll

- 下面摘自JDK8的Object#wait()方法的Javadoc：

Causes the current thread to wait until another thread invokes the notify() method or the notifyAll() method for this object. In other words, this method behaves exactly as if it simply performs the call wait(0).

The current thread must own this object's monitor. The thread releases ownership of this monitor and waits until another thread notifies threads waiting on this object's monitor to wake up either through a call to the notify method or the notifyAll method. The thread then waits until it can re-obtain ownership of the monitor and resumes execution.

调用某个对象的wait()方法能让当前线程挂起，并且当前线程必须拥有此对象的monitor（即锁），调用某个对象的notify()方法能够唤醒一个正在等待这个对象的monitor的线程，如果有多个线程都在等待这个对象的monitor，则只能唤醒其中一个线程；调用notifyAll()方法能够唤醒所有正在等待这个对象的monitor的线程。

调用wait()方法必须在同步块或者同步方法中进行（synchronized块或者synchronized方法）。调用某个对象的wait()方法，相当于让当前线程交出此对象的monitor，然后进入等待状态，等待后续再次获得此对象的锁，Thread类中的sleep方法使当前线程暂停执行一段时间，从而让其他线程有机会继续执行，但它并不释放对象锁，这是很重要的一点和sleep的区别。

As in the one argument version, interrupts and spurious wakeups are possible, and this method should always be used in a loop:

这里要注意如果wait被唤醒了，那么需要重新检查条件是否满足，否则可能线程已经被中断或者是假的不合法的状态，比如BlockingQueue的take和put的wait/notify版本就需要放在while循环里判断下。

           synchronized (obj) {
               while (<condition does not hold>)
                   obj.wait();
               ... // Perform action appropriate to condition
           }

This method should only be called by a thread that is the owner of this object's monitor. See the notify method for a description of the ways in which a thread can become the owner of a monitor.

- 下面摘自JDK8的Object#notify()方法的Javadoc：

Wakes up a single thread that is waiting on this object's monitor. If any threads are waiting on this object, one of them is chosen to be awakened. The choice is arbitrary and occurs at the discretion of the implementation. A thread waits on an object's monitor by calling one of the wait methods.

The awakened thread will not be able to proceed until the current thread relinquishes the lock on this object. The awakened thread will compete in the usual manner with any other threads that might be actively competing to synchronize on this object; for example, the awakened thread enjoys no reliable privilege or disadvantage in being the next thread to lock this object.

This method should only be called by a thread that is the owner of this object's monitor. A thread becomes the owner of the object's monitor in one of three ways:
1）By executing a synchronized instance method of that object.
2）By executing the body of a synchronized statement that synchronizes on the object.
3）For objects of type Class, by executing a synchronized static method of that class.
Only one thread at a time can own an object's monitor.



# 第一部分 基础知识

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

可重入意味着获取锁的操作的粒度是“线程”，而不是“调用”。重入的一种实现方法是为每一个锁关联一个获取计数值和一个所有者线程。当计数值为0时，这个锁没有被任何线程持有，当线程请求一个未被获取这个锁，计数值将递增，当线程退出同步代码块时，计数器会递减。当计数器为0时，释放该锁。

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

### 5.4阻塞和中断方法

线程可能会阻塞或者暂停执行，原因有很多种：

* 等待I/O操作结束
* 等待获取一个锁
* 等待从sleep中唤醒
* 等待另外一个线程的计算结果

当线程阻塞时，它通常被挂起，并处于某种阻塞状态（包括BLOCKED，WAITING，TIMED_WAITING，详细见准备工作一节的0x00部分）。阻塞操作与执行时间很长的普通操作差别在于，被阻塞线程必须等待某个不受它控制的事件发生才可能继续执行，例如等待I/O操作完成，等待某个锁可用，或者等待外部计算结果。当某个外部事件发生时，线程被置为RUNNABLE状态，才可有肯能被线程调度器选中继续执行。

BlockingQueue的put和take方法会抛出InterruptedException，还有Thread.sleep等，表示这个方法是一个阻塞方法，如果这个方法被Thread.interrupt()方法调用，被中断了，那么正在执行这些阻塞当中的线程将抛出InterruptedException，否则仅仅是置了一个interruped的status状态。

遇到interrupted异常应该如何处理？

1）传递异常。

2）恢复中断，如下代码。

```
public class TaskRunnable implements Runnable {
    BlockingQueue<Task> queue;
 
    public void run() {
        try {
            processTask(queue.take());
        } catch (InterruptedException e) {
            // restore interrupted status
            Thread.currentThread().interrupt();
        }
    }
 
    void processTask(Task task) {
        // Handle the task
    }
 
    interface Task {
    }
}
```

最不建议做的事swallow异常，啥也不做。

### 5.5 同步工具类

#### 5.5.1 闭锁

延迟进程的进度直到到达终止状态，相当于一扇大门，闭锁到达结束状态之前，这扇门一直关闭的，没有任何线程可以通过，到达结束状态，这扇门就会允许所有线程打开。举例：

* 确保某个计算在其需要的所有资源都已经被初始化后才继续执行，二元闭锁。

```
@Test
    public void testCountDownLatch() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        System.out.println("主线程开始...");
        new Thread(new Runner(latch)).start();
        System.out.println("等待Runner子线程完成中...");
        latch.await();
        latch.await(5, TimeUnit.MINUTES); // 已经countdown的再await就没用了
        System.out.println("主线程结束");
    }

    class Runner implements Runnable {

        private CountDownLatch latch;

        public Runner(CountDownLatch latch) {
            super();
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println("Runner子线程开始跑...");
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            latch.countDown();
            System.out.println("Runner子线程跑完！");
        }

    }
```

* 直到某个操作的所有参与者都就绪在继续执行。

```
final ReentrantLock rl = new ReentrantLock();
CountDownLatch cdl = new CountDownLatch(1);

        class Worker implements Runnable {
            private String name;

            Worker(String name) {
                this.name = name;
            }

            @Override
            public void run() {
                try {
                    cdl.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rl.lock();
                try {
                    System.out.printf("Thread %s is performing work.%n", name);
                } finally {
                    rl.unlock();
                }
            }
        }

        // 做一个CDL，让A1-A5充分竞争，谁都有可能先获得锁而执行。
        AsyncThread.run(new Worker("A1"));
        AsyncThread.run(new Worker("A2"));
        AsyncThread.run(new Worker("A3"));
        AsyncThread.run(new Worker("A4"));
        AsyncThread.run(new Worker("A5"));

        cdl.countDown();
```

官方书籍的例子是一个启停的案例：

```
public class TestHarness {
    public long timeTasks(int nThreads, final Runnable task)
            throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);
 
        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException ignored) {
                    }
                }
            };
            t.start();
        }
 
        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        return end - start;
    }
}
```

#### 5.5.2 FutureTask

FutureTask通过Callable实现，相当于一个可生成结果的Runnable，如果任务完成get立即返回结果，否则阻塞直到任务进行完成状态。

```
public class Preloader {
    ProductInfo loadProductInfo() throws DataLoadException {
        return null;
    }
 
    private final FutureTask<ProductInfo> future =
        new FutureTask<ProductInfo>(new Callable<ProductInfo>() {
            public ProductInfo call() throws DataLoadException {
                return loadProductInfo();
            }
        });
    private final Thread thread = new Thread(future);
 
    public void start() { thread.start(); }
 
    public ProductInfo get()
            throws DataLoadException, InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException)
                throw (DataLoadException) cause;
            else
                throw LaunderThrowable.launderThrowable(cause);
        }
    }
 
    interface ProductInfo {
    }
}
 
```

#### 5.5.3 信号量

用来控制访问某个资源的操作数量，或者同时执行某个操作的数量，还可以作为容器的边界检验。

Semaphore中管理着一组许可permit，初始数量通过构造函数构造，执行操作需要先获取许可，使用后释放回去，acquire可以当没有可用许可的时候可以阻塞，release用于返回一个许可给信号量。

下面的例子是限制同一时间某个资源的操作数量，

```
public static void main(String[] args) {
        int N = 10; // 工人数
        Semaphore semaphore = new Semaphore(5); // 机器数目
        for (int i = 0; i < N; i++)
            new Worker(i, semaphore).start();
    }

    static class Worker extends Thread {
        private int num;
        private Semaphore semaphore;

        public Worker(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("工人" + this.num + "占用一个机器在生产...");
                Thread.sleep(2000);
                System.out.println("工人" + this.num + "释放出机器");
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
```

下面的例子是官方的，使用Semaphore作为容器的设置边界。

```
public class BoundedHashSet <T> {
    private final Set<T> set;
    private final Semaphore sem;
 
    public BoundedHashSet(int bound) {
        this.set = Collections.synchronizedSet(new HashSet<T>());
        sem = new Semaphore(bound);
    }
 
    public boolean add(T o) throws InterruptedException {
        sem.acquire();
        boolean wasAdded = false;
        try {
            wasAdded = set.add(o);
            return wasAdded;
        } finally {
            if (!wasAdded)
                sem.release();
        }
    }
 
    public boolean remove(Object o) {
        boolean wasRemoved = set.remove(o);
        if (wasRemoved)
            sem.release();
        return wasRemoved;
    }
}
```

初始值为1的可以看做是一个Mutex互斥体，并具备不可重入的加锁语义（这句话的理解可以理解为向synchronized可以重入，但是这个同一个线程就不行了吗？），谁拥有这个唯一的许可，谁就拥有的互斥体。

#### 5.5.4 栅栏Barrier

闭锁是一次性的对象，一旦进入终止状态，就不能被重置。Barrier类似闭锁，它能阻塞一组线程直到某个事件发生。

栅栏与闭锁的区别：所有线程都必须同时到达栅栏才能继续执行，**闭锁用于等待事件，而栅栏用于等待其他线程**。

栅栏在反复迭代的算法中很有用，因为可以被重置。

```
public class CyclicBarrierTest {

    /**
     * 可以想象三个选手脚都绑到了一起，只有三个人都说ready准备好后才可以一起撤掉脚带，开始各自的奔跑
     * 
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException,
            InterruptedException {
        // 如果将参数改为4，但是下面只加入了3个选手，这永远等待下去
        // Waits until all parties have invoked await on this barrier.
        CyclicBarrier barrier = new CyclicBarrier(3, new TotalTask());

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(new Thread(new Runner(barrier, "1号选手")));
        executor.submit(new Thread(new Runner(barrier, "2号选手")));
        executor.submit(new Thread(new Runner(barrier, "3号选手")));

        executor.shutdown();
    }

}

class Runner implements Runnable {

    private CyclicBarrier barrier;

    private String name;

    public Runner(CyclicBarrier barrier, String name) {
        super();
        this.barrier = barrier;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000 * (new Random()).nextInt(8));
            System.out.println(name + " 准备好了...");
            // barrier的await方法，在所有参与者都已经在此 barrier 上调用 await 方法之前，将一直等待。
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(name + " 起跑！");
    }
}

/**
 * 主任务：汇总任务
 */
class TotalTask implements Runnable {

    public void run() {
        // 等到所人都准备好后，再开始
        System.out.println("=======================================");
        System.out.println("开始一起跑啦！");
    }
}
```

另一种形式的栅栏式Exchanger，它是Two-party栅栏，各方在栅栏处交换数据，当两方执行不对称的操作时，Exchanger很有用，例如当一个线程缓冲区写入数据，另外一个线程从缓冲区读数据。这些线程可以在Exchanger来回合，并将满的缓冲区和空的缓冲区交换。官方Javadoc的例子：

```
class FillAndEmpty {
   Exchanger<DataBuffer> exchanger = new Exchanger<DataBuffer>();
   DataBuffer initialEmptyBuffer = ... a made-up type
   DataBuffer initialFullBuffer = ...

   class FillingLoop implements Runnable {
     public void run() {
       DataBuffer currentBuffer = initialEmptyBuffer;
       try {
         while (currentBuffer != null) {
           addToBuffer(currentBuffer);
           if (currentBuffer.isFull())
             currentBuffer = exchanger.exchange(currentBuffer);
         }
       } catch (InterruptedException ex) { ... handle ... }
     }
   }

   class EmptyingLoop implements Runnable {
     public void run() {
       DataBuffer currentBuffer = initialFullBuffer;
       try {
         while (currentBuffer != null) {
           takeFromBuffer(currentBuffer);
           if (currentBuffer.isEmpty())
             currentBuffer = exchanger.exchange(currentBuffer);
         }
       } catch (InterruptedException ex) { ... handle ...}
     }
   }

   void start() {
     new Thread(new FillingLoop()).start();
     new Thread(new EmptyingLoop()).start();
   }
 }
```

另外还有一个草根的例子：

```
public class ThreadA implements Runnable {

  private final Exchanger<Integer> exchanger;

  private final AtomicReference<Integer> last = new AtomicReference<Integer>(
      5);

  public ThreadA(Exchanger<Integer> exchanger) {
    this.exchanger = exchanger;
  }

  public void run() {
    try {
      while (true) {
        last.set(exchanger.exchange(last.get()));
        System.out.println(" After calling exchange. Thread A has value: " + last.get());
        Thread.sleep(2000);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}

public class ThreadB implements Runnable {

  private Exchanger<Integer> exchanger;

  private final AtomicReference<Integer> last = new AtomicReference<Integer>(
      10);

  public ThreadB(Exchanger<Integer> exchanger) {
    this.exchanger = exchanger;
  }

  public void run() {
    try {
      while (true) {
        last.set(exchanger.exchange(last.get()));
        System.out.println(" After calling exchange. Thread B has value: " + last.get());
        Thread.sleep(2000);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}

public class ExchangerTest {

  public static void main(String[] args) {
    Exchanger<Integer> exchanger = new Exchanger<Integer>();
    new Thread(new ThreadA(exchanger)).start();
    new Thread(new ThreadB(exchanger)).start();
  }

}
```

会打印如下，不断的交换5、10两个数据：

```
After calling exchange. Thread B has value: 5 
After calling exchange. Thread A has value: 10 
After calling exchange. Thread B has value: 10 
After calling exchange. Thread A has value: 5 
After calling exchange. Thread A has value: 10 
After calling exchange. Thread B has value: 5 
After calling exchange. Thread B has value: 10 
After calling exchange. Thread A has value: 5 
```

### 5.6 构建高效的可伸缩的结果缓存

线程安全的版本，但是并发太差了。

```
public class Memoizer1 <A, V> implements Computable<A, V> {
    @GuardedBy("this") private final Map<A, V> cache = new HashMap<A, V>();
    private final Computable<A, V> c;
 
    public Memoizer1(Computable<A, V> c) {
        this.c = c;
    }
 
    public synchronized V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
 
 
interface Computable <A, V> {
    V compute(A arg) throws InterruptedException;
}
 
class ExpensiveFunction
        implements Computable<String, BigInteger> {
    public BigInteger compute(String arg) {
        // after deep thought...
        return new BigInteger(arg);
    }
}
```

使用封闭并发对象的方式，来解决，但是会存在重复计算的问题。

```
public class Memoizer2 <A, V> implements Computable<A, V> {
    private final Map<A, V> cache = new ConcurrentHashMap<A, V>();
    private final Computable<A, V> c;
 
    public Memoizer2(Computable<A, V> c) {
        this.c = c;
    }
 
    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
```

使用FutureTask，会极大的降低发生重复计算的概率，但还是有，因为if代码块是非原子的的“先检查再执行”操作。

```
public class Memoizer2 <A, V> implements Computable<A, V> {
    private final Map<A, V> cache = new ConcurrentHashMap<A, V>();
    private final Computable<A, V> c;
 
    public Memoizer2(Computable<A, V> c) {
        this.c = c;
    }
 
    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
```

最后正确的方式如下：

```
public class Memoizer <A, V> implements Computable<A, V> {
    private final ConcurrentMap<A, Future<V>> cache
            = new ConcurrentHashMap<A, Future<V>>();
    private final Computable<A, V> c;
 
    public Memoizer(Computable<A, V> c) {
        this.c = c;
    }
 
    public V compute(final A arg) throws InterruptedException {
        while (true) {
            Future<V> f = cache.get(arg);
            if (f == null) {
                Callable<V> eval = new Callable<V>() {
                    public V call() throws InterruptedException {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> ft = new FutureTask<V>(eval);
                f = cache.putIfAbsent(arg, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (CancellationException e) {
                cache.remove(arg, f);
            } catch (ExecutionException e) {
                throw LaunderThrowable.launderThrowable(e.getCause());
            }
        }
    }
}
```

## 前五章的小节

- 可变状态是至关重要的，所有的并发问题都归结为如何协调对并发状态的访问。可变状态越少，就越容易确保线程安全性。
- 尽量将域声明为final类型，除非他们是可变的。
- 不可变对象一定是线程安全的。不可变对象极大的降低了并发编程的复杂度，简单安全，可以任意共享而无需使用加锁或者保护性复制等机制，例如guava。
- 封装有利于管理复杂度。
- 用锁来保护每个可变变量。
- 当保护同一个不变性条件中的所有变量时，要使用同一个锁。
- 在执行复合操作期间，要持有锁。
- 如果从多个线程中访问同一个可变变量时没有同步机制，那么程序就会出现问题。



# 第二部分 结构化并发应用程序

## 第6章 任务执行

### 6.1 在线程中执行任务

#### 6.1.1 串行的执行任务

这是最经典的一个最简单的Socket server的例子，服务器的资源利用率非常低，因为单线程在等待I/O操作完成时，CPU处于空闲状态。从而阻塞了当前请求的延迟，还彻底阻止了其他等待中的请求被处理。

```
public class SingleThreadWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            Socket connection = socket.accept();
            handleRequest(connection);
        }
    }
 
    private static void handleRequest(Socket connection) {
        // request-handling logic here
    }
}
```

#### 6.1.2 显式地为任务创建线程

任务处理从主线程中分离出来，主循环可以快速等待下一个连接，提高响应性。同时任务可以并行处理了，吞吐量也提高了。

```
public class ThreadPerTaskWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                public void run() {
                    handleRequest(connection);
                }
            };
            new Thread(task).start();
        }
    }
 
    private static void handleRequest(Socket connection) {
        // request-handling logic here
    }
}
```

#### 6.1.3 无限制创建线程的不足

* 线程的生命周期开销非常高
* 资源消耗。大量的空闲线程占用内存，给GC带来压力，同时线程数量过多，竞争CPU资源开销太大。
* 稳定性。容易引起GC问题，甚至OOM。

### 6.2 Executor框架

任务就是一组逻辑工作单元（unit of work），而线程就是使任务异步执行的机制。J.U.C提供了Executor接口，它是代替Thread来做异步执行的入口，这个接口简单，却是非常灵活强大的异步任务执行框架提供了基础。提供了一种标准的方法将

* 任务的提交
* 任务的执行

解耦开来，并用Runnable（无返回时）或者Callable（有返回值）表示任务。

Executor基础生产者-消费者模式。

上面改造后的例子如下：

```
public class TaskExecutionWebServer {
    private static final int NTHREADS = 100;
    private static final Executor exec
            = Executors.newFixedThreadPool(NTHREADS);
 
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                public void run() {
                    handleRequest(connection);
                }
            };
            exec.execute(task);
        }
    }
 
    private static void handleRequest(Socket connection) {
        // request-handling logic here
    }
}
```

#### 6.2.2 执行策略

这一节主要介绍做一个Executor框架需要靠那些点？

* 在什么线程中执行任务？
* 任务按照什么顺序执行？FIFO/LIFO/优先级
* 有多少个任务可以并发执行？
* 队列中允许多少个任务等待？
* 如果系统过载了要拒绝一个任务，那么选择拒绝哪一个？如何通知客户端任务被拒绝了？
* 在执行任务过程中能不能有些别的动作before/after或者回调？

各种执行策略都是一种资源管理工具，最佳的策略取决于可用的计算资源以及对服务质量的要求。

因此每当看到

```
new Thread(runnable).start();
```

并且希望有一种灵活的执行策略的时候，请考虑使用Executor来代替Thread。

#### 6.2.3 线程池

在**线程池中执行任务**比**为每个任务分配一个线程**优势明显：

* 重用线程，减少开销。
* 延迟低，线程是等待任务到达。
* 最大化挖掘系统资源以及保证稳定性。CPU忙碌但是又不会出现线程竞争资源而耗尽内存或者失败的情况。

Executors可以看做一个工厂，的提供如下几种Executor的创建：

```
newCachedThreadPool
newFixedThreadPool
newSingleThreadExecutor
newScheduledThreadPool
```

#### 6.2.4 Executor生命周期

[JAVA 5 API](http://docs.oracle.com/javase/1.5.0/docs/api/java/util/concurrent/ExecutorService.html#method_summary)

```
public interface ExecutorService extends Executor {
 void shutdown();
 List<Runnable> shutdownNow();
 boolean isShutdown();
 boolean isTerminated();
 boolean awaitTermination(long timeout, TimeUnit unit)
 throws InterruptedException;
```

一个优雅停止的例子：

```
public class LifecycleWebServer {
    private final ExecutorService exec = Executors.newCachedThreadPool();
 
    public void start() throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (!exec.isShutdown()) {
            try {
                final Socket conn = socket.accept();
                exec.execute(new Runnable() {
                    public void run() {
                        handleRequest(conn);
                    }
                });
            } catch (RejectedExecutionException e) {
                if (!exec.isShutdown())
                    log("task submission rejected", e);
            }
        }
    }
 
    public void stop() {
        exec.shutdown();
    }
 
    private void log(String msg, Exception e) {
        Logger.getAnonymousLogger().log(Level.WARNING, msg, e);
    }
 
    void handleRequest(Socket connection) {
        Request req = readRequest(connection);
        if (isShutdownRequest(req))
            stop();
        else
            dispatchRequest(req);
    }
 
    interface Request {
    }
 
    private Request readRequest(Socket s) {
        return null;
    }
 
    private void dispatchRequest(Request r) {
    }
 
    private boolean isShutdownRequest(Request r) {
        return false;
    }
}
```

#### 6.2.5 延迟任务与周期任务

使用Timer的弊端在于如果某个任务执行时间过长，那么将破坏其他TimerTask的定时精确性。第二，TimerTask抛出异常后就会终止定时线程。

更加合理的做法是使用ScheduledThreadPoolExecutor，它是DelayQueue的应用场景。//TODO

### 6.3 找出可利用的并行性

这里的例子是串行的渲染网页的例子，包括HTML的骨架，TEXT以及IMAGE的渲染。

#### 6.3.2 携带结果的任务Callable和Future

Executor框架支持Runnable，同时也支持Callable，它将返回一个值或者抛出一个异常。

在Executor框架中，已提交但是尚未开始的任务可以取消，但是对于那些已经开始执行的任务，只有他们能响应中断时，才能取消。

Future非常实用，他的API如下：

```
boolean	cancel(boolean mayInterruptIfRunning) Attempts to cancel execution of this task.

 V	get() Waits if necessary for the computation to complete, and then retrieves its result
 
 V	get(long timeout, TimeUnit unit) Waits if necessary for at most the given time for the computation to complete, and then retrieves its result, if available.
 
 boolean	isCancelled()  Returns true if this task was cancelled before it completed normally.
 
 boolean	isDone()  Returns true if this task completed.
```

**内部get的阻塞是靠LockSupport.park来做的，在任务完成后Executor回调finishCompletion方法会依次唤醒被阻塞的线程。**

ExecutorService的submit方法接受Runnable和Callable，返回一个Future。ThreadPoolExecutor框架留了一个口子，子类可以重写newTaskFor来决定创建什么Future的实现，默认是FutureTask类。

#### 6.3.3 示例：使用Future实现页面的渲染器

```
public abstract class FutureRenderer {
    private final ExecutorService executor = Executors.newCachedThreadPool();
 
    void renderPage(CharSequence source) {
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<ImageData>> task =
                new Callable<List<ImageData>>() {
                    public List<ImageData> call() {
                        List<ImageData> result = new ArrayList<ImageData>();
                        for (ImageInfo imageInfo : imageInfos)
                            result.add(imageInfo.downloadImage());
                        return result;
                    }
                };
 
        Future<List<ImageData>> future = executor.submit(task);
        renderText(source);
 
        try {
            List<ImageData> imageData = future.get();
            for (ImageData data : imageData)
                renderImage(data);
        } catch (InterruptedException e) {
            // Re-assert the thread's interrupted status
            Thread.currentThread().interrupt();
            // We don't need the result, so cancel the task too
            future.cancel(true);
        } catch (ExecutionException e) {
            throw launderThrowable(e.getCause());
        }
    }
 
    interface ImageData {
    }
 
    interface ImageInfo {
        ImageData downloadImage();
    }
 
    abstract void renderText(CharSequence s);
 
    abstract List<ImageInfo> scanForImageInfo(CharSequence s);
 
    abstract void renderImage(ImageData i);
}
```

#### 6.3.5 CompletionService: Executor Meets BlockingQueue

计算完成后FutureTask会调用done方法，而CompletionService集成了FutureTask，对于计算完毕的结果直接放在自己维护的BlockingQueue里面，这样上层调用者就可以一个个take或者poll出来。

```
private class QueueingFuture<V> extends FutureTask<V> {
 QueueingFuture(Callable<V> c) { super(c); }
 QueueingFuture(Runnable t, V r) { super(t, r); }
 protected void done() {
 completionQueue.add(this);
 }
```

#### 6.3.6 示例：使用CompletionService提高渲染性能

```
void renderPage(CharSequence source) {
        final List<ImageInfo> info = scanForImageInfo(source);
        CompletionService<ImageData> completionService =
                new ExecutorCompletionService<ImageData>(executor);
        for (final ImageInfo imageInfo : info)
            completionService.submit(new Callable<ImageData>() {
                public ImageData call() {
                    return imageInfo.downloadImage();
                }
            });
 
        renderText(source);
 
        try {
            for (int t = 0, n = info.size(); t < n; t++) {
                Future<ImageData> f = completionService.take();
                ImageData imageData = f.get();
                renderImage(imageData);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw launderThrowable(e.getCause());
        }
    }
```

#### 6.3.7 为任务设置时限

Future的get支持timeout。

#### 6.3.8 批量提交任务

使用invokeAll方法提交`List<Callable>`，返回一个`List<Future>`



## 第7章 取消与关闭

JAVA媒体提供任务机制来安全的终止线程。但是它提供了中断（interruption），这是一种写作机制，能够使一个线程终止另外一个线程。

一般来说没人希望立即终止，因为必要时总要先清理再终止。

开发一个应用能够妥善处理失败、关闭、取消等过程非常重要也有挑战。

### 7.1 任务取消

一定不要使用Thread.stop和suspend这些机制。

一种协作机制就是“标记位”。例如使用volatile类型的field来保存取消状态。

```
@ThreadSafe
public class PrimeGenerator implements Runnable {
    private static ExecutorService exec = Executors.newCachedThreadPool();
 
    @GuardedBy("this") private final List<BigInteger> primes
            = new ArrayList<BigInteger>();
    private volatile boolean cancelled;
 
    public void run() {
        BigInteger p = BigInteger.ONE;
        while (!cancelled) {
            p = p.nextProbablePrime();
            synchronized (this) {
                primes.add(p);
            }
        }
    }
 
    public void cancel() {
        cancelled = true;
    }
 
    public synchronized List<BigInteger> get() {
        return new ArrayList<BigInteger>(primes);
    }
 
    static List<BigInteger> aSecondOfPrimes() throws InterruptedException {
        PrimeGenerator generator = new PrimeGenerator();
        exec.execute(generator);
        try {
            SECONDS.sleep(1);
        } finally {
            generator.cancel();
        }
        return generator.get();
    }
}
```

#### 7.1.1 中断

下面的例子会出现死锁，线程根本不会停止下来。

```
class BrokenPrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;
    private volatile boolean cancelled = false;
 
    BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }
 
    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!cancelled)
                queue.put(p = p.nextProbablePrime());
        } catch (InterruptedException consumed) {
        }
    }
 
    public void cancel() {
        cancelled = true;
    }
}
```

每个线程都有一个boolean类型的中断状态。当调用Thread.interrupt方法时，该值被设置为true，Thread.interruptted可以恢复中断。

阻塞库方法，例如sleep和wait、join都会检查中断，并且发现中断则提前返回，他们会清楚中断状态，并抛出InterruptedException。

但是对于其他方法interrupt仅仅是传递了中断的请求消息，并不会使线程中断，需要由线程在下一个合适的时刻中断自己。

通常，用中断是取消的最合理的实现方式。

上面的例子的改进方法就是:

```
public class PrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;
 
    PrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }
 
    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted())
                queue.put(p = p.nextProbablePrime());
        } catch (InterruptedException consumed) {
            /* Allow thread to exit */
        }
    }
 
    public void cancel() {
        interrupt();
    }
}
```

#### 7.1.2 中断策略

发生了中断，需要尽快退出执行流程，并把中断信息传递给调用者，从而使调用栈中的上层代码可以采取进一步的操作。当然任务也可以不需要放弃所有操作，可以推迟处理中断清楚，知道某个时机。

#### 7.1.3 响应中断

* 传递异常
* 回复中断状态

```
public class NoncancelableTask {
    public Task getNextTask(BlockingQueue<Task> queue) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    return queue.take();
                } catch (InterruptedException e) {
                    interrupted = true;
                    // fall through and retry
                }
            }
        } finally {
            if (interrupted)
                Thread.currentThread().interrupt();
        }
    }
 
    interface Task {
    }
}
```

#### 7.1.6 处理不可中断的阻塞

例如Socket I/O或者内置锁都不能响应中断，这时候该如何做才能终止他们呢？可以通过重写Thread.interrupt方法，例如加入close的逻辑。

### 7.2 停止基于线程的服务

#### 7.2.1 示例：日志服务

#### 7.2.2 关闭ExecutorService

#### 7.2.3 Poison Pill

例如CloseEvent机制或者POISON对象，来做特殊的识别，从而让程序自己处理停止操作，退出线程。

### 7.3 处理非正常的线程终止

### 7.4 JVM关闭



## 第8章 线程池的使用

一个很好的[ThreadPoolExecutor源码分析文档](https://my.oschina.net/xionghui/blog/494698)

ThreadPoolExecutor UML图：

![](http://neoremind.com/wp-content/uploads/2016/09/java-7-concurrent-executors-uml-class-diagram-example.png)

![](http://neoremind.com/wp-content/uploads/2016/09/java-7-concurrent-collections-uml-class-diagram-example.png)

![](http://neoremind.com/wp-content/uploads/2016/09/java-7-concurrent-future-uml-class-diagram-example.png)

### 8.1 在任务和执行策略之间隐形耦合

避免Thread starvation deadlock

### 8.2 设置线程池大小

### 8.3 配置ThreadPoolExecutor

![](http://neoremind.com/wp-content/uploads/2016/09/20111209111944_933.jpg)

构造函数如下：

```
public ThreadPoolExecutor(int corePoolSize,
 int maximumPoolSize,
 long keepAliveTime,
 TimeUnit unit,
 BlockingQueue<Runnable> workQueue,
 ThreadFactory threadFactory,
 RejectedExecutionHandler handler) { ... } 
```

- 核心和最大池大小：如果运行的线程少于 corePoolSize，则创建新线程来处理请求（即一个Runnable实例），即使其它线程是空闲的。如果运行的线程多于 corePoolSize 而少于 maximumPoolSize，则仅当队列满时才创建新线程。
- 保持活动时间：如果池中当前有多于 corePoolSize 的线程，则这些多出的线程在空闲时间超过 keepAliveTime 时将会终止。
- 排队：如果运行的线程等于或多于 corePoolSize，则 Executor 始终首选将请求加入队列BlockingQueue，而不添加新的线程。
- 被拒绝的任务：当 Executor 已经关闭，或者队列已满且线程数量达到maximumPoolSize时（即线程池饱和了），请求将被拒绝。这些拒绝的策略叫做Saturation Policy，即饱和策略。包括AbortPolicy,
  CallerRunsPolicy, DiscardPolicy, and DiscardOldestPolicy.

另外注意：

- 如果运行的线程少于 corePoolSize，ThreadPoolExecutor 会始终首选创建新的线程来处理请求；注意，这时即使有空闲线程也不会重复使用（这和数据库连接池有很大差别）。
- 如果运行的线程等于或多于 corePoolSize，则 ThreadPoolExecutor 会将请求加入队列BlockingQueue，而不添加新的线程（这和数据库连接池也不一样）。
- 如果无法将请求加入队列（比如队列已满），则创建新的线程来处理请求；但是如果创建的线程数超出 maximumPoolSize，在这种情况下，请求将被拒绝。

newCachedThreadPool使用了SynchronousQueue，并且是无界的。

线程工厂ThreadFactory

### 8.4 扩展ThreadPoolExecutor

重写beforeExecute和afterExecute方法。

### 8.5 递归算法的并行化

实际就是类似Number of Islands或者N-Queens等DFS问题的一种并行处理。

串行版本如下：

```
public class SequentialPuzzleSolver <P, M> {
    private final Puzzle<P, M> puzzle;
    private final Set<P> seen = new HashSet<P>();
 
    public SequentialPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
    }
 
    public List<M> solve() {
        P pos = puzzle.initialPosition();
        return search(new PuzzleNode<P, M>(pos, null, null));
    }
 
    private List<M> search(PuzzleNode<P, M> node) {
        if (!seen.contains(node.pos)) {
            seen.add(node.pos);
            if (puzzle.isGoal(node.pos))
                return node.asMoveList();
            for (M move : puzzle.legalMoves(node.pos)) {
                P pos = puzzle.move(node.pos, move);
                PuzzleNode<P, M> child = new PuzzleNode<P, M>(pos, move, node);
                List<M> result = search(child);
                if (result != null)
                    return result;
            }
        }
        return null;
    }
}
```

并行版本如下：

```
public class ConcurrentPuzzleSolver <P, M> {
    private final Puzzle<P, M> puzzle;
    private final ExecutorService exec;
    private final ConcurrentMap<P, Boolean> seen;
    protected final ValueLatch<PuzzleNode<P, M>> solution = new ValueLatch<PuzzleNode<P, M>>();
 
    public ConcurrentPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
        this.exec = initThreadPool();
        this.seen = new ConcurrentHashMap<P, Boolean>();
        if (exec instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) exec;
            tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        }
    }
 
    private ExecutorService initThreadPool() {
        return Executors.newCachedThreadPool();
    }
 
    public List<M> solve() throws InterruptedException {
        try {
            P p = puzzle.initialPosition();
            exec.execute(newTask(p, null, null));
            // block until solution found
            PuzzleNode<P, M> solnPuzzleNode = solution.getValue();
            return (solnPuzzleNode == null) ? null : solnPuzzleNode.asMoveList();
        } finally {
            exec.shutdown();
        }
    }
 
    protected Runnable newTask(P p, M m, PuzzleNode<P, M> n) {
        return new SolverTask(p, m, n);
    }
 
    protected class SolverTask extends PuzzleNode<P, M> implements Runnable {
        SolverTask(P pos, M move, PuzzleNode<P, M> prev) {
            super(pos, move, prev);
        }
 
        public void run() {
            if (solution.isSet()
                    || seen.putIfAbsent(pos, true) != null)
                return; // already solved or seen this position
            if (puzzle.isGoal(pos))
                solution.setValue(this);
            else
                for (M m : puzzle.legalMoves(pos))
                    exec.execute(newTask(puzzle.move(pos, m), m, this));
        }
    }
}
```



## 第9章 图形用户界面应用程序

略








## 第13章 显式锁 Explicit Locks 

Java5.0之前只能用synchronized和volatile，5.0后Doug Lea加入了ReentrantLock，并不是替代内置锁，而是当内置锁机制不适用时，作为一种可选择的高级功能。

### 13.1 Lock和ReentrantLock

Lock接口定义：

```
public interface Lock {
 void lock();
 void lockInterruptibly() throws InterruptedException;
 boolean tryLock();
 boolean tryLock(long timeout, TimeUnit unit)
 throws InterruptedException;
 void unlock();
 Condition newCondition();
}
```

使用范例模板：

```
Lock lock = new ReentrantLock();
...
lock.lock();
try {
 // update object state
 // catch exceptions and restore invariants if necessary
} finally {
 lock.unlock();
} 
```

之所以叫做RentrantLock，可理解为两部：

1）Rentrant，可重入，lock多少次都没关系，只需要unlock即可，或者lock里面嵌套了别的lock都可以。

2）Lock，提供了和synchronized一样的互斥性和内存可见性，与synchronized的monitor内存语义一样。

与synchronized的区别在于

* 可定时锁等待
* 可轮询锁等待
* 可中断锁等待
* 公平性
* 实现非块结构的加锁
* 绑定多个Condition。通过多次newCondition可以获得多个Condition对象,可以简单的实现比较复杂的线程同步的功能.通过await(),signal();

下面依次讲解：

轮询锁和定时锁，内置锁的死锁问题只能通过重启程序解决，那么可定时和可轮询的锁提供了另一种选择。

```
public class DeadlockAvoidance {
    private static Random rnd = new Random();
 
    public boolean transferMoney(Account fromAcct,
                                 Account toAcct,
                                 DollarAmount amount,
                                 long timeout,
                                 TimeUnit unit)
            throws InsufficientFundsException, InterruptedException {
        long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
        long randMod = getRandomDelayModulusNanos(timeout, unit);
        long stopTime = System.nanoTime() + unit.toNanos(timeout);
 
        while (true) {
            if (fromAcct.lock.tryLock()) {
                try {
                    if (toAcct.lock.tryLock()) {
                        try {
                            if (fromAcct.getBalance().compareTo(amount) < 0)
                                throw new InsufficientFundsException();
                            else {
                                fromAcct.debit(amount);
                                toAcct.credit(amount);
                                return true;
                            }
                        } finally {
                            toAcct.lock.unlock();
                        }
                    }
                } finally {
                    fromAcct.lock.unlock();
                }
            }
            if (System.nanoTime() < stopTime)
                return false;
            NANOSECONDS.sleep(fixedDelay + rnd.nextLong() % randMod);
        }
    }
 
    private static final int DELAY_FIXED = 1;
    private static final int DELAY_RANDOM = 2;
 
    static long getFixedDelayComponentNanos(long timeout, TimeUnit unit) {
        return DELAY_FIXED;
    }
 
    static long getRandomDelayModulusNanos(long timeout, TimeUnit unit) {
        return DELAY_RANDOM;
    }
 
    static class DollarAmount implements Comparable<DollarAmount> {
        public int compareTo(DollarAmount other) {
            return 0;
        }
 
        DollarAmount(int dollars) {
        }
    }
 
    class Account {
        public Lock lock;
 
        void debit(DollarAmount d) {
        }
 
        void credit(DollarAmount d) {
        }
 
        DollarAmount getBalance() {
            return null;
        }
    }
 
    class InsufficientFundsException extends Exception {
    }
}
```

带有时间限制的锁如下：

```
public class TimedLocking {
    private Lock lock = new ReentrantLock();
 
    public boolean trySendOnSharedLine(String message,
                                       long timeout, TimeUnit unit)
            throws InterruptedException {
        long nanosToLock = unit.toNanos(timeout)
                - estimatedNanosToSend(message);
        if (!lock.tryLock(nanosToLock, NANOSECONDS))
            return false;
        try {
            return sendOnSharedLine(message);
        } finally {
            lock.unlock();
        }
    }
 
    private boolean sendOnSharedLine(String message) {
        /* send something */
        return true;
    }
 
    long estimatedNanosToSend(String message) {
        return message.length();
    }
}
```

可中断的锁：

```
public class InterruptibleLocking {
    private Lock lock = new ReentrantLock();
 
    public boolean sendOnSharedLine(String message)
            throws InterruptedException {
        lock.lockInterruptibly();
        try {
            return cancellableSendOnSharedLine(message);
        } finally {
            lock.unlock();
        }
    }
 
    private boolean cancellableSendOnSharedLine(String message) throws InterruptedException {
        /* send something */
        return true;
    }
 
}
```

非结构块的加锁就不用说了。

另外，关于Condition的例子，最典型的就是阻塞的有界队列的实现。

```
public class BoundedBuffer {

    private static final Logger logger = LoggerFactory.getLogger(BoundedBuffer.class);

    final Lock lock = new ReentrantLock();

    final Condition notFull = lock.newCondition();

    final Condition notEmpty = lock.newCondition();

    final Object[] items = new Object[2]; // 阻塞队列

    int putptr, takeptr, count;
    
    private void log(String info) {
        logger.info(Thread.currentThread().getName() + " - " + info);
    }

    public void put(Object x) throws InterruptedException {
        log(x + ",执行put");
        lock.lock();
        log(x + ",put lock.lock()");
        try {
            while (count == items.length) { // 如果队列满了，notFull就一直等待
                log(x + ",put notFull.await() 队列满了");
                notFull.await(); // 调用await的意思取反，及not notFull -> Full
            }
            items[putptr] = x; // 终于可以插入队列
            if (++putptr == items.length) {
                putptr = 0; // 如果下标到达数组边界，循环下标置为0
            }
            ++count;
            log(x + ",put成功 notEmpty.signal() 周知队列不为空了");
            notEmpty.signal(); // 唤醒notEmpty
        } finally {
            log(x + ",put lock.unlock()");
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        log("执行take");
        lock.lock();
        Object x = null;
        log("take lock.lock()");
        try {
            while (count == 0) {
                log("take notEmpty.await() 队列为空等等");
                notEmpty.await();
            }
            x = items[takeptr];
            if (++takeptr == items.length) {
                takeptr = 0;
            }
            --count;
            log(x + ",take成功 notFull.signal() 周知队列有剩余空间了");
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
            log(x + ",take lock.unlock()");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final BoundedBuffer bb = new BoundedBuffer();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (char i = 'A'; i < 'F'; i++) {
            final char t = i;
            executor.execute(() -> {
                try {
                    bb.put(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        List<Character> res = new LinkedList<>();
        for (char i = 'A'; i < 'F'; i++) {
            executor.execute(() -> {
                try {
                    char c = (char) bb.take();
                    res.add(c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        logger.info(res.toString());
        executor.shutdownNow();
    }
}
```

### 13.2 性能考虑因素

Java5.0的时候J.U.C的ReentrantLock锁竞争性能非常好，到了Java6.0使用了改进后的算法来管理内置锁，所以现在差不太多了，只好一点点。

竞争性能的影响可伸缩性的关键要素：如果有越多的资源被耗费在锁的管理和线程调度上，那么应用程序得到的资源就越少，锁的实现方式越好，将需要越少的系统调用和上下文切换。

### 13.3 公平性

默认ReentrantLock创建的事非公平的锁，这个非公平是指被阻塞挂起的线程（使用LockSupport.park）都在AQS（下一节会讲）的CLH队列中排队等待自己被唤醒，他们是按照发出的请求顺序来排队的，但是一旦有一个唤醒的就会和新来的线程来竞争锁，新来的可能会“插队”，如果新来的成功获取锁，那么它将跳过所有等待线程而开始执行，这也就是以为着本该被唤醒的线程失败了，对不起您回到队列的尾部继续等。这就是非公平性。

一般，非公平锁的性能要好于公平锁。原因在于一个线程被唤醒是需要时间的，这个空隙如果有其他线程处于ready状态，不需要上下文切换，那么直接运行就行，这个“双赢”的局面，是提高吞吐量的原因。

### 13.4 在synchronized和ReentrantLock之间进行选择

除非使用到13.1提到的高级特性，或者内置锁无法满足需求时，否则还是老实用内置锁，毕竟是JVM自身提供的，而不是靠类库，因此可能会执行一些优化。

### 13.5 读-写锁

//TODO



## 第14章 构建自定义的同步工具 Building Custom Synchronizers



