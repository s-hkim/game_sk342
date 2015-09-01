import java.util.ArrayList;
import java.util.LinkedList;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class LevelState extends GameState {
	private Game myGame;
	private InputListener inputs;
	private Character myPlayer;
	private Character myEnemy;
	private Text p1Text;
	private Text p2Text;
	private Text inputText;
    
	private Scene myScene;
	private Group myRoot;
	double width;
	double height;
	private int frameCount;

    public InputTree moveTree;
	
	public LevelState (Game g) {
		myGame = g;
		myScene = myGame.myScene;
		myRoot = myGame.myRoot;
		inputs = myGame.myInputManager;
		inputs.keyCombo.clear();
		
		frameCount = 0;
		myRoot.getChildren().clear();
		width = myScene.getWidth();
		height = myScene.getHeight();
		
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("background.png"));
        ImageView background = new ImageView(image);
        myRoot.getChildren().add(background);
        
        image = new Image(getClass().getClassLoader().getResourceAsStream("spritesheet.gif"));
		ImageView imageView = new ImageView(image);
		imageView.setViewport(new Rectangle2D(277, 0, 50, 85));
		myGame.myRoot.getChildren().add(imageView);
		ImageView imageView2 = new ImageView(image);
		imageView2.setViewport(new Rectangle2D(277, 0, 50, 85));
		myGame.myRoot.getChildren().add(imageView2);
        
		imageView.setTranslateX(width/2 - 200);
		imageView.setTranslateY(height/2+25);
		imageView2.setTranslateX(width/2 + 150);
		imageView2.setTranslateY(height/2+25);
		
        image = new Image(getClass().getClassLoader().getResourceAsStream("duke.gif"));        
		myPlayer = new Character(myGame, true, imageView);
        myEnemy = new Character(myGame, false, imageView2);
        
		//myRoot.getChildren().add(playerImage);
        //myRoot.getChildren().add(enemyImage);
		
        p1Text = new Text();
        p1Text.setX(100);
        p1Text.setY(50);
        p1Text.setText(""+myPlayer.getHealth());
        myRoot.getChildren().add(p1Text);
        p2Text = new Text();
        p2Text.setX(700);
        p2Text.setY(50);
        p2Text.setText(""+myEnemy.getHealth());
        myRoot.getChildren().add(p2Text);
        inputText = new Text();
        inputText.setX(50);
        inputText.setY(100);
        myRoot.getChildren().add(inputText);
        
		moveTree = new InputTree();
		String[] codes = {KeyCode.A.toString(), "FORWARD", KeyCode.DOWN.toString()};
    	String[] codes2 = {KeyCode.A.toString(), "BACKWARD"};
//    	LinkedList<String> inputCodes = new LinkedList<String>();
//    	inputCodes.add(KeyCode.DOWN.toString());
//    	inputCodes.add("FORWARD");
//    	inputCodes.add(KeyCode.A.toString());
    	moveTree.addMove(codes, "QCFL");
    	moveTree.addMove(codes2, "BL");
    	codes = new String[]{KeyCode.S.toString(), "FORWARD", KeyCode.DOWN.toString()};
    	moveTree.addMove(codes, "QCFM");
    	codes = new String[]{KeyCode.D.toString(), "FORWARD", KeyCode.DOWN.toString()};
    	moveTree.addMove(codes, "QCFH");
//    	codes = new String[]{"FORWARD"};
//    	moveTree.addMove(codes, "F");
//    	moveTree.printTree(moveTree.myRoot);
//    	System.out.println("");
//    	System.out.println(moveTree.parseInput(inputCodes));
    	
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		ImageView playerHurtbox = myPlayer.getHurtbox();
		ImageView enemyHurtbox = myEnemy.getHurtbox();
		double myPlayerWidth = playerHurtbox.getBoundsInParent().getWidth();
        // double myPlayerHeight = playerHurtbox.getBoundsInParent().getHeight();
		double myEnemyCenter = enemyHurtbox.getBoundsInParent().getMinX() + enemyHurtbox.getBoundsInParent().getWidth() / 2;
		double myPlayerCenter = playerHurtbox.getBoundsInParent().getMinX() + myPlayerWidth/2;
		
		if (myEnemyCenter - myPlayerCenter > 0) {
			myPlayer.setOrientation(true);
		} else {
			myPlayer.setOrientation(false);
		}
		inputs.setOrientation(myPlayer.getOrientation());
		ArrayList<Fireball> playerHitboxes = myPlayer.getHitboxes();
		ArrayList<Fireball> tempArray = new ArrayList<Fireball>(playerHitboxes);
		for (Fireball f: tempArray) {
			Circle c = f.myHitbox;
    		double cx = c.getCenterX();
    		double cr = c.getRadius();
    		if (cx - cr < 0 || cx + cr > myScene.getWidth()) {
    			myRoot.getChildren().remove(c);
    		}
    		if (c.getBoundsInParent().intersects(enemyHurtbox.getBoundsInParent())) {
    			myEnemy.inflictDamage(f.myDamage);
    			p2Text.setText(""+myEnemy.getHealth());
    			myRoot.getChildren().remove(c);
    			playerHitboxes.remove(f);
    			
    		}
    	}
		inputText.setText(inputs.keyCombo.toString());
    	
    		// TODO: enemy hitboxes
    	
//        if (playerImage.getY() + myPlayerHeight / 2> groundLevel) {
//        	playerImage.setY(groundLevel - myPlayerHeight / 2);
//        }
        if (!myEnemy.getInAir()) {
        	myEnemy.executeAction("U");
        }
        if (!myPlayer.getInAir() && !myPlayer.getAttacking()) {

        	LinkedList<String> inputCodes = inputs.keyCombo;
        	String instruction = moveTree.parseInput(inputCodes);
        	if (instruction != null) {
        		inputCodes.clear();
        		myPlayer.executeAction(instruction);
        	}
        	//        if (inputs.aPressed) {
        	//        	myPlayer.executeAction("QCFL");
        	//        }


        	else if (inputs.upPressed && inputs.rightPressed) {
        		myPlayer.executeAction("UF");
        	} else if (inputs.upPressed && inputs.leftPressed) {
        		myPlayer.executeAction("UB");
        	} else if (inputs.upPressed) {
        		// System.out.println(myTopBlock.getY());
        		myPlayer.executeAction("U");
        	} else if (inputs.downPressed || (inputs.downPressed && inputs.backwardPressed)) {
        		myPlayer.executeAction("D");
        	} else if (inputs.rightPressed) {
        		if (playerHurtbox.getBoundsInParent().getMaxX() < width) {
        			playerHurtbox.setX(playerHurtbox.getX()+ 2);
        		}
        		if (inputs.forwardPressed) {
            		myPlayer.executeAction("F");
        		} else {
        			myPlayer.executeAction("B");
        		}
        	} else if (inputs.leftPressed) {
        		if (playerHurtbox.getBoundsInParent().getMinX() > 0) {
        			playerHurtbox.setX(playerHurtbox.getX()- 2);
        		}
        		if (inputs.backwardPressed) {
            		myPlayer.executeAction("B");
        		} else {
        			myPlayer.executeAction("F");
        		}
        	}
        }

        if (frameCount == 30) {
            myGame.myInputManager.discardInput();
        }
        frameCount++;
        if (frameCount > 30) { 
        	frameCount = 0;
        }
	}

}
