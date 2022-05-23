package shape;

import java.awt.*;
//不同的形状 为一个新的对象,记录了四个旋转后的状态保存在boolean数组中,旋转时只要选择对应的数据即可
public class ShapeType {


	private Color baseColor;
	

	private int spawnCol;

	private int spawnRow;

	private int dimension;
	

	private int rows;

	private int cols;
	

	private boolean[][] tiles;
	

	//构造函数
	public ShapeType(){}
	public ShapeType(Color color, int dimension, int cols, int rows, boolean[][] tiles) {
		this.baseColor = color;
		this.dimension = dimension;
		this.tiles = tiles;
		this.cols = cols;
		this.rows = rows;
		
		this.spawnCol = 5 - (dimension >> 1);
		this.spawnRow = getTopInset(0);
	}

	public void setColor(Color baseColor) {
		this.baseColor = baseColor;
	}

	public Color getBaseColor() {
		return baseColor;
	}
	

	public int getDimension() {
		return dimension;
	}

	public boolean[][] getTiles() {
		return tiles;
	}

	public int getSpawnColumn() {
		return spawnCol;
	}
	

	public int getSpawnRow() {
		return spawnRow;
	}
	

	public int getRows() {
		return rows;
	}
	

	public int getCols() {
		return cols;
	}
	

	public boolean isTile(int x, int y, int rotation) {
		return tiles[rotation][y * dimension + x];
	}
	

	public int getLeftInset(int rotation) {

		for(int x = 0; x < dimension; x++) {
			for(int y = 0; y < dimension; y++) {
				if(isTile(x, y, rotation)) {
					return x;
				}
			}
		}
		return -1;
	}
	

	public int getRightInset(int rotation) {

		for(int x = dimension - 1; x >= 0; x--) {
			for(int y = 0; y < dimension; y++) {
				if(isTile(x, y, rotation)) {
					return dimension - x;
				}
			}
		}
		return -1;
	}
	

	public int getTopInset(int rotation) {

		for(int y = 0; y < dimension; y++) {
			for(int x = 0; x < dimension; x++) {
				if(isTile(x, y, rotation)) {
					return y;
				}
			}
		}
		return -1;
	}
	

	public int getBottomInset(int rotation) {

		for(int y = dimension - 1; y >= 0; y--) {
			for(int x = 0; x < dimension; x++) {
				if(isTile(x, y, rotation)) {
					return dimension - y;
				}
			}
		}
		return -1;
	}
	
}
