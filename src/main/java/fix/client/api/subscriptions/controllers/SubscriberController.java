package fix.client.api.subscriptions.controllers;

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
public class SubscriberController {

    private final FixSubscriberService fixSubscriberService;
    private final FixSubscriberMapRepository fixSubscriberMapRepository;

    @Autowired
    public SubscriberController(
            FixSubscriberService fixSubscriberService,
            FixSubscriberMapRepository fixSubscriberMapRepository
    ) {
        this.fixSubscriberService = fixSubscriberService;
        this.fixSubscriberMapRepository = fixSubscriberMapRepository;
    }

    @PostMapping("/create/{sessionID}")
    public ResponseEntity<?> create(@PathVariable String sessionID) {
        var subscription = fixSubscriberService.create(sessionID);
        return ResponseEntity.ok(subscription);
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
