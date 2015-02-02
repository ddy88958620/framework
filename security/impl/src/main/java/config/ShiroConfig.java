package config;

import com.handu.apollo.security.backend.MySQLSecurity;
import com.handu.apollo.security.backend.SecurityBackend;
import com.handu.apollo.security.config.AbstractShiroConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by markerking on 14-5-14.
 */
@Configuration
public class ShiroConfig extends AbstractShiroConfig {
    @Bean
    public SecurityBackend securityBackend() {
        return new MySQLSecurity();
    }
}
