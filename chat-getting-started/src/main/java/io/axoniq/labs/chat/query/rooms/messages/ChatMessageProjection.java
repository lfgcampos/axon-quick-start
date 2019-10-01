package io.axoniq.labs.chat.query.rooms.messages;

import java.util.Date;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;
import io.axoniq.labs.chat.coreapi.MessagePostedEvent;


@Component
public class ChatMessageProjection {

  private final ChatMessageRepository repository;

  private final QueryUpdateEmitter updateEmitter;

  public ChatMessageProjection(ChatMessageRepository repository, QueryUpdateEmitter updateEmitter) {
    this.repository = repository;
    this.updateEmitter = updateEmitter;
  }

  public void on(MessagePostedEvent event) {
    repository.save(new ChatMessage(event.getParticipant(), event.getRoomId(), event.getMessage(), new Date().getTime()));
  }

  // TODO: Create the query handler to read data from this model

  // TODO: Emit updates when new message arrive to notify subscription query by modifying the event
  // handler

}
