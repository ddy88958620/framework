# 特性标记

* `[FEATURE]` 新特性
* `[HOTFIX]` BUG修改

# 1.1.0

* `[FEATURE]` 统一处理RabbitMQ的失败消息
* `[FEATURE]` 引入Guava，逐步重构
* `[FEATURE]` 统一使用一个system.mis.id
* `[FEATURE]` 日志输出错误信息栈

# 1.0.5

* `[HOTFIX]` 修复Shiro的Session默认30分钟超时的问题

# 1.0.4

* `[HOTFIX]` 统一使用一个system.mis.id
* `[HOTFIX]` 修复ExtUtil.success()返回false的问题
* `[FEATURE]` 某些配置不再提供默认值
* `[HOTFIX]` HttpClient每次发送请求后都关闭response和连接，防止线程始终Running

# 1.0.3

* `[HOTFIX]` 修复单例模式Cmd造成的属性值未清空

# 1.0.2

* `[HOTFIX]` 清空未设置的变量，以支持单例模式的Cmd
* `[HOTFIX]` Input注解默认length为Integer.MAX_VALUE，简化开发
* `[HOTFIX]` 将sessionKey放入请求参数中，防止验证失败

# 1.0.1

* `[HOTFIX]` 权限模块修改，支持分系统权限查询

# 1.0.0(0.7.0)

* `[HOTFIX]` 改进RabbitMQ类实现
* `[FEATURE]` 增加flume引用
* `[FEATURE]` 删除Apache BeanUtil引用，统一使用Spring的BeanUtils
* `[FEATURE]` 实现集中权限管理/统一Shiro在Redis中的前缀/重新设计Shiro模型
* `[FEATURE]` 增加ArrayResponse
* `[FEATURE]` 增加默认配置项，Shiro配置项增加前缀`shiro.`
* `[FEATURE]` 解决RabbitMQ将所有的Map转换为Hashtable的问题
* `[FEATURE]` 增加bean与map转换方法到MapUtil
* `[FEATURE]` 升级三方库版本
* `[FEATURE]` 增加parent统一管理BOM
* `[FEATURE]` 丰富Shiro的User模型字段
* `[FEATURE]` 将API方式调用Shiro改为dubbo方式，分离接口
* `[FEATURE]` 支持多系统集中菜单权限、功能权限
* `[FEATURE]` 加入对云配置的支持

# 0.6.1

* `[HOTFIX]` 修改Ext Tree后端封装方法

# 0.6.0

* `[FEATURE]` ExtUtil增加空参数的success方法
* `[HOTFIX]` Spring MVC日期类型处理
* `[FEATURE]` web-mvc依赖修改
* `[FEATURE]` 实现JsonP
* `[FEATURE]` 实现树型结构
* `[FEATURE]` 重构BaseVo
* `[FEATURE]` 升级Spring版本
* `[FEATURE]` 统一Json格式化类
* `[FEATURE]` Ext工具类ExtUtil
* `[FEATURE]` 集成RabbitMQ
* `[FEATURE]` 封装SdkUtil
* `[FEATURE]` 更换数据库连接池组件

# 0.5.0

* `[FEATURE]` Unix时间处理
* `[FEATURE]` API模块拆分
* `[FEATURE]` 命令缓存
* `[FEATURE]` 引入SpringMVC
* `[FEATURE]` 重新定义ApolloServerService接口
* `[FEATURE]` MVC缓存
* `[FEATURE]` 重新设计包结构与项目结构
* `[HOTFIX]` 修复部门管理问题

# 0.4.0

* `[FEATURE]` 将代码生成器移出平台项目
* `[FEATURE]` ApiCommand注解改为groupName和group注解
* `[FEATURE]` 引入my97datepicker日期控件
* `[FEATURE]` 数据库由PostgreSQL切换为MySQL
* `[FEATURE]` 实现文件上传功能
* `[FEATURE]` 将任务调度模块移出平台，作为通用模块实现，详情查看 [阿波罗集群任务调度系统](http://192.168.1.202/apollo/apollo-schedule)
* `[FEATURE]` 通过submodule方式引入任务调度
* `[HOTFIX]` 切换lombok到最新版本
* `[HOTFIX]` 由于BaseResponse中的属性为protected导致eclipse中提示错误，更改为private

# 0.3.2

* `[HOTFIX]` BaseCmd移除sessionKey，防止URL Encoding时空指针

# 0.3.1

* `[HOTFIX]` 修复HttpUtil工具了请求时中文乱码问题

# 0.3.0

* `[FEATURE]` CmdList修改为接口
* `[FEATURE]` 增加ApiCommand注解属性groupName，代替使用CmdGroup

# 0.2.1

* `[HOTFIX]` Dao测试不通过修复
* `[HOTIFX]` 改进代码生成器模板
* `[HOTIFX]` 改进定时任务
* `[FEATURE]` Command增加Double类型支持

# 0.2.0

* `[FEATURE]` 新增HttpClient封装
* `[FEATURE]` 新增BeanUtils支持
* `[FEATURE]` 定时任务集成

# 0.1.1

* `[HOTFIX]` 改进ApiServerService接口
* `[HOTIFX]` 改进代码生成器
