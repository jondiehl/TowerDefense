
package edu.moravian.StateMachine;

import edu.moravian.Entity.Agent;
import edu.moravian.Game.Game;
import edu.moravian.Math.Point2D;

public class Rest implements AgentState
{
    private static Rest instance;
    private double agentWX,agentWY;
    Point2D restLoc = new Point2D(94*32, 4*32);
    
    private Rest()
    { }
    
    public static Rest getInstance()
    {
        if(instance == null)
            instance = new Rest();
        return instance;
    }

    @Override
    public void Execute(Agent agentEntity) 
    {
        int health = agentEntity.getHealth();
        int maxEnergy = agentEntity.getMaxEnergy();
        int energy = agentEntity.getEnergy();
        
        if(energy != maxEnergy)
        {
            if(agentEntity.getEntitySx() == restLoc.getX() && agentEntity.getEntitySy() == restLoc.getY()) //agentEntity.getEntitySx() != Game.getInstance().getWorldWidth()/2 || agentEntity.getEntitySy() != Game.getInstance().getWorldHeight()/2)
            {
            	energy += 5;
            	agentEntity.setEnergy(energy);
            	agentEntity.setState("rightStill");
            }
            else
            {
            	this.performAction(agentEntity);
	            if (health > 0 ) {	
	                health -= .01;
	                agentEntity.setHealth(health);
	            }
            }
        }
        else
        {
            agentEntity.changeState(Chase.getInstance());
        }
    }

    @Override
    public void performAction(Agent agentEntity) 
    {
    	// Rest Map Location
        double restMX = restLoc.getX();
        double restMY = restLoc.getY();
        // Agent Map Location
        int agentMX = agentEntity.getEntitySx();
        int agentMY = agentEntity.getEntitySy();
        int delta = Game.getInstance().getDelta();
        
        if(restMX != agentMX) {
            if(restMX > agentMX) {
                if((restMX - agentMX) > Game.getInstance().getWorldWidth()*32/2) {
                    agentWX = (agentEntity.getEntityWx() -1*delta / 50.0);
                    if(agentWX < 0)
                        agentWX = Game.getInstance().getWorldWidth()*32*Game.getInstance().getWorldWidth()/Game.getInstance().getScreenWidth();
                }
                else {
                    agentWX =(agentEntity.getEntityWx() +1*delta / 50.0) % ((Game.getInstance().getWorldWidth()*32*Game.getInstance().getWorldWidth())/Game.getInstance().getScreenWidth());
                }
                agentEntity.setState("right");
            }
            else if(restMX < agentMX) {
                if((agentMX - restMX) > Game.getInstance().getWorldWidth()*32/2) {
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
        if(restMY != agentMY) {
            if(restMY > agentMY) {
                if((restMY - agentMY) > Game.getInstance().getWorldHeight()*32/2) {
                    agentWY = (agentEntity.getEntityWy() -1*delta / 50.0);
                    if(agentWY < 0)
                        agentWY = Game.getInstance().getWorldHeight()*32*Game.getInstance().getWorldHeight()/Game.getInstance().getScreenHeight();
                }
                else {
                    agentWY =(agentEntity.getEntityWy() +1*delta / 50.0) % ((Game.getInstance().getWorldHeight()*32*Game.getInstance().getWorldHeight())/Game.getInstance().getScreenHeight());
                }
                agentEntity.setState("down");
            }
            else if(restMY < agentMY) {
                if((agentMY - restMY) > Game.getInstance().getWorldHeight()*32/2) {
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
    }
}
