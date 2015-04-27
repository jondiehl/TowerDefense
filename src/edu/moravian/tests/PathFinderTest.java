package edu.moravian.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Test;
import org.newdawn.slick.SlickException;

import edu.moravian.Game.GameMap;
import edu.moravian.Math.Point2D;
import edu.moravian.PathFinding.Edge;
import edu.moravian.PathFinding.PathFinder;

public class PathFinderTest {

	@Test
	public void test() {
		GameMapMock map = new GameMapMock();
		PathFinder pf = new PathFinder(map);
		//set(x,y,object)
//		map.set(3,1,1);
		map.set(3,2, 1);
		map.set(3,3, 1);
		map.set(3,4, 1);
		map.set(3,5, 1);
		map.set(3,6, 1);
		map.set(3,7,1);
		map.set(3,8,1);
		map.set(3,9,1);
		
//		map.set(6,1,1);
//		map.set(6,2,1);
		map.set(6,3,1);
		map.set(6,4,1);
		map.set(6,5,1);
		map.set(6,6,1);
		map.set(6,6,1);
		map.set(6,7,1);

		map.set(8,1, 1);
		map.set(8,2, 1);
		map.set(8,3, 1);
		map.set(8,4, 1);
		map.set(8,5, 1);
		map.set(8,6, 1);
		map.set(8,7,1);
		map.set(8,8,1);
		map.set(8,9,1);
//		map.set(1,4,1);
//		map.set(4,5,1);
		
//		for (int ii=0; ii<10; ii++) {
//			Iterable<Point2D> neighbors = map.getNeighbors(new Point2D(ii,1));
//			
//			for (Point2D i : neighbors) {
//				System.out.println("neighbor: " + i);
//			}
//		}
		ArrayList<Point2D> cpath = new ArrayList<Point2D>();
		pf.generatePath(new Point2D(0,0), new Point2D(9,5));
//		System.out.println("correct path: " + pf.getPath());
		for (Edge e : pf.getConsideredEdges() ) {
			System.out.println("considered edge: " + e.getP1() + " , " + e.getP2());
			cpath.add(e.getP1());
			cpath.add(e.getP2());
		}
		
		ArrayList<Point2D> path = pf.getPath();
		
		for (int y=0; y<10; y++) {
			System.out.println();
			for (int x=0; x<10; x++) {
				if (path.contains(new Point2D(x,y))) { //(x,y is in path considered)
					System.out.print("0 ");
				}
				else if (cpath.contains(new Point2D(x, y)))
					System.out.print("+ ");
				else
					if (map.getValue(x, y)==0)
						System.out.print("_ ");//map.getValue(x, y) + " ");
					else
						System.out.print("| ");
			}
		}
		
//		assertEquals(0, new Point2D(1,0));
	}

}
