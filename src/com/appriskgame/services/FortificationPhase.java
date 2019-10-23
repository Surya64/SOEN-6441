package com.appriskgame.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appriskgame.model.Country;
import com.appriskgame.model.GameMap;
import com.appriskgame.model.GamePlayer;

/**
 * This class is for the fortification phase of the game. It checks the number
 * of countries user has proceeds if it is not less than 2. Takes the
 * fromCountry and toCountry value which identifies from where the player wishes
 * to move army and where. After performing requisite validation moves army of
 * the player.
 *
 * @author Dolly
 */
public class FortificationPhase {
	public boolean doFortification = false;
	public static List<String> playersCommandList = new ArrayList<String>();
	String strUser;
	String strfromCountry;
	String strtoCountry;
	String countryNumToPlace;

	/**
	 * This method is called from the Startup phase when the user opts to start the
	 * fortification. It internally calls the moveArmies method once all the
	 * validation with respect to fortification are performed.
	 *
	 * @param player  - The player who is doing fortification.
	 * @param gameMap - GameMap object
	 * @throws IOException - throws Input-Output exception
	 */
	public void startGameFortification(GamePlayer player, GameMap gameMap) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int countOfArmies = 0;
		if (player.getPlayerCountries().size() >= 2) {
			doFortification = true;
			boolean doFortificationNone = false;
			while (doFortification && !doFortificationNone) {
				doFortification = true;
				doFortificationNone = false;
				Country givingCountry = null;
				Country receivingCountry = null;
				do {
					doFortification = false;
					doFortificationNone = false;
					System.out.println("\nPlayer has the following list of countries with armies: \n");
					for (Country country : player.getPlayerCountries()) {
						System.out.println("* " + country.getCountryName() + ":" + country.getNoOfArmies() + "\n");
					}
					System.out.println("Enter the Command for fortification");
					strUser = br.readLine().trim().toUpperCase();
					playersCommandList = Arrays.asList(strUser.split(" "));
					if (playersCommandList.size() == 2) {
						playersCommandList = Arrays.asList(strUser.split(" "));
						String none = playersCommandList.get(1);
						String cmd = playersCommandList.get(0);
						if (none.equalsIgnoreCase("none") && cmd.equalsIgnoreCase("fortify")) {
							System.out.println("No Move in Forification Phase");
							doFortificationNone = true;
							doFortification = true;
						} else {
							System.out.println("Please enter the right format like : fortify none");
						}
					}

					if (!doFortification) {
						if (!(playersCommandList.size() == 4)) {
							System.out
									.println("Please enter the right format like : fortify fromcountry tocountry num");
							strUser = br.readLine().trim();
							playersCommandList = Arrays.asList(strUser.split(" "));
						}
						strfromCountry = playersCommandList.get(1);
						strtoCountry = playersCommandList.get(2);
						countryNumToPlace = playersCommandList.get(3);
						Pattern cmdPattern = Pattern.compile("fortify");
						Matcher cmdMatch = cmdPattern.matcher(playersCommandList.get(0));
						Pattern namePattern = Pattern.compile("[a-zA-Z-_]+");
						Matcher matchFromCountry = namePattern.matcher(strfromCountry);
						Matcher matchToCountry = namePattern.matcher(strtoCountry);
						Pattern numberPattern = Pattern.compile("[0-9]+");
						Matcher match1 = numberPattern.matcher(countryNumToPlace);
						while (!matchFromCountry.matches() || strfromCountry.isEmpty() || !matchToCountry.matches()
								|| strtoCountry.isEmpty() || !match1.matches() || countryNumToPlace.isEmpty() || !cmdMatch.matches()) {
							if (!matchFromCountry.matches() || strfromCountry.isEmpty()) {
								System.out.println("\\nInCorrect fromcountry name, please enter the command again:");
								strUser = br.readLine().trim().toUpperCase();
								playersCommandList = Arrays.asList(strUser.split(" "));
								strfromCountry = playersCommandList.get(1);
								strtoCountry = playersCommandList.get(2);
								countryNumToPlace = playersCommandList.get(3);
							}
							if (!matchToCountry.matches() || strtoCountry.isEmpty()) {
								System.out.println("\nInCorrect tocountry name, please enter the command again:");
								strUser = br.readLine().trim().toUpperCase();
								playersCommandList = Arrays.asList(strUser.split(" "));
								strfromCountry = playersCommandList.get(1);
								strtoCountry = playersCommandList.get(2);
								countryNumToPlace = playersCommandList.get(3);
							}
							if (!match1.matches() || countryNumToPlace.isEmpty()) {
								System.out.println("\nInCorrect Army Count, please enter the command again:");
								strUser = br.readLine().trim().toUpperCase();
								playersCommandList = Arrays.asList(strUser.split(" "));
								strfromCountry = playersCommandList.get(1);
								strtoCountry = playersCommandList.get(2);
								countryNumToPlace = playersCommandList.get(3);
								match1 = numberPattern.matcher(countryNumToPlace);
							}
							if (!cmdMatch.matches() || playersCommandList.get(0).isEmpty()) {
								System.out.println("\nInCorrect Command, please enter the command again:");
								strUser = br.readLine().trim().toUpperCase();
								playersCommandList = Arrays.asList(strUser.split(" "));
								strfromCountry = playersCommandList.get(1);
								strtoCountry = playersCommandList.get(2);
								countryNumToPlace = playersCommandList.get(3);
								cmdMatch = cmdPattern.matcher(playersCommandList.get(0));
							}
						}
						for (Country country : player.getPlayerCountries()) {
							if (country.getCountryName().equalsIgnoreCase(strfromCountry)) {
								givingCountry = country;
							}
						}
						for (Country country : player.getPlayerCountries()) {
							if (country.getCountryName().equalsIgnoreCase(strtoCountry)) {
								receivingCountry = country;
							}
						}
						if (player.getPlayerCountries().contains(givingCountry)
								&& player.getPlayerCountries().contains(receivingCountry)) {
							doFortification = false;
						} else {
							System.out.println(
									"Entered countries doesn't exist in player's owned country list, please enter country names again\n");
							doFortification = true;
						}
					}
				} while (doFortification && !doFortificationNone);
				if (!doFortification) {
					countOfArmies = Integer.parseInt(countryNumToPlace);
					if (countOfArmies >= givingCountry.getNoOfArmies()) {
						System.out.println(
								"Insufficient armies available, fortification is not possible with asked number of armies.");
						doFortification = true;
					}
					if (givingCountry.getNoOfArmies() == 1) {
						System.out.println("Insufficient armies available, " + givingCountry.getCountryName()
								+ " should have more than 1 army to Move");
						doFortification = true;
					}
					if (countOfArmies < 0) {
						System.out.println("Army count should be a positive number");
						doFortification = true;
					}
				}
				if (!doFortification) {
					boolean fortify = false;
					for (Country country : player.getPlayerCountries()) {
						for (String temp : country.getNeighbourCountries()) {
							if (temp.equalsIgnoreCase(strfromCountry) || temp.equalsIgnoreCase(strtoCountry)) {
								fortify = true;
							}
						}
					}
					if (fortify) {
						moveArmies(givingCountry, receivingCountry, countOfArmies);
					} else {
						doFortification = true;
						System.out
								.println("None of the players' countries are adjacent\n Fortification phase ends..!!");
					}
				}

			}

		} else {
			System.out.println("Sorry, Fortification is not possible if the country owned is less than 2");
		}
	}

	/**
	 * This method takes the values for each player from the startFortification
	 * method and does the manipulation of armies and assign the armies
	 *
	 * @param fromCountry - The country from where player want to move army
	 * @param toCountry   - The country to where player want to move army
	 * @param armiesCount - Count of armies player wish to move
	 */
	public void moveArmies(Country fromCountry, Country toCountry, int armiesCount) {

		boolean neighbourCountries = false;
		for (String country : fromCountry.getNeighbourCountries()) {
			if (country.equalsIgnoreCase(toCountry.getCountryName())) {
				int fromCountryArmy = fromCountry.getNoOfArmies();
				int toCountryArmy = toCountry.getNoOfArmies();
				fromCountry.setNoOfArmies(fromCountryArmy - armiesCount);
				toCountry.setNoOfArmies(toCountryArmy + armiesCount);
				neighbourCountries = true;
				doFortification = false;
				System.out.println("\nArmies successfully moved!");
				System.out.println("\nFortification phase ends!");
				break;
			}
		}
		if (!neighbourCountries) {
			System.out.println("Countries are not adjacent!");
			doFortification = true;
		}
	}
}
