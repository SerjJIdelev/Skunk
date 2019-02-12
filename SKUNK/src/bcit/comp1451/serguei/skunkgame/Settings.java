package bcit.comp1451.serguei.skunkgame;

import java.util.Random;
import java.util.Scanner;

/*
 * THINGS LEFT To DO:
 * -Create end game screen and score display 			DONE!
 * -Check that you can exit from any place in the game  DONE!
 * -Continue Screen Y/N 								DONE!
 * -Implement the 'custom game' feature 				DONE!
 * -check if an array has all the same numbers - 		DONE!
 * -Implement back to main menu feature 				DONE!
 * -Three dice - 										DONE!
 * -Add a delay 										DONE!
 * -Add maximum minimum dice, face, and turn amount. 	DONE!
 * -Add setters? getters?
 * -Add java doc descriptions.
 * -Add slot machine noises to three in a row~~~~ 		DONE!
 * -Add more descriptions
 * -Check for redundancies in the code!
 * 
 */
public class Settings {
	private int numberOfDice;
	private int numberOfTurns;
	private int numberOfFaces;
	
	private boolean endGame = false;
	private boolean cpuSit = false;
	private boolean pSit = false;
	private boolean endRound = false;
	private boolean firstRoll = true;
	private boolean exitSkunk = false;
	
	public final int MIN_DICE = 1;
	public final int MIN_TURNS = 1;
	public final int MIN_FACES = 1;

	public final int MAX_DICE = 50;
	public final int MAX_TURNS = 200;
	public final int MAX_FACES = 200;

	private int pScore = 0;
	private int pTotal = 0;

	private int cpuScore = 0;
	private int cpuTotal = 0;

	private int currentRoundScore = 0;

	Scanner scan = new Scanner(System.in);

	public void mainMenue() {
		int userChoice = 0;
		String userInput = null;
		displayMainMenue();
		while (!endGame && scan.hasNext()) {
			while (!endGame && scan.hasNextInt()) {
				userChoice = scan.nextInt();
				if (userChoice == 1) {//classic skunk
					numberOfDice = 2;
					numberOfTurns = 5;
					numberOfFaces = 6;
					playSkunk(numberOfDice, numberOfTurns, numberOfFaces);

				} else if (userChoice == 2) {
					numberOfDice = 3;//three dice skunk
					numberOfTurns = 5;
					numberOfFaces = 6;
					playSkunk(numberOfDice, numberOfTurns, numberOfFaces);
				} else if (userChoice == 3) {//custom skink
					System.out.println("Good Choice");
					boolean startGame = customSkunk();
					if (startGame) {
						System.out.println();
						playSkunk(numberOfDice, numberOfTurns, numberOfFaces);
					} else {
						displayMainMenue();
						resetScore();
					}
				} else {
					System.out.println(userChoice + " is not a number that is 1, 2 or 3");
				}
			}
			if (!endGame && scan.hasNext()) {
				userInput = scan.next();
				if (userInput.equalsIgnoreCase("EXIT")) {
					endGame = true;
					System.out.println("Thank you floor playing, good NIGHT!");

				} else {
					System.out.println(userInput + " is an invalid Input");
				}
			}

		}
	}

	public void displayMainMenue() {
		System.out.println();
		System.out.println("Welcome to skunk!");
		System.out.println();
		System.out.println("Please choose from the following:");
		System.out.println("1: Type '1' for original SKUNK");
		System.out.println("2: Type '2' for the three dice version ");
		System.out.println("3: Type '3' to change # of dice, # of turns or # of faces");
		System.out.println("4: Type 'EXIT' to exit to TERMINATE THE PROGRAM");

	}

	public void playSkunk(int numberOfDice, int numberOfTurns, int numberOfFaces) {
		for (int round = 1; round <= numberOfTurns; round++) {
			topOfTheRound(round);
			while (!exitSkunk && !endRound) {
				int[] diceRolled = rollDice(numberOfDice, numberOfFaces);// Rolls the Dice
				for (int i = 0; i < numberOfDice; i++) {
					delayTimer(50);
					System.out.println("Die " + (i + 1) + ": " + diceRolled[i]);
				}
				if(allTheSameNumber(diceRolled) && !hasAOne(diceRolled) && numberOfDice > 2) {
					System.out.println("~~~~Slot Mahine Noises~~~~~~~~~Slot Mahine Noises~~~~~~~~~Slot Mahine Noises~~~~~");
				}
				System.out.println("Total: " + sumDice(diceRolled));// displays total

				if ((!pSit && !cpuSit) && hasAOne(diceRolled)) {
					if (!firstRoll) {
						System.out.println("Doesnt Count- EVERYONE IS STANDING LOLOLO");
						playerAndCpuStandingChoice();
					} else {
						System.out.println();
						System.out.println("Re-Rolled");
					}

				} else {
					firstRoll = false;
					currentRoundScore += sumDice(diceRolled);// if does'nt have a 1, round score gets summed
					System.out.println("Current Round Score: " + currentRoundScore);
					if ((pSit && !cpuSit) && hasAOne(diceRolled)) {
						cpuRollsAOne(diceRolled);
					} else if ((cpuSit && !pSit) && hasAOne(diceRolled)) {
						playerRollsAOne(diceRolled);
					} else {
						playerAndCpuStandingChoice();
					}
					if (pSit && cpuSit) {// ALL SITTING -> end round
						endRound = true;
					}
				}
			}
			if (exitSkunk) {
				round = numberOfTurns;
				displayMainMenue();
				resetScore();
			} else if (endRound) {
				bottomOfTheRound();
			}
		}
		if (!exitSkunk) {
			endGameScreen();
		}
		exitSkunk = false;
	}

	public boolean allTheSameNumber(int[] diceRolled) {
		boolean theSame = true;// lets assume all of them are the same
		int x = diceRolled[0];//let x = the first number of the array
		for (int i = 1; i < numberOfDice && theSame; i++) {//lets loop and break out if one is not the same
			if (diceRolled[i] != x) {
				theSame = false;// if its not the same
			} else {
				theSame = true;//if it is
			}
		}
		return theSame;
	}

	public boolean customSkunk() {
		String userInput = null;
		boolean startGame = false;
		boolean back = false;
		System.out.println("Create a Custom game!!! type 'BACK' if you want to go back to the main menue");
		System.out.println();
		System.out.print("First, the number of dice you want to play with " + MIN_DICE + " to " + MAX_DICE + " : ");
		while (!back && !startGame && scan.hasNext()) {
			if (scan.hasNextInt()) {
				back = customDice();
				if (!back) {
					System.out.print("Now the amount of faces each die will have " + 
									  MIN_FACES + " to " + MAX_FACES + " : ");
					back = customFaces();
				}
				if (!back) {
					System.out.print("And Finally the amount of turns you will play for " + 
									  MIN_TURNS + " to " + MAX_TURNS + " : ");
					back = customTurns();
					if (!back) {
						startGame = true;
					}
				}
			} else {
				userInput = scan.next();
				if (userInput.equalsIgnoreCase("BACK")) {
					back = true;
				} else {
					System.out.println(userInput + " is not a valid Input");
				}

			}
		}
		return startGame;
	}

	public boolean customTurns() {
		boolean back = false;
		String userInput = null;
		boolean userChosen = false;
		while (!userChosen && !back && scan.hasNext()) {

			while (!userChosen && scan.hasNextInt()) {
				int userSetting = scan.nextInt();
				if (userSetting < MIN_TURNS || userSetting > MAX_TURNS) {
					System.out.println(userSetting + " is and invalid choice. " + MIN_TURNS + " to " + MAX_TURNS);
				} else {
					numberOfTurns = userSetting;
					userChosen = true;
				}
			}
			if (!userChosen) {
				userInput = scan.next();
				if (userInput.equalsIgnoreCase("BACK")) {
					back = true;
				} else {
					System.out.println(userInput + " is not a valid Input");
				}
			}
		}
		return back;
	}

	public boolean customFaces() {
		boolean back = false;
		String userInput = null;
		boolean userChosen = false;
		while (!userChosen && !back && scan.hasNext()) {

			while (!userChosen && scan.hasNextInt()) {
				int userSetting = scan.nextInt();
				if (userSetting < MIN_FACES || userSetting > MAX_FACES) {
					System.out.println(userSetting + " is and invalid choice. " + MIN_FACES + " to " + MAX_FACES);
				} else {
					numberOfFaces = userSetting;
					userChosen = true;
				}
			}
			if (!userChosen) {
				userInput = scan.next();
				if (userInput.equalsIgnoreCase("BACK")) {
					back = true;
				} else {
					System.out.println(userInput + " is not a valid Input");
				}
			}
		}
		return back;

	}

	public boolean customDice() {
		boolean back = false;
		String userInput = null;
		boolean userChosen = false;
		while (!userChosen && !back && scan.hasNext()) {
			while (!userChosen && scan.hasNextInt()) {
				int userSetting = scan.nextInt();
				if (userSetting < MIN_DICE || userSetting > MAX_DICE) {
					System.out.println(userSetting + " is and invalid choice. " + MIN_DICE + " to " + MAX_DICE + ".");
				} else {
					numberOfDice = userSetting;
					userChosen = true;
				}
			}
			if (!userChosen) {
				userInput = scan.next();
				if (userInput.equalsIgnoreCase("BACK")) {
					back = true;
				} else {
					System.out.println(userInput + " is not a valid Input");
				}
			}
		}
		return back;
	}

	public void playerAndCpuStandingChoice() {
		if (!cpuSit) {// CPU score gets added and given the choice to sit
			delayTimer(200);
			cpuSit = cpuSitStand();
			cpuScore = currentRoundScore;
		} else {
			System.out.println("CPU SITTING");
		}
		if (!pSit) {// player score gets added and given the choice to sit

			pSit = playerTurn();
			pScore = currentRoundScore;
		} else {
			System.out.println("PLAYER SITTING");
		}

	}

	public void topOfTheRound(int round) {
		if (round == 1) {
			System.out.println("Chosen parameters.");
			System.out.println("Number of Dice:  " + numberOfDice);
			System.out.println("Number of Turns: " + numberOfTurns);
			System.out.println("Number of Faces: " + numberOfFaces);
			delayTimer(500);
			System.out.println();
			System.out.println("Commands are as follows:");
			System.out.println();
			delayTimer(400);
			System.out.println("SIT:   you END your turn and lock in you SCORE.");
			delayTimer(400);
			System.out.println("STAND: you STAND while the dice are rolled, your fate, gambled.");
			delayTimer(400);
			System.out.println("EXIT:  you can run to the main menu, but there is no escape.");
			delayTimer(400);
			System.out.println("*********************************");
			System.out.println("*            HUMAN              *");
			System.out.println("*              VS               *");
			System.out.println("*    JAVA.RANDOM.UTIL PACKAGE   *");
			System.out.println("*********************************");
			System.out.println();
			System.out.println("NEW GAME! ROUND " + round + " --- FIGHT!!!          ");
		} else {
			System.out.println();
			System.out.println("NEXT ROUND: ROUND " + round + "!");
		}
		System.out.println("player = " + pTotal + " CPU = " + cpuTotal);
		firstRoll = true;
	}

	public void bottomOfTheRound() {
		pSit = false;
		cpuSit = false;
		currentRoundScore = 0;
		pTotal += pScore;
		cpuTotal += cpuScore;
		endRound = false;
	}

	public void cpuRollsAOne(int[] diceRolled) {
		if (sumDice(diceRolled) == numberOfDice) {// Snake Eyes
			cpuTotal = 0;// removes points
			cpuScore = 0;// removes points
		} else {
			cpuScore = 0;// just a single 1
		}
		cpuSit = true;// CPU forced to SITTING, player already SITTING
		System.out.println("OOOOOOPS!!!! THE CPU BOUGHT PEACH RINGS");
		currentRoundScore = 0;
	}

	public void playerRollsAOne(int[] diceRolled) {
		if (sumDice(diceRolled) == numberOfDice) {// snake Eyes
			pScore = 0;// removes points
			pTotal = 0;// removes points
		} else {
			pScore = 0;// just a single 1
		}
		pSit = true;// Player forced to SITTING, CPU already SITTING
		System.out.println("OOOOOOPS!!!! YOU BOUGHT PEACH RINGS");
		currentRoundScore = 0;
	}

	public boolean hasAOne(int[] diceRolled) {
		boolean hasAOne = false;
		for (int value : diceRolled) {
			if (value == 1) {
				hasAOne = true;
			}
		}
		return hasAOne;
	}

	public int sumDice(int[] diceRolled) {
		int sum = 0;
		if (allTheSameNumber(diceRolled) == true && numberOfDice > 2 && !hasAOne(diceRolled)) {
			Double multiplier = new Double(numberOfDice - 1);//
			sum = (int) Math.pow(10.0, (multiplier));//multiplier for any game that more than three dice.

		} else {
			for (int value : diceRolled) {
				sum += value;
			}
		}
		return sum;
	}

	public boolean playerTurn() {
		String userInput = null;// initialize userInput string
		boolean correctInput = false;// This checks for correct input
		System.out.println();
		System.out.println("Sit, Stand, or EXIT?");// asks for input
		while (correctInput == false) {
			userInput = scan.next();// scan for user input
			if (userInput.equalsIgnoreCase("Sit")) {
				pSit = true;
				correctInput = true;
			} else if (userInput.equalsIgnoreCase("Stand")) {
				pSit = false;
				correctInput = true;
			} else if (userInput.equalsIgnoreCase("Exit")) {
				exitSkunk = true;
				correctInput = true;
			} else {
				System.out.println(userInput + " is not a valid input.");
			}
		}
		return pSit;
	}

	public int[] rollDice(int numberOfDice, int numberOfFaces) {
		Random r = new Random();
		int[] rollArray = new int[numberOfDice];// this creates an array of the # of dice
		for (int i = 0; i < numberOfDice; i++) {
			int roll = r.nextInt(numberOfFaces) + 1;
			rollArray[i] = roll;
		}
		return rollArray;
	}

	public boolean cpuSitStand() {
		Random r = new Random();
		boolean cpuSit = false;
		cpuSit = r.nextBoolean();//Generates a random boolean
		if (cpuSit) {
			System.out.println();
			System.out.println("CPU decided to SIT with " + currentRoundScore + " points. "
					+ (cpuTotal + currentRoundScore) + " Total.");
		} else if (!cpuSit) {
			System.out.println();
			System.out.println("CPU decided to STAND");
		} else {
			System.out.println("Error, CPU CANT DECIDE");//should'nt get here
		}
		return cpuSit;
	}

	public void endGameScreen() {
		boolean exit = false;
		if (cpuTotal < pTotal) {
			winnerScreen();
		} else if (cpuTotal > pTotal) {
			loserScreen();
		} else {
			tieScreen();
		}
		while (!exit && scan.hasNext()) {
			String userInput = scan.next();
			if (userInput.equalsIgnoreCase("Y")) {
				resetScore();
				playSkunk(numberOfDice, numberOfTurns, numberOfFaces);
			} else if (userInput.equalsIgnoreCase("exit")) {
				exit = true;
				displayMainMenue();
				resetScore();
			} else {
				System.out.println(userInput + " Is not a Valid Input.");
			}
		}
	}

	public void resetScore() {
		pScore = 0;
		pTotal = 0;

		cpuScore = 0;
		cpuTotal = 0;

		currentRoundScore = 0;
	}

	public void winnerScreen() {
		System.out.println(" W                W  WWWWWWWWWWWWWW       NN      N    A       AAAAA       ");
		System.out.println("  W              W         W              N N     N    AA      A    A       ");
		System.out.println("   W             W         W              N  N    N   A  A     A    A    ");
		System.out.println("   W             W         W              N   N   N  A    A    A   A     ");
		System.out.println("    W     W     W          W              N    N  N  AAAAA A   A AA        ");
		System.out.println("     W   W  W   W          W              N     N N  A      A  A   A      ");
		System.out.println("      W W    W W           W              N      NN  A      A  A    A      ");
		System.out.println("       W      W      WWWWWWWWWWWWWW       N       N  A      A  A     A   ");
		System.out.println("You got " + pTotal + " points!!!! and the evel computer got " + cpuTotal + ".");
		System.out.println("well DONE!!! would you like to play again? Y for yes, 'exit' for main menu");

	}

	public void loserScreen() {
		System.out.println("L        LLLLLLLL    LLLLLLLL  LLLLLLLLLLLLL  RRRRRRR          ");
		System.out.println("L        L     LL    L         L              R      R          ");
		System.out.println("L        L      L    L         L              R     R           ");
		System.out.println("L        L      L    L         L              RRRRRRR            ");
		System.out.println("L        L      L    LLLLLLLL  LLLLLLL        R     R           ");
		System.out.println("L        L      L           L  L              R      R          ");
		System.out.println("L        L      L           L  L              R       R      ");
		System.out.println("LLLLLLLL LLLLLL L    LLLLLLL   LLLLLLLLLLL    R        R     ");
		System.out.println("HAHAHA SILLY HUMAN, you cannot best a computar! with your meesly " + pTotal
				+ " points against my " + cpuTotal + " points!!");
		System.out.println("would you like to lose again? Y for yes, 'exit' for main menue");
	}

	public void tieScreen() {
		System.out.println("    TIE                                          TIE        ");
		System.out.println("          YOU                         YOU TIED              ");
		System.out.println("        BECAUSE YOU TIED       TIE                         ");
		System.out.println("           TIE               YOU DIDNT WIN TIE       TIE    ");
		System.out.println("  TIE                TIED            TIED                       ");
		System.out.println("Would you like to battle witts aginst the random utility package?");
		System.out.println("y for yes, and 'exit'for the main menue");
	}

	public void delayTimer(int milliseconds) {
		try {
			//To remove delay activate code bellow
			//milliseconds = 0;
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setNumberOfDice(int numberOfDice) {
		if (numberOfDice <= MIN_DICE || numberOfDice >= MAX_DICE) {
			System.err.println(numberOfDice + " amount of dice is out of range!");
		} else {
			this.numberOfDice = numberOfDice;
		}
	}

	public void setNumberOfFaces(int numberOfFaces) {
		if (numberOfFaces <= MIN_FACES || numberOfFaces >= MAX_FACES) {
			System.err.println(numberOfFaces + " amount of faces is out of range!");
		} else
			this.numberOfFaces = numberOfFaces;
	}

	public void setNumberOfTurns(int numberOfTurns) {
		if (numberOfTurns <= MIN_TURNS || numberOfTurns >= MAX_TURNS) {
			System.err.println(numberOfTurns + "amount of turns is out of range!");
		} else
			this.numberOfTurns = numberOfTurns;
	}

	public int getNumberOfDice() {
		return numberOfDice;
	}

	public int getNumberOfFaces() {
		return numberOfFaces;
	}

	public int getNumberOfTurns() {
		return numberOfTurns;
	}
}