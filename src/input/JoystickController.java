package input;

import java.util.HashMap;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

public class JoystickController extends InputController {

  private Controller controller;
  protected HashMap<Integer, Action> axisMapping;

  private boolean thrust, strafe, turn;

  public JoystickController() {
    super();
    axisMapping = new HashMap<>();
    thrust = strafe = turn = false;

    try {
      Controllers.create();
      Controllers.poll();
      for (int i = 0; i < Controllers.getControllerCount(); i++) {
        Controller controler = Controllers.getController(i);
        System.out.println(controler.getName());
        if (controler.getName().equals("T.Flight Hotas X")) {
          controller = controler;
          break;
        }
      }

      for (int i = 0; i < controller.getAxisCount(); i++) {
        System.out.println(i + ": " + controller.getAxisName(i));
      }

      for (int i = 0; i < controller.getButtonCount(); i++) {
        System.out.println(i + ": " + controller.getButtonName(i));
      }

    } catch (LWJGLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.err.println("It broke.");
      return;
    }
    init();
  }

  private void init() {

    axisMapping.put(4, Action.FORWARD);
    axisMapping.put(0, Action.TURN_RIGHT);
    axisMapping.put(2, Action.STRAFE_RIGHT);
    mapping.put(1, Action.JUMP);
    mapping.put(9, Action.BREAK);

    for (Action action : Action.values()) {
      keyStatus.put(action, 0f);
      System.out.println(keyStatus.get(action));
    }

    Controllers.clearEvents();

  }

  public float wasPressed(Action action) {

    return 0f;

  }

  public float isDown(Action action) {

    return keyStatus.getOrDefault(action, 0f);

  }

  @Override
  public void update() {

    prevStatus.putAll(keyStatus);
    Controllers.poll();
    controller.poll();
    if (!thrust || !strafe || !turn) {
      if (Controllers.next()) {
        if (Controllers.isEventAxis()) {
          if (Controllers.getEventControlIndex() == 4) {
            thrust = true;
          } else if (Controllers.getEventControlIndex() == 0) {
            turn = true;
          } else if (Controllers.getEventControlIndex() == 2) {
            strafe = true;
          }
        }
      }
    }
    for (int i = 0; i < controller.getAxisCount(); i++) {
      if (axisMapping.get(i) != null) {
        float value = controller.getAxisValue(i);
        System.out.print(value + ", ");
        switch (axisMapping.get(i)) {
          case FORWARD:
            if (thrust) {
              if (value < 0f) {
                keyStatus.put(Action.FORWARD, -value);
                keyStatus.put(Action.BACKWARD, 0f);
              } else {
                keyStatus.put(Action.FORWARD, 0f);
                keyStatus.put(Action.BACKWARD, value);
              }
            }
            break;
          case STRAFE_RIGHT:
            if (strafe) {
              if (value > 0f) {
                keyStatus.put(Action.STRAFE_RIGHT, value);
                keyStatus.put(Action.STRAFE_LEFT, 0f);
              } else {
                keyStatus.put(Action.STRAFE_RIGHT, 0f);
                keyStatus.put(Action.STRAFE_LEFT, -value);

              }
            }
            break;
          case TURN_RIGHT:
            if (turn) {
              if (value > 0f) {
                keyStatus.put(Action.TURN_RIGHT, value);
                keyStatus.put(Action.TURN_LEFT, 0f);
              } else {
                keyStatus.put(Action.TURN_RIGHT, 0f);
                keyStatus.put(Action.TURN_LEFT, -value);
              }
            }
            break;
          default:
            keyStatus.put(axisMapping.get(i), value);
            break;
        }
      }
    }

    for (int i = 0; i < controller.getButtonCount(); i++) {
      if (mapping.get(i) != null) {
        System.out.print(controller.isButtonPressed(i) + ", ");
        keyStatus.put(mapping.get(i), controller.isButtonPressed(i) ? 1f : 0f);
      }
    }
    System.out.println();

  }

}
