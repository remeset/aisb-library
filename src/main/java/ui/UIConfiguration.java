package ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import net.infonode.docking.RootWindow;
import persistence.api.volume.VolumeEntity;
import persistence.dao.card.LibraryCardDAO;
import persistence.dao.student.StudentDAO;
import persistence.dao.volume.VolumeDAO;
import remote.googlebooks.domain.response.Volume;
import ui.configuration.MenuBarConfiguration;
import ui.configuration.RootWindowConfiguration;
import ui.configuration.ValidationConfiguration;
import ui.core.message.MessageResolver;
import ui.rent.config.RentConfiguration;
import ui.rent.control.CardChangeEventDispatcher;
import ui.rent.control.RentControl;
import ui.rent.model.RentModel;
import ui.rent.view.RentView;
import ui.student.control.StudentAdminControl;
import ui.student.model.StudentAdminModel;
import ui.student.view.StudentAdminView;
import ui.volume.admin.control.VolumeAdminControl;
import ui.volume.admin.model.VolumeAdminModel;
import ui.volume.admin.view.VolumeAdminView;
import ui.volume.search.facade.VolumeListItemModelFacade;
import ui.volume.search.item.domain.LocalVolumeListItemModel;
import ui.volume.search.item.domain.RemoteVolumeListItemModel;
import ui.volume.search.item.visitor.VolumeAdminModelConverter;

@Configurable
@ComponentScan
@PropertySource("classpath:/config/ui.properties")
@Import(value = {
    MenuBarConfiguration.class,
    RootWindowConfiguration.class,
    ValidationConfiguration.class
})
public class UIConfiguration {
    @Value("${ui.rent.defaultToDateOffsetInDays}")
    private long defaultRentToDateOffsetInDays;

    @Bean
    @SuppressWarnings("unused")
    // Please note: even if the main views are defined here, the assemble of the layout is defined in ui.configuration.RootWindowConfiguration
    public VolumeAdminView createVolumeAdminView(
            @Qualifier("localVolumeListItemModelFacade") VolumeListItemModelFacade<LocalVolumeListItemModel, VolumeEntity> localVolumeModelFacade,
            @Qualifier("remoteVolumeListItemModelFacade") VolumeListItemModelFacade<RemoteVolumeListItemModel, Volume> remoteVolumeModelFacade,
            VolumeAdminModelConverter volumeAdminModelConverter,
            VolumeDAO volumeDAO,
            MessageResolver messageResolver,
            ValidatorFactory validatorFactory) {
        VolumeAdminModel model = new VolumeAdminModel.Builder().build();
        VolumeAdminView view = new VolumeAdminView(model.getValidationResultModel(), messageResolver);
        VolumeAdminControl control = new VolumeAdminControl(view, model, localVolumeModelFacade, remoteVolumeModelFacade, volumeAdminModelConverter,volumeDAO, messageResolver, validatorFactory);
        return view;
    }

    @Bean
    private RentView createRentView(RentModel model, MessageResolver messageResolver) {
        return new RentView(model.getValidationResultModel(), messageResolver);
    }

    @Bean
    private RentModel createRentModel() {
        return new RentModel.Builder().build();
    }

    @Bean
    private CardChangeEventDispatcher createRentControl(
            RentView view,
            RentModel model,
            VolumeDAO volumeDAO,
            StudentDAO studentDAO,
            LibraryCardDAO libraryCardDAO,
            MessageResolver messageResolver,
            ValidatorFactory validatorFactory) {
        return new RentControl(
            view,
            model,
            volumeDAO,
            studentDAO,
            libraryCardDAO,
            messageResolver,
            validatorFactory,
            new RentConfiguration.Builder()
                .withDefaultToDateOffsetInDays(defaultRentToDateOffsetInDays)
                .build());
    }

    @Bean
    public StudentAdminView createStudentAdminView(StudentDAO studentDAO, CardChangeEventDispatcher cardChangeEventDispatcher, MessageResolver messageResolver, ValidatorFactory validatorFactory) {
        StudentAdminModel model = new StudentAdminModel.Builder().build();
        StudentAdminView view = new StudentAdminView(model.getValidationResultModel(), messageResolver);
        StudentAdminControl control = new StudentAdminControl(view, model, studentDAO, messageResolver, validatorFactory);
        // After library card the student list has to be updated to show the current state of rented books
        cardChangeEventDispatcher.addCardChangeEventListener(event -> control.reloadStudentList());
        return view;
    }

    @Bean("mainApplicationFrame")
    public JFrame createMainApplicationFrame(@Qualifier("mainApplicationMenuBar") JMenuBar menuBar, @Qualifier("rootWindow") RootWindow rootWindow, MessageResolver messageResolver) {
        JFrame frame = new JFrame(messageResolver.getMessage("i18n.frame.title"));
        frame.getContentPane().add(rootWindow, BorderLayout.CENTER);
        frame.setJMenuBar(menuBar);
        return frame;
    }
}
