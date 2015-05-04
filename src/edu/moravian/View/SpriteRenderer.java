package edu.Moravian.View;

import org.newdawn.slick.SlickException;

import edu.Moravian.Entity.Agent;
import edu.Moravian.Entity.Entity;
import edu.Moravian.Entity.Bullet;
import edu.Moravian.Entity.Tower;

public class SpriteRenderer {
	
	public SpriteRenderer() throws SlickException {
		
	}
	
	public void render(Entity e) {
		if (e instanceof Tower) {
			renderTower((Tower)e);
		}
		else if (e instanceof Agent) {
			renderAgent((Agent)e);
		}
		else if (e instanceof Bullet) {
			renderBullet((Bullet)e);
		}
		else {
			throw new RuntimeException("Could not render");
		}	
	}

	public void renderTower(Tower t) {
		
	}
	
	public void renderAgent(Agent a) {
		
	}
	
	public void renderBullet(Bullet b) {
		
	}
	

}
