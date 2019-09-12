import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainTestConfig {

    @Bean
    public BlopListener blopListener() {
        return new BlopListener();
    }

}
