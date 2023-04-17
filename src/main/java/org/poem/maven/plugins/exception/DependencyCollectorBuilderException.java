package org.poem.maven.plugins.exception;


/**
 * Indicates an issue with the DependencyCollectorBuilder
 *
 */
public class DependencyCollectorBuilderException extends Exception {


    /**
     * @param message   Message indicating why dependency graph could not be resolved.
     */
    public DependencyCollectorBuilderException(String message) {
        super(message);
    }

    /**
     * @param message   Message indicating why dependency graph could not be resolved.
     * @param cause     Throwable indicating at which point the graph failed to be resolved.
     */
    public DependencyCollectorBuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}
