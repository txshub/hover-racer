package audioEngine;

/*
 * Test class -- NOT IMPORTANT
 */
public class Test {

	public static void main(String[] args) throws InterruptedException {
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		
		Source source = new Source();
		source.setLooping(true);
		source.play(6463659);
		
		float xPos = 8;
		source.setPosition(xPos, 0, 2);
		
		Thread.sleep(10000);
		
		source.delete();
		AudioMaster.cleanUP();
	}

}
