package edu.moravian.tests;

import java.lang.reflect.Array;
import java.util.ArrayList;
import edu.moravian.Math.Point2D;
import edu.moravian.tests.GameMapInterface;

public class GameMapMock implements GameMapInterface {
	
	int[][] map = new int[10][10];
	
	GameMapMock() {
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				map[i][j] = 0;
			}
		}
	}

	public Iterable<Point2D> getNeighbors(Point2D p){
		
		ArrayList<Point2D> neighbors = new ArrayList<Point2D>();
		
		int x = (int)p.getX();
		int y = (int)p.getY();
		
		//search a specific (x,y) position to determine if there is an object present,
		//	if there is NOT then add it to the list of neighbors.
		
		for(int i=x-1; i < x+2; i++) {
			for(int j=y-1; j < y+2; j++) {
				//SURROUND BY IF THAT CHECKS FOR -1 & 100
				if (i >= 0 && i < 10) {//map.length) {//i != 11 && i != 10 && i != -1) {
					if (j >= 0 && j < 10) {//map.length) {//j != 11 && j != 10 && j != -1) {
						if (map[i][j] == 0) { 
							if (i!=x || j!=y) {
								neighbors.add(new Point2D(i,j));
							}
						}
					}
				}
			}
		}
		
		return neighbors;
	}
	
	public void set(int index1, int index2, int value) {
		map[index1][index2] = value;
	}
	
	public int getValue(int x, int y) {
		return map[x][y];
	}
}