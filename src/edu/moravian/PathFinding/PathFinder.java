package edu.moravian.PathFinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Comparator;

//import org.newdawn.slick.util.pathfinding.AStarPathFinder;


import edu.moravian.Game.GameMap;
import edu.moravian.Math.Point2D;
import edu.moravian.tests.GameMapInterface;

public class PathFinder {

	private GameMapInterface map;
	private ArrayList<Point2D> path;
	private Set<Edge> consideredEdges;
	
	public PathFinder(GameMapInterface map) {
		this.map = map;
		consideredEdges = new HashSet<Edge>();
	}
	
	//A* here
	public void generatePath(Point2D start, Point2D end) {
		
		//PQ, here's 8 points, whats the closest point? have to implement comparable that uses distance and vector approx.
		
		Point2D u = new Point2D();
		
//		for each vertex u within V:
//		d[u] = infinity
//		pred[u] = NULL
		
		//need a map for distance and predecessors
		Map<Point2D, Double> d = new HashMap<>(); //FIXME all INFINITY
		Map<Point2D, Point2D> pred = new HashMap<>(); //FIXME all NULL
		
//		d[s] = 0
//		//# Put all the vertexes of G into the priority queue
//		Q = V
		
		//set first node's dist. to 0
		d.put(start, 0.0);
		//PQ with comparator that will figure out ordering for the minimum distance to end
		PriorityQueue<Point2D> PQ = new PriorityQueue<Point2D>(new Comparator<Point2D>(){
			@Override
			public int compare(Point2D p1, Point2D p2) {
				double w1 = w(end, p1);
				double w2 = w(end, p2);
				return (int)(w1-w2);
			}
		});
		
		//add start node.point to Q
		PQ.add(start);
		
//		while not Q.empty()
//			//# Take the Vertex in Q with the minimum distance
//			u = ExtractMin(Q)

		while (!PQ.isEmpty()) {
//			System.out.println("PQ: " + PQ);
			//Take the vertex in PQ with the minimum distance
			u = PQ.poll();
//			System.out.println(u);
//			System.out.println(end);
			
			if (u.equals(end)) {
//				System.out.println("Pred: " + pred);
				constructPath(pred, start, u);
				return;
			}
			
			Iterable<Point2D> adjacent = map.getNeighbors(u);
			
//			System.out.println("outside of for loop");
			for (Point2D v : adjacent) {
				Double dist = d.get(u) + w(u,v);
				if (!d.containsKey(v)) {
					d.put(v, Double.POSITIVE_INFINITY);
				}
				if (!isInPQ(PQ, v) && (dist < d.get(v)) ) { //FIXME check if distance is INFINITY or if the pred. is NULL
//					System.out.println("added v: " + v);
					d.put(v, d.get(u) + w(u,v));
					if (!pred.containsKey(v))
						pred.put(v, u);
//					System.out.println("current pred: " + pred);
					PQ.add(v);
					consideredEdges.add(new Edge(u, v));
//					System.out.println("here 'if'");
				}
			}
		}
	}

	private void constructPath(Map<Point2D,Point2D> pred, Point2D start, Point2D current){
		ArrayList<Point2D> totalPath = new ArrayList<Point2D>();
		totalPath.add(current);
		Point2D predOfCurr;
		while (current != start) {
//			System.out.println("current: " + current);
			predOfCurr = pred.get(current);
			totalPath.add(predOfCurr);
			current = predOfCurr;
		}
		path = totalPath;
		Collections.reverse(path);
	}
	
	//Returns distance from point a to point b; equivalent to weight
	public double w(Point2D a, Point2D b) {
		return Math.sqrt( (a.getX()-b.getX())*(a.getX()-b.getX()) + 
							(a.getY()-b.getY())*(a.getY()-b.getY()) );
	}
	
	public ArrayList<Point2D> getPath() {
		return path;
	}
	
	public Set<Edge> getConsideredEdges() {
		return consideredEdges;
	}
	
	public Boolean isInPQ(PriorityQueue<Point2D> frontier, Point2D node) {
//		System.out.println("IsInPQ");
		Point2D[] array = (Point2D[]) frontier.toArray(new Point2D[0]);
//		System.out.println("IsInPQ array: " + array);
		for (Point2D currentPoint : array) {

			if (currentPoint.equals(node)) {
				return true;
			}
		}
		return false;
	}
}
