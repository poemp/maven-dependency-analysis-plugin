package org.poem.maven.plugins.dependency.internal;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Exclusion;
import org.poem.maven.plugins.dependency.DependencyNode;

import java.util.List;

/**
 * 详细 依赖 节点
 */
class VerboseDependencyNode extends DefaultDependencyNode {

    /**
     * 冲突
     */
    private final ConflictData data;

    /**
     * 构建函数
     * @param parent 父级节点
     * @param artifact  节点 Artifact 信息
     * @param premanagedVersion 获取在应用依赖项管理（如果有）之前指定依赖项节点的版本或版本范围
     * @param premanagedScope 获取在应用依赖项管理（如果有）之前指定依赖项节点的 返回
     * @param versionConstraint
     * @param optional  是否可选
     * @param exclusions 排除依赖列表
     * @param data
     */
    VerboseDependencyNode(
            DependencyNode parent,
            Artifact artifact,
            String premanagedVersion,
            String premanagedScope,
            String versionConstraint,
            Boolean optional,
            List<Exclusion> exclusions,
            ConflictData data) {
        super(parent, artifact, premanagedVersion, premanagedScope, versionConstraint, optional, exclusions);

        this.data = data;
    }

    @Override
    public String toNodeString() {
        StringBuilder buffer = new StringBuilder();

        boolean included = (data.getWinnerVersion() == null);

        if (!included) {
            buffer.append('(');
        }

        buffer.append(getArtifact());

        ItemAppender appender = new ItemAppender(buffer, included ? " (" : " - ", "; ", included ? ")" : "");

        if (getPremanagedVersion() != null) {
            appender.append("version managed from ", getPremanagedVersion());
        }

        if (getPremanagedScope() != null) {
            appender.append("scope managed from ", getPremanagedScope());
        }

        if (data.getOriginalScope() != null) {
            appender.append("scope updated from ", data.getOriginalScope());
        }

        if (data.getIgnoredScope() != null) {
            appender.append("scope not updated to ", data.getIgnoredScope());
        }

        if (!included) {
            String winnerVersion = data.getWinnerVersion();
            if (winnerVersion.equals(getArtifact().getVersion())) {
                appender.append("omitted for duplicate");
            } else {
                appender.append("omitted for conflict with ", winnerVersion);
            }
        }

        appender.flush();

        if (!included) {
            buffer.append(')');
        }

        return buffer.toString();
    }

    /**
     * Utility class to concatenate a number of parameters with separator tokens.
     */
    private static class ItemAppender {
        private final StringBuilder buffer;

        private final String startToken;

        private final String separatorToken;

        private final String endToken;

        private boolean appended;

        ItemAppender(StringBuilder buffer, String startToken, String separatorToken, String endToken) {
            this.buffer = buffer;
            this.startToken = startToken;
            this.separatorToken = separatorToken;
            this.endToken = endToken;

            appended = false;
        }

        public ItemAppender append(String item1) {
            appendToken();

            buffer.append(item1);

            return this;
        }

        public ItemAppender append(String item1, String item2) {
            appendToken();

            buffer.append(item1).append(item2);

            return this;
        }

        public void flush() {
            if (appended) {
                buffer.append(endToken);

                appended = false;
            }
        }

        private void appendToken() {
            buffer.append(appended ? separatorToken : startToken);

            appended = true;
        }
    }
}
