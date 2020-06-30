package ee.itcollege.anluik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableAsync
@SpringBootApplication
public class DictionaryApiApp {

    public static void main(String[] args) {
        SpringApplication.run(DictionaryApiApp.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                System.out.println("ADD CORS MAPPINGS ------------------------------------");
                registry.addMapping("/words")
                        .allowedMethods("*")
                        .allowedOrigins("http://localhost:8080");
            }
        };
    }
}
