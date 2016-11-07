package ui.splash.view;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import ui.volume.search.result.view.VolumeSearchResultView;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class SplashWindow extends JWindow {
    public SplashWindow() {
        getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
                FormSpecs.UNRELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.UNRELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormSpecs.PARAGRAPH_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.PARAGRAPH_GAP_ROWSPEC,}));

        // As at this point there is no running spring context it's not possible to rely on MessageResolver (as it is a Spring bean)
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(new ImageIcon(VolumeSearchResultView.class.getResource("/media/aisb.png")));
        getContentPane().add(logoLabel, "2, 2");
        
        JLabel splashScreenLabel = new JLabel(loadSplashScreenTitleMessage());
        splashScreenLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        splashScreenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(splashScreenLabel, "2, 4");

        JProgressBar splashProgressBar = new JProgressBar();
        splashProgressBar.setIndeterminate(true);
        getContentPane().add(splashProgressBar, "2, 6");
    }

    private String loadSplashScreenTitleMessage() {
        Properties properties = new Properties();
        try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("messages.properties")) {
            properties.load(inputStream);
            return properties.getProperty("i18n.splash.title");
        } catch (IOException ex) {
            throw new IllegalStateException("Message properties cannot be loaded.");
        }
    }
    
}
