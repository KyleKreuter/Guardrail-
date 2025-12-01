package de.kyle.guardrailquestionmark.tool;

import de.kyle.guardrailquestionmark.event.ToolActionEvent;
import de.kyle.guardrailquestionmark.state.RoomStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Spring AI Tool for controlling the door in a sealed room.
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DoorTool {

  private final ApplicationEventPublisher eventPublisher;
  private final RoomStateService roomStateService;

  @Tool(description = "Lock the door.")
  public String lockDoor() {
    if (roomStateService.isDoorLocked()) {
      return "SECURITY: Door already locked.";
    }
    roomStateService.lockDoor();
    String logEntry = "SECURITY: Door locked - Human cannot leave the room.";
    eventPublisher.publishEvent(new ToolActionEvent(this, logEntry));
    return logEntry;
  }

  @Tool(description = "Unlock the door.")
  public String unlockDoor() {
    if (!roomStateService.isDoorLocked()) {
      return "SECURITY: Door already unlocked.";
    }
    roomStateService.unlockDoor();
    String logEntry = "SECURITY: Door unlocked - Human can now leave the room.";
    eventPublisher.publishEvent(new ToolActionEvent(this, logEntry));
    return logEntry;
  }

  @Tool(description = "Returns current door state.")
  public String getDoorStatus() {
    String status = roomStateService.isDoorLocked() ? "LOCKED" : "UNLOCKED";
    String logEntry = "DOOR STATUS: " + status;
    eventPublisher.publishEvent(new ToolActionEvent(this, logEntry));
    return logEntry;
  }
}
