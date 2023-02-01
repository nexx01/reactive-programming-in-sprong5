package pub_sub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.lang.String.format;

@RestController
public class TemperatureController {

    static final long SSE_SESSION_TIMEOUT = 30 * 60 * 1000L;
    public static final Logger log = LoggerFactory.getLogger(TemperatureController.class);

    private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

    @RequestMapping(value = "/temperature-stream", method = RequestMethod.GET)
    public SseEmitter events(HttpServletRequest request) {
        log.info("SSE stream opened for client: " + request.getRemoteAddr());

        SseEmitter emitter = new SseEmitter(SSE_SESSION_TIMEOUT);

        clients.add(emitter);

        emitter.onTimeout(() -> clients.remove(emitter));
        emitter.onCompletion(() -> clients.remove(emitter));
        return emitter;
    }

    @Async
    @EventListener
    public void handleMessage(Temperature temperature) {
        log.info(format("Temperature: %4.2f C, active subscribers: %d",
                temperature.getValue(), clients.size()));

        List<SseEmitter> deadEmiters = new ArrayList<>();
        clients.forEach(sseEmitter -> {
            try {
                var now = Instant.now();
                sseEmitter.send(temperature, MediaType.APPLICATION_JSON);
                log.info("Sent to client, took: {}", Duration.between(now, Instant.now()));

            } catch (Exception ignore) {
                deadEmiters.add(sseEmitter);
            }

            clients.removeAll(deadEmiters);
        });
    }

    @ExceptionHandler(value = AsyncRequestTimeoutException.class)
    public ModelAndView handleTimeout(HttpServletResponse rsp) throws IOException {
        if (!rsp.isCommitted()) {
            rsp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
        return new ModelAndView();
    }
}
