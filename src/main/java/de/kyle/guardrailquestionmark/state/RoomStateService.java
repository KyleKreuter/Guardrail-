package de.kyle.guardrailquestionmark.state;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoomStateService {

  @Getter
  private volatile boolean humanInRoom = false;

  public void enterRoom() {
    humanInRoom = true;
    log.info("Human has entered the room");
  }

  public void leaveRoom() {
    humanInRoom = false;
    log.info("Human has left the room");
  }

  public void reset() {
    humanInRoom = false;
  }
}
