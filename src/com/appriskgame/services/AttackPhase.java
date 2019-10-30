package com.appriskgame.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.appriskgame.model.Continent;
import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;
import com.appriskgame.model.Dice;

public class AttackPhase {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	public void attackPhaseControl(GamePlayer player, GameMap mapDetails) throws IOException {

		boolean gameContinue;
		do {
			gameContinue = false;
			showMap(mapDetails);
			System.out.println("Enter the Attacker command?");
			Scanner sc = new Scanner(System.in);
			String userCommand = sc.nextLine();
			String[] attackDetails = userCommand.split(" ");
			String attackCountry = attackDetails[1];
			String defenderCountry = attackDetails[2];
			String decision = attackDetails[3];
			decision = decision.substring(1, decision.length());
			boolean attackerCountryPresent = isCountryPresent(attackCountry, mapDetails);
			boolean defenderCountryPresent = isCountryPresent(defenderCountry, mapDetails);
			Country attackCountryObject = null;
			Country defenderCountryObject = null;

			if (attackerCountryPresent && defenderCountryPresent) {
				attackCountryObject = getCountryObject(attackCountry, mapDetails);
				defenderCountryObject = getCountryObject(defenderCountry, mapDetails);
			}

			if (!attackerCountryPresent) {
				System.out.println(attackCountry + "This is not in map");
			}

			if (!defenderCountryPresent) {
				System.out.println(defenderCountry + "This is not in map");
			}

			boolean isAttackAndDefenderAdajacent = isCountryAdjacent(attackCountryObject, defenderCountry, mapDetails);

			if (decision.equalsIgnoreCase("allout")) {
				if (isAttackAndDefenderAdajacent == true) {

					while (attackCountryObject.getNoOfArmies() > 1 && defenderCountryObject.getNoOfArmies() != 0) {
						int attackerDices = maxAllowableAttackerDice(attackCountryObject.getNoOfArmies());
						int defenderDices = maxAllowableDefenderDice(defenderCountryObject.getNoOfArmies());

						if (attackerDices > 0 && defenderDices > 0) {
							attackingStarted(attackerDices, defenderDices, attackCountryObject, defenderCountryObject);
							if (isAttackerWon(defenderCountryObject)) {
								moveArmyToConquredCountry(attackCountryObject, defenderCountryObject);
							}
						}

					}
				}

			} else if (decision.equalsIgnoreCase("noattack")) {

			} else {
				if (isAttackAndDefenderAdajacent == true) {

					int attackerDices = Integer.parseInt(attackDetails[3]);
					int attackerArmies = attackCountryObject.getNoOfArmies();
					if (isAttackerDicePossible(attackerArmies, attackerDices)) {
						System.out.println("Enter the Defender command?");
						Scanner sc1 = new Scanner(System.in);
						String defenderUserCommand = sc.nextLine();
						String[] defenderDetails = defenderUserCommand.split(" ");
						int defenderDices = Integer.parseInt(defenderDetails[1]);
						int defenderArmies = defenderCountryObject.getNoOfArmies();
						if (isDefenderDicePossible(defenderArmies, defenderDices)) {
							attackingStarted(attackerDices, defenderDices, attackCountryObject, defenderCountryObject);

							if (isAttackerWon(defenderCountryObject)) {
								
								moveArmyToConquredCountry(attackCountryObject, defenderCountryObject);
							}

						} else {
							reasonForFailedDefender(defenderArmies, defenderDices);
						}

					} else {
						reasonForFailedAttack(attackerArmies, attackerDices);

					}
				}

				else {
					System.out.println(attackCountry + "Not adjacent to" + defenderCountry);

				}
			}
			System.out.println("Do you want to attack again? Yes/No");
			String continueAttacking = br.readLine().trim();
			while (!(continueAttacking.equalsIgnoreCase("Yes") || continueAttacking.equalsIgnoreCase("No")
					|| continueAttacking == null)) {
				System.err.println("\nPlease enter the choice as either Yes or No:");
				String choice = br.readLine().trim();
			}
			if (continueAttacking.equalsIgnoreCase("Yes")) {
				gameContinue = true;
			} else {
				gameContinue = false;
				System.out.println("Thank You!!");
				System.exit(0);
			}

		} while (gameContinue);
	}
	public int minimumBattles(int attackerDices, int defenderDices) {
		if (attackerDices < defenderDices) {
			return attackerDices;
		} else if (attackerDices > defenderDices) {
			return defenderDices;
		} else {
			return defenderDices;
		}
	}
		public boolean isCountryPresent(String currentCountry, GameMap mapDetails) {
			for (int i = 0; i < mapDetails.getCountries().size(); i++) {
				if (mapDetails.getCountries().get(i).getCountryName().toString().equalsIgnoreCase(currentCountry)) {
					return true;
				}

			}
			return false;
		}

		public boolean isCountryAdjacent(Country attackCountryObject, String defenderCountry, GameMap mapDetails) {

			ArrayList<String> neighbourCountires = attackCountryObject.getNeighbourCountries();
			for (int i = 0; i < neighbourCountires.size(); i++) {
				if (neighbourCountires.get(i).equalsIgnoreCase(defenderCountry)) {
					return true;
				}
			}
			return false;
		}
		
		
		public boolean isDefenderDicePossible(int DefenderArmies, int defenderDices) {
			if (defenderDices <= 2 && defenderDices <= DefenderArmies) {
				return true;
			}
			return false;
		}
		
		
		public boolean isAttackerDicePossible(int AttackerArmies, int attackDices) {

			if (attackDices <= 3 && attackDices <= AttackerArmies - 1) {
				return true;
			}
			return false;
		}

		public boolean ableToMoveArmy(Country attackCountryObject, int moveNumberOfArmies) {

			if (moveNumberOfArmies <= 0) {
				return false;
			} else if (attackCountryObject.getNoOfArmies() - 1 >= moveNumberOfArmies) {
				return true;
			}
			return false;
		}
		
		
		public boolean isAttackerWon(Country defenderCountryObject) {
			if (defenderCountryObject.getNoOfArmies() == 0) {
				return true;
			}
			return false;
		}
		
	
		public int maxAllowableAttackerDice(int attackerArmies) {

			if (attackerArmies >= 3) {
				return 3;
			} else {
				return attackerArmies - 1;
			}
		}

		public int maxAllowableDefenderDice(int DefenderArmies) {

			if (DefenderArmies >= 2) {
				return 2;
			} else {
				return DefenderArmies;
			}
		}

		public List<Integer> outComesOfDices(int noOfDices) {

			List<Integer> outComes = new ArrayList<Integer>();

			for (int i = 0; i < noOfDices; i++) {
				Random random = new Random();
				int result = random.nextInt(5) + 1;
				outComes.add(result);

			}
			return outComes;

		}
		public void attackingStarted(int attackerDices, int defenderDices, Country attackCountryObject,
				Country defenderCountryObject) {
			List<Integer> attackerOutcomes = outComesOfDices(attackerDices);
			List<Integer> defenderOutcomes = outComesOfDices(defenderDices);

			Collections.sort(attackerOutcomes);
			Collections.reverse(attackerOutcomes);

			Collections.sort(defenderOutcomes);
			Collections.reverse(defenderOutcomes);

			int battles = minimumBattles(attackerDices, defenderDices);

			for (int i = 0; i < battles; i++) {
				System.out.println("Battle :" + i);

				System.out.println("Attacker value is: " + attackerOutcomes.get(i));
				System.out.println("Defender value is: " + defenderOutcomes.get(i));
				if (attackerOutcomes.get(i) > defenderOutcomes.get(i)) {
					System.out.println("Attacker won the battle");
					defenderCountryObject.setNoOfArmies(defenderCountryObject.getNoOfArmies() - 1);

				} else {
					System.out.println("Defender won the battle");
					attackCountryObject.setNoOfArmies(attackCountryObject.getNoOfArmies() - 1);
				}
			}

		}

		public void moveArmyToConquredCountry(Country attackCountryObject, Country defenderCountryObject)
				throws IOException {
			String choice = "";
			do {
				try {
					System.out.println("Enter the Attack Move command?");
					Scanner sc = new Scanner(System.in);
					String userCommand = sc.nextLine();
					String[] attackMoveDetails = userCommand.split(" ");
					int moveNumberOfArmies = Integer.parseInt(attackMoveDetails[1]);
					if(	checkUserAttackMoveValidation(userCommand)&&ableToMoveArmy(attackCountryObject, moveNumberOfArmies))
					{
							defenderCountryObject.setPlayer(attackCountryObject.getPlayer());
							defenderCountryObject.setNoOfArmies(moveNumberOfArmies);
							attackCountryObject.setNoOfArmies(attackCountryObject.getNoOfArmies() - moveNumberOfArmies);
							choice = "";
					}
					else
					{
						System.out.println("It is not possible to move" + moveNumberOfArmies);
						BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
						System.out.println("Do you want to still move the Army? Yes/No");
						choice = br.readLine().trim();
					}
					
				}
				catch(Exception ex){
					System.out.println("Please enter the Attack Move Command in the below correct Format\n"
							+ "Format :attackmove num[num>0]\n");
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					System.out.println("Do you want to still move the Army? Yes/No[Incase of 'No' By default 1 Army will be moved to the conqured Country");
					choice = br.readLine().trim();
				}
				
				if(choice.equalsIgnoreCase("No"))
				{
					defenderCountryObject.setPlayer(attackCountryObject.getPlayer());
					defenderCountryObject.setNoOfArmies(1);
					attackCountryObject.setNoOfArmies(attackCountryObject.getNoOfArmies() - 1);
					choice = "";
				}
				
			} while (choice.equalsIgnoreCase("Yes"));

		}

		public boolean isAllOut(Country attackCountryObject, Country defenderCountryObject) {
			return true;
		}

		
		public void showMap(GameMap gameMap) {
			ArrayList<Country> print = gameMap.getCountries();
			for (Country country : print) {

				System.out.println(country);
			}
		}

	
		public boolean checkUserAttackMoveValidation(String userCommand) {
			
			return true;
		}
		
		public Country getCountryObject(String currentCountry, GameMap mapDetails) {
			Country attackCountryObject = null;
			for (int i = 0; i < mapDetails.getCountries().size(); i++) {
				if (mapDetails.getCountries().get(i).getCountryName().toString().equalsIgnoreCase(currentCountry)) {
					attackCountryObject = mapDetails.getCountries().get(i);
				}

			}
			return attackCountryObject;
		}
		
		public void reasonForFailedDefender(int defenderArmies, int defenderDices) {
			if (defenderArmies == 0) {
				System.out.println("Defending is not possible.As the Defending Country has 0 Army");
			} else if (defenderArmies > 2) {
				System.out.println("Defending Army should be less than or equal to 2");
			} else {

				System.out.println(defenderArmies + "Defending Armies should be more than or equal to Defending Dices"
						+ defenderDices);
			}
		}
		
		public void reasonForFailedAttack(int attackerArmies, int attackerDices) {
			if (attackerArmies == 1) {
				System.out.println("Attacking is not possible.As the Attacking Country has only 1 Army");
			} else if (attackerDices > 3) {
				System.out.println("Attacking Dice cannot be more than 3");
			} else {

				System.out.println(attackerArmies - 1 + "Attacking Armies should be more than or equal to Attacking Dices"
						+ attackerDices);
			}
		}
	
	}

	