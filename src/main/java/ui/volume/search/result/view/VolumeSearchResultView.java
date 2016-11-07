package ui.volume.search.result.view;

import java.awt.SystemColor;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import ui.core.message.MessageResolver;
import ui.volume.search.item.domain.VolumeListItemModel;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class VolumeSearchResultView<V extends VolumeListItemModel<D>, D> extends JPanel {
    private final JSpinner pageNumberSpinner;
    private final JLabel totalPageNumberLabel;
    private final JProgressBar searchProgressBar;
    private final JScrollPane resultScrollPane;
    private final JList<V> resultList;
    private final JPanel insufficientSearchCriteriaNotificationPanel;
    private final JLabel insufficientSearchCriteriaNotificationLabel;

    public VolumeSearchResultView(MessageResolver messageResolver) {
        setLayout(new FormLayout(new ColumnSpec[] {
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,},
            new RowSpec[] {
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,}));

        insufficientSearchCriteriaNotificationPanel = new JPanel();
        insufficientSearchCriteriaNotificationPanel.setBackground(SystemColor.info);
        insufficientSearchCriteriaNotificationPanel.setVisible(false);
        add(insufficientSearchCriteriaNotificationPanel, "2, 2, 9, 1, fill, fill");

        insufficientSearchCriteriaNotificationLabel = new JLabel(messageResolver.getMessage("i18n.search.notification.insufficientSearchCriteria"));
        insufficientSearchCriteriaNotificationLabel.setIcon(new ImageIcon(VolumeSearchResultView.class.getResource("/com/sun/java/swing/plaf/windows/icons/Warn.gif")));
        insufficientSearchCriteriaNotificationPanel.add(insufficientSearchCriteriaNotificationLabel);

        resultScrollPane = new JScrollPane();
        add(resultScrollPane, "2, 4, 9, 1, fill, fill");

        resultList = new JList<V>();
        resultScrollPane.setViewportView(resultList);

        searchProgressBar = new JProgressBar();
        searchProgressBar.setIndeterminate(true);
        searchProgressBar.setVisible(false);
        add(searchProgressBar, "2, 6");

        JLabel pageNumberLabel = new JLabel(messageResolver.getMessage("i18n.search.control.pageNumber"));
        add(pageNumberLabel, "4, 6, right, default");

        pageNumberSpinner = new JSpinner();
        pageNumberSpinner.setPreferredSize(new Dimension(40, 20));
        add(pageNumberSpinner, "6, 6");

        JLabel perLabel = new JLabel("/");
        add(perLabel, "8, 6, right, center");

        totalPageNumberLabel = new JLabel("0");
        add(totalPageNumberLabel, "10, 6, left, center");
    }

    public JPanel getInsufficientSearchCriteriaNotificationPanel() {
        return insufficientSearchCriteriaNotificationPanel;
    }

    public JList<V> getResultList() {
        return resultList;
    }

    public JSpinner getPageNumberSpinner() {
        return pageNumberSpinner;
    }

    public JLabel getTotalPageNumberLabel() {
        return totalPageNumberLabel;
    }

    public JProgressBar getSearchProgressBar() {
        return searchProgressBar;
    }

}
