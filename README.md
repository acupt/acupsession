# acupsession

基于redis实现的分布式session管理，通过配置filter拦截请求重设session

## 使用说明

+ maven

```
<repositories>
    <repository>
    <id>acupt-repository</id>
    <url>https://raw.githubusercontent.com/acupt/repository/snapshot</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.acupt</groupId>
        <artifactId>acupsession</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

+ web.xml

```
<!--session filter first-->
<filter>
    <filter-name>redisSessionFilter</filter-name>
    <filter-class>com.acupt.acupsession.RedisSessionFilter</filter-class>
    <!--参数配置，所有参数及默认值见 com.acupt.acupsession.SessionConfig-->
    <init-param>
        <param-name>redisHost</param-name>
        <param-value>localhost</param-value>
    </init-param>
    <init-param>
        <param-name>redisPassword</param-name>
        <param-value>000000</param-value>
    </init-param>
    <init-param>
        <param-name>redisPort</param-name>
        <param-value>6379</param-value>
    </init-param>
    <init-param>
        <param-name>sessionTimeout</param-name>
        <param-value>120</param-value>
    </init-param>
</filter>

<filter-mapping>
    <filter-name>redisSessionFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>

<!--下面配置其他业务filter-->

```