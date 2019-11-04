package game;

public interface GameConstants {	
	// DirectionConstants
	int DIRECTION_STOP = -1;
	int DIRECTION_UP = 0;
	int DIRECTION_RIGHT = 1;
	int DIRECTION_DOWN = 2;
	int DIRECTION_LEFT = 3;

	// Monster constants
	int MONSTER_NUMBER = 5;
	int MONSTER_WIDTH = 60;
	int MONSTER_HEIGHT = 60;

	// Item constants
	int BOMB_UP = 0;
	int VELOCITY_UP = 1;
	int POWER_UP = 2;

	// Display constants
	int CELL_WIDTH = 60;
	int CELL_HEIGHT = 60;
	int CELL_NUM_X = 16;
	int CELL_NUM_Y = 16;
	int SCREEN_WIDTH = CELL_WIDTH * CELL_NUM_X;
	int SCREEN_HEIGHT = CELL_HEIGHT * CELL_NUM_Y;
}
