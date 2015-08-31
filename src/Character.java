import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Character{
	public Game myGame;

	private ImageView myHurtbox;
	private ArrayList<Fireball> myHitboxes;
	private boolean inAir = false;
	private boolean attacking = false;
	private boolean left;
	private int myHealth;
	
	public Character(Game g, ImageView h, boolean l) {
		myGame = g;
		myHurtbox = h;
		myHitboxes = new ArrayList<Fireball>();
		left = l;
		myHealth = 100;
	}
	public void executeAction(String instructions) {
		switch (instructions) {
		case "U":
			jump(0);
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
			shootFireball(20, 2, 120, 15);
			break;
		case "QCFH":
			attacking = true;
			shootH();
			break;
		default: 
			attacking = false;
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
				double prevX = myHurtbox.getX();
				double prevY = myHurtbox.getY();
				if (prevX > 0 && prevX + myHurtbox.getBoundsInLocal().getWidth() < myGame.myScene.getWidth()) {
					myHurtbox.setX(prevX + dx*5);
				}
				myHurtbox.setY(prevY - dy);
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
		tl.play();
	}
	public void attackBL() {
		int direction;
		if (left) {
			direction = 1;
		} else {
			direction = 0;
		}
		Circle circle = new Circle(myHurtbox.getX() + direction* myHurtbox.getBoundsInLocal().getWidth(),
				myHurtbox.getY() + myHurtbox.getBoundsInLocal().getHeight()/2, 15);
		circle.setFill(Color.RED);
		myGame.myRoot.getChildren().add(circle);
		Timeline tl = new Timeline();
		tl.setCycleCount(20);
		KeyFrame move = new KeyFrame(Duration.millis(1000/60),
				new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				circle.setCenterX(myHurtbox.getX() + direction* myHurtbox.getBoundsInLocal().getWidth());
				circle.setCenterY(myHurtbox.getY() + myHurtbox.getBoundsInLocal().getHeight()/2);
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
		Circle circle = new Circle(myHurtbox.getX() + myHurtbox.getBoundsInLocal().getWidth()
				/2, myHurtbox.getY() + myHurtbox.getBoundsInLocal().getHeight()/2, radius);
		circle.setFill(Color.RED);
		myGame.myRoot.getChildren().add(circle);
		Timeline tl = new Timeline();
		tl.setCycleCount(duration);
		KeyFrame move;
		if (left) {
			move = new KeyFrame(Duration.millis(1000/60),
					new EventHandler<ActionEvent>() {
				int frame = 0;
				public void handle(ActionEvent event) {
					if(frame == 11) {
						attacking = false;
					}
					double prevX = circle.getCenterX();
					circle.setCenterX(prevX + speed);
					frame++;
				}
			});
		} else {
			move = new KeyFrame(Duration.millis(1000/60),
					new EventHandler<ActionEvent>() {
				int frame = 0;
				public void handle(ActionEvent event) {
					if(frame == 11) {
						attacking = false;
					}
					double prevX = circle.getCenterX();
					circle.setCenterX(prevX - speed);
					frame++;
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
		fireball.executeAction();
	}
	public void shootH() {
		Circle circle = new Circle(myHurtbox.getX() + myHurtbox.getBoundsInLocal().getWidth()
				/2, myHurtbox.getY() + myHurtbox.getBoundsInLocal().getHeight()/2, 15);
		circle.setFill(Color.RED);
		myGame.myRoot.getChildren().add(circle);
		Timeline tl = new Timeline();
		tl.setCycleCount(Animation.INDEFINITE);
		KeyFrame move;
		if (left) {
			move = new KeyFrame(Duration.millis(1000/60),
					new EventHandler<ActionEvent>() {
				int frame = 0;
				public void handle(ActionEvent event) {
					if(frame == 15) {
						attacking = false;
					}
					double prevX = circle.getCenterX();
					double prevY = circle.getCenterY();
					circle.setCenterX(prevX + 4);
					circle.setCenterY(prevY - 4);
					frame++;
				}
			});
		} else {
			move = new KeyFrame(Duration.millis(1000/60),
					new EventHandler<ActionEvent>() {
				int frame = 0;
				public void handle(ActionEvent event) {
					if(frame == 15) {
						attacking = false;
					}
					double prevX = circle.getCenterX();
					double prevY = circle.getCenterY();
					circle.setCenterX(prevX - 4);
					circle.setCenterY(prevY - 4);
					frame++;
				}
			});
		}
		tl.getKeyFrames().add(move);
		Fireball fireball = new Fireball(this, circle, tl, 5);
		myHitboxes.add(fireball);
		fireball.executeAction();
	}
	
	public ImageView getHurtbox() {
		return myHurtbox;
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
}
