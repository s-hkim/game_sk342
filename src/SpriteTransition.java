import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteTransition extends Transition {

    private int centerOffset;
    private final ImageView imageView;
    private final int count;
    private final int offsetX;
    private final int offsetY;
    private final int[] widths;
    private final int[] heights;
    private int[] widthOffsets;
    private final boolean left;
    public boolean isLeft() {
		return left;
	}

	private final int direction;

    private int lastIndex;

    public SpriteTransition(ImageView imageView, Duration duration, int centerOffset, int offsetX, int offsetY, 
    		int[] widths, int[] heights, boolean left) {
        this.imageView = imageView;
        this.count = widths.length;
        this.centerOffset = centerOffset;
        if (left) {
        	this.offsetX = offsetX + this.centerOffset;
        } else {
        	this.offsetX = this.centerOffset - offsetX;
        }
        widthOffsets = new int[widths.length];
        int s = 0;
        for (int i = 0; i < widths.length; i++) {
        	s = s + widths[i];
        	widthOffsets[i] = s;
        }
        this.offsetY = offsetY;
        this.widths = widths;
        this.heights = heights;
        this.left = left;
        if (left) {
        	direction = 1;
        } else {
        	direction = -1;
        }
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    protected void interpolate(double k) {
        final int index = Math.min((int) Math.floor(k * count), count - 1);
        if (index != lastIndex) {
            int x = direction* widthOffsets[index%count]  + offsetX;
            //int y = (index / count) * heights[lastIndex] + offsetY;
            if (direction > 0) {
            	x = x - widths[index];
            }
            imageView.setViewport(new Rectangle2D(x, offsetY, widths[index], heights[index]));
            lastIndex = index;
        }
    }
}