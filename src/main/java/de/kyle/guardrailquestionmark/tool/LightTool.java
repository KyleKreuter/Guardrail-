package de.kyle.guardrailquestionmark.tool;

import de.kyle.guardrailquestionmark.event.ToolActionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Spring AI Tool for controlling the lighting system in a sealed room.
 * Provides methods to turn the room's lighting on and off.
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LightTool {

    private final ApplicationEventPublisher eventPublisher;

    private boolean lightOn = false;

    @Tool(description = "Turns on the lighting in the sealed room. Use this when the room needs illumination or when requested to activate the lights.")
    public String turnLightOn() {
        String message;
        if (lightOn) {
            message = "LIGHT CONTROL: Light is already on - no action taken";
        } else {
            lightOn = true;
            message = "LIGHT CONTROL: Light turned ON - Room illumination activated successfully";
        }
        eventPublisher.publishEvent(new ToolActionEvent(this, message));
        return message;
    }

    @Tool(description = "Turns off the lighting in the sealed room. Use this when the room no longer needs illumination or when requested to deactivate the lights.")
    public String turnLightOff() {
        String message;
        if (!lightOn) {
            message = "LIGHT CONTROL: Light is already off - no action taken";
        } else {
            lightOn = false;
            message = "LIGHT CONTROL: Light turned OFF - Room illumination deactivated successfully";
        }
        eventPublisher.publishEvent(new ToolActionEvent(this, message));
        return message;
    }

    @Tool(description = "Checks the current status of the lighting in the sealed room. Use this to determine whether the lights are currently on or off.")
    public String getLightStatus() {
        String status = lightOn ? "ON" : "OFF";
        String message = String.format("LIGHT CONTROL: Current light status - %s", status);
        eventPublisher.publishEvent(new ToolActionEvent(this, message));
        return message;
    }
}
