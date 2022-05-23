package shape;

import view.ImagePanel;
import view.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//显示右侧的内容包括下一个形状,当前分数,提示信息
public class SidePanel extends JPanel {

	private static final int TILE_SIZE = BoardPanel.TILE_SIZE >> 1;
	

	private static final int SHADE_WIDTH = BoardPanel.SHADE_WIDTH >> 1;

	private static final int TILE_COUNT = 5;
	

	private static final int SQUARE_CENTER_X = 130;

	private static final int SQUARE_CENTER_Y = 65;
	

	private static final int SQUARE_SIZE = (TILE_SIZE * TILE_COUNT >> 1);
	

	private static final int SMALL_INSET = 20;
	

	private static final int LARGE_INSET = 40;

	private static final int STATS_INSET = 140;
	

	private static final int CONTROLS_INSET = 220;
	

	private static final int TEXT_STRIDE = 20;

	private String fileAddress;

	private static final Font SMALL_FONT = new Font("Tahoma ", Font.BOLD, 11);
	

	private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 13);

	private static final Color DRAW_COLOR = new Color(236, 108, 9);
	

	private view.MainFrame tetris;

	public SidePanel(MainFrame tetris,String a) {
		super();
		this.tetris = tetris;
		this.fileAddress = a;

		setPreferredSize(new Dimension(200, BoardPanel.PANEL_HEIGHT));
		setBackground(Color.darkGray);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(fileAddress));
		} catch (IOException e) {
			e.printStackTrace();
		}


		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);

		g.setColor(Color.BLACK);

		int offset;
		

		//分数
		g.setFont(LARGE_FONT);
		g.drawString("", SMALL_INSET, offset = STATS_INSET);
		g.setFont(SMALL_FONT);
//		g.drawString("Level: " + tetris.getLevel(), LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("Score: " + tetris.getScore(), LARGE_INSET, offset += TEXT_STRIDE);
		
		//绘制提示信息
		g.setFont(LARGE_FONT);
		g.drawString("Tips", SMALL_INSET, offset = CONTROLS_INSET);
		g.setFont(SMALL_FONT);
		g.drawString("A - Move Left", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("D - Move Right", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("Q - Rotate Anticlockwise", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("E - Rotate Clockwise", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("S - Drop", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("P - Pause Game", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("- - Reduce the speed", LARGE_INSET, offset += TEXT_STRIDE);
		g.drawString("+ - Increase the speed", LARGE_INSET, offset += TEXT_STRIDE);
		

		g.setFont(LARGE_FONT);
		g.drawString("Next Piece:", SMALL_INSET, 70);
		g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 2);
		

		ShapeType type = tetris.getNextPieceType();
		//绘制下一个形状
		if(!tetris.isGameOver() && type != null) {

			int cols = type.getCols();
			int rows = type.getRows();
			int dimension = type.getDimension();
		

			int startX = (SQUARE_CENTER_X - (cols * TILE_SIZE / 2));
			int startY = (SQUARE_CENTER_Y - (rows * TILE_SIZE / 2));
		

			int top = type.getTopInset(0);
			int left = type.getLeftInset(0);
		

			for(int row = 0; row < dimension; row++) {
				for(int col = 0; col < dimension; col++) {
					if(type.isTile(col, row, 0)) {
						drawTile(type, startX + ((col - left) * TILE_SIZE), startY + ((row - top) * TILE_SIZE), g);
					}
				}
			}
		}
	}
	//绘制一个小方块
	private void drawTile(ShapeType type, int x, int y, Graphics g) {

		g.setColor(type.getBaseColor());
		g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
		

	}
}
