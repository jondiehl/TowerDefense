package Tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.newdawn.slick.SlickException;

import edu.Moravian.Entity.Agent;

public class AgentTest {

	@Test
	public void test() throws SlickException {
		Agent agent = new Agent(200);
		
		agent.update(4);
	}

}
