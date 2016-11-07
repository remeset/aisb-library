package ui.volume.search.query.view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import persistence.api.volume.VolumeEntity;
import remote.googlebooks.domain.response.Volume;
import ui.core.message.MessageResolver;
import ui.volume.search.item.domain.LocalVolumeListItemModel;
import ui.volume.search.item.domain.RemoteVolumeListItemModel;
import ui.volume.search.result.view.VolumeSearchResultView;

@SuppressWarnings("serial")
public class VolumeSearchQueryModuleView extends JPanel {
    private final JTextField titleField;
    private final JTextField authorField;
    private final JTextField isbnField;
    private final JButton searchButton;
    private JTabbedPane resultTabbedPane;
    private final VolumeSearchResultView<LocalVolumeListItemModel, VolumeEntity> localResultView;
    private final VolumeSearchResultView<RemoteVolumeListItemModel, Volume> remoteResultView;

    public VolumeSearchQueryModuleView(MessageResolver messageResolver) {
        setLayout(new FormLayout(new ColumnSpec[] {
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_ROWSPEC,}));

        JLabel titleLabel = new JLabel(messageResolver.getMessage("i18n.search.form.byTitle"));
        add(titleLabel, "2, 2, right, default");

        titleField = new JTextField();
        add(titleField, "4, 2, 3, 1");
        titleField.setColumns(10);

        JLabel authorLabel = new JLabel(messageResolver.getMessage("i18n.search.form.byAuthor"));
        add(authorLabel, "2, 4, right, default");

        authorField = new JTextField();
        add(authorField, "4, 4, 3, 1");
        authorField.setColumns(10);

        JLabel isbnLabel = new JLabel(messageResolver.getMessage("i18n.search.form.byISBN"));
        add(isbnLabel, "2, 6, right, default");

        isbnField = new JTextField();
        add(isbnField, "4, 6, 3, 1, fill, default");
        isbnField.setColumns(10);

        searchButton = new JButton(messageResolver.getMessage("i18n.search.control.search"));
        add(searchButton, "6, 8, right, default");

        JSeparator resultListSeparator = new JSeparator();
        add(resultListSeparator, "2, 10, 5, 1");

        resultTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        add(resultTabbedPane, "2, 12, 5, 1, fill, fill");

        localResultView = new VolumeSearchResultView<LocalVolumeListItemModel, VolumeEntity>(messageResolver);
        resultTabbedPane.addTab("Local", null, localResultView, null);

        remoteResultView = new VolumeSearchResultView<RemoteVolumeListItemModel, Volume>(messageResolver);
        resultTabbedPane.addTab("Remote", null, remoteResultView, null);
    }

    public JTextField getTitleField() {
        return titleField;
    }

    public JTextField getAuthorField() {
        return authorField;
    }

    public JTextField getIsbnField() {
        return isbnField;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public VolumeSearchResultView<LocalVolumeListItemModel, VolumeEntity> getLocalResultView() {
        return localResultView;
    }

    public VolumeSearchResultView<RemoteVolumeListItemModel, Volume> getRemoteResultView() {
        return remoteResultView;
    }
}
