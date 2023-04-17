package org.poem.maven.plugins.dependency.remote;

import java.io.IOException;
import java.io.Writer;

/**
 * 远程
 * 刷新数据
 * @author poem
 */
public class RemoteInfoWriter  extends Writer {

    /**
     * 发送数据
     * @param cbuf 发送字节
     * @param off 字节开始位置
     * @param len 长度
     * @throws IOException
     */
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {

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
