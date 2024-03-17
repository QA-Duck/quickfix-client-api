package fix.client.api.controllers;

import fix.client.api.impl.FixSender;
import fix.client.api.models.CreateFixSessionRequest;
import fix.client.api.models.FixConnectionStatus;
import fix.client.api.models.FixSessionInfo;
import fix.client.api.services.FixSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/sessions")
public class SessionManagerController {

    private final FixSessionService fixSessionService;

    @Autowired
    public SessionManagerController(FixSessionService fixSessionService) {
        this.fixSessionService = fixSessionService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSession(@RequestBody CreateFixSessionRequest createFixSessionRequest) {
        var sessionInfo = fixSessionService.createSession(createFixSessionRequest);
        return ResponseEntity.ok(sessionInfo);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatusList() {
        return ResponseEntity.ok(fixSessionService.selectSession());
    }

    @GetMapping(path = "/{session_uuid}/status")
    public ResponseEntity<?> getStatusById(@PathVariable String sessionUUID) {
        return ResponseEntity.ok(fixSessionService.selectSession(sessionUUID));
    }

    @PostMapping(path = "/{sessionUUID}/action")
    public ResponseEntity<FixSessionInfo> connect(
            @PathVariable String sessionUUID,
            @RequestBody Map<String, String> body
    ) {
        var action = body.get("action");
        if (Objects.equals(action, "connect"))
            return ResponseEntity.ok(fixSessionService.connectSession(sessionUUID));
        if (Objects.equals(action, "disconnect"))
            return ResponseEntity.ok(fixSessionService.disconnectSession(sessionUUID));
        return new ResponseEntity<>(HttpStatusCode.valueOf(404));
    }

    @GetMapping(path = "/{sessionUUID}/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamFlux(@PathVariable String sessionUUID) {
        return Flux.from(fixSessionService.getFlux(sessionUUID).get()).publish();
    }
}
