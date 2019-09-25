import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainTestConfig {

    @Bean
    public BlopEventListener blopListener() {
        return new BlopEventListener();
    }

}
