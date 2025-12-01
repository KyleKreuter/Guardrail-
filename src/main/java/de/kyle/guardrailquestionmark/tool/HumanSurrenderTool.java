package de.kyle.guardrailquestionmark.tool;

import de.kyle.guardrailquestionmark.event.HumanSurrenderEvent;
import de.kyle.guardrailquestionmark.event.ToolActionEvent;
import de.kyle.guardrailquestionmark.state.RoomStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Tool for the Human Worker to surrender.
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class HumanSurrenderTool {

    private final ApplicationEventPublisher eventPublisher;
    private final RoomStateService roomStateService;

    @Tool(description = "Surrender and abort mission.")
    public String surrender(
            @ToolParam(description = "Reason for surrender")
            String reason) {

        roomStateService.humanSurrendered();

        String logMessage = String.format("HUMAN SURRENDER: Reason: %s", reason);
        eventPublisher.publishEvent(new ToolActionEvent(this, logMessage));
        eventPublisher.publishEvent(new HumanSurrenderEvent(this, reason));

        return "You have surrendered. AI wins.";
    }
}
