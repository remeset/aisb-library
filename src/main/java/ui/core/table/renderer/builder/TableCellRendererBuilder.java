package ui.core.table.renderer.builder;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

import javax.swing.table.DefaultTableCellRenderer;

import ui.core.table.renderer.FormattedTableCellRenderer;

public class TableCellRendererBuilder {
    @SuppressWarnings("serial")
    public static class StringFormat extends Format {
        private final String format;

        public StringFormat(String format) {
            this.format = format;
        }

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return toAppendTo.append(String.format(format, obj));
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            throw new IllegalStateException("Operation not supported");
        }
    }

    private Format format = new StringFormat("%s");
    private int alignment;

    public TableCellRendererBuilder withHorizontalAlignment(int alignment) {
        this.alignment = alignment;
        return this;
    }

    public TableCellRendererBuilder withFormat(Format format) {
        this.format = format;
        return this;
    }

    public DefaultTableCellRenderer build() {
        DefaultTableCellRenderer renderer = new FormattedTableCellRenderer(format);
        renderer.setHorizontalAlignment(alignment);
        return renderer;
    }
}
