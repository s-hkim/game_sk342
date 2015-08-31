import java.util.ArrayList;
import java.util.LinkedList;

public class InputTree {
	public InputTreeNode myRoot;
	public InputTree () {
		myRoot = new InputTreeNode(null, null);
		
	}
	public static class InputTreeNode {
		public String myCode;
		public String myMove;
	    public ArrayList<InputTreeNode> myChildren;

	    public InputTreeNode(String code, String move) {
	        myCode = code;
	        myMove = move;
	        myChildren = new ArrayList<InputTreeNode>();
	    }
	}
	public void addMove (String[] codes, String move) {
		InputTreeNode currentNode = myRoot;
		for (int i = 0; i < codes.length; i++) {
			InputTreeNode nextNode = null;
			for (InputTreeNode node: currentNode.myChildren) {
				if (node.myCode.equals(codes[i])) {
					nextNode = node;
					break;
				}
			}
			if (nextNode == null) {
				nextNode = new InputTreeNode(codes[i], null);
				currentNode.myChildren.add(nextNode);
			}
			if (i == codes.length - 1) {
				nextNode.myMove = move;
			}
			currentNode = nextNode;
		}
	}
	public String parseInput (LinkedList<String> keyCombo){
		LinkedList<String> copy = new LinkedList<String>(keyCombo);
		InputTreeNode currentNode = myRoot;
		while (currentNode != null) {
			InputTreeNode nextNode = null;
			String currentKey = copy.pollLast();
			for (InputTreeNode node: currentNode.myChildren) {
				if (node.myCode.equals(currentKey)) {
					nextNode = node;
					break;
				}
			}
			if (nextNode == null) {
				return currentNode.myMove;
			}
			currentNode = nextNode;
		}
		return null;
	}
	public void printTree (InputTreeNode node) {
		if (node == null) {
			return;
		}
		String a;
		String b;
		if (node.myCode == null) {
			a = "null";
		} else {
			a = node.myCode;
		}
		if (node.myMove == null) {
			b = "null";
		} else {
			b = node.myMove;
		}
		System.out.println(a + " " + b);
		for (InputTreeNode n: node.myChildren) {
			printTree(n);
		}
	}
}
