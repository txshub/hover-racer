package serverComms.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestByteArrayByte.class, TestClientTable.class, TestCommQueue.class, TestDetectTimeout.class,
		TestGameNameNumber.class, TestGameSettings.class, TestLobby.class, TestServerComm.class})
public class ServerTests {
	
}
