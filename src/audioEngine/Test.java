package audioEngine;

import java.util.Scanner;


/*
 * Tudor Suruceanu
 * 
 * Usage example
 */
public class Test {

	public static void main(String[] args) throws InterruptedException {
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		
		Scanner in = new Scanner(System.in);
		String c = "";
		
		AudioMaster.playMusic();
		
		while (!c.equals("stop")) {
			c = in.nextLine();
			
			if (c.equals("up")) {
				AudioMaster.increaseMasterVolume();
			}
			
			if (c.equals("down")) {
				AudioMaster.decreaseMasterVolume();
			}
		}
		
		AudioMaster.stopMusic();
		
		AudioMaster.cleanUP();
	}

}
