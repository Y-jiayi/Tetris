package view;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import jdk.jfr.events.FileReadEvent;
import shape.*;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import static shape.BoardPanel.*;


public class MainFrame extends JFrame {
	public static MainFrame mainFrame=new MainFrame() ;

	public static MainFrame tetris;

	private static ArrayList<JPanel> panels = new ArrayList<>();

	private static Color panelPen = Color.GRAY;
	private static Color shapePen;
	private static final Tuple frameSize = new Tuple(800, 800);
	private static final Tuple blockSize = new Tuple(40, 40);
	private static final long FRAME_TIME = 1000L / 50L;
	
	public static final ShapeType[] values ={new S_shape1(),new T_shape2(),new Z_shape3(),
			new Line_shape4(),new L_shape5(),new MirroredL_shape6(),new Square_shape7()};
	public static final int TYPE_COUNT = values.length;
		

	BoardPanel board;
	

	private SidePanel side;
	

	private boolean isPaused;
	

	private boolean isNewGame;

	private boolean isLoadGame;
	

	private boolean isGameOver;
	

//	private int level;
	

	public int score;
	

	private Random random;
	

	private Clock logicTimer;
				

	private ShapeType currentType;
	

	private ShapeType nextType;
		

	private int currentCol;

	private int currentRow;

	private int currentRotation;
		

	private int dropCooldown;
	

	public double gameSpeed;
	private double oneStep=1;
	private double gameSpeedMin=1.0;
	private double gameSpeedMax=9.0;

//线程一与线程二
	public static Thread1 thread1 = new Thread1();//新游戏
	public static Thread2 thread2 = new Thread2();//读档

//主函数，播放音乐并调用开始面板
	public static void main(String[] args) {
		String filepath = "out/background/背景音乐.wav";
           Music musicObject = new Music();
            musicObject.playMusic(filepath);
		startPanel();
	}

//弹出开始面板
	public static void startPanel(){
		mainFrame = new MainFrame();
		mainFrame.initiateFrame("TETRIS", frameSize);
//创建面板
		ImagePanel panel1 = new ImagePanel("out/background/开始2.png");
		panel1.setSize(300, 400);
//添加面板
		mainFrame.addPanel(panel1);
//创建按钮
		JButton BeginButton1 = new JButton("Play");
		JButton BeginButton3 = new JButton("Load");

//监听
		BeginButton1.addActionListener(event -> thread1.start());
		BeginButton3.addActionListener(event -> {
			thread2.start();
		});
		BeginButton3.addActionListener(event ->mainFrame.setVisible(false));

//布局
		panel1.setLayout(null);
		BeginButton1.setBounds(100, 150, 80, 30);
		BeginButton3.setBounds(100, 200, 80, 30);

		panel1.add(BeginButton1);
		panel1.add(BeginButton3);
		mainFrame.setVisible(true);
	}

	//无参构造，用于放置除游戏面板外的面板
	private MainFrame(){}
	//有参构造，用于创建游戏面板
	MainFrame(String a) {
		super("Tetris");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		this.board = new BoardPanel(this,"out/background/game.png");
		this.side = new SidePanel(this,"out/background/sidepanel.png");
		this.add(this.board, BorderLayout.CENTER);
		this.add(this.side, BorderLayout.EAST);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



		//键盘监听事件
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("press: "+e.getKeyCode()+" speed: "+gameSpeed);
				switch(e.getKeyCode()) {
					//+
					case 61:
						if(gameSpeed+oneStep<gameSpeedMax)
							gameSpeed+=oneStep;
						else gameSpeed=gameSpeedMax;
						break;
					//-
						case 45:
							if(gameSpeed-oneStep>gameSpeedMin)
								gameSpeed-=oneStep;
							else gameSpeed=gameSpeedMin;
							break;
				//S
				case KeyEvent.VK_S:
					if(!isPaused && dropCooldown == 0) {
						logicTimer.setCyclesPerSecond(25.0f);
					}
					break;
				//left
				case KeyEvent.VK_A:
					if(!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)) {
						currentCol--;
					}
					break;
				//right
				case KeyEvent.VK_D:
					if(!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)) {
						currentCol++;
					}
					break;
					//逆时针旋转
				case KeyEvent.VK_Q:
					if(!isPaused) {
						rotatePiece((currentRotation == 0) ? 3 : currentRotation - 1);
					}
					break;

					//顺时针旋转
				case KeyEvent.VK_E:
					if(!isPaused) {
						rotatePiece((currentRotation == 3) ? 0 : currentRotation + 1);
					}
					break;
					
				//暂停和恢复
				case KeyEvent.VK_P:
					if(!isGameOver && !isNewGame) {
						isPaused = !isPaused;
						logicTimer.setPaused(isPaused);

					}
					break;

					//简单
					case 49:
						if(isGameOver || isNewGame) {
							gameSpeed=1;

							resetGame();
						}
						break;
					//一般
					case 50:
						if(isGameOver || isNewGame) {
							gameSpeed=5;

							resetGame();
						}
						break;
						//困难
					case 51:
						if(isGameOver || isNewGame) {
							gameSpeed=9;

							resetGame();
						}
						break;

				}


			}



			
			@Override
			public void keyReleased(KeyEvent e) {
				
				switch(e.getKeyCode()) {

				case KeyEvent.VK_S:
					logicTimer.setCyclesPerSecond(gameSpeed);
					logicTimer.reset();
					break;
				}
				
			}
			
		});


		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
//开始新游戏
	void startGame() {
		this.random = new Random();
		this.isNewGame = true;


		this.logicTimer = new Clock(gameSpeed);
		logicTimer.setPaused(true);


		while(true) {
			long start = System.nanoTime();

			logicTimer.update();
			


			if(logicTimer.hasElapsedCycle()) {
				updateGame();
			}
		

			if(dropCooldown > 0) {
				dropCooldown--;
			}
			

			renderGame();

			long delta = (System.nanoTime() - start) / 1000000L;
			if(delta < FRAME_TIME) {
				try {
					Thread.sleep(FRAME_TIME - delta);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

//读档游戏，跳过难度选择界面，直接进入游戏
	void loadGame() {

		this.random = new Random();
		this.isLoadGame = true;
//		this.gameSpeed = 1.0f;

		this.logicTimer = new Clock(gameSpeed);
		logicTimer.setPaused(true);


		for(int i=0;i<values.length;i++)
		{
			values[i].setColor(new Color((int)(Math.random()*255),(int)(Math.random()*255),
					(int)(Math.random()*255)));
		}
		//随机生成下一个形状
		this.nextType = values[random.nextInt(TYPE_COUNT)];
		board.clear();
		logicTimer.reset();
		logicTimer.setCyclesPerSecond(gameSpeed);
		spawnPiece();
		while(true) {
			long start = System.nanoTime();

			logicTimer.update();



			if(logicTimer.hasElapsedCycle()) {
				updateGame();
			}


			if(dropCooldown > 0) {
				dropCooldown--;
			}


			renderGame();

			long delta = (System.nanoTime() - start) / 1000000L;
			if(delta < FRAME_TIME) {
				try {
					Thread.sleep(FRAME_TIME - delta);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
	

	private void updateGame() {

		if(board.isValidAndEmpty(currentType, currentCol, currentRow + 1, currentRotation)) {

			currentRow++;
		} else {
			System.out.println("1");//表示方格到达边界
			board.addPiece(currentType, currentCol, currentRow, currentRotation);
			

			int cleared = board.checkLines();
			if(cleared > 0) {
				score += 50 << cleared;
			}
			

			logicTimer.setCyclesPerSecond(gameSpeed);
			logicTimer.reset();
			

			dropCooldown = 25;
			

			spawnPiece();
		}		
	}
	
//重绘
	private void renderGame() {
		board.repaint();
		side.repaint();
	}

	private void resetGame() {
//		this.level = 1;
		this.score = 0;
//		this.gameSpeed = 1.0f;
		for(int i=0;i<values.length;i++)
		{
			values[i].setColor(new Color((int)(Math.random()*255),(int)(Math.random()*255),
				(int)(Math.random()*255)));
		}
		//随机生成下一个形状
		this.nextType = values[random.nextInt(TYPE_COUNT)];

		this.isNewGame = false;
		this.isGameOver = false;		
		board.clear();
		logicTimer.reset();
		logicTimer.setCyclesPerSecond(gameSpeed);
		spawnPiece();
	}
		
//判定游戏是否结束
	private void spawnPiece() {

		this.currentType = nextType;
		this.currentCol = currentType.getSpawnColumn();
		this.currentRow = currentType.getSpawnRow();
		this.currentRotation = 0;
		this.nextType = values[random.nextInt(TYPE_COUNT)];

		if(!board.isValidAndEmpty(currentType, currentCol, currentRow, currentRotation)) {
			this.isGameOver = true;
			logicTimer.setPaused(true);
		}		
	}

//旋转俄罗斯方块
	private void rotatePiece(int newRotation) {

		int newColumn = currentCol;
		int newRow = currentRow;
		

		int left = currentType.getLeftInset(newRotation);
		int right = currentType.getRightInset(newRotation);
		int top = currentType.getTopInset(newRotation);
		int bottom = currentType.getBottomInset(newRotation);

		if(currentCol < -left) {
			newColumn -= currentCol - left;
		} else if(currentCol + currentType.getDimension() - right >= BoardPanel.COL_COUNT) {
			newColumn -= (currentCol + currentType.getDimension() - right) - BoardPanel.COL_COUNT + 1;
		}
		

		if(currentRow < -top) {
			newRow -= currentRow - top;
		} else if(currentRow + currentType.getDimension() - bottom >= BoardPanel.ROW_COUNT) {
			newRow -= (currentRow + currentType.getDimension() - bottom) - BoardPanel.ROW_COUNT + 1;
		}
		

		if(board.isValidAndEmpty(currentType, newColumn, newRow, newRotation)) {
			currentRotation = newRotation;
			currentRow = newRow;
			currentCol = newColumn;
		}
	}
	

	public boolean isPaused() {
		return isPaused;
	}

	public boolean isGameOver() {
		return isGameOver;
	}
	

	public boolean isNewGame() {
		return isNewGame;
	}

	public boolean isLoadGame(){

		return isLoadGame;
	}
	public int getScore() {
		return score;
	}

	public void setGameSpeed(int a){gameSpeed = a;}



	public ShapeType getPieceType() {
		return currentType;
	}
	

	public ShapeType getNextPieceType() {
		return nextType;
	}
	

	public int getPieceCol() {
		return currentCol;
	}

	public int getPieceRow() {
		return currentRow;
	}
	

	public int getPieceRotation() {
		return currentRotation;
	}




	/**
	 * 关于如何合作
	 * This method performs...
	 *
	 * @param title set title of the frame...
	 * @param size  set size of the frame...
	 * @author yourName
	 */
//初始化面板，设置面板标题与尺寸
	private void initiateFrame(String title, Tuple size) {
		new JFrame(title);
		//窗口标题
		this.setTitle(title);
		//窗口尺寸
		setSize(size.getX(), size.getY());
		//绑定关闭
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//可见
		setVisible(true);
	}

//在frame中增加面板
	private void addPanel(JPanel panel) {
		panels.add(panel);
		add(panel);
		setBounds(panel.getBounds());
	}
}



