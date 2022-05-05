package io.github.chris2011.netbeans.plugins.yeoman4netbeans.npm;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @todo replace by RECORD
 *
 * @author ranSprd
 */
public class YeomanQuestion {

    private final String type;
    private final String label;
    private final ArrayList choices;
    private final JSONObject json;

    public YeomanQuestion(JSONObject json) {
        this.json = json;
        this.type = json.get("type").toString();
        this.label = json.get("message").toString();

        if (json.get("choices") != null && (((JSONArray) json.get("choices")).get(0) instanceof String || ((JSONArray) json.get("choices")).get(0) instanceof Integer)) {
            this.choices = (ArrayList<String>) json.get("choices");
        } else {
            this.choices = (ArrayList<ChoicesType>) json.get("choices");
        }
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public ArrayList getChoices() {
        return choices;
    }

    public JSONObject getJson() {
        return json;
    }

    public String getDefaultValue() {
        Object defaultValue = json.get("default");

        if(defaultValue != null) {
            return defaultValue.toString();
        }
        
        return null;
    }
}
