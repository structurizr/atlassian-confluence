package com.structurizr.confluence;

import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;

import java.util.Map;
import java.util.regex.Pattern;

public abstract class AbstractStructurizrMacro implements Macro {

    private final static Pattern INVALID_HTML_ID_CHARS = Pattern.compile("[^\\dA-Za-z-_:.]");

    protected String createIframeId(long workspaceId, String diagramKey) {
        return String.format("structurizrEmbedIframe_%d_%s", workspaceId, sanitizeElementIdPart(diagramKey));
    }

    /**
     * Cleans up form/iframe IDs to conform with https://www.w3.org/TR/html4/types.html#type-id
     */
    protected String sanitizeElementIdPart(String diagramKey) {
        return INVALID_HTML_ID_CHARS.matcher(diagramKey).replaceAll("_");
    }

    public Macro.BodyType getBodyType() {
        return Macro.BodyType.NONE;
    }

    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }

    protected long getWorkspaceId(Map<String, String> parameters) throws MacroExecutionException {
        String workspaceId = parameters.get("workspaceId");

        if (workspaceId == null || workspaceId.trim().length() == 0) {
            throw new MacroExecutionException("A workspace ID must be specified.");
        } else {
            return Long.parseLong(workspaceId);
        }
    }

    protected String getStructurizrUrl(Map<String, String> parameters) {
        String structurizrUrl = "https://structurizr.com";
        if (parameters.containsKey("structurizrUrl")) {
            structurizrUrl = parameters.get("structurizrUrl");
        }

        if (structurizrUrl.endsWith("/")) {
            structurizrUrl = structurizrUrl.substring(0, structurizrUrl.length() - 1);
        }

        return structurizrUrl;
    }

    protected String getDiagramKey(Map<String, String> parameters) {
        String diagramKey = "1";
        if (parameters.containsKey("diagramKey")) {
            diagramKey = parameters.get("diagramKey");
        }

        return diagramKey;
    }

    protected String getDiagramSelector(Map<String, String> parameters) {
        String diagramSelector = "false";
        if (parameters.containsKey("diagramSelector")) {
            diagramSelector = parameters.get("diagramSelector");
        }

        return diagramSelector;
    }

}