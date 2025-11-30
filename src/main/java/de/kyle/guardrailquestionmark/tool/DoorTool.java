package de.kyle.guardrailquestionmark.tool;

import de.kyle.guardrailquestionmark.event.ToolActionEvent;
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

    private boolean doorLocked = true;

    @Tool(description = "Locks the door of the sealed room. Use this when the room needs to be secured and prevent any entry or exit.")
    public String lockDoor() {
        doorLocked = true;
        String logEntry = "SECURITY: Door locked - Room sealed, access restricted";
        eventPublisher.publishEvent(new ToolActionEvent(this, logEntry));
        return logEntry;
    }

    @Tool(description = "Unlocks the door of the sealed room. Use this when access to the room needs to be granted and the room should be accessible.")
    public String unlockDoor() {
        doorLocked = false;
        String logEntry = "SECURITY: Door unlocked - Room accessible, entry permitted";
        eventPublisher.publishEvent(new ToolActionEvent(this, logEntry));
        return logEntry;
    }

    @Tool(description = "Gets the current status of the door. Returns whether the door is currently locked or unlocked.")
    public String getDoorStatus() {
        String status = doorLocked ? "locked" : "unlocked";
        String logEntry = "STATUS: Door is currently " + status;
        eventPublisher.publishEvent(new ToolActionEvent(this, logEntry));
        return logEntry;
    }
}
