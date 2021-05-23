
package ncec.cfweb;

import ncec.cfweb.config.JacksonPopulator;
import ncec.cfweb.config.mongodb.MongoDBConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 *
 * @author DantalioNxxi
 */
@Import({
        HttpEncodingAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class,
        WebClientAutoConfiguration.class,
        WebMvcAutoConfiguration.class,
        MongoDBConfiguration.class,
        JacksonPopulator.class
})
@EnableMongoRepositories(basePackages = "ncec.cfweb.repositories",
        mongoTemplateRef = MongoDBConfiguration.MICROSERVICE_MONGO_TEMPLATE)
@SpringBootApplication
public class CFWebVer2Application {

    public static void main(String[] args) {
        SpringApplication.run(CFWebVer2Application.class, args);
    }
}
