package pacman;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import pacman.Ghost.State;


public class Board extends JPanel implements ActionListener, KeyListener {
	
	Image introScreenImage = new ImageIcon("src/main/resources/pacmanIntro.png").getImage();
	
	//Delays and countdowns
	private final int REFRESH_DELAY = 40;
	private final static int BLINKING_DURATION = 15;
	private int blinkingCount = 0;
	static final int INVICIBLE_DURATION = 300;
	static int invicibleCountDown = INVICIBLE_DURATION;
	private final int LIFE_LOST_PAUSE = 2000;
	private final int RECOVERING_CYCLES = 40;
	private int recoveringCyclesLeft;
	private final int LEVEL_COMPLETED_DELAY = 30;
	private int levelCompletedCountDown = LEVEL_COMPLETED_DELAY;
	private final int LEVEL_PRESENTATION_DELAY = 60;
	private int levelPresentationCountDown = LEVEL_PRESENTATION_DELAY;

	
	//Special Fruit Animation
	short specialFruitCycle = 0;
	int specialFruitDiameter = 0;
	boolean increasingSpecialFruitDiameter = true;
	boolean decreasingSpecialFruitDiameter = false;
	
	static final int BLOCK_SIZE = Level.BLOCK_SIZE;
	static final int BLOCK_NUMBER = Level.N_BLOCKS;
	private Dimension dimension = new Dimension(BLOCK_SIZE * BLOCK_NUMBER, BLOCK_SIZE * (BLOCK_NUMBER + 1));
	private final int SCREEN_SIZE = BLOCK_NUMBER * BLOCK_SIZE;
	private final int INITIAL_LIVES = 3;
	
	private int levelNumber;
	static int fruitsLeft;
	static int livesLeft, score;
	
	private Level level = new Level();
	private Pacman pacman = new Pacman(this);
	
	static short[] screenData;
	
	private Timer timer;
	
	// TEXTS
	Text gameOverText = new Text(new Font("Courier New", Font.BOLD, dimension.height / 10), "Game over", Color.red);
	Text pressEnterText = new Text(new Font("Courier New", Font.BOLD, dimension.height / 20), "Press Enter   ", Color.red);
	Text winText = new Text(new Font("Courier New", Font.BOLD, dimension.height / 10), "You win!", Color.GREEN);
	Text levelCompletedText = new Text(new Font("Courier New", Font.BOLD, dimension.height / 12), "Level completed!", Color.WHITE);
	Text levelPresentationText = new Text(new Font("Courier New", Font.BOLD, dimension.height / 10), "Level " + levelNumber, Color.WHITE);
	Text levelPresentationCountDownText = new Text(new Font("Courier New", Font.BOLD, dimension.height / 10), "", Color.WHITE);
	Text welcomeText = new Text(new Font("Courier New", Font.BOLD, dimension.height / 13), "Welcome to Pacman! ", Color.BLUE);
	Text scoreText = new Text(new Font("Courier New", Font.BOLD, dimension.height / 22), "score : " + score, Color.BLACK);
	
	private Ghost[] ghosts;
	
	
	enum Scenario {
		INTRO,
		START_LEVEL,
		NORMAL,
		INVICIBLE,
		LIFE_LOST,
		RECOVERING,
		LEVEL_COMPLETED,
		WIN,
		END_OF_GAME,
	}
	
	static Scenario scenario = Scenario.INTRO;
	
	public Board() {
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setAlignmentY(Component.CENTER_ALIGNMENT);
	    setPreferredSize(dimension);
	    setMaximumSize(dimension);
		initVariables();
		initBoard();
	}


	private void initBoard() {
		addKeyListener(this);
		setFocusable(true);
		timer = new Timer(REFRESH_DELAY, this);
		timer.start();
	}


	private void initVariables() {
		screenData = new short[BLOCK_NUMBER * BLOCK_NUMBER];
		scoreText.y = dimension.height - BLOCK_SIZE / 3;
		livesLeft = INITIAL_LIVES;
		levelNumber = 0;
		fruitsLeft = 0 ;
		Level.initializeAllLevels();
	}


	private void setInitialFruitNumber() {
		for(short i: Level.levels[levelNumber].initialMazeData) {
			if((i & 16) != 0 || (i & 32) != 0 ) {
				fruitsLeft ++;
			}
		}
	}


	private void initializeGhosts() {
		ghosts = new Ghost[Level.levels[levelNumber].ghostsInitialCoordinates[0].length];
		for(int i = 0 ; i < ghosts.length; i++) {
			ghosts[i] = new Ghost(Level.levels[levelNumber].ghostsInitialCoordinates[0][i], Level.levels[levelNumber].ghostsInitialCoordinates[1][i]);
		}
	}


	@Override
	public void addNotify() {
		super.addNotify();
	}

	
	private void initializeLevel() {
		pacman.coordinatesX = level.levels[levelNumber].pacmanInitialX;
		pacman.coordinatesY = level.levels[levelNumber].pacmanInitialY;
		score = 0;
		levelPresentationCountDown = LEVEL_PRESENTATION_DELAY;
		
		initializeGhosts();
		initializeMaze();
		fruitsLeft = 0;
		setInitialFruitNumber();
	}


	private void initializeMaze() {
		for(int i = 0; i < BLOCK_NUMBER * BLOCK_NUMBER; i++) {
			screenData[i] = Level.levels[levelNumber].initialMazeData[i];
		}
	}


	@Override
	public void paint(Graphics g) {
		super.paint(g);
		doDrawing(g);
	}

	
	private void doDrawing(Graphics g) {
		drawBackgroundAndMaze(g);
		gameScenario(g);
		g.dispose();
	}
	
	
	private void drawBackgroundAndMaze(Graphics g) {
		Graphics2D g2D = (Graphics2D)g;
		g2D.setColor(Color.BLACK);
		drawMaze(g2D);		
	}


	private void drawMaze(Graphics2D g) {
		
		short i = 0;
		int x, y;
		calcSpecialFruitDiameter();
		
		g.drawImage(level.levels[levelNumber].background, 0, 0, this);
		
		for(y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
			
			for(x = 0; x <  SCREEN_SIZE; x += BLOCK_SIZE) {
				/*g.setColor(mazeColor);
				g.setStroke(new BasicStroke(2));
				
				if((screenData[i] & 1) != 0) {
					g.drawLine(x, y, x, y + BLOCK_SIZE-1); 
				}
				
				if((screenData[i] & 2) != 0) {
					g.drawLine(x, y, x + BLOCK_SIZE-1, y ); 
				}
				 
				if((screenData[i] & 4) != 0) {
					g.drawLine(x + BLOCK_SIZE-1, y, x + BLOCK_SIZE-1, y + BLOCK_SIZE-1); 
				} 	
				
				if((screenData[i] & 8) != 0) {
					g.drawLine(x, y +  BLOCK_SIZE-1, x + BLOCK_SIZE-1, y + BLOCK_SIZE-1); 
				}*/
				
				//Fruit
				if((screenData[i] & 16) != 0) {
					g.setColor(level.levels[levelNumber].dotColor);
					g.fillOval(x + 8 , y + 8, 5, 5); 
				}
				
				//Special fruit
				if((screenData[i] & 32) != 0) {
					g.setColor(level.levels[levelNumber].fruitColor);
					g.fillOval(x + BLOCK_SIZE/2 - specialFruitDiameter/2 , y + BLOCK_SIZE/2 - specialFruitDiameter/2 , specialFruitDiameter, specialFruitDiameter);
				}
				i++;
			}
		}
	}
	
	
	private void calcSpecialFruitDiameter() {
		
		if(specialFruitCycle <= 8 && increasingSpecialFruitDiameter) {
			specialFruitCycle ++;
		}else if(specialFruitCycle > 8 && increasingSpecialFruitDiameter){
			increasingSpecialFruitDiameter = false;
			decreasingSpecialFruitDiameter = true;
			specialFruitCycle --;
		}else if (specialFruitCycle >= -8 && decreasingSpecialFruitDiameter) {
			specialFruitCycle --;
		}else if (specialFruitCycle < -8 && decreasingSpecialFruitDiameter) {
			increasingSpecialFruitDiameter = true;
			decreasingSpecialFruitDiameter = false;
			specialFruitCycle ++;
		}
		
		if(specialFruitCycle % 2 == 0) {
			specialFruitDiameter = 6 + specialFruitCycle;
		}
	}


	private void gameScenario(Graphics g) {
		
		switch(scenario) {
			case INTRO:
				introScenario(g);
				break;
				
			case START_LEVEL:
				startLevelScenario(g);
				break;
		
			case NORMAL:
				normalScenario(g);
				break;
				
			case INVICIBLE:
				invicibleScenario(g);				
				break;
				
			case LIFE_LOST:
				lifeLostScenario(g);
				break;
				
			case RECOVERING:
				recoveringScenario(g);
				break;
			
			case LEVEL_COMPLETED:
				levelCompletedScenario(g);
				break;
				
			case END_OF_GAME:
				endOfGameScenario(g);
				break;
				
			case WIN:
				winScenario(g);
				break;
		}
	}


	
	private void introScenario(Graphics g) {

		drawBlackBackground(g);
		
		g.drawImage(introScreenImage, 0, 0, this);
		
		welcomeText.setXCentered(g, dimension);
		welcomeText.setYCentered(g, dimension);
		pressEnterText.setXCentered(g, dimension);
		pressEnterText.y = welcomeText.y + g.getFontMetrics(pressEnterText.font).getHeight() * 2;
		g.setColor(welcomeText.color);
		g.setFont(welcomeText.font);
		g.drawString(welcomeText.string, welcomeText.x, welcomeText.y);	
			
		if(blinkingCount < BLINKING_DURATION) {
				g.drawString(pressEnterText.string, pressEnterText.x, pressEnterText.y);
			}else if(blinkingCount > BLINKING_DURATION * 2) {
				blinkingCount = 0;
			}
			blinkingCount ++;
	}
	
	
	
	private void startLevelScenario(Graphics g) {

		drawBlackBackground(g);
		
		if(levelPresentationCountDown > 0) {

			levelPresentationText.string = "Level " + (levelNumber + 1);
			
			if(levelPresentationCountDown > 3 * LEVEL_PRESENTATION_DELAY / 4) {
				levelPresentationCountDownText.string = "" + 3;
			}
			else if(levelPresentationCountDown > LEVEL_PRESENTATION_DELAY / 2) {
				levelPresentationCountDownText.string = "" + 2;
			}
			else if(levelPresentationCountDown > LEVEL_PRESENTATION_DELAY / 4){
				levelPresentationCountDownText.string = "" + 1;
			}else {
				levelPresentationCountDownText.string = "GO! ";
			}
			
			levelPresentationText.setXCentered(g, dimension);
			levelPresentationText.setYCentered(g, dimension);
			levelPresentationCountDownText.setXCentered(g, dimension);
			g.setColor(levelPresentationText.color);
			g.setFont(levelPresentationText.font);
			g.drawString(levelPresentationText.string, levelPresentationText.x, levelPresentationText.y);	
			g.drawString(levelPresentationCountDownText.string, levelPresentationCountDownText.x, levelPresentationText.y + BLOCK_SIZE * 2);
			
		}else{
			initializeLevel();
			scenario = Scenario.NORMAL;
		}
		
		levelPresentationCountDown --;
	}
	
	
	
	private void levelCompletedScenario(Graphics g) {
		
		drawBlackBackground(g);
		
		if(levelCompletedCountDown == LEVEL_COMPLETED_DELAY) {
			levelCompletedText.setXCentered(g, dimension);
			levelCompletedText.setYCentered(g, dimension);
		}
		
		if(levelCompletedCountDown  > 0) {
			g.setColor(levelCompletedText.color);
			g.setFont(levelCompletedText.font);
			g.drawString(levelCompletedText.string, levelCompletedText.x, levelCompletedText.y);	
		}else {
			levelPresentationCountDown = LEVEL_PRESENTATION_DELAY;
			scenario = Scenario.START_LEVEL;
		}
		levelCompletedCountDown --;
	}
	
	private void normalScenario(Graphics g) {
		moveAndDrawGhosts(g);
		drawPacman(g);
		drawLives(g);
		drawScore(g);
		checkPacmanTouchingGhost(g, this);
	}

	

	private void invicibleScenario(Graphics g) {
		invicibleCountDown --;
		if(invicibleCountDown < 0 ) {
			scenario = Scenario.NORMAL;
		}					
		moveAndDrawGhosts(g);
		drawLives(g);
		drawScore(g);
		
		drawPacman(g);
		checkPacmanTouchingGhost(g, this);
	}


	private void lifeLostScenario(Graphics g) {
		pacman.drawRed(g, this);
		drawLives(g);
		drawScore(g);
		recoveringCyclesLeft = RECOVERING_CYCLES;
		scenario = Scenario.RECOVERING;
		timer.setDelay(REFRESH_DELAY);
	}


	
	private void recoveringScenario(Graphics g) {
		drawPacmanBlinking(g);
		moveAndDrawGhosts(g);
		drawLives(g);
		drawScore(g);
		if(recoveringCyclesLeft -- < 0) {
			scenario = Scenario.NORMAL;
		}
	}
	
	
	private void endOfGameScenario(Graphics g) {
		setGameOverMessages(g);
		displayGameOverMessages(g);
	}
	
	
	void winScenario(Graphics g) {
		setWinMessages(g);
		displayWinMessages(g);
	}



	private void drawPacmanBlinking(Graphics g) {
		if(recoveringCyclesLeft % 3 == 0) {
			drawPacmanInvisible(g);
		}else {
			drawPacman(g);
		}		
	}


	private void drawPacman(Graphics g) {
		pacman.animate();
		pacman.move();
		pacman.draw(g, this);	
	}
	
	
	private void drawPacmanInvisible(Graphics g) {
		pacman.animate();
		pacman.move();		
	}
	
	
	private void moveAndDrawGhosts(Graphics g) {
		for(Ghost ghost: ghosts) {
			ghost.move();
			ghost.draw(g, this);
		}
	}
	
	
	private void drawLives(Graphics g) {
		for(int i = 0 ; i < INITIAL_LIVES; i++) {
			g.drawImage(pacman.lifeEmpty, BLOCK_SIZE * (i), dimension.height - BLOCK_SIZE, this);
		}
		
		for(int j = 0 ; j < livesLeft; j++) {
			g.drawImage(pacman.imagePacmanStill, BLOCK_SIZE * (j), dimension.height - BLOCK_SIZE, this);
		}
		
	}
	
	
	private void drawScore(Graphics g) {
		g.setColor(scoreText.color);
		g.setFont(scoreText.font);
		scoreText.string = "Score : " + score;
		g.drawString(scoreText.string, dimension.width - (g.getFontMetrics().stringWidth(scoreText.string) + BLOCK_SIZE * 2), scoreText.y);
	}
	

	private void checkPacmanTouchingGhost(Graphics g, Board board) {
		for(Ghost ghost: ghosts) {
			if(pacmanIsTouching(ghost) && scenario == Scenario.NORMAL && ghost.state == State.NORMAL) {
				loseALife(g, this);
				break;
			}else if(pacmanIsTouching(ghost) && ghost.state == State.VULNERABLE){
				ghost.eaten();
				score += 10;
			}
		}
	}
	
	
	private boolean pacmanIsTouching(Ghost ghost) {		
		return horizontalIntersectionWithPacman(ghost) || verticalIntersectionWithPacman(ghost);
	}
	
	private boolean verticalIntersectionWithPacman(Ghost ghost) {
		return (Math.abs(ghost.coordinatesX - pacman.coordinatesX) < BLOCK_SIZE) && ghost.coordinatesY == pacman.coordinatesY;
	}

	private boolean horizontalIntersectionWithPacman(Ghost ghost) {
		return (Math.abs(ghost.coordinatesY - pacman.coordinatesY) < BLOCK_SIZE) && ghost.coordinatesX == pacman.coordinatesX;
	}
	
	
	void toInvicibleMode() {
		Board.scenario = Board.Scenario.INVICIBLE;
		Board.invicibleCountDown = Board.INVICIBLE_DURATION;
		
		for(Ghost ghost: ghosts) {
			ghost.vulnerableCountDown = Ghost.VULNERABLE_CYCLES;
			if(ghost.state == State.NORMAL || ghost.state == State.REAPPEARING) {
				ghost.setImageAfraid();
				ghost.state = State.VULNERABLE;
			}
		}
	}


	private void loseALife(Graphics g, Board board) {
		scenario = Scenario.LIFE_LOST;
		timer.setDelay(LIFE_LOST_PAUSE);
		livesLeft --;
		if(livesLeft < 0) {
			scenario = Scenario.END_OF_GAME;
			timer.setDelay(REFRESH_DELAY);
		}
	}
	
	
	public void checkFruitsLeft() {
		if(fruitsLeft <=0) {
			if(levelNumber < Level.levels.length - 1 ) {
				levelNumber ++;
				scenario = Scenario.LEVEL_COMPLETED;
				levelCompletedCountDown = LEVEL_COMPLETED_DELAY;
			}else {
				scenario = Scenario.WIN;
			}
		}
	}
	
	
	
	private void drawBlackBackground(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, dimension.width, dimension.height);
		
	}


	
	private void displayWinMessages(Graphics g) {
		
		drawBlackBackground(g);
		
		g.setColor(winText.color);
		g.setFont(winText.font);
		g.drawString(winText.string, winText.x, winText.y);	
		g.setFont(pressEnterText.font);
		
		if(blinkingCount < BLINKING_DURATION) {
			g.drawString(pressEnterText.string, pressEnterText.x, pressEnterText.y);
		}else if(blinkingCount > BLINKING_DURATION * 2) {
			blinkingCount = 0;
		}
		
		blinkingCount ++;		
	}


	private void setWinMessages(Graphics g) {
		winText.setXCentered(g, dimension);
		winText.setYCentered(g, dimension);
		pressEnterText.setXCentered(g, dimension);
		pressEnterText.y = winText.y + g.getFontMetrics(pressEnterText.font).getHeight() * 2;		
	}


	
	private void displayGameOverMessages(Graphics g) {
		
		drawBlackBackground(g);
		
		g.setColor(gameOverText.color);
		g.setFont(gameOverText.font);
		g.drawString(gameOverText.string, gameOverText.x, gameOverText.y);	
		g.setFont(pressEnterText.font);
		
		if(blinkingCount < BLINKING_DURATION) {
			g.drawString(pressEnterText.string, pressEnterText.x, pressEnterText.y);
		}else if(blinkingCount > BLINKING_DURATION * 2) {
			blinkingCount = 0;
		}
		
		blinkingCount ++;
	}

	
	void setGameOverMessages(Graphics g) {
		gameOverText.setXCentered(g, dimension);
		gameOverText.setYCentered(g, dimension);
		pressEnterText.setXCentered(g, dimension);
		pressEnterText.y = gameOverText.y + g.getFontMetrics(pressEnterText.font).getHeight() * 2;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}


	@Override
	public void keyTyped(KeyEvent e) {
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_LEFT) {
			pacman.setPressedDirectionLeft();
		}else if(key == KeyEvent.VK_RIGHT) {
			pacman.setPressedDirectionRight();
		}else if(key == KeyEvent.VK_UP) {
			pacman.setPressedDirectionUp();
		}else if(key == KeyEvent.VK_DOWN) {
			pacman.setPressedDirectionDown();
		}			
		
		if(scenario == Scenario.INTRO  && key == KeyEvent.VK_ENTER){
			scenario = Scenario.START_LEVEL;
		}
		
		if((scenario == Scenario.END_OF_GAME || scenario == Scenario.WIN)  && key == KeyEvent.VK_ENTER) {
			scenario = Scenario.INTRO;
			initVariables();
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
			
			pacman.setPressedDirectionStill();
		}		
	}

}


class Text {
	
	Font font;
	String string;
	Color color;
	int x, y;
	
	Text(Font font, String string, Color color){
		this.font = font;
		this.string = string;
		this.color = color;
	}
	
	
	void setXCentered(Graphics g, Dimension dimension) {
		g.setFont(this.font);
		int width = g.getFontMetrics().stringWidth(this.string);
		x = (dimension.width - width) / 2;
	}
	
	void setYCentered(Graphics g, Dimension dimension) {
		g.setFont(this.font);
		int height = g.getFontMetrics().getHeight();
		y = (dimension.height - height) / 2;
	}
	
}
