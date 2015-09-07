// This entire file is part of my masterpiece.
// SUNG-HOON KIM

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class CharacterState {
	private int myState;
	private Animation myAnimation;
	private Character myCharacter;
	private SpriteGetter mySprites;

	public CharacterState (Character character, int state) {
		myState = state;
		myCharacter = character;
		mySprites = new SpriteGetter();
		setMyAnimation();
	}
	private void setMyAnimation() {
		stopAnimation();
		switch (myState) {
		case Character.WALKING_FORWARD:
			setWalkingForwardAnimation();
			break;
		case Character.WALKING_BACKWARD:
			setWalkingBackwardAnimation();
			break;
		case Character.CROUCHING:
			setCrouchingAnimation();
			break;
		case Character.JUMPING:
			setJumpingAnimation();
			break;
		case Character.IN_HITSTUN:
			setInHitstunAnimation();
			break;
		default:
			setIdlingAnimation();
			break;
		}
		startAnimation();
	}
	private void setMyAnimation(Animation animation) {
		stopAnimation();
		myAnimation = animation;
		startAnimation();
	}
	private SpriteTransition createSpriteTransition(int state, Duration duration, int cycleCount) {
		SpriteTransition spriteTransition = new SpriteTransition(myCharacter.getMyImage(), duration, 
				Character.SPRITE_CENTER, mySprites.getSprite(state), myCharacter.getOrientation());
		spriteTransition.setCycleCount(cycleCount);
		spriteTransition.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				changeState(Character.IDLING);
			}
		});
		return spriteTransition;
	}
	private Timeline createStaticAnimation(int state) {
		Timeline animation = new Timeline();
		animation.getKeyFrames().add(new KeyFrame(Duration.millis(Main.MILLISECOND_DELAY),
				new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				myCharacter.getMyImage().setViewport(mySprites.getViewportRectangle(state, myCharacter.getOrientation()));
			}
		}));
		animation.setCycleCount(1);
		animation.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				if (myState == state) {
					changeState(Character.IDLING);
				}
			}
		});
		return animation;
	}
	private void setIdlingAnimation() {
		myAnimation = createSpriteTransition(Character.IDLING, Duration.millis(1000), Animation.INDEFINITE);
	}
	private void setWalkingForwardAnimation() {
		myAnimation = createSpriteTransition(Character.WALKING_FORWARD, Duration.millis(500), 1);
	}
	private void setWalkingBackwardAnimation() {
		myAnimation = createStaticAnimation(Character.WALKING_BACKWARD);
	}
	private void setCrouchingAnimation() {
		myAnimation = createStaticAnimation(Character.CROUCHING);
	}
	private void setJumpingAnimation() {
		myAnimation = createSpriteTransition(Character.JUMPING, Duration.millis(47*Main.MILLISECOND_DELAY), 1);
	}
	private void setInHitstunAnimation() {
		myAnimation = createSpriteTransition(Character.IN_HITSTUN, Duration.millis(300), 1);
	}
	public void startAnimation() {
		myAnimation.play();
	}
	public void stopAnimation() {
		if (myAnimation != null) {
			myAnimation.stop();
		}
	}
	public int getState() {
		return myState;
	}
	public void changeState(int state) {
		if (myState == state) return;
		myState = state;
		setMyAnimation();
	}
	public void changeState(int state, Animation animation) {
		if (myState == state) return;
		myState = state;
		setMyAnimation(animation);
	}
}
