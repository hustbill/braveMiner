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

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
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

public class Miner extends Entity {

	private Vector velocity;
	private int countdown;
	public final int RED = 0;
	public final int YELLOW = 1;
	public final int BLUE = 2;
	public final int PINK = 3;
	public int x;
	public int y;
	public int tileSize = MinerGame.TileSize;
	public boolean powerUp = false;
	public int count = 0;
	private float vx = 0.0f, vy = 0.0f;

	private Random random;

	private static class ValueComparator implements
			Comparator<Map.Entry<Tile, Integer>> {
		public int compare(Map.Entry<Tile, Integer> m,
				Map.Entry<Tile, Integer> n) {
			return n.getValue() - m.getValue();
		}
	}

	public Miner(final int px, final int py, float vx, float vy) {
		super(px, py);
		addImageWithBoundingBox(MinerGame.minerRpgImg[0][0]);
		// image from
		// http://opengameart.org/content/tmim-heroine-bleeds-game-art
		countdown = 0;
		velocity = new Vector(vx, vy);
		x = px;
		y = py;

		random = new Random();

	}

	public void setPowerup(boolean p) {
		countdown = 6000;
		powerUp = p;
	}

	public boolean getPowerup() {
		return powerUp;
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}

	// lock the ing target
	public Ghost lockTargetGhost(MinerGame bg) {
		Ghost ghost = null;
		int miner_x = (int) bg.miner.getX() / tileSize;
		int miner_y = (int) bg.miner.getY() / tileSize;
		int blue_x = (int) bg.blueGhost.getX() / tileSize;
		int blue_y = (int) bg.blueGhost.getY() / tileSize;
		int red_x = (int) bg.redGhost.getX() / tileSize;
		int red_y = (int) bg.redGhost.getY() / tileSize;
		int pink_x = (int) bg.pinkGhost.getX() / tileSize;
		int pink_y = (int) bg.pinkGhost.getY() / tileSize;

		if (miner_y == blue_y || miner_x == blue_x)
			ghost = bg.blueGhost;
		if (miner_y == red_y || miner_x == red_x)
			ghost = bg.redGhost;
		if (miner_y == pink_y || miner_y == pink_y)
			ghost = bg.pinkGhost;
		return ghost;

	}

	/*
	 * Shot the bullet to kill ghost Miner can the bullet to monster by decrease
	 * its health -0.5, If the monster was hit by two bullets, it will die.
	 */

	public Ghost lockTarget(MinerGame bg, Bullet bt, int delta) {
		Ghost ghost = null;
		if (Math.abs(bg.miner.getY() - bg.blueGhost.getY()) < tileSize
				|| (Math.abs(bg.miner.getX() - bg.blueGhost.getX()) < tileSize)) {
			ghost = bg.blueGhost;
		}
		if (Math.abs(bg.miner.getY() - bg.blueGhost.getY()) < tileSize
				|| (Math.abs(bg.miner.getX() - bg.blueGhost.getX()) < tileSize)) {
			ghost = bg.blueGhost;
		}

		return ghost;
	}

	public void shoot(MinerGame bg, Bullet bt, Ghost ghost, int delta) {
		// System.out.println(" bullet to kill ghost");

		if (Math.abs(bg.miner.getY() - ghost.getY()) < tileSize) {
			bt.setPosition(bg.miner.getX(), ghost.getY());
			if (ghost.getVelocity().getX() > 0 && bt.getX() < ghost.getX()) {
				bt.setVelocity(new Vector(0.9f, 0.0f));
			}
			if (ghost.getVelocity().getX() > 0 && bt.getX() > ghost.getX()) {
				bt.setVelocity(new Vector(-0.9f, 0.0f));
			}
			if (ghost.getVelocity().getX() < 0 && bt.getX() > ghost.getX()) {
				bt.setVelocity(new Vector(-0.9f, 0.0f));
			}
			if (ghost.getVelocity().getX() < 0 && bt.getX() < ghost.getX()) {
				bt.setVelocity(new Vector(0.9f, 0.0f));
			}

		}

		if (Math.abs(bg.miner.getX() - ghost.getX()) < tileSize) {
			bt.setPosition(ghost.getX(), bg.miner.getY());
			// bt.setPosition(ghost.getX(), ghost.getY());
			if (ghost.getVelocity().getY() > 0 && bt.getY() < ghost.getY()) {
				bt.setVelocity(new Vector(0.0f, 0.9f));
			}
			if (ghost.getVelocity().getY() > 0 && bt.getY() > ghost.getY()) {
				bt.setVelocity(new Vector(0.0f, -0.9f));
			}
			if (ghost.getVelocity().getY() < 0 && bt.getY() > ghost.getY()) {
				bt.setVelocity(new Vector(0.0f, -0.9f));
			}
			if (ghost.getVelocity().getY() < 0 && bt.getY() < ghost.getY()) {
				bt.setVelocity(new Vector(0.0f, 0.9f));
			}

		}
		if (bt.getVelocity().getX() > 0) {
			bt.removeImage(ResourceManager.getImage(MinerGame.BULLET_RSC));
			bt.addImageWithBoundingBox(ResourceManager
					.getImage(MinerGame.BULLET_RIGHT_RSC));
		}
		if (bt.getVelocity().getX() < 0) {
			bt.removeImage(ResourceManager.getImage(MinerGame.BULLET_RSC));
			bt.addImageWithBoundingBox(ResourceManager
					.getImage(MinerGame.BULLET_LEFT_RSC));
		}
		if (bt.getVelocity().getY() < 0) {
			bt.removeImage(ResourceManager.getImage(MinerGame.BULLET_RSC));
			bt.addImageWithBoundingBox(ResourceManager
					.getImage(MinerGame.BULLET_UP_RSC));
		}

	}

	public void shootDemo(MinerGame bg, Bullet bt, Ghost ghost, int delta) {
		// System.out.println(" bullet to kill ghost");
		if (Math.abs(bg.miner.getY() - ghost.getY()) < tileSize) {
			bt.setPosition((ghost.getPosition().getX() + bg.miner.getY()) / 2,
					bg.miner.getY());
			if (ghost.getVelocity().getX() > 0 && bt.getX() < ghost.getX()) {
				bt.setVelocity(new Vector(8 * ghost.getVelocity().getX(), 0.0f));
			}
			if (ghost.getVelocity().getX() > 0 && bt.getX() > ghost.getX()) {
				bt.setVelocity(new Vector(-8 * ghost.getVelocity().getX(), 0.0f));
			}
			if (ghost.getVelocity().getX() < 0 && bt.getX() > ghost.getX()) {
				bt.setVelocity(new Vector(8 * ghost.getVelocity().getX(), 0.0f));
			}
			if (ghost.getVelocity().getX() < 0 && bt.getX() < ghost.getX()) {
				bt.setVelocity(new Vector(-8 * ghost.getVelocity().getX(), 0.0f));
			}

		}

		if (Math.abs(bg.miner.getX() - ghost.getX()) < tileSize) {
			bt.setPosition(bg.miner.getX(),
					(ghost.getPosition().getY() + bg.miner.getX()) / 2);
			if (ghost.getVelocity().getY() > 0 && bt.getY() < ghost.getY()) {
				bt.setVelocity(new Vector(0.0f, 8 * ghost.getVelocity().getY()));
			}
			if (ghost.getVelocity().getY() > 0 && bt.getY() > ghost.getY()) {
				bt.setVelocity(new Vector(0.0f, -8 * ghost.getVelocity().getY()));
			}
			if (ghost.getVelocity().getY() < 0 && bt.getY() > ghost.getY()) {
				bt.setVelocity(new Vector(0.0f, 8 * ghost.getVelocity().getY()));
			}
			if (ghost.getVelocity().getY() < 0 && bt.getY() < ghost.getY()) {
				bt.setVelocity(new Vector(0.0f, -8 * ghost.getVelocity().getY()));
			}
			if (bt.getVelocity().getX() > 0) {
				bt.removeImage(ResourceManager.getImage(MinerGame.BULLET_RSC));
				bt.addImageWithBoundingBox(ResourceManager
						.getImage(MinerGame.BULLET_RIGHT_RSC));
			}
			if (bt.getVelocity().getX() < 0) {
				bt.removeImage(ResourceManager.getImage(MinerGame.BULLET_RSC));
				bt.addImageWithBoundingBox(ResourceManager
						.getImage(MinerGame.BULLET_LEFT_RSC));
			}
			if (bt.getVelocity().getY() < 0) {
				bt.removeImage(ResourceManager.getImage(MinerGame.BULLET_RSC));
				bt.removeImage(ResourceManager
						.getImage(MinerGame.BULLET_LEFT_RSC));
				bt.removeImage(ResourceManager
						.getImage(MinerGame.BULLET_RIGHT_RSC));
				bt.addImageWithBoundingBox(ResourceManager
						.getImage(MinerGame.BULLET_UP_RSC));
			}

		}
	}

	/*
	 * Control the velocity and location of miner in playingState
	 */
	public void controlMiner(Input input, MinerGame bg, int delta) {
		float mx = bg.miner.getX();
		float my = bg.miner.getY();
		float x = (60 * delta / 1000.0f) * (float) (Math.sqrt(bg.levels)) / 10;
		float vx = bg.miner.getVelocity().getX();
		float vy = bg.miner.getVelocity().getY();

		if (mx <= MinerGame.TileSize
				|| mx > bg.ScreenWidth - MinerGame.TileSize * 2) {
			vx = 0.0f;
			// System.out.println("arrive the x boundary, vx=0 !");
		}

		if (my <= MinerGame.TileSize
				|| my > bg.ScreenHeight - MinerGame.TileSize * 2) {

			vy = 0.0f;
			// System.out.println("arrive the y boundary, vy=0 !");
		}

		if (input.isKeyDown(Input.KEY_LEFT)) {
			System.out.println("x=" + x);
			// bg.miner.setX(mx -x);
			vx = -x;
			vy = 0;

		}
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			// System.out.println(String.format("RIGHT: col =%d, row =%d",
			// col,row));
			System.out.println("x=" + x);
			// bg.miner.setX(mx +x);
			vx = x;
			vy = 0;

		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			System.out.println("x=" + x);
			// bg.miner.setY(my +x);
			vy = x;
			vx = 0;
		}

		if (input.isKeyDown(Input.KEY_UP)) {
			System.out.println("x=" + x);
			// bg.miner.setY(my - x);
			vy = -x;
			vx = 0;
		}
		Vector v = new Vector(vx, vy);
		bg.miner.setVelocity(v);

	}

	/**
	 * Collision detection between miner and brick
	 * 
	 */
	public void collisionDetect(MinerGame bg, int[][] mazeArray, int delta) {
		float vx = bg.miner.getVelocity().getX();
		float vy = bg.miner.getVelocity().getY();

		float mx = bg.miner.getCoarseGrainedMaxX();
		float my = bg.miner.getCoarseGrainedMaxY();
		int col = (int) bg.miner.getCoarseGrainedMaxX() / tileSize;
		int row = (int) bg.miner.getCoarseGrainedMaxY() / tileSize;

		// System.out.println(String.format("col =%d, row =%d  ", col,row));
		// System.out.println(String.format("mx1 =%d, mx2 =%d mx3=%d ",
		// (int)mx,(int)bg.miner.getCoarseGrainedMinX(),
		// (int) bg.miner.getCoarseGrainedMaxX() ));
		// System.out.println(String.format("my1 =%d, my2 =%d my3=%d ",
		// (int)my,(int )bg.miner.getCoarseGrainedMinY(), (int)
		// bg.miner.getCoarseGrainedMaxY()));
		// int row1= (int) bg.miner.getY() / tileSize;
		// int row2 = (int )bg.miner.getCoarseGrainedMinY() /tileSize;
		// int row3 = (int) bg.miner.getCoarseGrainedMaxY() /tileSize;
		// System.out.println(String.format("row1 =%d, row2 =%d , row3 =%d ",
		// row1,
		// row2, row3));
		// int col1= (int) bg.miner.getX() / tileSize;
		// int col2 = (int )bg.miner.getCoarseGrainedMinX() /tileSize;
		// int col3 = (int) bg.miner.getCoarseGrainedMaxX() /tileSize;
		// System.out.println(String.format("col1 =%d, col2 =%d , col3 =%d ",
		// col1,
		// col2, col3));
		if (vx > 0) { // right
			col = (int) (mx + tileSize / 2) / tileSize;
			if (checkIfMoveOK(mazeArray, row, col) == false) {
				// System.out.println(String.format("Map[%d,%d]=%d", row,
				// col+1, mazeArray[row][col +1]));
				vx = 0.0f;
			}
		}

		if (vx < 0) { // Left
			col = (int) (mx - tileSize / 2) / tileSize;
			// printGameBoard(mazeArray);
			if (checkIfMoveOK(bg.map.entityMaze, row, col) == false) {
				vx = 0.0f;
			}
		}
		if (vy > 0) {
			row = (int) (my + tileSize / 2) / tileSize;
			if (checkIfMoveOK(mazeArray, row, col) == false) {
				vy = 0.0f;
			}
		}
		if (vy < 0) {
			row = (int) (my - tileSize / 2) / tileSize;
			if (checkIfMoveOK(bg.map.entityMaze, row, col) == false) {
				vy = 0.0f;
			}
		}
		Vector v = new Vector(vx, vy);
		bg.miner.setVelocity(v);

	}

	public void printGameBoard(int[][] gameBoard) {

		for (int r = 0; r < 15; r++) {
			for (int c = 0; c < 20; c++) {
				if (gameBoard[r][c] == Tile.BRICK
						|| gameBoard[r][c] == Tile.MINER)
					System.out.print(gameBoard[r][c] + " ");
				else {
					System.out.print(" " + " ");
				}
			}
			System.out.println();
		}
	}

	public void printTileBoard(Tile[][] tileBoard) {

		for (int r = 0; r < 15; r++) {
			for (int c = 0; c < 20; c++) {
				if (tileBoard[r][c].myValue == Tile.BRICK
						|| tileBoard[r][c].myValue == Tile.MINER)
					System.out.print(tileBoard[r][c].myValue + " ");
				else {
					System.out.print(" " + " ");
				}
			}
			System.out.println();
		}
	}

	/*
	 * Check the move is valid or not
	 */
	public boolean checkIfMoveValid(Tile[][] tileBoard, int r, int c) {
		if (r >= 0 && c >= 0 && r < 15 && c < 20) {
			if (tileBoard[r][c].myValue != 1) {
				return true;
			} else { // bricks
				printTileBoard(tileBoard);
				System.out.println(String.format(
						"Cannot Move: tileBoard[%d][%d].myValue=%d", r, c,
						tileBoard[r][c].myValue));
				return false;
			}
		} else {
			System.out.println("Out of the boundry");
			return false;
		}
	}

	/*
	 * Check the move is valid or not
	 */
	public boolean checkIfMoveOK(int[][] gameBoard, int r, int c) {
		if (r >= 0 && c >= 0 && r < 15 && c < 20) {
			if (gameBoard[r][c] != 1) {
				return true;
			} else {
				// printGameBoard(gameBoard);
				System.out.println(String.format(
						"Cannot move to gameBoard[%d][%d]=%d", r, c,
						gameBoard[r][c]));

				return false;

			}
		} else {
			System.out.println("Out of the boundry");
			return false;
		}
	}

	/**
	 * For PlayingState: Update the miner based on how much time has passed...
	 * 
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	public void isPowerUp(MinerGame bg, final int delta) {
		translate(velocity.scale(delta));
		if (countdown > 0) {
			countdown -= delta;
			// System.out.println("countdown=" + countdown + "delta=" + delta);
			if (countdown <= 0) {
				bg.miner.setPowerup(false);
				bg.blueGhost.removeImage(MinerGame.whiteGhostImg[0]);
				bg.redGhost.removeImage(MinerGame.whiteGhostImg[0]);
				bg.pinkGhost.removeImage(MinerGame.whiteGhostImg[0]);
				bg.blueGhost
						.addImageWithBoundingBox(MinerGame.blueGhostImg[0]);
				bg.redGhost.addImageWithBoundingBox(MinerGame.redGhostImg[0]);
				bg.pinkGhost
						.addImageWithBoundingBox(MinerGame.pinkGhostImg[0]);
			}
		}
	}

	/*
	 * Miner is in powerup state, set all ghosts colors to white color. Miner
	 * able to kill the ghost byhand.
	 */
	public void inPowerUp(MinerGame bg) {
		bg.blueGhost.removeImage(MinerGame.blueGhostImg[0]);
		bg.blueGhost.addImageWithBoundingBox(MinerGame.whiteGhostImg[0]);
		bg.redGhost.removeImage(MinerGame.redGhostImg[0]);
		bg.redGhost.addImageWithBoundingBox(MinerGame.whiteGhostImg[0]);
		bg.pinkGhost.removeImage(MinerGame.pinkGhostImg[0]);
		bg.pinkGhost.addImageWithBoundingBox(MinerGame.whiteGhostImg[0]);
	}

	/**
	 * For StartUpState: update the miner based on how much time has passed...
	 * 
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	public void update(final int delta) {
		translate(velocity.scale(delta));
		// if (countdown > 0) {
		// countdown -= delta;
		// if (countdown <= 0) {
		// addImageWithBoundingBox(ResourceManager
		// .getImage(MinerGame.FISH_RSC));
		// removeImage(ResourceManager
		// .getImage(MinerGame.ZOMBIE_RSC));
		// }
		// }
	}

	/**
	 * Bounce the ball off a surface. This simple implementation, combined with
	 * the test used when calling this method can cause "issues" in some
	 * situations. Can you see where/when? If so, it should be easy to fix!
	 * 
	 * @param surfaceTangent
	 */
	public void bounce(float surfaceTangent) {
		// removeImage(ResourceManager.getImage(MinerGame.FISH_RSC));
		// addImageWithBoundingBox(ResourceManager
		// .getImage(MinerGame.ZOMBIE_RSC));
		countdown = 500;
		velocity = velocity.bounce(surfaceTangent);
	}

	/**
	 * make the miner move in circle : right -> down -> left -> up
	 * 
	 * @param MinerGame
	 *            bg
	 */
	public void circleMove(Miner miner) {
		int col = (int) miner.getX() / MinerGame.TileSize;
		int row = (int) miner.getY() / MinerGame.TileSize;
		Vector v = new Vector(0.10f, 0.0f);
		miner.setVelocity(v);
		System.out.println(String.format("row =%d,  col=%d", row, col));

		if (row == 1 || row == 0 || row == 0 && col >= 1 && col < 11) {
			vx = 0.1f; // move right
			vy = 0.0f;

		}
		if (row < 10 && col == 11 || col == 12) {
			vx = 0.0f; // move down
			vy = 0.1f;

		}
		// System.out.println(String.format(
		// "Enter into circle move: row =%d,  col=%d", row, col));
		if (row == 10 && col <= 11) {
			vx = -0.1f; // move left
			vy = 0.0f;

		}

		if (row >= 1 && row <= 11 && col <= 1) {
			vy = -0.1f; // move up
			vx = 0.0f;
		}
		// if ( vx > 0 && vy ==0 ) {
		//
		// addImageWithBoundingBox(ResourceManager
		// .getImage(MinerGame.MINER_RIGHT_RSC));
		// removeImage(ResourceManager
		// .getImage(MinerGame.MONSTER_RSC));
		// removeImage(ResourceManager
		// .getImage(MinerGame.MINER_LEFT_RSC));
		//
		// }
		// if(vx < 0 && vy ==0) {
		// addImageWithBoundingBox(ResourceManager
		// .getImage(MinerGame.MINER_LEFT_RSC));
		// removeImage(ResourceManager
		// .getImage(MinerGame.MONSTER_RSC));
		// removeImage(ResourceManager
		// .getImage(MinerGame.MINER_RIGHT_RSC));
		// }
		//
		// if (vx==0) {
		// addImageWithBoundingBox(ResourceManager
		// .getImage(MinerGame.MINER_RSC));
		// removeImage(ResourceManager
		// .getImage(MinerGame.MINER_RIGHT_RSC));
		//
		// removeImage(ResourceManager
		// .getImage(MinerGame.MINER_LEFT_RSC));
		// }

		v = new Vector(vx, vy);
		miner.setVelocity(v);

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
		int m_col = (int) bg.miner.getX() / MinerGame.TileSize;
		int m_row = (int) bg.miner.getY() / MinerGame.TileSize;
		System.out.println(String.format("Current: m_row =%d,  m_col=%d",
				m_row, m_col));

		if (m_col == col && m_row == row) {
			System.out.println("Current======== Target");
			// bg.miner.setVelocity(new Vector(0.0f, 0.1f));
		} else {
			// bg.map.map[m_row][m_col].setValid(true);
			System.out.println(String.format("Move to: row =%d,  col=%d", row,
					col));
			bg.miner.setX(col * MinerGame.TileSize);
			bg.miner.setY(row * MinerGame.TileSize);
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

				bg.miner.setX(t.getX() * MinerGame.TileSize);
				bg.miner.setY(t.getY() * MinerGame.TileSize);
				// path.remove(0);
			}
		}

		// if (path.size() > 0) {
		// //path.remove(0);
		// bg.miner.setX(path.get(1).X);
		// bg.miner.setY(path.get(1).Y);
		// }
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

	// move the miner in specific path
	public void moveMinerInPath(MinerGame bg, Tile dst) {
		int row = (int) bg.miner.y / MinerGame.TileSize;
		int col = (int) bg.miner.x / MinerGame.TileSize;
		bg.map.map[row][col].setValid(true);
		bg.map.map[row][col].setType(Tile.EMPTY);
		bg.miner.x = dst.X;
		bg.miner.y = dst.Y;
		row = (int) dst.Y / MinerGame.TileSize;
		col = (int) dst.X / MinerGame.TileSize;
		bg.map.map[row][col].setValid(false);
		bg.map.map[row][col].setType(Tile.MINER);

	}

	// move the miner in random direction
	public void move(MinerGame bg) {
		int srcY = (int) bg.miner.getY() / MinerGame.TileSize;
		int srcX = (int) bg.miner.getX() / MinerGame.TileSize;
		System.out.println(String.format("srcY=%d, srcX=%d", srcY, srcX));

		int dstY = srcY;
		int dstX = srcX;

		if (!(dstY < 1 || dstY > 13 || dstX < 1 || dstX > 12)) {
			switch (random.nextInt(4)) {
			case 0:
				dstY = srcY - 1;
				dstX = srcX;
				// bg.miner.setVelocity(new Vector(0.0f, -.1f));
				break;
			case 1:
				dstY = srcY;
				dstX = srcX + 1;
				// bg.miner.setVelocity(new Vector(.1f, 0.0f));
				break;
			case 2:
				dstY = srcY + 1;
				dstX = srcX;
				// bg.miner.setVelocity(new Vector(0.0f, 0.1f));
				break;
			case 3:
				dstY = srcY;
				dstX = srcX - 1;
				// bg.miner.setVelocity(new Vector(-0.1f, 0.0f));
				break;
			}

			if (dstY > 0 && dstX > 0
					&& bg.miner.checkIfMoveValid(bg.map.map, dstY, dstX)) {
				bg.miner.setY(MinerGame.TileSize * (dstY));
				bg.miner.setX(dstX * MinerGame.TileSize);
				bg.miner.setVelocity(new Vector(0.1f, 0.0f));
			}
			// bg.miner.setY(MinerGame.TileSize * (dstY));
			// bg.miner.setX(dstX * MinerGame.TileSize);
		}// while (dstY < 1 || dstY > 13 || dstX < 1 || dstX >12);

	}

	// if( mx < 0 || mx > 13) {
	// //bg.miner.bounce(0);
	// bg.miner.setX(MinerGame.TileSize*(2));
	// bg.miner.setVelocity(new Vector(-0.10f, .0f));
	// System.out.println("bg.miner should bounce to left");
	// }
	//
	// if( my < 0 || my > 11 )
	// bg.miner.bounce(90);
	// // bg.miner.setVelocity(new Vector(0.0f, .02f));
	//
	// //
	// System.out.println(String.format("getRow=%d , getY/MinerGame.TileSize=%d",
	// // bg.miner.getRow(), (int)bg.miner.getY()/MinerGame.TileSize ));
	// //
	// System.out.println(String.format("getCol=%d , getX/MinerGame.TileSize=%d",
	// // bg.miner.getCol(), (int)bg.miner.getX()/MinerGame.TileSize ));
	//
	// // if (bg.miner.getRow() < 12 && bg.miner.getCol() < 12
	// // && bg.miner.getRow() > 0
	// // && bg.miner.getCol()> 0)
	// //
	// if (my > 9 && mx <12) {
	// if (bg.miner.checkIfMoveValid(bg.map.map, my , mx-1)) {
	// // bg.miner.setY(MinerGame.TileSize*(my-4));
	//
	// bg.miner.setVelocity(new Vector(-0.10f, .0f));
	// System.out.println("Up ");
	// }
	// }
	//
	// if (mx > 0 && my > 0 && mx < 12 && my < 9) {
	// // if (bg.miner.checkIfMoveValid(bg.gameBoard, bg.miner.getRow() +
	// // 1,
	// // bg.miner.getCol())) {
	// // bg.miner.setRow(my);
	// // bg.miner.setVelocity(new Vector(0.0f, .2f));
	// // System.out.println("Down ");
	// // }
	// //
	// if (bg.miner.checkIfMoveValid(bg.map.map, my + 1, mx)) {
	// bg.miner.setY(MinerGame.TileSize*(my + 1));
	// bg.miner.setVelocity(new Vector(0.0f, .051f));
	// System.out.println("Down ");
	// }
	//
	// // if (bg.miner.checkIfMoveValid(bg.gameBoard, bg.miner.getRow() -
	// // 1,
	// // bg.miner.getCol())) {
	// // // bg.miner.moveEntity(bg.miner, bg.gameBoard, bg.miner.getRow()
	// // + 1,
	// // // bg.miner.getCol());
	// // // bg.miner.getRow() = bg.miner.getRow() + 1;
	// //
	// System.out.println(String.format("getRow=%d , getY/MinerGame.TileSize=%d",
	// // bg.miner.getRow(), (int)bg.miner.getY()/MinerGame.TileSize ));
	// // bg.miner.setRow(my);
	// // bg.miner.setVelocity(new Vector(0.0f, -.1f));
	// // System.out.println("Up ");
	// // }
	//
	// if (bg.miner.checkIfMoveValid(bg.map.map, my, mx + 1)) {
	// bg.miner.setX(MinerGame.TileSize*(mx + 1));
	// bg.miner.setVelocity(new Vector(.051f, 0.0f));
	// System.out.println("Right");
	// }
	//
	// // if (bg.miner.checkIfMoveValid(bg.gameBoard, my,
	// // mx - 1)) {
	// // bg.miner.setCol(mx-1);
	// // bg.miner.setVelocity(new Vector(-.1f, 0.0f));
	// // System.out.println("Left" + bg.miner.getCol());
	// // }
	//
	// // if (bg.miner.checkIfMoveValid(bg.gameBoard, bg.miner.getRow(),
	// // bg.miner.getCol() + 1)) {
	// // bg.miner.setCol(mx);
	// // bg.miner.setVelocity(new Vector(.3f, 0.0f));
	// // System.out.println("Right");
	// // }
	//
	// // if (bg.miner.checkIfMoveValid(bg.gameBoard, bg.miner.getRow(),
	// // bg.miner.getCol() - 1)) {
	// // bg.miner.setCol(mx );
	// // bg.miner.setVelocity(new Vector(-.3f, 0.0f));
	// // System.out.println("Left" + bg.miner.getCol());
	// // }
	// }
	// else {
	// System.out.println("bounce the miner");
	// //bg.miner.bounce(90);
	// bg.miner.setVelocity(new Vector(0,-bg.miner.getVelocity().getY() ));
	// System.out.println("miner.vx="+ bg.miner.getVelocity().getX());
	// System.out.println("miner.vy="+ bg.miner.getVelocity().getY());
	// }

	// public void move(MinerGame bg) {
	//
	// // move the miner ...
	//
	//
	// int my = (int) bg.miner.getY() / MinerGame.TileSize;
	// int mx = (int) bg.miner.getX() / MinerGame.TileSize;
	//
	// // path= [Tile [X=9, Y=7], Tile [X=7, Y=10], Tile [X=11, Y=7], Tile
	// // [X=6, Y=11], Tile [X=11, Y=5], Tile [X=4, Y=11], Tile [X=10, Y=4],
	// // Tile [X=3, Y=10], Tile [X=10, Y=2], Tile [X=2, Y=9]]
	//
	//
	//
	// System.out.println(String.format("my=%d, mx=%d", my, mx));
	//
	// if (bg.miner.getCoarseGrainedMinY() < 0.0f
	// || bg.miner.getCoarseGrainedMaxY() > bg.ScreenHeight -
	// MinerGame.TileSize.0f
	// || bg.miner.getCoarseGrainedMaxX() > bg.ScreenWidth -
	// MinerGame.TileSize.0f
	// || bg.miner.getCoarseGrainedMinX() < -19.0f ) {
	// bg.miner.bounce(90);
	//
	// }
	//
	// if( mx < 0 || mx > 13) {
	// //bg.miner.bounce(0);
	// bg.miner.setX(MinerGame.TileSize*(2));
	// bg.miner.setVelocity(new Vector(-0.10f, .0f));
	// System.out.println("bg.miner should bounce to left");
	// }
	//
	// if( my < 0 || my > 11 )
	// bg.miner.bounce(90);
	// // bg.miner.setVelocity(new Vector(0.0f, .02f));
	//
	// //
	// System.out.println(String.format("getRow=%d , getY/MinerGame.TileSize=%d",
	// // bg.miner.getRow(), (int)bg.miner.getY()/MinerGame.TileSize ));
	// //
	// System.out.println(String.format("getCol=%d , getX/MinerGame.TileSize=%d",
	// // bg.miner.getCol(), (int)bg.miner.getX()/MinerGame.TileSize ));
	//
	// // if (bg.miner.getRow() < 12 && bg.miner.getCol() < 12
	// // && bg.miner.getRow() > 0
	// // && bg.miner.getCol()> 0)
	// //
	// if (my > 9 && mx <12) {
	// if (bg.miner.checkIfMoveValid(bg.map.map, my , mx-1)) {
	// // bg.miner.setY(MinerGame.TileSize*(my-4));
	//
	// bg.miner.setVelocity(new Vector(-0.10f, .0f));
	// System.out.println("Up ");
	// }
	// }
	//
	// if (mx > 0 && my > 0 && mx < 12 && my < 9) {
	// // if (bg.miner.checkIfMoveValid(bg.gameBoard, bg.miner.getRow() +
	// // 1,
	// // bg.miner.getCol())) {
	// // bg.miner.setRow(my);
	// // bg.miner.setVelocity(new Vector(0.0f, .2f));
	// // System.out.println("Down ");
	// // }
	// //
	// if (bg.miner.checkIfMoveValid(bg.map.map, my + 1, mx)) {
	// bg.miner.setY(MinerGame.TileSize*(my + 1));
	// bg.miner.setVelocity(new Vector(0.0f, .051f));
	// System.out.println("Down ");
	// }
	//
	// // if (bg.miner.checkIfMoveValid(bg.gameBoard, bg.miner.getRow() -
	// // 1,
	// // bg.miner.getCol())) {
	// // // bg.miner.moveEntity(bg.miner, bg.gameBoard, bg.miner.getRow()
	// // + 1,
	// // // bg.miner.getCol());
	// // // bg.miner.getRow() = bg.miner.getRow() + 1;
	// //
	// System.out.println(String.format("getRow=%d , getY/MinerGame.TileSize=%d",
	// // bg.miner.getRow(), (int)bg.miner.getY()/MinerGame.TileSize ));
	// // bg.miner.setRow(my);
	// // bg.miner.setVelocity(new Vector(0.0f, -.1f));
	// // System.out.println("Up ");
	// // }
	//
	// if (bg.miner.checkIfMoveValid(bg.map.map, my, mx + 1)) {
	// bg.miner.setX(MinerGame.TileSize*(mx + 1));
	// bg.miner.setVelocity(new Vector(.051f, 0.0f));
	// System.out.println("Right");
	// }
	//
	// // if (bg.miner.checkIfMoveValid(bg.gameBoard, my,
	// // mx - 1)) {
	// // bg.miner.setCol(mx-1);
	// // bg.miner.setVelocity(new Vector(-.1f, 0.0f));
	// // System.out.println("Left" + bg.miner.getCol());
	// // }
	//
	// // if (bg.miner.checkIfMoveValid(bg.gameBoard, bg.miner.getRow(),
	// // bg.miner.getCol() + 1)) {
	// // bg.miner.setCol(mx);
	// // bg.miner.setVelocity(new Vector(.3f, 0.0f));
	// // System.out.println("Right");
	// // }
	//
	// // if (bg.miner.checkIfMoveValid(bg.gameBoard, bg.miner.getRow(),
	// // bg.miner.getCol() - 1)) {
	// // bg.miner.setCol(mx );
	// // bg.miner.setVelocity(new Vector(-.3f, 0.0f));
	// // System.out.println("Left" + bg.miner.getCol());
	// // }
	// }
	// // else {
	// // System.out.println("bounce the miner");
	// // //bg.miner.bounce(90);
	// // bg.miner.setVelocity(new Vector(0,-bg.miner.getVelocity().getY() ));
	// // System.out.println("miner.vx="+ bg.miner.getVelocity().getX());
	// // System.out.println("miner.vy="+ bg.miner.getVelocity().getY());
	// // }
	// }

	// public void moveEntity(Miner miner, GameTile[][] gameBoard, int r, int c)
	// {
	//
	// gameBoard[miner.row][miner.col].setValid(true);
	// // miner.row = r;
	// // miner.col = c;
	// miner.setX(c * MinerGame.TileSize.0f);
	// miner.setY(r * MinerGame.TileSize.0f);
	// gameBoard[r][c].setValid(false); // put the miner in new tile
	// System.out.println("Move entity to: \t r= " + r + ", c= " + c);
	// }

	/*
	 * 
	 */
	// public void printGameBoard(GameTile[][] gameBoard) {
	//
	// for (int row = 0; row < 13; row++) {
	// for (int col = 0; col < 13; col++) {
	// System.out.print(String.format("[%d][%d]=(%d, %d, %d, %c) ",
	// row, col, gameBoard[row][col].row,
	// gameBoard[row][col].col, gameBoard[row][col].myValue,
	// gameBoard[row][col].getType()));
	// }
	// System.out.println();
	// }
	// }

	// public void getNextMove(Miner miner, GameTile[][] gb) {
	// // System.out.println("Get next move - miner!");
	// // System.out.println("gameBoard.length = " + gb.length);
	//
	// int row = miner.row;
	// int col = miner.col;
	//
	// if (row == 0) {
	// moveEntity(miner, gb, row + 1, col);
	// miner.row = row + 1;
	// }
	// if (row == 12) {
	// moveEntity(miner, gb, row - 1, col);
	// miner.row = row - 1;
	// }
	//
	// if (col == 0) {
	// moveEntity(miner, gb, row, col + 1);
	// miner.col = col + 1;
	// }
	// if (col == 12) {
	// moveEntity(miner, gb, row, col - 1);
	// miner.col = col - 1;
	// }
	// if (row < 12 && col < 12 && row > 0 && col > 0) {
	// System.out.println("Begin getNextMove \trow=" + row + "\tcol="
	// + col);
	// // printGameBoard(gb);
	//
	// // Down
	// if (checkIfMoveValid(gb, row + 1, col)) {
	// moveEntity(miner, gb, row + 1, col);
	// miner.row = row + 1;
	// System.out.println("Down ");
	// }
	//
	// // Up
	// if (checkIfMoveValid(gb, row - 1, col)) {
	// moveEntity(miner, gb, row - 1, col);
	// miner.row = row - 1;
	// System.out.println("UP ");
	// }
	//
	// // left
	// if (checkIfMoveValid(gb, row, col - 1)) {
	// moveEntity(miner, gb, row, col - 1);
	// miner.col = col - 1;
	// System.out.println("Left ");
	// }
	// // right
	// if (checkIfMoveValid(gb, row, col + 1)) {
	// moveEntity(miner, gb, row, col + 1);
	// miner.col = col + 1;
	// System.out.println("Right  ");
	// }
	// System.out.println(" End getNextMove \t row= " + miner.row
	// + ", col= " + miner.col);
	// }
	//
	// }

	// public GameTile getGameTile(MinerGame bg, int x, int y) {
	// if (x >= 0 && y >= 0) {
	// // if (x < width && y < height) {
	// if (x < 13 && y < 13) {
	// // return map[y][x];
	// return bg.gameBoard[y][x];
	// }
	// }
	// return null;
	// }

	//
	// public List<GameTile> getNeighbors(MinerGame bg, GameTile current) {
	// List<GameTile> lists = new ArrayList<GameTile>();
	// // int x = current.getX();
	// // int y = current.getY();
	// int x = current.col;
	// int y = current.row;
	// lists.add(getGameTile(bg, x - 1, y));
	// lists.add(getGameTile(bg, x + 1, y));
	// lists.add(getGameTile(bg, x, y - 1));
	// lists.add(getGameTile(bg, x, y + 1));
	// return lists;
	// }

	// public void moveMiner(MinerGame bg, final int delta) {
	//
	// // translate(velocity.scale(delta));
	// // if (countdown > 0) {
	// // countdown -= delta;
	// //
	// // if (countdown <= 0) {
	// // System.out.println("Enter update the step3, countdown = "
	// // + countdown);
	// // test AI
	// AI ai = new AI();
	// GameTile current = new GameTile(bg.miner.getRow(), bg.miner.getCol(), 1,
	// 'M');
	// List<GameTile> neighbors = getNeighbors(bg, current);
	// // check if there are any tile which miner can move to
	// // For multiThread environment, use this to generate the random index
	// // Refer to
	// // http://www.mkyong.com/java/java-return-a-random-item-from-a-list/
	//
	// for (GameTile t : neighbors) {
	// System.out.println(String.format(
	// " t=(%d,%d) ", t.row, t.col));
	//
	// if (checkIfMoveValid(bg.gameBoard, t.row, t.col) ==false) {
	//
	// System.out.println(String.format(
	// "Remove t(%d,%d) from neighbors", t.row, t.col));
	//
	// //neighbors.remove(t);
	// }
	// }
	// if (neighbors != null) {
	// int index = ThreadLocalRandom.current().nextInt(neighbors.size());
	// if (index > 0) {
	// GameTile tile = neighbors.get(index);
	// //System.out.println("tile =" + neighbors.get(index));
	// if (tile != null && (checkIfMoveValid(bg.gameBoard, tile.row, tile.col)
	// ==true)) {
	// System.out.println(String.format(
	// "tile(index) = (%d, %d)", tile.row, tile.col));
	// // if(bg.miner.checkIfMoveValid(gameBoard, r, c))
	// if (tile.validPos == true && tile.col > 0 && tile.row > 0
	// && tile.col < 12 && tile.row < 12) {
	//
	// bg.gameBoard[bg.miner.row][bg.miner.col].setValid(true);
	// // bg.miner.setX(tile.col * MinerGame.TileSize.0f);
	// // bg.miner.setY(tile.row * MinerGame.TileSize.0f);
	// Vector v = setNextVelocity(bg.miner, tile);
	// bg.miner.setVelocity(v);
	//
	// bg.gameBoard[tile.col][tile.row].setValid(false);
	// // put the miner in new tile
	// System.out
	// .println(String.format(
	// "Finding path for (%d,%d), (%d,%d): ",
	// bg.miner.row, bg.miner.col, tile.row,
	// tile.col));
	// bg.miner.setCol(tile.col);
	// bg.miner.setRow(tile.row);
	// }
	// }
	// }
	// }
	// }

	// }
	// }

	// /*
	// * set next Velocity for the miner
	// */
	// public Vector setNextVelocity(Miner miner, GameTile tile) {
	// Vector v = null;
	// float vx = 0.0f, vy = 0.0f;
	// if (miner.row < tile.row)
	// vx = 0.4f;
	// if (miner.row > tile.row)
	// vx = -0.4f;
	// if (miner.col < tile.col)
	// vy = 0.4f;
	// if (miner.col > tile.col)
	// vy = -0.4f;
	// v = new Vector(vx, vy);
	// return v;
	// }

	/*
	 * Configure the miner: location, shape, image
	 */
	public void configminer(StateBasedGame game, int levels) {
		MinerGame bg = (MinerGame) game;
		System.out.println("levels= " + levels);
		bg.miner.setX(200.0f); // set the position and velocity of miner
		bg.miner.setY(360.0f);
		switch (levels) {
		case 1:
			bg.miner.scale(1.0f);
			break;
		case 2:
			bg.miner.scale(.95f);
			break;
		case 3:
			bg.miner.scale(.90f);
			break;
		case 4:
			bg.miner.scale(.85f);
			break;

		default:
			bg.miner.scale(.90f);
			break;
		}
	}
}