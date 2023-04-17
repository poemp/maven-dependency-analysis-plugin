package org.poem.maven.plugins.dependency.traversal;


import org.poem.maven.plugins.dependency.DependencyNode;

import java.io.IOException;

/**
 * Defines a hierarchical visitor for processing dependency node trees.
 *
 * @author poem
 */
public interface DependencyNodeVisitor {
    /**
     * Starts the visit to the specified dependency node.
     *
     * @param node the dependency node to visit
     * @return <code>true</code> to visit the specified dependency node's children, <code>false</code> to skip the
     *         specified dependency node's children and proceed to its next sibling
     */
    boolean visit(DependencyNode node) throws IOException;

    /**
     * Ends the visit to to the specified dependency node.
     *
     * @param node the dependency node to visit
     * @return <code>true</code> to visit the specified dependency node's next sibling, <code>false</code> to skip the
     *         specified dependency node's next siblings and proceed to its parent
     */
    boolean endVisit(DependencyNode node);


}
