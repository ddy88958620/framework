# 阿波罗集群任务调度系统

## 任务管理界面引用方法

1. 在需要管理界面的项目中引用 `schedule-command-support`
2. 在需要管理界面的项目中的 `config/RootConfig.java` 中增加 `JobConfig.class` 引用(类似MyBatis引用)

## 调度任务编写方法

* 在 `schedule-job` 模块中编写任务实现类，实现 `ApolloJobBean` 接口
* 不要忘记为任务类增加 `@ApolloJob("这里输入任务名称")` 注解

## 任务执行器部署方法

编译后将 `schedule-boot-[VERSION].jar` 拷贝到需要运行的机器上，而后运行 `java -jar schedule-boot-[VERSION].jar` 即可。

> 请将application.properties设置为生产机器相关配置