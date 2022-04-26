package io.github.chris2011.netbeans.plugins.yeoman4netbeans.npm;

import org.json.simple.JSONObject;

/**
 * @todo replace by RECORD
 *
 * @author ranSprd
 */
public class YeomanQuestion {

    private final String type;
    private final String label;
    private final JSONObject json;

    public YeomanQuestion(JSONObject json) {
        this.json = json;
        this.type = json.get("type").toString();
        this.label = json.get("message").toString();
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public JSONObject getJson() {
        return json;
    }

    public String getValue(String name) {
        return json.get(name).toString();
    }
}
