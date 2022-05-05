package io.github.chris2011.netbeans.plugins.yeoman4netbeans.npm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This command should be able to fix errors like...
 * 
 * Error in runGenerator:  Error: You don't seem to have a generator with the name “zx-vue” installed.
 * But help is on the way:
 * You can see available generators via npm search yeoman-generator or via http://yeoman.io/generators/. 
 * Install them with npm install generator-zx-vue.
 * To see all your installed generators run yo --generators.......
 *
 * @author ranSprd
 */
public class CheckInstallationCommand {
    
    private CheckInstallationCommandDialog dialog;

    public CheckInstallationCommand(CheckInstallationCommandDialog dialog) {
        this.dialog = dialog;
    }
    

    public void testAll() {

        dialog.setVisible(true);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("npm", "ll");
            Process process = processBuilder.start();
            
            // the background thread watches the output from the process
            new Thread(() -> {
                try(final InputStream input = process.getInputStream()) {
                    BufferedReader reader
                            = new BufferedReader(new InputStreamReader(input));
                    // no exception thrown here, NPM should available
                    dialog.flagNPMAvail(true);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                        if (line.contains("Yeoman")) {
                            dialog.flagYeomanAvail(true);
                        }
                    }
                } catch (IOException e) {
                    dialog.addMessage(e.getMessage());
                    e.printStackTrace();
                }
            }).start();
            
        } catch (IOException e) {
            dialog.addMessage(e.getMessage());
            e.printStackTrace();
        }

    }
}
