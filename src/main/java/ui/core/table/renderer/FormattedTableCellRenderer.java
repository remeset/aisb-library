package ui.core.table.renderer;

import java.text.Format;
import java.util.Optional;

import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class FormattedTableCellRenderer extends DefaultTableCellRenderer {
    private final Format format;

    public FormattedTableCellRenderer(Format format) {
        this.format = format;
    }

    @Override
    protected void setValue(Object value) {
        super.setValue(Optional.ofNullable(value).map(date -> format.format(date)).orElse(""));
    }
}
