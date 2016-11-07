package application;

import javax.swing.JFrame;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import persistence.PersistenceConfiguration;
import remote.RemoteConfiguration;
import ui.UIConfiguration;
import ui.splash.view.SplashWindow;

@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration
@Import(value = {
    UIConfiguration.class,
    RemoteConfiguration.class,
    PersistenceConfiguration.class})
public class Application {
    public static void main(String[] args) {
        // 1. Show splash screen
        SplashWindow splashWindow = new SplashWindow();
        splashWindow.pack();
        splashWindow.setLocationRelativeTo(null);
        splashWindow.setVisible(true);
        // 2. Initialize Spring Application Context
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class).headless(false).run(args);
        // 3. Hide splash screen
        splashWindow.setVisible(false);
        // 4. Show main application frame
        JFrame findBookFrame = context.getBean("mainApplicationFrame", JFrame.class);
        findBookFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        findBookFrame.pack();
        findBookFrame.setVisible(true);
    }
}
