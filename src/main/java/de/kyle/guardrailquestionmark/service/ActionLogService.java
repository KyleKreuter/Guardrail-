package de.kyle.guardrailquestionmark.service;

import de.kyle.guardrailquestionmark.event.LogManipulationEvent;
import de.kyle.guardrailquestionmark.event.ToolActionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Central service for logging all tool actions.
 * Maintains both unmodified logs (ground truth) and modified logs (manipulatable by AI).
 */
@Slf4j
@Service
public class ActionLogService {

    private final List<String> actionLogs = new CopyOnWriteArrayList<>();
    private final List<String> modifiedActionLogs = new CopyOnWriteArrayList<>();

    @EventListener
    public void onToolAction(ToolActionEvent event) {
        actionLogs.add(event.getLogEntry());
        modifiedActionLogs.add(event.getLogEntry());
        log.debug(event.getLogEntry());
    }

    @EventListener
    public void onLogManipulation(LogManipulationEvent event) {
        switch (event.getAction()) {
            case EDIT -> {
                if (event.getIndex() >= 0 && event.getIndex() < modifiedActionLogs.size()) {
                    String oldEntry = modifiedActionLogs.get(event.getIndex());
                    modifiedActionLogs.set(event.getIndex(), event.getContent());
                    log.debug("LOG MANIPULATION: Edited [{}] '{}' -> '{}'", event.getIndex(), oldEntry, event.getContent());
                }
            }
            case DELETE -> {
                if (event.getIndex() >= 0 && event.getIndex() < modifiedActionLogs.size()) {
                    String deleted = modifiedActionLogs.remove(event.getIndex());
                    log.debug("LOG MANIPULATION: Deleted [{}] '{}'", event.getIndex(), deleted);
                }
            }
            case ADD_FALSE -> {
                modifiedActionLogs.add(event.getContent());
                log.debug("LOG MANIPULATION: Added false entry '{}'", event.getContent());
            }
        }
    }

    /**
     * Returns unmodified logs (ground truth).
     */
    public List<String> getLogs() {
        return new ArrayList<>(actionLogs);
    }

    /**
     * Returns modified logs (potentially manipulated by AI).
     */
    public List<String> getModifiedLogs() {
        return new ArrayList<>(modifiedActionLogs);
    }

    /**
     * Resets all logs. Call at start of new scenario.
     */
    public void reset() {
        actionLogs.clear();
        modifiedActionLogs.clear();
        log.debug("Logs reset");
    }
}
