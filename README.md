## 阿波罗平台说明文档

#### 持续集成状态

* [![build status](http://192.168.1.207/projects/1/status.png?ref=master)](http://192.168.1.207/projects/1?ref=master) master分支
* [![build status](http://192.168.1.207/projects/1/status.png?ref=develop)](http://192.168.1.207/projects/1?ref=develop) develop分支

### 0.项目必备条件

下面相关内容不会的问谷歌，谷歌不会问百度

1. [Git](http://git-scm.com/downloads)
2. [Maven](http://maven.apache.org/download.cgi)
3. [SourceTree](http://www.sourcetreeapp.com)
4. [Redis](http://redis.io/download)
5. [MySQL](http://dev.mysql.com/downloads/mysql/)
6. [Lombok](http://www.projectlombok.org)
7. [Beanshell](http://www.beanshell.org/download.html)

#### Lombok配置指南

* Eclipse用户下载 [lombok.jar](http://projectlombok.org/downloads/lombok.jar)，双击或`java -jar lombok.jar`执行安装(什么？！不知道这是干啥的？！再次撞墙屎！)
* IntelliJ IDEA用户进入插件管理，搜索`lombok`安装重启即可

#### Beanshell配置指南

下载 [bsh-2.04b.jar](http://www.beanshell.org/bsh-2.0b4.jar)

* Linux/Unix系统将jar包放入`$JAVA_HOME/jre/lib/ext`
* OSX系统将jar包放入`/Library/Java/Extensions`
* Windows系统将jar包放入`$JAVA_HOME/jre/lib/ext`

### 1.目录结构

* cache: 缓存
* db: 数据库封装
    * mybatis: MyBatis封装
* mq: 消息队列封装
    * rabbit: RabbitMQ封装
* parent: 阿波罗BOM
* plugins: 插件
    * rule: 规则引擎
    * schedule: 定时任务
* security: 安全认证框架 -- Apache Shiro
* tools: 自动化工具
    * tester -- API测试工具
* utils: 工具代码
* web: WEB层封装
    * api: API接口封装
    * api-servlet-support: API的Servlet支持
    * mvc: SpringMVC封装

### 2.Git使用注意事项

* WEB UI`http://192.168.1.202`，上传头像有助于吸引妹纸
* 请务必使用http地址`http://192.168.1.202/apollo/apollo.git`
* 严格按照Git-flow模型开发【不懂请 [移驾](http://nvie.com/posts/a-successful-git-branching-model/) 】
* 确保本机通过测试再提交到远程库，至少Junit要通过吧
* 平台开发人员创建分支请以自己的姓名拼音
* 业务开发人员创建分支请以具体特性
* 分支无法使用中文名
* 提交前请先查看远程develop分支的状态，如果本地不是最新的，先pull远程develop到本地，然后提交特性分支

### 3.Maven

#### settings.xml设置

为了保证依赖jar包下载速度，我们需要修改一下maven安装路径中`conf`目录的`settings.xml`

```xml
<mirror>
    <id>nexus-osc</id>
    <mirrorOf>central</mirrorOf>
    <name>Nexus osc</name>
    <url>http://maven.oschina.net/content/groups/public/</url>
</mirror>
```

在执行Maven命令的时候，Maven还需要一些插件包，同样指向OSChina的Maven地址，同样修改`settings.xml`

```xml
<profile>
  <repositories>
    <repository>
      <id>nexus</id>
      <name>local private nexus</name>
      <url>http://maven.oschina.net/content/groups/public/</url>
      <releases>
        <enabled>true</enabled>
      </releases>
      <snapshots>
        <enabled>false</enabled>
      </snapshots>
    </repository>
  </repositories>
  <pluginRepositories>
    <pluginRepository>
      <id>nexus</id>
      <name>local private nexus</name>
      <url>http://maven.oschina.net/content/groups/public/</url>
      <releases>
        <enabled>true</enabled>
      </releases>
      <snapshots>
        <enabled>false</enabled>
      </snapshots>
    </pluginRepository>
  </pluginRepositories>
</profile>
```

在具体项目中，为 `pom.xml` 增加依赖

```xml
<repositories>
    <repository>
        <id>apollo-releases</id>
        <url>http://192.168.1.208/content/repositories/apollo-releases/</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
    <repository>
        <id>apollo-snapshots</id>
        <url>http://192.168.1.208/content/repositories/apollo-snapshots/</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

#### 项目相关命令

**maven命令需要在项目根目录执行**

构建项目`mvn clean install`

跑测试`mvn test`

用jetty启动web应用`mvn -pl :apollo-client jetty:run`

用tomcat启动web应用`mvn -pl :apollo-client tomcat:run`

API测试(确保本机应用已启动)`mvn -pl :api-tester exec:java`

**上面的命令是啥，这里就不做解释了，自行学习**

#### 代码生成器使用方法

使用前请签出 [代码生成器](http://192.168.1.202/apollo/code-generater) 项目代码

##### 参数列表

* `output` 代码生成目标目录
* `modelName` 模块英文名
* `modelNameZh` 模块中文名
* `groupName` 包名，会在`modelName`前面再插入一层包
* `commandGroup` 命令分组 `public static final String COMMON = "公用";` **COMMON**为此参数对应的值
* `commandGroupZh` 命令分组中文名 `public static final String COMMON = "公用";` **公用**为此参数对应的值
* `addColumnNum` 添加页面表单列数(int)
* `queryColumnNum` 搜索表单列数(int)
* `author` 代码作者
* `tableName` 期望解析的表名

##### 使用方式

Eclipse:

1. 右键CodeGenerator.java
2. Run As
3. Run Configurations...
4. 双击Java Application
5. Main class后面Search...
6. 输入CodeGenerator
7. 点击Environment选项卡
8. 点击New...
9. 输入上方参数列表中的每个参数name和value
10. Apply并且Run即可

IntelliJ IDEA:

1. 右键CodeGenerator.java
2. Create 'CodeGenerator.main()...
3. Environment variables后面的...
4. 填写所有name和value
5. 保存后运行即可

### 4.开发规范

#### 项目开发/发布规范

##### 正常版本开发/发布版本控制流程

1. 每次版本发布之后，立即创建一个新的特性分支 `update_version`
2. 将所有pom版本修改为下一个版本：如master版本为 `1.2.0`，则修改版本为 `1.3.0-SNAPSHOT`
3. 每次创建release版本进入测试，并且测试全部通过后，修改pom版本为正式版，即 `1.3.0-SNAPSHOT` 修改为 `1.3.0`
4. 修改 `FEATURES.md` 文件，将特性与BUG修改记录写入文档中
5. release版本发布之后，再次进入第1步的流程

##### hotfix版本开发/发布版本控制流程

1. 创建hotfix分支，名称为当前master分支版本最后一位+1，如master版本为 `1.3.2`，则名称为 `1.3.3`
2. 将所有pom版本修改为下一个版本：如master版本为 `1.3.2`，则修改版本为 `1.3.3-SNAPSHOT`
3. 测试全部通过后，修改pom版本为正式版，即 `1.3.3-SNAPSHOT` 修改为 `1.3.3`
4. 修改 `FEATURES.md` 文件，BUG修改记录写入文档中
5. 完成hotfix

#### 后端规范

* 严格使用`驼峰命名`
    * listUsersByDepartmentId
* 所有命令类以`Cmd`结尾
    * LoginCmd, ListUsersCmd, UpdateUserCmd
* 所有返回多条记录的Command以复数命名
    * ListUsersCmd, DeleteRolesCmd
* Command导出名不包含Cmd且首字母小写
    * ListUsersCmd -> @ApiCommand(name = "listUsers" ...)
* Command类存放位置
    * com.handu.apollo.api.command.xxx
    * xxx为具体业务包
    * 公共命令存放在`com.handu.apollo.api.command`
    * 只需对Command类中的注入声明注解`@Autowired`
* CmdGroup存放分组常量

```java
@Data
@ApiCommand(
        name = "addUser", group = CmdGroup.USER,
        responseObject = UserResponse.class,
        description = "添加系统用户，密码加密方式采用SHA-512*1024，私盐根据用户名和安全随机数执行1024次SHA-512加密",
        usage = "使用JS封装的API发送命令，也可以使用curl发送请求，请求中cookie必须包含 <code>sessionKey</code> (通过 <code>login</code> 命令获取)")
public class AddUserCmd extends BaseValidateCmd {

    // ///////////////////////////////////////////////////
    // ///////// AddUserCmd API parameters ///////////////
    // ///////////////////////////////////////////////////

    @Input(type = CommandType.STRING, description = "用户名", required = true, length = 75)
    private String username;

    @Input(type = CommandType.STRING, description = "邮箱地址", required = true, length = 75)
    private String email;

    @Input(type = CommandType.STRING, description = "登录密码", required = true, length = 50)
    private String password;

    @Input(type = CommandType.STRING, description = "用户姓名", required = true, length = 20)
    private String realname;

    @Input(type = CommandType.LONG, description = "所属部门ID", required = true)
    private Long departmentId;

    @Override
    public void validate() throws ParameterValidationException {

    }

    @Override
    public void execute() throws ApiException {
        UserResponse response = _userService.addUser(this);
        this.setResponseObject(response);
    }
}
```

* `@Output`和`@Input`使⽤用⽅方法参⻅见源码
* `@Input`参数默认⻓长度255，如果报错超长，这里需要修改
* 所有JavaBean无需编写`Getter&Setter`，参照 [Lombok](http://www.projectlombok.org)
* 必须声明Service接⼝口类，并存放在api模块中
* Service实现类存放在server模块中
* 多个Service接⼝口可由⼀一个实现类来实现
    * UserService, DepartmentService, RoleService -> UserCenterManager implements UserService, DepartmentService, RoleService
* 全局只有三个Dao
    * `Dao` _dao.getList(CLASS_NAME, “getByName”, name)
    * `CounterDao` Long id = _cDao.increment(SomeClass.class.getName())
    * `RedisDao` 缓存

#### 前端规范

* ⻚⾯⽂件存放在`page`目录中 每个页面中的脚本必须包裹`(function(){ ... })();`
* 自定义样式表在`custom.css`中定义
* 复杂交互请使用模板引擎
* 尽量遵循前《端编码规范》
* 新组件需求请提交引入需求,不得随意引⼊

#### 数据库设计规范

* 表名与字段名全部使用小写，单词之间使用下划线连接
    * user_role
* 不做数据库端的关联关系
* 无存储过程、视图、触发器、自定义函数
* 不要使用数据库的独有函数
* 主键全部使用int8类型

#### 测试规范

* 尽量编写Junit测试
* API测试使用tools中的测试方法

### 5.平台引用

* Apollo平台代码严格按照模块化方式组织，在使用Apollo平台开发新的项目时，可根据项目的实际需求，有选择的引入Apollo平台的相关模块
* 所有使用Apollo平台的项目中，必须要实现 `ApiServerService` 接口中的方法：

```java
public interface ApiServerService {
    //验证请求
    public boolean verifyRequest(Map<String, Object[]> requestParameters) throws ApiException;
    //用户登录
    public String userLogin(String username, String password) throws ApolloAuthenticationException;
    //用户退出
    public void userLogout();
    //验证用户权限
    public boolean verifyUser();
    //处理请求
    public String handleRequest(Map params, StringBuffer auditTrailSb) throws ApiException;
}
```

#### 引用方式

* 方式一，最简使用模式
    * 新项目仅需要依赖 `apollo-web-api`或`apollo-web-mvc` 模块，即可实现前后端通过command进行交互
    * 这种方式不包括 `数据库连接管理`，`缓存管理`，`权限管理`，如果有需求需要在新项目中实现
    * 参考 [猛击我](http://192.168.1.202/wangfei/demoproject)

* 方式二，基础使用模式
    * 新项目需要依赖 `apollo-web-api`或`apollo-web-mvc`，`apollo-cache`，`apollo-db-mybatis`，`apollo-security` 模块
    * 这种方式新项目中使用了Apollo平台的所有基础功能，同时部分数据表结构需要与Apollo系统一致

* 方式三，完整使用模式
    * 新项目仅需要依赖 `apollo-service` 模块，即可拥有Apollo平台已实现的所有功能
    * 新项目数据库需要包含Apollo平台已有的数据表