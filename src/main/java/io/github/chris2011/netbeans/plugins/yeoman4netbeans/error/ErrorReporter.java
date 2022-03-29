package io.github.chris2011.netbeans.plugins.yeoman4netbeans.error;

/**
 *
 * @author jocke
 */
public interface ErrorReporter {
    
    void handle(LintError error);
    
    void done();
}
