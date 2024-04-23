package pacman;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

class Ghost{
	
	Image ghostImage = new ImageIcon("src/main/resources/pacman/pictures/ghost.png").getImage();
	Image afraidImage = new ImageIcon("src/main/resources/pacman/pictures/ghostAfraid.png").getImage();
	Image eyes = new ImageIcon("src/main/resources/pacman/pictures/eyes.png").getImage();
	Image currentImage = ghostImage;
	
	private int speed = 4;
	
	int coordinatesX;
	int coordinatesY;
	int initialCoordinatesX;
	int initialCoordinatesY;
	
	int posInScreenData;
	int currentBlock;
	
	Direction direction;
	
	static int DISAPPEARED_CYCLES = 150;
	int disappearedCountDown = 0;
	
	static int REAPPEARING_CYCLES = 40;
	int reappearingCountDown;
	
	static int VULNERABLE_CYCLES = 200;
	int vulnerableCountDown;
	
	enum State {
		NORMAL,
		VULNERABLE,
		GOING_HOME,
		DEAD,
		REAPPEARING
	}
	
	State state = State.NORMAL;
	
	
	Ghost(int coordinatesX, int coordinatesY){
		this.coordinatesX = coordinatesX;
		this.coordinatesY = coordinatesY;
		this.initialCoordinatesX = coordinatesX;
		this.initialCoordinatesY = coordinatesY;
				
	}
	
	public void setImageAfraid() {
		currentImage = afraidImage;
	}
	
	public void setImageNormal() {
		currentImage = ghostImage;
	}
	
	void setImageEyes() {
		currentImage = eyes;
	}
	
	void draw(Graphics g, Board board) {
		g.drawImage(currentImage, coordinatesX, coordinatesY, board);
	}
	
	
	void move() {
		
		switch(state) {
			case NORMAL:
				normalMove();
				break;
			case VULNERABLE:
				vulnerableMove();
				break;
			case GOING_HOME:
				goingHome();
				break;
			case DEAD:
				dead();
				break;
			case REAPPEARING:
				reappearing();
				break;
			default:
				break;
		}
	}
	
	
	
	void normalMove() {
	
		if(coordinatesX %  Board.BLOCK_SIZE == 0 && coordinatesY % Board.BLOCK_SIZE == 0) {
			direction = Direction.randomDirection();
		}
		
		if(direction == Direction.RIGHT || direction == Direction.DOWN) {
			posInScreenData = coordinatesX / Board.BLOCK_SIZE + Board.BLOCK_NUMBER*(int)(coordinatesY / Board.BLOCK_SIZE);
		}else if(direction == Direction.LEFT){
			posInScreenData = (coordinatesX + Board.BLOCK_SIZE - 1) / Board.BLOCK_SIZE + Board.BLOCK_NUMBER*(int)((coordinatesY) / Board.BLOCK_SIZE);
		}else if(direction == Direction.UP){
			posInScreenData = (coordinatesX) / Board.BLOCK_SIZE + Board.BLOCK_NUMBER*(int)((coordinatesY + Board.BLOCK_SIZE - 1) / Board.BLOCK_SIZE);
		}
		
		currentBlock = Board.screenData[posInScreenData];
		
		if(noWallBlocking()) {
			updateCoordinates();
		}else {
			direction = Direction.randomDirection();
		}
	}
	
	
	
	void vulnerableMove() {
		
		normalMove();
		vulnerableCountDown --;
		if(vulnerableCountDown < 40 && vulnerableCountDown % 3 == 0) {
			setImageAfraid();
		}else if(vulnerableCountDown < 40 && vulnerableCountDown % 3 != 0) {
			setImageNormal();
		}
		
		if(vulnerableCountDown < 0){
			setImageNormal();
			state = State.NORMAL;
		}
		
	}
	
	
	
	void goingHome() {
		
		if(initialCoordinatesX < coordinatesX ) {
			coordinatesX --;
		}else if(initialCoordinatesX > coordinatesX){
			coordinatesX ++;
		}
		
		if(initialCoordinatesY < coordinatesY ) {
			coordinatesY --;
		}else if(initialCoordinatesY > coordinatesY){
			coordinatesY ++;
		}
			
		if(initialCoordinatesX == coordinatesX && initialCoordinatesY == coordinatesY) {	
			disappearedCountDown = DISAPPEARED_CYCLES;
			state = State.DEAD;
		}
	}
	
	
	
	void dead() {
		if(disappearedCountDown -- < 0) {
			reappearingCountDown = REAPPEARING_CYCLES;
			state = State.REAPPEARING;
		}
	}

	
	
	void reappearing() {
		if(reappearingCountDown -- % 3 == 0) {
			setImageEyes();
		}else{
			setImageNormal();
		}
		
		if(reappearingCountDown < 0) {
			setImageNormal();
			state = State.NORMAL;
		}
	}
	
	
	
	private boolean noWallBlocking() {
		return (direction == Direction.LEFT && (currentBlock & 1) == 0) 
				|| (direction == Direction.RIGHT && (currentBlock & 4) == 0)  
				|| (direction == Direction.UP && (currentBlock & 2) == 0)
				|| (direction == Direction.DOWN && (currentBlock & 8) == 0);
	}
	
	
	
	private void updateCoordinates() {
		switch(direction) {
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
	

	
	void eaten(){
		setImageEyes();
		state = State.GOING_HOME;
	}
}
