package io.github.chris2011.netbeans.plugins.yeoman4netbeans.npm;

import java.io.IOException;
import javax.swing.JOptionPane;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openide.util.Exceptions;

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
            new Thread(() -> {
                try (final YeomanIO io = new YeomanIO(process)) {

                    // no exception thrown here, NPM should available
                    String line;
                    while ((line = io.readLine()) != null) {
//                        String outline = line + "\n";
                        // TODO: Show full yeoman output in TextArea
//                        dialog.txtaOutputMessage.setText(outline);
                        YeomanQuestion parsedLine;
//                        try {
                        parsedLine = tryParseOutputline(line);

                        if (parsedLine != null) {
                            consumeParsedLine(parsedLine, io);
                        } else {
                            dialog.addErrorMessage(line);
                        }
//                        } catch (Exception ex) {
//                            continue;
//                        }
                    }
                } catch (IOException e) {
                    printError(e);
                }
            }).start();

        } catch (IOException e) {
            printError(e);
        }
    }

    private YeomanQuestion tryParseOutputline(String line) {
//        JOptionPane.showMessageDialog(null, line);
        try {
            final JSONObject jsonLineToParse = (JSONObject) jsonParser.parse(line);
            return new YeomanQuestion(jsonLineToParse);
        } catch (ParseException e) {
//            try {
            // log debug ?
//                JOptionPane.showMessageDialog(null, e);
//                final JSONObject jsonLineToParse = (JSONObject) jsonParser.parse(line);
//                return new YeomanQuestion(jsonLineToParse);
//            } catch (ParseException ex) {
//                Exceptions.printStackTrace(ex);
//            }
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
