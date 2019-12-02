package ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.Border;

import game.Game;
import game.GameConstants;
import game.TimerListener;


/**
 * Main menu panel. Fill in the MainFrame. Contain buttons to jump to other
 * panels.
 * 
 * @author Wang
 * @version 0.9
 */
public class StagePanel extends JPanel implements GameConstants {
	private MainFrame mainFrame;
	private JFrame thumbFrame;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton buttonBack;

	private ImageIcon stageBackgroundIcon;
	private JLabel stageBackgroundLabel;
	private int[][] wallMatrix=new int[CELL_NUM_X][CELL_NUM_Y];
	private int gameMode;
	private int player1CharacterID;
	private int player2CharacterID;
	
	public StagePanel(MainFrame mainFrame,int gameMode,int p1cID, int p2cID) {
		this.mainFrame = mainFrame;
		this.gameMode=gameMode;
		this.player1CharacterID = p1cID;
		this.player2CharacterID = p2cID;

		thumbFrame = new JFrame();
		thumbFrame.setAlwaysOnTop(true);
		thumbFrame.setUndecorated(true);
		thumbFrame.setVisible(false);
		thumbFrame.setSize(CELL_WIDTH/SCALE_FACTOR*CELL_NUM_X,
				CELL_HEIGHT/SCALE_FACTOR*CELL_NUM_Y);

		this.addButton();
		this.addBackground();
	}

	public void addBackground() {
		stageBackgroundIcon = new ImageIcon("image/menu/ChoosePlayerPanelBackground.png");// Background image
		stageBackgroundIcon.setImage(stageBackgroundIcon.getImage().getScaledInstance(WINDOW_WIDTH, WINDOW_HEIGHT, 1));
		stageBackgroundLabel = new JLabel(stageBackgroundIcon);
		stageBackgroundLabel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.add(stageBackgroundLabel);
	}

	public void addButton() {
		/*
		 * Stage 1
		 */
		button1 = new JButton("Stage 1");
		button1.setBounds(50, 150, 150, 50);
		initializeButton(button1);

		/*
		 * Stage 2
		 */
		button2 = new JButton("Stage 2");
		button2.setBounds(50, 250, 150, 50);
		initializeButton(button2);

		/*
		 * Stage 3
		 */
		button3 = new JButton("Stage 3");
		button3.setBounds(50, 350, 150, 50);
		initializeButton(button3);
		
		/*
		 * Back
		 */
		buttonBack = new JButton("Back");
		buttonBack.setBounds(50, 550, 150, 50);
		initializeButton(buttonBack);

		this.setLayout(null);

		this.add(button1);
		this.add(button2);
		this.add(button3);
		this.add(buttonBack);

		button1.addMouseListener(new ButtonListener(mainFrame, "1",this.player1CharacterID,this.player2CharacterID));
		button2.addMouseListener(new ButtonListener(mainFrame, "2",this.player1CharacterID,this.player2CharacterID));
		button3.addMouseListener(new ButtonListener(mainFrame, "3",this.player1CharacterID,this.player2CharacterID));
		buttonBack.addMouseListener(new ButtonListener(mainFrame, "Back",this.player1CharacterID,this.player2CharacterID));
	}

	/**
	 * Initialize buttons
	 */
	public void initializeButton(JButton button) {

		Border originBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
		// This is the default border of WIN10 system.For macOS, use this border to make
		// sure the buttons are correctly initialized.

		Font buttonFont = new Font("Times New Roman Italic", Font.BOLD, 14);

		button.setForeground(Color.BLACK);
		button.setBorder(originBorder);
		button.setBackground(Color.WHITE);
		button.setFont(buttonFont);
		button.setOpaque(true);

	}

	/**
	 * Highlight the buttons when the mouse is on them
	 */
	public void highLightButton(JButton button) {
		button.setBackground(Color.ORANGE);
		button.setBounds(button.getX() - 20, button.getY() - 10, button.getWidth() + 40, button.getHeight() + 20);
		Font buttonFont = new Font("Times New Roman Italic", Font.BOLD, 20);
		button.setFont(buttonFont);
	}

	/**
	 * Reset the buttons when the mouse leaves
	 */
	public void resetButton(JButton button) {
		button.setBackground(Color.WHITE);
		button.setBounds(button.getX() + 20, button.getY() + 10, button.getWidth() - 40, button.getHeight() - 20);
		Font buttonFont = new Font("Times New Roman Italic", Font.BOLD, 14);
		button.setFont(buttonFont);
	}
	
	public static int[][] loadStage(int stageNumber) {
		BufferedReader in;
		int[][] wallMatrix=new int[CELL_NUM_X][CELL_NUM_Y];
		try {
			in = new BufferedReader(new FileReader(new File("data/stage"+stageNumber+".txt")));
			String line;
			int row = 0;
			while ((line = in.readLine()) != null) {
				String[] temp = line.split("\t");
				for (int j = 0; j < temp.length; j++) {
					wallMatrix[row][j] = Integer.parseInt(temp[j]);
				}
				row++;
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return wallMatrix;
	}
	
	/**
	 * Respond to button events
	 */
	class ButtonListener implements MouseListener {

		MainFrame mainFrame;
		String name;
		private int player1CharacterID;
		private int player2CharacterID;

		public ButtonListener(MainFrame mainFrame, String name,int p1cid,int p2cid) {
			this.mainFrame = mainFrame;
			this.name = name;
			this.player1CharacterID = p1cid;
			this.player2CharacterID = p2cid;
		}
		
		public void generateStage(int stageNumber) {
			wallMatrix=loadStage(stageNumber);
			Game game = new Game(wallMatrix,0,0,new int[5],new int[5],gameMode,stageNumber,
					player1CharacterID,player2CharacterID);
			
			MapPanel mapPanel = new MapPanel(game);
			StatusPanel statusPanel = new StatusPanel(game, mainFrame);

			JPanel mainPanel = (JPanel) mainFrame.getContentPane();
			mainPanel.removeAll();

			mainFrame.add(mapPanel);
			mainFrame.validate();// repaint

			mainFrame.add(statusPanel);
			mainFrame.validate();// repaint

			mainFrame.setLayout(null);

			mapPanel.setLocation(0, 0);
			mapPanel.setSize(MAP_WIDTH, MAP_HEIGHT);

			statusPanel.setLocation(MAP_WIDTH, 0);
			statusPanel.setSize(STATUS_PANEL_WIDTH, STATUS_PANEL_HEIGHT);

			TimerListener timerListenerPve = new TimerListener(game, mapPanel, statusPanel);
			Timer timerPve = new Timer(REFRESH, timerListenerPve);
			timerPve.start();
			
	
		}


		private void updateThumbFrame(JButton button, int stageNum) {
			thumbFrame.getContentPane().removeAll();
			thumbFrame.add(new ThumbnailPanel(stageNum));
			thumbFrame.setLocation(button.getX()+mainFrame.getX()+button.getWidth()+40,
					button.getY()+mainFrame.getY()-thumbFrame.getHeight()/2+button.getHeight()/2);
			thumbFrame.setVisible(true);
		}
		
		
		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			thumbFrame.setVisible(false);
			thumbFrame.dispose();

			switch (this.name) {
			case "1":
				generateStage(1);
				break;
			case "2":
				generateStage(2);
				break;
			case "3":
				generateStage(3);
				break;
			case "Back":
				ChoosePlayerPanel choosePlayerPanel = new ChoosePlayerPanel(mainFrame,gameMode);

				JPanel mainPanel = (JPanel) mainFrame.getContentPane();
				mainPanel.removeAll();

				mainFrame.add(choosePlayerPanel);
				mainFrame.validate();

				mainFrame.setLayout(null);

				choosePlayerPanel.setLocation(0, 0);
				choosePlayerPanel.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
				break;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			switch (this.name) {
			case "1":
				highLightButton(button1);
				updateThumbFrame(button1, 1);
				break;

			case "2":
				highLightButton(button2);
				updateThumbFrame(button2, 2);
				break;
			case "3":
				highLightButton(button3);
				updateThumbFrame(button3, 3);
				break;
			case "Back":
				highLightButton(buttonBack);
				break;
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			thumbFrame.setVisible(false);
			thumbFrame.dispose();	// necessary when mouse move too quickly

			switch (this.name) {
			case "1":
				resetButton(button1);
				break;
			case "2":
				resetButton(button2);
				break;
			case "3":
				resetButton(button3);
				break;
			case "Back":
				resetButton(buttonBack);
				break;
			}
		}
	}
}
