package miner;

/**
 * PathFinder class is a basic implementation of the A* Algorithm.
 * Ref1: http://www.raywenderlich.com/4946/introduction-to-a-pathfinding
 * Ref2 : https://github.com/Tadimsky/A--Pathfinding
 * 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import java.awt.Point;  
import java.util.LinkedList;  
 

public class PathFinder {  
  private OpenList openList;  
  // completed list
  private LinkedList<Tile> closedList;  
  private int[][] map;  

  private int[] neighborList;  
 public PathFinder(int[][] tileMap, int[] neighbors) {  
    map = tileMap;  
    neighborList = neighbors;  
    openList = new OpenList();  
    closedList = new LinkedList<Tile>();  
  }  
  public List<Tile> searchPath( Tile startTile,  Tile objectTile) {  
    startTile.costFromStart = 0;  
    startTile.costToObject = startTile.getCost(objectTile);  
    startTile.parentTile = null;  
    openList.add(startTile);  
    while (!openList.isEmpty()) {  
    	// remove the initial item
      Tile firstTile = (Tile) openList.removeFirst();  
      if (firstTile.equals(objectTile)) { 
    	  // make the path routine
        return makePath(firstTile);  
      } else {  
        closedList.add(firstTile);
        // get the neighbor tiles
        LinkedList<Tile> neighborList = firstTile.getNeighbors();  
        for (int i = 0; i < neighborList.size(); i++) {  
          Tile neighborTile = (Tile) neighborList.get(i);  
          boolean isOpen = openList.contains(neighborTile);  
          boolean isClosed = closedList.contains(neighborTile);
          //  check the tile can be pass or not
          boolean isHit = isHit(neighborTile.X, neighborTile.Y);  
          if (!isOpen && !isClosed && !isHit) {  
            neighborTile.costFromStart = firstTile.costFromStart + 1;  
            neighborTile.costToObject = neighborTile.getCost(objectTile);  
            neighborTile.parentTile = firstTile;  
            openList.add(neighborTile);  
          }  
        }  
      }  
    }  
    openList.clear();  
    closedList.clear();  
    return null;  
  }  

  private boolean isHit(int x, int y) {  
	//  System.out.println(String.format("y=%d x=%d", y , x) );
    for (int i = 0; i < neighborList.length; i++) {  
      if (map[y][x] == neighborList[i]) {  
        return true;  
      }  
    }  
    return false;  
  }  

  private LinkedList<Tile> makePath(Tile Tile) {  
    LinkedList<Tile> path = new LinkedList<Tile>();  
    while (Tile.parentTile != null) {  
      path.addFirst(Tile);  
      Tile = Tile.parentTile;  
    }  
    path.addFirst(Tile);  
    return path;  
  }
  
  private class OpenList extends LinkedList{  
    private static final long serialVersionUID = 1L;  
   
    public void add(Tile Tile) {  
      for (int i = 0; i < size(); i++) {  
        if (Tile.compareTo(get(i)) <= 0) {  
          add(i, Tile);  
          return ;  
        }  
      }  
      addLast(Tile);  
    }  
  }  
}

//package miner;
//
///**
// * PathFinder class is a basic implementation of the A* Algorithm.
// * Ref1: http://www.raywenderlich.com/4946/introduction-to-a-pathfinding
// * Ref2 : https://github.com/Tadimsky/A--Pathfinding
// * 
// */
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//
//public class PathFinder {
//	List<Tile> openSet;
//	List<Tile> closedSet;
//	HashMap<Tile, Tile> comesFrom;
//	HashMap<Tile, Double> g_score;
//	HashMap<Tile, Double> f_score;
//
//	private double Fmax;
//	private double Gmax;
//
//	public PathFinder() {
//
//	}
//
//	private double getMin(HashMap<Tile, Double> f) {
//		double max = Collections.max(f.values(), new Comparator<Double>() {
//			@Override
//			public int compare(Double o1, Double o2) {
//				if (o1 > 1000000) {
//					return -1;
//				}
//				if (o2 > 1000000) {
//					return 1;
//				}
//				return o1.compareTo(o2);
//			}
//		});
//		return max;
//	}
//
//	private double heuristic(Tile start, Tile goal) {
//		if (start == null || goal == null)
//			return 100000;
//		if (start.getValue() > 1) {
//			return 1000000;
//		}
//		return manhattan(start, goal);
//	}
//
//	private double manhattan(Tile start, Tile goal) {
//		int dx = Math.abs(goal.getX() - start.getX());
//		int dy = Math.abs(goal.getY() - start.getY());
//		return Tile.SIZE * (dx + dy);
//	}
//
//	private Tile getLowest(HashMap<Tile, Double> f_score, List<Tile> fromSet) {
//		Iterator<Tile> i = fromSet.iterator();
//		double minValue = Double.MAX_VALUE;
//		Tile minTile = null;
//
//		while (i.hasNext()) {
//			Tile cur = i.next();
//			double val = f_score.get(cur);
//
//			if (val < minValue) {
//				if (cur.getValue() == 1) {
//					minTile = cur;
//					minValue = val;
//				}
//			}
//		}
//		return minTile;
//	}
//
//	public List<Tile> findpath(Map map, int Xs, int Ys, int Xg, int Yg) {
//		Tile start = map.getTile(Xs, Ys);
//		Tile goal = map.getTile(Xg, Yg);
//
//		// Tiles that have been processed
//		closedSet = new ArrayList<Tile>();
//
//		openSet = new ArrayList<Tile>();
//		openSet.add(start);
//
//		// Map what came before the key Tile
//		comesFrom = new HashMap<Tile, Tile>();
//
//		// Distant from start
//		g_score = new HashMap<Tile, Double>();
//
//		// Distant to goal
//		f_score = new HashMap<Tile, Double>();
//
//		// distance from start to start is zero
//		g_score.put(start, 0.0);
//		f_score.put(start, heuristic(start, goal));
//
//		int countLoop = 0;
//
//		while (openSet.size() != 0) {
//			// get lowest fscore
//			countLoop++;
//			Tile current = getLowest(f_score, openSet);
//			if (current == null) {
//				return null;
//			}
//			if (current.getValue() > 1) {
//				openSet.remove(current);
//				continue;
//			}
//			if (current.equals(goal)) {
//				Fmax = getMin(f_score);
//				Gmax = getMin(g_score);
//				System.out.println("\t Generating Path");
//				List<Tile> v = construct_path(comesFrom, goal);
//				System.out.println(countLoop);
//				return v;
//			}
//
//			openSet.remove(current);
//			closedSet.add(current);
//			List<Tile> neighbors = map.getNeighbors(current);
//			for (Tile neighbor : neighbors) {
//				if (neighbor == null) {
//					continue;
//				}
//				double tentative_g_score = g_score.get(current) + Tile.SIZE;
//				if (closedSet.contains(neighbor)) {
//					if (tentative_g_score >= g_score.get(neighbor)) {
//						continue;
//					}
//				}
//				if ((!openSet.contains(neighbor))
//						|| tentative_g_score < g_score.get(neighbor)) {
//					comesFrom.put(neighbor, current);
//					g_score.put(neighbor, tentative_g_score);
//					f_score.put(neighbor,
//							g_score.get(neighbor) + heuristic(neighbor, goal));
//					if (!openSet.contains(neighbor)) {
//						if (!(neighbor.getValue() > 1)) {
//							openSet.add(neighbor);
//						}
//					}
//				}
//			}
//		}
//
//		return null;
//	}
//	
//	private static List<Tile> construct_path(HashMap<Tile, Tile> comesFrom,
//			Tile goal) {
//		List<Tile> path = new ArrayList<Tile>();
//		if (comesFrom.containsKey(goal)) {
//			path.addAll(construct_path(comesFrom, comesFrom.get(goal)));
//			path.add(goal);
//		} else {
//			path.add(goal);
//		}
//		return path;
//	}
//
//}