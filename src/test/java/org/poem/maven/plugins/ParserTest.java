package org.poem.maven.plugins;

import com.alibaba.fastjson2.JSON;
import org.poem.maven.plugins.javaparser.dto.ClassDto;
import org.poem.maven.plugins.javaparser.parser.NeoParser;
import org.poem.maven.plugins.javaparser.parser.SpoonParser;

public class ParserTest {

    public static void main(String[] args) {
        SpoonParser spoonParser = SpoonParser.getInstance();

        System.out.println("开始解析...");
        long start = System.currentTimeMillis();
        spoonParser.parse(spoonParser.getFluentModel("C:\\Users\\eventec\\Downloads\\dpd-master", 8));
        System.out.println("解析完成，用时 " + (System.currentTimeMillis() - start) + " ms.");
        System.out.println("-----");

        System.out.println("开始构建图数据库...");
        start = System.currentTimeMillis();
        NeoParser neoParser = NeoParser.getInstance();
        ClassDto classDto = neoParser.create(spoonParser);
        System.out.println("图创建完成，用时 " + (System.currentTimeMillis() - start) + " ms.");
        System.out.println("-----");
        System.out.println(JSON.toJSONString(classDto));
    }
}
