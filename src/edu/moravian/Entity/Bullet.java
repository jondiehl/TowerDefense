package edu.Moravian.Entity;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import edu.Moravian.Game.Game;
import edu.Moravian.Math.CoordinateTranslator;
import edu.Moravian.Math.Point2D;

public class Bullet extends Entity {
	
//	private ArrayList<Point2D> agentPath;
	private Point2D nextNode, agentLoc;
	public boolean dead;
	private double velocity, distance, locX, locY, nextX, nextY;
	private Agent agent;
	private CoordinateTranslator CT;
	private Point screenPoint;
	private int towerDamage;

	public Bullet(Point2D towerLoc, Agent agent, int towerDamage) {
		this.location = towerLoc;
		dead = false;
		velocity = 1200;
		this.agent = agent;
		CT = Game.getInstance().getCT();
		this.towerDamage = towerDamage;
//		nextNode = towerLoc;
	}
	
	public void update(int delta) {
//		agentPath.add(agentLoc);

			agentLoc = agent.getLocation();
			if ( ( Math.abs(location.getX() - agentLoc.getX()) < 10 && Math.abs(location.getY() - agentLoc.getY()) < 10) ) {
				dead = true;
			}
			
			distance = (velocity*delta)/1000;
			
			nextX = agentLoc.getX();
			nextY = agentLoc.getY();
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
	
	public void killBullet() {
		agent.takeDamage(towerDamage);
		System.out.println("health: " + agent.getHealth());
	}
	
	public void render(Graphics g) {
		if (agent.dead) {
			dead = true;
		}
		else {
			screenPoint = CT.worldToScreen(location);
			g.setColor(Color.blue);
			g.fillOval((float)screenPoint.getX(), (float)(screenPoint.getY()), 10, 10);
			g.drawOval((float)screenPoint.getX(), (float)(screenPoint.getY())-10, 10, 10);
		}
		
	}
}
