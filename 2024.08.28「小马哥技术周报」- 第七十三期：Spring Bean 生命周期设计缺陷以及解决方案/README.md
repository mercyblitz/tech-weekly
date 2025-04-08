> ### 主要内容
> + Spring Framework 6.1 新特性
> + Spring Bean 生命周期
>     - 设计缺陷
>     - 解决方案
> + ~~Spring AOT ~~
>

## Spring 事件（Event）
Event Sourcing

Spring Framework 事件接口

+ ApplicationEvent 
+ ApplicationListener
    - 作为普通对象注册
        * 直接插入 ApplicationListener 对象
    - 作为 Spring Bean 对象注册
        * @Bean
        * @Component
        * 其他 Spring Bean 注册方式
    - 通过 @EventListener 来注册
        * 适配器实现
            + Spring IoC 容器启动过程中
                - 依赖查找 <font style="color:#080808;background-color:#ffffff;">EventListenerFactory Beans</font>
                    * <font style="color:#080808;background-color:#ffffff;">其中 DefaultEventListenerFactory 就是 @EventListener 底层实现</font>
                        + <font style="color:#080808;background-color:#ffffff;">ApplicationListenerMethodAdapter</font>
                            - <font style="color:#080808;background-color:#ffffff;">事件处理阶段在于 afterSingletonsInstantiated 方法回调</font>
                                * <font style="color:#080808;background-color:#ffffff;">晚于 Bean 初始化后回调</font>
        * 事件监听时机
            + 需要在 Spring IoC 容器处理完非单例延迟 Bean 之后



## Spring Bean 生命周期
+ BeanPostProcessor
    - Bean 初始化生命周期
+ <font style="color:#080808;background-color:#ffffff;">SmartInitializingSingleton</font>
    - <font style="color:#080808;background-color:#ffffff;">所有单例非延迟 Beans Ready 后生命周期</font>

## Spring Framework 6.1 新特性










