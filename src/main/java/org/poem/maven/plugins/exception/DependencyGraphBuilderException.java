package org.poem.maven.plugins.exception;
/**
 * 异常处理
 * @author eventec
 */
public class DependencyGraphBuilderException extends Exception {


    /**
     * @param message   Message indicating why dependency graph could not be resolved.
     */
    public DependencyGraphBuilderException(String message) {
        super(message);
    }

    /**
     * @param message   Message indicating why dependency graph could not be resolved.
     * @param cause     Throwable indicating at which point the graph failed to be resolved.
     */
    public DependencyGraphBuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}
