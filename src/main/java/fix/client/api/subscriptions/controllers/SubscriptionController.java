package fix.client.api.subscriptions.controllers;

import fix.client.api.repositories.FixSessionMapRepository;
import fix.client.api.repositories.FixSubscriberMapRepository;
import fix.client.api.subscriptions.services.FixSubscriberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


@Slf4j
@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final FixSubscriberService fixSubscriberService;
    private final FixSessionMapRepository fixSessionMapRepository;
    private final FixSubscriberMapRepository fixSubscriberMapRepository;

    @Autowired
    public SubscriptionController(
            FixSubscriberService fixSubscriberService,
            FixSessionMapRepository fixSessionMapRepository,
            FixSubscriberMapRepository fixSubscriberMapRepository
    ) {
        this.fixSubscriberService = fixSubscriberService;
        this.fixSessionMapRepository = fixSessionMapRepository;
        this.fixSubscriberMapRepository = fixSubscriberMapRepository;
    }

    @PostMapping("/create/{sessionID}")
    public ResponseEntity<?> create(@PathVariable String sessionID) {
        return ResponseEntity.ok(fixSubscriberService.create(sessionID));
    }

    @GetMapping("/select/{subscriberID}")
    public ResponseEntity<?> select(@PathVariable String subscriberID) {
        return ResponseEntity.ok(fixSubscriberMapRepository.select(subscriberID));
    }

    @GetMapping("/select/session/{sessionID}")
    public ResponseEntity<?> selectBySessionID(@PathVariable String sessionID) {
        return ResponseEntity.ok(fixSubscriberMapRepository.selectBySessionID(sessionID));
    }

    @GetMapping(path = "/subscribe/{subscriberID}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> subscribe(@PathVariable String subscriberID) {
         return Flux.create(sink ->
                {
                    fixSubscriberService.subscribe(subscriberID, sink);
                    sink.onDispose(() -> fixSubscriberService.unsubscribe(subscriberID));
                }
        );
    }
}
