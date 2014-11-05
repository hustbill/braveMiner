package miner;

import java.util.Iterator;

import jig.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import jig.Vector;

/**
 * This state is active prior to the Game starting. In this state, sound is
 * turned off, and the bounce counter shows '?'. The user can only interact with
 * the game by pressing the SPACE key which transitions to the Playing State.
 * Otherwise, all game objects are rendered and updated normally.
 * 
 * Transitions From (Initialization), GameOverState
 * 
 * Transitions To PlayingState
 */
class StartUpState extends BasicGameState {

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		MinerGame bg = (MinerGame) game;
	

	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.setSoundOn(false);
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		MinerGame bg = (MinerGame) game;

//		if (bg.miner != null) {
//			bg.miner.render(g);
//		}
		 if(bg.monster != null )
		 bg.monster.render(g);
		if (bg.blueGhost != null){
		
			bg.blueGhost.render(g);
			
		}
		if (bg.redGhost != null)
			bg.redGhost.render(g);
		if (bg.pinkGhost != null)
			bg.pinkGhost.render(g);

		if (ResourceManager.getImage(MinerGame.STARTUP_BANNER_RSC) == null) {
			g.drawString("Brave Miner", 225, 125);
			g.drawString("1 Basic game  ", 225, 160);
			g.drawString("2 Maze game ", 225, 195);
			g.drawString("3 Isometric game ", 225, 220);
			g.drawString("Esc Quit", 225, 255);
		} else {
			g.drawImage(ResourceManager.getImage(MinerGame.STARTUP_BANNER_RSC),
					75, 10);
		}

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {

		Input input = container.getInput();
		MinerGame bg = (MinerGame) game;

		if (input.isKeyDown(Input.KEY_1)) {
			bg.setLevels(1);
			bg.enterState(MinerGame.PLAYINGSTATE);
		}

		if (input.isKeyDown(Input.KEY_2)) {
			bg.setLevels(2);
			bg.enterState(MinerGame.PLAYINGSTATE);
		}

		if (input.isKeyDown(Input.KEY_3)) {
			bg.setLevels(3);
			
			bg.enterState(MinerGame.PLAYINGSTATE);
		}

		if (input.isKeyDown(Input.KEY_Q))
			bg.enterState(MinerGame.GAMEOVERSTATE);

		if (bg.blueGhost != null && bg.redGhost != null && bg.pinkGhost != null) {
			if (bg.blueGhost != null) {
				// let blue ghost run in the circle
				bg.blueGhost.circleMove(bg.blueGhost);
				bg.blueGhost.update(bg, delta);
			}

			if (bg.pinkGhost != null) {
				// add pink ghost
				bg.pinkGhost.findPath(bg, bg.pinkGhost);
				bg.pinkGhost.update(bg, delta);
			}
			if (bg.redGhost != null) {
				// move the Red ghost
				bg.redGhost.findPath(bg, bg.redGhost);
				bg.redGhost.update(bg, delta);
				// collidWithGhost(bg, bg.blueGhost);
			}
		}
//		if (bg.miner.getY() > bg.ScreenHeight- MinerGame.TileSize * 3
//				|| bg.miner.getCoarseGrainedMinY() < MinerGame.TileSize * 3) {
//			bg.miner.bounce(0);
//		}
//		bg.miner.update(delta);
//		if (bg.monster.getX() > bg.ScreenWidth - MinerGame.TileSize * 2
//				|| bg.monster.getCoarseGrainedMinX() < MinerGame.TileSize * 2) {
//			bg.monster.bounce(90);
//		}
//		bg.monster.update(delta);

	}

	@Override
	public int getID() {
		return MinerGame.STARTUPSTATE;
	}

}