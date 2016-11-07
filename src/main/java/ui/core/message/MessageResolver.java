package ui.core.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageResolver {
    private final MessageSource messageSource;

    @Autowired
    public MessageResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code, Object ... args) throws NoSuchMessageException {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
