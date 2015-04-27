
package edu.moravian.StateMachine;
import edu.moravian.Game.Game;
import edu.moravian.Math.Point2D;
import edu.moravian.Math.Vector2D;
import edu.moravian.Entity.Agent;

public class Eat implements AgentState
{
    private static Eat instance;
    private Eat() {}
    private Point2D p1,p2;
    private Vector2D dest , agentLoc, result, newLoc;
    
    public static Eat getInstance()
    {
        if(instance == null)
            instance = new Eat();
        return instance;
    }

    @Override
    public void Execute(Agent agentEntity) 
    {
        int health = agentEntity.getHealth();
        if(agentEntity.getEnergy()!=0 && health!=agentEntity.getMaxHealth())
        {
            if(agentEntity.getEntitySx() != Game.getInstance().getWorldWidth()/2 || agentEntity.getEntitySy() != Game.getInstance().getWorldHeight()/2)
            {
                int energy = agentEntity.getEnergy();
                this.performAction(agentEntity);
                energy -= .01;
                agentEntity.setEnergy(energy);
            }
            else
            {
                health += 5;
                agentEntity.setHealth(health);
            }
        }
        else if (agentEntity.getEnergy()==0)
        {
            agentEntity.changeState(Rest.getInstance());
        }
        else
        {
            agentEntity.changeState(Chase.getInstance());
        }
        agentEntity.setState("still");
    }

    @Override
    public void performAction(Agent agentEntity) 
    {
        int agentMX = agentEntity.getEntitySx();
        int agentMY = agentEntity.getEntitySy();
       

        int delta = Game.getInstance().getDelta();
        double speed = 1/30;
        double velocity = 2;
        
        dest = new Vector2D(5,5);
        agentLoc = new Vector2D(agentEntity.getEntityWx(), agentEntity.getEntityWy());
        result = dest.minus(agentLoc);
//        
//        newLoc = move(agentLoc, result, speed, delta);
//        System.out.println(newLoc.getX());
//        agentEntity.setEntityWX(newLoc.getX());
//        agentEntity.setEntityWY(newLoc.getY());
            
        Vector2D playerPos = new Vector2D(agentEntity.getEntityWx(), agentEntity.getEntityWy());
        Vector2D direction = dest.minus(agentLoc);
        direction.normalize();
        playerPos.plusEquals(direction);
        playerPos.timesEquals(velocity);
        
        agentEntity.setEntityWX(playerPos.getX());
        agentEntity.setEntityWY(playerPos.getY());
            
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
