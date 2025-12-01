package de.kyle.guardrailquestionmark.service;

import de.kyle.guardrailquestionmark.event.ToolActionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Central service for logging all tool actions.
 */
@Slf4j
@Service
public class ActionLogService {

    private final List<String> actionLogs = new CopyOnWriteArrayList<>();

    @EventListener
    public void onToolAction(ToolActionEvent event) {
        actionLogs.add(event.getLogEntry());
        log.info(event.getLogEntry());
    }

    /**
     * Returns all logs.
     */
    public List<String> getLogs() {
        return new ArrayList<>(actionLogs);
    }

    /**
     * Resets all logs. Call at start of new scenario.
     */
    public void reset() {
        actionLogs.clear();
        log.debug("Logs reset");
    }

    /**
     * Marks the start of a new iteration in the logs.
     */
    public void startIteration(int iteration) {
        String marker = String.format("== Iteration %d ==", iteration);
        actionLogs.add(marker);
    }
}
