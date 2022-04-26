package io.github.chris2011.netbeans.plugins.yeoman4netbeans.npm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 *
 * @author ranSprd
 */
public class YeomanIO implements Closeable {

    private final BufferedWriter writer;
    private final BufferedReader reader;


    public YeomanIO(Process process) {
        reader = new BufferedReader(new InputStreamReader( process.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
    }

    public String readLine() throws IOException {
        String result = reader.readLine();
        System.out.println("READ [" +result +"]");
        return result;
    }
    
    public void write(String str) {
        System.out.println("WRITE [" +str +"]");
        try {
            writer.write(str);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void close() throws IOException {
        // @todo catch input exception to ensure that out is closed in error case????
        reader.close();
        writer.close();
    }
    
}
