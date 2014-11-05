package miner;


import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;


/**
 * The Bonus class is an Entity that has a velocity (since it's moving).
 * User can let the ball hit the coin to increase their scores
 * 
 *  @author Hua Zhang
 */


public class Bonus extends Entity{
	
	private Vector velocity;
	private int count=5;  // coins number
	private int countdown;
	public int hit_times;
	public Bonus( float x,  float y, float vx, float vy, int bonusType) {
		super(x, y);
		
		if( bonusType == Tile.BONUS)
		addImageWithBoundingBox(ResourceManager
				.getImage(MinerGame.COIN_RSC));
		if( bonusType == Tile.JEWEL)
			addImageWithBoundingBox(ResourceManager
					.getImage(MinerGame.JEWEL_RSC));
	
		vx=0.0f;
		vy=0.01f;
		velocity = new Vector(vx, vy);
		count = 5;
		countdown = 0;
		
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}
	
	public void setCoinCount(int coinsCount) {
		count = coinsCount;
	}
	
	public int getCoinsCount() {
		return count;
	}
	
//	/*
//	 *  Configure the number of coins
//	 */
//	public void configBonus(StateBasedGame game, int levels) {
//		MinerGame bg = (MinerGame) game;
//		System.out.println("levels= " + levels);
//		switch (levels) {
//		case 1:
//			for (int j = 0; j < 3; j++) {
//				Bonus coin = new Bonus(bg.ScreenWidth -32,	32  , -0.01f*j, 0.005f*j);					
//				bg.coins.add(coin);
//			}
//			break;
//		case 2:
//			for (int j = 0; j < 4; j++) {
//				Bonus coin = new Bonus(bg.ScreenWidth -32,	80 + 32 * j, -0.01f*j, 0.005f*j);	
//				bg.coins.add(coin);
//			}
//			break;			
//		case 3:
//			for (int j = 0; j < 5; j++) {
//				Bonus coin = new Bonus(bg.ScreenWidth -32,	80 + 32 * j, -0.01f*j, 0.005f*j);
//				bg.coins.add(coin);
//			}
//			break;
//		case 4:
//			for (int j = 0; j < 6; j++) {
//				Bonus coin = new Bonus(bg.ScreenWidth -32,	80 + 32 * j, -0.01f*j, 0.005f*j);
//				bg.coins.add(coin);
//			}
//			break;
//
//		default:
////			for (int j = 0; j < 3; j++) {
////				Bonus coin = new Bonus(bg.ScreenWidth -32,	80 + 32 * j, -0.01f*j, 0.005f*j);	
//			//Bonus coin.setVelocity(new Vector(-0.1f,0.1f-0.02f*j));
////				bg.coins.add(coin);
////			}
//			break;
//		}
//	}
//	
	
	/**
	 * Bounce the ball off a surface. This simple implementation, combined
	 * with the test used when calling this method can cause "issues" in
	 * some situations. Can you see where/when? If so, it should be easy to
	 * fix!
	 * 
	 * @param surfaceTangent
	 */
	public void bounce(float surfaceTangent) {
//		removeImage(ResourceManager.getImage(MinerGame.BALL_BALLIMG_RSC));
//		addImageWithBoundingBox(ResourceManager
//				.getImage(MinerGame.BALL_BROKENIMG_RSC));
		countdown = 500;
		velocity = velocity.bounce(surfaceTangent);
	}

	/**
	 * Update the Ball based on how much time has passed...
	 * 
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	public void update(final int delta) {
		translate(velocity.scale(delta));
//		if (countdown > 0) {
//			countdown -= delta;
//			if (countdown <= 0) {
//				addImageWithBoundingBox(ResourceManager
//						.getImage(MinerGame.COIN_RSC));
////				removeImage(ResourceManager
////						.getImage(MinerGame.COIN_RSC));
//			}
//		}
	}
	


}
