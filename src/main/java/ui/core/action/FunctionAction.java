package ui.core.action;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class FunctionAction extends AbstractAction {
    private final Consumer<ActionEvent> action;

    public FunctionAction(Consumer<ActionEvent> action) {
        this.action = action;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        action.accept(event);
    }

}
