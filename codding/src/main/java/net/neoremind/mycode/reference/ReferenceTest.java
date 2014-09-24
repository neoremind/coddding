package net.neoremind.mycode.reference;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import org.junit.Test;

/**
 * 本文介绍对象的强、软、弱和虚引用的概念
1．对象的强、软、弱和虚引用
   在JDK 1.2以前的版本中，若一个对象不被任何变量引用，那么程序就无法再使用这个对象。也就是说，只有对象处于可触及（reachable）状态，程序才能使用它。从JDK 1.2版本开始，把对象的引用分为4种级别，从而使程序能更加灵活地控制对象的生命周期。这4种级别由高到低依次为：强引用、软引用、弱引用和虚引用。图1为对象应用类层次。

⑴强引用（StrongReference）
    强引用是使用最普遍的引用。如果一个对象具有强引用，那垃圾回收器绝不会回收它。当内存空间不足，Java虚拟机宁愿抛出OutOfMemoryError错误，使程序异常终止，也不会靠随意回收具有强引用的对象来解决内存不足的问题。
⑵软引用（SoftReference）
    如果一个对象只具有软引用，则内存空间足够，垃圾回收器就不会回收它；如果内存空间不足了，就会回收这些对象的内存。只要垃圾回收器没有回收它，该对象就可以被程序使用。软引用可用来实现内存敏感的高速缓存（下文给出示例）。
软引用可以和一个引用队列（ReferenceQueue）联合使用，如果软引用所引用的对象被垃圾回收器回收，Java虚拟机就会把这个软引用加入到与之关联的引用队列中。
⑶弱引用（WeakReference）
    弱引用与软引用的区别在于：只具有弱引用的对象拥有更短暂的生命周期。在垃圾回收器线程扫描它所管辖的内存区域的过程中，一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。不过，由于垃圾回收器是一个优先级很低的线程，因此不一定会很快发现那些只具有弱引用的对象。
弱引用可以和一个引用队列（ReferenceQueue）联合使用，如果弱引用所引用的对象被垃圾回收，Java虚拟机就会把这个弱引用加入到与之关联的引用队列中。
⑷虚引用（PhantomReference）
    “虚引用”顾名思义，就是形同虚设，与其他几种引用都不同，虚引用并不会决定对象的生命周期。如果一个对象仅持有虚引用，那么它就和没有任何引用一样，在任何时候都可能被垃圾回收器回收。
虚引用主要用来跟踪对象被垃圾回收器回收的活动。虚引用与软引用和弱引用的一个区别在于：虚引用必须和引用队列 （ReferenceQueue）联合使用。当垃圾回收器准备回收一个对象时，如果发现它还有虚引用，就会在回收对象的内存之前，把这个虚引用加入到与之 关联的引用队列中。
ReferenceQueue queue = new ReferenceQueue ();
PhantomReference pr = new PhantomReference (object, queue);
   程序可以通过判断引用队列中是否已经加入了虚引用，来了解被引用的对象是否将要被垃圾回收。如果程序发现某个虚引用已经被加入到引用队列，那么就可以在所引用的对象的内存被回收之前采取必要的行动。
   
 * @author zhangxu
 *
 */
public class ReferenceTest {

	@Test  
	public void strongReference() {  
	    Object referent = new Object();  
	      
	    /** 
	     * 通过赋值创建 StrongReference  
	     */  
	    Object strongReference = referent;  
	      
	    assertThat(referent, is(strongReference));  
	      
	    referent = null;  
	    System.gc();  
	      
	    /** 
	     * StrongReference 在 GC 后不会被回收 
	     */  
	    assertThat(strongReference, notNullValue());  
	} 
	
	@Test  
	public void weakReference() {  
	    Object referent = new Object();  
	    WeakReference<Object> weakRerference = new WeakReference<Object>(referent);  
	  
	    assertThat(referent, is(weakRerference.get()));  
	      
	    referent = null;  
	    System.gc();  
	      
	    /** 
	     * 一旦没有指向 referent 的强引用, weak reference 在 GC 后会被自动回收 
	     */  
	    assertThat(weakRerference.get(), nullValue());  
	}  
	
	@Test  
	public void weakHashMap() throws InterruptedException {  
	    Map<Object, Object> weakHashMap = new WeakHashMap<Object, Object>();  
	    Object key = new Object();  
	    Object value = new Object();  
	    weakHashMap.put(key, value);  
	  
	    assertThat(weakHashMap.containsValue(value), is(true));  
	      
	    key = null;  
	    System.gc();  
	      
	    /** 
	     * 等待无效 entries 进入 ReferenceQueue 以便下一次调用 getTable 时被清理 
	     */  
	    Thread.sleep(1000);  
	      
	    /** 
	     * 一旦没有指向 key 的强引用, WeakHashMap 在 GC 后将自动删除相关的 entry 
	     */  
	    assertThat(weakHashMap.containsValue(value), is(false));  
	}  
	
	@Test  
	public void softReference() {  
	    Object referent = new Object();  
	    SoftReference<Object> softRerference = new SoftReference<Object>(referent);  
	  
	    assertThat(softRerference.get(), notNullValue());  
	      
	    referent = null;  
	    System.gc();  
	      
	    /** 
	     *  soft references 只有在 jvm OutOfMemory 之前才会被回收, 所以它非常适合缓存应用 
	     */  
	    assertThat(softRerference.get(), notNullValue());  
	}  
	
	
	@Test  
	public void phantomReferenceAlwaysNull() {  
	    Object referent = new Object();  
	    PhantomReference<Object> phantomReference = new PhantomReference<Object>(referent, new ReferenceQueue<Object>());  
	      
	    /** 
	     * phantom reference 的 get 方法永远返回 null  
	     */  
	    assertThat(phantomReference.get(), nullValue());  
	}  
	
	@Test  
	public void referenceQueue() throws InterruptedException {  
	    Object referent = new Object();       
	    ReferenceQueue<Object> referenceQueue = new ReferenceQueue<Object>();  
	    WeakReference<Object> weakReference = new WeakReference<Object>(referent, referenceQueue);  
	      
	    assertThat(weakReference.isEnqueued(), is(false));  
	    Reference<? extends Object> polled = referenceQueue.poll();  
	    assertThat(polled, nullValue());  
	      
	    referent = null;  
	    System.gc();  
	  
	    assertThat(weakReference.isEnqueued(), is(true));  
	    Reference<? extends Object> removed = referenceQueue.remove();  
	    assertThat(removed, notNullValue());  
	}  
	
}
