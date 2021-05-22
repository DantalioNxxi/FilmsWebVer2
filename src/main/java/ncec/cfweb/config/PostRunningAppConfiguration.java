package ncec.cfweb.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ncec.cfweb.config.annotation.PostRunning;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class PostRunningAppConfiguration {

    @PostRunning
    public void runPostRunningActions() {
        log.info("Post running actions started...");
    }
}
