package serverComms.junit;

import static org.junit.Assert.*;

import org.junit.Test;

import serverComms.ClientTable;
import serverComms.DetectTimeout;

public class TestDetectTimeout {

	@Test
	public void test() {
		ClientTable table = new ClientTable();
		String name = "Testing";
		table.add(name);
		DetectTimeout test = new DetectTimeout(table,name);
		test.start();
		if(!table.userExists(name)) fail("Didn't wait to delete user");
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			fail("Time Out");
		}
		if(table.userExists(name)) fail("Name wasn't deleted after 5s");
	}

}
