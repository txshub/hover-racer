package audioEngine;

import java.util.Scanner;

/**
 * Usage example
 * @author Tudor Suruceanu 
 */
public class Test {

  public static void main(String[] args) throws InterruptedException {
    AudioMaster.init();

    Scanner in = new Scanner(System.in);
    String c = "";

    Source s = new Source();
    s.setLooping(true);
    s.play(Sounds.BUTTON_CLICK);
    
    float xPos = 8;
    s.setPosition(xPos, 0, 0);
    
    while (c != "q") {
    	xPos -= 0.03f;
    	s.setPosition(xPos, 0, 0);
    	Thread.sleep(3);
    }
    
    s.delete();
    
    // float pitch = 1;
    //
    //
    // Source engine = AudioMaster.createSFXSource();
    // engine.play(Sounds.ENGINE);
    //
    // int x = 0;
    //
    // Thread.sleep(3000);
    //
    // while (x < 500) {
    //
    // if (pitch < 2) {
    // pitch += 0.01f;
    // engine.setPitch(pitch);
    // }
    //
    // x++;
    // Thread.sleep(10);
    //
    // }
    //
    // while (pitch > 1) {
    //
    // pitch -= 0.01f;
    // engine.setPitch(pitch);
    // Thread.sleep(10);
    //
    // }
    //
    // Thread.sleep(3000);
    //
    // while (pitch > 0) {
    //
    // pitch -= 0.01f;
    // engine.setPitch(pitch);
    // Thread.sleep(10);
    //
    // }

    // AudioMaster.playMusic();
    //
    // while (!c.equals("stop")) {
    //
    // if (c.equals("skip")) {
    // AudioMaster.skipMusic();
    // }
    //
    // c = in.nextLine();
    //
    // }
    //
    // AudioMaster.stopMusic();
    

    AudioMaster.cleanUp();
  }

}
