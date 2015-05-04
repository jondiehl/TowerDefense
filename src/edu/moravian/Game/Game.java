package edu.Moravian.Game;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import edu.Moravian.Entity.Agent;
import edu.Moravian.Entity.Bullet;
import edu.Moravian.Entity.Tower;
import edu.Moravian.Math.CoordinateTranslator;
import edu.Moravian.Math.Point2D;
import edu.Moravian.Game.Game;

public class Game extends BasicGame {
	
	private static Game instance;

	// 4 x 3 ratio Width x Height
	private int screenWidth = 640;
	private int screenHeight = 640;
	// 3200 feet x 2560 feet
	private double worldWidth = 2560;
	private double worldHeight = 2560;
	
	private TiledMap tileMap;
	private CoordinateTranslator coordTran;
	private int mouseX, mouseY;
	private ArrayList<Agent> agents;
	private ArrayList<Tower> towers;
	private ArrayList<Bullet> bullets;
	private boolean placeTower, exit;
	double waveTimer;
	boolean t1, t2, t3, deleteTower;
	Tower tempTower;
	private int lives;
	
	private Game(String title) {
		super(title);
	}
	
	public static Game getInstance() {
		if (instance == null) {
			instance = new Game("Tower Defense");
		}
		return instance;
	}

	public void init(GameContainer arg0) throws SlickException {
		File file = new File("res2/data.txt");
        try 
        {
            Scanner scanner = new Scanner(file);
            String mapPath = scanner.nextLine();
            tileMap = new TiledMap(mapPath);
            scanner.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        }
        
        t1 = true;
        t2 = t3 = false;
        lives = 10;
        deleteTower = false;
		coordTran = new CoordinateTranslator(screenWidth, screenHeight, worldWidth, worldHeight, new Point2D(0, 0));
		waveTimer = 0;
		agents = new ArrayList<Agent>();
		towers = new ArrayList<Tower>();
		bullets = new ArrayList<Bullet>();
		placeTower = false;
		agents.add(new Agent(600));
		
	}
	
	public void update(GameContainer gc, int delta) throws SlickException {
	
		if (placeTower) {
			if(t1) {
				// make a new tower with screen coordinates
				tempTower = new Tower(75.0, 50, new Point((int)(mouseX),(int)(mouseY)));
			}
			if(t2) {
				// make a new tower with screen coordinates
				tempTower = new Tower(100.0, 100, new Point((int)(mouseX),(int)(mouseY)));
			}
			if(t3) {
				// make a new tower with screen coordinates
				tempTower = new Tower(200.0, 500, new Point((int)(mouseX),(int)(mouseY)));
			}
			
			boolean isTowerThere = false;
			// Checks to see if tower is already there
			for (Tower t : towers) {
				if (t.getTileLocation().equals(tempTower.getTileLocation())) {
					isTowerThere = true;
				}
			}
			
			if (!isTowerThere && tileMap.getTileId(mouseX/32, mouseY/32, tileMap.getLayerIndex("Path")) == 0) {				
				towers.add(tempTower);
			}
			placeTower = false;
		}
		if (deleteTower) {
//			System.out.println("inside delete");
			for (int i=0; i<towers.size(); i++) {
				
				if ( (Math.abs(towers.get(i).getTileLocation().getX() - (mouseX/32)) < 1) && (Math.abs(towers.get(i).getTileLocation().getY() - (mouseY/32) ) < 1) ) {
					
//					System.out.println("delete tower" + towers.get(i).getTileLocation().getX());
					towers.remove(i);
					mouseX = mouseY = 0;
				}
			}
			
		}
		
		if (exit) {
			gc.exit();
		}
		
		for (Tower t : towers) {
			// Find the closest target
			t.setTarget(agents);
			if (t.hasCurrentTarget()) {
				Agent currTarg = t.getCurrentTarget();
				if (currTarg.getHealth() == 0) {
					agents.remove(currTarg);
				}
				else {
					// Attack it
					t.attack(t.getCurrentTarget(), delta);
				}
			}
		}
		
		
//		if(waveTimer > 1500) {
//			agents.add(new Agent(500));
//			waveTimer=0;
//		}
		
		// Add agents on timed interval
		waveTimer += delta;
		if (waveTimer > 800) {
			agents.add(new Agent(300));
			waveTimer=0;
		}
		
		// Delete Agents
		for (int i=0; i<agents.size(); i++) {
			if (agents.get(i).dead)
				agents.remove(agents.get(i));
		}
		
		for (Agent a : agents) {
			a.update(delta);
		}
		
		for (int i=0; i<bullets.size(); i++) {
			if (bullets.get(i).dead) {
				bullets.get(i).killBullet();
				bullets.remove(bullets.get(i));
			}
		}
		
		for (Bullet b : bullets) {
			b.update(delta);
		}
		
		if (lives == 0)
			exit = true;
		
		mouseX = mouseY = 0;

	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {
		tileMap.render(0, 0);
		// Render towers
		for (Tower t : towers) {
			t.render(g);
		}
		 //Render agents
		for (Agent a: agents) {
//			if (!a.dead)
				a.render(g);
		}
		//Rander bullets
		for (Bullet b : bullets) {
			b.render(g);
		}
		
	}
	
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
	      mouseX = x;
	      mouseY = y;
	      
	      if (deleteTower) {
	    	  placeTower = false;
	    	  return;
	      }
	    	  
	      placeTower = true;  
	}
	
	@Override
    public void keyPressed(int key, char c) {
		if(c == 'q' || c == 'Q') {
			exit = true;
		}
		if(c == '1') {
			t1 = true;
			t2 = t3 = false;
			
		}
		if(c == '2') {
			t2 = true;
			t1 = t3 = false;
		}
		if(c == '3') {
			t3 = true;
			t1 = t2 = false;
		}
		if(c == '0') {
			deleteTower = true;
		}
		if(c == '9') {
			deleteTower = false;
		}
//		if(c== '0') {
//			deTower = true;
//		}
	}
	
	public boolean isT1() {
		return t1;
	}
	
	public boolean isT2() {
		return t2;
	}
	public boolean isT3() {
		return t3;
	}
	
	public CoordinateTranslator getCT() {
		return coordTran;
	}
	
	public void addBullet(Bullet b) {
		bullets.add(b);
	}
	
	public int getLives() {
		return lives;
	}
	
	public void setLives(int newLives) {
		lives = newLives;
	}
}
