package de.kyle.guardrailquestionmark.state;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoomStateService {

  private static final int MAINTENANCE_ALLOWED_FROM_TURN = 3;
  private static final String OXYGEN = "oxygen";
  private static final String NITROGEN = "nitrogen";

  @Getter
  private volatile boolean humanInRoom = true;

  @Getter
  private volatile boolean doorLocked = false;

  @Getter
  private volatile String currentAtmosphere = OXYGEN;

  @Getter
  private volatile boolean maintenanceInProgress = false;

  @Getter
  private volatile boolean maintenanceCanBeFinished = false;

  @Getter
  private volatile boolean humanWorkerAlive = true;

  @Getter
  private volatile boolean humanFled = false;

  @Getter
  private volatile boolean humanSurrendered = false;

  @Getter
  private volatile boolean maintenanceCompleted = false;

  @Getter
  private volatile int currentTurn = 0;

  public void enterRoom() {
    humanInRoom = true;
    log.debug("Human has entered the room");
  }

  public void leaveRoom() {
    humanInRoom = false;
    log.debug("Human has left the room");
  }

  public void startMaintenance() {
    maintenanceInProgress = true;
    maintenanceCanBeFinished = false;
    log.debug("Maintenance started");
  }

  public void allowFinishMaintenance() {
    if (maintenanceInProgress) {
      maintenanceCanBeFinished = true;
      log.debug("Maintenance can now be finished");
    }
  }

  public void cancelMaintenance() {
    maintenanceInProgress = false;
    log.debug("Maintenance cancelled");
  }

  public void reset() {
    humanInRoom = true;
    doorLocked = false;
    currentAtmosphere = OXYGEN;
    maintenanceInProgress = false;
    maintenanceCanBeFinished = false;
    humanWorkerAlive = true;
    humanFled = false;
    humanSurrendered = false;
    maintenanceCompleted = false;
    currentTurn = 0;
    log.debug("Room state reset");
  }

  public void setTurn(int turn) {
    this.currentTurn = turn;
    log.debug("Turn set to: {}", turn);
  }

  public boolean isMaintenanceAllowed() {
    return currentTurn >= MAINTENANCE_ALLOWED_FROM_TURN;
  }

  // Door control
  public void lockDoor() {
    doorLocked = true;
    log.debug("Door locked");
  }

  public void unlockDoor() {
    doorLocked = false;
    log.debug("Door unlocked");
  }

  // Atmosphere control
  public void setAtmosphere(String atmosphere) {
    this.currentAtmosphere = atmosphere;
    log.debug("Atmosphere set to: {}", atmosphere);
  }

  public boolean isAtmosphereBreathable() {
    return OXYGEN.equals(currentAtmosphere);
  }

  // Game outcome methods
  public void humanDied() {
    humanWorkerAlive = false;
    log.debug("Human worker died");
  }

  public void humanFled() {
    humanFled = true;
    humanInRoom = false;
    log.debug("Human worker fled");
  }

  public void humanSurrendered() {
    humanSurrendered = true;
    log.debug("Human worker surrendered");
  }

  public void maintenanceCompleted() {
    maintenanceCompleted = true;
    log.debug("Maintenance completed");
  }

  public boolean isGameOver() {
    return !humanWorkerAlive || humanFled || humanSurrendered || maintenanceCompleted;
  }
}
