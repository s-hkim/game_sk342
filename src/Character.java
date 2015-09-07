import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Character{
	public static final int IDLING = 0;
	public static final int WALKING_FORWARD = 1;
	public static final int WALKING_BACKWARD = 2;
	public static final int CROUCHING = 3;
	public static final int JUMPING = 4;
	public static final int ATTACKING = 5;
	public static final int IN_HITSTUN = 6;
	public static final int SPRITE_CENTER = 274;
	
	private int myID;
	private Game myGame;
	private ImageView myImage;
	private Timeline myJumpingPhysics;
	private ArrayList<Fireball> myHitboxes;
	private boolean myIsLeft;
	private int myHealth;
	
	private CharacterState myState;
	
	public Character(Game g, boolean l, ImageView i, int id) {
		this.myGame = g;
		this.myHitboxes = new ArrayList<Fireball>();
		this.myIsLeft = l;
		this.myHealth = 100;
		this.myImage = i;
		this.myID = id;
		
		myState = new CharacterState(this, IDLING);
	}
	
	public void executeAction(String instructions) {
		switch (instructions) {
		case "F":
			walkAnimation();
			break;
		case "B":
			block();
			break;
		case "U":
			jump(0);
			break;
		case "D":
			crouch();
			break;
		case "UF":
			jump(1);
			break;
		case "UB":
			jump(-1);
			break;
		case "L":
			lHit();
			break;
		case "M":
			mHit();
			break;
		case "H":
			hHit();
			break;
		case "QCFL":
			shootFireball(10, 5, Animation.INDEFINITE, 5);
			break;
		case "QCFM":
			shootFireball(20, 2, 120, 20);
			break;
		case "QCFH":
			shootH();
			break;
		default:
			break;
		}
	}
	private void createHitbox(int damage, int cycleCount) {
		Bounds bounds = myImage.getBoundsInParent();
		Circle hitbox = new Circle(bounds.getMinX() + bounds.getWidth()/2, bounds.getMinY() + bounds.getHeight()/2,
				bounds.getWidth()/2);
		Timeline tl = new Timeline();
		tl.setCycleCount(cycleCount);
		KeyFrame move = new KeyFrame(Duration.millis(1000/60),
				new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
			}
		});
		tl.getKeyFrames().add(move);
		Fireball fireball = new Fireball(this, hitbox, tl, damage, null);
		tl.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				myHitboxes.remove(fireball);
			}
		});
		myHitboxes.add(fireball);
		fireball.executeAction();
	}
	private void lHit() {
		SpriteTransition attacking = new SpriteTransition(myImage, Duration.millis(500), 
				SPRITE_CENTER, new SpriteObject(3, 87, new int[]{0,49,63,55,75}, new int[]{0,85,85,85,85}), myIsLeft);
		attacking.setCycleCount(1);
		attacking.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				animateIdle();
			}
		});
		createHitbox(5, 28);
		changeState(ATTACKING, attacking);
	}
	private void mHit() {
		SpriteTransition attacking = new SpriteTransition(myImage, Duration.millis(500), 
				SPRITE_CENTER, new SpriteObject(3, 174, new int[]{0,46,76}, new int[]{85,85,85}), myIsLeft);
		attacking.setCycleCount(1);
		attacking.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				animateIdle();
			}
		});
		createHitbox(10, 28);
		changeState(ATTACKING, attacking);
	}
	private void hHit() {
		SpriteTransition attacking = new SpriteTransition(myImage, Duration.millis(750), 
				SPRITE_CENTER, new SpriteObject(1, 1152, new int[]{0,49,56,50,48,70}, new int[]{85,85,85,85,85,85}), myIsLeft);
		attacking.setCycleCount(1);
		attacking.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				animateIdle();
			}
		});
		createHitbox(10, 43);
		changeState(ATTACKING, attacking);
	}

	public void animateHit() {
		changeState(IN_HITSTUN);
	}
	/*
	 * Animations/actions
	 */
	private void jump(double dx) {
		Timeline tl = new Timeline();
		tl.setCycleCount(47);
		KeyFrame moveJump = new KeyFrame(Duration.millis(1000/60),
				new EventHandler<ActionEvent>() {
			double dy = 12;
			public void handle(ActionEvent event) {
				dy = dy - 0.5;
				double prevX = myImage.getX();
				double prevY = myImage.getY();
				if (myImage.getBoundsInParent().getMinX() > 0 && 
						myImage.getBoundsInParent().getMaxX() < myGame.getMyScene().getWidth()) {
					myImage.setX(prevX + dx*5);
				}
				myImage.setY(prevY - dy);
			}
		});
		tl.getKeyFrames().add(moveJump);
		// originally combined into a parallel transition
		//myJumpingAnimation = jumpAnimation();
		myJumpingPhysics = tl;
		myJumpingPhysics.play();
		jumpAnimation();
		//myJumpingAnimation.play();
	}
	
	public void shootFireball(int radius, int speed, int duration, int damage) {
		int direction;
		if (myIsLeft) {
			direction = 1;
		} else {
			direction = -1;
		}
		Bounds bounds = myImage.getBoundsInParent();
		Circle circle = new Circle(bounds.getMinX() + 3*bounds.getWidth()/4 + direction*(40),
				bounds.getMinY() + bounds.getHeight()/3, radius);
		circle.setFill(Color.RED);
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("fireball.png"));
		ImageView imageView = new ImageView(image);
		if (!myIsLeft) imageView.setScaleX(-1);
		myGame.getMyRoot().getChildren().add(imageView);
		Timeline tl = new Timeline();
		tl.setCycleCount(duration);
		KeyFrame move;
		if (myIsLeft) {
			move = new KeyFrame(Duration.millis(1000/60),
					new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					double prevX = circle.getCenterX();
					circle.setCenterX(prevX + speed);
				}
			});
		} else {
			move = new KeyFrame(Duration.millis(1000/60),
					new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					double prevX = circle.getCenterX();
					circle.setCenterX(prevX - speed);
				}
			});
		}
		
		tl.getKeyFrames().add(move);
		
		Fireball fireball = new Fireball(this, circle, tl, damage, imageView);
		tl.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				myHitboxes.remove(fireball);
				myGame.getMyRoot().getChildren().remove(imageView);
			}
		});
		fireball.updateImage();
		myHitboxes.add(fireball);
		if (duration == Animation.INDEFINITE) {
			fireballAnimation(Duration.millis(500), fireball);
		} else fireballAnimation(Duration.millis(1000), fireball);
	}
	public void shootH() {
		int direction;
		if (myIsLeft) {
			direction = 1;
		} else {
			direction = -1;
		}
		Bounds bounds = myImage.getBoundsInParent();
		Circle circle = new Circle(bounds.getMinX() + 3*bounds.getWidth()/4 + direction*(40),
				bounds.getMinY() + bounds.getHeight()/3, 15);
		circle.setFill(Color.RED);
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("fireball.png"));
		ImageView imageView = new ImageView(image);
		if (!myIsLeft) imageView.setScaleX(-1);
		myGame.getMyRoot().getChildren().add(imageView);
		Timeline tl = new Timeline();
		tl.setCycleCount(Animation.INDEFINITE);
		KeyFrame move;
		if (myIsLeft) {
			move = new KeyFrame(Duration.millis(1000/60),
					new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					double prevX = circle.getCenterX();
					double prevY = circle.getCenterY();
					circle.setCenterX(prevX + 4);
					circle.setCenterY(prevY - 4);
				}
			});
		} else {
			move = new KeyFrame(Duration.millis(1000/60),
					new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					double prevX = circle.getCenterX();
					double prevY = circle.getCenterY();
					circle.setCenterX(prevX - 4);
					circle.setCenterY(prevY - 4);
				}
			});
		}
		tl.getKeyFrames().add(move);
		Fireball fireball = new Fireball(this, circle, tl, 15, imageView);
		fireball.updateImage();
		myHitboxes.add(fireball);
		fireballAnimation(Duration.millis(500), fireball);
	}
	
	
	
	private void animateIdle () {
		changeState(IDLING);
	}
	private void fireballAnimation(Duration d, Fireball fireball) {
		
		SpriteTransition sprite = new SpriteTransition(myImage, d, SPRITE_CENTER,
				new SpriteObject(5, 2298, new int[]{54,58,65,74}, new int[] {81,81,81,81}),myIsLeft);
		sprite.setCycleCount(1);
		sprite.setOnFinished(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				animateIdle();
			}
			
		});
		changeState(ATTACKING, sprite);
		fireball.getMyTimeline().setDelay(d);
		fireball.executeAction();
	}
	private void jumpAnimation() {
		changeState(JUMPING);
	}
	private void walkAnimation() {
		changeState(WALKING_FORWARD);
	}
	private void block() {
		changeState(WALKING_BACKWARD);
	}
	private void crouch() {
		changeState(CROUCHING);
	}
	public boolean isTakingAction() {
		int state = getState();
		return state == JUMPING || state == ATTACKING || state == IN_HITSTUN;
	}
	public ImageView getMyImage() {
		return myImage;
	}
	public ArrayList<Fireball> getHitboxes() {
		return myHitboxes;
	}
	public boolean getOrientation() {
		return myIsLeft;
	}
	public void setOrientation(boolean a) {
		myIsLeft = a;
	}
	public int getHealth() {
		return myHealth;
	}
	public void setHealth(int i) {
		myHealth = i;
	}
	public int getMyID() {
		return myID;
	}
	public void inflictDamage(int d) {
		myHealth = myHealth - d;
	}
	
	public int getState() {
		return this.myState.getState();
	}
	public void changeState(int state) {
		this.myState.changeState(state);
	}
	public void changeState(int state, Animation animation) {
		this.myState.changeState(state, animation);
	}
}
