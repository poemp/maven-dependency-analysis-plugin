# dependency-analysis-maven-plugin

## How To Use

````
use command:  mvn dependency-analysis:analysis 
````

## Config 

``````````
<plugin>
   <groupId>org.poem.maven.plugins</groupId>
   <artifactId>dependency-analysis-maven-plugin</artifactId>
   <version>x.y.z</version>
   <configuration>
       <outputFile>target/tree-verbose.txt</outputFile>
       <verbose>true</verbose>
   </configuration>
</plugin>
``````````

## 后端服务地址
``````````
https://github.com/poemp/dependency-analysis-maven-plugin-backend
``````````

## 前端服务
``````````
https://github.com/poemp/dependency-analysis-maven-plugin-frontend
``````````