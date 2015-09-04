import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Character{
	private int myID;
	
	private Game myGame;
	private static final int SPRITE_CENTER = 274;
	private ImageView myImage;
	private Timeline myJumpingPhysics;
	private SpriteTransition myIdle;
	private SpriteTransition myAttack;
	private SpriteTransition myWalkingForward;
	private SpriteTransition myJumpingAnimation;
	private Timeline myWalkingBackward;
	private Timeline myCrouching;
	private ArrayList<Fireball> myHitboxes;
	private boolean myLeft;
	private int myHealth;
	
	public Character(Game g, boolean l, ImageView i, int id) {
		this.myGame = g;
		this.myHitboxes = new ArrayList<Fireball>();
		this.myLeft = l;
		this.myHealth = 100;
		this.myImage = i;
		this.myID = id;
		animateWalkingForward();
		animateWalkingBackward();
		animateCrouching();
		myJumpingPhysics = new Timeline();
		animateIdle();
	}
	
	private void animateCrouching() {
		myCrouching = new Timeline();
		myCrouching.getKeyFrames().add(new KeyFrame(Duration.millis(1000/60),
				new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (myLeft){
					myImage.setViewport(new Rectangle2D(330, 423, 48, 80));
				} else {
					myImage.setViewport(new Rectangle2D(274 - 56 - 48, 423, 48, 80));
				}
			}
		}));
		myCrouching.setCycleCount(1);
		myCrouching.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				animateIdle();
			}
		});
	}
	private void animateWalkingBackward() {
		myWalkingBackward = new Timeline();
		myWalkingBackward.getKeyFrames().add(new KeyFrame(Duration.millis(1000/60),
				new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (myLeft){
					myImage.setViewport(new Rectangle2D(279, 423, 48, 80));
				} else {
					myImage.setViewport(new Rectangle2D(221, 423, 48, 80));
				}
			}
		}));
		myWalkingBackward.setCycleCount(1);
		myWalkingBackward.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				animateIdle();
			}
		});
	}
	private void animateWalkingForward() {
		myWalkingForward = new SpriteTransition(myImage, Duration.millis(500), 
				SPRITE_CENTER, 5, 1070, 
				new int[]{48,48,48,48}, new int[] {85,85,85,85}, myLeft);
		myWalkingForward.setCycleCount(1);
		myWalkingForward.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				animateIdle();
			}
		});
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
		// TODO Auto-generated method stub
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
		stopAnimation();
		myAttack = new SpriteTransition(myImage, Duration.millis(500), 
				SPRITE_CENTER, 3, 87, new int[]{0,49,63,55,75}, new int[]{0,85,85,85,85}, myLeft);
		myAttack.setCycleCount(1);
		myAttack.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				animateIdle();
			}
		});
		createHitbox(5, 28);
		myAttack.play();
	}
	private void mHit() {
		stopAnimation();
		myAttack = new SpriteTransition(myImage, Duration.millis(500), 
				SPRITE_CENTER, 3, 174, new int[]{0,46,76}, new int[]{85,85,85}, myLeft);
		myAttack.setCycleCount(1);
		myAttack.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				animateIdle();
			}
		});
		createHitbox(10, 28);
		myAttack.play();
	}
	private void hHit() {
		stopAnimation();
		myAttack = new SpriteTransition(myImage, Duration.millis(750), 
				SPRITE_CENTER, 1, 1152, new int[]{0,49,56,50,48,70}, new int[]{85,85,85,85,85,85}, myLeft);
		myAttack.setCycleCount(1);
		myAttack.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				animateIdle();
			}
		});
		createHitbox(10, 43);
		myAttack.play();
	}

	public void animateHit() {
		stopAnimation();
		myAttack = new SpriteTransition(myImage, Duration.millis(300), 
				SPRITE_CENTER, 2, 1966, new int[]{0,47,50,52,48}, new int[]{78,78,78,78,78}, myLeft);
		myAttack.setCycleCount(1);
		myAttack.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				animateIdle();
			}
		});
		myAttack.play();
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
		myJumpingAnimation = jumpAnimation();
		myJumpingPhysics = tl;
		myJumpingPhysics.play();
		myJumpingAnimation.play();
	}
	
	public void shootFireball(int radius, int speed, int duration, int damage) {
		int direction;
		if (myLeft) {
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
		if (!myLeft) imageView.setScaleX(-1);
		myGame.getMyRoot().getChildren().add(imageView);
		Timeline tl = new Timeline();
		tl.setCycleCount(duration);
		KeyFrame move;
		if (myLeft) {
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
		// TODO: merge this with shootFireball
		int direction;
		if (myLeft) {
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
		if (!myLeft) imageView.setScaleX(-1);
		myGame.getMyRoot().getChildren().add(imageView);
		Timeline tl = new Timeline();
		tl.setCycleCount(Animation.INDEFINITE);
		KeyFrame move;
		if (myLeft) {
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
	
	public ImageView getMyImage() {
		return myImage;
	}
	public ArrayList<Fireball> getHitboxes() {
		return myHitboxes;
	}
	public boolean getBlocking() {
		return myWalkingBackward.getStatus() == Animation.Status.RUNNING;
	}
	public boolean getOrientation() {
		return myLeft;
	}
	public void setOrientation(boolean a) {
		myLeft = a;
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
	
	private void animateIdle () {
		stopAnimation();
		
		SpriteTransition sprite = new SpriteTransition(myImage, Duration.millis(1000), 
				SPRITE_CENTER,	3, 0,	new int[]{48,48,48}, new int[]{85,85,85}, myLeft);
		sprite.setCycleCount(Animation.INDEFINITE);
		myIdle = sprite;
		myIdle.play();
	}
	private void stopAnimation() {
		myWalkingForward.stop();
		myWalkingBackward.stop();
		myCrouching.stop();
		if (myJumpingAnimation != null) {
			myJumpingAnimation.stop();
		}
		if (myIdle != null) {
			myIdle.stop();
		}
		if (myAttack != null) {
			myAttack.stop();
		}
	}
	private void fireballAnimation(Duration d, Fireball fireball) {
		stopAnimation();
		SpriteTransition sprite = new SpriteTransition(myImage, d, SPRITE_CENTER,
				5, 2298, new int[]{54,58,65,74}, new int[] {81,81,81,81},myLeft);
		sprite.setCycleCount(1);
		sprite.setOnFinished(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				//fireball.executeAction();
				animateIdle();
			}
			
		});
		myAttack = sprite;
		myAttack.play();
		fireball.getMyTimeline().setDelay(d);
		fireball.executeAction();
	}
	private SpriteTransition jumpAnimation() {
		stopAnimation();
		SpriteTransition sprite = new SpriteTransition(myImage, Duration.millis(47*1000/60), 
				SPRITE_CENTER, 5, 902, 
				new int[]{45,45,45,45}, new int[] {100,100,100,100},myLeft);
		sprite.setCycleCount(1);
		sprite.setOnFinished(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				//myImage.setTranslateY(myGame.myScene.getHeight()/2+30);
				animateIdle();
			}
			
		});
		return sprite;
	}
	private void walkAnimation() {
		myIdle.stop();
		myJumpingPhysics.stop();
		myWalkingBackward.stop();
		myCrouching.stop();
		if (myWalkingForward.isLeft() != myLeft) {
			animateWalkingForward();
		}
		myWalkingForward.play();
	}
	private void block() {
		myIdle.stop();
		myJumpingPhysics.stop();
		myWalkingForward.stop();
		myCrouching.stop();
		myWalkingBackward.play();
	}
	private void crouch() {
		myIdle.stop();
		myJumpingPhysics.stop();
		myWalkingForward.stop();
		myWalkingBackward.stop();
		myCrouching.play();
	}
	public boolean getInAir() {
		return myJumpingPhysics.getStatus() == Animation.Status.RUNNING;
	}
	public boolean getAttacking() {
		if (myAttack == null) {
			return false;
		}
		return myAttack.getStatus() == Animation.Status.RUNNING;
	}
}
