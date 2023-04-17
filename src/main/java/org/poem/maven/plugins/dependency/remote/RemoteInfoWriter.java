package org.poem.maven.plugins.dependency.remote;

import org.apache.maven.plugin.logging.Log;

import java.io.IOException;
import java.io.Writer;

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
        log.info(new String(cbuf));
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
