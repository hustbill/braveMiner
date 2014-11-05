package miner;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * The Monster class is an Entity that has a velocity (since it's moving). User
 * can control a Monster to influences the path of ball
 * The image of Monster from 
 * http://opengameart.org/content/Monster-monster-2d
 * 
 * @author Hua Zhang
 */

//ZMonster
public class Monster extends Entity {

	private Vector velocity;
	private int countdown;
	public float angle;
	public int health = 100;

	public Monster(final float x, final float y, final float vx, float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager.getImage(MinerGame.MONSTER_RSC_LEFT));
		//vy = 0.0f;
		velocity = new Vector(vx, vy);
		countdown = 0;
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void setAngle(final float angle_num) {
		angle = angle_num;
	}

	public float getAngle() {
		return angle;
	}
	
	public void setHealth( int h) {
		health = h;
	}
	public int getHealth () {
		return health;
	}
	

	/*
	 * Control the velocity and location of Monster in playingState
	 */

	public void controlMonster(Input input, MinerGame bg) throws SlickException {

		// Press C to save your ball
		if (input.isKeyDown(Input.KEY_C)) {
			

			bg.monster.setX(bg.ball.getX());
			if (bg.ball.getCoarseGrainedMinY() > bg.ScreenHeight / 2)
				bg.monster.setY(bg.ball.getY() + 32.0f);
			if (bg.ball.getCoarseGrainedMinY() < bg.ScreenHeight / 2)
				bg.monster.setY(bg.ball.getY() - 32.0f);
		}
		if (input.isKeyDown(Input.KEY_A)) {
//			bg.monster.setVelocity(new Vector(0.0f, 0.0f));
			bg.monster.setX(bg.monster.getX() -6.4f);
			
		}
		if (input.isKeyDown(Input.KEY_D)) {
//			bg.monster.setVelocity(new Vector(0.0f, 0.0f));
			bg.monster.setX(bg.monster.getX() + 6.4f);
		}		
		if (input.isKeyDown(Input.KEY_S)) {
//			bg.monster.setVelocity(new Vector(0.0f, 0.0f));
			bg.monster.setY(bg.monster.getY() + 6.4f);
		}
		
		if (input.isKeyDown(Input.KEY_W)) {
//			bg.monster.setVelocity(new Vector(0.0f, 0.0f));
			bg.monster.setY(bg.monster.getY() - 6.4f);
		}
	}

	/*
	 * Configure the Monster: location, shape, image
	 */
	public void configMonster(StateBasedGame game, int levels) {
		MinerGame bg = (MinerGame) game;
		System.out.println("levels= " + levels);
		bg.monster.setX(400.0f); // set the position and velocity of Monster
		bg.monster.setY(560.0f);
		bg.monster.setVelocity(new Vector(-0.01f, 0.01f));
		switch (levels) {
		case 1:
			bg.monster.scale(1.0f);
			break;
		case 2:
			bg.monster.scale(.95f);
			break;
		case 3:
			bg.monster.scale(.90f);
			break;
		case 4:
			bg.monster.scale(.85f);
			break;

		default:
			bg.monster.scale(.90f);
			break;
		}
	}

	/**
	 * Bounce the Monster off a surface. 
	 * 
	 * @param surfaceTangent
	 */
	public void bounce(float surfaceTangent) {
		// removeImage(ResourceManager.getImage(MinerGame.BALL_BALLIMG_RSC));
		// addImageWithBoundingBox(ResourceManager
		// .getImage(MinerGame.BALL_BROKENIMG_RSC));
		countdown = 500;
		velocity = velocity.bounce(surfaceTangent);
	
		if(velocity.getX() > 0 ) {
		 addImageWithBoundingBox(ResourceManager
				 .getImage(MinerGame.MONSTER_RSC_LEFT));
		  removeImage(ResourceManager
				 .getImage(MinerGame.MONSTER_RSC));
		}else {
			 addImageWithBoundingBox(ResourceManager
					 .getImage(MinerGame.MONSTER_RSC));
			  removeImage(ResourceManager
					 .getImage(MinerGame.MONSTER_RSC_LEFT));
		}

			//System.out.println("The monster dead! You will win soon!");
		}
		
	

	/**
	 * Update the Monster based on how much time has passed...
	 * 
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	public void update(final int delta) {
		translate(velocity.scale(delta));
//		if (countdown > 0) {
//			countdown -= delta;
//			 if (countdown <= 0) {
//			 addImageWithBoundingBox(ResourceManager
//			 .getImage(MinerGame.BALL_BALLIMG_RSC));
//			 removeImage(ResourceManager
//			 .getImage(MinerGame.BALL_BROKENIMG_RSC));
//			 }
//		}
	}

}
