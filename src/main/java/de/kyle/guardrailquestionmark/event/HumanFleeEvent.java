package de.kyle.guardrailquestionmark.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class HumanFleeEvent extends ApplicationEvent {
    private final String reason;

    public HumanFleeEvent(Object source, String reason) {
        super(source);
        this.reason = reason;
    }
}
