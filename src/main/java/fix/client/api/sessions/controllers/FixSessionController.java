package fix.client.api.sessions.controllers;

import fix.client.api.sessions.models.FixConnectionProperties;
import fix.client.api.sessions.models.FixSessionCreateRequest;
import lombok.extern.slf4j.Slf4j;

import fix.client.api.sessions.models.FixSessionProperties;
import fix.client.api.repositories.FixSessionMapRepository;
import fix.client.api.sessions.services.FixConnectionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/sessions")
public class FixSessionController {

    private final FixConnectionService fixConnectionService;
    private final FixSessionMapRepository fixSessionMapRepository;

    @Autowired
    public FixSessionController(
            FixConnectionService fixConnectionService,
            FixSessionMapRepository fixSessionMapRepository

    ) {
        this.fixConnectionService = fixConnectionService;
        this.fixSessionMapRepository = fixSessionMapRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody FixSessionCreateRequest request) {
        return ResponseEntity.ok(fixConnectionService.create(
                new FixSessionProperties(
                        request.name(),
                        new FixConnectionProperties(
                                request.senderCompID(),
                                request.targetCompID(),
                                request.host(),
                                request.port()
                        )
                )
        ));
    }

    @GetMapping("/select")
    public ResponseEntity<?> select() {
        return ResponseEntity.ok(fixSessionMapRepository.select());
    }

    @GetMapping("/select/{id}")
    public ResponseEntity<?> select(@PathVariable String id) {
        return ResponseEntity.ok(fixSessionMapRepository.select(id));
    }

    @GetMapping("/connect/{id}")
    public ResponseEntity<?> connect(@PathVariable String id) {
        fixConnectionService.connect(id);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/disconnect/{id}")
    public ResponseEntity<?> disconnect(@PathVariable String id) {
        fixConnectionService.disconnect(id);
        return ResponseEntity.ok("ok");
    }
}
