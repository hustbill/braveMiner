package miner;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Point;  
import java.util.LinkedList;  
import java.util.Vector;

/*
 * Tile:   definition of each tile 
 * @author:  Hua Zhang
 * 
 * Version History: 
 * Initial Date:     Oct 5, 2014 
 * Revised Date: 	 Oct 10, 2014
 * Alpha version:    Oct 30, 2014 
 */
public class Tile  {
	public static int SIZE = 1;
	public boolean validPos;

	public int X;
	public int Y;
	public Vector position =new Vector(X, Y);
	public int myValue; // the default is brick
	public int costFromStart;
	public int costToObject;
	public Tile parentTile;

	public final static int EMPTY = 0;
	public final static int BRICK = 1;
	public final static int GRASS = 2;
	public final static int TREE = 3;
	public final static int BONUS = 5;
	public final static int JEWEL = 6;
	public final static int MINER = 7;
	public final static int CYCLOPS = 8;
	public final static int GHOST_BLUE =4;
	public final static int GHOST_RED = 9;
	public final static int GHOST_PINK = 10;
	

	public int entityType = 1;

	public Tile(int x, int y, int val, int type) {
		X = x; // column
		Y = y; // row
		myValue = val;
		entityType = type;

	}

	public int getX() {
		return X;
	}

	public int getY() {
		return Y;
	}

	public int getValue() {
		return myValue;
	}

	public int getType() {
		return entityType;
	}

	public void setType(int type) {
		entityType = type;
	}

//	public void setPiece(GamePiece piece) {
//		this.add(piece);
//	}
//
//	private void add(GamePiece piece) {
//		// TODO Auto-generated method stub
//
//	}

	public boolean isValid() {
		return validPos;
	}

	public void setValid(boolean valid) {
		validPos = valid;
	}

	public void print() {
		System.out.print(myValue + " ");
	}

	public  int getCost( Tile t) {
		int m = t.X - X;
		int n = t.Y - Y;
		return (int) Math.sqrt( m*m + n* n);
	}

	  public boolean equals(Object tile) {  
	    if (X == ((Tile) tile).X && Y == ((Tile) tile).Y) {  
	      return true;  
	    }  
	    return false;  
	  }  
	  
	  // compare two Tiles to get one which has the smallest cost
	  public int compareTo(Object obj ) {  
	    int a1 = costFromStart + costToObject;  
	    int a2 = ((Tile) obj).costFromStart + ((Tile) obj).costToObject;  
	    if (a1 < a2) {  
	      return -1;  
	    } else if (a1 == a2) {  
	      return 0;  
	    } else {  
	      return 1;  
	    }  
	  }  
	  
	  public LinkedList<Tile> getNeighbors() {  
	    LinkedList<Tile> neighbors = new LinkedList<Tile>();  
	    int x = X;  
	    int y = Y;  
	    // Up 
	    neighbors.add(new Tile(x, y - 1, 1, 4)); // ghostColor=4, blue ghost  
		// Down
	    neighbors.add(new Tile(x, y + 1 , 1, 4));  
	    // Left
	    neighbors.add(new Tile(x - 1, y, 1, 4));  
	    // Right
	    neighbors.add(new Tile(x + 1, y, 1, 4));  
	    return neighbors;  
	  }  
	
	@Override
	public String toString() {
		return "Tile [X=" + X + ", Y=" + Y + "]";
	}


}
