package audioEngine.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import audioEngine.AudioMaster;
import audioEngine.Sounds;
import audioEngine.Source;

/**
 * JUnit tests for the subsystem
 * 
 * @author Tudor Suruceanu
 */
public class AudioEngineTests {

  private Source source1, source2;

  @Before
  public void init() {
    AudioMaster.init();
    source1 = AudioMaster.createMusicSource();
    source2 = AudioMaster.createSFXSource();
  }

  @Test
  public void test1m() {
    assertFalse(source1.isPlaying());
  }

  @Test
  public void test2m() {
    source1.play(Sounds.MUSIC_1);
    assertTrue(source1.isPlaying());
    source1.stop();
  }

  @Test
  public void test3m() {
    source1.setCurrentVolume(0f);
    AudioMaster.increaseMusicVolume();
    assert (source1.getCurrentVolume() == source1.getInitialVolume() * AudioMaster.getMusicVolume());
  }

  @Test
  public void test4m() {
    source1.setCurrentVolume(0f);
    AudioMaster.decreaseMusicVolume();
    assert (source1.getCurrentVolume() == source1.getInitialVolume() * AudioMaster.getMusicVolume());
  }

  @Test
  public void test5m() {
    source1.setInitialVolume(0.5f);
    AudioMaster.setMusicVolume(0.2f);
    assert (source1.getCurrentVolume() == 0.1f);
  }

  @Test
  public void test1sfx() {
    source2.setCurrentVolume(0f);
    AudioMaster.increaseSFXVolume();
    assert (source2.getCurrentVolume() == source2.getInitialVolume() * AudioMaster.getSFXVolume());
  }

  @Test
  public void test2sfx() {
    source2.setCurrentVolume(0f);
    AudioMaster.decreaseSFXVolume();
    assert (source2.getCurrentVolume() == source2.getInitialVolume() * AudioMaster.getSFXVolume());
  }

  @Test
  public void test3sfx() {
    source2.setInitialVolume(0.5f);
    AudioMaster.setSFXVolume(0.2f);
    assert (source2.getCurrentVolume() == 0.1f);
  }

  @After
  public void cleanUp() {
    AudioMaster.cleanUp();
  }

}
