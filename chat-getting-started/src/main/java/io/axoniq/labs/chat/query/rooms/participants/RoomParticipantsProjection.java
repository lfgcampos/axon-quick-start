package io.axoniq.labs.chat.query.rooms.participants;

import org.axonframework.eventsourcing.EventSourcingHandler;
import org.springframework.stereotype.Component;
import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent;

@Component
public class RoomParticipantsProjection {

  private final RoomParticipantsRepository repository;

  public RoomParticipantsProjection(RoomParticipantsRepository repository) {
    this.repository = repository;
  }

  @EventSourcingHandler
  public void on(ParticipantJoinedRoomEvent event) {
    repository.save(new RoomParticipant(event.getRoomId(), event.getParticipant()));
  }

  @EventSourcingHandler
  public void on(ParticipantLeftRoomEvent event) {
    repository.deleteByParticipantAndRoomId(event.getParticipant(), event.getRoomId());
  }

  // TODO: Create the query handler to read data from this model
}
