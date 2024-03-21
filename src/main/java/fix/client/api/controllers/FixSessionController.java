package fix.client.api.controllers;

import fix.client.api.models.properties.FixSessionDescriptionProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/fix-sessions")
public class FixSessionController {

    private final FixSessionDescriptionService fixSessionDescriptionService;

    @Autowired
    public FixSessionController(FixSessionDescriptionService fixSessionDescriptionService) {
        this.fixSessionDescriptionService = fixSessionDescriptionService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSession(@RequestBody CreateFixSessionRequest createFixSessionRequest) {
        var sessionInfo = fixSessionDescriptionService.createSession(createFixSessionRequest);
        return ResponseEntity.ok(sessionInfo);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatusList() {
        return ResponseEntity.ok(fixSessionDescriptionService.selectSession());
    }

    @GetMapping(path = "/{session_uuid}/status")
    public ResponseEntity<?> getStatusById(@PathVariable String sessionUUID) {
        return ResponseEntity.ok(fixSessionDescriptionService.selectSession(sessionUUID));
    }

    @PostMapping(path = "/{sessionUUID}/action")
    public ResponseEntity<FixSessionDescriptionProperties> action(
            @PathVariable String sessionUUID,
            @RequestBody Map<String, String> body
    ) {
        var action = body.get("action");
        if (Objects.equals(action, "connect"))
            return ResponseEntity.ok(fixSessionDescriptionService.connectSession(sessionUUID));
        if (Objects.equals(action, "disconnect"))
            return ResponseEntity.ok(fixSessionDescriptionService.disconnectSession(sessionUUID));
        return new ResponseEntity<>(HttpStatusCode.valueOf(404));
    }

    @PostMapping(path = "/{sessionUUID}/subscription")
    public ResponseEntity<String> createSubscriptionToken() {

    }

    @GetMapping(path = "/{sessionUUID}/{subscriptionUUID}/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamFlux(@PathVariable String sessionUUID) {
        return Flux.create(sink ->
            {
                var uuid = fixSessionDescriptionService.subscribe(sessionUUID, sink);
                sink.onDispose(() -> fixSessionDescriptionService.unsubscribe(sessionUUID, uuid));
            }
        );
    }
}
