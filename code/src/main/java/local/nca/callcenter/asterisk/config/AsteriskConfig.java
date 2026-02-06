package local.nca.callcenter.asterisk.config;

import org.asteriskjava.manager.ManagerConnectionFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AsteriskProperties.class)
public class AsteriskConfig {

    @Bean
    public ManagerConnectionFactory managerConnectionFactory(AsteriskProperties properties) {
        return new ManagerConnectionFactory(
            properties.getHost(),
            properties.getPort(),
            properties.getUsername(),
            properties.getPassword()
        );
    }

}
