package ui.configuration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.ViewSerializer;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.MixedViewHandler;
import net.infonode.docking.util.ViewMap;
import net.infonode.util.Direction;
import ui.core.message.MessageResolver;
import ui.rent.view.RentView;
import ui.student.view.StudentAdminView;
import ui.volume.admin.view.VolumeAdminView;

@Configuration
public class RootWindowConfiguration {
    @Bean("views")
    public List<View> createViews(VolumeAdminView volumeAdminView, StudentAdminView studentAdminView, RentView rentView, MessageResolver messageResolver) {
        // This is where the views are defined however the layout is initialized with the 'rootWindow.setWindow' method later on
        return Arrays.asList(
            new View(messageResolver.getMessage("i18n.volume.title"), null, volumeAdminView),
            new View(messageResolver.getMessage("i18n.student.title"), null, studentAdminView),
            new View(messageResolver.getMessage("i18n.rent.title"), null, rentView));
    }

    @Bean("viewMap")
    public ViewMap createViewMap(@Qualifier("views") List<View> views) {
        ViewMap viewMap = new ViewMap();
        views.stream().forEach(view -> viewMap.addView(views.indexOf(view), view));
        return viewMap;
    }

    @Bean("rootWindow")
    public RootWindow createRootWindow(@Qualifier("viewMap") ViewMap viewMap, @Qualifier("views") List<View> views, @Qualifier("viewHandler") MixedViewHandler viewHandler, @Qualifier("rootWindowProperties") RootWindowProperties rootWindowProperties, @Qualifier("defaultDockingWindowsTheme") DockingWindowsTheme defaultDockingWindowsTheme) {
        RootWindow rootWindow = DockingUtil.createRootWindow(viewMap, viewHandler, true);
        // Set gradient theme. The theme properties object is the super object of our properties object, which
        // means our property value settings will override the theme values
        rootWindowProperties.addSuperObject(defaultDockingWindowsTheme.getRootWindowProperties());
        // Our properties object is the super object of the root window properties object, so all property values of the
        // theme and in our property object will be used by the root window
        rootWindow.getRootWindowProperties().addSuperObject(rootWindowProperties);
        // Enable window bar
        rootWindow.getWindowBar(Direction.LEFT).setEnabled(true);
        // Setup initial layout of the existing views
        rootWindow.setWindow(
            new SplitWindow(true, 0.35f, new TabWindow(new DockingWindow[]{views.get(0), views.get(1)}), views.get(2))
            // new SplitWindow(true, 0.25f, views.get(0), new SplitWindow(true, 0.6f, new View("blabla", null, new JPanel()), views.get(1)))
        );

        // Store theme as client property
        rootWindow.putClientProperty("theme", defaultDockingWindowsTheme);
        return rootWindow;
    }

    @Bean("rootWindowProperties")
    public RootWindowProperties createRootWindowProperties() {
        return new RootWindowProperties();
    }

    @Bean("viewHandler")
    public MixedViewHandler createViewHandler(@Qualifier("viewMap") ViewMap viewMap, @Qualifier("views") List<View> views) {
        return new MixedViewHandler(viewMap, new ViewSerializer() {
            public void writeView(View view, ObjectOutputStream out) throws IOException {
                out.writeInt(views.indexOf(view));
            }
            public View readView(ObjectInputStream in) throws IOException {
                return views.get(in.readInt());
            }
        });
    }

    @Bean("defaultDockingWindowsTheme")
    public DockingWindowsTheme createDefaultDockingWindowsTheme() {
        return new ShapedGradientDockingTheme();
    }
}
