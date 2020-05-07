# 基础镜像
FROM openjdk:8-jdk-alpine

# 作者信息
MAINTAINER "chanshiyu me@chanshiyu.com"

# 添加一个存储空间
VOLUME /tmp

# 暴露8080端口
EXPOSE 8080

# 添加变量
ARG JAR_FILE=target/yuko.jar

# 往容器中添加jar包
ADD ${JAR_FILE} yuko.jar

# 启动镜像自动运行程序
ENTRYPOINT ["java","-jar","/yuko.jar"]