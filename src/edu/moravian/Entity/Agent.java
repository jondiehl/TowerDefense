package edu.moravian.Entity;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import edu.moravian.Game.Game;
import edu.moravian.Math.Point2D;
import edu.moravian.Math.Vector2D;
import edu.moravian.PathFinding.PathFinder;
import edu.moravian.StateMachine.AgentState;
import edu.moravian.StateMachine.Chase;


public class Agent extends Entity {
	
	private AgentState currentState;
	private String currentMovementState;
    private final int maxEnergy;
    private int energy;
    private final int maxHealth;
    private int health;
    private PathFinder pf;
//    private Point2D eatLoc = new Point2D(5*32, 2*32);
    private Point2D eatLoc = new Point2D(96, 32);
    private Point2D restLoc = new Point2D(94*32, 4*32);
    private Point2D currEatNode;
    private Point2D currRestNode;
    private Iterator<Point2D> eatIT;
    private Iterator<Point2D> restIT;
    private ArrayList<Point2D> path;

    public Agent(int maxHealth, int maxEnergy) 
    {
    	this.type = "agent";
        this.currentState = Chase.getInstance();
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.entityWx = (int) (Math.random()*((CT.getWorldWidth()*32)*CT.getWorldWidth()/CT.getScreenWidth()));
        this.entityWy = (int) (Math.random()*((CT.getWorldHeight()*32)*CT.getWorldHeight()/CT.getScreenHeight()));
        pf = new PathFinder(Game.getInstance().getGameMap());
    }
    
    @Override
    public void update() 
    {
        Point entityCT = CT.worldToScreen(entityWx, entityWy);
        entitySx = (int) entityCT.getX();
        entitySy = (int)(entityCT.getY() - CT.getScreenHeight())*-1;
        currentState.Execute(this);
    }
    
    @Override
    public void reset()
    {
        this.entityWx = (int) (Math.random()*((CT.getWorldWidth()*32)*CT.getWorldWidth()/CT.getScreenWidth()));
        this.entityWy = (int) (Math.random()*((CT.getWorldHeight()*32)*CT.getWorldHeight()/CT.getScreenHeight()));
    }
    
    public void genEatPath() {
//    	System.out.println("ent x: " + entitySx + ", ent y: " + entitySy);
//    	System.out.println("eat x: " + eatLoc.getX() + ", eat y: " + eatLoc.getY());
    	pf.generatePath(new Point2D(entitySx, entitySy), eatLoc);
    	System.out.println("ent x: " + entitySx + ", ent y: " + entitySy);
    	System.out.println("eat x: " + eatLoc.getX() + ", eat y: " + eatLoc.getY());
    	System.out.println("here in gen path2");
    	
//    	eatIT = pf.getPath().iterator();
    	path = pf.getPath();
    	eatIT = path.iterator();
    	System.out.println("here");
    	currEatNode = eatIT.next();
    	System.out.println("currEatNote gen: " + currEatNode);
    	System.out.println("path: " + path);
    }
    
    public Point2D nextEatNode () {
    	if (new Point2D(entitySx, entitySy).equals(currEatNode)) {
    		if (eatIT.hasNext())
    			currEatNode = eatIT.next();
    	}
    	System.out.println("currEatNode next: " + currEatNode);
    	System.out.println("currAGENTNode next: " + entitySx + ", " + entitySy);
    	
    	return new Point2D(currEatNode.getX(), currEatNode.getY());
    }
    
    public void changeState(AgentState newState)
    {
        currentState = newState;
    }
    
    public void setEntityWX(double entityWX)
    {
        this.entityWx = entityWX;
    }
    
    public void setEntityWY(double entityWY)
    {
        this.entityWy = entityWY;
    }
    
    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }
    
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

	@Override
	public void setState(String state) {
		currentMovementState = state;
	}

	@Override
	public String getState() {
		return currentMovementState;
	}
}
