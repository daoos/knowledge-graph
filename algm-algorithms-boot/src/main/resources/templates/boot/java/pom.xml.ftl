<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.beyond.algm</groupId>
	<artifactId>${userCode}-${projectName}</artifactId>
	<version>${algoVersion}</version>
	<packaging>jar</packaging>

	<name>${userCode}-${projectName}</name>
	<description>algmarket publish</description>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.5.8.RELEASE</version>
		<relativePath/>
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>com.google.code.gson</groupId>
			<artifactId>gson</artifactId>
			<version>2.5</version>
		</dependency>

		<dependency>
			<groupId>com.vaadin.external.google</groupId>
			<artifactId>android-json</artifactId>
			<version>RELEASE</version>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<mainClass>com.beyond.algm.algmarketpublish.AlgmarketPublishApplication</mainClass>
					<fork>true</fork>
				</configuration>
				<executions>
					<execution>
						<goals>
							<goal>repackage</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<artifactId>maven-compiler-plugin</artifactId>
				<configuration>
					<source>1.8</source>
					<target>1.8</target>
					<encoding>UTF-8</encoding>
					<compilerArguments>
						<extdirs>${r'${project.basedir}'}/dist</extdirs>
					</compilerArguments>
				</configuration>
			</plugin>
		</plugins>
		<resources>
			<resource>
				<directory>dist</directory>
				<targetPath>BOOT-INF/lib/</targetPath>
				<includes>
					<include>**/*.jar</include>
				</includes>
			</resource>
			<resource>
				<directory>src/main/resources</directory>
				<targetPath>BOOT-INF/classes/</targetPath>
			</resource>
		</resources>
	</build>


</project>
