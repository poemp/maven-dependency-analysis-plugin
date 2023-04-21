package org.poem.maven.plugins.dependency.remote;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;
import org.poem.maven.plugins.utils.HttpClientUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * 远程
 * 刷新数据
 * @author poem
 */
public class RemoteInfoWriter  extends Writer {



    private String remoteUrl;

    private Log log;
    /**
     * 远程地址
     * @param remoteUrl
     */
    public RemoteInfoWriter(String remoteUrl, Log log ) {
        super();
        this.remoteUrl = remoteUrl;
        this.log = log;
    }

    /**
     * 发送数据
     * @param cbuf 发送字节
     * @param off 字节开始位置
     * @param len 长度
     * @throws IOException
     */
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        log.info("Upload Dependencies To:"+ remoteUrl);
        log.info("Upload Dependencies:  "+ new String(cbuf));
        FileWriter fileWriter = new FileWriter("json-.json");
        fileWriter.write(new String(cbuf));
        fileWriter.flush();
        fileWriter.close();
        String result = HttpClientUtils.doPostJson(remoteUrl, new String(cbuf));
        log.info("Upload Dependencies Result Is :" + result);
    }

    /**
     * 刷新
     * @throws IOException
     */
    @Override
    public void flush() throws IOException {

    }

    /**
     * 关闭链接
     * @throws IOException
     */
    @Override
    public void close() throws IOException {

    }
}
