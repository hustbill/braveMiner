package miner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/**
 * This state is active when the Game is being played. In this state, sound is
 * turned on, the bounce counter begins at 0 and increases until 10 at which
 * point a transition to the Game Over state is initiated. The user can also
 * control the ball using the WAS & D keys.
 * 
 * Transitions From StartUpState
 * 
 * Transitions To GameOverState
 */
class PlayingState extends BasicGameState {
	int lives = 3;
	int count = 0;
	int eatCount = 0;
	int scores = 0;

	private List<Tile> path;
	private int tileSize = MinerGame.TileSize;
	private float vx = 0.01f, vy = 0.01f;
	private Tile objectTile = new Tile(13, 16, 1, 7);

	// for level2
	public Tile[][] mazeBoard = new Tile[15][20];

	Bullet bt;

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		MinerGame bg = (MinerGame) game;
		if (bg.levels <= 4) {
			lives = 3; // reset the lives to 3 for each level
			ResourceManager.getSound(MinerGame.START_GAME_RSC).play();

		}
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		lives = 3;
		container.setSoundOn(true);
		MinerGame bg = (MinerGame) game;
		for (int i = 0; i < 20; i++) {
			bt = new Bullet((20 + i) * tileSize / 4, (14) * tileSize + 26,
					0.01f, 0.0f);
			bg.bullets.add(bt);
		}

		switch (bg.levels) {
		case 1:
			configTileEntity(bg);
			break;
		case 2:
			configMazeEnity(bg);
			break;
		case 3:
			configIsoEnity(bg);
			break;
		default:
			configTileEntity(bg);
			break;
		}

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		MinerGame bg = (MinerGame) game;

		switch (bg.levels) {
		case 1:
			for (Brick bk : bg.bricks)
				bk.render(g);
			for (Bonus cn : bg.coins)
				cn.render(g);
			bg.blueGhost.render(g);
			bg.redGhost.render(g);
			bg.pinkGhost.render(g);
			bg.miner.render(g);
			break;
		case 2:
			g.drawImage(ResourceManager.getImage(MinerGame.MAZE_BOARD_RSC),
					0.0f, 0.0f);
//			for (Brick bk : bg.bricks)
//				bk.render(g);
			for (Bonus cn : bg.coins)
				cn.render(g);
			bg.monster.render(g);
			bg.miner.render(g);
			break;
		case 3:
			bg.monster.render(g);
			bg.miner.render(g);
			break;
		default:
			bg.miner.render(g);
			break;
		}

		for (Bullet bt : bg.bullets)
			bt.render(g);
		g.drawString("Lives: " + lives, 10, 30);
		g.drawString("Scores: " + scores, 20, bg.ScreenHeight - 25);
		g.drawString("Levels: " + bg.levels, bg.ScreenWidth - 82,
				bg.ScreenHeight - 25);
		for (Bang b : bg.explosions)
			b.render(g);

	}

	/*
	 * Configure the Entity based on maze2.txt: position, entity Type, image
	 */
	public void configMazeEnity(MinerGame bg) {
		// System.out.println("Haha, go to level2 , cross the maze carefully!");
		// bg.miner = new Miner(tileSize * 16, tileSize * 13, .0f, .0f);
		// configTileEntity(bg);
		if (bg.miner == null) {
			bg.miner = new Miner(tileSize * 16, tileSize * 12, .0f, .0f);
		} else {
			bg.miner.setVelocity(new Vector(0.0f, 0.0f));// make the miner still
			bg.miner.setPosition(16 * tileSize, 12 * tileSize); // ready to
		}
		bg.miner.scale(1.00f);

		System.out.println("start configMazeEnity !");
		if (bg.map.map == null)
			System.out.println("mazeBoard is null ");

		int[][] mazeArray = bg.map.entityMaze2;
		for (int row = 0; row < 15; row++) {
			for (int col = 0; col < 20; col++) {
				if (mazeArray[row][col] == 1 // BRICK
				// // || mazeArray[row][col] == 2 // GRASS
				// || mazeArray[row][col] == 3 // TREE	
					){
					mazeBoard[row][col] = new Tile(row, col, 1,
							mazeArray[row][col]);
					mazeBoard[row][col].setValid(false); // brick , tree or
															// grass is in tile
				} else { // character in tile which is able to move
					mazeBoard[row][col] = new Tile(row, col, 0,
							mazeArray[row][col]);
					mazeBoard[row][col].setValid(true);
				}
			}
		}
		if (mazeBoard == null) {
			System.out.println("mazeBoard is null ");
		}
		for (int row = 0; row < mazeBoard.length; row++) {
			for (int col = 0; col < mazeBoard[row].length; col++) {

				switch (mazeBoard[row][col].getType()) {
				case Tile.BRICK:
					bg.brick = new Brick(tileSize * col, tileSize * row, 1);
					bg.bricks.add(bg.brick);
					break;
				case Tile.JEWEL:
					bg.coin = new Bonus(tileSize * col, tileSize * row, 0.1f,
							0, 6);
					bg.coins.add(bg.coin);
					break;
				case Tile.CYCLOPS:
					bg.monster = new Monster(tileSize * col, tileSize * row,
							0.05f, .0f);
					break;
				default: // empty , path

					break;
				}
			}
		}
		System.out.println("End configMazeEnity !");

	}

	/*
	 * Configure the Entity based on maze2.txt: position, entity Type, image
	 */
	public void configIsoEnity(MinerGame bg) {
		// System.out.println("Haha, go to level2 , cross the maze carefully!");
		// bg.miner = new Miner(tileSize * 6, tileSize * 12, .0f, .0f);
		if(bg.monster == null) {
			bg.monster = new Monster(tileSize * 6, tileSize * 7,
					0.05f, .0f);
		} else {
			bg.monster.setVelocity(new Vector(0.0f, 0.0f));// make the monster still
			bg.monster.setPosition(6 * tileSize, 7 * tileSize); // ready to
		}
		if (bg.miner == null) {
			bg.miner = new Miner(tileSize * 8, tileSize * 9, .0f, .0f);
		} else {
			bg.miner.setVelocity(new Vector(0.0f, 0.0f));// make the miner still
			bg.miner.setPosition(8 * tileSize, 9 * tileSize); // ready to
		}
		bg.miner.scale(1.20f);
	}

	/*
	 * Configure the Entity in Tile: position, entity Type, image
	 */
	public void configTileEntity(MinerGame bg) {
		System.out.println("start configEntity !");
		if (bg.map.map == null)
			System.out.println("gameboard is null ");
		Tile[][] gameBoard = bg.map.map;
		if (gameBoard == null) {
			System.out.println("gameboard is null ");
		}
		for (int row = 0; row < gameBoard.length; row++) {
			for (int col = 0; col < gameBoard[row].length; col++) {

				switch (gameBoard[row][col].getType()) {
				case Tile.BRICK:
					bg.brick = new Brick(tileSize * col, tileSize * row, 1);
					bg.bricks.add(bg.brick);
					break;
				case Tile.GRASS:
					bg.brick = new Brick(tileSize * col, tileSize * row, 2);
					bg.bricks.add(bg.brick);
					break;
				case Tile.TREE:
					bg.brick = new Brick(tileSize * col, tileSize * row, 3);
					bg.bricks.add(bg.brick);
					break;
				case Tile.CYCLOPS:
					bg.monster = new Monster(tileSize * col, tileSize * row,
							0.05f, .0f);
					break;
				case Tile.BONUS:
					bg.coin = new Bonus(tileSize * col, tileSize * row, 0.1f,
							0, 5);
					bg.coins.add(bg.coin);
					break;
				case Tile.JEWEL:
					bg.coin = new Bonus(tileSize * col, tileSize * row, 0.1f,
							0, 6);
					bg.coins.add(bg.coin);
					break;
				case Tile.MINER:
					bg.miner = new Miner(tileSize * col, tileSize * row, .0f,
							.0f);
					bg.miner.scale(1.0f); // keep the original size.
					break;
				case Tile.GHOST_RED:
					bg.redGhost = new Ghost(tileSize * col, tileSize * row,
							.0f, 0.1f, Tile.GHOST_RED);
					bg.redGhost.scale(0.20f);
					break;
				case Tile.GHOST_BLUE:
					bg.blueGhost = new Ghost(tileSize * col, tileSize * row,
							.0f, 0.08f, Tile.GHOST_BLUE);
					bg.blueGhost.scale(0.20f);
					break;
				case Tile.GHOST_PINK:
					bg.pinkGhost = new Ghost(tileSize * col, tileSize * row,
							.10f, .0f, Tile.GHOST_PINK);
					bg.pinkGhost.scale(0.20f);
					break;
				default: // empty , path
					// System.out.println(" gameBoard[row][col].getType()=" +
					// gameBoard[row][col].getType());
					// gameBoard[row][col].getType()=0
					break;
				}
			}
		}
		System.out.println("End configEntity !");
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		MinerGame bg = (MinerGame) game;
		switch (bg.levels) {
		case 1:
			update1(container, game, delta);
			break;
		case 2:
			update2(container, game, delta);
			break;
		case 3:
			update3(container, game, delta);
			break;
		default:
			update1(container, game, delta);
			break;
		}
	}

	// update for level3
	public void update3(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input input = container.getInput();
		MinerGame bg = (MinerGame) game;
		for (int i=0; i< 4; i++ ) {
			bg.miner.removeImage(MinerGame.minerRpgImg[i][0]);
		}
		//farmerImg[k]
		bg.miner.removeImage(MinerGame.farmerImg[count % 12]);
		float x = (60 * delta) / 1000.0f;
		// move the player based on the numpad key pressed
		if (input.isKeyDown(Input.KEY_7)) {
			count++;
			System.out.println("move north west");
			bg.miner.setX(bg.miner.getX() - x); // move north west
			bg.miner.setY(bg.miner.getY() - x / 2);
		} else if (input.isKeyDown(Input.KEY_9)) {
			// player.move(TiledSprite.NE); // north east
			bg.miner.setX(bg.miner.getX() + x); // move north west
			bg.miner.setY(bg.miner.getY() - x / 2);
			count++;
		} else if (input.isKeyDown(Input.KEY_3)) {
			// player.move(TiledSprite.SE); // south east
			bg.miner.setX(bg.miner.getX() + x); // move north west
			bg.miner.setY(bg.miner.getY() + x / 2);
			count++;
		} else if (input.isKeyDown(Input.KEY_1)) {
			// player.move(TiledSprite.SW); // south west
			bg.miner.setX(bg.miner.getX() - x); // move north west
			bg.miner.setY(bg.miner.getY() + x / 2);
			count++;
		} else if (input.isKeyDown(Input.KEY_5)) {
			// player.standStill(); // stand still
			bg.miner.setX(bg.miner.getX()); // move north west
			bg.miner.setY(bg.miner.getY());
			count++;
		}
		bg.miner.addImage(MinerGame.farmerImg[count % 12]);
		//bg.miner.addImage(MinerGame.minerRpgImg[count % 4][0]);
		bg.miner.update(delta);

	}

	// update for level2
	public void update2(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input input = container.getInput();
		MinerGame bg = (MinerGame) game;

		float mx = bg.miner.getX();
		float my = bg.miner.getY();
		
		//float mx = bg.miner.getCoarseGrainedMaxX();
		//float my = bg.miner.getCoarseGrainedMaxY();
		int col = (int) bg.miner.getCoarseGrainedMaxX() / tileSize;
		int row = (int) bg.miner.getCoarseGrainedMaxY() / tileSize;

		float x = (60 * delta / 1000.0f);
		//float x = (60 * delta / 1000.0f) * (float) (Math.sqrt(bg.levels)) / 10;
//		float vx = bg.miner.getVelocity().getX();
//		float vy = bg.miner.getVelocity().getY();
		bg.miner.removeImage(MinerGame.minerRpgImg[count % 4][0]);
	

		if (my <= MinerGame.TileSize)
			my = MinerGame.TileSize;
		if (my > bg.ScreenHeight - MinerGame.TileSize * 2) {
			// System.out.println("arrive the y boundary, vy=0 !");
			my = bg.ScreenHeight - MinerGame.TileSize * 2;
		}
		if (input.isKeyDown(Input.KEY_LEFT)) {
			col = (int) (mx - tileSize / 2) / tileSize;
			if (bg.miner.checkIfMoveOK(bg.map.entityMaze2, row, col) == true) {
				bg.miner.setX(mx - x);
			System.out.println(String.format("Left: col =%d, row =%d", (int) mx
					/ tileSize, (int) my / tileSize));
			}else {
				bg.miner.setX(mx);
			}
			count++;
		}
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			col = (int) (mx+ tileSize / 2) / tileSize;
				if (bg.miner.checkIfMoveOK(bg.map.entityMaze2, row, col) == true) {
			bg.miner.setX(mx + x);
			System.out.println(String.format("RIGHT: col =%d, row =%d",
					(int) mx / tileSize, (int) my / tileSize));
			}else {
				bg.miner.setX(mx);
			}
				
				count++;
		}
		if (input.isKeyDown(Input.KEY_DOWN)) {
			row = (int) (my + tileSize / 2) / tileSize;
			if (bg.miner.checkIfMoveOK(bg.map.entityMaze2, row, col) == true) {
				bg.miner.setY(my + x);
			
			}	else {
				bg.miner.setY(my);
			}
			
//			System.out.println(String.format("DOWN: col =%d, row =%d", (int) mx
//					/ tileSize, (int) my / tileSize));
			count++;
		}

		if (input.isKeyDown(Input.KEY_UP)) {
			row = (int) (my- tileSize / 2) / tileSize;
			if (bg.miner.checkIfMoveOK(bg.map.entityMaze2, row, col) == true) {
				bg.miner.setY(my- x);
			
			}else {
				bg.miner.setY(my);
			}
			
//			System.out.println(String.format("UP: col =%d, row =%d", (int) mx
//					/ tileSize, (int) my / tileSize));
			count++;
		}
		bg.miner.addImage(MinerGame.minerRpgImg[count % 4][0]);
//		Vector v = new Vector(vx, vy);
//		bg.miner.setVelocity(v);

		
		//bg.miner.collisionDetect(bg, miner.Map.entityMaze2, delta);
		bg.miner.update(delta);

		if (bg.monster.getX() <MinerGame.TileSize * 8
				|| bg.monster.getCoarseGrainedMinX() > MinerGame.TileSize * 13) {
			bg.monster.bounce(90);
		}
		bg.monster.update(delta);
		
		// detect the collision between coin and miner
		for (int i = 0; i < bg.coins.size(); i++) {
			Bonus cn = bg.coins.get(i);
			if (bg.miner.collides(cn) != null) {
				ResourceManager.getSound(MinerGame.PICKED_COIN_RSC).play();
				// coin sound
				// http://www.freesound.org/people/NenadSimic/sounds/171696/
				scores += 10; // one coin equals 10 points
				bg.coins.remove(cn); // remove coin from ArrayList

			}
		}
		// If no coins, go to next level
		if (bg.coins.size() == 0  && (col==4 && row ==1)) {
			// ready to level3
			bg.setLevels(bg.getLevels() + 1);
			game.enterState(MinerGame.STARTUPSTATE);
//			bg.miner.setVelocity(new Vector(0.0f, 0.0f));// make the miner still
//			bg.miner.setPosition(9 * tileSize, 6 * tileSize); 
//			
//			bg.monster.setVelocity(new Vector(0.0f, 0.0f));// make the monster still
//			bg.monster.setPosition(6 * tileSize, 7 * tileSize); 
			//configMazeEnity(bg);
		}

		// detect the collision between coin and monster
		// if (bg.levels >= 2) {
		// for (int i = 0; i < bg.coins.size(); i++) {
		// Bonus cn = bg.coins.get(i);
		// if (bg.monster.collides(cn) != null) {
		// bg.coins.remove(cn); // remove coin from ArrayList
		// ResourceManager.getSound(MinerGame.PICKED_COIN_RSC).play();
		// scores += 10; // one coin equals 10 points
		// }
		// }
		// }
		if (input.isKeyDown(Input.KEY_HOME))
			bg.enterState(MinerGame.CONFIGSTATE);

	}

	// update for level1
	public void update1(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input input = container.getInput();
		MinerGame bg = (MinerGame) game;
		float mx = bg.miner.getX();
		float my = bg.miner.getY();
		int col = (int) mx / MinerGame.TileSize;
		int row = (int) my / MinerGame.TileSize;

		// stop the miner on the boundary
		if (col < 0 || col > 20) {
			vx = 0.0f;
			Vector v = new Vector(vx, vy);
			bg.miner.setVelocity(v);
		}
		if (row < 0 || row > 15) {
			vy = 0.0f;
			Vector v = new Vector(vx, vy);
			bg.miner.setVelocity(v);
		}
		bg.miner.controlMiner(input, bg, delta);
		bg.miner.collisionDetect(bg, miner.Map.entityMaze, delta);
		// bg.miner.circleMove(bg.miner);
		bg.miner.isPowerUp(bg, delta);

		// let blue ghost run in the circle
		bg.blueGhost.circleMove(bg.blueGhost);
		bg.blueGhost.update(bg, delta);

		// add pink ghost
		bg.pinkGhost.findPath(bg, bg.pinkGhost);
		bg.pinkGhost.update(bg, delta);
		// move the Red ghost
		bg.redGhost.findPath(bg, bg.redGhost);
		bg.redGhost.update(bg, delta);
		collidWithGhost(bg, bg.blueGhost);
		collidWithGhost(bg, bg.redGhost);
		collidWithGhost(bg, bg.pinkGhost);
		if (input.isKeyDown(Input.KEY_HOME))
			bg.enterState(MinerGame.CONFIGSTATE);
		// detect the collision between coin and miner
		for (int i = 0; i < bg.coins.size(); i++) {
			Bonus cn = bg.coins.get(i);
			if (bg.miner.collides(cn) != null) {
				ResourceManager.getSound(MinerGame.PICKED_COIN_RSC).play();
				// coin sound
				// http://www.freesound.org/people/NenadSimic/sounds/171696/
				scores += 10; // one coin equals 10 points
				bg.coins.remove(cn); // remove coin from ArrayList

			}
		}

		// State-Based Behavior:
		// miner's abilities change for a limited time after eat the power-dot
		for (int i = 0; i < bg.bricks.size(); i++) {
			Brick bk = bg.bricks.get(i);
			if (bk.brickType == Tile.GRASS) { // powerUp grass
				if (bg.miner.collides(bk) != null) {
					// miner eat the power-dots
					bg.bricks.remove(bk); // remove grass from ArrayList
					ResourceManager.getSound(MinerGame.GET_POWERUP_RSC).play();
					// powerUp sound
					// http://www.freesound.org/people/RandomationPictures/sounds/138491/
					bg.miner.setPowerup(true);
					bg.miner.setVelocity(new Vector(bg.miner.getVelocity()
							.getX() * 1.2f,
							bg.miner.getVelocity().getY() * 1.2f));
				}
			}
		}

		if (bg.miner.getPowerup() == true) {
			// bg.blueGhost.removeImage(ResourceManager
			// .getImage(MinerGame.BLUE_RSC));
			// bg.blueGhost.addImageWithBoundingBox(ResourceManager
			// .getImage(MinerGame.WHITE_RSC));
			// bg.redGhost
			// .removeImage(ResourceManager.getImage(MinerGame.RED_RSC));
			// bg.redGhost.addImageWithBoundingBox(ResourceManager
			// .getImage(MinerGame.WHITE_RSC));
			// bg.pinkGhost.removeImage(ResourceManager
			// .getImage(MinerGame.PINK_RSC));
			// bg.pinkGhost.addImageWithBoundingBox(ResourceManager
			// .getImage(MinerGame.WHITE_RSC));
			bg.miner.inPowerUp(bg); // set all ghost color to white.
		}

		Ghost ghost = null;
		// lock the shooting target
		ghost = bg.miner.lockTargetGhost(bg);

		// miner shoot the bullets
		if (input.isKeyDown(Input.KEY_0)) {
			if (ghost != null) {
				if (bg.bullets.size() > 0) {
					bg.miner.shoot(bg, bg.bullets.get(0), ghost, delta);
				} else {
					System.out.println("out of bullet now!");
				}
			} else {
				System.out.println(" ghost is not locked!");
			}
		}
		if (bg.bullets.size() > 0) {
			if (ghost == bg.blueGhost || ghost == bg.redGhost
					|| ghost == bg.pinkGhost)
				bg.bullets.get(0).killObject(bg, delta, ghost);
		}
		// monster dead
		// if (bg.monster.getHealth() <= 0) {
		// bg.monster.removeImage(ResourceManager
		// .getImage(MinerGame.MONSTER_RSC_LEFT));
		// bg.monster.removeImage(ResourceManager
		// .getImage(MinerGame.MONSTER_RSC));
		// // System.out.println("The monster dead! You will win soon!");
		// }

		// move the monster or monster ...
		if (bg.levels >= 2) {
			if (bg.monster.getX() > bg.ScreenWidth - MinerGame.TileSize * 2
					|| bg.monster.getCoarseGrainedMinX() < MinerGame.TileSize / 2) {
				bg.monster.bounce(90);
			}
			bg.monster.update(delta);
		}
		// check if there are any finished explosions, if so remove them
		for (Iterator<Bang> i = bg.explosions.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
			}
		}
		// If no coins, go to next level
		if (bg.coins.size() == 0) {
			// ready for level 2
			bg.setLevels(bg.getLevels() + 1);
//			bg.miner.setVelocity(new Vector(0.0f, 0.0f));// make the miner still
//			bg.miner.setPosition(17 * tileSize, 12 * tileSize); 
//			bg.monster.setVelocity(new Vector(0.0f, 0.0f));// make the monster still
//			bg.monster.setPosition(10 * tileSize, 5 * tileSize); 
			game.enterState(MinerGame.STARTUPSTATE);
		
		}
		if (lives == 0) {
			((GameOverState) game.getState(MinerGame.GAMEOVERSTATE))
					.setUserScore(lives);
			game.enterState(MinerGame.GAMEOVERSTATE);
		}
	}

	private void isDeadMessage(Graphics g) {
		// String msg = "You're Dead.\n You were slain by " +
		// hero.wasKilledBy();
		String msg = "You're  Dead. \n ";
		int PWIDTH = 640;
		int PHEIGHT = 480;
		Font msgsFont;
		// FontMetrics metrics;

		// msgsFont = new Font("SansSerif", Color.black, 24);
		// metrics = getFontMetrics(msgsFont);

		// int x = (PWIDTH - metrics.stringWidth(msg))/2;
		// int y = (PHEIGHT - metrics.getHeight())/2;
		int x = (PWIDTH) / 2;
		int y = (PHEIGHT) / 2;
		// g.setColor(Color.BLUE);
		// g.setColor(Color.red);
		// g.setFont(msgsFont);
		g.drawString(msg, x, y);
		// g.setColor(Color.black);
	}

	/*
	 * collision detection between miner and ghost
	 */
	public void collidWithGhost(MinerGame bg, Ghost ghost) {

		if (bg.miner.collides(ghost) != null) {
			if (bg.miner.powerUp == true) {
				// when ghost dies, back to spawn point
				ghost.back2Spawn(bg, ghost);
			} else {
				bg.miner.setX(16 * MinerGame.TileSize);
				bg.miner.setY(4 * MinerGame.TileSize);
				lives--;
			}
		}
	}

	@Override
	public int getID() {
		return MinerGame.PLAYINGSTATE;
	}

	/*
	 * Update the gameBoard according to the monster, and miner movement
	 */
	public void updateGameBoard(MinerGame bg) {
		Tile[][] mazeArr = bg.map.map;
		System.out.println("bg.miner.x =" + bg.miner.x);
		System.out.println("bg.miner.y =" + bg.miner.y);
		bg.miner.setX(bg.miner.getX() + tileSize);
		System.out.println("bg.miner.getX() =" + bg.miner.getX());

	}

	public void moveStep(MinerGame bg) {
		if (path == null) {
			System.out.println(String.format("No path exists"));
		} else {
			if (path.size() > 0) {
				Tile t = path.get(0);
				bg.miner.setX(t.X * tileSize);
				bg.miner.setY(t.Y * tileSize);
				path.remove(0);
				System.out.println(String.format("Success!"));
				System.out.println("path= " + path.toString());
			}
		}

	}

}

// Ref1 : about FPS calculate:
// http://www.lwjgl.org/wiki/index.php?title=LWJGL_Basics_4_%28Timing%29