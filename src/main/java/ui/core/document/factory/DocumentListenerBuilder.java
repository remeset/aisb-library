package ui.core.document.factory;

import java.util.function.Consumer;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DocumentListenerBuilder {
    private static final Consumer<DocumentEvent> EMPTY_LISTENER = event -> {};

    private Consumer<DocumentEvent> changeListener = EMPTY_LISTENER;
    private Consumer<DocumentEvent> insertListener = EMPTY_LISTENER;
    private Consumer<DocumentEvent> removeListener = EMPTY_LISTENER;
    private Consumer<DocumentEvent> genericListener = EMPTY_LISTENER;

    public DocumentListenerBuilder withListener(Consumer<DocumentEvent> genericListener) {
        this.genericListener = genericListener;
        return this;
    }

    public DocumentListenerBuilder withChangeListener(Consumer<DocumentEvent> changeListener) {
        this.changeListener = changeListener;
        return this;
    }

    public DocumentListenerBuilder withInsertListener(Consumer<DocumentEvent> insertListener) {
        this.insertListener = insertListener;
        return this;
    }

    public DocumentListenerBuilder withRemoveListener(Consumer<DocumentEvent> removeListener) {
        this.removeListener = removeListener;
        return this;
    }

    public DocumentListener build() {
        return new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                removeListener.accept(e);
                genericListener.accept(e);
            }
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                insertListener.accept(e);
                genericListener.accept(e);
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                changeListener.accept(e);
                genericListener.accept(e);
            }
        };
    }
}
