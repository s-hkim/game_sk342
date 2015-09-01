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
	public Game myGame;
	
	private ImageView myImage;
	private ParallelTransition myAnimation;
	private SpriteTransition walkingForward;
	private Timeline walkingBackward;
	private Timeline crouching;
	private ArrayList<Fireball> myHitboxes;
	private boolean inAir = false;
	private boolean attacking = false;
	private boolean left;
	private int myHealth;
	
	public Character(Game g, boolean l, ImageView i) {
		this.myGame = g;
		this.myHitboxes = new ArrayList<Fireball>();
		this.left = l;
		this.myHealth = 100;
		this.myImage = i;
		walkingForward = new SpriteTransition(myImage, Duration.millis(1000),
				5, 1070, new int[]{48,48,48,48}, new int[] {85,85,85,85}, left);
		walkingForward.setCycleCount(1);
		walkingBackward = new Timeline();
		walkingBackward.getKeyFrames().add(new KeyFrame(Duration.millis(1000/60),
				new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (left){
					myImage.setViewport(new Rectangle2D(279, 423, 48, 80));
				} else {
					myImage.setViewport(new Rectangle2D(221, 423, 48, 80));
				}
			}
		}));
		walkingBackward.setCycleCount(5);
		crouching = new Timeline();
		crouching.getKeyFrames().add(new KeyFrame(Duration.millis(1000/60),
				new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (left){
					myImage.setViewport(new Rectangle2D(330, 423, 48, 80));
				} else {
					myImage.setViewport(new Rectangle2D(274 - 56 - 48, 423, 48, 80));
				}
			}
		}));
		crouching.setCycleCount(5);
		idleAnimation();
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
		case "BL":
			attacking = true;
			attackBL();
			break;
		case "QCFL":
			attacking = true;
			shootFireball(10, 5, Animation.INDEFINITE, 5);
			break;
		case "QCFM":
			attacking = true;
			shootFireball(20, 2, 120, 20);
			break;
		case "QCFH":
			attacking = true;
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
    	inAir = true;
    	
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
						myImage.getBoundsInParent().getMaxX() < myGame.myScene.getWidth()) {
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
				// TODO Auto-generated method stub
				inAir = false;
			}
			
		});
		SpriteTransition st = jumpAnimation();
		myAnimation = new ParallelTransition();
		myAnimation.getChildren().addAll(tl, st);
		myAnimation.play();
	}
	public void attackBL() {
		int direction;
		if (left) {
			direction = 1;
		} else {
			direction = 0;
		}
		Bounds bounds = myImage.getBoundsInParent();
		Circle circle = new Circle(bounds.getMinX() + direction* bounds.getWidth(),
				bounds.getMinY() + bounds.getHeight()/2, 15);
		circle.setFill(Color.RED);
		myGame.myRoot.getChildren().add(circle);
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
		tl.setOnFinished(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				myGame.myRoot.getChildren().remove(circle);
				attacking = false;
			}
			
		});
		Fireball fireball = new Fireball(this, circle, tl, 50);
		myHitboxes.add(fireball);
		fireball.executeAction();
	}
	public void shootFireball(int radius, int speed, int duration, int damage) {
		int direction;
		if (left) {
			direction = 1;
		} else {
			direction = -1;
		}
		Bounds bounds = myImage.getBoundsInParent();
		Circle circle = new Circle(bounds.getMinX() + 3*bounds.getWidth()/4 + direction*(40),
				bounds.getMinY() + bounds.getHeight()/3, radius);
		circle.setFill(Color.RED);
		myGame.myRoot.getChildren().add(circle);
		Timeline tl = new Timeline();
		tl.setCycleCount(duration);
		KeyFrame move;
		if (left) {
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
		tl.setOnFinished(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				myGame.myRoot.getChildren().remove(circle);
			}
			
		});
		Fireball fireball = new Fireball(this, circle, tl, damage);
		myHitboxes.add(fireball);
		if (duration == Animation.INDEFINITE) {
			fireballAnimation(Duration.millis(500), fireball);
		} else fireballAnimation(Duration.millis(1000), fireball);
	}
	public void shootH() {
		int direction;
		if (left) {
			direction = 1;
		} else {
			direction = -1;
		}
		Bounds bounds = myImage.getBoundsInParent();
		Circle circle = new Circle(bounds.getMinX() + 3*bounds.getWidth()/4 + direction*(40),
				bounds.getMinY() + bounds.getHeight()/3, 15);
		circle.setFill(Color.RED);
		myGame.myRoot.getChildren().add(circle);
		Timeline tl = new Timeline();
		tl.setCycleCount(Animation.INDEFINITE);
		KeyFrame move;
		if (left) {
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
		return inAir;
	}
	public void setInAir(boolean a) {
		inAir = a;
	}
	public boolean getAttacking() {
		return attacking;
	}
	public boolean getBlocking() {
		return walkingBackward.getStatus() == Animation.Status.RUNNING;
	}
	public boolean getOrientation() {
		return left;
	}
	public void setOrientation(boolean a) {
		left = a;
	}
	public int getHealth() {
		return myHealth;
	}
	public void setHealth(int i) {
		myHealth = i;
	}
	public void inflictDamage(int d) {
		myHealth = myHealth - d;
	}
	
	private void idleAnimation () {
		stopAnimation();
		
		SpriteTransition mySprite = new SpriteTransition(myImage, Duration.millis(1000), 
				3, 0,	new int[]{48,48,48}, new int[]{85,85,85}, left);
		mySprite.setCycleCount(Animation.INDEFINITE);
		myAnimation = new ParallelTransition();
		myAnimation.getChildren().add(mySprite);
		myAnimation.play();
	}
	private void stopAnimation() {
		walkingForward.stop();
		walkingBackward.stop();
		crouching.stop();
		if (myAnimation != null) {
			myAnimation.stop();
		}
	}
	private void fireballAnimation(Duration d, Fireball fireball) {
		attacking = true;
		stopAnimation();
		SpriteTransition mySprite = new SpriteTransition(myImage, d, 5, 2298, 
				new int[]{54,58,65,74}, new int[] {81,81,81,81},left);
		mySprite.setCycleCount(1);
		mySprite.setOnFinished(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				fireball.executeAction();
				idleAnimation();
				attacking = false;
			}
			
		});
		myAnimation = new ParallelTransition();
		myAnimation.getChildren().add(mySprite);
		myAnimation.play();
	}
	private SpriteTransition jumpAnimation() {
		stopAnimation();
		SpriteTransition mySprite = new SpriteTransition(myImage, Duration.millis(47*1000/60), 
				5, 902, new int[]{45,45,45,45}, new int[] {100,100,100,100},left);
		mySprite.setCycleCount(1);
		mySprite.setOnFinished(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//myImage.setTranslateY(myGame.myScene.getHeight()/2+25);
				idleAnimation();
			}
			
		});
		return mySprite;
	}
	private void walkAnimation() {
		walkingBackward.stop();
		crouching.stop();
		if (walkingForward.left != left) {
			walkingForward = new SpriteTransition(myImage, Duration.millis(1000),
					5, 1070, new int[]{48,48,48,48}, new int[] {85,85,85,85}, left);
			walkingForward.setCycleCount(1);
		}
		walkingForward.play();
	}
	private void block() {
		walkingForward.stop();
		crouching.stop();
		walkingBackward.play();
	}
	private void crouch() {
		walkingForward.stop();
		walkingBackward.stop();
		crouching.play();
	}
}
