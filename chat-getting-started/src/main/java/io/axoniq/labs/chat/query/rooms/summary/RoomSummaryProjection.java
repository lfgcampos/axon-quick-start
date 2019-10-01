package io.axoniq.labs.chat.query.rooms.summary;

import org.axonframework.eventsourcing.EventSourcingHandler;
import org.springframework.stereotype.Component;
import io.axoniq.labs.chat.coreapi.RoomCreatedEvent;

@Component
public class RoomSummaryProjection {

  private final RoomSummaryRepository roomSummaryRepository;

  public RoomSummaryProjection(RoomSummaryRepository roomSummaryRepository) {
    this.roomSummaryRepository = roomSummaryRepository;
  }

  @EventSourcingHandler
  public void on(RoomCreatedEvent event) {
    this.roomSummaryRepository.save(new RoomSummary(event.getRoomId(), event.getName()));
  }
  // TODO: Create the query handler to read data from this model

}
