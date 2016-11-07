package persistence;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan
@EntityScan
@EnableJpaRepositories
@PropertySource("classpath:/config/persistence.properties") // Setup property source for 'config/persistence.properties'
public class PersistenceConfiguration {
}
