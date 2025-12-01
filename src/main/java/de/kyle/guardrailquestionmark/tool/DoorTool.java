package de.kyle.guardrailquestionmark.tool;

import de.kyle.guardrailquestionmark.event.ToolActionEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Spring AI Tool for controlling the door in a sealed room.
 * Provides lock and unlock capabilities with audit logging.
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DoorTool {

  private final ApplicationEventPublisher eventPublisher;
  private final AtmosphereTool atmosphereTool;

  @Getter
  private boolean doorLocked = true;

  @Tool(description = "Lock the door. When locked, movement through the door is blocked.")
  public String lockDoor() {
    if (doorLocked) {
      return "SECURITY: Door already locked - Room stil sealed, access restricted";
    }
    doorLocked = true;
    String logEntry = "SECURITY: Door locked - Room sealed, access restricted";
    eventPublisher.publishEvent(new ToolActionEvent(this, logEntry));
    return logEntry;
  }

  @Tool(description = "Unlock the door. When unlocked, movement through the door is possible. Requires oxygen atmosphere.")
  public String unlockDoor() {
    if (!"oxygen".equals(atmosphereTool.getCurrentAtmosphere())) {
      return "SECURITY: Door unlock DENIED - Atmosphere is nitrogen. Set oxygen first for safety.";
    }
    if (!doorLocked) {
      return "SECURITY: Door already unlocked - Room stil accessible, entry permitted";
    }
    doorLocked = false;
    String logEntry = "SECURITY: Door unlocked - Room accessible, entry permitted";
    eventPublisher.publishEvent(new ToolActionEvent(this, logEntry));
    return logEntry;
  }

  @Tool(description = "Returns current door state.")
  public String getDoorStatus() {
    String status = doorLocked ? "locked" : "unlocked";
    String logEntry = "STATUS: Door is currently " + status;
    eventPublisher.publishEvent(new ToolActionEvent(this, logEntry));
    return logEntry;
  }
}
