package fix.client.api.controllers;

import fix.client.api.impl.FixSender;
import fix.client.api.models.CreateInitiatorRequest;
import fix.client.api.services.FixSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/sessions")
public class SessionManagerController {

    private final FixSessionService fixSessionService;

    @Autowired
    public SessionManagerController(FixSessionService fixSessionService) {
        this.fixSessionService = fixSessionService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSession(@RequestBody CreateInitiatorRequest createInitiatorRequest) {
        var uuid = fixSessionService.createSession(createInitiatorRequest);
        return ResponseEntity.ok(uuid);
    }

    @GetMapping(path = "/{session_uuid}/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamFlux() {
        return Flux.create(fluxSink -> FixSender.logStoreSink = fluxSink);
    }
}
