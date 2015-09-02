import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Character{
	private int myID;
	
	private Game myGame;
	private int mySpriteCenterOffset;
	private ImageView myImage;
	private ParallelTransition myAnimation;
	private SpriteTransition myWalkingForward;
	private Timeline myWalkingBackward;
	private Timeline myCrouching;
	private ArrayList<Fireball> myHitboxes;
	private boolean myInAir = false;
	private boolean myAttacking = false;
	private boolean myLeft;
	private int myHealth;
	
	public Character(Game g, boolean l, ImageView i, int id) {
		this.myGame = g;
		this.mySpriteCenterOffset = 274;
		this.myHitboxes = new ArrayList<Fireball>();
		this.myLeft = l;
		this.myHealth = 100;
		this.myImage = i;
		this.myID = id;
		animateWalkingForward();
		animateWalkingBackward();
		animateCrouching();
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
		myCrouching.setCycleCount(5);
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
		myWalkingBackward.setCycleCount(5);
		myWalkingBackward.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				animateIdle();
			}
		});
	}
	private void animateWalkingForward() {
		myWalkingForward = new SpriteTransition(myImage, Duration.millis(1000), 
				mySpriteCenterOffset, 5, 1070, 
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
			// TODO
			break;
		case "M":
			// TODO
			break;
		case "H":
			// TODO
			break;
		case "BL":
			myAttacking = true;
			attackBL();
			break;
		case "QCFL":
			myAttacking = true;
			shootFireball(10, 5, Animation.INDEFINITE, 5);
			break;
		case "QCFM":
			myAttacking = true;
			shootFireball(20, 2, 120, 20);
			break;
		case "QCFH":
			myAttacking = true;
			shootH();
			break;
		default:
			break;
		}
	}
	
	/*
	 * Animations/actions
	 */
	private void jump(double dx) {
    	myInAir = true;
    	
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
				//System.out.println(myTopBlock.getY());
			}
		});
		tl.getKeyFrames().add(moveJump);
		tl.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				myInAir = false;
			}
			
		});
		SpriteTransition st = jumpAnimation();
		myAnimation = new ParallelTransition();
		myAnimation.getChildren().addAll(tl, st);
		myAnimation.play();
	}
	public void attackBL() {
		int direction;
		if (myLeft) {
			direction = 1;
		} else {
			direction = 0;
		}
		Bounds bounds = myImage.getBoundsInParent();
		Circle circle = new Circle(bounds.getMinX() + direction* bounds.getWidth(),
				bounds.getMinY() + bounds.getHeight()/2, 15);
		circle.setFill(Color.RED);
		myGame.getMyRoot().getChildren().add(circle);
		Timeline tl = new Timeline();
		tl.setCycleCount(20);
		KeyFrame move = new KeyFrame(Duration.millis(1000/60),
				new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				circle.setCenterX(bounds.getMinX() + direction* bounds.getWidth());
				circle.setCenterY(bounds.getMinY() + bounds.getHeight()/2);
			}
		
		});
		tl.getKeyFrames().add(move);
		Fireball fireball = new Fireball(this, circle, tl, 50);
		tl.setOnFinished(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				myHitboxes.remove(fireball);
				myGame.getMyRoot().getChildren().remove(circle);
				myAttacking = false;
			}
			
		});
		myHitboxes.add(fireball);
		fireball.executeAction();
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
		myGame.getMyRoot().getChildren().add(circle);
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
		Fireball fireball = new Fireball(this, circle, tl, damage);
		tl.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				myHitboxes.remove(fireball);
				myGame.getMyRoot().getChildren().remove(circle);
			}
		});
		myHitboxes.add(fireball);
		if (duration == Animation.INDEFINITE) {
			fireballAnimation(Duration.millis(500), fireball);
		} else fireballAnimation(Duration.millis(1000), fireball);
	}
	public void shootH() {
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
		myGame.getMyRoot().getChildren().add(circle);
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
		Fireball fireball = new Fireball(this, circle, tl, 5);
		myHitboxes.add(fireball);
		fireballAnimation(Duration.millis(500), fireball);
	}
	
	public ImageView getHurtbox() {
		return myImage;
	}
	public ArrayList<Fireball> getHitboxes() {
		return myHitboxes;
	}
	public boolean getInAir() {
		return myInAir;
	}
	public void setInAir(boolean a) {
		myInAir = a;
	}
	public boolean getAttacking() {
		return myAttacking;
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
				mySpriteCenterOffset,	3, 0,	new int[]{48,48,48}, new int[]{85,85,85}, myLeft);
		sprite.setCycleCount(Animation.INDEFINITE);
		myAnimation = new ParallelTransition();
		myAnimation.getChildren().add(sprite);
		myAnimation.play();
	}
	private void stopAnimation() {
		myWalkingForward.stop();
		myWalkingBackward.stop();
		myCrouching.stop();
		if (myAnimation != null) {
			myAnimation.stop();
		}
	}
	private void fireballAnimation(Duration d, Fireball fireball) {
		myAttacking = true;
		stopAnimation();
		SpriteTransition sprite = new SpriteTransition(myImage, d, mySpriteCenterOffset,
				5, 2298, new int[]{54,58,65,74}, new int[] {81,81,81,81},myLeft);
		sprite.setCycleCount(1);
		sprite.setOnFinished(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				fireball.executeAction();
				animateIdle();
				myAttacking = false;
			}
			
		});
		myAnimation = new ParallelTransition();
		myAnimation.getChildren().add(sprite);
		myAnimation.play();
	}
	private SpriteTransition jumpAnimation() {
		stopAnimation();
		SpriteTransition sprite = new SpriteTransition(myImage, Duration.millis(47*1000/60), 
				mySpriteCenterOffset, 5, 902, 
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
		myAnimation.stop();
		myWalkingBackward.stop();
		myCrouching.stop();
		if (myWalkingForward.isLeft() != myLeft) {
			animateWalkingForward();
		}
		myWalkingForward.play();
	}
	private void block() {
		myAnimation.stop();
		myWalkingForward.stop();
		myCrouching.stop();
		myWalkingBackward.play();
	}
	private void crouch() {
		myAnimation.stop();
		myWalkingForward.stop();
		myWalkingBackward.stop();
		myCrouching.play();
	}
}
