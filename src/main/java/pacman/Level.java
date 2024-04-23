package pacman;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;

class Level {
	
	int[][] ghostsInitialCoordinates = new int[2][];
	short[] initialMazeData;
	static Level[] levels = new Level[2];
	Image background;
	Color dotColor = Color.yellow;
	Color fruitColor = Color.blue;
	
	final static int BLOCK_SIZE = 24;
	static final int N_BLOCKS = 17;
	int pacmanInitialX, pacmanInitialY;
	int upperMargin, sideMargin;
	int numberOfGhosts;
	
	static void initializeAllLevels(){
		initializeLevel0();
		initializelevel1();
	}


	private static void initializeLevel0() {
		levels[0] = new Level();
		
levels[0].background = new ImageIcon("src/main/resources/pictures/mazeLevel1.png").getImage();
		
		levels[0].dotColor = Color.red;
		levels[0].fruitColor = Color.blue;
		
		levels[0].sideMargin = BLOCK_SIZE;
		levels[0].upperMargin = BLOCK_SIZE;
		
		levels[0].pacmanInitialX = levels[0].sideMargin;
		levels[0].pacmanInitialY = levels[0].upperMargin;
		
		levels[0].ghostsInitialCoordinates = new int[][]{
			{336 + levels[0].sideMargin, levels[0].sideMargin, Board.BLOCK_SIZE + levels[0].sideMargin,
			6 * Board.BLOCK_SIZE + levels[0].sideMargin, 8 * Board.BLOCK_SIZE + levels[0].sideMargin, 336 + levels[0].sideMargin},
			{0 + levels[0].upperMargin, 6*Board.BLOCK_SIZE + levels[0].upperMargin, 13 * Board.BLOCK_SIZE + levels[0].upperMargin, 
			6 * Board.BLOCK_SIZE + levels[0].upperMargin, 8 * Board.BLOCK_SIZE + levels[0].upperMargin, 336 + levels[0].upperMargin}
			};
			
			levels[0].numberOfGhosts = levels[0].ghostsInitialCoordinates[0].length;
			
		
		levels[0].initialMazeData = new short[]
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
				0, 19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 38, 0, 
				0, 21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 
				0, 21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,
				0, 21, 0, 0, 0, 17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20, 0,
				0, 17, 18, 18, 18, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 20, 0,
				0, 33, 16, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 16, 24, 20, 0,
				0, 25, 16, 16, 16, 24, 24, 28, 0, 25, 24, 24, 16, 20, 0, 21, 0,
				0, 1, 17, 16, 20, 0, 0, 0, 0, 0, 0, 0, 17, 20, 0, 21, 0,
				0, 1, 17, 16, 16, 18, 18, 22, 0, 19, 18, 18, 16, 20, 0, 21, 0,
				0, 1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21, 0,
				0, 1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21, 0,
				0, 1, 17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0, 21, 0,
				0, 1, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 21, 0,
				0, 1, 25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20, 0,
				0, 9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 25, 24, 24, 24, 44, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
	}




	private static void initializelevel1() {
		levels[1] = new Level();
		
levels[1].background = new ImageIcon("src/main/resources/pictures/mazeLevel2.png").getImage();
		
		levels[1].dotColor = Color.white;
		levels[1].fruitColor = Color.blue;
		
		levels[1].sideMargin = BLOCK_SIZE;
		levels[1].upperMargin = BLOCK_SIZE;
		
		levels[1].pacmanInitialX = levels[1].sideMargin;
		levels[1].pacmanInitialY = levels[1].upperMargin;
		
		levels[1].ghostsInitialCoordinates = new int[][]{
			{levels[1].sideMargin, 11 * Board.BLOCK_SIZE + levels[1].sideMargin, levels[1].sideMargin + 2* Board.BLOCK_SIZE, 
				levels[1].sideMargin + 14 * Board.BLOCK_SIZE, levels[1].sideMargin, levels[1].sideMargin + 13 *Board.BLOCK_SIZE},
			{levels[1].upperMargin + 4 * Board.BLOCK_SIZE, levels[1].upperMargin + 2*Board.BLOCK_SIZE, 
					levels[1].upperMargin + 8 * Board.BLOCK_SIZE, levels[1].upperMargin + 7 * Board.BLOCK_SIZE,
					levels[1].upperMargin + 13 * Board.BLOCK_SIZE, levels[1].upperMargin + 11 * Board.BLOCK_SIZE}};
		
		levels[1].numberOfGhosts = levels[1].ghostsInitialCoordinates[0].length;

			
		levels[1].initialMazeData = new short[]
				{	
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 19, 26, 18, 26, 26, 26, 26, 26, 26, 22, 0, 19, 26, 26, 46, 0,
					0, 21, 0, 21, 0, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0,
					0, 21, 0, 25, 26, 22, 0, 19, 26, 26, 24, 26, 24, 26, 22, 0, 0,
					0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 0, 21, 0, 0,
					0, 25, 26, 18, 26, 16, 26, 24, 26, 26, 22, 0, 19, 26, 24, 30, 0,
					0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0,
					0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0,
					0, 0, 0, 21, 0, 25, 26, 18, 26, 26, 24, 26, 24, 18, 18, 30, 0,
					0, 0, 0, 21, 0, 0, 0, 21, 0, 0, 0, 0, 0, 17, 28, 0, 0,
					0, 0, 0, 21, 0, 19, 26, 20, 0, 0, 0, 0, 0, 21, 0, 0, 0,
					0, 0, 0, 21, 0, 21, 0, 21, 0, 0, 0, 0, 0, 21, 0, 23, 0,
					0, 0, 0, 17, 26, 28, 0, 17, 26, 26, 26, 26, 26, 24, 18, 28, 0, 
					0, 0, 0, 21, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 21, 0, 0,
					0, 43, 26, 24, 18, 26, 26, 24, 26, 26, 22, 0, 19, 26, 28, 0, 0,
					0, 0, 0, 0, 29, 0, 0, 0, 0, 0, 29, 0, 29, 0, 0, 0, 0,
					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		
	}
}