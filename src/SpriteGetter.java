import javafx.geometry.Rectangle2D;

public class SpriteGetter {
	private SpriteObject idleSprite;
	private SpriteObject walkingForwardSprite;
	private SpriteObject jumpingSprite;
	private SpriteObject hitstunSprite;
	private Rectangle2D walkingBackwardDimensionsLeft;
	private Rectangle2D walkingBackwardDimensionsRight;
	private Rectangle2D crouchingDimensionsLeft;
	private Rectangle2D crouchingDimensionsRight;
	public SpriteGetter () {
		idleSprite = new SpriteObject(3, 0, new int[]{48,48,48}, new int[]{85,85,85});
		walkingForwardSprite = new SpriteObject(5, 1070, 
				new int[]{48,48,48,48}, new int[] {85,85,85,85});
		jumpingSprite = new SpriteObject(5, 902, 
				new int[]{45,45,45,45}, new int[] {100,100,100,100});
		hitstunSprite = new SpriteObject(2, 1966, new int[]{0,47,50,52,48}, new int[]{78,78,78,78,78});
		walkingBackwardDimensionsLeft = new Rectangle2D(279, 423, 48, 80);
		walkingBackwardDimensionsRight = new Rectangle2D(221, 423, 48, 80);
		crouchingDimensionsLeft = new Rectangle2D(330, 423, 48, 80);
		crouchingDimensionsRight = new Rectangle2D(170, 423, 48, 80);
	}
	public SpriteObject getSprite(int state) {
		if (state == Character.IDLING) {
			return idleSprite;
		} else if (state == Character.WALKING_FORWARD) {
			return walkingForwardSprite;
		} else if (state == Character.JUMPING) {
			return jumpingSprite;
		} else if (state == Character.IN_HITSTUN) {
			return hitstunSprite;
		}
		return null;
	}
	public Rectangle2D getViewportRectangle(int state, boolean isLeft) {
		if (state == Character.WALKING_BACKWARD) {
			if (isLeft) {
				return walkingBackwardDimensionsLeft;
			} else {
				return walkingBackwardDimensionsRight;
			}
		} else if (state == Character.CROUCHING) {
			if (isLeft) {
				return crouchingDimensionsLeft;
			} else {
				return crouchingDimensionsRight;
			}
		}
		return null;
	}
}
