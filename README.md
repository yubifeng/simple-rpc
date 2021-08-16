## 一个简单的RPC框架
本项目基于netty+zookeeper+ProtoStuff等，是一个轻量级的RPC框架。通过造轮子的方式，可以深入理解RPC的底层细节。
### RPC介绍
RPC，即 Remote Procedure Call（远程过程调用），是通过网络调用远程计算机上的服务，它屏蔽了底层网络协议等实现细节，就像调用本地服务一样。它是分布式的基础。

#### RPC的步骤
![调用示意图](https://i.loli.net/2021/08/16/7SdVpcAWD2oujRr.jpg)
1. 服务提供者在启动时，向注册中心注册自己提供的服务。
1. 客户端执行远程方法时调用代理类，将类名、方法名、参数类型、参数封装传给client stub
2. client stub将消息体反序列化并加上消息头构成完整协议
3. client stub发现服务后，选择一个，与服务端建立连接并发送出去
4. 服务端收到数据包后，server stub 需要进行解析反序列化为类名、方法名和参数等信息。
5. server stub调用对应的本地方法，并把执行结果返回给客户端

### 实现的功能
- 实现jdk和cglib两种动态代理
- 多种序列化（FST、Hessian2、Json、KRYO、PROTOBUF）
- 自定义协议(设置编解码器)
- 使用netty作为通信框架（NIO）
- 解决TCP粘包问题
- 心跳检测和长连接
- 使用zookeeper作为注册中心
- 自定义注解实现自动发现注册服务
- 设置版本号
- 简单的负载均衡
- 连接复用
- 服务端增加线程池提高消息处理能力
- 退出服务器自动注销所有注册的服务
### 下一步优化
- [ ] 增加javassist动态代理
- [ ] 扩展负载均衡算法 加权轮询、一致性哈希、最小连接及加权最小连接、ip或url散列
- [ ] 配置中心，通过配置文件指定序列化器、负载均衡器、动态代理方式等
- [ ] 集成到Spring中
- [ ] 增加统一管理控制台
- [ ] 增加统计服务的调用次数和调用时间的监控中心

### 用到的技术栈
- RPC的概念及运作流程
- RPC协议及RPC框架的概念
- Netty的基本使用
- 序列化及反序列化技术
- Zookeeper的基本使用（注册中心）
- 自定义Java注解实现特殊业务逻辑
- Java的动态代理
- Java反射机制
- TCP的粘包拆包
- 心跳机制和长连接
- Java多线程


### 参考
- [SnailClimb / guide-rpc-framework](https://github.com/Snailclimb/guide-rpc-framework)
- [烟味i](https://www.cnblogs.com/2YSP/p/13545217.html)