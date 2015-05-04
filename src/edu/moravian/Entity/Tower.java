package edu.Moravian.Entity;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Map.Entry;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import edu.Moravian.Game.Game;
import edu.Moravian.Math.Point2D;

public class Tower extends Entity {
	
	private Double attackRange;
	private int attackPower;
	private Agent currentTarget;
	private HashMap<Agent, Double> map;
	private Point tileLocation;
	private Image img, img2, img3, img4;
	private int shotTimer;
	public String type;
	
	public Tower (Double attackRange, int attackPower, Point tileLocation) throws SlickException {
		this.attackRange = attackRange;
		this.attackPower = attackPower;
		img2 = new Image("res2/tower2.png");
		img3 = new Image("res2/tower3.png");
		img4 = new Image("res2/tower4.png");
		// Convert the screen coordinates to tile coordinates
		this.tileLocation = new Point((int)tileLocation.x/32, (int)tileLocation.y/32);
		// Convert the screen coordinates to world coordinates
		this.location = Game.getInstance().getCT().screenToWorld(new Point(this.tileLocation.x*32+16, this.tileLocation.y*32+16));
		this.currentTarget = null;
		if (Game.getInstance().isT1()) {
			img = img2;
			type = "t1";
		}
		if (Game.getInstance().isT2()) {
			img = img3;
			type = "t2";
		}
		if (Game.getInstance().isT3()) {
			img = img4;
			type = "t3";
		}
		
		shotTimer = 1000;
	}
	
	public void render(Graphics g) {
		g.drawImage(img, (float)(tileLocation.x*32), (float)(tileLocation.y*32));

	}
	
	public void setTarget(ArrayList<Agent> agents) {
				
		map = new HashMap<Agent, Double>();
		
		if (agents.size() == 0) {
			this.currentTarget = null;
			return;
		}
	
		for (Agent a : agents) {
			
			// Tower's Location component - Agent's component
//			System.out.println("TowerX: " + this.getLocation().getX() + ", AgentX: " + a.getLocation().getX());
//			System.out.println("TowerY: " + this.getLocation().getY() + ", AgentY: " + a.getLocation().getY());
			double x = this.getLocation().getX() - a.getLocation().getX();
			double y = this.getLocation().getX() - a.getLocation().getY();
			double r = attackRange;
			double d = Math.sqrt(x*x + y*y);

//			System.out.println("r: " + r);
//			System.out.println("d: " + d);
			// If it's in range, add it to the map
			if (r > d) {
//				System.out.println("Did I get here?");
				map.put(a, d);
			}
		}
	
		if (map.size() == 0) {
			this.currentTarget = null;
			return;
		}
		
		double min = Double.POSITIVE_INFINITY;
		for (Entry<Agent, Double> entry : map.entrySet()) {
			if (entry.getValue() < min ) {
				min = entry.getValue();
				this.currentTarget = entry.getKey();
			}
		}
		
		
//		Double minValueInMap=(Collections.min(map.values()));
//        for (Entry<Agent, Double> entry : map.entrySet()) {
//            if (entry.getValue()==minValueInMap) {
//                this.currentTarget = entry.getKey();
//            }
//            System.out.println("value: " + entry.getValue());
//        }
		
//        System.out.println("currentTarget: " + currentTarget.getLocation());
		// Get the first key, which should be the lowest
		//this.currentTarget = (Agent) sortedMap.keySet().toArray()[0];
	}
	
	public void attack(Agent target, int delta) {
		if (shotTimer >= 1000) {
			if (target == currentTarget) {
				Bullet b = new Bullet(location, target, attackPower);
				Game.getInstance().addBullet(b);
			}
			else {
				return;
			}
			shotTimer = 0;
		}
		else {
			shotTimer += delta;
		}
	}
	
	public Agent getCurrentTarget() {
		return currentTarget;
	}
	
	public Point getTileLocation() {
		return tileLocation;
	}
	
	public Boolean hasCurrentTarget() {
		if (currentTarget == null) {
			return false;
		}
		return true;
	}
}
