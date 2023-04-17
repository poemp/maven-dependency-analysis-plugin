package org.poem.maven.plugins.dependency.traversal;

import org.poem.maven.plugins.dependency.DependencyNode;
import org.poem.maven.plugins.dependency.remote.RemoteInfoWriter;

import java.io.IOException;
import java.util.List;

/**
 * 远程信息
 * remote analysis plugin
 * @author poem
 */
public class RemoteDependencyNodeVisitor implements DependencyNodeVisitor{

    /**
     * The writer to serialize to.
     */
    private final RemoteInfoWriter writer;

    /**
     * The tokens to use when serializing the dependency graph.
     */
    private final SerializingDependencyNodeVisitor.GraphTokens tokens;

    /**
     * The depth of the currently visited dependency node.
     */
    private int depth;

    public RemoteDependencyNodeVisitor(RemoteInfoWriter writer, SerializingDependencyNodeVisitor.GraphTokens tokens) {
        this.writer = writer;
        this.tokens = tokens;
    }

    /**
     * Writes the necessary tokens to indent the specified dependency node to this visitor's writer.
     *
     * @param node the dependency node to indent
     */
    private void indent(DependencyNode node) throws IOException {
        for (int i = 1; i < depth; i++) {
            writer.write(tokens.getFillIndent(isLast(node, i)));
        }

        if (depth > 0) {
            writer.write(tokens.getNodeIndent(isLast(node)));
        }
    }

    @Override
    public boolean visit(DependencyNode node) throws IOException {
        indent(node);

        writer.write(node.toNodeString());

        depth++;

        return true;
    }

    @Override
    public boolean endVisit(DependencyNode node) {

        return true;
    }
    /**
     * Gets whether the specified dependency node is the last of its siblings.
     *
     * @param node the dependency node to check
     * @return <code>true</code> if the specified dependency node is the last of its last siblings
     */
    private boolean isLast(DependencyNode node) {

        DependencyNode parent = node.getParent();

        boolean last;

        if (parent == null) {
            last = true;
        } else {
            List<DependencyNode> siblings = parent.getChildren();

            last = (siblings.indexOf(node) == siblings.size() - 1);
        }

        return last;
    }
    /**
     * Gets whether the specified dependency node ancestor is the last of its siblings.
     *
     * @param node the dependency node whose ancestor to check
     * @param ancestorDepth the depth of the ancestor of the specified dependency node to check
     * @return <code>true</code> if the specified dependency node ancestor is the last of its siblings
     */
    private boolean isLast(DependencyNode node, int ancestorDepth) {
        // TODO: remove node argument and calculate from visitor calls only

        int distance = depth - ancestorDepth;

        while (distance-- > 0) {
            node = node.getParent();
        }

        return isLast(node);
    }
}
