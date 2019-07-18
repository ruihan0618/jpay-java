# MasPay Java SDK

## 简介

1. [docs](/docs) 目录下为 MasPay Java SDK 的使用文档。
2. example 目录下面为一个 Eclipse IDE 的示例工程。
3. libs 为 MasPay Java SDK 的 jar 包和 MasPay Java SDK 所依赖的Gson 包。
4. src 为 MasPay Java SDK 的源代码，可以关联到 maspay-java-x.x.x.jar 文件。或者直接把源代码引入到工程之中。

## 版本要求

Java SDK 要求 JDK 版本 1.7 及以上

## 安装

#### 手动安装

将 libs/ 下面的 jar 包导入工程

#### maven 安装

maven 远程仓库

``` xml
<repository>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
    <id>central</id>
    <name>bintray</name>
    <url>http://jcenter.bintray.com</url>
</repository>
```

安装 MasPay SDK
``` xml
<dependency>
    <groupId>MasPayplusplus</groupId>
    <artifactId>maspay-java</artifactId>
    <version>2.3.11</version>
    <type>jar</type>
</dependency>
```

#### gradle 安装

gradle 远程仓库

```
repositories {
    maven {
        url  "http://jcenter.bintray.com"
    }
}
```

安装 MasPay SDK

```
compile 'jpaypp:maspay-java:2.3.11'
```

### 使用示例

- 参考 [example](/example) 示例项目工程。该工程提供了付款、相关的 demo。