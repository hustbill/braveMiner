package miner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jig.Vector;

import org.newdawn.slick.state.StateBasedGame;

/**
 * The Map class is use to parse the maze.txt, store the info into a 2D array
 * and build tile based Game Map with tile Objects
 * 
 * @author Hua Zhang
 */

public class Map {
//	private char[][] entityMaze = {
//            { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-','-'  }, // 0
//			{ 'X', '-', '-', '-', '-', '-', 'M', '-', '-', '-', '-', '-', 'X' , '-', '-','-' }, // 1
//			{ 'X', '.', '-', 'X', '.', '.', '-', '-', 'X', '.', '.', '.', 'X' , '-', '-','-' }, // 2
//			{ 'X', '-', '-', '-', '.', 'X', 'X', '-', '.', '-', '-', '-', 'X' , '-', '-','-' }, // 3
//			{ 'X', '.', '-', 'X', '.', '-', '.', '.', 'X', '-', 'X', '-', '-' , '-', '-','-' }, // 4
//			{ 'X', '.', '-', 'X', 'X', 'X', 'X', '.', 'X', '.', 'X', 'X', 'X' , '-', '-','-' }, // 5
//			{ 'X', 'X', '-', '-', '-', '.', 'X', '.', '-', '-', '-', '.', 'X' , '-', '-','-' }, // 6
//			{ 'X', '.', '-', 'X', '.', '-', '-', '-', 'X', 'X', '-', '.', 'X' , '-', '-','-' }, // 7
//			{ 'X', '.', 'X', 'X', '-', 'X', 'X', '.', 'X', '-', '-', '-', '-' , '-', '-','-' }, // 8
//			{ 'X', '.', '-', 'X', '-', 'X', '.', '-', '-', '-', 'X', '-', 'X' , '-', '-','-' }, // 9
//			{ 'X', '-', '.', '-', '-', 'X', 'X', '.', 'X', 'X', 'X', '-', 'X' , '-', '-','-' }, // 10
//			{ 'X', '.', 'X', '-', 'C', '-', '-', '-', '-', '-', '.', '.', 'X' , '-', '-','-'} }; // 11
	
//	private char[][] entityMaze = 
//        {{'X','X','X','X','X','X','X','X','X','X','X','X','X','X','X'}, 
//        {'X','M','.','.','.','X','.','X','.','.','.','.','.','.','X'}, 
//        {'X','.','X','X','.','X','.','X','.','X','X','X','X','.','X'}, 
//        {'X','.','X','.','.','.','.','.','.','.','.','.','X','.','X'}, 
//        {'X','.','.','.','.','X','.','X','.','X','.','.','.','.','X'}, 
//        {'X','.','X','X','.','X','X','X','X','X','.','X','X','.','X'}, 
//        {'X','.','.','X','.','.','.','X','.','.','.','X','.','.','X'}, 
//        {'X','X','X','X','.','X','X','.','X','X','.','X','X','X','X'}, 
//        {'X','.','.','X','.','.','.','.','.','.','.','X','.','.','X'}, 
//        {'X','.','X','X','.','X','X','X','X','X','.','X','.','.','X'}, 
//        {'X','.','.','.','.','X','.','X','.','X','.','.','.','.','X'}, 
//        {'X','.','X','.','.','.','C','.','.','.','.','.','X','.','X'}, 
//        {'X','.','X','.','X','X','.','X','.','X','X','X','.','.','X'}, 
//        {'X','.','.','.','.','X','.','X','.','X','X','.','.','.','X'}, 
//        {'X','X','X','X','X','X','X','X','X','X','X','X','X','X','X'}};
	/*
	 * 	public final static int EMPTY = 0;
public final static int BRICK = 1;
public final static int GRASS = 2;
public final static int TREE = 3;
public final static int BONUS = 5;
public final static int JEWEL = 6;
public final static int MINER = 7;
public final static int CYCLOPS = 8;
public final static int RED_GHOST = 9;
	 */
	
	  final static public int[][] entityMaze = {
//		  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },  
//	      { 1, 9, 10,0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 0, 1 }, 
//	      { 1, 0, 0, 0, 0, 5, 1, 0, 0, 0, 1, 0, 0, 0, 1 },  
//	      { 0, 0, 0, 0, 0, 5, 1, 0, 0, 0, 1, 0, 0, 0, 1 }, 
//	      { 1, 0, 5, 0, 0, 0, 0, 5, 0, 0, 0, 5, 1, 0, 1 },  
//	      { 1, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5, 1, 0, 1 }, 
//	      { 1, 0, 5, 6, 0, 0, 0, 0, 0, 1, 0, 5, 6, 0, 1 },  
//	      { 1, 0, 5, 1, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 1 }, 
//	      { 1, 0, 5, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1 },  
//	      { 1, 0, 0, 1, 0, 1, 0, 7, 0, 1, 0, 0, 1, 0, 1 }, 
//	      { 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 5, 5, 0, 0, 1 },  
//	      { 1, 0, 6, 1, 0, 0, 0, 0, 0, 0, 0, 1, 6, 0, 1 }, 
//	      { 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1 },  
//	      { 1, 0, 0, 0, 8, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0 },
//	      { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 } };  
		  
		  //0  1  2  3   4  5  6  7  8  9 10 11 12  13 14 15 16 17 18 19	
		  { 1, 1, 1, 1,  1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },  //0
	      { 1, 9, 4, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1 },   //1
	      { 1, 0, 0, 0,  1, 0, 1, 0, 0, 0, 1, 0, 5, 1, 1, 0, 1, 1, 5, 1 },  //2
	      { 1, 0, 0, 0,  1, 0, 1, 0, 0, 0, 1, 0, 5, 1, 0, 0, 0, 1, 5, 1 }, //3
	      { 1, 0, 0, 0,  1, 0, 1, 0, 0, 0, 1, 0, 5, 1, 0, 0, 7, 1, 5, 1 },  //4
	      { 1, 0, 0, 0,  1, 0, 1, 0, 2, 0, 1, 0, 5, 1, 0, 0, 0, 1, 5, 1 }, //5
	      { 1, 1, 0, 1,  1, 0, 1, 1, 0, 1, 1, 0, 5, 1, 0, 0, 1, 1, 5, 1 },  //6
	      { 1, 0, 5, 5,  5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2, 5, 5, 0, 1 }, //7
	      { 1, 0, 0, 1,  1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1 ,0, 1 },  //8
	      { 1, 0, 0, 0,  1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1 }, //9
	      { 1, 0, 0, 2,  1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1 },  //10
	      { 1, 0, 0, 1,  1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1 }, //11
	      { 1, 0, 5, 5,  5, 5, 5, 0, 5, 5, 5, 5, 0, 5, 5, 5, 5 ,5, 0, 1 },  //12
	      { 1, 2, 2, 0,  8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },   //13
	      { 1, 1, 1, 1,  1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 ,1, 1, 1 } };  //14
	
	  static public int[][] entityMaze2 = {
			  //0  1  2  3   4  5  6  7  8  9 10 11 12  13 14 15 16 17 18 19	
			  { 1, 1, 1, 1,  1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },  //0
		      { 1, 1, 1, 0,  0, 1, 6, 0, 1, 0, 0, 6, 0, 0, 0, 0, 0, 1, 0, 1 },   //1
		      { 1, 1, 0, 0,  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1 },  //2
		      { 1, 1, 0, 1,  0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 6, 0, 1 }, //3
		      { 1, 1, 0, 1,  0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1 },  //4
		      { 1, 1, 0, 1,  0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 1, 0, 0, 0, 0, 1 }, //5
		      { 1, 1, 0, 1,  0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1 },  //6
		      { 1, 1, 0, 1,  0, 0, 0, 0, 1, 6, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 }, //7
		      { 1, 1, 0, 1,  0, 0, 0, 0, 1, 0, 0, 0, 6, 0, 0, 0, 0, 1 ,0, 1 },  //8
		      { 1, 1, 1, 1,  0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1 }, //9
		      { 1, 0, 0, 1,  0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1 },  //10
		      { 1, 0, 0, 1,  6, 1, 1, 0, 0, 0, 1, 0, 0, 1, 6, 0, 1, 0, 0, 1 }, //11
		      { 1, 0, 0, 1,  0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1 },  //12
		      { 1, 0, 0, 1,  0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },   //13
		      { 1, 1, 1, 1,  1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 ,1, 1, 1 } };  //14
	  
	  static public int[][] entityMaze3 = {
		  //0  1  2  3   4  5  6  7  8  9 10 11 12  13 14 15 16 17 18 19	
		  { 1, 1, 1, 1,  1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },  //0
	      { 1, 9, 4, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1 },   //1
	      { 1, 0, 0, 0,  1, 0, 1, 0, 0, 0, 1, 0, 5, 1, 1, 0, 1, 1, 5, 1 },  //2
	      { 1, 0, 0, 0,  1, 0, 1, 0, 0, 0, 1, 0, 5, 1, 0, 0, 0, 1, 5, 1 }, //3
	      { 1, 0, 0, 0,  1, 0, 1, 0, 0, 0, 1, 0, 5, 1, 0, 0, 0, 1, 5, 1 },  //4
	      { 1, 0, 0, 0,  1, 0, 1, 0, 2, 0, 1, 0, 5, 1, 0, 0, 0, 1, 5, 1 }, //5
	      { 1, 1, 0, 1,  1, 0, 1, 1, 0, 1, 1, 0, 5, 1, 0, 0, 1, 1, 5, 1 },  //6
	      { 1, 0, 5, 5,  5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2, 5, 5, 0, 1 }, //7
	      { 1, 0, 0, 1,  1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1 ,0, 1 },  //8
	      { 1, 0, 0, 0,  1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1 }, //9
	      { 1, 0, 0, 2,  1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1 },  //10
	      { 1, 0, 0, 1,  1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1 }, //11
	      { 1, 0, 5, 5,  5, 5, 5, 0, 5, 5, 5, 5, 0, 5, 5, 5, 5 ,5, 0, 1 },  //12
	      { 1, 2, 2, 0,  8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },   //13
	      { 1, 1, 1, 1,  1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 ,1, 1, 1 } };  //14 
				

	private int[][] mazeArray; // read from maze.txt
	public Tile[][] map;
	private int width;
	private int height;
	
	 final static public int[] HIT = { 1 }; 
	  final static private int ROW = 15;  
	  final static private int COL = 20;  
	  

	public Map(int r, int c, String filename) {
		width = c;  // rows
		height = r;  // cols 
		map = new Tile[height][width];
		mazeArray = new int[height][width]; // read from maze.txt	
		//mazeArray = parseMaze(filename);
		//if (mazeArray == null) // if read maze.txt file failed,
			mazeArray = entityMaze; // replace it by default maze array
		System.out.println(String.format("mazeArray[%d][%d]: ", height, width));
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {	
				if(mazeArray[row][col] == 0) {
					System.out.print( " " + " ");
				} else {
					System.out.print( mazeArray[row][col] + " ");
				}
			}
			System.out.println();
		}

		/*
		 * BRICK = 1; GRASS = 2; TREE = 3;
		 */
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (mazeArray[row][col] == 1              // BRICK  
//						|| mazeArray[row][col] == 2       // GRASS
						|| mazeArray[row][col] == 3       // TREE
					
						) {  
					map[row][col] = new Tile(row, col, 1, mazeArray[row][col]);
					map[row][col].setValid(false); //  brick , tree or grass is in tile
				} else {  // character in tile which is able to move
					map[row][col] = new Tile(row, col, 0, mazeArray[row][col]);
					map[row][col].setValid(true); 
				}
			}
		}
	}

	/*
	 *  Adjust the maze setting when levels changed
	 */
	public void configBall(StateBasedGame game, int levels) {
		MinerGame bg = (MinerGame) game;
		System.out.println("levels= " + levels);
		bg.ball.setX(400.0f);
		bg.ball.setY(540.0f);
		switch (levels) {
		case 1:
			bg.ball.scale(1.0f);
			bg.ball.setVelocity(new Vector(.1f, -.12f));			
			break;
		case 2:
			bg.ball.scale(.98f);
			bg.ball.setVelocity(new Vector(-.12f, -.13f));
			break;			
		case 3:
			bg.ball.scale(.95f);
			bg.ball.setVelocity(new Vector(.13f, -.14f));
			break;
		case 4:
			bg.ball.scale(.92f);
			bg.ball.setVelocity(new Vector(-.14f, -.15f));
			break;

		default:
			bg.ball.scale(.92f);
			bg.ball.setVelocity(new Vector(.12f, -.14f));
			break;
		}
	}
	
	/*
	 * Parsing the maze.txt into 2D array
	 */
	private int[][] parseMaze(String filename) {
		final String dir = System.getProperty("user.dir");
		File file = new File(dir + "/" + filename);

		int i = 0;
		try (Scanner scanner = new Scanner(file)) {
			// Open the file, and read it line by line
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				
				char [] list = line.toCharArray();
				mazeArray[i] = new int[list.length];

			    for (int k = 0; k < list.length; k++){
			        mazeArray[i][k] = list[i] - '0';
			    }
				
				i++;
			}
			System.out.println("i= " + i);
			scanner.close();
		} catch (Exception ex) {
			System.out.println("Parsing maze.txt failed! " + ex.toString());
		}
		for (int row = 0; row < mazeArray.length; row++) {
			for (int col = 0; col < mazeArray[row].length; col++) {
				// mazeArray[row][col] = 'T';

				System.out.print(String.format("[%d][%d]= %d ", row, col,
						mazeArray[row][col]));
			}
			System.out.println();
		}
		return mazeArray;
	}

	public void print() {
		print(new ArrayList<Tile>());
	}

	public void print(List<Tile> path) {
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (path.contains(map[row][col])) {
					System.out.print("X ");
				} else {
					map[row][col].print();
				}
			}
			System.out.println();
		}
	}

	public Tile getTile(int row, int col) {
		if (row >= 0 && col >= 0) {
			if (row < width && col < height) {
				return map[row][col];
			}
		}
		return null;
	}

	public List<Tile> getNeighbors(Tile current) {
		List<Tile> lists = new ArrayList<Tile>();
		if (current != null) {

			int x = (int) current.getX();

			int y = (int) current.getY();
			if (x > 0 && x < 12 && y > 0 && y < 12) {
				lists.add(getTile(x - 1, y));
				lists.add(getTile(x + 1, y));
				lists.add(getTile(x, y - 1));
				lists.add(getTile(x, y + 1));
			}
		}
		// System.out.println("lists =" + lists.toString());
		return lists;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
