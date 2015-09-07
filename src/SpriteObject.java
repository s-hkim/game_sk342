public class SpriteObject {
		private int xOffset;
		private int yOffset;
		private int[] widths;
		private int[] heights;
		
		public SpriteObject(int xOffset, int yOffset, int[] widths, int[] heights) {
			this.xOffset = xOffset;
			this.yOffset = yOffset;
			this.widths = widths;
			this.heights = heights;
		}
		public int getxOffset() {
			return xOffset;
		}
		public int getyOffset() {
			return yOffset;
		}
		public int[] getWidths() {
			return widths;
		}
		public int[] getHeights() {
			return heights;
		}
	}