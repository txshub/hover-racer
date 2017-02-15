package audioEngine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;

/**
 * @author Tudor Suruceanu
 * JUnit tests for the subsystem
 */
public class JUTests {

	Source source;
	
	@Before
	public void init() {
		AudioMaster.init();
		source = AudioMaster.createMusicSource();
	}
	
	@Test
	public void test1() {
		assertFalse(source.isPlaying());
	}
	
	@Test
	public void test2() {
		source.play(Sounds.MUSIC_1);
		assertTrue(source.isPlaying());
		source.stop();
	}
	
	@After
	public void cleanUP() {
		AudioMaster.cleanUP();
	}

}
