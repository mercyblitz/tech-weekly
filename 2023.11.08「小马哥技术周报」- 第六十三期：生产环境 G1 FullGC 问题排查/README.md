# 问题描述
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699241378130-f8177cd7-4a2c-48bd-a1c3-d6f65b4006bc.png#averageHue=%23f0f0f0&clientId=uc5eea495-4e48-4&from=paste&id=uda5fd143&originHeight=638&originWidth=2078&originalType=binary&ratio=2.5&rotation=0&showTitle=false&size=116738&status=done&style=none&taskId=uf9f1e8b5-9012-4d18-8185-91125caa7ec&title=)
## GC 日志
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699241455446-b7f7e8be-8678-46d0-b83d-5f309a31e23f.png#averageHue=%23f0efef&clientId=uc5eea495-4e48-4&from=paste&height=342&id=MfVMI&originHeight=856&originWidth=1248&originalType=binary&ratio=2.5&rotation=0&showTitle=false&size=118921&status=done&style=none&taskId=u19ba45bd-e5c7-455b-9f2b-c7663edc3a9&title=&width=499.2)
[gc_2023-10-28_20-15-38.log](https://www.yuque.com/attachments/yuque/0/2023/log/222258/1699241035771-cadd15e1-c691-4e1f-98a3-33edf6552b6e.log?_lake_card=%7B%22src%22%3A%22https%3A%2F%2Fwww.yuque.com%2Fattachments%2Fyuque%2F0%2F2023%2Flog%2F222258%2F1699241035771-cadd15e1-c691-4e1f-98a3-33edf6552b6e.log%22%2C%22name%22%3A%22gc_2023-10-28_20-15-38.log%22%2C%22size%22%3A52391227%2C%22ext%22%3A%22log%22%2C%22source%22%3A%22%22%2C%22status%22%3A%22done%22%2C%22download%22%3Atrue%2C%22taskId%22%3A%22ub89fcab2-6750-4578-a339-0d9d248176f%22%2C%22taskType%22%3A%22upload%22%2C%22type%22%3A%22%22%2C%22__spacing%22%3A%22both%22%2C%22mode%22%3A%22title%22%2C%22id%22%3A%22u9559efae%22%2C%22margin%22%3A%7B%22top%22%3Atrue%2C%22bottom%22%3Atrue%7D%2C%22card%22%3A%22file%22%7D)
## 流量监控
## ![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699240647302-a1dd60f8-171c-40d9-8f6c-54bcddadbfb2.png#averageHue=%23fefefe&clientId=uc5eea495-4e48-4&from=paste&id=dRixJ&originHeight=603&originWidth=1309&originalType=binary&ratio=2.5&rotation=0&showTitle=false&size=88544&status=done&style=none&taskId=u4a8301fd-503b-4f3d-9ceb-53b2f6c6bb3&title=)
# 问题分析
## 猜测原因：是否出现流量激增
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699243171695-35f2db73-2713-4a9e-b584-f7fd7181cf1d.png#averageHue=%23f0f0f0&clientId=uc5eea495-4e48-4&from=paste&id=u554ba4cd&originHeight=458&originWidth=1037&originalType=binary&ratio=2.5&rotation=0&showTitle=false&size=67539&status=done&style=none&taskId=ubcdc4452-a1fd-498e-849e-5b61ec45090&title=)

## 初步分析
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699243213233-d1199d4e-651d-43f3-9a48-adbcb7b82cea.png#averageHue=%23ededed&clientId=uc5eea495-4e48-4&from=paste&id=uf547d037&originHeight=595&originWidth=724&originalType=binary&ratio=2.5&rotation=0&showTitle=false&size=90816&status=done&style=none&taskId=u641a51d7-d8f9-4cb0-be84-60f9eb7bc25&title=)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699243253617-425529d8-7b83-4e0d-8462-7935a879df6d.png#averageHue=%23edecec&clientId=uc5eea495-4e48-4&from=paste&id=uf492bd1c&originHeight=293&originWidth=623&originalType=binary&ratio=2.5&rotation=0&showTitle=false&size=38316&status=done&style=none&taskId=ue21e075a-d4f7-4316-89b3-d754d5acb5d&title=)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699243281448-e1672962-77ae-4ae6-a70d-e454d7428eda.png#averageHue=%23ededed&clientId=uc5eea495-4e48-4&from=paste&id=u7f4f2ba3&originHeight=349&originWidth=564&originalType=binary&ratio=2.5&rotation=0&showTitle=false&size=44111&status=done&style=none&taskId=ud033b95f-89a5-497f-99b6-d9fdd3ab06f&title=)
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699245957254-25674e13-f0a3-4d1a-bf75-4d4d185000b9.png#averageHue=%23ececec&clientId=uc5eea495-4e48-4&from=paste&id=u15bc84ee&originHeight=300&originWidth=642&originalType=binary&ratio=2.5&rotation=0&showTitle=false&size=37449&status=done&style=none&taskId=u96f1ac0e-b1d9-4367-89bf-c9b64d57b19&title=)

## 日志分析
### 基本信息
#### JDK 版本
OpenJDK 64-Bit Server VM (25.202-b08) for linux-amd64 JRE (1.8.0_202-b08), built on Jan 22 2019 13:35:07 by "jenkins" with gcc 4.8.2 20140120 (Red Hat 4.8.2-15)
#### 内存信息
Memory: 4k page, physical 16777216k(16773588k free), swap 0k(0k free)
#### JVM 启动参数
-XX:CompressedClassSpaceSize=528482304 -XX:ConcGCThreads=4 -XX:G1ReservePercent=15 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/home/work/logs/applogs/ -XX:InitialHeapSize=8589934592 -XX:InitiatingHeapOccupancyPercent=35 -XX:MaxHeapSize=8589934592 -XX:MaxMetaspaceSize=536870912 -XX:MetaspaceSize=536870912 -XX:NativeMemoryTracking=detail -XX:ParallelGCThreads=8 -XX:+PrintCommandLineFlags -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -XX:ThreadStackSize=512 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseFastAccessorMethods -XX:+UseG1GC 
### 日志分析
#### YGC 日志节选
```
2023-10-28T20:15:47.042+0800: 8.783: [GC pause (G1 Evacuation Pause) (young), 0.0312390 secs]
   [Parallel Time: 11.1 ms, GC Workers: 8]
      [GC Worker Start (ms): Min: 8785.2, Avg: 8785.3, Max: 8785.3, Diff: 0.2]
      [Ext Root Scanning (ms): Min: 0.8, Avg: 1.4, Max: 4.1, Diff: 3.4, Sum: 11.5]
      [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
         [Processed Buffers: Min: 0, Avg: 0.0, Max: 0, Diff: 0, Sum: 0]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.2]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.2, Max: 0.6, Diff: 0.6, Sum: 1.7]
      [Object Copy (ms): Min: 6.7, Avg: 9.0, Max: 9.6, Diff: 2.9, Sum: 71.9]
      [Termination (ms): Min: 0.0, Avg: 0.1, Max: 0.3, Diff: 0.3, Sum: 1.1]
         [Termination Attempts: Min: 1, Avg: 161.5, Max: 212, Diff: 211, Sum: 1292]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.3]
      [GC Worker Total (ms): Min: 10.7, Avg: 10.8, Max: 10.9, Diff: 0.2, Sum: 86.6]
      [GC Worker End (ms): Min: 8796.1, Avg: 8796.1, Max: 8796.1, Diff: 0.1]
   [Code Root Fixup: 0.1 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.3 ms]
   [Other: 19.7 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 16.8 ms]
      [Ref Enq: 0.1 ms]
      [Redirty Cards: 0.1 ms]
      [Humongous Register: 1.8 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.3 ms]
   [Eden: 408.0M(408.0M)->0.0B(376.0M) Survivors: 0.0B->32.0M Heap: 408.0M(8192.0M)->30.7M(8192.0M)]
Heap after GC invocations=237 (full 0):
 garbage-first heap   total 8388608K, used 1937408K [0x00000005e0800000, 0x00000005e0c04000, 0x00000007e0800000)
  region size 4096K, 5 young (20480K), 5 survivors (20480K)
 Metaspace       used 251356K, capacity 279246K, committed 296064K, reserved 1310720K
  class space    used 26669K, capacity 31324K, committed 34176K, reserved 1048576K
}
Heap after GC invocations=1 (full 0):
 garbage-first heap   total 8388608K, used 31463K [0x00000005e0800000, 0x00000005e0c04000, 0x00000007e0800000)
  region size 4096K, 8 young (32768K), 8 survivors (32768K)
 Metaspace       used 29936K, capacity 30724K, committed 30976K, reserved 1077248K
  class space    used 3885K, capacity 4086K, committed 4096K, reserved 1048576K
}
 [Times: user=0.10 sys=0.01, real=0.04 secs]
```
##### 第1行日志
> 2023-10-28T20:15:47.042+0800: 8.783: [GC pause (G1 Evacuation Pause) (young), 0.0312390 secs]

- 2023-10-28T20:15:47.042+0800 - 由于 JVM 启动参数 `-XX:+PrintGCDateStamps `控制的时间输出格式
- 8.783 - 相对于 JVM 启动时间的 timestamp
- G1 Evacuation Pause (young)- GC 类型，YGC Evacuation 停顿
- 0.0312390 secs - 本次 GC 执行时间
#### 第2行日志
> [Parallel Time: 11.1 ms, GC Workers: 8]

- Parallel Time - 并行 GC 任务的 Stop-The-World 时间
- GC Workers - GC 工作线程数量，当前数值为 8，符合 `-XX:ParallelGCThreads=8`参数的设定

#### 第3行日志
> [GC Worker Start (ms): Min: 8785.2, Avg: 8785.3, Max: 8785.3, Diff: 0.2]

- GC Worker Start - GC 工作线程开始 timestamp，其中 Min 为最早的 GC 工作线程启动时间，Avg 为 GC 工作线程的平均启动时间，Max 为最后 GC 工作线程的启动时间
#### 第4行日志
> [Ext Root Scanning (ms): Min: 0.8, Avg: 1.4, Max: 4.1, Diff: 3.4, Sum: 11.5]

- Ext Root Scanning - 扫描外部 roots（线程栈 roots、JNI、全局变量、系统字典等）以查找任何进入当前收集集合的根所花费的时间（单位：毫秒）

#### 第5-6行日志
> [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
> [Processed Buffers: Min: 0, Avg: 0.0, Max: 0, Diff: 0, Sum: 0]

- Update RS - 每个区域都有自己的记忆集(Remembered Set or RSet)。它跟踪包含某个区域中引用的卡的地址。当写入发生时，写入后屏障通过将新引用的卡标记为脏卡并放入日志缓冲区或脏卡队列来管理区域间引用的更改。一旦完成，并发优化线程将与运行的应用程序线程并行处理这些队列。Update RS 使 GC Worker 能够处理在集合开始之前未处理的任何未处理缓冲区。这样可以确保每个RSet都是最新的
- Processed Buffers - 在更新RS期间处理了多少个更新缓冲区

#### 第7行日志
> [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.2]

- Scan RS - 扫描每个区域的记忆集以查找指向收集集合区域的引用的时间（单位：毫秒）

#### 第8行日志
> [Code Root Scanning (ms): Min: 0.0, Avg: 0.2, Max: 0.6, Diff: 0.6, Sum: 1.7]

- Code Root Scanning - 扫描已编译源代码的 Roots 以查找对收集集合的引用所花费的时间

##### 第9行日志
> [Object Copy (ms): Min: 6.7, Avg: 9.0, Max: 9.6, Diff: 2.9, Sum: 71.9]

- Object Copy - 在 Evacuation 暂停期间，必须 Evacuation 集合中的所有区域。Object Copy 负责将所有剩余的活动对象复制到新区域

#### 第10 - 11行日志
> [Termination (ms): Min: 0.0, Avg: 0.1, Max: 0.3, Diff: 0.3, Sum: 1.1]
> [Termination Attempts: Min: 1, Avg: 161.5, Max: 212, Diff: 211, Sum: 1292]

- Termination - 当  GC 工作线程完成时，它进入一个终止例程，在那里它与其他工作线程同步，并试图窃取未完成的任务。时间表示从 GC 工作线程第一次尝试终止到实际终止所用的时间
- Termination Attempts - 如果 GC 工作线程成功窃取任务，它将重新进入终止例程，并尝试窃取更多工作或终止。每次任务被盗并重新输入终止时，尝试终止的次数都会增加

#### 第12 - 14行日志
> [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.3]
> [GC Worker Total (ms): Min: 10.7, Avg: 10.8, Max: 10.9, Diff: 0.2, Sum: 86.6]
> [GC Worker End (ms): Min: 8796.1, Avg: 8796.1, Max: 8796.1, Diff: 0.1]

- GC Worker Other - 表示在未计入以前任务的任务上花费的时间
- GC Worker Total - 每个并行工作线程花费的时间的最小值、最大值、平均值、差异和总和
- GC Worker End - GC 工作线程结束时JVM启动后的最小/最大时间戳。diff表示第一个线程和最后一个线程结束之间的时间（以毫秒为单位）。理想情况下，你希望它们在同一时间迅速结束
#### 第15 - 17行日志
> [Code Root Fixup: 0.1 ms]
> [Code Root Purge: 0.0 ms]
> [Clear CT: 0.3 ms]

- Code Root Fixup - 遍历标记的方法，这些方法指向CSet以修复GC期间可能移动的任何指针
- Code Root Purge - 清除代码根表中的条目
- Clear CT - Card Table 清除污染 Card


JVM C++ 手动回收

JIT -> 优化和反优化

#### 第18 - 25 行日志
> [Other: 19.7 ms]
> [Choose CSet: 0.0 ms]
>        [Ref Proc: 16.8 ms]
>        [Ref Enq: 0.1 ms]
>        [Redirty Cards: 0.1 ms]
>        [Humongous Register: 1.8 ms]
>        [Humongous Reclaim: 0.0 ms]
>        [Free CSet: 0.3 ms]

- Choose CSet - 选择区域作为收集集合
- Ref Proc - STW 引用处理器处理任意被发现的软、弱、幻象、final 以及 JNI 引用
- Ref Enq - 循环引用并将它们排入挂起列表
- Reditry Cards - 在收集过程中修改的卡被标记为脏卡
- Humongous Register - 启用“G1ReclaimDeadHumongousObjectsAtYoungGC”（JDK8u60中添加的默认true/功能）后，G1将尝试在YoungGC期间急切地收集Humongous区域。这代表了评估 Humongous 区域是否是渴望开垦的候选地区并将其记录下来所花的时间。有效的候选者将没有现有的强代码根，并且在记忆集中只有稀疏条目。每个候选者都将其记忆集刷新到脏卡队列中，如果清空，则该区域将添加到当前集合中
- Humongous Reclaim - 确保 Humongous 对象死并清理、释放区域、重置区域类型、将区域返回到可用列表并占用释放空间所花费的时间
- Free CSet - 现在 evacuated 区域被添加回空闲列表

#### 第26行日志
> [Eden: 408.0M(408.0M)->0.0B(376.0M) Survivors: 0.0B->32.0M Heap: 408.0M(8192.0M)->30.7M(8192.0M)]

- Eden: 408.0M(408.0M)->0.0B(376.0M) - YGC 触发，Young 区域已满，408.0MB空间分配，Eden 空间腾挪出 376.0MB
- Survivors: 0.0B->32.0M - Survivors 空间从 0.0B 增长到 32.0MB
- Heap: 408.0M(8192.0M)->30.7M(8192.0M)] - YGC 前 408.0MB 堆空间被分配，YGC 后只剩 30.7MB 空间占据，最大堆空间为 8192.0MB

# 基础知识
## G1 垃圾收集器



