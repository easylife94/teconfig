# Teconfig
一个管理配置文件的maven插件
# Describe
为了配合maven项目在打包的时候更好的管理配置文件。
#Usage
1. install
因为没有提交到中央仓库所以第一步需要下载源码并使用```mvn install```安装到本地。
2. reference
在你的maven项目的pom文件中使用本插件并添加对应的配置
````
  <build>
    <finalName>teops</finalName>
    <plugins>   
      <plugin>
          <groupId>cn.teracloud.plugin</groupId>
		  <artifactId>teconfig-maven-plugin</artifactId>
		  <version>1.0</version>
		  <configuration>
		  	<configs>
		  		<config>
		  			<name>基本配置</name>
		  			<type>properties</type>
		  			<template>src/main/resources/config-template.properties</template>
		  			<scopes>
		  				<scope>
		  					<name>test</name>
		  					<file>src/main/resources/config.test.properties</file>
		  				</scope>
		  				<scope>
		  					<name>product</name>
		  					<file>src/main/resources/config.product.properties</file>
		  				</scope>
		  			</scopes>
		  			<target>src/main/resources/config.properties</target>
		  		</config>
		  	</configs>
		  </configuration>
      </plugin>
    </plugins>
</build>
````

3.command
````
 -Dteconfig.scope=test
````
在上一节的配置基础上执行这个mvn命令就可以将```config.properties```的内容替换为```config.test.properties```的。
# Config

##1. config  - 代表一个配置

name  - 配置的名称
  
type  - 配置文件类型

##2. scope - 代表一个环境
name - 环境名称

file - 配置文件路径

##3. target - 被替换内容的配置文件，也是软件硬编码指定的文件 - 值为文件路径
