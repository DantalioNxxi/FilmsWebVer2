package ncec.cfweb.config.annotation;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.lang.annotation.*;

/**
 * The annotated method executed immediately after the application running.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EventListener(ApplicationReadyEvent.class)
public @interface PostRunning {
}
