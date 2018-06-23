## JavaToolKit
[![Build Status](https://travis-ci.org/Kuangcp/JavaToolKit.svg?branch=master)](https://travis-ci.org/Kuangcp/JavaToolKit)
[![Java Version](https://img.shields.io/badge/Java-JRE%208-red.svg)](https://www.java.com/download/)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a98ea20e4ff64eee90c43ac2a480e9a8)](https://www.codacy.com/app/Kuangcp/JavaToolKit?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Kuangcp/JavaToolKit&amp;utm_campaign=Badge_Grade)
[![codebeat badge](https://codebeat.co/badges/9ff07ca0-4c34-448d-a594-507fd9d34ec6)](https://codebeat.co/projects/github-com-kuangcp-javatoolkit-master)
[![License](https://img.shields.io/badge/license-MIT-brightgreen.svg)](LICENSE.md) 
[![Versuib](https://img.shields.io/github/tag/Kuangcp/JavaToolKit.svg)](https://github.com/Kuangcp/JavaToolKit/releases)

> 这是一个使用maven构建的常用工具项目，使用该JAR包有详细的doc规范提示

- [ ] 急需重构整个项目!!!!

**********************************
- 下载Jar安装，由于没有发布到中央仓库
    - `mvn install:install-file -Dfile=下载jar包的路径 -DgroupId=Myth -DartifactId=JavaToolKit -Dversion=1.0-SNAPSHOT -Dpackaging=jar`
- 或者编译安装
    - `git clone https://github.com/kuangcp/JavaToolKit.git`
    - `cd JavaToolKit` && ` mvn install`
- *注意 : *配置文件的建立，为了安全没有提交 ，
    - 只要将config目录下复制到src/main/resources/目录下
    - 然后更名 去掉每个配置文件前的 `model_ ` 即可正常使用了
    - 改成自己需要的配置就可以了
- 如果要发布到中央仓库，注意groupid格式 com.github.kuangcp

### 使用码云做私服方式

#### Gradle 
```groovy
repositories {
    maven{
        url "https://gitee.com/kcp1104/MavenRepos/raw/master"
    }
} 
```
- 然后添加依赖: `compile "com.github.kuangcp:JavaToolKit:1.1.1-SNAPSHOT"`   

#### Maven
```xml
<repositories>
  <repository>
    <id>mvnrepo</id>
    <name>mvn repository</name>
    <url>https://gitee.com/kcp1104/MavenRepos/raw/master</url>
  </repository>
</repositories>
```

****************************************
## 各个包的说明：

- [com.myth.countfileline](./src/main/java/com/myth/countfileline)
    -  检索记录某文件夹下各种代码文件行数，相对于Python效率比较低

- [com.myth.file](./src/main/java/com/myth/file)
    -  文件，压缩包操作相关类

- [com.myth.mail](./src/main/java/com/myth/mail)
    -  使用配置文件中的配置信息发送指定邮件（发送的是html）

- [com.myth.mysql ](./src/main/java/com/myth/mysql)
    -  是Mysql数据库的相关操作，数据库表和Excel的转换

-  [com.myth.reflect](./src/main/java/com/myth/reflect)
    - 关于Java反射，ORM 的手动编写
    - [ ] DBType的丰富和测试通过

- [com.myth.qrcode](./src/main/java/com/myth/qrcode)
    - 关于二维码的相关操作类

- [com.myth.time](./src/main/java/com/myth/time)
    - 关于记录运行时间的相关类

*******************
> 其他相关仓库: [util](https://github.com/zhazhapan/util)

- [ ] 将常用类保留, 其他的放在test下 作为代码片段
