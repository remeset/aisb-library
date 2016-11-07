package remote.googlebooks.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import remote.googlebooks.api.GoogleBooksResource;
import remote.googlebooks.domain.request.VolumesSearchQuery;
import remote.googlebooks.domain.response.Volumes;
import remote.googlebooks.factory.GoogleBooksQueryFactory;

@Component
public class BooksFacade {
    Logger logger = LoggerFactory.getLogger(BooksFacade.class);

    @Autowired
    private GoogleBooksQueryFactory googleBooksQueryFactory;

    @Autowired
    private GoogleBooksResource googleBooksResource;

    public Volumes findVolumes(VolumesSearchQuery query) {
        String request = googleBooksQueryFactory.create(query);
        logger.info("Calling Google Books API with '{}' query from '{}' to '{}'.", request, query.getStartIndex(), query.getStartIndex() + query.getMaxResults());
        return googleBooksResource.lookup(request, query.getStartIndex(), query.getMaxResults()).getBody();
    }
}
