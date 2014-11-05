package miner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The Miner class is an Entity that has a velocity (since it's moving). User
 * can control a Miner to move left,right, up, and down
 * 
 * @author Hua Zhang
 */

public class Ghost extends Entity {

	private Vector velocity;
	private int countdown;
	public final int RED = 0;
	public final int YELLOW = 1;
	public final int BLUE = 2;
	public final int PINK = 3;
	public int x;
	public int y;
	public int count = 0;
	private float vx = 0.0f, vy = 0.0f;

	public int ghostColor = Tile.GHOST_BLUE;
	public int tileSize = MinerGame.TileSize;
	

	
	private Random random;

	private PathFinder astar;

	private static class ValueComparator implements
			Comparator<Map.Entry<Tile, Integer>> {
		public int compare(Map.Entry<Tile, Integer> m,
				Map.Entry<Tile, Integer> n) {
			return n.getValue() - m.getValue();
		}
	}

	public Ghost(final int px, final int py, float vx, float vy, int ghost_color) {
		super(px, py);
		ghostColor = ghost_color;
		
//		for(int i=0 ; i< 9; i++ ) {
//			for(int j =0; j <10; j++ ) {
//				ghostImgs[i][j] = ss.getSubImage(i, j);
//				ghostImgs[i][j].draw(i*tileSize, j*tileSize);
//			}
//		}

		
		//
		switch (ghostColor) {
		case Tile.GHOST_BLUE:
			addImageWithBoundingBox(MinerGame.blueGhostImg[0]);
			break;
		case Tile.GHOST_RED:
			addImageWithBoundingBox(MinerGame.redGhostImg[0]);
			//addImageWithBoundingBox(ResourceManager.getImage(MinerGame.RED_RSC));
			break;
		case Tile.GHOST_PINK:
			addImageWithBoundingBox(MinerGame.pinkGhostImg[0]);
//			addImageWithBoundingBox(ResourceManager
//					.getImage(MinerGame.PINK_RSC));
			break;
		default:
			addImageWithBoundingBox(MinerGame.blueGhostImg[0]);
			break;
		}
		countdown = 0;
		velocity = new Vector(vx, vy);
		x = px;
		y = py;
		random = new Random();

	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}

	// Set target Tile for ghost in random
	public Tile setTargetInRandom(MinerGame bg, Tile startTile) {
		Tile targetTile;
		int max_col = bg.map.entityMaze.length;
		int max_row = bg.map.entityMaze[0].length;
		System.out.println(String.format("max_col=%d,  max_row=%d", max_col,
				max_row));
		// find the target tile in random
		int i = ThreadLocalRandom.current().nextInt(2, max_col - 2);
		int j = ThreadLocalRandom.current().nextInt(2, max_row - 2);
		System.out.println("i= " + i + "j= " + j);

		// targetTile = new Tile(i, j, 1, 7);
		targetTile = new Tile(12, 12, 1, 7);
		//
		return targetTile;

	}
	
	/* 
	 * when ghost dies, it follows a 'fastest' path back to the spawn point to restart
	 */
	public void back2Spawn(MinerGame bg,  Ghost ghost) {
		System.out.println("died ghost follows a 'fastest' path back to the spawn point to restart ");
		// a spawn point tile
		Tile spawnTile = new Tile( 1, 2, 1, ghost.ghostColor);
		int row = (int) ghost.getY() / MinerGame.TileSize;
		int col = (int) ghost.getX() / MinerGame.TileSize;
		// System.out.println("row=" + row + "col=" + col);
		Tile startTile = new Tile(col, row, 1, ghost.ghostColor);
		Vector v = new Vector(0.0f, 0.1f);
		Tile current = bg.map.getTile(row, col);

		// System.out.println(String.format(" Enety to startMove DST: row =%d,  col=%d",
		// (int) current.Y, (int) current.X));
		List<Tile> neibors = bg.map.getNeighbors(current);
		System.out.println(String.format(" neibors =%s", neibors.toString()));

		Tile dst =spawnTile;

		Tile t = findNearestTile(bg, neibors, dst);
		if (t != null && t != current) {
			System.out.println(String.format("Next step: row =%d,  col=%d",
					t.X, t.Y));
			System.out.println("t.X, t.Y =" + t.toString());
			v = moveStep(bg, t.X, t.Y);
		}
	}
	

	/*
	 * Make the redGhost move in A star path finding
	 */
	public void findPath(MinerGame bg, Ghost ghost) {
		int row = (int) ghost.getY() / MinerGame.TileSize;
		int col = (int) ghost.getX() / MinerGame.TileSize;
		// System.out.println("row=" + row + "col=" + col);
		Tile startTile = new Tile(col, row, 1, 7);
		Tile objectTile = new Tile(18, 13, 1, 7);
		if (startTile.equals(objectTile)) {
			int max_col = bg.map.entityMaze.length;
			int max_row = bg.map.entityMaze[0].length;

			int i = ThreadLocalRandom.current().nextInt(1, 5);
			int j = ThreadLocalRandom.current().nextInt(1, 6);
			Tile targetTile = new Tile(i, j, 1, 7);
			if (objectTile.equals(targetTile)) {
				// move the ghost to original position
				ghost.setX(1 * MinerGame.TileSize);
				ghost.setY(2 * MinerGame.TileSize);
			} else {
				// move the ghost to a random position
				ghost.setX(i * MinerGame.TileSize);
				ghost.setY(j * MinerGame.TileSize);
			}
			// ... 3rd option:  move the ghost towards the miner's position
		}
		List<Tile> path;
		astar = new PathFinder(bg.map.entityMaze, bg.map.HIT);
		path = astar.searchPath(startTile, objectTile);
		// System.out.println("path = " + path.toString());
		if (path != null && path.size() > 1) {
			Tile t = path.get(1);
			if (t.X == col && row < t.Y) {
				ghost.setVelocity(new Vector(0.0f, 0.08f));
			}
			if (t.X == col && row > t.Y) {
				ghost.setVelocity(new Vector(0.0f, -0.08f));
			}
			if (t.Y == row && col < t.X) {
				ghost.setVelocity(new Vector(0.08f, 0.0f));
			}
			if (t.Y == row && col > t.X) {
				ghost.setVelocity(new Vector(-0.08f, 0.0f));
			}
			path.remove(1); // move to next step
			// System.out.println("t.X = " + t.X + "t.Y= " + t.Y);
		}
	}


	/**
	 * Update the blueGhost based on how much time has passed...
	 * 
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	public void update(MinerGame bg, final int delta) {
		translate(velocity.scale(delta));
	}

	/**
	 * make the blueGhost move in circle : right -> down -> left -> up
	 * 
	 * @param MinerGame
	 *            bg
	 */
	public void circleMove(Ghost ghost) {
		int col = (int) ghost.getX() / MinerGame.TileSize;
		int row = (int) ghost.getY() / MinerGame.TileSize;
		Vector v = new Vector(0.10f, 0.0f);
		if (row == 1 || row == 0 && col > 0 && col < 11) {
			vx = 0.08f; // move right
			vy = 0.0f;
		}
		if (row < 10 && col == 11 || col == 12) {
			vx = 0.0f; // move down
			vy = 0.08f;
		}
		// System.out.println(String.format(
		// "Enter into circle move: row =%d,  col=%d", row, col));
		if (row == 10 && col <= 11) {
			vx = -0.08f; // move left
			vy = 0.0f;

		}

		if (row >= 1 && row <= 11 && col <= 1) {
			vy = -0.08f; // move up
			vx = 0.0f;
		}
//		if (vx > 0 && vy == 0) {
//
//			addImageWithBoundingBox(ResourceManager
//					.getImage(MinerGame.BLUE_RIGHT_RSC));
//			removeImage(ResourceManager.getImage(MinerGame.BLUE_RSC));
//			removeImage(ResourceManager.getImage(MinerGame.BLUE_LEFT_RSC));
//
//		}
//		if (vx < 0 && vy == 0) {
//			addImageWithBoundingBox(ResourceManager
//					.getImage(MinerGame.BLUE_LEFT_RSC));
//			removeImage(ResourceManager.getImage(MinerGame.BLUE_RSC));
//			removeImage(ResourceManager.getImage(MinerGame.BLUE_RIGHT_RSC));
//		}
//		if (vx == 0) {
//			addImageWithBoundingBox(ResourceManager
//					.getImage(MinerGame.BLUE_RSC));
//			removeImage(ResourceManager.getImage(MinerGame.BLUE_RIGHT_RSC));
//
//			removeImage(ResourceManager.getImage(MinerGame.BLUE_LEFT_RSC));
//		}

		v = new Vector(vx, vy);
		ghost.setVelocity(v);

	}

	public List<Tile> updatePath(Map map, List<Tile> p) {
		List<Tile> retList = null;
		Tile first = null;
		Tile end = null;
		System.out.println("p= " + p.toString());

		if (p != null) {
			first = p.get(1);
			end = p.get(p.size() - 1);

			System.out.println(String.format("first=(%d, %d), end= (%d, %d)",
					first.X, first.Y, end.X, end.Y));

		}
		// retList = pathFinder.findpath(map, first.X, first.Y, end.X, end.Y);

		return retList;

	}

	public Vector moveStep(MinerGame bg, int row, int col) {
		Vector v = new Vector(0.0f, 0.0f);
		int m_col = (int) bg.miner.getX() / tileSize;
		int m_row = (int) bg.miner.getY() / tileSize;
		System.out.println(String.format("Current: m_row =%d,  m_col=%d",
				m_row, m_col));

		if (m_col == col && m_row == row) {
			System.out.println("Current======== Target");
			// bg.miner.setVelocity(new Vector(0.0f, 0.1f));
		} else {
			// bg.map.map[m_row][m_col].setValid(true);
			System.out.println(String.format("Move to: row =%d,  col=%d", row,
					col));
			bg.blueGhost.setX(col *tileSize);
			bg.blueGhost.setY(row * tileSize);
			bg.map.map[row][col].setValid(false);
		}

		return v;
	}

	public void moveMiner(MinerGame bg, List<Tile> path) {
		// path= [Tile [X=9, Y=7], Tile [X=7, Y=10], Tile [X=11, Y=7], Tile
		// [X=6, Y=11], Tile [X=11, Y=5], Tile [X=4, Y=11], Tile [X=10, Y=4],
		// Tile [X=3, Y=10], Tile [X=10, Y=2], Tile [X=2, Y=9]]
		// pathFinder = new PathFinder();
		//
		// path = pathFinder.findpath(bg.map, 11, 11, 9, 1);
		if (path == null) {
			System.out.println(String.format("No path exists"));
		} else {
			// System.out.println(String.format("Success!"));
			System.out.println("path= " + path.toString());

			System.out.println(String.format("path.size= %d", path.size()));

			if (path.size() > 0) {

				Tile t = path.get(0);
				System.out.println(String.format("t.x =%d,  t.y=%d", t.X, t.Y));

				bg.miner.setX(t.getX() * 64);
				bg.miner.setY(t.getY() * 64);
				// path.remove(0);
			}
		}

		// if (path.size() > 0) {
		// //path.remove(0);
		// bg.miner.setX(path.get(1).X);
		// bg.miner.setY(path.get(1).Y);
		// }
	}

	/*
	 * Check the move is valid or not
	 */
	// public boolean checkIfMoveValid(GameTile[][] gameBoard, int r, int c) {
	public boolean checkIfMoveValid(Tile[][] gameBoard, int r, int c) {
		System.out.println("tile position is : " + r + ", " + c);
		// printGameBoard(gameBoard);
		if (r > 0 && c > 0 && r < 12 && c < 12
				&& gameBoard[r][c].validPos == true) {
			System.out.println(String.format("gameBoard[%d][%d].validPos=%s",
					r, c, gameBoard[r][c].validPos));
			return true;
		} else
			return false;
	}

	// Find the nearest tile which the distance to target is shortest
	public Tile findNearestTile(MinerGame bg, List<Tile> neibors, Tile d) {
		Tile s = null;
		double distance = 0;
		HashMap<Tile, Integer> distanceMap = new HashMap();

		if (neibors.size() > 0 && neibors != null && d != null) {
			for (Tile t : neibors) {
				if (t != d & t != null) {
					// System.out.println(String.format("d.X =%d, d.Y=%d,  t.x =%d, t.y=%d ",
					// d.X, d.Y, t.X, t.Y));
					if (bg.miner.checkIfMoveValid(bg.map.map, (int) t.X,
							(int) t.Y)) {
						double power = Math.pow(t.X - d.X, 2)
								+ Math.pow(t.Y - d.Y, 2);
						distance = Math.sqrt(power);
						distanceMap.put(t, (int) distance);
					}
				}
			}
			if (distanceMap != null) {
				// sort the distanceMap by distance,
				// get the tile in the shortest distance

				List<Map.Entry<Tile, Integer>> list = new ArrayList<>();
				list.addAll(distanceMap.entrySet());
				ValueComparator vc = new ValueComparator();
				Collections.sort(list, vc);
				for (Iterator<Entry<Tile, Integer>> it = list.iterator(); it
						.hasNext();) {
					System.out.println(it.next());
				}
				// System.out.println("Target Tile = "
				// + list.get(list.size() - 1).getKey());
				if (list.size() > 0)
					s = list.get(list.size() - 1).getKey();

			}
		}

		// Set<Entry<Tile, int>> set = null; //distanceMap.entrySet();

		return s;
	}

}
