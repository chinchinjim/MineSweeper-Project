import java.io.*;
import java.util.*;

/**
* This program runs an interactive Minesweeper game.
* Author: Chin Chin
* Prompt: Final Project for ICS3U - Minesweeper
* Date Created: May 31, 2020
* Last Modified: June 15, 2020
* Assumptions:
 ---
*/
class Main {
  public static void main(String[] args) throws IOException{
    new Main();
  }

  /**
  *Controls the flow of the program.
  */
  public Main() throws IOException{
    Scanner reader = new Scanner(System.in);
    
    greetings();
    menu(reader);

    reader.close();
    
  }//end Main method

  /**
  *Prints out a greeting message for the user with some ASCII art.
  */
  public void greetings(){

    System.out.println("                                Welcome to text-based");
    System.out.println(" ___ ___  ____  ____     ___  _____ __    __    ___    ___  ____   ___  ____  ");
    System.out.println("|   |   ||    ||    \\   /  _]/ ___/|  |__|  |  /  _]  /  _]|    \\ /  _]|    \\ ");
    System.out.println("| _   _ | |  | |  _  | /  [_(   \\_ |  |  |  | /  [_  /  [_ |  o  )  [_ |  D  )");
    System.out.println("|  \\_/  | |  | |  |  ||    _]\\__  ||  |  |  ||    _]|    _]|   _/    _]|    / ");
    System.out.println("|   |   | |  | |  |  ||   [_ /  \\ ||  `  '  ||   [_ |   [_ |  | |   [_ |    \\ ");
    System.out.println("|   |   | |  | |  |  ||     |\\    | \\      / |     ||     ||  | |     ||  .  \\");
    System.out.println("|___|___||____||__|__||_____| \\___|  \\_/\\_/  |_____||_____||__| |_____||__|\\_|");
    System.out.println("\n\n");

  }//end greetings

  /**
  *Runs the menu for the user to see game options.
  *@param reader Scanner used to read input
  */
  public void menu(Scanner reader) throws IOException{

    System.out.println("What would you like to do?");
    System.out.println("\nA: Play Game");
    System.out.println("B: See Instructions");
    System.out.println("C: Exit Game");
    System.out.println("\n");

    menuReader(reader);

  }//end menu

  /**
  *Executes menu action based on user's choice.
  *@param reader Scanner used to read input
  */
  public void menuReader(Scanner reader) throws IOException{

    System.out.println("Please enter a letter:");
    String menuChoice = reader.nextLine();
    
    //play option
    if(menuChoice.equalsIgnoreCase("A")){
      play(reader);
    }

    //instructions option
    else if(menuChoice.equalsIgnoreCase("B")){
      instructions(reader);
    }

    //exit option
    else if(menuChoice.equalsIgnoreCase("C")){
      exit();
    }

    //error handling
    else{
      System.out.println("Please enter a valid choice!\n");
      menuReader(reader);     
    }

  }//end menuReader

  /**
  *Displays instructions for the game.
  *@param reader Scanner used to read input
  */
  public void instructions(Scanner reader) throws IOException{

    System.out.println("\n                               INSTRUCTIONS");
    System.out.println("\nThe object of Minesweeper is simple: Choose every box except for the mines.");
    System.out.println("When you select a box, it will uncover a number.");
    System.out.println("This number tells you how many mines surround the box. This goes in the adjacent horizontal, vertical, and diagonal directions all around the box.");
    System.out.println("By knowing the number of surrounding mines, you can figure out through logic which boxes have mines under them.");
    System.out.println("If you're sure a box has a mine, then you can flag it to make sure you don't choose it accidentally, and to give you a visual clue of where all the mines are. You can flag a flagged box to unflag it.");
    System.out.println("If you end up choosing a mine, the game is over. But no worries, you can always play again!");
    System.out.println("To win the game, make sure you uncover all non-mine boxes.");
    System.out.println("Happy Minesweeping!");

    System.out.println("\nPress enter to return to Menu:");

    reader.nextLine();
    clear();
    menu(reader);

  }//end instructions

  /**
  *Exits the game.
  */
  public void exit(){

    System.out.println("\nThank you for playing Minesweeper!");

    System.exit(0);

  }//end exit


  /**
  *Clears the screen.
  */
  public void clear(){

    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

  }//end clear

  /**
  *Plays the game.
  *@param reader Scanner used to read input.
  */
  public void play(Scanner reader) throws IOException{
    
    clear();

    //getting chosen level from user
    String level =levelDisplay(reader);

    //creating reference list for grid sizes
    HashMap<String, Integer> grids = new HashMap<String, Integer>();
    grids.put("beginner", 3);
    grids.put("easy", 5);
    grids.put("medium", 7);
    grids.put("hard", 9);
    grids.put("expert", 10);

    //creating reference list for mine numbers in each level
    HashMap<String, Integer> mines = new HashMap<String, Integer>();
    mines.put("beginner", 1);
    mines.put("easy", 3);
    mines.put("medium", 6);
    mines.put("hard", 14);
    mines.put("expert", 20);

    //custom mine levels
    if(level.equals("custom")){
      level = customLevel(reader);
      mines.put(level, customMines(reader, grids.get(level)));
    }

    //creating 2D array for info on each box
    String[][] gridInfo = new String[grids.get(level)][grids.get(level)];
    for(int k = 0; k<grids.get(level); k++){
      Arrays.fill(gridInfo[k], "blank");
    }

    //creating 2D array for status of each box
    boolean[][] covered = new boolean[grids.get(level)][grids.get(level)];
    for(int k = 0; k<grids.get(level); k++){
      Arrays.fill(covered[k], true);
    }

    //creating 2D array for flags
    boolean[][] flags = new boolean[grids.get(level)][grids.get(level)];
    for(int k = 0; k<grids.get(level); k++){
      Arrays.fill(flags[k], false);
    }

    //generating mines for the grid
    for(int i = 1; i<=mines.get(level); i++){
      int rand1 = mineGenerator(grids.get(level));
      int rand2 = mineGenerator(grids.get(level));
      if(gridInfo[rand1][rand2].equals("mine")){
        i--;
      }
      else{
        gridInfo[rand1][rand2] = "mine";
      }
    }

    //filling gridInfo with surrounding mine number info
    for(int p = 0; p<grids.get(level); p++){
      for(int m = 0; m<grids.get(level); m++){
        if(!gridInfo[p][m].equals("mine"))
        gridInfo[p][m] = String.valueOf(aroundSum(gridInfo, p, m));
      }
    }

    //printing grid for the first time
    gridPrint(grids.get(level), covered, gridInfo, flags);
    
    //user interaction  gameplay
    do{
      //decides to flag or mine
      String boxChoice = boxChoice(reader);
      userPlay(reader, gridInfo, covered, boxChoice, flags);
      //printing
      gridPrint(grids.get(level), covered, gridInfo, flags);
    }while(winCheck(covered, gridInfo)==false);

    //game will exit if lost, so if they make it to the end of the loop, the win method is called
    win(reader, mines, level, grids);

  }//end play

  /**
  *Checks if a player has won.
  *@param covered the 2D array of all covered/uncovered boxes.
  *@param gridInfo the 2D array containing all box info
  *@return whether or not the player has won
  */
  public boolean winCheck (boolean[][] covered, String[][] gridInfo){
     
     //counts up all uncovered none mine boxes and covered mine boxes
     boolean winChecker = false;
     int num = 0;
      for(int k = 0; k<covered.length; k++){
        for(int j = 0; j<covered.length; j++){
          if(gridInfo[k][j].equals("mine")){
            if(covered[k][j]){
              num++;
            }
          }
          else{
            if(covered[k][j] == false){
              num++;
            }
          }
        }
      }
    //should add up to the total number of boxes
    if((covered.length*covered.length)==num) winChecker = true;

    return winChecker;

  }//end winCheck

  /**
  *Runs the user interface for playing the game.
  *@param reader Scanner used to read input
  *@param gridInfo the 2D array containing all box info
  *@param covered the 2D array containing whether each box is covered/uncovered
  *@param boxChoice user's choice to mine or flag a box
  *@param flags the 2D array contianing all flagged box locations
  */
  public void userPlay(Scanner reader, String[][] gridInfo, boolean[][] covered, String boxChoice, boolean[][] flags) throws IOException{

    //getting row and column choices from user
    int row = -1; 
    int col = -1;
    
    //gets valid row and column numbers from player
    do{
      row = rowValid(reader, covered);
      if(row>-1){
        col = colValid(row, reader, covered, gridInfo, flags, boxChoice);
      }
    }while(row<0||col<0);

    //flagging mechanisms
    if(boxChoice.equals("flag")){
      if(flags[row][col]){
        flags[row][col] = false;
      }
      else{
        flags[row][col] = true;
      }
    }
    
    //uncovers the boxes
    if(boxChoice.equals("mine")){
      covered[row][col] = false;
      if(gridInfo[row][col].equals("mine")){
        lose(reader, gridInfo, covered, flags);
      }
      else
      uncover(covered, gridInfo, flags, row, col);
    }

  }//end userPlay
  
  /**
  *Displays winning screen to user.
  *@param reader Scanner used to read input
  *@param mines HashMap with mine number info
  *@param level chosen level by user
  *@param grids Gridsize Hashmap
  */
  public void win(Scanner reader, HashMap mines, String level, HashMap grids)throws IOException{
    
    System.out.println("You have won the game! :D");

    //adds name to highscores file
    hallOfFame(reader, mines, level, grids);

  }//end win

  /**
  *Prompts user for name to add their win to the Hall of Fame file (File I/O)
  *@param reader Scanner used to read input
  *@param mines HashMap contiang mine numbers
  *@param level chosen difficulty by user
  *@param grids gridsizes for all levels
  */
  public void hallOfFame(Scanner reader, HashMap mines, String level, HashMap grids) throws IOException{
    
    System.out.println("Enter your name to add it to the Hall of Fame:");
    String name = reader.nextLine();

    //reading from existing high scores
    File myFile = new File("Hall of Fame");
    Scanner inputFile = new Scanner(myFile);
    ArrayList<String> hallWords = new ArrayList<>();
    while(inputFile.hasNextLine()){
      hallWords.add(inputFile.nextLine());
    }

    inputFile.close();
    
    //adding new win
    hallWords.add(2, name + "                       " + level + "     " + grids.get(level) + "x" + grids.get(level) + "     " + mines.get(level));
    
    //printing out Hall of Fame
    PrintWriter outputFile = new PrintWriter("Hall of Fame");
    for(int k = 0; k<hallWords.size(); k++){
      outputFile.println(hallWords.get(k));
    }

    outputFile.close();

    //returning to menu
    System.out.println("Press enter to return to menu:");
    reader.nextLine();
    clear();
    menu(reader);

  }//end hallOfFame

  /**
  *Displays losing screen to user.
  *@param reader Scanner used to read input
  *@param gridInfo 2D array containing all box info
  *@param covered 2D array containg all covered/uncovered box info
  *@param 2D array containg all flagged box locations
  */
  public void lose(Scanner reader, String[][] gridInfo, boolean[][]covered, boolean[][] flags) throws IOException{
    
    //displays locations of all mines
    for(int k = 0; k<gridInfo.length; k++){
      for(int j = 0; j<gridInfo.length; j++){
        if(gridInfo[k][j].equals("mine")){
          covered[k][j] = false;
        }
      }
    }
    gridPrint(gridInfo.length, covered, gridInfo, flags);

    System.out.println("YOU LOSE! :(");

    //returning to menu
    System.out.println("Press enter to return to menu:");
    reader.nextLine();
    clear();
    menu(reader);

  }//end lose

  /**
  *Uncovers boxes according to 0 number mechanism.
  *@param covered the 2D array containing whether each box is covered/uncovered
  *@param gridInfo the 2D array containing all box info
  *@param flags 2D array contianing all flagged box info
  *@param row The row of the element in question
  *@param col The column of the element in question
  */
  public void uncover(boolean[][] covered, String[][] gridInfo, boolean[][] 
  flags, int row, int col){
    
    //should uncover all adjacent empty boxes (boxes with no numbers, mines, or flags)
    if(gridInfo[row][col].equals("0")){

      for(int k = -1; k<2; k++){
        for(int j = -1; j<2; j++){
          if(isValid(gridInfo, row+k, col+j)){
            if(covered[row+k][col+j]){
              covered[row+k][col+j] = false;
              if(gridInfo[row+k][col+j].equals("0")&&flags[row+k][col+j]==false&&covered[row+k][col+j]){
                uncover(covered, gridInfo, flags, row+k, col+j);
              }
            }
          }
        }
      }


    }

  }//end uncover

  /**
  *Prompts user for valid box column number.
  *@param row The row of the element in question 
  *@param reader Scanner used to read input
  *@param covered the 2D array containing whether each box is covered/uncovered
  *@param gridInfo the 2D array containing all box info
  *@param flags 2D array containing all flagged box info
  *@param boxChoice user's choice to mine or flag a box
  *@return the valid column number
  */
  public int colValid(int row, Scanner reader, boolean[][] covered, String[][] gridInfo, boolean[][] flags, String boxChoice){
     
    //prompting
    System.out.println("\n Which column is the box in that you would like to select?");

    int col = -1;
    
    //checking if input is a number
    String colString = reader.nextLine();
    int len = colString.length();
    int letterChecker = 0;
    for(int i = 0; i<len; i++){
      if ((Character.isDigit(colString.charAt(i)))) letterChecker++;
    }
    //error handling
    if(letterChecker<len){
      System.out.println("That is an invalid column number!");
    }
    //more error handling
    else if(letterChecker==len){
      col = Integer.parseInt(colString)-1;
      if(col<0||col>(gridInfo.length-1)||covered[row][col]==false||       (boxChoice.equals("mine")&&flags[row][col])){
        System.out.println("That is an invalid column number!");
        col = -1;
      }
    }
    
    return col;

  }//end colValid

  /**
  *Prompts user for valid box row number.
  *@param reader Scanner used to read input
  *@param covered 2D array containing all covered/uncovered box info
  *@return the valid row number
  */
  public int rowValid(Scanner reader, boolean[][]covered){
    
    //prompting user
    System.out.println("\n Which row is the box in that you would like to select?");
    int row = 0;
    
    //checking that input is numerical
    String rowString = reader.nextLine();
    int len = rowString.length();
    int letterChecker = 0;
    for(int i = 0; i<len; i++){
      if ((Character.isDigit(rowString.charAt(i)))) letterChecker++;
    }
    //error handling
    if(letterChecker<len){
      System.out.println("That is an invalid row number!");
      row = -1;
    }
    //more error handling
    else if(letterChecker==len){
      row = Integer.parseInt(rowString)-1;
      if(row<0||row>(covered.length-1)){
        System.out.println("That is an invalid row number!");
        row = -1;
      }
    }

    return row;

  }//end rowValid

  /**
  *Prompts user for their choice of play (what they want to do to a box)
  *@param reader Scanner used to read input
  *@return their choice of "flag" or "mine"
  */
  public String boxChoice(Scanner reader){

    System.out.println("Would you like to mine or flag a box?");
    String playChoice = reader.nextLine();
    if(playChoice.equalsIgnoreCase("mine")||playChoice.equalsIgnoreCase("flag")){
    }
    //error handling
    else{
      playChoice = boxChoice(reader);
    }
  
    return playChoice;

  }//end boxChoice

  /**
  *Prints out the grid for the Minesweeper game.
  *@param level The grid size that the player chose
  *@param covered the 2D array containing whether each box is covered/uncovered
  *@param gridInfo the 2D array containing all information about the boxes
  *@param flags the 2D array containing flagged box locations
  */
  public void gridPrint(int size, boolean[][] covered, String[][] gridInfo, boolean[][] flags){
    
    //number formatting along top
    clear();
    System.out.print(" ");
    for(int p = 0; p<size; p++){
      System.out.print(" " + (p+1));
    }
    System.out.print("\n");
    //number formatting along side
    for(int k = 0; k<size; k++){
      if(k+1==10){
        System.out.print((k+1));
      }
      else{
        System.out.print((k+1) + " ");
      }
      for(int j = 0; j<size; j++){
        if(flags[k][j] == true){
          System.out.print("🚩 ");
        }
        else if(covered[k][j]==true){
          System.out.print("⬜ ");
        }
        else{
          if(gridInfo[k][j].equals("mine"))System.out.print("💣 ");
          if(gridInfo[k][j].equals("1"))System.out.print("1️⃣ ");
          if(gridInfo[k][j].equals("2"))System.out.print("2️⃣ ");
          if(gridInfo[k][j].equals("3"))System.out.print("3️⃣ ");
          if(gridInfo[k][j].equals("4"))System.out.print("4️⃣ ");
          if(gridInfo[k][j].equals("5"))System.out.print("5️⃣ ");
          if(gridInfo[k][j].equals("6"))System.out.print("6️⃣ ");
          if(gridInfo[k][j].equals("7"))System.out.print("7️⃣ ");
          if(gridInfo[k][j].equals("8"))System.out.print("8️⃣ ");
          if(gridInfo[k][j].equals("0"))System.out.print("🟦 ");
        }
      }
      System.out.print("\n");
    }
    
  }//end gridPrint
    
  /**
  *Adds the sum of the number of mines surrounding a chosen box.
  *@param arr The array with the grid information
  *@param row The row where the box is located
  *@param col The column where the box is located
  *@return The sum of the mines surrounding the box.
  */
  public int aroundSum(String[][] arr, int row, int col){
    
    int sum = 0;

    //checking if a surrounding box exists, then adding it to the sum if it has a mine
    if(isValid(arr, row +1, col)&&arr[row+1][col].equals("mine")) sum ++;
    if(isValid(arr, row -1, col)&&arr[row-1][col].equals("mine")) sum++;
    if(isValid(arr, row, col +1)&&arr[row][col+1].equals("mine")) sum++;
    if(isValid(arr, row, col -1)&&arr[row][col-1].equals("mine")) sum++;
    if(isValid(arr, row +1, col+1)&&arr[row+1][col+1].equals("mine")) sum++;
    if(isValid(arr, row +1, col-1)&&arr[row+1][col-1].equals("mine")) sum++;
    if(isValid(arr, row -1, col+1)&&arr[row-1][col+1].equals("mine")) sum++;
    if(isValid(arr, row -1, col-1)&&arr[row-1][col-1].equals("mine")) sum++;
    
    return sum;

  }//end aroundSum

  /**
  *Checks if a 2D array's "coordinates" exist/is valid.
  *@param arr The 2D array
  *@param row The row of the element in question
  *@param col The column of the element in question
  *@return if the coordinate is valid/exists
  */
  public boolean isValid(String[][] arr, int row, int col){

  //checking for rows or columns outside the existing range of the array
  if(row<0||col<0||row==arr.length||col==arr[0].length||row>arr.length||col>arr[0].length) return false;
  
  return true;
  
  }//end isValid

  /**
  *Prompts user for different difficulty levels for the Minesweeper game.
  *@param reader Scanner used to read input.
  *@return the selected level of difficulty for the game
  */
  public String levelDisplay(Scanner reader){

    System.out.println("\nGRID SIZES:");
    System.out.println("\nBeginner (3x3 grid x 1 mine)");
    System.out.println("Easy (5x5 grid x 3 mines)");
    System.out.println("Medium (7x7 grid x 6 mines)");
    System.out.println("Hard (9x9 grid x 14 mines");
    System.out.println("Expert (10x10 grid x 20 mines)");
    System.out.println("Custom: _______ x ? mines");

    //checking if user input is valid
    System.out.println("\nPlease choose a difficulty level:");
    String level = reader.nextLine();
    level = level.toLowerCase();

    if(level.equals("easy")||level.equals("medium")||level.equals("beginner")||level.equals("hard")||level.equals("expert")||level.equals("custom")){
    }
    else{
      //error handling
      System.out.println("\n\nThat is not a valid level choice!");
      level = levelDisplay(reader);
    }

    return level;

  }//end levelDisplay

  /**
  *Generates the random placement of the mines.
  *@param length The length of the grid used.
  *@return the row or column where the mine is placed.
  */
  public int mineGenerator(int length){

    int num = (int)(length * Math.random());

    return num;

  }//end mineGenerator

  //under construction
  /**
  *Creates custom number of mines to be played in the game.
  *@param reader Scanner used to read input
  *@param gridSize the grid length/width
  *@return the number of desired mines
  */
  public int customMines(Scanner reader, int gridSize){

    System.out.println("How many mines would you like to have in the game?");
    int num = Integer.parseInt(reader.nextLine());

    if(num>(gridSize*gridSize)||num<1){
      System.out.println("That is not a valid number of mines!");
      num = customMines(reader, gridSize);
    }
   return num;

  }//end customMines

  //under construction
  /**
  *Prompts user for default grid size they would like to be played for their custom game.
  *@param reader Scanner used to read input
  *@return their chosen difficulty level
  */
  public String customLevel(Scanner reader){
    
    System.out.println("\nPlease choose a difficulty level first:");

    String level = reader.nextLine();

    if(!level.equals("easy")&&!level.equals("medium")&&!level.equals("beginner")&&!level.equals("hard")&&!level.equals("expert")){
      System.out.println("That is not a valid level!");
      level = customLevel(reader);
    }
    return level;
    
  }//end customLevel

}//end Main class



