package miner;

import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * The Bullet class is an Entity that has a velocity (since it's moving). When
 * the bullet was shoot by miner, it temporarily displays a image with
 * cracks for a nice visual effect.
 * 
 */
 class Bullet extends Entity {

	private Vector velocity;
	private int countdown;

	public Bullet(final float x, final float y,  float vx,  float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager
				.getImage(MinerGame.BULLET_RSC));
		velocity = new Vector(vx, vy);
		countdown = 0;
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}

	/*
	 * Power up the bullet 
	 */
//	public void powerUp(MinerGame bg) {
//		System.out.println("******** powerUp begin ************");
//		System.out.println("bullet.vx="+ bg.bt.getVelocity().getX());
//		System.out.println("bullet.vy="+ bg.bt.getVelocity().getY());
//		if(bg.bt.getVelocity().getY() < 0) {
//			bg.bt.setVelocity(new Vector( bg.bt.getVelocity().getX() *1.1f,  bg.bt.getVelocity().getY()* 1.1f));
//		
//		   }
//		
//		System.out.println("******** powerUp end ************");
//		System.out.println("bullet.vx="+ bg.bt.getVelocity().getX());
//		System.out.println("bullet.vy="+ bg.bt.getVelocity().getY());
//		
//	}
	
	

	
	
	/*
	 *  Adjust the velocity, shape, image of bullet when levels changed
	 */
//	public void configbullet(StateBasedGame game, int levels) {
//		MinerGame bg = (MinerGame) game;
//		System.out.println("levels= " + levels);
//		bg.bt.setX(400.0f);
//		bg.bt.setY(540.0f);
//	
//	}
	
	
	/**
	 * Bounce the bullet off a surface. This simple implementation, combined
	 * with the test used when calling this method can cause "issues" in
	 * some situations. Can you see where/when? If so, it should be easy to
	 * fix!
	 * 
	 * @param surfaceTangent
	 */
	public void bounce(float surfaceTangent) {
//		removeImage(ResourceManager.getImage(MinerGame.bullet_bulletIMG_RSC));
//		addImageWithBoundingBox(ResourceManager
//				.getImage(MinerGame.bullet_BROKENIMG_RSC));
		countdown = 500;
		velocity = velocity.bounce(surfaceTangent);
	}

	/**
	 * Update the bullet based on how much time has passed...
	 * 
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	public void update(final int delta) {
		translate(velocity.scale(delta));
	
		if (countdown > 0) {
			countdown -= delta;
			if (countdown <= 0) {
				 ResourceManager.getSound(MinerGame.SHOOT_BULLET_RSC).play();
			}
		}
	}
	
	public void killObject(MinerGame bg, int delta, Ghost ghost) {
		bg.bullets.get(0).update(delta);
		boolean beShot = false;
		if (bg.bullets.get(0).collides(ghost) != null) {
			 beShot = true;
			if (beShot) {
				bg.explosions.add(new Bang(ghost.getX(),
						ghost.getY()));
			}
			ghost.setPosition(new Vector(2 * MinerGame.TileSize, 4 * MinerGame.TileSize));
			ResourceManager.getSound(MinerGame.SHOOT_BULLET_RSC).play();

			bg.bullets.remove(0);
		} 

	}
}
