package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

import game.Game;
import game.GameConstants;
import game.TimerListener;

/**
 * Create a panel on the right side of mapManel. Show the status of player(s)
 * and provide buttons for specific usage. Related to mainFrame in order to
 * realize the "back" function.
 *
 * @author Chengsong Xiong, Wang
 * @version 0.9
 */
public class StatusPanel extends JPanel implements GameConstants {
    private BufferedImage playerImage[] = new BufferedImage[2];

    private BufferedImage bombImage[] = new BufferedImage[2];
    private JTextField bombText[] = new JTextField[2];


    private JTextField playerLifeText[] = new JTextField[2];
    private JButton pauseButton = new JButton("Pause");
    private JButton backButton = new JButton("Back");
    private JButton restartButton = new JButton("Restart");
    private Game game;
    private MainFrame mainFrame;

    public StatusPanel(Game game, MainFrame mainFrame) {
        this.game = game;
        this.mainFrame = mainFrame;

        try {
            loadImage();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        this.setLayout(null);

        for (int i = 0; i < game.getPlayerNum(); i++) {

            this.playerLifeText[i] = new JTextField("");
            this.playerLifeText[i].setBounds(4 * CELL_WIDTH, 3 * (i + 1) * CELL_HEIGHT, 4 * CELL_WIDTH, CELL_HEIGHT / 2);
            this.bombText[i] = new JTextField("");
            this.bombText[i].setBounds(4 * CELL_WIDTH, (3 * i + 1) * CELL_HEIGHT, 4 * CELL_WIDTH, CELL_HEIGHT / 2);


            Font textFont = new Font("Times New Roman Italic", Font.BOLD, 14);
            this.playerLifeText[i].setEditable(false);
            this.playerLifeText[i].setForeground(Color.BLUE);
            this.playerLifeText[i].setBorder(null);
            this.playerLifeText[i].setFont(textFont);
            this.playerLifeText[i].setVisible(true);
            this.playerLifeText[i].setBackground(null);
            this.bombText[i].setEditable(false);
            this.bombText[i].setBorder(null);
            this.bombText[i].setFont(textFont);
            this.bombText[i].setVisible(true);
            this.bombText[i].setBackground(null);

            this.add(this.playerLifeText[i]);
            this.add(this.bombText[i]);
        }

        // Pause
        this.pauseButton.setBounds(CELL_WIDTH, 8 * CELL_HEIGHT, 2 * CELL_WIDTH, CELL_HEIGHT);
        initializeButton(pauseButton, "pause");

        // Back to main menu
        this.backButton.setBounds(CELL_WIDTH, 10 * CELL_HEIGHT, 2 * CELL_WIDTH, CELL_HEIGHT);
        initializeButton(backButton, "back");

        // Restart game
        this.restartButton.setBounds(CELL_WIDTH, 12 * CELL_HEIGHT, 2 * CELL_WIDTH, CELL_HEIGHT);
        initializeButton(restartButton, "restart");

    }

    public void initializeButton(JButton button, String name) {

        Border originBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
        // This is the default border of WIN10 system.For macOS, use this border to make
        // sure the buttons are correctly initialized.
        Font buttonFont = new Font("Times New Roman Italic", Font.BOLD, 14);

        button.setForeground(Color.BLACK);
        button.setBorder(originBorder);
        button.setBackground(Color.WHITE);
        button.setFont(buttonFont);
        button.setOpaque(true);
        button.setVisible(true);
        button.addMouseListener(new ButtonListener(mainFrame));
        this.add(button);

    }

    /**
     * Highlight the buttons when the mouse is on them
     */
    public void highlightButton(JButton button) {
        button.setBackground(Color.CYAN);
        button.setBounds(button.getX() - CELL_WIDTH / 2, button.getY() - CELL_HEIGHT / 4,
                button.getWidth() + CELL_WIDTH, button.getHeight() + CELL_HEIGHT / 2);
        Font buttonFont = new Font("Times New Roman Italic", Font.BOLD, 20);
        button.setFont(buttonFont);
    }

    /**
     * Reset the buttons when the mouse leaves
     */
    public void resetButton(JButton button) {
        button.setBackground(Color.WHITE);
        button.setBounds(button.getX() + CELL_WIDTH / 2, button.getY() + CELL_WIDTH / 4, button.getWidth() - CELL_WIDTH,
                button.getHeight() - CELL_HEIGHT / 2);
        Font buttonFont = new Font("Times New Roman Italic", Font.BOLD, 14);
        button.setFont(buttonFont);
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        for (int i = 0; i < game.getPlayerNum(); i++) {
            int playerHP = game.getPlayer()[i].getHP();
            int bombNum = game.getPlayer()[i].getBombMaxNumber() - game.getPlayer()[i].getBombPlantedNumber();
            int pivotY = 3 * (i + 1);


            this.playerLifeText[i].setText(String.valueOf(playerHP));
            g.setColor(Color.BLUE);
            g.drawRect(CELL_WIDTH, pivotY * CELL_HEIGHT, 100, CELL_HEIGHT / 2);
            g.setColor(Color.getHSBColor((float) playerHP / 300, 1, 1));
            g.fillRect(CELL_WIDTH, pivotY * CELL_HEIGHT, game.getPlayer()[i].getHP(), CELL_HEIGHT / 2);

            g.drawImage(playerImage[i], CELL_WIDTH, (pivotY - 2) * CELL_HEIGHT, CELL_WIDTH + CELL_WIDTH / 2,
                    CELL_HEIGHT + CELL_HEIGHT / 2, this);
            // bomb
            this.bombText[i].setText(String.valueOf(bombNum));
            g.drawImage(bombImage[1], 3 * CELL_WIDTH, (pivotY - 2) * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, this);


        }

    }

    /**
     * Respond to button events
     */
    class ButtonListener implements MouseListener {

        MainFrame mainFrame;

        public ButtonListener(MainFrame mainFrame) {
            this.mainFrame = mainFrame;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mousePressed(MouseEvent e) {
            /*
             * Pause
             */
            if (e.getSource() == pauseButton) {
                if (game.getPauseFlag() == 0) {
                    game.setPauseFlag(1);
                    pauseButton.setText("Start");
                } else {
                    game.setPauseFlag(0);
                    pauseButton.setText("Pause");
                }
            }

            /*
             * Back
             */
            if (e.getSource() == backButton) {
                int gameMode = game.getGameMode();
                StagePanel newStagePanel = new StagePanel(mainFrame, gameMode);

                JPanel mainPanel = (JPanel) mainFrame.getContentPane();
                mainPanel.removeAll();

                mainFrame.add(newStagePanel);
                mainFrame.validate();

                mainFrame.setLayout(null);

                newStagePanel.setLocation(0, 0);
                newStagePanel.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            }

            /*
             * Restart
             */
            if (e.getSource() == restartButton) {
                int[][] wallMatrix = StagePanel.loadStage(game.getStageNumber());
                Game newGame = new Game(wallMatrix, 0, 0, new int[5], new int[5], game.getGameMode(), game.getStageNumber());
                MapPanel newMapPanel = new MapPanel(newGame);
                StatusPanel newStatusPanel = new StatusPanel(newGame, mainFrame);

                JPanel mainPanel = (JPanel) mainFrame.getContentPane();
                mainPanel.removeAll();

                mainFrame.add(newMapPanel);
                mainFrame.validate();// repaint

                mainFrame.add(newStatusPanel);
                mainFrame.validate();// repaint

                mainFrame.setLayout(null);

                newMapPanel.setLocation(0, 0);
                newMapPanel.setSize(MAP_WIDTH, MAP_HEIGHT);

                newStatusPanel.setLocation(MAP_WIDTH, 0);
                newStatusPanel.setSize(STATUS_PANEL_WIDTH, STATUS_PANEL_HEIGHT);

                TimerListener newTimerListener = new TimerListener(newGame, newMapPanel, newStatusPanel);
                Timer newTimer = new Timer(REFRESH, newTimerListener);
                newTimer.start();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseEntered(MouseEvent e) {

            if (e.getSource() == pauseButton)
                highlightButton(pauseButton);
            else if (e.getSource() == backButton)
                highlightButton(backButton);
            else if (e.getSource() == restartButton)
                highlightButton(restartButton);

        }

        @Override
        public void mouseExited(MouseEvent e) {

            if (e.getSource() == pauseButton)
                resetButton(pauseButton);
            else if (e.getSource() == backButton)
                resetButton(backButton);
            else if (e.getSource() == restartButton)
                resetButton(restartButton);

        }
    }

    public void loadImage() throws Exception {
        // TODO Load all the images here

        playerImage[0] = ImageIO.read(new File("image/player/p1DOWN.png"));
        playerImage[1] = ImageIO.read(new File("image/player/p2DOWN.png"));


        bombImage[1] = ImageIO.read(new File("image/bomb/bomb.png"));
    }


}
