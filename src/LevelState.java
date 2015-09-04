import java.util.ArrayList;
import java.util.LinkedList;

import javafx.animation.Animation;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LevelState extends GameState {
	private static final int PLAYER_1 = 0;
	private static final int PLAYER_2 = 1;
	private static final int STOCK_LEVEL = 0;
	private static final int TIMED_LEVEL = 1;
	private Game myGame;
	private InputListener myInputs;
	private Character myPlayer;
	private Character myEnemy;
	private Text myPlayerText;
	private Text myEnemyText;
	private Text myTimerText;
	private Text myInputsText;
	private Group myRoot;
	double mySceneWidth;
	double mySceneHeight;
	private int myFrameCount;
	private int myLevelType;
	private int myTimer = 100;

    private InputTree myMoveTree;
	
	public LevelState (Game g, int type) {
		myGame = g;
		
		myLevelType = type;
		myRoot = myGame.getMyRoot();
		myInputs = myGame.getMyInputManager();
		myInputs.getPlayerKeyCombo().clear();
		
		myFrameCount = 0;
		myRoot.getChildren().clear();
		Scene scene = myGame.getMyScene();
		mySceneWidth = scene.getWidth();
		mySceneHeight = scene.getHeight();
		
		createBackground();
		createPlayer();
		createEnemy();
        createCharacterTexts();
        createInputsText();
		createMoveTree();
		if (myLevelType == TIMED_LEVEL) {
			createTimerText();
		}
    	
	}

	private void createTimerText() {
		// TODO Auto-generated method stub
		myTimerText = new Text();
        myTimerText.setFont(new Font("Courier", 100));
        myTimerText.setX(320);
        myTimerText.setY(100);
        myTimerText.setStroke(Color.RED);
        myRoot.getChildren().add(myTimerText);
	}

	private void createMoveTree() {
		myMoveTree = new InputTree();
    	myMoveTree.addMove(new String[]{"LIGHT", "FORWARD", "DOWN"}, "QCFL");
    	myMoveTree.addMove(new String[]{"LIGHT", "BACKWARD"}, "BL");
    	myMoveTree.addMove(new String[]{"MEDIUM", "FORWARD","DOWN"}, "QCFM");
    	myMoveTree.addMove(new String[]{"HARD", "FORWARD", "DOWN"}, "QCFH");
    	myMoveTree.addMove(new String[]{"LIGHT"}, "L");
    	myMoveTree.addMove(new String[]{"MEDIUM"}, "M");
    	myMoveTree.addMove(new String[]{"HARD"}, "H");
	}

	private void createEnemy() {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("spritesheet2.gif"));
		ImageView enemyImageView = new ImageView(image);
		enemyImageView.setViewport(new Rectangle2D(221, 0, 50, 85));
		myRoot.getChildren().add(enemyImageView);
        
		enemyImageView.setTranslateX(mySceneWidth/2 + 150);
		enemyImageView.setTranslateY(mySceneHeight/2+45);
        myEnemy = new Character(myGame, false, enemyImageView, PLAYER_2);
	}

	private void createPlayer() {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("spritesheet.gif"));
		ImageView playerImageView = new ImageView(image);
		playerImageView.setViewport(new Rectangle2D(277, 0, 50, 85));
		myRoot.getChildren().add(playerImageView);

		playerImageView.setTranslateX(mySceneWidth/2 - 200);
		playerImageView.setTranslateY(mySceneHeight/2+45);
        myPlayer = new Character(myGame, true, playerImageView, PLAYER_1);
	}

	private void createBackground() {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("backgroundSprite.png"));
		ImageView background = new ImageView(image);
		background.setViewport(new Rectangle2D(0,0,800,336));
		myRoot.getChildren().add(background);
		SpriteTransition backgroundAnimation = new SpriteTransition(background, 
				Duration.millis(1000), 0, 0, 0, new int[]{800,800,800,800,800,800,800,800}, 
				new int[]{336,336,336,336,336,336,336,336}, true);
		backgroundAnimation.setCycleCount(Animation.INDEFINITE);
		backgroundAnimation.play();
	}

	private void createCharacterTexts() {
		myPlayerText = new Text();
        myPlayerText.setFont(new Font("Courier", 100));
        myPlayerText.setX(100);
        myPlayerText.setY(100);
        myRoot.getChildren().add(myPlayerText);
        myEnemyText = new Text();
        myEnemyText.setFont(new Font("Courier", 100));
        myEnemyText.setX(550);
        myEnemyText.setY(100);
        myRoot.getChildren().add(myEnemyText);

        myPlayerText.setText(""+myPlayer.getHealth());
        myEnemyText.setText(""+myEnemy.getHealth());
        myPlayerText.setStroke(Color.WHITE);
        myEnemyText.setStroke(Color.WHITE);
	}

	private void createInputsText() {
		myInputsText = new Text();
        myInputsText.setX(50);
        myInputsText.setY(150);
        myInputsText.setStroke(Color.WHITE);
        myRoot.getChildren().add(myInputsText);
        myInputsText.setVisible(false);
	}
	
	@Override
	public void update() {
		handleOrientation();
		
		handleCollision(myPlayer, myEnemy);
		handleCollision(myEnemy, myPlayer);
		
		myInputsText.setText(myInputs.getPlayerKeyCombo().toString());
		if (myLevelType == STOCK_LEVEL) {
			checkDeath(myPlayer);
			checkDeath(myEnemy);
		}
		
        doCharacterAction(myPlayer);
        doCharacterAction(myEnemy);
        if (myInputs.isEnterPressed()) {
        	myInputsText.setVisible(!myInputsText.isVisible());
        }
        if (myInputs.isSpacePressed()) {
        	myPlayer.setHealth(100);
        	myEnemy.setHealth(100);
        	myPlayerText.setText(""+myPlayer.getHealth());
        	myEnemyText.setText(""+myEnemy.getHealth());
        }
        if (myLevelType == TIMED_LEVEL) {
        	updateTimer();
        }
        if (myFrameCount == 30) {
            myGame.getMyInputManager().discardInputsFromBoth();
        }
        myFrameCount++;
        if (myFrameCount > 30) { 
        	myFrameCount = 0;
        }
	}
	private void updateTimer() {
		// TODO Auto-generated method stub
		if (myFrameCount == 30) {
			myTimer--;
		}
		myTimerText.setText(""+myTimer);
		if (myTimer <= 0) {
			winByTime();
		}
	}

	private void winByTime() {
		if (myPlayer.getHealth() > myEnemy.getHealth()) {
			setLoss(myEnemy);
		} else setLoss(myPlayer);
	}

	private void checkDeath(Character character) {
		if (character.getHealth() <= 0) {
			if (character.getMyID() == PLAYER_1) {
				myPlayerText.setText("KO");
			}
			if (character.getMyID() == PLAYER_2) {
				myEnemyText.setText("KO");
			}
			setLoss(character);
		}
	}
	private void handleOrientation() {
		ImageView playerHurtbox = myPlayer.getMyImage();
		ImageView enemyHurtbox = myEnemy.getMyImage();
		double myPlayerWidth = playerHurtbox.getBoundsInParent().getWidth();
//      double myPlayerHeight = playerHurtbox.getBoundsInParent().getHeight();
		double myEnemyCenter = enemyHurtbox.getBoundsInParent().getMinX() + enemyHurtbox.getBoundsInParent().getWidth() / 2;
		double myPlayerCenter = playerHurtbox.getBoundsInParent().getMinX() + myPlayerWidth/2;
		
		if (myEnemyCenter - myPlayerCenter > 0) {
			myPlayer.setOrientation(true);
			myEnemy.setOrientation(false);
		} else {
			myPlayer.setOrientation(false);
			myEnemy.setOrientation(true);
		}
		myInputs.setPlayerLeft(myPlayer.getOrientation());
		myInputs.setEnemyLeft(myEnemy.getOrientation());
	}
	private void handleCollision(Character actor, Character victim) {
		ArrayList<Fireball> hitboxes = actor.getHitboxes();
		ArrayList<Fireball> tempArray = new ArrayList<Fireball>(hitboxes);
		Text text;
		if (victim == myPlayer) {
			text = myPlayerText;
		} else if (victim == myEnemy) {
			text = myEnemyText;
		} else {
			return;
		}
		for (Fireball f: tempArray) {
			f.updateImage();
			Circle c = f.getMyHitbox();
			ImageView fireballImage = f.getMyImage();
    		double cx = c.getCenterX();
    		double cr = c.getRadius();
    		if (cx - cr < 0 || cx + cr > mySceneWidth) {
    			myRoot.getChildren().removeAll(c, fireballImage);
    		}
    		if (c.getBoundsInParent().intersects(victim.getMyImage().getBoundsInParent())) {
    			victim.inflictDamage(f.getMyDamage());
    			victim.animateHit();
    			text.setText(""+victim.getHealth());
    			myRoot.getChildren().removeAll(c, fireballImage);
    			hitboxes.remove(f);
    			// TODO: remove victim's hitboxes?
    		}
    	}
		if (actor.getAttacking()) {
			Bounds actorBounds = actor.getMyImage().getBoundsInParent();
			Bounds victimBounds = victim.getMyImage().getBoundsInParent();
			if (actorBounds.intersects(victimBounds)) {
				// TODO: figure this out
			}
		}
	}
	private void doCharacterAction(Character character) {
		boolean up;
		boolean down;
		boolean right;
		boolean left;
		boolean back;
		boolean front;
		ImageView hurtbox = character.getMyImage();
		LinkedList<String> inputCodes;
		if (character.getMyID() == PLAYER_1) {
			up = myInputs.iswPressed();
			right = myInputs.isdPressed();
			left = myInputs.isaPressed();
			down = myInputs.issPressed();
			front = myInputs.isPlayerForwardPressed();
			back = myInputs.isPlayerBackwardPressed();
			inputCodes = myInputs.getPlayerKeyCombo();
		} else if (character.getMyID() == PLAYER_2) {
			up = myInputs.isUpPressed();
			right = myInputs.isRightPressed();
			left = myInputs.isLeftPressed();
			down = myInputs.isDownPressed();
			front = myInputs.isEnemyForwardPressed();
			back = myInputs.isEnemyBackwardPressed();
			inputCodes = myInputs.getEnemyKeyCombo();
		} else {
			return;
		}
		if (!character.getInAir() && !character.getAttacking()) {

        	String instruction = myMoveTree.parseInput(inputCodes);
        	if (instruction != null) {
        		inputCodes.clear();
        		character.executeAction(instruction);
        	}
        	
        	else if (up && right) {
        		character.executeAction("UF");
        	} else if (up && left) {
        		character.executeAction("UB");
        	} else if (up) {
        		character.executeAction("U");
        	} else if (down || 
        			(down && back)) {
        		character.executeAction("D");
        	} else if (right) {
        		if (hurtbox.getBoundsInParent().getMaxX() < mySceneWidth) {
        			hurtbox.setX(hurtbox.getX()+ 2);
        		}
        		if (front) {
            		character.executeAction("F");
        		} else {
        			character.executeAction("B");
        		}
        	} else if (left) {
        		if (hurtbox.getBoundsInParent().getMinX() > 0) {
        			hurtbox.setX(hurtbox.getX()- 2);
        		}
        		if (back) {
            		character.executeAction("B");
        		} else {
        			character.executeAction("F");
        		}
        	}
        }
	}
	private void setLoss(Character character) {
		myInputs.discardInputsFromBoth();
		myGame.changeState(new VictoryState(myGame, character.getMyID()));
	}
}
