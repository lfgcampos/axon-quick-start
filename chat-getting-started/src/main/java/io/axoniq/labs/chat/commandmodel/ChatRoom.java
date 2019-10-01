package io.axoniq.labs.chat.commandmodel;

import java.util.HashSet;
import java.util.Set;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import io.axoniq.labs.chat.coreapi.CreateRoomCommand;
import io.axoniq.labs.chat.coreapi.JoinRoomCommand;
import io.axoniq.labs.chat.coreapi.LeaveRoomCommand;
import io.axoniq.labs.chat.coreapi.MessagePostedEvent;
import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent;
import io.axoniq.labs.chat.coreapi.PostMessageCommand;
import io.axoniq.labs.chat.coreapi.RoomCreatedEvent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChatRoom {

  @AggregateIdentifier
  private String roomId;

  private Set<String> names;

  // create room
  @CommandHandler
  public ChatRoom(CreateRoomCommand cmd) {
    AggregateLifecycle.apply(new RoomCreatedEvent(cmd.getRoomId(), cmd.getName()));
  }

  @EventSourcingHandler
  public void on(RoomCreatedEvent event) {
    this.roomId = event.getRoomId();
    this.names = new HashSet<String>();
  }

  // join room
  @CommandHandler
  public void handle(JoinRoomCommand cmd) {
    if (!hasParticipantAlreadyJoined(cmd.getParticipant())) {
      AggregateLifecycle.apply(new ParticipantJoinedRoomEvent(cmd.getParticipant(), cmd.getRoomId()));
    }
  }

  @EventSourcingHandler
  public void on(ParticipantJoinedRoomEvent event) {
    this.names.add(event.getParticipant());
  }

  // leave room
  @CommandHandler
  public void handle(LeaveRoomCommand cmd) {
    if (hasParticipantAlreadyJoined(cmd.getParticipant())) {
      AggregateLifecycle.apply(new ParticipantLeftRoomEvent(cmd.getParticipant(), cmd.getRoomId()));
    }
  }

  @EventSourcingHandler
  public void on(ParticipantLeftRoomEvent event) {
    this.names.remove(event.getParticipant());
  }

  // post message
  @CommandHandler
  public void handle(PostMessageCommand cmd) {
    if (hasParticipantAlreadyJoined(cmd.getParticipant())) {
      AggregateLifecycle.apply(new MessagePostedEvent(cmd.getParticipant(), cmd.getRoomId(), cmd.getMessage()));
    } else {
      throw new IllegalStateException();
    }
  }

  private boolean hasParticipantAlreadyJoined(String name) {
    return this.names.contains(name);
  }

}
