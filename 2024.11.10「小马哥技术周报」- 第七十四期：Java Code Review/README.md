> 主要内容
>
> + [@极海Channel](https://space.bilibili.com/1525355/) 大佬讨论的[高级切面编程 ](https://www.bilibili.com/video/BV1YiDBY8EJt)
> + [@J3code](https://space.bilibili.com/236629125) 大佬讨论的 [Hashcode 实现](https://www.bilibili.com/video/BV1bMSmY2ExY)
> + 训练营小伙伴提到的 [Java 反射性能问题](https://t.zsxq.com/KfP5L)
>



## 项目亮点实战！横向能力抽象！
需求：实现一个限流 AOP 切面

注解：@LimitCheck，能够提供简单地配置，通过细粒度地控制重试限制

切面注解在  @Controller Bean 上的方法。



### 实现瑕疵
#### Bean 被过早地初始化
首先，LimitCheckAspect 类是 Spring Component（Bean），所以它属于 Spring IoC 容器被管理的成员，在其的初始化中，强行地所有的 Spring Beans 初始化，可能会导致 Bean 过早初始化

#### @LimitCheck 切面搜索范围太大
只需要定位在 @Controller Bean 之上就行。

#### @PostConstruct 尽可能“避免” Spring Bean 中使用
在 Java EE 生态中，@PostConstruct 属于 Common Annotations 规范，不过目前最新的规范隶属于 Jakarta EE，所以前后版本的类 Package 不同，会导致源代码级别兼容性。

@PostConstruct 使用 Spring InitializingBean 接口

##### 解析目标方法依赖于 Spring Bean 的初始化
尽可能地避免获取目标方法时，将 Spring Bean 无效地初始化。

+ 假设通过 Java 反射来处理，需要得到目标 Bean 类即可，不需要 Bean 对象。通常 BeanDefinition 通过 Class 来注册，可以通过 getBeanClass() 或者 getBeanClassName()
+ 如果通过字节码来处理，没有 Class 对象，通过 <font style="color:#080808;background-color:#ffffff;">ClassVisitor</font> 处理。



### 实现优化
#### 通过 ApplicationListener<ContextRefreshedEvent> 来替代 Bean @PostConstruct
ContextRefreshedEvent 相当于当前 Spring 应用上下文已经完全启动

#### 通过 <font style="color:#080808;background-color:#ffffff;">SmartInitializingSingleton 来替代 Bean @PostConstruct</font>
<font style="color:#080808;background-color:#ffffff;">SmartInitializingSingleton 会在 Spring 单例非延迟 Bean 初始化之后调用</font>

<font style="color:#080808;background-color:#ffffff;">必须在 Spring 4.1+ 的版本出现</font>

#### <font style="color:#080808;background-color:#ffffff;">通过 BeanDefinition 获取 Bean Class 定义，从而获取目标方法 Method 对象</font>
#### 通过 APT 技术 在 Java 编译时获取目标 Method 信息
#### 业务异常信息的捕获可以通过 Spring Web 中的 @ExceptionHandler
#### 通过 Spring Aspect 扩展获取注解对象
```java
@Before("com.xyz.Pointcuts.publicMethod() && @annotation(auditable)") 
public void audit(Auditable auditable) {
	AuditCode code = auditable.value();
	// ...
}
```

#### 通过 Spring WebMVC HandlerInterceptor
```java
default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		return true;
	}
```

handler 参数通常是 Spring Web <font style="color:#080808;background-color:#ffffff;">HandlerMethod 对象，该对象会关联 Method 引用。</font>

#### <font style="color:#080808;background-color:#ffffff;">通过 RequestMappingHandlerMapping 在 Spring 容器初始化完成时，获取所有的 HandlerMethod</font>


<font style="color:#080808;background-color:#ffffff;">RequestMappingHandlerMapping#getHandlerMethods() 拿到所有的 HandlerMethod 集合</font>

<font style="color:#080808;background-color:#ffffff;"></font>

<font style="color:#080808;background-color:#ffffff;">RequestMappingHandlerMapping 在初始化阶段，拿到所有的 Bean Class，进一步获取 HandlerMethod 对象。</font>

<font style="color:#080808;background-color:#ffffff;"></font>

<font style="color:#080808;background-color:#ffffff;"></font>

## 3年Java裸辞，真实的 25k 面试题，复盘 | hashcode 问题


User

+ String name
+ int age
+ int gender



User hashCode()



HashMap Hashcode 碰撞问题

未看过 String hashCode() 方法实现



### 实现瑕疵
#### 错误地使用 String#intern() 方法
```java
    public int hashCode(){
        String str = name + age + gender;
        str.intern(); // String 从变量放入常量池
        return str.hashCode();
    }
```

JVM 常量池是有限制，User 对象组合可能无限。

String hashCode() 方法是由缓存的：

String.class JDK 9+:

```java
    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            hash = h = isLatin1() ? StringLatin1.hashCode(value)
                                  : StringUTF16.hashCode(value);
        }
        return h;
    }
```

#### 在 hashCode() 方法中，尽可能地避免 String 与其他类型数据拼接
```java
    public int hashCode() {
        String str = name + age + gender; // 局部对象 str 仅由于 String hashCode 计算，产生临时对象，增加了 YGC 压力。
        return str.hashCode();
    }
```

< JDK 9，String 拼接通常会被转化成 StringBuilder

JDK 9，String 字节码提升来拼接 String



#### hashCode() 直接成员 hashCode 累加
假设两个 User 对象 name 是相同的，age + gender 累加和也是相同的，那么出现 hashCode

name = zhangsan, age  =19, gender = 1

name = zhangsan, age = 20, gender = 0



### 实现优化
#### 移除 String#intern() 方法
#### 通过个成员 hashCode * 31 累加
```java
    public int hashCode() {
        int hash = name.hashCode();
        hash = 31 * hash + age;
        hash = 31 * hash + gender;
        return hash;
    }
```

比大 31 的质素还是存在，容易出现 int 越界

Objects#hashCode 方法计算



#### 通过整数站“位”
<font style="color:rgb(24, 25, 28);">我提出一个建议 将 gender 做整体 hashCode 的个位，age 当作整体 hashCode 十位到千位，再将String hashCode 乘上 10000，将三者累加，那么不同的性别和年龄生成的后四位是不同的，相同的概率为1/200，再通过判断 String hashCode，类似的算法可以减少整体 hashCode 重复率。</font>

