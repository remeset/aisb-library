package remote.googlebooks.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import remote.googlebooks.domain.response.Volumes;

/**
 * Feing (as no Eureka in the background Hystrix and Ribbon has to be turned off due to the following issue: https://github.com/spring-cloud/spring-cloud-netflix/issues/768)
 * More info (API): https://developers.google.com/books/docs/v1/using
 */
@FeignClient(url = "https://www.googleapis.com", name = "googleapis")
public interface GoogleBooksResource {
    @RequestMapping(value = "/books/v1/volumes?key=${remote.api.google.books.apiKey}", method = RequestMethod.GET)
    public ResponseEntity<Volumes> lookup(@RequestParam("q") String query, @RequestParam("startIndex") int startIndex, @RequestParam("maxResults") int maxResults);
}
