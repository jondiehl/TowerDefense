package edu.Moravian.Entity;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import edu.Moravian.Game.Game;
import edu.Moravian.Math.Point2D;

public class Agent extends Entity {
	
	private int health;
	private int maxHealth;
	private Image img;
	private int velocity;
	private double distance;
	private Point2D nextNode;
	private ArrayList<Point2D> path;
	private Iterator<Point2D> pathIT;
	double locX, locY, nextX, nextY;
	public boolean dead;

	public Agent(int maxHealth) throws SlickException {
		img = new Image("res2/bomb.png");
		this.health = maxHealth;
		this.maxHealth = maxHealth;
		distance = 0;
		velocity = 600;
		dead = false;
		path = new ArrayList<Point2D>(); 	
		path.add(new Point2D(2110, 2500)); path.add(new Point2D(2110, 2242)); //64,2242
		path.add(new Point2D(2110, 1730)); path.add(new Point2D(318, 1730));
		path.add(new Point2D(318, 450)); path.add(new Point2D(2496, 450));
		
		pathIT  = path.iterator();
		this.location = nextNode = pathIT.next();
		locX = nextX = location.getX();
		locY = nextY = location.getY();
	}
	
	public void update(int delta) {
		if ( ( Math.abs(nextNode.getX() - location.getX()) < 10 && Math.abs(nextNode.getY() - location.getY()) < 10) ) {
			if(pathIT.hasNext())
				nextNode = pathIT.next();
			else {
				dead = true;
				Game.getInstance().setLives(Game.getInstance().getLives()-1);
			}
		}
		
		distance = (velocity*delta)/1000;
		
		nextX = nextNode.getX();
		nextY = nextNode.getY();
		locX = location.getX();
		locY = location.getY();
		
		if(locX < nextX) 
			locX += distance;
		else if(locX > nextX)
			locX -= distance;
		
		if(locY < nextY)
			locY += distance;
		else if (locY > nextY)
			locY -= distance;
		
		location = new Point2D(locX, locY);
	}
	
	public void render(Graphics g) {
		Point screenLocation = Game.getInstance().getCT().worldToScreen(location);
		g.drawImage(img, (float)screenLocation.x, (float)screenLocation.y);
	}
	
	public void move() {
		
	}
	
	public void setLocation(Point2D loc) {
		this.location = loc;
	}
	
	public void takeDamage(int damage) {
		if ( (health - damage) < 0) {
			health = 0;
		}
		else
			this.health -= damage;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
}
