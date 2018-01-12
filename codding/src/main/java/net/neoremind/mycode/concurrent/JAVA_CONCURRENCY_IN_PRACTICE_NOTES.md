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
* 其他情况，Thread.interrupt()仅仅只是更新了status标志位。然后你的工作线程通过Thread.isInterrrupted()进行检查，可以做相应的处理，比如也throw InterruptedException或者是清理状态，任务cancel， 数据清理等。 

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


先说两个概念：锁池和等待池

锁池:

假设线程A已经拥有了某个对象(注意:不是类)的锁，而其它的线程想要调用这个对象的某个synchronized方法(或者synchronized块)，由于这些线程在进入对象的synchronized方法之前必须先获得该对象的锁的拥有权，但是该对象的锁目前正被线程A拥有，所以这些线程就进入了该对象的锁池中。

等待池:

假设一个线程A调用了某个对象的wait()方法，线程A就会释放该对象的锁后，进入到了该对象的等待池中

然后再来说notify和notifyAll的区别 

如果线程调用了对象的 wait()方法，那么线程便会处于该对象的等待池中，等待池中的线程不会去竞争该对象的锁。当有线程调用了对象的 notifyAll()方法（唤醒所有 wait 线程）或 notify()方法（只随机唤醒一个 wait 线程），被唤醒的的线程便会进入该对象的锁池中，锁池中的线程会去竞争该对象锁。
也就是说，调用了notify后只要一个线程会由等待池进入锁池，而notifyAll会将该对象等待池内的所有线程移动到锁池中，等待锁竞争优先级高的线程竞争到对象锁的概率大，假若某线程没有竞争到该对象锁，它还会留在锁池中，唯有线程再次调用 wait()方法，它才会重新回到等待池中。而竞争到对象锁的线程则继续往下执行，直到执行完了 synchronized 代码块，它会释放掉该对象锁，这时锁池中的线程会继续竞争该对象锁。

综上，所谓唤醒线程，另一种解释可以说是将线程由等待池移动到锁池，notifyAll调用后，会将全部线程由等待池移到锁池，然后参与锁的竞争，竞争成功则继续执行，如果不成功则留在锁池等待锁被释放后再次参与竞争。而notify只会唤醒一个线程。有了这些理论基础，后面的notify可能会导致死锁，而notifyAll则不会的例子也就好解释了

主要的效果区别是notify用得不好容易导致死锁，例如下面提到的例子。

在书里的PAGE 247相关有详细的说明，实际上这是一种信号丢失，是一种被劫持的信号。粗粒度的wait、notify容易，在不同的事情上，比如"满了"，或者"空了"上分别的做条件队列，混合使用，用notifyAll要安全些。

```
public final ArrayList<Object> buf = new ArrayList<Object>();

public final static int MAX_SIZE = 100;

public synchronized void put(Object o) throws InterruptedException
{
	while (buf.size() == MAX_SIZE)
	{
		wait(); // called if the buffer is full (try/catch removed for
				// brevity)
	}
	buf.add(o);
	notify(); // called in case there are any getters or putters waiting
}

public synchronized Object get() throws InterruptedException
{

	// Y: this is where C2 tries to acquire the lock (i.e. at the beginning
	// of the method)
	while (buf.size() == 0)
	{
		wait(); // called if the buffer is empty (try/catch removed for
				// brevity)
				// X: this is where C1 tries to re-acquire the lock (see
				// below)
	}
	Object o = buf.remove(0);
	notify(); // called if there are any getters or putters waiting
	return o;

}

生产者、消费者数量都为2，缓冲区为1，当按照下列并发顺序执行的时候出现死锁：

1.消费者1获得锁，发现缓冲区为0，wait（wait自动释放锁）；

2.消费者2获得锁，发现缓冲区为0，wait（wait自动释放锁）；

3.生产者1获得锁，发现缓冲区为0可以生产，生产以后放在缓冲区，notify，现在缓冲区为1；

4.第3步notify唤醒了消费者1，但是消费者1没有抢到锁，锁被生产者2拿到了；

5.生产者2发现缓冲区为1（因为只是唤醒了消费者1，但是消费者1没有抢到锁，没法消费），wait（wait自动释放锁）；

6.现在消费者1获得了锁，消费并且notify（此时生产者2和消费者2都处于wait状态），缓冲区为0；

7.很不幸，第6步notify唤醒了消费者2，消费者2醒来后拿到锁发现缓冲区为0，接着wait，与此同时，生产者2也在wait，死锁产生。

可以看出问题的关键在于两个地方，一个是第4步notify并不能保证notify唤醒的线程获得锁，一个是第7步notify可能会唤醒同一种角色的线程。

可以用Lock/Condition解决，两个Condition可以保证notify（signal）不同角色的线程，也可以用notifyAll解决，使线程间变成对锁的竞争。
```

自己的理解总结：

当多线程走sync，就会有线程被blocked，进入锁池，此时不会占用CPU，因为根本不是runnable状态，
只有一个运行中的，退出了，才由JVM（重点是JVM哦）选择让大家竞争锁，胜出的才进入runnable状态。

进入runnable状态的线程和linux的LWP轻量级线程一对一，所以才有可能被OS scheduling分片的执行的机会，
退出后再返回锁，周而复始的进入新的锁竞争阶段。

wait就不同了，他是主动让出了锁，并且等等别人唤醒它，此时这些线程肯定也不是runnable状态，不会占用CPU
都进入了等待池，notify由JVM随机挑一个进入锁池，notifyAll由JVM把所有等待的都放入锁池，让你们竞争去吧，
但是最终只会有一个成功，这也就是为什么wait和notify都必须放在synchronized块里面，否则编译器都报错。

试想一下notifyAll把所有等待池里面的线程，放到了blocked状态，也就是锁池，大家还是需要一次走一次独木桥才可以进入等待池，
这是多大的一笔开销啊，频繁的notifyAll，有很多线程阻塞确实会有很多性能损失。

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

  刚刚明明说了线程可以提高性能，但是怎么又降低了？降低的现象可能是服务响应时间长、吞吐量低、资源消耗过多，伸缩性（见第三部分）较差。

  这是因为线程本身存在运行开销，挂起、唤醒线程需要频繁的上下文切换（context switch），CPU花了不少时间在线程调度上而不是线程运行上，因此共享数据并为了安全，使用同步机制的时候，会抑制某些编译器优化，使内存缓冲区数据无效，以及增加内存总线的同步数量等等。这些额外的开销都属于性能问题。

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

1）如果输出是0，那么出现了指令重排序，read=true先执行，然后number=42后执行，因为可见性问题，reader读不到最新的42，而是默认的0。根据Javadoc虽然yield能让出CPU，但这个JNI的调用完全没有任何保障，所以你等不到number赋值为42了也是有可能的。

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
* 不会被缓存在寄存器中（只有拥有线程可见）或者其他对CPU不可见的地方，每次总是从主存中读取volatile变量的结果。也就是在happens-before法则中，对一个valatile变量的写操作后，其后的任何读操作立即可见此写操作的结果。

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

不可变对象很好的一个例子，这在guava类库中非常常见。

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

例如可以通过封装ConcurrentMap来达到线程安全的目的。这要确保委托是正确的足以保护类的安全。

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
```

非常经典的例子，本来是想做到安全，但是不是用的同一把锁。正确的如下：

```
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

get操作的高效之处在于不需要加锁,原因是它的get方法里将要使用的共享变量都定义成 volatile,如用于统计当前Segement大小的count字段和用于存储值的HashEntry的 value。定义成volatile的变量,能够在线程之间保持可见性,能够被多线程同时读,并且保证不会读到过期的值,但是只能被单线程写(有一种情况 可以被多线程写,就是写入的值不依赖于原值),在get 操作里只需要读不需要写共享变量count和value,所以可以不用加锁。之所以不会读到过期的值,是根据JMM内存模型的 happens-before原则,对volatile字段的写入操作先于读操作,即使两个线程同时修改和获取 volatile变量,get操作也能拿到最新的值,这是用volatile替换锁的经典应用场景。

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

putIfAbsen解决了如下的原子性问题：

```
ConcurrentMap<String,String> map = new ConcurrentHashMap<String,String>();

if(!map.containsKey(key)){
    map.put(key,value);
}
```

从Java 8开始，HashMap，ConcurrentHashMap和LinkedHashMap在处理频繁冲突时将使用平衡树来代替链表，当同一hash桶中的元素数量超过特定的值便会由链表切换到平衡树，这会将get()方法的性能从O(n)提高到O(logn)。

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

FutureTask通过Callable实现，同时也实现了Runnable接口，相当于一个可生成结果的Runnable，如果任务完成get立即返回结果，否则阻塞直到任务进行完成状态。

FutureTask的以上功能却是依靠通过一个叫AbstractQueuedSynchronizer的类来实现，至少在JDK 1.5、JDK1.6版本是这样的（从1.7开始FutureTask已经被其作者Doug Lea修改为不再依赖AbstractQueuedSynchronizer实现了，这是JDK1.7的变化之一）。

一定要看看FutureTask的源代码，内部使用WaitNode记录所有调用get()被阻塞的线程，run方法实际的实现简单如下：

```
run() {
  try {
     result = callable.call()
  } catch (Throwable t) {
     result = t;
  }
  if (UNSAFE.compareAndSwapInt(this, stateOffset, NEW, COMPLETING)) {  //原子操作设置状态。
      outcome = result;
      UNSAFE.putOrderedInt(this, stateOffset, NORMAL); // final state
      for (WaitNode q; (q = waiters) != null;) {
            for (;;) {
               Thread t = q.thread;
               LockSupport.unpark(t);
               WaitNode next = q.next;
               if (next == null)
                  break;
            }
            break;
      }
  }
}
```

get()阻塞方法如下：

```
if (s <= COMPLETING)
    s = waiters queue append this waitNode and LockSupport.parkNanos(this, nanos);
return report(s);

 private V report(int s) throws ExecutionException {
        Object x = outcome;
        if (s == NORMAL)
            return (V)x;  //正常返回
        if (s >= CANCELLED)
            throw new CancellationException();
        throw new ExecutionException((Throwable)x);  错误返回经过封装的异常
    }
```

圆规正传：

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



# 第三部分 Liveness, Performance, and Testing

## 第10章 避免活跃性危险

### 10.1 死锁

哲学家问题

有环

A等B，B等A

数据库往往可以检测和解决死锁//TODO

JVM不行，一旦死锁只有停止重启。

下面分别介绍了几种典型的死锁情况：

#### 10.1.1 Lock ordering Deadlocks

下面是一个经典的锁顺序死锁：两个线程用不同的顺序来获得相同的锁，**如果按照锁的请求顺序来请求锁，就不会发生这种循环依赖的情况。**

```
public class LeftRightDeadlock {
    private final Object left = new Object();
    private final Object right = new Object();
 
    public void leftRight() {
        synchronized (left) {
            synchronized (right) {
                doSomething();
            }
        }
    }
 
    public void rightLeft() {
        synchronized (right) {
            synchronized (left) {
                doSomethingElse();
            }
        }
    }
 
    void doSomething() {
    }
 
    void doSomethingElse() {
    }
}
```

#### 10.1.1 Dynamic Lock Order Deadlocks

下面的转账例子，如果一个线程X向Y转，而另外一个线程Y向X也转，那么就会发生死锁。

```
public class DynamicOrderDeadlock {
    // Warning: deadlock-prone!
    public static void transferMoney(Account fromAccount,
                                     Account toAccount,
                                     DollarAmount amount)
            throws InsufficientFundsException {
        synchronized (fromAccount) {
            synchronized (toAccount) {
                if (fromAccount.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                }
            }
        }
    }
 
    static class DollarAmount implements Comparable<DollarAmount> {
        // Needs implementation
 
        public DollarAmount(int amount) {
        }
 
        public DollarAmount add(DollarAmount d) {
            return null;
        }
 
        public DollarAmount subtract(DollarAmount d) {
            return null;
        }
 
        public int compareTo(DollarAmount dollarAmount) {
            return 0;
        }
    }
 
    static class Account {
        private DollarAmount balance;
        private final int acctNo;
        private static final AtomicInteger sequence = new AtomicInteger();
 
        public Account() {
            acctNo = sequence.incrementAndGet();
        }
 
        void debit(DollarAmount d) {
            balance = balance.subtract(d);
        }
 
        void credit(DollarAmount d) {
            balance = balance.add(d);
        }
 
        DollarAmount getBalance() {
            return balance;
        }
 
        int getAcctNo() {
            return acctNo;
        }
    }
 
    static class InsufficientFundsException extends Exception {
    }
}
```

解决办法还是顺序话锁，考虑针对两种情况取hashcode然后判断if-else里面决定锁顺序。

```
class Helper {
            public void transfer() throws InsufficientFundsException {
                if (fromAcct.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else {
                    fromAcct.debit(amount);
                    toAcct.credit(amount);
                }
            }
        }
        int fromHash = System.identityHashCode(fromAcct);
        int toHash = System.identityHashCode(toAcct);
 
        if (fromHash < toHash) {
            synchronized (fromAcct) {
                synchronized (toAcct) {
                    new Helper().transfer();
                }
            }
        } else if (fromHash > toHash) {
            synchronized (toAcct) {
                synchronized (fromAcct) {
                    new Helper().transfer();
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized (fromAcct) {
                    synchronized (toAcct) {
                        new Helper().transfer();
                    }
                }
            }
        }
```

#### 10.1.3 在协作对象之间发生死锁Deadlocks Between Cooperating Objects

下面的例子setLocation和getImage都会获取两把锁，会存在两个线程按照不同的顺序获取锁的情况。

```
public class CooperatingDeadlock {
    // Warning: deadlock-prone!
    class Taxi {
        @GuardedBy("this") private Point location, destination;
        private final Dispatcher dispatcher;
 
        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }
 
        public synchronized Point getLocation() {
            return location;
        }
 
        public synchronized void setLocation(Point location) {
            this.location = location;
            if (location.equals(destination))
                dispatcher.notifyAvailable(this);
        }
 
        public synchronized Point getDestination() {
            return destination;
        }
 
        public synchronized void setDestination(Point destination) {
            this.destination = destination;
        }
    }
 
    class Dispatcher {
        @GuardedBy("this") private final Set<Taxi> taxis;
        @GuardedBy("this") private final Set<Taxi> availableTaxis;
 
        public Dispatcher() {
            taxis = new HashSet<Taxi>();
            availableTaxis = new HashSet<Taxi>();
        }
 
        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }
 
        public synchronized Image getImage() {
            Image image = new Image();
            for (Taxi t : taxis)
                image.drawMarker(t.getLocation());
            return image;
        }
    }
 
    class Image {
        public void drawMarker(Point p) {
        }
    }
}
```

#### 10.1.4 开放调用

减小锁的力度，锁不嵌套。

```
class CooperatingNoDeadlock {
    @ThreadSafe
    class Taxi {
        @GuardedBy("this") private Point location, destination;
        private final Dispatcher dispatcher;
 
        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }
 
        public synchronized Point getLocation() {
            return location;
        }
 
        public synchronized void setLocation(Point location) {
            boolean reachedDestination;
            synchronized (this) {
                this.location = location;
                reachedDestination = location.equals(destination);
            }
            if (reachedDestination)
                dispatcher.notifyAvailable(this);
        }
 
        public synchronized Point getDestination() {
            return destination;
        }
 
        public synchronized void setDestination(Point destination) {
            this.destination = destination;
        }
    }
 
    @ThreadSafe
    class Dispatcher {
        @GuardedBy("this") private final Set<Taxi> taxis;
        @GuardedBy("this") private final Set<Taxi> availableTaxis;
 
        public Dispatcher() {
            taxis = new HashSet<Taxi>();
            availableTaxis = new HashSet<Taxi>();
        }
 
        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }
 
        public Image getImage() {
            Set<Taxi> copy;
            synchronized (this) {
                copy = new HashSet<Taxi>(taxis);
            }
            Image image = new Image();
            for (Taxi t : copy)
                image.drawMarker(t.getLocation());
            return image;
        }
    }
 
    class Image {
        public void drawMarker(Point p) {
        }
    }
 
}
```

#### 1.0.15 资源死锁

* 数据库连接池，A持有数据库D1连接，等待与D2连接，B持有D2的连接，等待与D1连接。
* 线程饥饿死锁，如8.1.1小节的例子。

### 10.2 死锁的避免与诊断

#### 10.2.1 支持定时的锁

tryLock

#### 10.2.2 kill -3 发信号给JVM dump线程

### 10.3 其他活跃性危险

#### 10.3.1 饥饿

#### 10.3.3 活锁Livelock

他不会阻塞线程，但是也不能继续执行，因为线程在不断的重复执行相同的操作，而且总会失败。

例如处理事务消，回滚后再次重新把任务放在队头。

又例如发送数据包，都选择1s后重试，那么总会冲突，所以可以考虑一个随机数时间间隔。



## 第11章 性能与可伸缩性Performance and Scalability

线程可以充分发挥系统的处理能力，提高资源利用率。同时现有的线程可以提升系统响应性。

但是在安全性与极限性能上，我们首先需要保证的是安全性。

### 11.1 对性能的思考

提升性能=用更少的资源做更多的事情（太对了，这才是问题的本质）。

资源包括：CPU时钟周期，内存，网络带宽，I/O带宽，数据请求，磁盘空间等。

资源密集型说的就是对上述维度敏感的应用。

与单线程相比，多线程总会一起一些额外的性能开销：

* 线程协调with coordinating between threads (locking, signaling, and memory synchronization)
* 上下文切换increased context switching
* 线程创建和销毁thread creation and teardown
* 线程调度scheduling overhead

可伸缩性是指：增加资源，程序的吞吐可以成比例的增加。

性能的提高往往是一个权衡的过程，需要考虑诸多因素。

### 11.2 Amdahl定律 Amdahl's Law

收割可以靠并行提高性能，而作物生长则不行。这是一个很简单的自然界的问题，在计算机界也存在，需要对问题进行合理的分解，发现潜在的并行能力。

Amdahl定律：[并行计算](https://zh.wikipedia.org/wiki/%E5%B9%B6%E8%A1%8C%E8%AE%A1%E7%AE%97)中的**加速比**是用并行前的执行速度和并行后的执行速度之比来表示的，它表示了在并行化之后的效率提升情况。

speedup <= 1 /  F + (1 - F) /N 

F表示被串行化的部分，N表示处理器数量。

如果N无穷大，那么最大的加速比例是1/F。理论上如果50%是串行的，那么最大的加速比只能是2。如果10%串行。那么最大加速比接近10，如果N=10也就是说有10个处理器资源，那么最高的加速比是5.4，在100个处理器的情况下是9.2。

但是任何程序都存在串行部分，例如从队列中take数据，访问数据库的操作等，这是绝对的。

书中举了一个例子是Synchronized linkedlist和ConcurrentLinkedQueue的吞吐率对比，在处理器数量到达上限后，他们的吞吐都基本是一条持平的线，但是Synchronized linkedlist吞吐率更低，在处理器较少的情况下就到达了极限，这主要受context switch的限制。

### 11.3 线程引入的开销

单线程不存在线程调度，也不存在同步开销，不需要使用锁来保证安全一致性。而多线程这些都需要考虑。

#### 11.3.1 上下文切换

操作系统的设计者巧妙地利用了时间片轮转的方式, CPU给每个任务都服务一定的时间, 然后把当前任务的状态保存下来, 在加载下一任务的状态后, 继续服务下一任务. 如果可运行的线程数大于CPU数量，那么OS会最终将某个正在运行的线程调度出来，从而让其他线程能够使用CPU，这会导致一次上下文切换，主要包括当前线程“保存现场”，并且新调度出来的线程需要“恢复现场“。这里的context switch直接消耗包括: CPU寄存器需要保存和加载, 系统调度器的代码需要执行, TLB实例需要重新加载, CPU 的pipeline需要刷掉; 间接消耗指的是多核的cache之间得共享数据, 间接消耗对于程序的影响要看线程工作区操作数据的大小). 

JVM和OS消耗的CPU时钟周期越少，那么APP可用的CPU时钟周期就越多。

往往OS有一个最小的执行时间，防止过于频繁的上下文切换。

JVM会因为阻塞比如锁、阻塞I/O而挂起线程，如果频繁的阻塞，就会无法使用完整的调度时间片。//?

如果可运行的线程数大于CPU的内核数，那么OS会根据一定的调度算法，强行切换正在运行的线程，从而使其它线程能够使用CPU周期。

切换线程会导致上下文切换。线程的调度会导致CPU需要在操作系统和进程间花费更多的时间片段，这样真正执行应用程序的时间就减少了。另外上下文切换也会导致缓存的频繁进出，对于一个刚被切换的线程来说，可能由于高速缓冲中没有数据而变得更慢，从而导致更多的IO开销。

`vmstat`	命令可以看cs这一个字段看上下文切换的数据。

#### 11.3.2 内存同步

同步的性能开销包括多个方面。在synchronized和volatile提供的可见性保证中会使用一些特殊指令，即内存栅栏（memory barrier），内存栅栏可以刷新缓存，满足可见性，但是它也会抑制一些编译器优化，例如不能指令重排序。

现代的JVM对于无竞争的synchronized的消耗非常小，基本微乎其微。

同时现代的JVM编译优化做的非常成熟，一些不必要的同步开销往往可以优化掉。例如，下面的代码会去掉锁获取。

```
synchronized (new Object()) {
 // do something
} 
```

还有一些比如escape analysis会找出不会发布到堆上的本地对象，锁的获取和释放会被优化为最小的次数甚至去掉。例如下面的操作。

```
public String getStoogeNames() {
 List<String> stooges = new Vector<String>();
 stooges.add("Moe");
 stooges.add("Larry");
 stooges.add("Curly");
 return stooges.toString();
} 
```

当然即使不escape，也会有lock coarsening过程，将临近的同步代码块使用同一个锁合并起来。这都减少了同步的开销。

所以不必过度担心非竞争同步带来的开销，这个基本的机制已经非常的快了，而且JVM还有能进行额外的优化以进一步降低或者消除开销的本领。

不同线程间要进行数据同步，synchronized以及volatile提供的可见性都会导致缓存失效。线程栈之间的数据要和主存进行同步，这些同步有一些小小的开销。如果线程间同时要进行数据同步，那么这些同步的线程可能都会受阻。

#### 11.3.3 阻塞

竞争的同步需要OS介入，从而增加了开销。当在锁上发生竞争时，失败者线程会被阻塞，JVM在实现发现阻塞的行为时，可以采用

* 自旋等待 spin-waiting
* 或者OS挂起被阻塞的线程

这两种的效率高低取决于上下文切换的开销以及成功获取锁之前的等待时间，如果等待时间较短，则spin-waiting，如果较长则挂起。

一个线程被阻塞会产生上下文切换的影响，但是它到底何时执行这是由OS决定的，靠时间分片机制，这个调度的策略是OS解决的，而JVM的scheduler解决的是阻塞释放锁之后哪个线程需要被select出来执行，也就是转到runnable状态。

There is no single Java Virtual Machine; JVM is a specification, and there are multiple implementations of it, including the OpenJDK version and the Sun version of it, among others. I don't know for certain, but I would guess that any reasonable JVM would simply use the underlying threading mechanism provided by the OS, which would imply POSIX Threads (pthreads) on UNIX (Mac OS X, Linux, etc.) and would imply WIN32 threads on Windows. Typically, those systems use a round-robin strategy by default. Many types of algorithms exist like **preemptive** and **time slicing**with **round robin** etc. 

The JVM is based on **preemptive and priority based** scheduling algorithm to select thread to run.

每个Java线程一对一映射到Solaris平台上的一个本地线程上，并将线程调度交由本地线程的调度程序。由于Java线程是与本地线程是一对一地绑在一起的，所以改变Java线程的优先权也不会有可靠地运行结果。

对于类Unix系统而言，一般都是进程作为任务的调度单位，也即是操作系统调度器，只会针对进程来分配CPU等资源。由于进程彼此独立，相互不可进行直接访问，这增加了应用的通信成本。所以后面有了微进程，微进程与进程不同的是，允许一定程度上，彼此可以直接进行访问，详细可参考[LinuxThreads](http://en.wikipedia.org/wiki/LinuxThreads)。JVM在一些类Unix平台下，就是将线程映射到操作系统的微进程，来实现线程调度。这样多线程能够直接被系统调度器进行调度，与此对应的就是其线程的创建和销毁的成本就比较高，而且JVM的线程优先级很难进行匹配，无法提供确切的保证，仅仅是个hint。

当发生锁竞争时，失败的线程会导致阻塞。通常阻塞的线程可能在JVM内部进行自旋等待，或者被操作系统挂起。自旋等待可能会导致更多的CPU切片浪费，而操作系统挂起则会导致更多的上下文切换。

### 11.4 减少锁的竞争

减少锁的竞争能够提高性能和可伸缩性。

在并发程序中，对可伸缩性的最主要的威胁就是独占方式的资源锁。

有三种方式可以减低锁的竞争程度：

* 减少锁的持有时间
* 降低锁的请求频率
* 使用带有协调机制的独占锁，这些机器允许更好的并发性。//?

#### 11.4.1 缩小锁的范围（快进快出）

原理就是Amdah定律，串行的代码总量减少了。

#### 11.4.2 减小锁的粒度

这种方式就是降低线程请求锁的频率，通过锁分解来实现。

下面的应用明显锁的粒度太粗了。

```
public class ServerStatusBeforeSplit {
    @GuardedBy("this") public final Set<String> users;
    @GuardedBy("this") public final Set<String> queries;
 
    public ServerStatusBeforeSplit() {
        users = new HashSet<String>();
        queries = new HashSet<String>();
    }
 
    public synchronized void addUser(String u) {
        users.add(u);
    }
 
    public synchronized void addQuery(String q) {
        queries.add(q);
    }
 
    public synchronized void removeUser(String u) {
        users.remove(u);
    }
 
    public synchronized void removeQuery(String q) {
        queries.remove(q);
    }
}
```

锁分解就是独立的变量独立分配锁，不适用全局锁。优化后如下：

```
public class ServerStatusAfterSplit {
    @GuardedBy("users") public final Set<String> users;
    @GuardedBy("queries") public final Set<String> queries;
 
    public ServerStatusAfterSplit() {
        users = new HashSet<String>();
        queries = new HashSet<String>();
    }
 
    public void addUser(String u) {
        synchronized (users) {
            users.add(u);
        }
    }
 
    public void addQuery(String q) {
        synchronized (queries) {
            queries.add(q);
        }
    }
 
    public void removeUser(String u) {
        synchronized (users) {
            users.remove(u);
        }
    }
 
    public void removeQuery(String q) {
        synchronized (users) {
            queries.remove(q);
        }
    }
}
```

#### 11.4.3 锁分段

最典型的例子就是ConcurrentHashMap。

```
public class StripedMap {
    // Synchronization policy: buckets[n] guarded by locks[n%N_LOCKS]
    private static final int N_LOCKS = 16;
    private final Node[] buckets;
    private final Object[] locks;
 
    private static class Node {
        Node next;
        Object key;
        Object value;
    }
 
    public StripedMap(int numBuckets) {
        buckets = new Node[numBuckets];
        locks = new Object[N_LOCKS];
        for (int i = 0; i < N_LOCKS; i++)
            locks[i] = new Object();
    }
 
    private final int hash(Object key) {
        return Math.abs(key.hashCode() % buckets.length);
    }
 
    public Object get(Object key) {
        int hash = hash(key);
        synchronized (locks[hash % N_LOCKS]) {
            for (Node m = buckets[hash]; m != null; m = m.next)
                if (m.key.equals(key))
                    return m.value;
        }
        return null;
    }
 
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            synchronized (locks[i % N_LOCKS]) {
                buckets[i] = null;
            }
        }
    }
}
```

#### 11.4.4 避免热点域hot field

比如HashMap的size方法，ConcurrentHashMap采用了牺牲size的准确性的策略。

#### 11.4.5 一些替代独占锁的方法

ReadWriteLock，AtomicInteger，UNSAFE.compareAndSwap(..)

#### 11.4.6 监测CPU的利用率

vmstat，kill -3 pid

”waiting to lock monitor…“有这句就证明竞争太激烈了。

### 11.5 示例：比较Map的性能

比较了ConcurrentHashMap和synchronized hashmap的性能对比。

串行访问Map一个锁 pk 多个线程能并发的访问Map通过分段锁。

竞争非常激烈的时候，synchronized hashmap伸缩性非常差，吞吐量不会随着线程数增加而增加，反而降低，因为每个操作消耗的时间大部分都用于上下文切换和调度延迟上了。

### 11.6 减少上下文切换的开销

举个例子，就是APP记录日志，例如写日志到本地或者远程RPC，直接记录会存在I/O阻塞，靠一个轻量级的queue来解耦，使得APP不感知影响，减少阻塞。

http://www.artima.com/insidejvm/ed2/threadsynch.html  //TODO

### 总结

了解了性能的提升的几个方面，也了解性能的开销后，应用程序就要根据实际的场景进行取舍和评估。没有一劳永逸的优化方案，不断的进行小范围改进和调整是提高性能的有效手段。当前一些大的架构调整也会导致较大的性能的提升。

性能提升考虑的方面：

* 系统平台的资源利用率

一个程序对系统平台的资源利用率是指某一个设备繁忙且服务于此程序的时间占所有时间的比率。从物理学的角度讲类似于有用功的比率。简单的说就是：资源利用率=有效繁忙时间/总耗费时间。

也就说尽可能的让设备做有用的功，同时榨取其最大值。无用的循环可能会导致CPU 100%的使用率，但不一定是有效的工作。有效性通常难以衡量，通常只能以主观来评估，或者通过被优化的程序的行为来判断是否提高了有效性。

* 延迟

延迟描述的是完成任务所耗费的时间。延迟有时候也成为响应时间。如果有多个并行的操作，那么延迟取决于耗费时间最大的任务。

* 多处理

多处理是指在单一系统上同时执行多个进程或者多个程序的能力。多处理能力的好处是可以提高吞吐量。多处理可以有效利用多核CPU的资源。

* 多线程

多线程描述的是同一个地址空间内同时执行多个线程的过程。这些线程都有不同的执行路径和不同的栈结构。我们说的并发性更多的是指针对线程。

* 并发性

同时执行多个程序或者任务称之为并发。单程序内的多任务处理或者多程序间的多任务处理都认为是并发。

* 吞吐量

吞吐量衡量系统在单位之间内可以完成的工作总量。对于硬件系统而言，吞吐量是物理介质的上限。在没有达到物理介质之前，提高系统的吞吐量也可以大幅度改进性能。同时吞吐量也是衡量性能的一个指标。

* 瓶颈

程序运行过程中性能最差的地方。通常而言，串行的IO、磁盘IO、内存单元分配、网络IO等都可能造成瓶颈。某些使用太频繁的算法也有可能成为瓶颈。

* 可扩展性

这里的可扩展性主要是指程序或系统通过增加可使用的资源而增加性能的能力。



## 第12章 并发程序的测试

略




## 第13章 显式锁 Explicit Locks 

Java5.0之前只能用synchronized和volatile，5.0后Doug Lea加入了ReentrantLock，并不是替代内置锁，而是当内置锁机制不适用时，作为一种可选择的高级功能。

不适用可以包括无法中断一个正在等待获取锁的线程，无限的锁等待，内置锁必须放在代码块里面（编程有些局限性），所以提供了J.U.C的lock。

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

轮询锁和定时锁，内置锁的死锁问题只能通过重启程序解决，那么可定时和可轮询的锁提供了另一种选择。通过tryLock解决。

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
        long stopTime = System.nanoTime() + unit.toNanos(timeout); //定时，轮询
 
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

一般，非公平锁的性能要好于公平锁。原因在于一个线程被唤醒是需要时间的，挂起线程和唤醒回复线程存在开销，这个空隙如果有其他线程处于ready状态，不需要上下文切换，那么直接运行就行，A持有锁，B请求，但是B在恢复的过程中,C可以插队“非公平”的获取锁，然后执行再释放，这时候B刚刚好做完上下文切换可以执行，这个对于B和C来说是一个“双赢”的局面，是提高吞吐量的原因。

那么JVM也没有在其内置锁上采用公平性的机制。

### 13.4 在synchronized和ReentrantLock之间进行选择

除非使用到13.1提到的高级特性，或者内置锁无法满足需求时，否则还是老实用内置锁，毕竟是JVM自身提供的，而不是靠类库，因此可能会执行一些优化。

另外内置锁在利用kill -3 dump thread的时候可以发现栈帧上的一些monitor lock的信息，识别死锁，而J.U.C的锁这方面就不太行，当然JAVA6之后提供了管理和调试接口解决了。

### 13.5 读-写锁

ReentrantLock每次只有一个线程能持有锁，但是这种严格的互斥也会抑制并发。会抑制：

* 写/写
* 写/读
* 读/读

冲突，但是很多情况下读操作是非常多的，如果放宽加锁的需求，允许多个读操作可以同时访问数据，那么就可以提升性能。**但是要保证读取的数据是最新的，不会有其他线程修改数据。**

使用ReadWriteLock的场景是：一个资源可以被多个读操作访问，或者被一个写操作访问，但是二者不能同时进行。API如下：

```
Lock	readLock() Returns the lock used for reading.
 Lock	writeLock()  Returns the lock used for writing.
```

如果读正在持有锁，这时候另外一个线程写，那么会优先获取写。//？

下面是一个应用：

```
public class ReadWriteMap <K,V> {
    private final Map<K, V> map;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock r = lock.readLock();
    private final Lock w = lock.writeLock();
 
    public ReadWriteMap(Map<K, V> map) {
        this.map = map;
    }
 
    public V put(K key, V value) {
        w.lock();
        try {
            return map.put(key, value);
        } finally {
            w.unlock();
        }
    }
 
    public V remove(Object key) {
        w.lock();
        try {
            return map.remove(key);
        } finally {
            w.unlock();
        }
    }
 
    public void putAll(Map<? extends K, ? extends V> m) {
        w.lock();
        try {
            map.putAll(m);
        } finally {
            w.unlock();
        }
    }
 
    public void clear() {
        w.lock();
        try {
            map.clear();
        } finally {
            w.unlock();
        }
    }
 
    public V get(Object key) {
        r.lock();
        try {
            return map.get(key);
        } finally {
            r.unlock();
        }
    }
 
    public int size() {
        r.lock();
        try {
            return map.size();
        } finally {
            r.unlock();
        }
    }
 
    public boolean isEmpty() {
        r.lock();
        try {
            return map.isEmpty();
        } finally {
            r.unlock();
        }
    }
 
    public boolean containsKey(Object key) {
        r.lock();
        try {
            return map.containsKey(key);
        } finally {
            r.unlock();
        }
    }
 
    public boolean containsValue(Object value) {
        r.lock();
        try {
            return map.containsValue(value);
        } finally {
            r.unlock();
        }
    }
}
```



## 第14章 构建自定义的同步工具 Building Custom Synchronizers

类库中包含了许多存在状态依赖的类，例如FutureTask、Semaphore和BlockingQueue，他们的一些操作都有前提条件，例如非空，或者任务已完成等。

创建状态依赖类的最简单的房就是在JDK提供了的状态依赖类基础上构造。例如第八章的ValueLactch，如果这些不满足，可以使用Java语言或者类库提供的底层机制来构造，包括

* 内置的条件队列
* condition
* AQS

这一章就介绍这些。

### 14.1 状态依赖性的管理 State Dependence

在14.2节会介绍使用条件队列来解决阻塞线程运行的问题。下面先介绍通过轮询和休眠的方式（勉强）的解决。

下面是一个标准的模板，

```
void blockingAction() throws InterruptedException {
   acquire lock on object state
   while (precondition does not hold) {
      release lock
      wait until precondition might hold
      optionally fail if interrupted or timeout expires
      reacquire lock
   }
   perform action
}
```

下面介绍阻塞有界队列的集中实现方式。依赖的前提条件是：

* 不能从空缓存中获取元素
* 不能将元素放入已满的缓存中

不满足条件时候，依赖状态的操作可以

* 抛出异常
* 返回一个错误状态（码）
* 阻塞直到进入正确的状态

下面是基类，线程安全，但是非阻塞。

```
@ThreadSafe
public abstract class BaseBoundedBuffer <V> {
    @GuardedBy("this") private final V[] buf;
    @GuardedBy("this") private int tail;
    @GuardedBy("this") private int head;
    @GuardedBy("this") private int count;
 
    protected BaseBoundedBuffer(int capacity) {
        this.buf = (V[]) new Object[capacity];
    }
 
    protected synchronized final void doPut(V v) {
        buf[tail] = v;
        if (++tail == buf.length)
            tail = 0;
        ++count;
    }
 
    protected synchronized final V doTake() {
        V v = buf[head];
        buf[head] = null;
        if (++head == buf.length)
            head = 0;
        --count;
        return v;
    }
 
    public synchronized final boolean isFull() {
        return count == buf.length;
    }
 
    public synchronized final boolean isEmpty() {
        return count == 0;
    }
}
```

“先检查再运行”的逻辑解决方案如下，调用者必须自己处理前提条件失败的情况。当然也可以返回错误消息。

当然调用者可以不Sleep，而是直接重试，这种方法叫做**忙等待或者自旋等待（busy waiting or spin waiting. ）**，如果换成很长时间都不变，那么这将会消耗大量的CPU时间！！！所以调用者自己休眠，sleep让出CPU。但是这个时间就很尴尬了，sleep长了万一一会前提条件就满足了岂不是白等了从而响应性低，sleep短了浪费CPU时钟周期。另外可以试试yield，但是这也不靠谱。

```
@ThreadSafe
        public class GrumpyBoundedBuffer <V> extends BaseBoundedBuffer<V> {
    public GrumpyBoundedBuffer() {
        this(100);
    }
 
    public GrumpyBoundedBuffer(int size) {
        super(size);
    }
 
    public synchronized void put(V v) throws BufferFullException {
        if (isFull())
            throw new BufferFullException();
        doPut(v);
    }
 
    public synchronized V take() throws BufferEmptyException {
        if (isEmpty())
            throw new BufferEmptyException();
        return doTake();
    }
}
 
class ExampleUsage {
    private GrumpyBoundedBuffer<String> buffer;
    int SLEEP_GRANULARITY = 50;
 
    void useBuffer() throws InterruptedException {
        while (true) {
            try {
                String item = buffer.take();
                // use item
                break;
            } catch (BufferEmptyException e) {
                Thread.sleep(SLEEP_GRANULARITY);
            }
        }
    }
}
```

下一步改进下，首先让客户端舒服些。

```
@ThreadSafe
public class SleepyBoundedBuffer <V> extends BaseBoundedBuffer<V> {
    int SLEEP_GRANULARITY = 60;
 
    public SleepyBoundedBuffer() {
        this(100);
    }
 
    public SleepyBoundedBuffer(int size) {
        super(size);
    }
 
    public void put(V v) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isFull()) {
                    doPut(v);
                    return;
                }
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }
 
    public V take() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (!isEmpty())
                    return doTake();
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }
}
```

这种方式测试失败，那么释放锁，让别人做，自己休眠下，然后再检测，不断的重复这个过程，当然可以解决，但是还是需要做权衡，CPU使用率与响应性之间的抉择。

那么我们想如果这种轮询和休眠的dummy方式不用，而是存在某种挂起线程的方案，并且这种方法能够确保党某个条件成真时候立刻唤醒线程，那么将极大的简化实现工作，这就是条件队列的实现。

Condition Queues的名字来源：it gives a group of threads called the **wait set** a way to wait for a specific
condition to become true. Unlike typical queues in which the elements are data items, the elements of a condition queue are the threads waiting for the condition.

每个Java对象都可以是一个锁，每个对象同样可以作为一个条件队列，并且Object的wait、notify和notifyAll就是内部条件队列的API。对象的内置锁（intrinsic lock ）和内置条件队列是关联的，**要调用X中的条件队列的任何一个方法，都必须持有对象X上的锁。**

Object.wait自动释放锁，并且请求操作系统挂起当前线程，从而其他线程可以获得这个锁并修改对象状态。当被挂起的线程唤醒时。它将在返回之前重新获取锁。

```
@ThreadSafe
public class BoundedBuffer <V> extends BaseBoundedBuffer<V> {
    // CONDITION PREDICATE: not-full (!isFull())
    // CONDITION PREDICATE: not-empty (!isEmpty())
    public BoundedBuffer() {
        this(100);
    }
 
    public BoundedBuffer(int size) {
        super(size);
    }
 
    // BLOCKS-UNTIL: not-full
    public synchronized void put(V v) throws InterruptedException {
        while (isFull())
            wait();
        doPut(v);
        notifyAll();
    }
 
    // BLOCKS-UNTIL: not-empty
    public synchronized V take() throws InterruptedException {
        while (isEmpty())
            wait();
        V v = doTake();
        notifyAll();
        return v;
    }
 
    // BLOCKS-UNTIL: not-full
    // Alternate form of put() using conditional notification
    public synchronized void alternatePut(V v) throws InterruptedException {
        while (isFull())
            wait();
        boolean wasEmpty = isEmpty();
        doPut(v);
        if (wasEmpty)
            notifyAll();
    }
}
```

注意，如果某个功能无法通过“轮询和休眠"来实现，那么条件队列也无法实现。

### 14.2 Using Condition Queues

#### 14.2.1 条件谓词The Condition Predicate

The Condition Predicate 是使某个操作成为状态依赖操作的前提条件。take方法的条件谓词是”缓存不为空“，take方法在执行之前必须首先测试条件谓词。同样，put方法的条件谓词是”缓存不满“。

在条件等待中存在一种重要的三元关系，包括

* 加锁
* wait方法
* 条件谓词

条件谓词中包含多个状态变量，而状态变量由一个锁来保护，因此在测试条件谓词之前必须先持有这个锁。锁对象和条件队列对象必须是同一个对象。wait释放锁，线程挂起阻塞，等待知道超时，然后被另外一个线程中断或者被一个通知唤醒。唤醒后，wait在返回前还需要重新获取锁，当线程从wait方法中唤醒，它在重新请求锁时不具有任何特殊的优先级，和其他人一起竞争。

#### 14.2.2 过早唤醒

其他线程中间插足了，获取了锁，并且修改了遍历，这时候线程获取锁需要重新检查条件谓词。

```
wait block ----------race to get lock ------------------------------------------get lock ----- 
                    ^
wait block --------> race to get lock ------get lock------> perform action  ---> release lock
                    ^
                    notifyAll
```

当然有的时候，比如一个你根本不知道为什么别人调用了notify或者notifyAll，也许条件谓词压根就没满足，但是线程还是获取了锁，然后test条件谓词，释放所，其他线程都来了这么一趟，发生这就是“谎报军情”啊。

基于以上这两种情况，都必须重新测试条件谓词。

When using condition waits (Object.wait or Condition.await):

* Always have a condition predicate——some test of object state that must hold before proceeding;
* Always test the condition predicate before calling wait, and again after returning from wait;
* Always call wait in a loop;
* Ensure that the state variables making up the condition predicate are guarded by the lock associated with the condition queue;
* Hold the lock associated with the the condition queue when calling wait, notify, or notifyAll
* Do not release the lock after checking the condition predicate but before acting on it.

模板就是：

```
void stateDependentMethod() throws InterruptedException {
 // condition predicate must be guarded by lock
 synchronized(lock) {  
     while (!conditionPredicate())  //一定在循环里面做条件谓词
         lock.wait();  //确保和synchronized的是一个对象
     // object is now in desired state  //不要释放锁
 }
} 
```

#### 14.2.3 丢失的信号

保证notify一定在wait之后

#### 14.2.4 通知

下面介绍通知。

调用notify和notifyAll也得持有与条件队列对象相关联的锁。调用notify，JVM Thread Scheduler在这个条件队列上等待的多个线程中选择一个唤醒，而notifyAll则会唤醒所有线程。因此一旦notify了那么就需要尽快的释放锁，否则别人都竞争等着拿锁，都会进行blocked的状态，而不是线程挂起waiting状态，竞争都了不是好事，但是这是你考了性能因素和安全性因素的一个矛盾，具体问题要具体分析。

下面的方法可以进来减少竞争，但是确然程序正确的实现有些难写，所以这个折中还得自己考虑：

```
public synchronized void alternatePut(V v) throws InterruptedException {
        while (isFull())
            wait();
        boolean wasEmpty = isEmpty();
        doPut(v);
        if (wasEmpty)
            notifyAll();
    }
```

使用notify容易丢失信号，所以大多数情况下用notifyAll，比如take notify，却通知了另外一个take，没有通知put，那么这就是信号丢失，是一种“被劫持的”信号。

因此只有满足下面两个条件，才能用notify，而不是notifyAll：

* 所有等待线程的类型都相同
* 单进单出

#### 14.2.5 示例：阀门类A Gate Class

和第5章的那个TestHarness中使用CountDownLatch类似，完全可以使用wait/notifyAll做阀门。

```
@ThreadSafe
public class ThreadGate {
    // CONDITION-PREDICATE: opened-since(n) (isOpen || generation>n)
    @GuardedBy("this") private boolean isOpen;
    @GuardedBy("this") private int generation;
 
    public synchronized void close() {
        isOpen = false;
    }
 
    public synchronized void open() {
        ++generation;
        isOpen = true;
        notifyAll();
    }
 
    // BLOCKS-UNTIL: opened-since(generation on entry)
    public synchronized void await() throws InterruptedException {
        int arrivalGeneration = generation;
        while (!isOpen && arrivalGeneration == generation)
            wait();
    }
}
```

### 14.3 Explicit Condition Objects

Lock是一个内置锁的替代，而Condition也是一种广义的**内置条件队列**。

Condition的API如下：

```
public interface Condition {
 void await() throws InterruptedException;
 boolean await(long time, TimeUnit unit)throws InterruptedException;
 long awaitNanos(long nanosTimeout) throws InterruptedException;
 void awaitUninterruptibly();
 boolean awaitUntil(Date deadline) throws InterruptedException;
 void signal();
 void signalAll();
}
```

内置条件队列存在一些缺陷，每个内置锁都只能有一个相关联的条件队列，记住是**一个**。所以在BoundedBuffer这种累中，**多个线程可能在同一个条件队列上等待不同的条件谓词**，所以notifyAll经常通知不是同一个类型的需求。如果想编写一个带有多个条件谓词的并发对象，或者想获得除了条件队列可见性之外的更多的控制权，可以使用Lock和Condition，而不是内置锁和条件队列，这更加灵活。

一个Condition和一个lock关联，想象一个条件队列和内置锁关联一样。在Lock上调用newCondition就可以新建无数个条件谓词，这些condition是可中断的、可有时间限制的，公平的或者非公平的队列操作。

The equivalents of wait, notify, and notifyAll for Condition objects are await, signal, and
signalAll。

下面的例子就是改造后的BoundedBuffer，

```
@ThreadSafe
public class ConditionBoundedBuffer <T> {
    protected final Lock lock = new ReentrantLock();
    // CONDITION PREDICATE: notFull (count < items.length)
    private final Condition notFull = lock.newCondition();
    // CONDITION PREDICATE: notEmpty (count > 0)
    private final Condition notEmpty = lock.newCondition();
    private static final int BUFFER_SIZE = 100;
    @GuardedBy("lock") private final T[] items = (T[]) new Object[BUFFER_SIZE];
    @GuardedBy("lock") private int tail, head, count;
 
    // BLOCKS-UNTIL: notFull
    public void put(T x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length)
                notFull.await();
            items[tail] = x;
            if (++tail == items.length)
                tail = 0;
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
 
    // BLOCKS-UNTIL: notEmpty
    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                notEmpty.await();
            T x = items[head];
            items[head] = null;
            if (++head == items.length)
                head = 0;
            --count;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }
}
```

注意这里使用了signal而不是signalll，能极大的减少每次缓存操作中发生的上下文切换和锁请求次数。

使用condition和内置锁和条件队列一样，必须保卫在lock里面。

### 14.4 Synchronizer剖析

看似ReentrantLock和Semaphore功能很类似，每次只允许一定的数量线程通过，到达阀门时

* 可以通过 lock或者acquire
* 等待，阻塞住了
* 取消tryLock，tryAcquire
* 可中断的，限时的
* 公平等待和非公平等待

下面的程序是使用Lock做一个Mutex也就是持有一个许可的Semaphore。

```
@ThreadSafe
public class SemaphoreOnLock {
    private final Lock lock = new ReentrantLock();
    // CONDITION PREDICATE: permitsAvailable (permits > 0)
    private final Condition permitsAvailable = lock.newCondition();
    @GuardedBy("lock") private int permits;
 
    SemaphoreOnLock(int initialPermits) {
        lock.lock();
        try {
            permits = initialPermits;
        } finally {
            lock.unlock();
        }
    }
 
    // BLOCKS-UNTIL: permitsAvailable
    public void acquire() throws InterruptedException {
        lock.lock();
        try {
            while (permits <= 0)
                permitsAvailable.await();
            --permits;
        } finally {
            lock.unlock();
        }
    }
 
    public void release() {
        lock.lock();
        try {
            ++permits;
            permitsAvailable.signal();
        } finally {
            lock.unlock();
        }
    }
}
```

实际上很多J.U.C下面的类都是基于AbstractQueuedSynchronizer (AQS)构建的，例如CountDownLatch, ReentrantReadWriteLock, SynchronousQueue,and FutureTask（java7之后不是了）。AQS解决了实现同步器时设计的大量细节问题，例如等待线程采用FIFO队列操作顺序。AQS不仅能极大极少实现同步器的工作量，并且也不必处理竞争问题，基于AQS构建只可能在一个时刻发生阻塞，从而降低上下文切换的开销，提高吞吐量。在设计AQS时，充分考虑了可伸缩性，可谓大师Doug Lea的经典作品啊！

### 14.5 AbstractQueuedSynchronizer (AQS) 

基于AQS构建的同步器勒种，最进步的操作包括各种形式的获取操作和释放操作。获取操作是一种依赖状态的操作，并且通常会阻塞。

如果一个类想成为状态依赖的类，它必须拥有一些状态，AQS负责管理这些状态，通过getState,setState, compareAndSetState等protected类型方法进行操作。这是设计模式中的模板模式。

使用AQS的模板如下：

获取锁：首先判断当前状态是否允许获取锁，如果是就获取锁，否则就阻塞操作或者获取失败，也就是说如果是独占锁就可能阻塞，如果是共享锁就可能失败。另外如果是阻塞线程，那么线程就需要进入阻塞队列。当状态位允许获取锁时就修改状态，并且如果进了队列就从队列中移除。

释放锁:这个过程就是修改状态位，如果有线程因为状态位阻塞的话就唤醒队列中的一个或者更多线程。

```
boolean acquire() throws InterruptedException {
 while (state does not permit acquire) {
 if (blocking acquisition requested) {
 enqueue current thread if not already queued
 block current thread
 }
 else
 return failure
 }
 possibly update synchronization state
 dequeue thread if it was queued
 return success
}
void release() {
 update synchronization state
 if (new state may permit a blocked thread to acquire)
 unblock one or more queued threads
}
```

要支持上面两个操作就必须有下面的条件：

- 原子性操作同步器的状态位
- 阻塞和唤醒线程
- 一个有序的队列

**1 状态位的原子操作**

这里使用一个32位的整数来描述状态位，前面章节的原子操作的理论知识整好派上用场，在这里依然使用CAS操作来解决这个问题。事实上这里还有一个64位版本的同步器（AbstractQueuedLongSynchronizer），这里暂且不谈。

**2 阻塞和唤醒线程**

标准的JAVA API里面是无法挂起（阻塞）一个线程，然后在将来某个时刻再唤醒它的。JDK 1.0的API里面有Thread.suspend和Thread.resume，并且一直延续了下来。但是这些都是过时的API，而且也是不推荐的做法。

HotSpot在Linux中中通过调用pthread_mutex_lock函数把线程交给系统内核进行阻塞。

在JDK 5.0以后利用JNI在LockSupport类中实现了此特性。

> LockSupport.park()
> LockSupport.park(Object)
> LockSupport.parkNanos(Object, long)
> LockSupport.parkNanos(long)
> LockSupport.parkUntil(Object, long)
> LockSupport.parkUntil(long)
> LockSupport.unpark(Thread)

上面的API中park()是在当前线程中调用，导致线程阻塞，带参数的Object是挂起的对象，这样监视的时候就能够知道此线程是因为什么资源而阻塞的。由于park()立即返回，所以通常情况下需要在循环中去检测竞争资源来决定是否进行下一次阻塞。park()返回的原因有三：

- 其他某个线程调用将当前线程作为目标调用 [`unpark`](http://www.blogjava.net/xylz/java/util/concurrent/locks/LockSupport.html#unpark(java.lang.Thread))；
- 其他某个线程[中断](http://www.blogjava.net/xylz/java/lang/Thread.html#interrupt())当前线程；
- 该调用不合逻辑地（即毫无理由地）返回。

其实第三条就决定了需要循环检测了，类似于通常写的while(checkCondition()){Thread.sleep(time);}类似的功能。

**3 有序队列**

在AQS中采用CHL列表来解决有序的队列的问题。

AQS采用的CHL模型采用下面的算法完成FIFO的入队列和出队列过程。该队列的操作均通过Lock-Free（CAS）操作.

自己实现的CLH SpinLock如下：

```
class ClhSpinLock {
    private final ThreadLocal<Node> prev;
    private final ThreadLocal<Node> node;
    private final AtomicReference<Node> tail = new AtomicReference<Node>(new Node());

    public ClhSpinLock() {
        this.node = new ThreadLocal<Node>() {
            protected Node initialValue() {
                return new Node();
            }
        };

        this.prev = new ThreadLocal<Node>() {
            protected Node initialValue() {
                return null;
            }
        };
    }

    public void lock() {
        final Node node = this.node.get();
        node.locked = true;
        // 一个CAS操作即可将当前线程对应的节点加入到队列中，
        // 并且同时获得了前继节点的引用，然后就是等待前继释放锁
        Node pred = this.tail.getAndSet(node);
        this.prev.set(pred);
        while (pred.locked) {// 进入自旋
        }
    }

    public void unlock() {
        final Node node = this.node.get();
        node.locked = false;
        this.node.set(this.prev.get());
    }

    private static class Node {
        private volatile boolean locked;
    }
}
```

对于入队列(*enqueue)：*采用CAS操作，每次比较尾结点是否一致，然后插入的到尾结点中。

```
do {
        pred = tail;
}while ( !compareAndSet(pred,tail,node) );
```

对于出队列(*dequeue*):由于每一个节点也缓存了一个状态，决定是否出队列，因此当不满足条件时就需要自旋等待，一旦满足条件就将头结点设置为下一个节点。

AQS里面有三个核心字段：

> private volatile int state;
>
> private transient volatile Node head;
>
> private transient volatile Node tail;

其中state描述的有多少个线程取得了锁，对于互斥锁来说state<=1。head/tail加上CAS操作就构成了一个CHL的FIFO队列。下面是Node节点的属性。

独占操作的API都是不带有shared，而共享的包括semaphore和countdownlatch都是使用带有shared字面的API。

一些有用的参考资料：

**java.util.concurrent.locks.AbstractQueuedSynchronizer - **AQS

[http://gee.cs.oswego.edu/dl/papers/aqs.pdf](http://gee.cs.oswego.edu/dl/papers/aqs.pdf)论文

http://www.blogjava.net/xylz/archive/2010/07/08/325587.html 一个比较全面的另外一个人的解读

[http://suo.iteye.com/blog/1329460](http://suo.iteye.com/blog/1329460)

[http://www.infoq.com/cn/articles/jdk1.8-abstractqueuedsynchronizer](http://www.infoq.com/cn/articles/jdk1.8-abstractqueuedsynchronizer)

[http://www.cnblogs.com/zhanjindong/p/java-concurrent-package-aqs-overview.html](http://www.cnblogs.com/zhanjindong/p/java-concurrent-package-aqs-overview.html)

[http://www.cnblogs.com/zhanjindong/p/java-concurrent-package-aqs-clh-and-spin-lock.html](http://www.cnblogs.com/zhanjindong/p/java-concurrent-package-aqs-clh-and-spin-lock.html)

[http://www.cnblogs.com/zhanjindong/p/java-concurrent-package-aqs-locksupport-and-thread-interrupt.html](http://www.cnblogs.com/zhanjindong/p/java-concurrent-package-aqs-locksupport-and-thread-interrupt.html)

独占的就用TRyAcquire, TRyRelease, and isHeldExclusively,共享的就用 tryAcquireShared and TRyReleaseShared. 带有try前缀的方法都是模板方法，AQS用于判断是否可以继续，例如如果tryAcquireShared返回一个负值，那么表示获取锁失败，失败的就需要进入CLH队列，并且挂起线程。

举一个例子，一个简单的闭锁。

```
@ThreadSafe
public class OneShotLatch {
    private final Sync sync = new Sync();
 
    public void signal() {
        sync.releaseShared(0);
    }
 
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(0);
    }
 
    private class Sync extends AbstractQueuedSynchronizer {
        protected int tryAcquireShared(int ignored) {
            // Succeed if latch is open (state == 1), else fail
            return (getState() == 1) ? 1 : -1;
        }
 
        protected boolean tryReleaseShared(int ignored) {
            setState(1); // Latch is now open
            return true; // Other threads may now be able to acquire
 
        }
    }
}
```

下面是自己实现的一个Mutex。

```
/**
 * Lock free的互斥锁，简单实现，不可重入锁
 */
public class Mutex implements Lock {

    private static final int FREE = 0;
    private static final int BUSY = 1;

    private static class LockSync extends AbstractQueuedSynchronizer {

        private static final long serialVersionUID = 4689388770786922019L;

        protected boolean isHeldExclusively() {
            return getState() == BUSY;
        }

        public boolean tryAcquire(int acquires) {
            return compareAndSetState(FREE, BUSY);
        }

        protected boolean tryRelease(int releases) {
            if (getState() == FREE) {
                throw new IllegalMonitorStateException();
            }

            setState(FREE);
            return true;
        }

        Condition newCondition() {
            return new ConditionObject();
        }

    }

    private final LockSync sync = new LockSync();

    public void lock() {
        sync.acquire(0);
    }

    public boolean tryLock() {
        return sync.tryAcquire(0);
    }

    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }

    public void unlock() {
        sync.release(0);
    }

    public Condition newCondition() {
        return sync.newCondition();
    }

    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

    public boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(0);
    }

}
```



### 14.6 J.U.C同步器勒种的AQS

* ReentrantLock

```
protected boolean tryAcquire(int ignored) {
 final Thread current = Thread.currentThread();
 int c = getState();
 if (c == 0) {
 if (compareAndSetState(0, 1)) {
 owner = current;
 return true;
 }
 } else if (current == owner) {
 setState(c+1);
 return true;
 }
 return false;
} 
```

* Semaphore和CountDownLatch

```
protected int tryAcquireShared(int acquires) {
 while (true) {
 int available = getState();
 int remaining = available - acquires;
 if (remaining < 0
 || compareAndSetState(available, remaining))
 return remaining;
 }
}
protected boolean tryReleaseShared(int releases) {
 while (true) {
 int p = getState();
 if (compareAndSetState(p, p + releases))
 return true;
 }
} 
```



## 第15章 原子遍历与非阻塞同步机制Atomic Variables and Non-blocking Synchronization

近年来，在并发算法领域的大多数研究都侧重于非阻塞算法，这种算法用底层的原子机器指令来代替锁来确保数据在并发访问中的一致性，非阻塞算法被广泛应用于OS和JVM中实现线程/进程调度机制和GC以及锁，并发数据结构中。

与锁的方案相比，非阻塞算法都要复杂的多，他们在可伸缩性和活跃性上（避免死锁）都有巨大的优势。

非阻塞算法，顾名思义，多个线程竞争相同的数据时不会发生阻塞，因此他能在粒度更细的层次上进行协调，而且极大的减少调度开销。

### 15.1 锁的劣势

独占，可见性是锁要保证的。

许多JVM都对非竞争的锁获取和释放做了很多优化，性能很不错了。但是如果一些线程被挂起然后稍后恢复运行，当线程恢复后还得等待其他线程执行完他们的时间片，才能被调度，所以挂起和恢复线程存在很大的开销，其实很多锁的力度很小的，很简单，如果锁上存在着激烈的竞争，那么多调度开销/工作开销比值就会非常高。

与锁相比volatile是一种更轻量的同步机制，因为使用volatile不会发生上下文切换或者线程调度操作，但是volatile的指明问题就是虽然保证了可见性，但是原子性无法保证，比如i++的字节码就是N行。

锁的其他缺点还包括，如果一个线程正在等待锁，它不能做任何事情，如果一个线程在持有锁的情况下呗延迟执行了，例如发生了缺页错误，调度延迟，那么就没法执行。如果被阻塞的线程优先级较高，那么就会出现priority invesion的问题，被永久的阻塞下去。

### 15.2 硬件对并发的支持

独占锁是悲观所，对于细粒度的操作，更高效的应用是乐观锁，这种方法需要借助**冲突监测机制来判断更新过程中是否存在来自其他线程的干扰，如果存在则失败重试**。

几乎所有的现代CPU都有某种形式的原子读-改-写指令，例如compare-and-swap等，JVM就是使用这些指令来实现无锁并发。

#### 15.2.1 比较并交换

CAS（Compare and set）乐观的技术。Java实现的一个compare and set如下，这是一个模拟底层的示例：

```
@ThreadSafe
public class SimulatedCAS {
    @GuardedBy("this") private int value;
 
    public synchronized int get() {
        return value;
    }
 
    public synchronized int compareAndSwap(int expectedValue,
                                           int newValue) {
        int oldValue = value;
        if (oldValue == expectedValue)
            value = newValue;
        return oldValue;
    }
 
    public synchronized boolean compareAndSet(int expectedValue,
                                              int newValue) {
        return (expectedValue
                == compareAndSwap(expectedValue, newValue));
    }
}
```

#### 15.2.2 非阻塞的计数器

```
public class CasCounter {
    private SimulatedCAS value;
 
    public int getValue() {
        return value.get();
    }
 
    public int increment() {
        int v;
        do {
            v = value.get();
        } while (v != value.compareAndSwap(v, v + 1));
        return v + 1;
    }
}
```

Java中使用AtomicInteger。

首先在竞争激烈一般时候，CAS性能远超过第三章基于锁的计数器。看起来他的指令更多，但是无需上下文切换和线程挂起，JVM内部的代码路径实际很长，所以反而好些。但是激烈程度比较高的时候，它的开销还是比较大，但是你会发生这种激烈程度非常高的情况只是理论，实际生产环境很难遇到。况且JIT很聪明，这种操作往往能非常大的优化。

为了确保正常更新，可能得将CAS操作放到for循环里，从语法结构上来看，使用**CAS**比使用锁更加复杂，得考虑失败的情况（锁会挂起线程，直到恢复）；但是基于**CAS**的原子操作，在性能上基本超过了基于锁的计数器，即使只有很小的竞争或者不存在竞争！

在轻度到中度的争用情况下，非阻塞算法的性能会超越阻塞算法，因为 CAS 的多数时间都在第一次尝试时就成功，而发生争用时的开销也不涉及**线程挂起**和**上下文切换**，只多了几个循环迭代。没有争用的 CAS 要比没有争用的锁便宜得多（这句话肯定是真的，因为没有争用的锁涉及 CAS 加上额外的处理，加锁至少需要一个CAS，在有竞争的情况下，需要操作队列，线程挂起，上下文切换），而争用的 CAS 比争用的锁获取涉及更短的延迟。

CAS的缺点是它使用调用者来处理竞争问题，通过重试、回退、放弃，而锁能自动处理竞争问题，例如阻塞。

原子变量可以看做更好的volatile类型变量。

AtomicInteger在JDK8里面做了改动。

```
public final int getAndIncrement() {
    return unsafe.getAndAddInt(this, valueOffset, 1);
}
```

JDK7里面的实现如下：

```
public final int getAndAdd(int delta) {
       for(;;) {
           intcurrent= get();
           intnext=current+delta;
           if(compareAndSet(current,next))
               returncurrent;
        }
    }
```

Unsafe是经过特殊处理的，不能理解成常规的java代码，区别在于：

- 1.8在调用getAndAddInt的时候，如果系统底层支持fetch-and-add，那么它执行的就是native方法，使用的是fetch-and-add；


- 如果不支持，就按照上面的所看到的getAndAddInt方法体那样，以java代码的方式去执行，使用的是compare-and-swap；

这也正好跟openjdk8中Unsafe::getAndAddInt上方的注释相吻合：

```
// The following contain CAS-based Java implementations used on
// platforms not supporting native instructions
```
### 15.3 原子变量类

J.U.C的AtomicXXX

例如一个AtomictReference的使用如下：

```
public class CasNumberRange {
    @Immutable
            private static class IntPair {
        // INVARIANT: lower <= upper
        final int lower;
        final int upper;
 
        public IntPair(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }
    }
 
    private final AtomicReference<IntPair> values =
            new AtomicReference<IntPair>(new IntPair(0, 0));
 
    public int getLower() {
        return values.get().lower;
    }
 
    public int getUpper() {
        return values.get().upper;
    }
 
    public void setLower(int i) {
        while (true) {
            IntPair oldv = values.get();
            if (i > oldv.upper)
                throw new IllegalArgumentException("Can't set lower to " + i + " > upper");
            IntPair newv = new IntPair(i, oldv.upper);
            if (values.compareAndSet(oldv, newv))
                return;
        }
    }
 
    public void setUpper(int i) {
        while (true) {
            IntPair oldv = values.get();
            if (i < oldv.lower)
                throw new IllegalArgumentException("Can't set upper to " + i + " < lower");
            IntPair newv = new IntPair(oldv.lower, i);
            if (values.compareAndSet(oldv, newv))
                return;
        }
    }
}
```

#### 15.3.2 性能比较：锁与原子变量

略

### 15.4 非阻塞算法

Lock-free算法，可以实现栈、队列、优先队列或者散列表。

#### 15.4 非阻塞的栈

Trebier算法，1986年提出的。

```
 public class ConcurrentStack <E> {
    AtomicReference<Node<E>> top = new AtomicReference<Node<E>>();
 
    public void push(E item) {
        Node<E> newHead = new Node<E>(item);
        Node<E> oldHead;
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
    }
 
    public E pop() {
        Node<E> oldHead;
        Node<E> newHead;
        do {
            oldHead = top.get();
            if (oldHead == null)
                return null;
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));
        return oldHead.item;
    }
 
    private static class Node <E> {
        public final E item;
        public Node<E> next;
 
        public Node(E item) {
            this.item = item;
        }
    }
}
```

#### 15.4.2 非阻塞的链表

有点复杂哦，实际J.U.C的ConcurrentLinkedQueue也是参考了这个由Michael and Scott，1996年实现的算法。

```
public class LinkedQueue <E> {
 
    private static class Node <E> {
        final E item;
        final AtomicReference<LinkedQueue.Node<E>> next;
 
        public Node(E item, LinkedQueue.Node<E> next) {
            this.item = item;
            this.next = new AtomicReference<LinkedQueue.Node<E>>(next);
        }
    }
 
    private final LinkedQueue.Node<E> dummy = new LinkedQueue.Node<E>(null, null);
    private final AtomicReference<LinkedQueue.Node<E>> head
            = new AtomicReference<LinkedQueue.Node<E>>(dummy);
    private final AtomicReference<LinkedQueue.Node<E>> tail
            = new AtomicReference<LinkedQueue.Node<E>>(dummy);
 
    public boolean put(E item) {
        LinkedQueue.Node<E> newNode = new LinkedQueue.Node<E>(item, null);
        while (true) {
            LinkedQueue.Node<E> curTail = tail.get();
            LinkedQueue.Node<E> tailNext = curTail.next.get();
            if (curTail == tail.get()) {
                if (tailNext != null) {
                    // Queue in intermediate state, advance tail
                    tail.compareAndSet(curTail, tailNext);
                } else {
                    // In quiescent state, try inserting new node
                    if (curTail.next.compareAndSet(null, newNode)) {
                        // Insertion succeeded, try advancing tail
                        tail.compareAndSet(curTail, newNode);
                        return true;
                    }
                }
            }
        }
    }
}
```

#### 15.4.3 原子域更新

AtomicReferenceFieldUpdater,一个基于反射的工具类，它能对指定类的指定的volatile引用字段进行原子更新。(注意这个字段不能是private的) 

通过调用AtomicReferenceFieldUpdater的静态方法newUpdater就能创建它的实例，该方法要接收三个参数： 

* 包含该字段的对象的类 
* 将被更新的对象的类 
* 将被更新的字段的名称 

```
AtomicReferenceFieldUpdater updater=AtomicReferenceFieldUpdater.newUpdater(Dog.class,String.class,"name");  
        Dog dog1=new Dog();  
        updater.compareAndSet(dog1,dog1.name,"test") ;  
        System.out.println(dog1.name);  
  
class Dog  {  
     volatile  String name="dog1";  
  
}  
```

#### 15.4.4 ABA问题

AtomicStampedReference //TODO



## 第16章 Java内存模型JMM

前面几章介绍的安全发布、同步策略的规范还有一致性，这些安全性都来自于JMM。

### 16.1 什么是内存模型，为什么需要它？

假设

```
a=3
```

内存模型要解决的问题是：“在什么条件下，读取a的线程可以看到这个值为3？”

如果缺少同步会有很多因素导致无法立即、甚至永远看不到一个线程的操作结果，包括

* 编译器中指令顺序
* 变量保存在寄存器而不是内存中
* 处理器可以乱序或者并行执行指令
* 缓存可能会改变将写入变量提交到主内存的次序
* 处理器中也有本地缓存，对其他处理器不可见

单线程中，会为了提高速度使用这些技术，但是Java语言规范要求JVM在线程中维护一种类似串行的语义：只要程序的最终结果与在严格环境中的执行结果相同，那么上述操作都是允许的。

随着处理器越来越强大，编译器也在不断的改进，通过指令重排序实现优化执行，使用成熟的全局寄存器分配算法，但是单处理器存在瓶颈，转而变为多核，提高并行性。

在多线程环境中，维护程序的串行性将导致很大的性能开销，并发程序中的线程，大多数时间各自为政，线程之间协调操作只会降低应用程序的运行速度，不会带来任何好处，只有当多个线程要共享数据时，才必须协调他们之间的操作，并且JVM依赖程序通过同步操作找出这些协调操作将何时发生。

JMM规定了JVM必须遵循一组最小的保证，**保证规定了对变量的写入操作在何时将对其他线程可见。**JMM需要在各个处理器体系架构中实现一份。

#### 16.1.1 平台的内存模型

在共享内存的多处理器体系架构中，每个处理器拥有自己的缓存，并且定期的与主内存进行协调。在不同的处理器架构中提供了不同级别的缓存一致性（cache coherence）。其中一部分只提供最小的保证，即允许不同的处理器在任意时刻从同一个存储位置上看到不同的值。操作系统、编译器以及runtime需要弥补这种硬件能力与线程安全需求之间的差异。

要确保每个处理器在任意时刻都知道其他处理器在进行的工作，这将开销巨大。多数情况下，这完全没必要，可随意放宽存储一致性，换取性能的提升。存在一些特殊的指令（成为内存栅栏），当需要共享数据时，这些指令就能实现额外的存储协调保证。为了使Java开发人员无须关心不同架构上内存模型之间的差异，产生了JMM，JVM通过在适当的位置上插入内存栅栏来屏蔽JMM与底层平台内存模型之间的差异。

按照程序的顺序执行，这种乐观的串行一致性在任何一款现代多处理器架构中都不会提供这种串行一致性。当跨线程共享数据时，会出现一些奇怪的情况，除非通过使用内存栅栏来防止这种情况的发生。

#### 16.1.2 重排序

下面的代码，4中输出都是有可能的。

```
public class ReorderingDemo {

    static int x = 0, y = 0, a = 0, b = 0;

    public static void main(String[] args) throws Exception {
        Bag bag = new HashBag();
        for (int i = 0; i < 10000; i++) {
            x = y = a = b = 0;
            Thread one = new Thread() {
                public void run() {
                    a = 1;
                    x = b;
                }
            };
            Thread two = new Thread() {
                public void run() {
                    b = 1;
                    y = a;
                }
            };
            one.start();
            two.start();
            one.join();
            two.join();
            bag.add(x + "_" + y);
        }
        System.out.println(bag.getCount("0_1"));
        System.out.println(bag.getCount("1_0"));
        System.out.println(bag.getCount("1_1"));
        System.out.println(bag.getCount("0_0"));
        // 结果是如下的或者其他情况，证明可能发生指令重排序
        //        9999
        //        1
        //        0
        //        0

        //        9998
        //        2
        //        0
        //        0
    }
```

#### 16.1.3 Java内存模型简介

JMM通过各种操作来定义，包括对变量的读写操作，监视器monitor的加锁和释放操作，以及线程的启动和合并操作，JMM为程序中所有的操作定义了一个偏序关系，成为Happens-before，要想保证执行操作B的线程看到A的结果，那么A和B之间必须满足Happens-before关系。如果没有这个关系，JVM可以任意的重排序。

**JVM来定义了JMM（Java内存模型）来屏蔽底层平台不同带来的各种同步问题，使得程序员面向JAVA平台预期的结果都是一致的，对于“共享的内存对象的访问保证因果性正是JMM存在的理由”（这句话说的太好了！！！）。**

因为没法枚举各种情况，所以提供工具辅助程序员自定义，另外一些就是JMM提供的通用原则，叫做happens-before原则，就是如果动作B要看到动作A的执行结果（无论A/B是否在同一个线程里面执行），那么A/B就需要满足happens-before关系。下面是所有的规则，满足这些规则是一种特殊的处理措施，否则就按照上面背景提到的对于可见性、顺序性是没有保障的，会出现“意外”的情况。

如果多线程写入遍历，没有happens-before来排序，那么会产生race condition。在正确使用同步的的程序中，不存在数据竞争，会表现出串行一致性。

* （1）同一个线程中的每个Action都happens-before于出现在其后的任何一个Action。//控制流，而非语句
* （2）对一个监视器的解锁happens-before于每一个后续对同一个监视器的加锁。//lock、unlock
* （3）对volatile字段的写入操作happens-before于每一个后续的同一个字段的读操作。
* （4）Thread.start()的调用会happens-before于启动线程里面的动作。
* （5）Thread中的所有动作都happens-before于其他线程检查到此线程结束或者Thread.join（）中返回或者Thread.isAlive()==false。
* （6）一个线程A调用另一个另一个线程B的interrupt（）都happens-before于线程A发现B被A中断（B抛出异常或者A检测到B的isInterrupted（）或者interrupted()）。
* （7）一个对象构造函数的结束happens-before与该对象的finalizer的开始
* （8）如果A动作happens-before于B动作，而B动作happens-before与C动作，那么A动作happens-before于C动作。

#### 16.1.4 借助同步

piggyback（借助）现有的同步机制可见性。例如在AQS中借助一个volatile的state变量保证happens-before进行排序。

举例：Inner class of FutureTask illustrating synchronization piggybacking. (See JDK source)

还可以记住CountDownLatch，Semaphore，Future，CyclicBarrier等完成自己的希望。

### 16.2 发布

第三章介绍了如何安全的或者不正确的发布一个对象，其中介绍的各种技术都依赖JMM的保证，而造成发布不正确的原因就是

* 发布一个共享对象
* 另外一个线程访问该对象

之间缺少一种happens-before关系。

#### 16.2.1 不安全的发布

缺少happens-before就会发生重排序，会造成发布一个引用的时候，和内部各个field初始化重排序，比如

```
init field a
init field b
发布ref
init field c
```

这时候从使用这角度就会看到一个被部分构造的对象。

错误的延迟初始化将导致不正确的发布，如下代码。这段代码不光有race condition、创建低效等问题还存储在另外一个线程会看到部分构造的Resource实例引用。

```
@NotThreadSafe
public class UnsafeLazyInitialization {
    private static Resource resource;
 
    public static Resource getInstance() {
        if (resource == null)
            resource = new Resource(); // unsafe publication
        return resource;
    }
 
    static class Resource {
    }
}
```

那么，除非使用final，或者发布操作线程在使用线程开始之前执行，这些都满足了happens-before原则。

#### 16.2.2 安全的发布

使用第三章的各种技术可以安全发布对象，去报发布对象的操作在使用对象的线程开始使用对象的引用之前执行。如果A将X放入BlockingQueue，B从队列中获取X，那么B看到的X与A放入的X相同，实际上由于使用了锁保护，实际B能看到A移交X之前所有的操作。

#### 16.2.3 安全的初始化模式

有时候需要延迟初始化，最简单的方法：

```
@ThreadSafe
public class SafeLazyInitialization {
    private static Resource resource;
 
    public synchronized static Resource getInstance() {
        if (resource == null)
            resource = new Resource();
        return resource;
    }
 
    static class Resource {
    }
}
```

如果getInstance调用不频繁，这绝对是最佳的。

在初始化中使用static会提供额外的线程安全保证。静态初始化是由JVM在类的初始化阶段执行，并且在类被加载后，在线程使用前的。静态初始化期间，内存写入操作将自动对所有线程可见。因此静态初始化对象不需要显示的同步。下面的代码叫做eager initialization。

```
@ThreadSafe
public class EagerInitialization {
    private static Resource resource = new Resource();
 
    public static Resource getResource() {
        return resource;
    }
 
    static class Resource {
    }
}
```

下面是lazy initialization。JVM推迟ResourceHolder的初始化操作，直到开始使用这个类时才初始化，并且通过一个static来做，不需要额外的同步。

```
@ThreadSafe
public class ResourceFactory {
    private static class ResourceHolder {
        public static Resource resource = new Resource();
    }
 
    public static Resource getResource() {
        return ResourceFactory.ResourceHolder.resource;
    }
 
    static class Resource {
    }
}
```

#### 16.2.4 双重检查加锁CDL

DCL实际是一种糟糕的方式，是一种anti-pattern，它只在JAVA1.4时代好用，因为早期同步的性能开销较大，但是现在这都不是事了，已经不建议使用。

```
@NotThreadSafe
public class DoubleCheckedLocking {
    private static Resource resource;
 
    public static Resource getInstance() {
        if (resource == null) {
            synchronized (DoubleCheckedLocking.class) {
                if (resource == null)
                    resource = new Resource();
            }
        }
        return resource;
    }
 
    static class Resource {
 
    }
}
```

初始化instance变量的伪代码如下所示：

```
memory = allocate();   //1：分配对象的内存空间
ctorInstance(memory);  //2：初始化对象
instance = memory;     //3：设置instance指向刚分配的内存地址
```

之所以会发生上面我说的这种状况，是因为在一些编译器上存在指令排序，初始化过程可能被重排成这样：

```
memory = allocate();   //1：分配对象的内存空间
instance = memory;     //3：设置instance指向刚分配的内存地址
                       //注意，此时对象还没有被初始化！
ctorInstance(memory);  //2：初始化对象

```

而volatile存在的意义就在于禁止这种重排！解决办法是声明为volatile类型。这样就可以用DCL了。

```
@NotThreadSafe
public class DoubleCheckedLocking {
    private static volatile Resource resource;
 
    public static Resource getInstance() {
        if (resource == null) {
            synchronized (DoubleCheckedLocking.class) {
                if (resource == null)
                    resource = new Resource();
            }
        }
        return resource;
    }
 
    static class Resource {
 
    }
}
```

### 16.3 初始化过程中的安全性

final不会被重排序。

下面的states因为是final的所以可以被安全的发布。即使没有volatile，没有锁。但是，如果除了构造函数外其他方法也能修改states。如果类中还有其他非final域，那么其他线程仍然可能看到这些域上不正确的值。也导致了构造过程中的escape。

写final的重排规则：

- JMM禁止编译器把final域的写重排序到构造函数之外。
- 编译器会在final域的写之后，构造函数return之前，插入一个StoreStore屏障。这个屏障禁止处理器把final域的写重排序到构造函数之外。也就是说：写final域的重排序规则可以确保：在对象引用为任意线程可见之前，对象的final域已经被正确初始化过了。

读final的重排规则：

- 在一个线程中，初次读对象引用与初次读该对象包含的final域，JMM禁止处理器重排序这两个操作（注意，这个规则仅仅针对处理器）。编译器会在读final域操作的前面插入一个LoadLoad屏障。也就是说：读final域的重排序规则可以确保：在读一个对象的final域之前，一定会先读包含这个final域的对象的引用。

如果final域是引用类型，那么增加如下约束：

- 在构造函数内对一个final引用的对象的成员域的写入，与随后在构造函数外把这个被构造对象的引用赋值给一个引用变量，这两个操作之间不能重排序。（个人觉得基本意思也就是确保在构造函数外把这个被构造对象的引用赋值给一个引用变量之前，final域已经完全初始化并且赋值给了当前构造对象的成员域，至于初始化和赋值这两个操作则不确保先后顺序。）

```
@ThreadSafe
public class SafeStates {
    private final Map<String, String> states;
 
    public SafeStates() {
        states = new HashMap<String, String>();
        states.put("alaska", "AK");
        states.put("alabama", "AL");
        /*...*/
        states.put("wyoming", "WY");
    }
 
    public String getAbbreviation(String s) {
        return states.get(s);
    }
}
```

