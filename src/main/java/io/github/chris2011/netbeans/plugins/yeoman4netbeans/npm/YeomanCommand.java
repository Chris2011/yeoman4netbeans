package io.github.chris2011.netbeans.plugins.yeoman4netbeans.npm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author ranSprd
 */
public class YeomanCommand {

    private final YeomanDialog dialog;
    private final JSONParser jsonParser = new JSONParser();
    

    public YeomanCommand(YeomanDialog dialog) {
        this.dialog = dialog;
    }
    

    public void execute() {
        dialog.setVisible(true);
        
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "node",
                    "./src/main/java/io/github/chris2011/netbeans/plugins/yeoman4netbeans/cli.js",
                    "--generatorName",
                    "zx-vue",
                    "--newYoVersion"                    
            );
            Process process = processBuilder.start();
            
            // the background thread watches the output from the process
            new Thread(new Runnable() {
                public void run() {
                    
                    try(final YeomanIO io = new YeomanIO(process)) {
                        
                        // no exception thrown here, NPM should available
                        String line;
                        while ((line = io.readLine()) != null) {
                            YeomanQuestion parsedLine = tryParseOutputline(line);
                            if (parsedLine != null) {
                                consumeParsedLine(parsedLine, io);
                            } else {
                                dialog.addErrorMessage(line);
                            }
                        }
                    } catch (IOException e) {
                        printError(e);
                    }
                }
            }).start();
            
        } catch (Exception e) {
            printError(e);
        }
    }
    
    private YeomanQuestion tryParseOutputline(String line) {
        try {
            final JSONObject jsonLineToParse = (JSONObject) jsonParser.parse(line);
            return new YeomanQuestion(jsonLineToParse);
        } catch (Exception e) {
            // log debug ?
        }
        return null;
    }
    
    private void consumeParsedLine(YeomanQuestion line, YeomanIO io) {
        dialog.updateQuestion(line, io);
    }
    
    private void printError(Exception e) {
        dialog.switchToErrorPanel();
        dialog.addErrorMessage(e.getMessage());
        e.printStackTrace();
    }
    
    
}
