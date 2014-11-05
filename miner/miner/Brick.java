package miner;

import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * The Bricks class is an Entity that cannot move. Bricks near the top of the
 * screen, and are "destroyed" when hit by ball
 * 
 */
public class Brick extends Entity {
	public int hit_times = 0;
	public int brickType = Tile.BRICK;

	public Brick(float x, float y , int bkType) {
		super(x, y);
		brickType = bkType;
		if( brickType == Tile.BRICK)
			addImageWithBoundingBox(ResourceManager.getImage(MinerGame.BRICK_RSC));
		if( brickType == Tile.GRASS)
			addImageWithBoundingBox(ResourceManager.getImage(MinerGame.GRASS_RSC));
		if( brickType == Tile.TREE)
			addImageWithBoundingBox(ResourceManager.getImage(MinerGame.TREE_RSC));
	}

	public void changePic(int levels) {
		switch (levels) {
		case 1:
			addImageWithBoundingBox(ResourceManager
					.getImage(MinerGame.BRICK_RSC));
			break;
		case 2:
			removeImage(ResourceManager.getImage(MinerGame.BRICK_RSC));
			// http://icons.iconarchive.com/icons/pino/looney/32/Porky-Pig-icon.png
			addImageWithBoundingBox(ResourceManager
					.getImage(MinerGame.PIG_RSC));
			break;

		case 3:
			removeImage(ResourceManager.getImage(MinerGame.BRICK_RSC));
			addImageWithBoundingBox(ResourceManager
					.getImage(MinerGame.FISH_RSC));
			break;
		case 4:
			removeImage(ResourceManager.getImage(MinerGame.BRICK_RSC));
			// http://img1.wikia.nocookie.net/__cb20110116163235/zombiefarm/images/a/a2/Zombie_Quest.png
			addImageWithBoundingBox(ResourceManager
					.getImage(MinerGame.ZOMBIE_RSC));
			break;
		default:
			addImageWithBoundingBox(ResourceManager
					.getImage(MinerGame.BRICK_RSC));
			break;
		}
	}

	/**
	 * "Destroy" the Brick when the ball hit...
	 * 
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	// public void update(final int delta, int levels) {
	public void update(int levels) {

		switch (levels) {
		case 1:
			removeImage(ResourceManager.getImage(MinerGame.BRICK_RSC));
			break;

		case 2: // for first hit, just change the image to skull
			System.out.println("Enter into Brick update method");
			removeImage(ResourceManager.getImage(MinerGame.ZOMBIE_RSC));
			addImageWithBoundingBox(ResourceManager
					.getImage(MinerGame.SKULL_RSC)); // for second hit, the
														// bricks were destroyed

			break;
		case 3:
			removeImage(ResourceManager.getImage(MinerGame.COIN_RSC));
			addImageWithBoundingBox(ResourceManager
					.getImage(MinerGame.SKULL_RSC)); // for second hit, the
														// bricks were destroyed
		default:
			addImageWithBoundingBox(ResourceManager
					.getImage(MinerGame.SKULL_RSC));
			break;
		}
	}

}
