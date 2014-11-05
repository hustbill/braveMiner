package miner;

import java.util.ArrayList;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.StateBasedGame;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * An Arcade Game - Brave Miner.
 * 
 * Here is my updated tile based background design for level 1. To complete this
 * level, the player need to lead he blue miner to pick up 15 coins and return
 * the entrance on time . In this level, the miner only has three bullets to
 * kill the monsters and one power pill in green color. The four monsters will
 * move around and chase the miner by their hands. I am still working on the
 * collision detection with 2D array-game board[][] for bricks, monsters and
 * miners.
 * 
 * Graphics resources courtesy of qubodup:
 * http://opengameart.org/content/bomb-explosion-animation
 * 
 *  A 2D RPG character walk spritesheet. 
 *  http://opengameart.org/content/2d-rpg-character-walk-spritesheet
 * 
 * Sound resources courtesy of DJ Chronos:
 * http://www.freesound.org/people/DJ%20Chronos/sounds/123236/
 * 
 * Sound resources courtesy of plasterbrain: game start
 * http://www.freesound.org/people/plasterbrain/sounds/243020/
 * 
 * coin sound 
 * http://www.freesound.org/people/NenadSimic/sounds/171696/
 * 
 * @author wallaces
 * @author hua.zhang
 * 
 */
public class MinerGame extends StateBasedGame {

	public static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	public static final int CONFIGSTATE = 3;
		
	public static final String BULLET_RSC = "resx/resource/bullet.png";
	public static final String BULLET_LEFT_RSC = "resx/resource/bullet_left.png";
	public static final String BULLET_RIGHT_RSC = "resx/resource/bullet_right.png";
	public static final String BULLET_UP_RSC = "resx/resource/bullet_up.png";
	
	public static final String POWERUP_RSC = "resx/resource/powerUp.png";
	public static final String MINER_RSC = "resx/resource/miner_green.png";
	public static final String BRICK_RSC = "resx/resource/brick.png";
	public static final String GRASS_RSC = "resx/resource/grass.png";
	public static final String TREE_RSC = "resx/resource/tree.png";
	public static final String PIG_RSC = "resx/resource/pig.png";
	public static final String FISH_RSC = "resx/resource/fish.png";
	public static final String ZOMBIE_RSC = "resx/resource/zombie.png";
	public static final String SKULL_RSC = "resx/resource/skull.png";
	public static final String COIN_RSC = "resx/resource/coin.png";
	public static final String JEWEL_RSC = "resx/resource/diamond.png";

	public static final String MONSTER_RSC = "resx/resource/cyclops.png";
	public static final String MONSTER_RSC_LEFT = "resx/resource/cyclops_left.png";
	public static final String BALL_BALLIMG_RSC = "resx/resource/ball.png";
	public static final String BALL_BROKENIMG_RSC = "resx/resource/brokenball.png";
	public static final String YOUWIN_BANNER_RSC = "resx/resource/youwin.png";
	public static final String GAMEOVER_BANNER_RSC = "resx/resource/gameover.png";
	public static final String STARTUP_BANNER_RSC = "resx/resource/menu.png";
	public static final String BANG_EXPLOSIONIMG_RSC ="resx/resource/explosion.png";
	public static final String MAZE_BOARD_RSC = "resx/resource/maze.png";
	public static final String SPRITES_CHARACTERS_RSC=  "resx/resource/Sprites_Characters.png";
	public static final String SPRITES_MINER_LEFT_RSC=  "resx/resource/Walk_9(15,70,109).png";
	public static final String SPRITES_MINER_RPG_RSC=  "resx/resource/rpg_sprite_walk.png";
	public static final String SPRITES_FARMER_RSC = "resx/resource/2_Farmer_Walk_strip15.png";
	
	public static final String BANG_EXPLOSIONSND_RSC = "resx/resource/explosion.wav";
	public static final String SHOOT_BULLET_RSC = "resx/resource/qubodup-BangMid.ogg";
	public static final String START_GAME_RSC = "resx/resource/gameStart.ogg";
	public static final String GET_POWERUP_RSC = "resx/resource/getPowerup.wav";
	public static final String PICKED_COIN_RSC = "resx/resource/pickedCoin.wav";
	public static final String DROP_BRICK_RSC = "resx/resource/dropBrick.wav";

	public static final String MAZE_RSC = "resx/resource/maze.txt";
	public static final int TileSize = 32; // tile size

	public final int ScreenWidth;
	public final int ScreenHeight;

	public int levels = 1;
	public SpriteSheet ghostSheet;
	public SpriteSheet minerRpgSheet;
	public SpriteSheet minerLeftSheet;
	public SpriteSheet farmerSheet;
	public static Image[][] ghostImgs;
	public static Image[] blueGhostImg = new Image[4];
	public static Image[] redGhostImg = new Image[4];
	public static Image[] pinkGhostImg = new Image[4];
	public static Image[] whiteGhostImg = new Image[4];
	public static Image[] minerLeftImg = new Image[15];
	public static Image[] farmerImg = new Image[15];
	
	public static Image[][] minerRpgImg = new Image[4][8];
	

	Ball ball;
	Monster monster;
	Brick brick;
	Bonus coin;
	Miner miner;
	Ghost redGhost;
	Ghost blueGhost;
	Ghost pinkGhost;

	int r, c = 0;
	ArrayList<Miner> miners;
	ArrayList<Bullet> bullets;
	ArrayList<Bonus> coins;
	ArrayList<Brick> bricks;
	ArrayList<Ghost> ghosts;
	ArrayList<Bang> explosions;
	Brick[][]  tiles;

	// create a 2D Map with tile objects
	Map map;

	/*
	 * set levels 
	 */
	public void setLevels(int level) {
		this.levels = level;
	}
	
	public int getLevels() {
		return this.levels;
	}
	
	/**
	 * Create the MinerGame frame, saving the width and height for later use.
	 * 
	 * @param title
	 *            the window's title
	 * @param width
	 *            the window's width
	 * @param height
	 *            the window's height
	 */
	public MinerGame(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;

		System.out.println(String.format("MinerGame height= %d width =%d",
				height, width));

		// Create a 2D tile array by read maze.txt
		int rows = (int) height/ TileSize;
		int cols = (int) width /TileSize;
		map = new Map(rows, cols, MAZE_RSC);

		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
		explosions = new ArrayList<Bang>(10);
		bricks = new ArrayList<Brick>(800);
		coins = new ArrayList<Bonus>(100);
		bullets = new ArrayList<Bullet>(10);
		miners = new ArrayList<Miner>(3);
		
		tiles = new Brick[15][20];
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new StartUpState());
		addState(new GameOverState());
		addState(new PlayingState());
		addState(new ConfigState());
		// the sound resource takes a particularly long time to load,
		// we preload it here to (1) reduce latency when we first play it
		// and (2) because loading it will load the audio libraries and
		// unless that is done now, we can't *disable* sound as we
		// attempt to do in the startUp() method.
		ResourceManager.loadSound(BANG_EXPLOSIONSND_RSC);
		ResourceManager.loadSound(START_GAME_RSC);
		ResourceManager.loadSound(GET_POWERUP_RSC);
		ResourceManager.loadSound(PICKED_COIN_RSC);
		ResourceManager.loadSound(DROP_BRICK_RSC);
		ResourceManager.loadSound(SHOOT_BULLET_RSC);

		// preload all the resources to avoid warnings & minimize latency...
		ResourceManager.loadImage(BANG_EXPLOSIONIMG_RSC);
		ResourceManager.loadImage(BULLET_RSC);
		ResourceManager.loadImage(BULLET_LEFT_RSC);
		ResourceManager.loadImage(BULLET_RIGHT_RSC);
		ResourceManager.loadImage(MINER_RSC);
		ResourceManager.loadImage(POWERUP_RSC);

		ResourceManager.loadImage(MONSTER_RSC);
		ResourceManager.loadImage(MONSTER_RSC_LEFT);
		ResourceManager.loadImage(GRASS_RSC);
		ResourceManager.loadImage(TREE_RSC);
		ResourceManager.loadImage(BRICK_RSC);
		ResourceManager.loadImage(COIN_RSC);
		ResourceManager.loadImage(JEWEL_RSC);
		ResourceManager.loadImage(ZOMBIE_RSC);
		ResourceManager.loadImage(PIG_RSC);
		ResourceManager.loadImage(FISH_RSC);
		ResourceManager.loadImage(SKULL_RSC);
		ResourceManager.loadImage(BALL_BALLIMG_RSC);
		ResourceManager.loadImage(BALL_BROKENIMG_RSC);
		ResourceManager.loadImage(GAMEOVER_BANNER_RSC);		
		ResourceManager.loadImage(STARTUP_BANNER_RSC);
		ResourceManager.loadImage(YOUWIN_BANNER_RSC);
		ResourceManager.loadImage(MAZE_BOARD_RSC);
		ResourceManager.loadImage(SPRITES_CHARACTERS_RSC);
		ResourceManager.loadImage(SPRITES_MINER_RPG_RSC);
		ResourceManager.loadImage(SPRITES_FARMER_RSC);

		ball = new Ball(ScreenWidth / 2, ScreenHeight - 40, .11f, .1f);
		
		///Ghost images
		//Sprite gets a subimage from the spritesheet at the specified coordinates.  
		ghostSheet =ResourceManager.getSpriteSheet(MinerGame.SPRITES_CHARACTERS_RSC, 133, 165);
		
		//
		//<SubTexture name="Blue_Front1.png" x="272" y="0" width="132" height="165"/>
		blueGhostImg[0] =  ghostSheet.getSubImage(272, 0, 132, 165);
		//<SubTexture name="Red_Front1.png" x="270" y="1002" width="132" height="163"/>
		redGhostImg[0] = ghostSheet.getSubImage(270,1002, 132, 163);
		
		//redGhostImg[0] = ghostSheet.getSubImage(2,6);
		//<SubTexture name="Pink_Front1.png" x="538" y="1158" width="132" height="161"/>
		pinkGhostImg[0] = ghostSheet.getSubImage(538, 1158, 132, 161);
		//<SubTexture name="White_Front1.png" x="135" y="1669" width="132" height="164"/>
		whiteGhostImg[0] = ghostSheet.getSubImage(135, 1669, 132, 164);
		
		// Miner RPG images
		minerRpgSheet = ResourceManager.getSpriteSheet(MinerGame.SPRITES_MINER_RPG_RSC, 24, 32);
		for(int i=0; i< 4; i++) {
			for(int j=0; j<4; j++) {
				minerRpgImg[i][j]=  minerRpgSheet.getSubImage(i, j);
			}
		}
		// Farmer sheet for level3 demo
		  farmerSheet =  ResourceManager.getSpriteSheet(MinerGame.SPRITES_FARMER_RSC, 47, 60);
		  for(int k=0; k< 14; k++ ) {
			  farmerImg[k] = farmerSheet.getSubImage(k, 0);
		  }
	}

	public static void main(String[] args) {
		AppGameContainer app;
		try {
//			app = new AppGameContainer(new MinerGame("Brave Miner!", 1024, 768));
//			app.setDisplayMode(1024, 768, false);
			
			app = new AppGameContainer(new MinerGame("Brave Miner!", 640, 480));
			app.setDisplayMode(640, 480 , false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}
}

/*
 * 
 * Ref
 * http://stackoverflow.com/questions/9197679/making-a-player-move-on-2d-array
 * -game-grid
 * 
 * Looks like you're using row-major ordering, judging from the way your board
 * prints out. Based on that, here's what you'll need to do:
 * 
 * First, you need to store the player's position somewhere. Right now it's
 * hardcoded to 0,0. Second, you need to read in the player's move. That will
 * have to happen in a loop, where you get a move, check if the move is allowed,
 * perform the move, and display the results. Third, you need to be able to
 * calculate the new position based on the move. Up means row -= 1. Right means
 * column += 1. Etc. Given the new coordinates, you need to make sure the move
 * is valid. At the very least, you have to stop them from walking off the
 * board, but you may also prevent them from entering a square with an obstacle,
 * etc. Once you know that the move is valid, you have to update the variables
 * you're storing the current coordinates in. At the end of the loop, you'll
 * need to redraw the board. That's the basic gist of it. Right now you are
 * doing everything in main(), and that's okay, but if it were me I would start
 * to split things out into separate methods, like InitializeBoard(),
 * GetNextMove(), CheckIfMoveIsValid(int r, int c), and so on. That way, main()
 * becomes a high-level view of your game loop, and the guts of the different
 * operations are compartmentalized and more easy to deal with. This will
 * require storing off things like your game board into class variables rather
 * than local variables, which should actually make things like obstacle
 * detection easier than it would be currently.
 */
