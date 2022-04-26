package io.github.chris2011.netbeans.plugins.yeoman4netbeans;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.netbeans.api.extexecution.base.BaseExecutionDescriptor;
import org.netbeans.api.extexecution.base.BaseExecutionService;
import org.netbeans.api.extexecution.base.ProcessBuilder;
import org.netbeans.api.extexecution.base.input.InputProcessors;
import org.openide.awt.NotificationDisplayer;
import org.openide.util.Exceptions;

/**
 *
 * @author Chris
 */
public class YeomanTerminal extends JDialog {
    private static final Logger LOG = Logger.getLogger(YeomanTerminal.class.getName());

    /**
     * Creates new form YeomanTerminal
     */
    public YeomanTerminal(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        SwingUtilities.invokeLater(() -> {
            test();
        });
        //        getCliOutput();
        //        getCliOutput(new ErrorReporter() {
        //            @Override
        //            public void handle(LintError error) {
        ////                Line currentLine = lineCookie.getLineSet().getCurrent(error.getLine() - 1);
        ////                Line.Part currentPartLine = currentLine.createPart(error.getStartCol() - 1, error.getEndCol() - error.getStartCol());
        ////
        ////                final ESLintAnnotation annotation = ESLintAnnotation.create(
        ////                        ESLintAnnotation.Type.get(error.getSeverity()),
        ////                        error.getMessage(),
        ////                        error.getLine(),
        ////                        error.getStartCol(),
        ////                        error.getEndCol(),
        ////                        currentPartLine);
        ////
        ////                MAPPING.get(fileObject).add(annotation);
        ////
        ////                annotation.addPropertyChangeListener(new PropertyChangeListener() {
        ////                    @Override
        ////                    public void propertyChange(PropertyChangeEvent evt) {
        ////                        if (ESLintAnnotation.ATTACHED.equals(evt.getPropertyName())) {
        ////                            annotation.removePropertyChangeListener(this);
        ////                            if (MAPPING.containsKey(fileObject)) {
        ////                                MAPPING.get(fileObject).remove(annotation);
        ////                            }
        ////                        }
        ////                    }
        ////                });
        //            }
        //
        //            @Override
        //            public void done() {
        ////                LOG.log(Level.FINE, "Scannig done of {0}", fileObject.getName());
        //            }
        //        });
    }

//    ExecutionDescriptor descriptor2 = new ExecutionDescriptor().frontWindow(true).controllable(true);
    private void renderControl(String type, String label) {
        switch (type) {
            case "input":
                JLabel lblQuestion = new JLabel(label);
                JTextField txtAnswer = new JTextField();

                pnlQuestionControls.setVisible(false);
                pnlQuestionControls.removeAll();
                pnlQuestionControls.add(lblQuestion, BorderLayout.CENTER);
                pnlQuestionControls.add(txtAnswer, BorderLayout.SOUTH);
                pnlQuestionControls.setVisible(true);

                btnNext.addActionListener((e) -> {
                    txtAnswer.getText(); // TODO: Use this text to send back to the CLI which is waiting for user input

//                    descriptor.outProcessorFactory(() -> {
//                        InputProcessors.copying(new BufferedWriter(new OutputStreamWriter(System.out)));
//                    });
                    // TODO: Send answer back to console/exection process which is waiting for input.
                });
                break;
            default:
                break;
        }
    }

    private BaseExecutionDescriptor descriptor;

    private void test() {
        JOptionPane.showMessageDialog(null, "In Test");
        try {
            JLabel lblLoading = new JLabel("Loading generator...");

            pnlQuestionControls.add(lblLoading);

//                ".\\src\\main\\java\\io\\github\\chris2011\\netbeans\\plugins\\yeoman4netbeans\\cli.js",
            Runtime rt = Runtime.getRuntime();
            String[] commands = {
                "node",
                "./src/main/java/io/github/chris2011/netbeans/plugins/yeoman4netbeans/cli.js",
                "--generatorName",
                "zx-vue",
                "--newYoVersion"
            };
            Process proc = rt.exec(commands);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
//
//            BufferedWriter stdWriter = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
            // read the output from the command
            String s = null;
            
            LOG.log(java.util.logging.Level.SEVERE, null, "Smth is going on");
            System.out.println("Here is the standard output of the command:\n");
            JLabel l = new JLabel(s);
            txtPnlYoConsoleOutput.add(l);

//            JOptionPane.showMessageDialog(null, "Bla bla bla" + stdInput.readLine());
            while ((s = stdInput.readLine()) != null) {
//                LOG.log(Level.FINE, "Show line from yeoman {0}", s);
//                System.out.println(s);

                try {
                    JOptionPane.showMessageDialog(null, "in while processLine: " + s);

                    final JSONParser jsonParser = new JSONParser();
//                final JSONArray jsonArray = (JSONArray) jsonParser.parse(string);
                    final JSONObject jsonLineToParse = (JSONObject) jsonParser.parse(s);
                    String type = jsonLineToParse.get("type").toString();
                    String label = jsonLineToParse.get("message").toString();

                    renderControl(type, label);
//                    reporter.handle(new LintError("", 1, 1, 2, 1, "bla"));
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(null, "Error in while: " + ex);
                    Exceptions.printStackTrace(ex);
                }
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
                JOptionPane.showMessageDialog(null, s);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
    }

//    private Future<Integer> getCliOutput(ErrorReporter reporter) {
    private Future<Integer> getCliOutput() {
        String nodeExec = "node";
        String command = nodeExec;

        JLabel lblLoading = new JLabel("Loading generator...");

        pnlQuestionControls.add(lblLoading);

        descriptor = new BaseExecutionDescriptor();
//        descriptor = descriptor.errProcessorFactory(() -> InputProcessors.bridge(new LineProcessorAdapter() {
//            @Override
//            public void processLine(String string) {
//                JOptionPane.showMessageDialog(null, "in processLine but error: " + string);
//                LOG.log(Level.FINE, "Show error line from yeoman {0}", string);
//                ErrorManager.getDefault().log(string);
//            }
//        }));
        descriptor = descriptor.outProcessorFactory(() -> InputProcessors.bridge(new LineProcessorAdapter() {
            @Override
            public void processLine(String string) {
                JLabel l = new JLabel(string);
                txtPnlYoConsoleOutput.add(l);

                JOptionPane.showMessageDialog(null, "in processLine: " + string);
                LOG.log(Level.FINE, "Show line from yeoman {0}", string);

                try {
                    final JSONParser jsonParser = new JSONParser();
//                final JSONArray jsonArray = (JSONArray) jsonParser.parse(string);
                    final JSONObject jsonLineToParse = (JSONObject) jsonParser.parse(string);
                    String type = jsonLineToParse.get("type").toString();
                    String label = jsonLineToParse.get("message").toString();

                    renderControl(type, label);
//                    reporter.handle(new LintError("", 1, 1, 2, 1, "bla"));
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                    Exceptions.printStackTrace(ex);
                }
            }

            @Override
            public void close() {
                LOG.log(Level.FINE, "Scanning done");
//                reporter.done();
            }
        }));

        final ProcessBuilder builder = ProcessBuilder.getLocal();

        if (!command.isEmpty()) {
//            if (fileObject.isFolder()) {
//                builder.setWorkingDirectory(FileUtil.toFile(fileObject).getAbsolutePath());
//            } else {
//                final Project owner = FileOwnerQuery.getOwner(fileObject);
//
//                if (owner != null) {
//                    Project project = ProjectUtils.getInformation(owner).getProject();
//
//                    if (project != null) {
//                        FileObject projectDirectory = project.getProjectDirectory();
//                        builder.setWorkingDirectory(projectDirectory.getPath());
//                    }
//                }
//            }

            BaseExecutionService service = null;

            builder.setExecutable(command.trim());

            builder.setArguments(Arrays.asList(
                    ".\\src\\main\\java\\io\\github\\chris2011\\netbeans\\plugins\\yeoman4netbeans\\cli.js",
                    "--generatorName",
                    "zx-vue",
                    "--newYoVersion"));

            LOG.log(Level.INFO, "Running command {0}", command);

            service = BaseExecutionService.newService(() -> {
                try {
                    return builder.call();
                } catch (IOException err) {
                    NotificationDisplayer.getDefault().notify("Smth went wrong", NotificationDisplayer.Priority.HIGH.getIcon(), "There is a problem while using ESLint, please check your options while clicking here. Probably the path to the ESLint CLI is not correct.", (ActionEvent arg0) -> {
                        // TODO: set yeoman executable for example.
                        JOptionPane.showMessageDialog(null, "nulllllllll");
                    });

                    return null;
                }
            }, descriptor);

            return service.run();
        }

        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtPnlYoConsoleOutput = new javax.swing.JTextPane();
        pnlQuestionControls = new javax.swing.JPanel();
        btnNext = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        txtPnlYoConsoleOutput.setEditable(false);
        jScrollPane1.setViewportView(txtPnlYoConsoleOutput);

        pnlQuestionControls.setLayout(new java.awt.BorderLayout());

        btnNext.setText("Next");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlQuestionControls, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnNext)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlQuestionControls, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNext)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(YeomanTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            YeomanTerminal dialog = new YeomanTerminal(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNext;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlQuestionControls;
    private javax.swing.JTextPane txtPnlYoConsoleOutput;
    // End of variables declaration//GEN-END:variables
}
