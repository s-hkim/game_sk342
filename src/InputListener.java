import java.util.LinkedList;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputListener {
	public boolean enterPressed;
	public boolean upPressed;
	public boolean downPressed;
	public boolean rightPressed;
	public boolean leftPressed;
	public boolean forwardPressed;
	public boolean backwardPressed;
	public boolean aPressed;
	public boolean sPressed;
	public boolean dPressed;
	public LinkedList<String> keyCombo;
	public boolean left = true;
	
	public InputListener () {
		upPressed = false;
		downPressed = false;
		rightPressed = false;
		leftPressed = false;
		forwardPressed = false;
		backwardPressed = false;
		aPressed = false;
		sPressed = false;
		dPressed = false;
		keyCombo = new LinkedList<String>();
	}
	
	public void keyPressed(KeyEvent ke) {
		KeyCode code = ke.getCode();
		if (code == KeyCode.ENTER) {
			enterPressed = true;
		}
		if (code == KeyCode.UP) {
			upPressed = true;
			keyCombo.addLast(code.toString());
		} if (code == KeyCode.DOWN) {
			downPressed = true;
			keyCombo.addLast(code.toString());
		} if (code == KeyCode.RIGHT) {
			rightPressed = true;
			if (left) {
				forwardPressed = true;
				keyCombo.addLast("FORWARD");
			} else {
				backwardPressed = true;
				keyCombo.addLast("BACKWARD");
			}
		} if (code == KeyCode.LEFT) {
			leftPressed = true;
			if (left) {
				backwardPressed = true;
				keyCombo.addLast("BACKWARD");
			} else {
				forwardPressed = true;
				keyCombo.addLast("FORWARD");
			}
		} if (code == KeyCode.A) {
			aPressed = true;
			keyCombo.addLast(code.toString());
		} if (code == KeyCode.S) {
			sPressed = true;
			keyCombo.addLast(code.toString());
		} if (code == KeyCode.D) {
			dPressed = true;
			keyCombo.addLast(code.toString());
		} 
		if (keyCombo.size() > 10) {
			discardInput();
		}
	}

	public void keyReleased(KeyEvent ke) {
		KeyCode code = ke.getCode();
		if (code == KeyCode.ENTER) {
			enterPressed = false;
		}
		if (code == KeyCode.UP) {
			upPressed = false;
		} if (code == KeyCode.DOWN) {
			downPressed = false;
		} if (code == KeyCode.RIGHT) {
			rightPressed = false;
			if (left) {
				forwardPressed = false;
			} else {
				backwardPressed = false;
			}
		} if (code == KeyCode.LEFT) {
			leftPressed = false;
			if (left) {
				backwardPressed = false;
			} else {
				forwardPressed = false;
			}
		} if (code == KeyCode.A) {
			aPressed = false;
		} if (code == KeyCode.S) {
			sPressed = false;
		} if (code == KeyCode.D) {
			dPressed = false;
		}
	}
	public void setOrientation (boolean l) {
		left = l;
	}
	public void discardInput() {
		keyCombo.pollFirst();
	}

}
