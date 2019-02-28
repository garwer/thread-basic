##interrupt

早期java中中断一个线程，可以使用抢占式的stop，但是该方法太暴力了官方不推荐使用【因为调用了stop，被stop的线程会立马释放锁，都没来的及做出处理，【好比开车时要停车没找好安全的地方突然停车是件危险的事】、比如说可能还没正确处理完数据就thread death了，导致其他线程读到脏数据】，不给线程完成资源正常释放的机会，很不安全。

#### 使用协同式中断锁的interrupt

```
interrupt:线程中断机制最重要方法,主要用于设置个标志位，是中断的关键，并不是真正中断的方法【立个flag】
【个人觉得就好比开车的时候，路上有个路标[设置好的flag],有的人可能无视掉继续开，这个时候需要司机[开发者]去判断对这个标志的处理】

单纯的立个flag，使用Thread.interrupt()并不会中断一个正在运行的线程，该方法可以在线程受到阻塞的时候【如sleep,wait,join】抛出一个中断信号，使线程提早退出阻塞状态。
```

#### 

### interrupt源码【jvm层面(jni)】

怎么看jdk底层源码，r大的[回答](https://hllvm-group.iteye.com/group/topic/35385#post-236056)

我下的为8的openjdk，以8为例

先去找到interrupt0的位置

openjdk\openjdk-8u40-src-b25-10_feb_2015\openjdk\jdk\src\share\native\java\lang

```c++
{"interrupt0",       "()V",        (void *)&JVM_Interrupt}
```

函数名带"JVM_"前缀说明是HotSpot VM与JDK类库之间的私有接口，具体实现在jvm里，与JDK类库之间的私有接口的实现的入口在[jvm.cpp](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/tip/src/share/vm/prims/jvm.cpp)里3347行

```c++
JVM_ENTRY(void, JVM_Interrupt(JNIEnv* env, jobject jthread))
  JVMWrapper("JVM_Interrupt");

  // Ensure that the C++ Thread and OSThread structures aren't freed before we operate
  oop java_thread = JNIHandles::resolve_non_null(jthread);
  MutexLockerEx ml(thread->threadObj() == java_thread ? NULL : Threads_lock);
  // We need to re-resolve the java_thread, since a GC might have happened during the
  // acquire of the lock
  JavaThread* thr = java_lang_Thread::thread(JNIHandles::resolve_non_null(jthread));
  if (thr != NULL) {
    Thread::interrupt(thr);
  }
JVM_END
```

对应路径[thread.cpp](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/tip/src/share/vm/runtime/thread.cpp)的804行

```c++
void Thread::interrupt(Thread* thread) {
  trace("interrupt", thread);
  debug_only(check_for_dangling_thread_pointer(thread);)
  os::interrupt(thread); //不同系统有不同实现 os表示对应多个系统 表示调用平台的interrupt方法
}
```

以linux为例，在[os.linux](http://hg.openjdk.java.net/jdk8u/jdk8u/hotspot/file/9ce27f0a4683/src/os/linux/vm/os_linux.cpp)4463行

```c++
// interrupt support
void os::interrupt(Thread* thread) {
  assert(Thread::current() == thread || Threads_lock->owned_by_self(),
    "possibility of dangling Thread pointer");

  OSThread* osthread = thread->osthread();

  if (!osthread->interrupted()) { //先判断是否已中断过 若没有
    osthread->set_interrupted(true); //设置中断标志为true
    // More than one thread can get here with the same value of osthread,
    // resulting in multiple notifications.  We do, however, want the store
    // to interrupted() to be visible to other threads before we execute unpark().
    //OrderAccess::fence()目的是保证执行unpark()之前保存的interrupted()状态值可见性 其它线程立即可见 
    OrderAccess::fence();
    ParkEvent * const slp = thread->_SleepEvent ; //如果线程调用了sleep
    if (slp != NULL) slp->unpark() ;//slp->unpark()通过unpark用于唤醒Thread.sleep()
  }

  // For JSR166. Unpark even if interrupt status already was set
  if (thread->is_Java_thread())
    ((JavaThread*)thread)->parker()->unpark();//用于中断LockSupport.park()
  //_ParkEvent用于synchronized同步块和Object.wait()，这里相当于也是通过unpark进行唤醒
  ParkEvent * ev = thread->_ParkEvent ; //_ParkEvent用于synchronized同步块和Object.wait()
  if (ev != NULL) ev->unpark() ; //ev->unpark() 也是通过unpark唤醒

}
```

以Thread.sleep为例

```c++
JVM_ENTRY(void, JVM_Sleep(JNIEnv* env, jclass threadClass, jlong millis))
  JVMWrapper("JVM_Sleep");
  //如果设置时间<0直接抛异常   
  if (millis < 0) {
    THROW_MSG(vmSymbols::java_lang_IllegalArgumentException(), "timeout value is negative");
  }
  //这里通过is_interrupted判断中断状态 如果为true抛出中断异常
  if (Thread::is_interrupted (THREAD, true) && !HAS_PENDING_EXCEPTION) {
    THROW_MSG(vmSymbols::java_lang_InterruptedException(), "sleep interrupted");
  }
  // 保存当前线程状态。。 接下来正常睡眠操作
  // Save current thread state and restore it at the end of this block.
  // And set new thread state to SLEEPING. 
  JavaThreadSleepState jtss(thread);
```

is_interrupted (THREAD, true):判断中断状态

```c++
bool os::is_interrupted(Thread* thread, bool clear_interrupted) {
  assert(Thread::current() == thread || Threads_lock->owned_by_self(),
    "possibility of dangling Thread pointer");

  OSThread* osthread = thread->osthread();

  bool interrupted = osthread->interrupted(); //获取中断标识

  if (interrupted && clear_interrupted) { //如果当前中断标识为true并且参数为true
    osthread->set_interrupted(false); //设置中断标识为false
    // consider thread->_SleepEvent->reset() ... optional optimization
  }

  return interrupted;
}
```

> 以sleep为例，到这可以解释的通为何遇到诸如Object.wait、Thread.sleep和Thread.join的阻塞方法时，如果有设置中断标志将抛出InterruptedException异常，并且重置中断标识。



需要注意的是:

1.InterruptedException抛出不意味着线程必须停止，而是提醒当前线程有中断操作发生，至于接下来怎么处理取决于线程自身。

比如

```
1.不处理
2.异常往外抛
3.停止当前线程，并打印异常信息
```

2.中断复位有两种方式

```
1.抛出InterruptedException异常后将复位
2.Thread.interrupted()
```

将中断标志补上，如果挂起了再中断不生效。

## 总结
中断一个线程，其本意是给这个线程一个通知信号，会影响这个线程内部的一个中断标识位 。这

个线程本身并不会因此而改变状态(如阻塞，终止等)。

1.

调用 interrupt()方法并不会中断一个正在运行的线程。也就是说处于 Running 状态的线

程并不会因为被中断而被终止，仅仅改变了内部维护的中断标识位而已。

2.

若调用 sleep()而使线程处于 TIMED-WATING 状态，这时调用 interrupt()方法，会抛出

InterruptedException,从而使线程提前结束 TIMED-WATING 状态。


许多声明抛出 InterruptedException 的方法(如 Thread.sleep(long mills 方法))，抛出异

常前，都会清除中断标识位，所以抛出异常后，调用 isInterrupted()方法将会返回 false。

4.

中断状态是线程固有的一个标识位，可以通过此标识位安全的终止线程。比如 ,你想终止

一个线程 thread 的时候，可以调用 thread.interrupt()方法，在线程的 run 方法内部可以

根据 thread.isInterrupted()的值来优雅的终止线程。

#join
### Thread.join()有什么用

https://www.cnblogs.com/huangzejun/p/7908898.html

让父【主】线程等待子线程结束之后才能继续运行

​         当我们调用某个线程的这个方法时，这个方法会挂起调用线程，直到被调用线程结束执行，调用线程才会继续执行。

```java
@Slf4j
public class ThreadJoinTest {
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            log.info("线程1执行");
            try {
                Thread.sleep(2000);
                log.info("线程1睡眠结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            log.info("线程2执行");
        });

        t1.start();
        t1.join();
        t2.start();
    }
}
```

join() 将几个并行的线程"**合并为一个单线程"**执行，这里并不是真正的单线程 只是看起来，仍然由多个线程，但因为底层调度会出现调用 join() 的线程进入 TIMED_WAITING 状态，等待 join() 所属线程运行结束后再继续运行

也不是因为子线程结束后唤醒主线程，子线程并非做什么操作，只是由于底层调度引起的。

很多情况下，主线程生成并启动了子线程，需要用到子线程返回的结果，也就是需要主线程需要
在子线程结束后再结束，这时候就要用到 join() 方法。【其实感觉futureTask也可以

#wait
#### wait和sleep的区别、notify和notifyAll

wait和sleep

> 1. 从代码层面来看，有个很重要的区别，即wait使Object类的方法，而sleep是Thread类的方法，且是个静态方法，只对当前线程有效。
> 2. sleep方法没有释放锁，wait方法会释放锁可以使得其它线程
> 3. 使用范围，wait、notify、notifyAll只能在同步方法或同步块，sleep则都可以。
> 4. `sleep需捕获异常`，sleep表示睡眠，当等待一定时间后也不会马上运行，因为线程调度也需要时间，当在sleep的过程中可能被其它对象调用到它的interrupt()方法，那么就会报InterruptedException异常，如果不去捕获这个异常那么线程将会被异常终止，其它则不需要捕获异常。
> 5. wait属于Object的成员方法，一旦一个对象调用了wait，则表示放弃当前对资源的占用权，必须唤醒【notify、notifyAll】才会执行代码

# notify
Object 类中的 notify() 方法，唤醒在此对象监视器上等待的单个线程，如果所有线程都在此对象
上等待，则会选择唤醒其中一个线程，选择是任意 的，并在对实现做出决定时发生，线程通过调
用其中一个 wait() 方法，在对象的监视器上等待，直到当前的线程放弃此对象上的锁定，才能继
续执行被唤醒的线程 ，被唤醒的线程将以常规方式与在该对象上主动同步的其他所有线程进行竞
争。类似的方法还有 notifyAll() ，唤醒再次监视器上等待的所有线程。

随机唤醒-后续根据cpu调度
notify和notifyAll
> 都是用于唤醒wait状态的线程，简要区别如下
>
> notify()只是随机【不设置优先级的情况】让一个线程从wait恢复，而notifyAll()是唤醒所有等待中的线程，
>
> notify是通知一个线程获取锁【无法保证通知哪一个】，notifyAll是通知所有相关的线程去竞争锁。

