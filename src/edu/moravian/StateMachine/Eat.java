
package edu.moravian.StateMachine;

import edu.moravian.Game.Game;
import edu.moravian.Math.Point2D;
import edu.moravian.Math.Vector2D;
import edu.moravian.Entity.Agent;

public class Eat implements AgentState
{
    private static Eat instance;
    private double agentWX, agentWY;
//    private Point2D eatLoc = new Point2D(3*32, 4*32);
//    private Point2D p1,p2;
//    private Point2D dest = new Point2D(100, 100);
//    private Vector2D agentLoc, result, newLoc;
    
    private Eat() {}
    
    public static Eat getInstance()
    {
        if(instance == null)
            instance = new Eat();
        return instance;
    }

    @Override
    public void Execute(Agent agentEntity) 
    {
    	Point2D eatLoc = agentEntity.nextEatNode();
        int health = agentEntity.getHealth();
        int maxHealth = agentEntity.getMaxHealth();
        int energy = agentEntity.getEnergy();
        
        if(energy !=0 && health != maxHealth)
        {
            if(agentEntity.getEntitySx() == eatLoc.getX() && agentEntity.getEntitySy() == eatLoc.getY()) //agentEntity.getEntitySx() != Game.getInstance().getWorldWidth()/2 || agentEntity.getEntitySy() != Game.getInstance().getWorldHeight()/2)
            {
            	health += 5;
            	agentEntity.setHealth(health);
            	agentEntity.setState("rightStill");
            }
            else
            {
            	this.performAction(agentEntity);
                energy -= .01;
                agentEntity.setEnergy(energy);
            }
        }
        else if (energy == 0)
        {
            agentEntity.changeState(Rest.getInstance());
        }
        else
        {
            agentEntity.changeState(Chase.getInstance());
        }
    }

    @Override
    public void performAction(Agent agentEntity) 
    {
    	// Eat Map Location
        Point2D eatNode = agentEntity.nextEatNode();
        double eatMX = eatNode.getX();
        double eatMY = eatNode.getY();
        // Agent Map Location
        int agentMX = agentEntity.getEntitySx();
        int agentMY = agentEntity.getEntitySy();
        int delta = Game.getInstance().getDelta();
        
        if(eatMX != agentMX) {
            if(eatMX > agentMX) {
                if((eatMX - agentMX) > Game.getInstance().getWorldWidth()*32/2) {
                    agentWX = (agentEntity.getEntityWx() -1*delta / 50.0);
                    if(agentWX < 0)
                        agentWX = Game.getInstance().getWorldWidth()*32*Game.getInstance().getWorldWidth()/Game.getInstance().getScreenWidth();
                }
                else {
                    agentWX =(agentEntity.getEntityWx() +1*delta / 50.0) % ((Game.getInstance().getWorldWidth()*32*Game.getInstance().getWorldWidth())/Game.getInstance().getScreenWidth());
                }
                agentEntity.setState("right");
            }
            else if(eatMX < agentMX) {
                if((agentMX - eatMX) > Game.getInstance().getWorldWidth()*32/2) {
                    agentWX =(agentEntity.getEntityWx() +1*delta / 50.0) % ((Game.getInstance().getWorldWidth()*32*Game.getInstance().getWorldWidth())/Game.getInstance().getScreenWidth());
                }
                else {
                    agentWX = (agentEntity.getEntityWx() -1*delta / 50.0);
                    if(agentWX < 0)
                        agentWX = Game.getInstance().getWorldWidth()*32*Game.getInstance().getWorldWidth()/Game.getInstance().getScreenWidth();
                }
                agentEntity.setState("left");

            }
            agentEntity.setEntityWX(agentWX);
        }
        if(eatMY != agentMY) {
            if(eatMY > agentMY) {
                if((eatMY - agentMY) > Game.getInstance().getWorldHeight()*32/2) {
                    agentWY = (agentEntity.getEntityWy() -1*delta / 50.0);
                    if(agentWY < 0)
                        agentWY = Game.getInstance().getWorldHeight()*32*Game.getInstance().getWorldHeight()/Game.getInstance().getScreenHeight();
                }
                else {
                    agentWY =(agentEntity.getEntityWy() +1*delta / 50.0) % ((Game.getInstance().getWorldHeight()*32*Game.getInstance().getWorldHeight())/Game.getInstance().getScreenHeight());
                }
                agentEntity.setState("down");
            }
            else if(eatMY < agentMY) {
                if((agentMY - eatMY) > Game.getInstance().getWorldHeight()*32/2) {
                    agentWY =(agentEntity.getEntityWy() +1*delta / 50.0) % ((Game.getInstance().getWorldHeight()*32*Game.getInstance().getWorldHeight())/Game.getInstance().getScreenHeight());
                }
                else {
                    agentWY = (agentEntity.getEntityWy() -1*delta / 50.0);
                    if(agentWY < 0)
                        agentWX = Game.getInstance().getWorldHeight()*32*Game.getInstance().getWorldHeight()/Game.getInstance().getScreenHeight();
                }
                agentEntity.setState("up");
            }
            agentEntity.setEntityWY(agentWY);
        }
    	
    	
//        int agentMX = agentEntity.getEntitySx();
//        int agentMY = agentEntity.getEntitySy();
//       

//        int delta = Game.getInstance().getDelta();
//        double speed = 1/30;
//        double velocity = 2;
//        
//        dest = new Vector2D(5,5);
//        agentLoc = new Vector2D(agentEntity.getEntityWx(), agentEntity.getEntityWy());
//        result = dest.minus(agentLoc);
////        
////        newLoc = move(agentLoc, result, speed, delta);
////        System.out.println(newLoc.getX());
////        agentEntity.setEntityWX(newLoc.getX());
////        agentEntity.setEntityWY(newLoc.getY());
//            
//        Vector2D playerPos = new Vector2D(agentEntity.getEntityWx(), agentEntity.getEntityWy());
//        Vector2D direction = dest.minus(agentLoc);
//        direction.normalize();
//        playerPos.plusEquals(direction);
//        playerPos.timesEquals(velocity);
//        
//        agentEntity.setEntityWX(playerPos.getX());
//        agentEntity.setEntityWY(playerPos.getY());
            
//        agentEntity.setEntityWX(agentWX);
        
            
            
            
//        agentEntity.setEntityWY(agentWY);

    }
    
//    if(Game.getInstance().getWorldHeight()/2 > agentMY)
//    {
//        agentWY =(agentEntity.getEntityWy() +1*delta / 30.0) % ((Game.getInstance().getWorldHeight()*32*Game.getInstance().getWorldHeight())/Game.getInstance().getScreenHeight());
//
//    }
//    else if(Game.getInstance().getWorldHeight()/2 < agentMY)
//    {
//        agentWY = (agentEntity.getEntityWy() -1*delta / 30.0);
//    }
    
	public Vector2D move(Vector2D loc, Vector2D v, double speed, int delta) {
		Vector2D velocity = v;
		
		if(v.magnitude() > Vector2D.TOL){
//			v.normalize();
			velocity = v.times(speed * delta);
			Vector2D newLoc = new Vector2D((loc.getX() + velocity.getX()),
					(loc.getY() + velocity.getY()));
			
//			wrap(newLoc);
			
			return newLoc;
//			direction = velocity.angle();
		}
		return loc;
	}
}
