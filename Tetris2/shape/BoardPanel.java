package shape;


import view.ImagePanel;
import view.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import static view.MainFrame.*;

//游戏界面,绘制线条,边框,不同的形状
public class BoardPanel extends JPanel {

	//界面的内边距
	private static final int BORDER_WIDTH = 5;
	
	//水平方向格子的数量
	public static final int COL_COUNT = 10;

	//垂直方向格子数量
	private static final int VISIBLE_ROW_COUNT = 20;
	

	private static final int HIDDEN_ROW_COUNT = 2;
	

	public static final int ROW_COUNT = VISIBLE_ROW_COUNT + HIDDEN_ROW_COUNT;
	

	public static final int TILE_SIZE = 24;
	

	public static final int SHADE_WIDTH = 4;
	

	private static final int CENTER_X = COL_COUNT * TILE_SIZE / 2;
	

	private static final int CENTER_Y = VISIBLE_ROW_COUNT * TILE_SIZE / 2;
		

	public static final int PANEL_WIDTH = COL_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
	

	public static final int PANEL_HEIGHT = VISIBLE_ROW_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
	private String fileAddress;

	public static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 23);


	public static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);
//暂停时出现的标签、按钮
	public JLabel f3 = new JLabel("PAUSED");;
	JButton PauseButton1 = new JButton("Save");

	JButton PauseButton2 = new JButton("Home");
//创建数组读取文件信息
	public ArrayList<Color> colors = new ArrayList<>();
	public ArrayList<Integer>rows = new ArrayList<>();
	public ArrayList<Integer>columns = new ArrayList<>();
	public ArrayList<String>load = new ArrayList<>();

	private MainFrame tetris;


	
	//二维数组保存游戏界面,null表示该格子为空
	private ShapeType[][] shapes;
		
	//构造函数初始化
	public BoardPanel(MainFrame tetris,String a) {
		this.tetris = tetris;
		this.shapes = new ShapeType[ROW_COUNT][COL_COUNT];
		this.fileAddress = a;
		
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(Color.darkGray);
	}
	
	//清空画面
	public void clear() {

		for(int i = 0; i < ROW_COUNT; i++) {
			for(int j = 0; j < COL_COUNT; j++) {
				shapes[i][j] = null;
			}
		}
	}
	
	//这个位置是否是合法的并且是空的
	public boolean isValidAndEmpty(ShapeType type, int x, int y, int rotation) {
				
		//判断X是否合法
		if(x < -type.getLeftInset(rotation) || x + type.getDimension() - type.getRightInset(rotation) >= COL_COUNT) {
			return false;
		}
		

		if(y < -type.getTopInset(rotation) || y + type.getDimension() - type.getBottomInset(rotation) >= ROW_COUNT) {
			return false;
		}

		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.isTile(col, row, rotation) && isOccupied(x + col, y + row)) {
					return false;
				}
			}
		}
		return true;
	}
	

	public void addPiece(ShapeType type, int x, int y, int rotation) {
		/*
		 * Loop through every tile within the piece and add it
		 * to the board only if the boolean that represents that
		 * tile is set to true.
		 */
		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.isTile(col, row, rotation)) {
					setShape(col + x, row + y, type);
				}
			}
		}
	}
	

	public int checkLines() {
		int completedLines = 0;
		
		/*
		 * Here we loop through every line and check it to see if
		 * it's been cleared or not. If it has, we increment the
		 * number of completed lines and check the next row.
		 * 
		 * The checkLine function handles clearing the line and
		 * shifting the rest of the board down for us.
		 */
		for(int row = 0; row < ROW_COUNT; row++) {
			if(checkLine(row)) {
				completedLines++;
			}
		}
		return completedLines;
	}
			

	private boolean checkLine(int line) {
		/*
		 * Iterate through every column in this row. If any of them are
		 * empty, then the row is not full.
		 */
		for(int col = 0; col < COL_COUNT; col++) {
			if(!isOccupied(col, line)) {
				return false;
			}
		}
		
		/*
		 * Since the line is filled, we need to 'remove' it from the game.
		 * To do this, we simply shift every row above it down by one.
		 */
		for(int row = line - 1; row >= 0; row--) {
			for(int col = 0; col < COL_COUNT; col++) {
				setShape(col, row + 1, getShape(col, row));
			}
		}
		return true;
	}
	
	

	private boolean isOccupied(int x, int y) {
		return shapes[y][x] != null;
	}

	private void setShape(int  x, int y, ShapeType type) {
		shapes[y][x] = type;
	}
		

	private ShapeType getShape(int x, int y) {
		return shapes[y][x];
	}

	private JButton music = new JButton("Music");


	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		g.translate(BORDER_WIDTH, BORDER_WIDTH);

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(fileAddress));
		} catch (IOException e) {
			e.printStackTrace();
		}


		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
		//暂停状态

		if (tetris.isPaused()) {
//设置字体
			f3.setFont(LARGE_FONT);
			f3.setForeground(Color.white);
//添加按钮监听
			PauseButton1.addActionListener(event -> name());
			PauseButton2.addActionListener(event -> startPanel());
			PauseButton2.addActionListener(event -> tetris.setVisible(false));
//标签与按钮可见
			f3.setVisible(true);
			PauseButton1.setVisible(true);
			PauseButton2.setVisible(true);
//绝对布局
			this.setLayout(null);
			PauseButton1.setBounds(80, 200, 80, 40);
			PauseButton2.setBounds(80, 300, 80, 40);
			f3.setBounds(80, 100, 150, 40);
//将元素添加到面板上
			this.add(PauseButton1);
			this.add(PauseButton2);
			this.add(f3);

			//还未开始游戏
		} else if (tetris.isNewGame()) {
			g.setFont(LARGE_FONT);
			g.setColor(Color.WHITE);

			String msg = "TETRIS";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 150);
			g.setFont(SMALL_FONT);
			msg = "Choose Level";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 280);
			msg = "[1]: Easy  [2]: Middle [3]: Hard";
			g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 300);
//游戏结束
		} else if (tetris.isGameOver()) {
//创建新面板、标签
			JLabel score = new JLabel("Score:" + tetris.score,SwingConstants.CENTER);
			ImagePanel losePanel = new ImagePanel("out/background/gameOver2.png");
			losePanel.setSize(300, 400);
			tetris.add(losePanel);
//设置字体
			score.setFont(LARGE_FONT);
			score.setForeground(Color.BLACK);

//创建按钮
			JButton LoseButton1 = new JButton("Home");

//添加按钮监听
			LoseButton1.addActionListener(event -> startPanel());
			LoseButton1.addActionListener(event -> tetris.setVisible(false));

//绝对布局
			losePanel.setLayout(null);
			score.setBounds(55,130,150,40);
			LoseButton1.setBounds(75, 250, 100, 40);

//将元素添加到新面板上
			losePanel.add(LoseButton1);
			losePanel.add(score);
			losePanel.setVisible(true);
			this.setVisible(false);
//读档游戏
		} else if (tetris.isLoadGame()) {
//设置暂停面板的元素不可见
			f3.setVisible(false);
			PauseButton1.setVisible(false);
			PauseButton2.setVisible(false);
//创建新的ShapeType对象：一个小方格
			boolean[][]tiles2 = new boolean[3][1];
			for(int i=0;i<3;i++){
				for(int j=0;j<1;j++){
					tiles2[i][j]=true;
				}
			}
//绘制读档时已存在的方块，并使它们成为新边界
			for (int i = 0; i <= colors.size() - 1; i++) {
				drawTile(colors.get(i), rows.get(i) * TILE_SIZE, (columns.get(i) - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
				ShapeType t = new ShapeType(colors.get(i),1,1,1,tiles2);
				setShape(rows.get(i),columns.get(i),t);
			}
//绘制已经存在的方块
		for (int x = 0; x < COL_COUNT; x++) {
				for (int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
					ShapeType tile = getShape(x, y);
					if (tile != null) {
						drawTile(tile, x * TILE_SIZE, (y - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
					}
				}
			}
			try {
				ShapeType type = tetris.getPieceType();
				int pieceCol = tetris.getPieceCol();
				int pieceRow = tetris.getPieceRow();
				int rotation = tetris.getPieceRotation();

//绘制当前正在控制的形状
				for (int col = 0; col < type.getDimension(); col++) {
					for (int row = 0; row < type.getDimension(); row++) {
						if (pieceRow + row >= 2 && type.isTile(col, row, rotation)) {
							drawTile(type, (pieceCol + col) * TILE_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
						}
					}
				}

				Color base = type.getBaseColor();
				base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
				for (int lowest = pieceRow; lowest < ROW_COUNT; lowest++) {
					if (isValidAndEmpty(type, pieceCol, lowest, rotation)) {
						continue;
					}

					lowest--;


					break;
				}
			}catch(NullPointerException e){}
//绘制网格
			g.setColor(Color.BLACK);
			for (int x = 0; x < COL_COUNT; x++) {
				for (int y = 0; y < VISIBLE_ROW_COUNT; y++) {
					g.drawLine(0, y * TILE_SIZE, COL_COUNT * TILE_SIZE, y * TILE_SIZE);
					g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, VISIBLE_ROW_COUNT * TILE_SIZE);
				}
			}

		}
//游戏中
		else {
			f3.setVisible(false);
			PauseButton1.setVisible(false);
			PauseButton2.setVisible(false);
			music.setVisible(false);
//已经存在的方块们
//绘制每个格子上对应的内容,没有就不处理
			for (int x = 0; x < COL_COUNT; x++) {
				for (int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
					ShapeType tile = getShape(x, y);
					if (tile != null) {
						drawTile(tile, x * TILE_SIZE, (y - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
					}
				}
			}

			try {
				ShapeType type = tetris.getPieceType();
				int pieceCol = tetris.getPieceCol();
				int pieceRow = tetris.getPieceRow();
				int rotation = tetris.getPieceRotation();
//绘制当前正在控制的形状
				for (int col = 0; col < type.getDimension(); col++) {
					for (int row = 0; row < type.getDimension(); row++) {
						if (pieceRow + row >= 2 && type.isTile(col, row, rotation)) {
							drawTile(type, (pieceCol + col) * TILE_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
						}
					}
				}

				Color base = type.getBaseColor();
				base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
				for (int lowest = pieceRow; lowest < ROW_COUNT; lowest++) {
					if (isValidAndEmpty(type, pieceCol, lowest, rotation)) {
						continue;
					}

					lowest--;


					break;
				}
			}catch (NullPointerException e){}
//绘制网格
			g.setColor(Color.BLACK);
			for (int x = 0; x < COL_COUNT; x++) {
				for (int y = 0; y < VISIBLE_ROW_COUNT; y++) {
					g.drawLine(0, y * TILE_SIZE, COL_COUNT * TILE_SIZE, y * TILE_SIZE);
					g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, VISIBLE_ROW_COUNT * TILE_SIZE);
				}
			}
		}

		g.setColor(Color.WHITE);
		g.drawRect(0, 0, TILE_SIZE * COL_COUNT, TILE_SIZE * VISIBLE_ROW_COUNT);
	}


	private void drawTile(ShapeType type, int x, int y, Graphics g) {
		drawTile(type.getBaseColor(), x, y, g);
	}
	

	private void drawTile(Color base,  int x, int y, Graphics g) {
		

		g.setColor(base);
		g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

	}
//文件命名面板
	public void name(){
//创建新面板、标签、按钮与输入区
		ImagePanel namePanel = new ImagePanel("out/background/name.png");
		JLabel text = new JLabel("Name the File");
		text.setFont(LARGE_FONT);
		JTextField nameField = new JTextField();
		JButton NameButton = new JButton("Save");
//添加按钮监听
		NameButton.addActionListener(event -> {
			try {
				save(nameField.getText());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		NameButton.addActionListener(event ->tetris.setVisible(false));
		NameButton.addActionListener(event ->startPanel());
//绝对布局
		namePanel.setLayout(null);
		text.setBounds(50,30,250,40);
		nameField.setBounds(75,80,100,30);
		NameButton.setBounds(80,130,80,40);
		namePanel.add(nameField);
		namePanel.add(NameButton);
		namePanel.add(text);
		tetris.add(namePanel);
//游戏面板不可见，文件命名面板可见
		namePanel.setVisible(true);
		this.setVisible(false);

	}



//保存游戏
	public void save(String Filename) throws IOException{
//创建新的文件
		String WholeFilename = Filename+".txt";
		String FileLocation ="out/saving/"+WholeFilename;
		File file = new File(FileLocation);

		try {
// 若文档存在，询问是否覆盖
				if(file.exists()){
				int n = JOptionPane.showConfirmDialog(this, "是否覆盖?", "取消", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					file.delete();
				}
			}

// 在文件中输入信息
			FileWriter fileWriter = new FileWriter(FileLocation,true);
//记录游戏难度与分数
			fileWriter.write(tetris.gameSpeed+"\n"+tetris.score+"\n");
//遍历游戏方格，非空则记录该方格的颜色和坐标
			for(int x = 0; x < COL_COUNT; x++) {
				for(int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
					ShapeType tile = getShape(x, y);
					if(tile != null) {
						fileWriter.write(translate(tile.getBaseColor())+" "+x+" "+ y);
						fileWriter.write("\n");
					}
				}
			}  fileWriter.close();
			System.out.println("Save Done");
		} catch (IOException e){
			e.printStackTrace();
		}
	}

//将color类变为字符串
	public static String translate(Color color){
		String R = Integer.toHexString(color.getRed());
		R = R.length()<2?('0'+R):R;
		String B = Integer.toHexString(color.getBlue());
		B = B.length()<2?('0'+B):B;
		String G = Integer.toHexString(color.getGreen());
		G = G.length()<2?('0'+G):G;
		return '#'+R+B+G;
	}




}
