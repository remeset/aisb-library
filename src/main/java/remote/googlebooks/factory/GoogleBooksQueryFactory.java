package remote.googlebooks.factory;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import remote.googlebooks.domain.request.VolumesSearchQuery;

@Component
public class GoogleBooksQueryFactory {
    public static enum Type {
        INTITLE(new MessageFormat("intitle:{0}")),
        INAUTHOR(new MessageFormat("inauthor:{0}")),
        ISBN(new MessageFormat("isbn:{0}"));

        private final MessageFormat format;

        private Type(MessageFormat format) {
            this.format = format;
        }

        private String format(String value) {
            return format.format(new String[]{value});
        }
    }

    private static class QueryFragment {
        private final Type type;
        private final String value;

        public QueryFragment(Type type, String value) {
            this.type = type;
            this.value = value;
        }
    }

    public String create(VolumesSearchQuery query) {
        return Arrays.asList(
                query.getTitle().map(title -> new QueryFragment(Type.INTITLE, title)),
                query.getAuthor().map(title -> new QueryFragment(Type.INAUTHOR, title)),
                query.getIsbn().map(title -> new QueryFragment(Type.ISBN, title)))
            .stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(fragment -> fragment.type.format(fragment.value))
            .collect(Collectors.joining("+"));
    }
}
