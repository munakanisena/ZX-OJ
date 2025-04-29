# OJ 判题学习

## 数据库建表 

### JSON使

存 json 的好处：便于扩展，只需要改变对象内部的字段，而不用修改数据库表（可能会影响数据库）

数据库使用json的前提

存 json 的前提：

1. 你不需要根据某个字段去倒查这条数据
2. 你的字段含义相关，属于同一类的值
3. 你的字段存储空间占用不能太大

###  索引使用

原则上：能不用索引就不用索引；能用单个索引就别用联合 / 多个索引；不要给没区分度的字段加索引（比如性别，就男 / 女）。因为索引也是要占用空间的。



## 三种模式

### 策略模式
不同的情况给出不同的解决方案

不把判题结果的逻辑全写在一个逻辑里面 因为可能会java cpp 的判定逻辑不同

### 工厂模式

### 代理模式





## JAVA操作docker



~~~java
        //docker 客户端
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
//        PingCmd pingCmd = dockerClient.pingCmd();
//        pingCmd.exec();

        //拉取镜像
        String img="nginx:latest";
//        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(img);
//        //回调函数
//        PullImageResultCallback pullImageResultCallback = new PullImageResultCallback(){
//            @Override
//            public void onNext(PullResponseItem item) {
//                System.out.println(item.getStatus());
//                super.onNext(item);
//            }
//        };
//        pullImageCmd.exec(pullImageResultCallback)
//                    .awaitCompletion();
//        System.out.println("下载完成了");

        //创建容器
        CreateContainerResponse exec = dockerClient.createContainerCmd(img).
                withCmd("ech","Hello Docker")
                .exec();
        String containerId = exec.getId();
        System.out.println(exec);

        //查看容器信息
        List<Container> containerList = dockerClient.listContainersCmd()
            //不启动的也一并展示    
            	.withShowAll(true)
                .exec();
        for (Container container :containerList) {
            System.out.println(container);
        }

        //启动容器
        dockerClient.startContainerCmd(containerId).exec();

        //查看日志
        LogContainerResultCallback logContainerResultCallback = new LogContainerResultCallback() {
            @Override
            public void onNext(Frame item) {
                System.out.println("日志：" + new String(item.getPayload()));
                super.onNext(item);
            }
        };
        dockerClient.logContainerCmd(containerId)
                .withStdOut(true)
                .withStdErr(true)
                .exec(logContainerResultCallback)
                .awaitCompletion();

        //删除容器(强制删除)
        dockerClient.removeContainerCmd(containerId).withForce(true).exec();

        //删除镜像
        dockerClient.removeImageCmd(img).exec();
~~~





#### 操作已经启动的容器

~~~shell
 docker exec [OPTIONS] CONTAINER COMMAND [ARG...]
~~~





#### 获取容器操作的内存

~~~java
// 获取占用的内存
StatsCmd statsCmd = dockerClient.statsCmd(containerId);
ResultCallback<Statistics> statisticsResultCallback = statsCmd.exec(new ResultCallback<Statistics>() {

    @Override
    public void onNext(Statistics statistics) {
        System.out.println("内存占用：" + statistics.getMemoryStats().getUsage());
        maxMemory[0] = Math.max(statistics.getMemoryStats().getUsage(), maxMemory[0]);
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void onStart(Closeable closeable) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
});
statsCmd.exec(statisticsResultCallback);

~~~



### 代码沙箱安全



### 容器内存

通过设置容器内存实现

~~~java
        hostConfig.withMemory(100*1000*1000L);
~~~





#### 容器超时

执行容器可以设置等待时间

~~~java
            dockerClient.execStartCmd(execCmdId)
                    .exec(execStartResultCallback)
                    //等待5秒后退出(不管有没有执行完毕)
                    .awaitCompletion(Time_OUT, TimeUnit.SECONDS); 
~~~

但是，这种方式无论超时与否，都会往下执行，无法判断是否超时。



#### 容器内存溢出

~~~java
        hostConfig.withMemory(100*1000*1000L);
~~~



#### 容器网络资源

~~~java
        CreateContainerResponse exec = dockerClient.createContainerCmd(img)
                .withHostConfig(hostConfig)
                //禁用网络配置
                .withNetworkDisabled(false)
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                //交互容器
                .withTty(true)
                .exec();
~~~





#### 容器权限配置

1)结合java安全管理去使用

2)限制用户root根目录写文件

~~~java
        CreateContainerResponse exec = dockerClient.createContainerCmd(img)
                .withHostConfig(hostConfig)
                //禁用root写
                .withReadonlyRootfs(false)
~~~



3）Linux 自带的一些安全管理措施，比如 seccomp（Secure Computing Mode）是一个用于 Linux 内核的安全功能，它允许你限制进程可以执行的系统调用，从而减少潜在的攻击面和提高容器的安全性。通过配置 seccomp，你可以控制容器内进程可以使用的系统调用类型和参数。

~~~java
     	//linux 内核安全管理
        String profile = ResourceUtil.readStr("/profile.json",StandardCharsets.UTF_8);
~~~



### 单体项目改造为微服务项目(重点)

#### 什么是微服务？

微服务是一种**思想**   微服务专注于提供某类特定功能的代码。而不是将代码全部放在一个单体项目中

将一个单体的项目 分为多个子项目。子项目相关通讯、但不互相联系



#### 微服务技术

Spring Cloud

**Spring Cloud Alibaba（本项目采用）**

Dubbo（DubboX）

RPC（GRPC、TRPC）

本质上是通过 HTTP、或者其他的网络协议进行通讯来实现的。

#### Spring Cloud Alibaba

https://github.com/alibaba/spring-cloud-alibaba

推荐参考中文文档来学习：https://sca.aliyun.com/zh-cn/

本质：是在 Spring Cloud 的基础上，进行了增强，补充了一些额外的能力，根据阿里多年的业务沉淀做了一些定制化的开发

1. Spring Cloud Gateway：网关
2. Nacos：服务注册和配置中心
3. Sentinel：熔断限流
4. Seata：分布式事务
5. RocketMQ：消息队列，削峰填谷
6. Docker：使用Docker进行容器化部署
7. Kubernetes：使用k8s进行容器化部署

![](https://pic.code-nav.cn/post_picture/1610518142000300034/1a87f480-c259-445b-8cf5-962abd9dfd28.png)



#### 开始前

单体项目改为微服务需要注意的

1. 分布式登录(如何微服务之间共享登录态？) 
2. 多数据库操作如何保证事务?
3. 将单锁改为分布式锁
4. 本地缓存需要改造为分布式缓存（Redis）



