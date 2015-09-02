import java.util.LinkedList;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputListener {
	private boolean enterPressed;
	
	private boolean playerForwardPressed;
	private boolean playerBackwardPressed;
	private boolean wPressed;
	private boolean aPressed;
	private boolean sPressed;
	private boolean dPressed;
	private boolean fPressed;
	private boolean gPressed;
	private boolean hPressed;
	
	private boolean enemyForwardPressed;
	private boolean enemyBackwardPressed;
	private boolean upPressed;
	private boolean downPressed;
	private boolean rightPressed;
	private boolean leftPressed;
	private boolean iPressed;
	private boolean oPressed;
	private boolean pPressed;
	
	private LinkedList<String> playerKeyCombo;
	private LinkedList<String> enemyKeyCombo;
	private boolean playerLeft = true;
	private boolean enemyLeft = false;
	
	public boolean isEnterPressed() {
		return enterPressed;
	}

	public void setEnterPressed(boolean enterPressed) {
		this.enterPressed = enterPressed;
	}

	public boolean isUpPressed() {
		return upPressed;
	}

	public void setUpPressed(boolean upPressed) {
		this.upPressed = upPressed;
	}

	public boolean isDownPressed() {
		return downPressed;
	}

	public void setDownPressed(boolean downPressed) {
		this.downPressed = downPressed;
	}

	public boolean isRightPressed() {
		return rightPressed;
	}

	public void setRightPressed(boolean rightPressed) {
		this.rightPressed = rightPressed;
	}

	public boolean isLeftPressed() {
		return leftPressed;
	}

	public void setLeftPressed(boolean leftPressed) {
		this.leftPressed = leftPressed;
	}

	public boolean isForwardPressed() {
		return playerForwardPressed;
	}

	public void setForwardPressed(boolean forwardPressed) {
		this.playerForwardPressed = forwardPressed;
	}

	public boolean isBackwardPressed() {
		return playerBackwardPressed;
	}

	public void setBackwardPressed(boolean backwardPressed) {
		this.playerBackwardPressed = backwardPressed;
	}

	public boolean isaPressed() {
		return aPressed;
	}

	public void setaPressed(boolean aPressed) {
		this.aPressed = aPressed;
	}

	public boolean issPressed() {
		return sPressed;
	}

	public void setsPressed(boolean sPressed) {
		this.sPressed = sPressed;
	}

	public boolean isdPressed() {
		return dPressed;
	}

	public void setdPressed(boolean dPressed) {
		this.dPressed = dPressed;
	}
	public boolean isPlayerForwardPressed() {
		return playerForwardPressed;
	}

	public void setPlayerForwardPressed(boolean playerForwardPressed) {
		this.playerForwardPressed = playerForwardPressed;
	}

	public boolean isPlayerBackwardPressed() {
		return playerBackwardPressed;
	}

	public void setPlayerBackwardPressed(boolean playerBackwardPressed) {
		this.playerBackwardPressed = playerBackwardPressed;
	}

	public boolean iswPressed() {
		return wPressed;
	}

	public void setwPressed(boolean wPressed) {
		this.wPressed = wPressed;
	}

	public boolean isfPressed() {
		return fPressed;
	}

	public void setfPressed(boolean fPressed) {
		this.fPressed = fPressed;
	}

	public boolean isgPressed() {
		return gPressed;
	}

	public void setgPressed(boolean gPressed) {
		this.gPressed = gPressed;
	}

	public boolean ishPressed() {
		return hPressed;
	}

	public void sethPressed(boolean hPressed) {
		this.hPressed = hPressed;
	}

	public boolean isEnemyForwardPressed() {
		return enemyForwardPressed;
	}

	public void setEnemyForwardPressed(boolean enemyForwardPressed) {
		this.enemyForwardPressed = enemyForwardPressed;
	}

	public boolean isEnemyBackwardPressed() {
		return enemyBackwardPressed;
	}

	public void setEnemyBackwardPressed(boolean enemyBackwardPressed) {
		this.enemyBackwardPressed = enemyBackwardPressed;
	}

	public boolean isiPressed() {
		return iPressed;
	}

	public void setiPressed(boolean iPressed) {
		this.iPressed = iPressed;
	}

	public boolean isoPressed() {
		return oPressed;
	}

	public void setoPressed(boolean oPressed) {
		this.oPressed = oPressed;
	}

	public boolean ispPressed() {
		return pPressed;
	}

	public void setpPressed(boolean pPressed) {
		this.pPressed = pPressed;
	}

	public LinkedList<String> getPlayerKeyCombo() {
		return playerKeyCombo;
	}

	public void setPlayerKeyCombo(LinkedList<String> playerKeyCombo) {
		this.playerKeyCombo = playerKeyCombo;
	}

	public LinkedList<String> getEnemyKeyCombo() {
		return enemyKeyCombo;
	}

	public void setEnemyKeyCombo(LinkedList<String> enemyKeyCombo) {
		this.enemyKeyCombo = enemyKeyCombo;
	}

	public boolean isLeft() {
		return playerLeft;
	}

	public void setLeft(boolean left) {
		this.playerLeft = left;
	}

	public InputListener () {
		enterPressed = false;
		
		enemyForwardPressed = false;
		enemyBackwardPressed = false;
		upPressed = false;
		downPressed = false;
		rightPressed = false;
		leftPressed = false;
		iPressed = false;
		oPressed = false;
		pPressed = false;
		enemyKeyCombo = new LinkedList<String>();
		
		playerForwardPressed = false;
		playerBackwardPressed = false;
		wPressed = false;
		aPressed = false;
		sPressed = false;
		dPressed = false;
		fPressed = false;
		gPressed = false;
		hPressed = false;
		playerKeyCombo = new LinkedList<String>();
	}
	
	public void keyPressed(KeyEvent ke) {
		KeyCode code = ke.getCode();
		if (code == KeyCode.ENTER) {
			enterPressed = true;
		}
		if (code == KeyCode.UP) {
			upPressed = true;
			enemyKeyCombo.addLast("UP");
		} if (code == KeyCode.DOWN) {
			downPressed = true;
			enemyKeyCombo.addLast("DOWN");
		} if (code == KeyCode.RIGHT) {
			rightPressed = true;
			if (enemyLeft) {
				enemyForwardPressed = true;
				enemyKeyCombo.addLast("FORWARD");
			} else {
				enemyBackwardPressed = true;
				enemyKeyCombo.addLast("BACKWARD");
			}
		} if (code == KeyCode.LEFT) {
			leftPressed = true;
			if (enemyLeft) {
				enemyBackwardPressed = true;
				enemyKeyCombo.addLast("BACKWARD");
			} else {
				enemyForwardPressed = true;
				enemyKeyCombo.addLast("FORWARD");
			}
		} if (code == KeyCode.I) {
			iPressed = true;
			enemyKeyCombo.addLast("LIGHT");
		} if (code == KeyCode.O) {
			oPressed = true;
			enemyKeyCombo.addLast("MEDIUM");
		} if (code == KeyCode.P) {
			pPressed = true;
			enemyKeyCombo.addLast("HARD");
		}

		
		if (code == KeyCode.W) {
			wPressed = true;
			playerKeyCombo.addLast("UP");
		} if (code == KeyCode.A) {
			aPressed = true;
			if (playerLeft) {
				playerBackwardPressed = true;
				playerKeyCombo.addLast("BACKWARD");
			} else {
				playerForwardPressed = true;
				playerKeyCombo.addLast("FORWARD");
			}
		} if (code == KeyCode.S) {
			sPressed = true;
			playerKeyCombo.addLast("DOWN");
		} if (code == KeyCode.D) {
			dPressed = true;
			if (playerLeft) {
				playerForwardPressed = true;
				playerKeyCombo.addLast("FORWARD");
			} else {
				playerBackwardPressed = true;
				playerKeyCombo.addLast("BACKWARD");
			}
		} if (code == KeyCode.F) {
			fPressed = true;
			playerKeyCombo.addLast("LIGHT");
		} if (code == KeyCode.G) {
			gPressed = true;
			playerKeyCombo.addLast("MEDIUM");
		} if (code == KeyCode.H) {
			hPressed = true;
			playerKeyCombo.addLast("HARD");
		}
		if (playerKeyCombo.size() > 10) {
			discardInput(playerKeyCombo);
		}
		if (enemyKeyCombo.size() > 10) {
			discardInput(enemyKeyCombo);
		}
	}
	// TODO: update
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
			if (enemyLeft) {
				enemyForwardPressed = false;
			} else {
				enemyBackwardPressed = false;
			}
		} if (code == KeyCode.LEFT) {
			leftPressed = false;
			if (enemyLeft) {
				enemyBackwardPressed = false;
			} else {
				enemyForwardPressed = false;
			}
		} if (code == KeyCode.I) {
			iPressed = false;
		} if (code == KeyCode.O) {
			oPressed = false;
		} if (code == KeyCode.P) {
			pPressed = false;
		}

		
		if (code == KeyCode.W) {
			wPressed = false;
		} if (code == KeyCode.A) {
			aPressed = false;
			if (playerLeft) {
				playerBackwardPressed = false;
			} else {
				playerForwardPressed = false;
			}
		} if (code == KeyCode.S) {
			sPressed = false;
		} if (code == KeyCode.D) {
			dPressed = false;
			if (playerLeft) {
				playerForwardPressed = false;
			} else {
				playerBackwardPressed = false;
			}
		} if (code == KeyCode.F) {
			fPressed = false;
			playerKeyCombo.addLast("LIGHT");
		} if (code == KeyCode.G) {
			gPressed = false;
		} if (code == KeyCode.H) {
			hPressed = false;
		}
	}
	public boolean isPlayerLeft() {
		return playerLeft;
	}
	public void setPlayerLeft (boolean l) {
		playerLeft = l;
	}
	public boolean isEnemyLeft() {
		return enemyLeft;
	}
	public void setEnemyLeft(boolean enemyLeft) {
		this.enemyLeft = enemyLeft;
	}

	public void discardInput(LinkedList<String> keyCombo) {
		keyCombo.pollFirst();
	}
	public void discardInputsFromBoth() {
		playerKeyCombo.pollFirst();
		enemyKeyCombo.pollFirst();
	}
	public boolean checkKeyCombos() {
		return !playerKeyCombo.isEmpty() || !enemyKeyCombo.isEmpty();
	}

}
