package remote;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import feign.Logger;
import feign.Logger.Level;
import feign.slf4j.Slf4jLogger;

@Configuration
@ComponentScan
@EnableFeignClients
@PropertySource("classpath:/config/remote.properties") // Setup property source for 'config/remote.properties'
public class RemoteConfiguration {
    @Bean("feignLogger")
    public Logger createFeignLogger() {
        return new Slf4jLogger();
    }

    @Bean("feignLoggerLevel")
    public Level setupFeignLoggerLevel() {
        return Level.FULL;
    }
}
