package pacman;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

public class Pacman {
	
	private final int ANIM_DELAY = 3;
	private int animCount = ANIM_DELAY;
	private int animPos = 0;
	private int speed = 6;
	int posInScreenData;
	int currentBlock;
	
	int coordinatesX, coordinatesY;
	
	Board board;
	
	Direction currentDirection, pressedDirection;
	
	Image imagePacmanStill = new ImageIcon("src/main/resources/pictures/pacmanpacman.png").getImage();
	Image imageUp1 = new ImageIcon("src/main/resources/pictures/up1.png").getImage();
	Image imageUp2 = new ImageIcon("src/main/resources/pictures/up2.png").getImage();
	Image imageUp3 = new ImageIcon("src/main/resources/pictures/up3.png").getImage();
	Image imageDown1 = new ImageIcon("src/main/resources/pictures/down1.png").getImage();
	Image imageDown2 = new ImageIcon("src/main/resources/pictures/down2.png").getImage();
	Image imageDown3 = new ImageIcon("src/main/resources/pictures/down3.png").getImage();
	Image imageRight1 = new ImageIcon("src/main/resources/pictures/right1.png").getImage();
	Image imageRight2 = new ImageIcon("src/main/resources/pictures/right2.png").getImage();
	Image imageRight3 = new ImageIcon("src/main/resources/pictures/right3.png").getImage();
	Image imageLeft1 = new ImageIcon("src/main/resources/left1.png").getImage();
	Image imageLeft2 = new ImageIcon("src/main/resources/left2.png").getImage();
	Image imageLeft3 = new ImageIcon("src/main/resources/left3.png").getImage();
	Image imageRed = new ImageIcon("src/main/resources/red.png").getImage();
	Image lifeEmpty = new ImageIcon("src/main/resources/pictures/lifeEmpty.png").getImage();
	Image currentImage;

	
	Pacman(Board board){
		this.board = board;
	}
	
	public void setPressedDirectionUp() {
		pressedDirection = Direction.UP;
	}
	
	public void setPressedDirectionRight() {
		pressedDirection = Direction.RIGHT;
	}
	
	public void setPressedDirectionDown() {
		pressedDirection = Direction.DOWN;
	}
	
	public void setPressedDirectionLeft() {
		pressedDirection = Direction.LEFT;
	}
	
	public void setPressedDirectionStill() {
		pressedDirection = Direction.STILL;
	}

	
	
	void draw(Graphics g, Board board){
		setImage();
		g.drawImage(currentImage, coordinatesX, coordinatesY, board);
	}
	
	
	public void setImage() {
		switch(currentDirection) {
			case UP:
				setImageUp();
				break;
			case RIGHT:
				setImageRight();
				break;
			case DOWN:
				setImageDown();
				break;
			case LEFT:
				setImageLeft();
				break;
			case STILL:
				setImageStill();
				break;
			default : 
				break;
		}
	}
	

	void drawRed(Graphics g, Board board) {
		g.drawImage(imageRed, coordinatesX, coordinatesY, board);
	}

	private void setImageStill() {
		currentImage = imagePacmanStill;		
	}

	private void setImageLeft() {
		switch(animPos) {
			case 1:
				currentImage = imageLeft1;
				break;
			case 2:
				currentImage = imageLeft2;
				break;
			case 3:
				currentImage = imageLeft3;
				break;
			case 4:
				currentImage = imageLeft2;
				break;
			default:
				currentImage = imagePacmanStill;
				break;
		}
	}


	
	private void setImageDown() {
		switch(animPos) {
			case 1:
				currentImage = imageDown1;
				break;
			case 2:
				currentImage = imageDown2;
				break;
			case 3:
				currentImage = imageDown3;
				break;
			case 4:
				currentImage = imageDown2;
				break;
			default:
				currentImage = imagePacmanStill;
				break;
		}
	}

	

	private void setImageRight() {
		switch(animPos) {
			case 1:
				currentImage = imageRight1;
				break;
			case 2:
				currentImage = imageRight2;
				break;
			case 3:
				currentImage = imageRight3;
				break;
			case 4:
				currentImage = imageRight2;
				break;
			default:
				currentImage = imagePacmanStill;
				break;
		}
	}
		


	private void setImageUp() {
		switch(animPos) {
			case 1:
				currentImage = imageUp1;
				break;
			case 2:
				currentImage = imageUp2;
				break;
			case 3:
				currentImage = imageUp3;
				break;
			case 4:
				currentImage = imageUp2;
				break;
			default:
				currentImage = imagePacmanStill;
				break;
		}
	}
	
	
	void move() {
		
		if(isOnIntersection()) {
			posInScreenData = coordinatesX / Board.BLOCK_SIZE + Board.BLOCK_NUMBER*(int)(coordinatesY / Board.BLOCK_SIZE);
			currentBlock = Board.screenData[posInScreenData];
			
			currentDirection = Direction.STILL;
			
			checkIfOnFruit();
			
			checkIfOnSpecialFruit();
			
			board.checkFruitsLeft();
			
			if(pressedDirection != Direction.STILL) { // if an arrow is being pressed
				moveIfNoWall();
			}
			stopIfWallBlocking();
		}
			updateCoordinates();
	}
	
	
	private void stopIfWallBlocking() {
		if((currentDirection == Direction.LEFT && (currentBlock & 1) != 0)
				|| (currentDirection == Direction.RIGHT && (currentBlock & 4) != 0)
				|| (currentDirection == Direction.UP && (currentBlock & 2) != 0)
				|| (currentDirection == Direction.DOWN && (currentBlock & 8) != 0)) {
				
					currentDirection = Direction.STILL;
					
				}		
	}

	
	
	private boolean isOnIntersection() {
		return coordinatesX %  Board.BLOCK_SIZE == 0 && coordinatesY % Board.BLOCK_SIZE == 0;
	}
	
	
	
	private void moveIfNoWall() {
		if(((pressedDirection == Direction.LEFT && (currentBlock & 1) == 0)  
				|| (pressedDirection == Direction.RIGHT && (currentBlock & 4) == 0)  
				|| (pressedDirection == Direction.UP && (currentBlock & 2) == 0)
				|| (pressedDirection == Direction.DOWN && (currentBlock & 8) == 0))) {
			
			currentDirection = pressedDirection;
		}		
	}
	
	
	
	void checkIfOnFruit() {
		if((currentBlock & 16) != 0) {
			Board.screenData[posInScreenData] = (short)(currentBlock & 15);
			Board.score++;
			Board.fruitsLeft --;
		}
	}
	
	
	
	void checkIfOnSpecialFruit() {
		if((currentBlock & 32) != 0) {
			Board.screenData[posInScreenData] = (short)(currentBlock & 31);
			Board.score += 2;
			Board.fruitsLeft --;
			board.toInvicibleMode();
		}
	}

	
	
	private void updateCoordinates() {
		
		switch(currentDirection) {
		case UP:
			coordinatesY -= speed;
			break;
		case RIGHT:
			coordinatesX += speed;
			break;
		case DOWN:
			coordinatesY += speed;
			break;
		case LEFT:
			coordinatesX -= speed;
			break;
		case STILL:
			break;
		default : 
			break;
		}	
	}



	void animate() {
		if(animCount % ANIM_DELAY == 0) {
			animPos = (animPos + 1) % 4;
		}
			animCount++;
	}

}



class Block{
	
	Boolean upWall;
	Boolean rightWall;
	Boolean downWall;
	Boolean leftWall;
	Boolean hasFruit;
	
	Block(boolean upWall, boolean rightWall, boolean downWall, boolean leftWall, boolean hasFruit){
		
		this.hasFruit = hasFruit;
		this.downWall = downWall;
		this.rightWall = rightWall;
		this.upWall = upWall;
		this.leftWall = leftWall;
		
	}
	
	public Boolean getFruit() {
		return hasFruit;
	}

	public void setFruit(Boolean fruit) {
		this.hasFruit = fruit;
	}

	public Boolean getUpWall() {
		return upWall;
	}
	
	public void setUpWall(Boolean upWall) {
		this.upWall = upWall;
	}
	public Boolean getRightWall() {
		return rightWall;
	}
	
	public void setRightWall(Boolean rightWall) {
		this.rightWall = rightWall;
	}
	
	public Boolean getDownWall() {
		return downWall;
	}
	
	public void setDownWall(Boolean downWall) {
		this.downWall = downWall;
	}
	
	public Boolean getLeftWall() {
		return leftWall;
	}
	
	public void setLeftWall(Boolean leftWall) {
		this.leftWall = leftWall;
	}
}


enum Direction {
	UP, 
	RIGHT, 
	DOWN, 
	LEFT,
	START,
	STILL,
	END;

	static Direction randomDirection() {
		Direction[] directions = values();
		return directions[new Random().nextInt(directions.length - 3)];
	}
}