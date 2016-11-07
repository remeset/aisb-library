package ui.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.infonode.docking.RootWindow;
import net.infonode.docking.View;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.BlueHighlightDockingTheme;
import net.infonode.docking.theme.ClassicDockingTheme;
import net.infonode.docking.theme.DefaultDockingTheme;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.GradientDockingTheme;
import net.infonode.docking.theme.LookAndFeelDockingTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.theme.SlimFlatDockingTheme;
import net.infonode.docking.theme.SoftBlueIceDockingTheme;
import net.infonode.docking.util.DockingUtil;
import ui.core.message.MessageResolver;

/**
 * The configuration is responsible for main application menu initialization with the following menu structure
 * + [File]
 * |   \ [Exit]
 * |
 * \ [View]
 *     + [Application view #1]
 *     + ...
 *     + [Application view #N]
 *     + ---------------------
 *     + [Look And Feels]
 *     |   + [Available Look And Feel #1]
 *     |   + ...
 *     |   \ [Available Look And Feel #N]
 *     + ---------------------
 *     \ [Themes]
 */
@Configuration
public class MenuBarConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(MenuBarConfiguration.class);

    @Bean("mainApplicationMenuBar")
    public JMenuBar createMenuBar(@Qualifier("fileMenu") JMenu fileMenu, @Qualifier("viewMenu") JMenu viewMenu) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        return menuBar;
    }

    @Bean("fileMenu")
    public JMenu createFileMenu(@Qualifier("exitMenuItem") JMenuItem exitMenuItem, MessageResolver messageResolver) {
        JMenu fileMenu = new JMenu(messageResolver.getMessage("i18n.menu.file"));
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    @Bean("exitMenuItem")
    public JMenuItem createExitMenuItem(@Qualifier("rootWindow") RootWindow rootWindow, MessageResolver messageResolver) {
        JMenuItem exitMenuItem = new JMenuItem(messageResolver.getMessage("i18n.menu.exit"));
        exitMenuItem.addActionListener(event -> {
            SwingUtilities.getWindowAncestor(rootWindow).dispose();
            System.exit(0);
        });
        return exitMenuItem;
    }

    @Bean("viewMenu")
    public JMenu createViewMenu(@Qualifier("viewMenuItems") List<JMenuItem> viewMenuItems, @Qualifier("lookAndFeelMenu") JMenu lookAndFeelMenu, @Qualifier("themeMenu") JMenu themeMenu, MessageResolver messageResolver) {
        JMenu viewMenu = new JMenu(messageResolver.getMessage("i18n.menu.view"));
        viewMenuItems.stream().forEach(item -> viewMenu.add(item));
        viewMenu.addSeparator();
        viewMenu.add(lookAndFeelMenu);
        viewMenu.add(themeMenu);
        return viewMenu;
    }

    @Bean("lookAndFeelMenu")
    public JMenu createLookAndFeelMenu(@Qualifier("rootWindow") RootWindow rootWindow, MessageResolver messageResolver) {
        JMenu lookAndFeelMenu = new JMenu(messageResolver.getMessage("i18n.menu.lookAndFeels"));
        ButtonGroup lookAndFeelRadioButtonGroup = new ButtonGroup();
        Arrays.asList(UIManager.getInstalledLookAndFeels()).stream().forEach(lookAndFeelInfo -> {
            JRadioButtonMenuItem item;
            item = new JRadioButtonMenuItem(lookAndFeelInfo.getName());
            item.setSelected(UIManager.getLookAndFeel().getName().equals(lookAndFeelInfo.getName())); // Select radio button if the given look and feel is used by the application
            item.addActionListener(event -> {
                try {
                    UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
                    SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(rootWindow)); // Please note: this is just a workaround to avoid circular dependencies
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    logger.warn("The following error occured dureing the look and feel selection: ", ex);
                }
            });
            lookAndFeelRadioButtonGroup.add(item); // Update radio button group
            lookAndFeelMenu.add(item);
        });
        return lookAndFeelMenu;
    }

    @Bean("themeMenu")
    public JMenu createThemeMenu(@Qualifier("rootWindow") RootWindow rootWindow, @Qualifier("rootWindowProperties") RootWindowProperties rootWindowProperties, @Qualifier("themes") List<DockingWindowsTheme> themes, MessageResolver messageResolver) {
        JMenu themeMenu = new JMenu(messageResolver.getMessage("i18n.menu.themes"));
        ButtonGroup themeRadioButtonGroup = new ButtonGroup();
        themes.stream().forEach(dockingWindowsTheme -> {
            JRadioButtonMenuItem item;
            item = new JRadioButtonMenuItem(dockingWindowsTheme.getName());
            item.setSelected(((DockingWindowsTheme) rootWindow.getClientProperty("theme")).getName().equals(dockingWindowsTheme.getName())); // Please note: the current name must be in client properties
            item.addActionListener(event -> {
                // Update root window property set
                rootWindowProperties.getMap().clear(true);
                rootWindowProperties.replaceSuperObject(((DockingWindowsTheme) rootWindow.getClientProperty("theme")).getRootWindowProperties(), dockingWindowsTheme.getRootWindowProperties());
                // Store selected theme as client property
                rootWindow.putClientProperty("theme", dockingWindowsTheme);
            });
            themeRadioButtonGroup.add(item); // Update radio button group
            themeMenu.add(item);
        });
        return themeMenu;
    }

    @Bean("viewMenuItems")
    public List<JMenuItem> createViewMenuItems(@Qualifier("rootWindow") RootWindow rootWindow, @Qualifier("views") List<View> views) {
        return views.stream().map(view -> {
            final JMenuItem item;
            item = new JMenuItem(view.getTitle());
            item.addActionListener(event -> {
                // If the view is already displayed then it needs to get the focus
                if (view.getRootWindow() != null)
                    view.restoreFocus();
                // Otherwise it needs to be opened
                else {
                    DockingUtil.addWindow(view, rootWindow);
                }
            });
            return item;
        }).collect(Collectors.toList());
    }

    @Bean("themes")
    public List<DockingWindowsTheme> createThemes() {
        return Arrays.asList(
            new DefaultDockingTheme(),
//            new LookAndFeelDockingTheme(),
            new BlueHighlightDockingTheme(),
            new SlimFlatDockingTheme(),
            new GradientDockingTheme(),
            new ShapedGradientDockingTheme(),
            new SoftBlueIceDockingTheme(),
            new ClassicDockingTheme());
    }
}
