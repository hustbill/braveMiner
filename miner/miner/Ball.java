package miner;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * The Ball class is an Entity that has a velocity (since it's moving). When
 * the Ball bounces off a surface, it temporarily displays a image with
 * cracks for a nice visual effect.
 * 
 */
 class Ball extends Entity {

	private Vector velocity;
	private int countdown;

	public Ball(final float x, final float y,  float vx,  float vy) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager
				.getImage(MinerGame.BALL_BALLIMG_RSC));
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
	 * Power up the ball when ball hit the paddle 
	 */
	public void powerUp(MinerGame bg) {
		System.out.println("******** powerUp begin ************");
		System.out.println("ball.vx="+ bg.ball.getVelocity().getX());
		System.out.println("ball.vy="+ bg.ball.getVelocity().getY());
		if(bg.ball.getVelocity().getY() < 0) {
			bg.ball.setVelocity(new Vector( bg.ball.getVelocity().getX() *1.1f,  bg.ball.getVelocity().getY()* 1.1f));
		
		   }
		
		System.out.println("******** powerUp end ************");
		System.out.println("ball.vx="+ bg.ball.getVelocity().getX());
		System.out.println("ball.vy="+ bg.ball.getVelocity().getY());
		
	}
	
	
	/*
	 *  Control the velocity of ball in playingState 
	 */
	
	public void controlBall(Input input, MinerGame bg) 
			throws SlickException{
		
		if (input.isKeyDown(Input.KEY_W)) {
			bg.ball.setVelocity(bg.ball.getVelocity().add(
					new Vector(0f, -.001f)));
		}
		if (input.isKeyDown(Input.KEY_S)) {
			bg.ball.setVelocity(bg.ball.getVelocity().add(
					new Vector(0f, +.001f)));
		}
		if (input.isKeyDown(Input.KEY_A)) {
			bg.ball.setVelocity(bg.ball.getVelocity()
					.add(new Vector(-.001f, 0)));
		}
		if (input.isKeyDown(Input.KEY_D)) {
			bg.ball.setVelocity(bg.ball.getVelocity().add(
					new Vector(+.001f, 0f)));
		}

		if (input.isKeyDown(Input.KEY_N)) {
			bg.ball.setX(400.0f);
			bg.ball.setY(540.0f);
		}
		
		// Cheat codes to allow user to access all of levels by press "P"
		if (input.isKeyDown(Input.KEY_P)) {
			for (int i = 0; i < bg.bricks.size(); i++) {
				Brick bk = bg.bricks.get(i);
				bg.ball.setY(bk.getY());
				bg.ball.setX(bk.getX());
			}
		}
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			bg.ball.bounce(90);
		}

		if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
			bg.ball.bounce(180);

		}
		if (input.isKeyDown(Input.KEY_ADD)) {
			bg.ball.setVelocity(bg.ball.getVelocity().add(
					new Vector(+.001f, 0f)));

		}
		if (input.isKeyDown(Input.KEY_0)) {
			bg.ball.scale(0.5f);
		}

		if (input.isKeyDown(Input.KEY_1)) {
			bg.ball.scale(1.5f);
		}
	}
	
	/*
	 *  Adjust the velocity, shape, image of ball when levels changed
	 */
	public void configBall(StateBasedGame game, int levels) {
		MinerGame bg = (MinerGame) game;
		System.out.println("levels= " + levels);
		bg.ball.setX(400.0f);
		bg.ball.setY(540.0f);
		switch (levels) {
		case 1:
			bg.ball.scale(1.0f);
			bg.ball.setVelocity(new Vector(.1f, -.12f));			
			break;
		case 2:
			bg.ball.scale(.98f);
			bg.ball.setVelocity(new Vector(-.12f, -.13f));
			break;			
		case 3:
			bg.ball.scale(.95f);
			bg.ball.setVelocity(new Vector(.13f, -.14f));
			break;
		case 4:
			bg.ball.scale(.92f);
			bg.ball.setVelocity(new Vector(-.14f, -.15f));
			break;

		default:
			bg.ball.scale(.92f);
			bg.ball.setVelocity(new Vector(.12f, -.14f));
			break;
		}
	}
	
	
	/**
	 * Bounce the ball off a surface. This simple implementation, combined
	 * with the test used when calling this method can cause "issues" in
	 * some situations. Can you see where/when? If so, it should be easy to
	 * fix!
	 * 
	 * @param surfaceTangent
	 */
	public void bounce(float surfaceTangent) {
		removeImage(ResourceManager.getImage(MinerGame.BALL_BALLIMG_RSC));
		addImageWithBoundingBox(ResourceManager
				.getImage(MinerGame.BALL_BROKENIMG_RSC));
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
		if (countdown > 0) {
			countdown -= delta;
			if (countdown <= 0) {
				addImageWithBoundingBox(ResourceManager
						.getImage(MinerGame.BALL_BALLIMG_RSC));
				removeImage(ResourceManager
						.getImage(MinerGame.BALL_BROKENIMG_RSC));
			}
		}
	}
}
