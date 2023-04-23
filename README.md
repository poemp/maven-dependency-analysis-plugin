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
        <verbose>true</verbose>
        <analysisUrl>http://127.0.0.1:8000/upload </analysisUrl>
    </configuration>
    <executions>
        <execution>
            <!-- 执行id -->
            <id>dependency-analysis-analysis</id>
            <!-- 生效阶段，不指定则取插件类的@Mojo注解中的defaultPhase。此处专门设定了一个与install不同的阶段值 -->
            <phase>install</phase>
            <!-- 需要运作的Goal -->
            <goals>
                <!-- 执行  mvn dependency-analysis:analysis 绑定到maven 的生命周期中 clear 生命周期 -->
                <!-- link org.poem.maven.plugins.DependencyAnalysisPluginMojo  -->
                <goal>analysis</goal>
            </goals>
        </execution>
    </executions>
</plugin>

exec command : mvn install -DskipTests 

will upload depend 

``````````

## 后端服务地址
``````````
https://github.com/poemp/dependency-analysis-maven-plugin-backend
``````````

## 前端服务
``````````
https://github.com/poemp/dependency-analysis-maven-plugin-frontend
``````````