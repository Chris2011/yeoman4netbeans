package io.github.chris2011.netbeans.plugins.yeoman4netbeans;

import io.github.chris2011.netbeans.plugins.yeoman4netbeans.npm.CheckInstallationCommand;
import io.github.chris2011.netbeans.plugins.yeoman4netbeans.npm.CheckInstallationCommandDialog;
import io.github.chris2011.netbeans.plugins.yeoman4netbeans.npm.YeomanCommand;
import io.github.chris2011.netbeans.plugins.yeoman4netbeans.npm.YeomanDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Bugtracking",
        id = "io.github.chris2011.netbeans.plugins.yeoman4netbeans.OpenYeoman"
)
@ActionRegistration(
        iconBase = "io/github/chris2011/netbeans/plugins/yeoman4netbeans/g.png",
        displayName = "#CTL_OpenYeoman"
)
@ActionReference(path = "Toolbars/File", position = 0)
@Messages("CTL_OpenYeoman=Open Yeoman")
public final class OpenYeoman implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
//        YeomanTerminal term = new YeomanTerminal(null, false);
//        term.setVisible(true);

//        CheckInstallationCommand checkInstallation = new CheckInstallationCommand(new CheckInstallationCommandDialog(null, false));
//        checkInstallation.testAll();

        YeomanCommand yeomanCommand = new YeomanCommand( new YeomanDialog(null, false));
        yeomanCommand.execute();
    }
}
