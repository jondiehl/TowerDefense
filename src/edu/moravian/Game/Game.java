package edu.moravian.Game;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import edu.moravian.Entity.Agent;
import edu.moravian.Entity.Entity;
import edu.moravian.Entity.Player;
import edu.moravian.Entity.Prize;
import edu.moravian.Math.CoordinateTranslator;
import edu.moravian.Math.Point2D;
import edu.moravian.View.SpriteRenderer;
import edu.moravian.View.miniMap;

public class Game extends BasicGame {

	private static Game instance;
	
	private final static int screenWidth = 800;
	private final static int screenHeight = 600;
	private GameMap map;
	private miniMap miniMap;
	private SpriteRenderer playerRen, agent1Ren, agent2Ren, agent3Ren, prizeRen;
	private Player player;
	private Agent agent1, agent2, agent3;
	private Prize prize;
	private ArrayList<Entity> ents;
	private double temp_x, temp_y, x, y; // Player x and y
	private int delta; // Time between frames
	private int prizesFound, deaths;
	private CoordinateTranslator CT;
	private GameSoundManager gsm;
	private GameLog log;
	private Boolean exit, gameLog, goRight, goLeft, goUp, goDown;
	//private Boolean agentRight, agentLeft, agentUp, agentDown;

	private TiledMap tMap;

	private Game(String title) {
		super(title);
	}
	
	public static Game getInstance() {
		if (instance == null) {
			instance = new Game("Jon's Game 3");
		}
		return instance;
	}
	
	@Override
	public void init(GameContainer arg0) throws SlickException {
		this.getMapFromFile("res2/data.txt");
		CT = new CoordinateTranslator(tMap.getWidth(), tMap.getHeight(), 0, 0, screenWidth, screenHeight);
		map = new GameMap(tMap);
		miniMap = new miniMap(instance, "res2/miniMapPic.png");
		
		playerRen = new SpriteRenderer();
		agent1Ren = new SpriteRenderer();
		agent2Ren = new SpriteRenderer();
		agent3Ren = new SpriteRenderer();
		prizeRen = new SpriteRenderer();
		prizesFound = 0;
		deaths = 0;
		x = y = 0;
		ents = new ArrayList<Entity>();
		ents.add(player = new Player());
		ents.add(agent1 = new Agent(25000, 10));
		ents.add(agent2 = new Agent(27000, 20));
		ents.add(agent3 = new Agent(1000, 15000));//30000, 28000)); hunger, energy
		ents.add(prize = new Prize());
		gsm = new GameSoundManager();
		exit = false;
		gameLog = false;
		goRight = false;
		goLeft = false;
		goUp = false;
		goDown = false;
		log = new GameLog(this);
		gsm = new GameSoundManager();
	    gsm.playNextSong();
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		this.delta = delta;
		
		if (gsm.isPlaying()) {/* do nothing*/}
		else {
			gsm.playNextSong();
		}
		
		if(exit) {
			gc.exit();
		}
		if(goRight||goLeft||goDown||goUp) {
			if (goRight) {
	            temp_x = (x + 1 * delta / 30.0) % screenWidth;
	            System.out.println("tile Open? " + isXTileOpen(temp_x));
	            if (isXTileOpen(temp_x)) {
	            	x = temp_x;
	            }
	            player.setState("right");
			}
			if (goLeft) {
	            temp_x = (x - 1 * delta / 30.0) % screenWidth;
	            if (isXTileOpen(temp_x)) {
	            	x = temp_x;
	            }
	            player.setState("left");
	        }
			if (goDown) {
	            temp_y = (y - 1 * delta / 30.0) % screenHeight;
	            if (isYTileOpen(temp_y)) {
	            	y = temp_y;
	            }
	            player.setState("down");
	        }
			if (goUp) {
	            temp_y = (y + 1 * delta / 30.0) % screenHeight;
	            if (isYTileOpen(temp_y)) {
	            	y = temp_y;
	            }
	            player.setState("up");
	        }
		}
		else {
			player.setState("still");
		}
        
        map.checkPlayerWrap(x, y);
        x = map.getX();
        y = map.getY();
        
        System.out.println("x: " + x);
        
        //update entities themselves
        for (Entity e : ents) {
//        	if(e.getType() != "agent")
			e.update();
		}
        
        //update sprite rendering
        miniMap.update();
        playerRen.update(map, player);
        agent1Ren.update(map, agent1);
        agent2Ren.update(map, agent2);
        agent3Ren.update(map, agent3);
        prizeRen.update(map, prize);
        
        //Collision detection
        if(Collision.checkCollisionPrize(player.getEntitySx(), player.getEntitySy(), prize.getEntitySx(), prize.getEntitySy())) {
            prizesFound += 1;
            prize.reset();
        }
        if(Collision.checkCollision(player.getEntitySx(), player.getEntitySy(), agent1.getEntitySx(), agent1.getEntitySy())
                || Collision.checkCollision(player.getEntitySx(), player.getEntitySy(), agent2.getEntitySx(), agent2.getEntitySy())
                || Collision.checkCollision(player.getEntitySx(), player.getEntitySy(), agent3.getEntitySx(), agent3.getEntitySy())) {
            deaths += 1;
            x = 0;
            y = 0;
            
            //reset the position of the agent in the world
            for(Entity e:ents) {
            	if(e.getType() == "agent") { e.reset(); }
            }
        }
//        System.out.println("x: " + x);
        System.out.println("player Wx: " + player.getEntityWx());
        System.out.println("player Sx: " + player.getEntitySx());
        // supposed to print neighbors of current player pos.
//        Iterable<Point2D> neighbors = map.getNeighbors(new Point2D(player.getEntitySx(),player.getEntitySy()));
//        for(Point2D p : neighbors) {
//        	System.out.println(p);
//        }
        
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// Render the map and miniMap
		map.render();
		miniMap.render(g);
		// Render all the entities
		playerRen.render(player);
		agent1Ren.render(agent1);
        agent2Ren.render(agent2);
        agent3Ren.render(agent3);
        prizeRen.render(prize);
        if (gameLog) {
        	log.display(g);
        }
	}
	
	@Override
    public void keyPressed(int key, char c) {
		if(c == 'q' || c == 'Q') {
			exit = true;
		}
		/*if(key == Input.KEY_SPACE) {
			playSound = true;
		}*/
		if(key == Input.KEY_TAB) {
			gameLog = gameLog == false;
		}
        if(c == 'w' || c == 'W' || key == Input.KEY_UP) {
            goUp = true;
        }
        if(c == 'a' || c == 'A' || key == Input.KEY_LEFT) {
            goLeft = true;
        }
        if(c == 's' || c == 'S' || key == Input.KEY_DOWN) {
            goDown = true;
        }
        if(c == 'd' || c == 'D' || key == Input.KEY_RIGHT) {
            goRight = true;
        }
	}
	@Override
    public void keyReleased(int key, char c) {
		if(c == 'q' || c == 'Q') {
			exit = false;
		}
		/*if(key == Input.KEY_SPACE) {
			playSound = false;
		}*/
        if(c == 'w' || c == 'W' || key == Input.KEY_UP) {
            goUp = false;
        }
        if(c == 'a' || c == 'A' || key == Input.KEY_LEFT) {
            goLeft = false;
        }
        if(c == 's' || c == 'S' || key == Input.KEY_DOWN) {
            goDown = false;
        }
        if(c == 'd' || c == 'D' || key == Input.KEY_RIGHT) {
            goRight = false;
        }
	}
	
	private Boolean isXTileOpen(double temp) {
		Point point = CT.worldToScreen(temp, y);
		int real_x = (int)((point.getX() + screenWidth/2))/32;
		int real_y = (int)((point.getY()-screenHeight)+screenHeight/2)/32;
		int objectLayer = tMap.getLayerIndex("Objects");
		
		Point2D p = checkXY(real_x, real_y);
		
		if (tMap.getTileId((int)p.getX(), (int)p.getY(), objectLayer) == 0) {
			return true;
		}
		return false;
	}
	
	private Boolean isYTileOpen(double temp) {
		Point point = CT.worldToScreen(x, temp);
		int real_x = (int)((point.getX() + screenWidth/2))/32;
		int real_y = (int)((point.getY()-screenHeight)+screenHeight/2)/32;
		int objectLayer = tMap.getLayerIndex("Objects");
		
		Point2D p = checkXY(real_x, real_y);
		
		if (tMap.getTileId((int)p.getX(), (int)p.getY(), objectLayer) == 0) {
			return true;
		}
		return false;
	}
	
	// Checks and sets x & y to make sure they are valid tile ID's
	public Point2D checkXY(int real_x, int real_y) {
		if (real_x == tMap.getWidth())
			real_x = 0;
		if (real_x == -1)
			real_x = tMap.getWidth()-1;
		if (real_y == tMap.getHeight())
			real_y = 0;
		if (real_y == -1)
			real_y = tMap.getHeight()-1;
		return new Point2D(real_x, real_y);
	}
	
	public TiledMap getMapFromFile(String filename) throws SlickException {
		File file = new File(filename);
        try {
            Scanner scanner = new Scanner(file);
            String mapPath = scanner.nextLine();
            tMap = new TiledMap(mapPath);
        }
        catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        }
		return tMap;
	}
	
	public GameMap getGameMap() {
		return map;
	}

	public CoordinateTranslator getCT() {
		return CT;
	}

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
	
	public int getWorldWidth() {
		return tMap.getWidth();
	}
	
	public int getWorldHeight() {
		return tMap.getHeight();
	}
	
	public Entity getEntity(int i) {
		return ents.get(i);
	}
	
	public int getPrizesFound() {
		return prizesFound;
	}
	
	public int getDeaths() {
		return deaths;
	}
	
	public int getDelta() {
		return delta;
	}
	
	private void reset() {
		prizesFound = 0;
		deaths = 0;
		x = 0;
		y = 0;
	}
}
