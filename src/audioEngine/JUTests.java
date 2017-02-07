package audioEngine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JUTests {

	Source source;
	
	@Before
	public void init() {
		AudioMaster.init();
		source = new Source();
	}
	
	@Test
	public void test1() {
		assertFalse(source.isPlaying());
	}
	
	@Test
	public void test2() {
		source.play(Sounds.MUSIC);
		assertTrue(source.isPlaying());
		source.stop();
	}
	
	@After
	public void cleanUP() {
		AudioMaster.cleanUP();
	}

}
