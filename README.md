# acupsession

基于redis实现的分布式session管理，通过配置filter拦截请求重设session。

## 使用说明

+ web.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <!--session filter配置在最前面-->
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

</web-app>
```