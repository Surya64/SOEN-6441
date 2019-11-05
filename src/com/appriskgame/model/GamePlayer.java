package com.appriskgame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.appriskgame.view.CardView;

/**
 * This class stores the value associated to each player. It stores player's
 * name of String type, total armyCount of player as Integer type, and the
 * ArrayList of type Country which the player owns.
 *
 * @author Dolly
 * @author Sahana
 */
public class GamePlayer extends Observable {

	private String playerName;
	private ArrayList<Country> playerCountries = new ArrayList<Country>();
	private int noOfArmies = 0;
	private List<Card> cardList = new ArrayList<>();
	public GamePlayer current;

	/**
	 * This method gets the card list
	 * 
	 * @return List of cards
	 */
	public List<Card> getCardList() {
		return cardList;
	}

	/**
	 * This method sets the card list
	 * 
	 * @param cardList List of cards
	 */
	public void setCardList(List<Card> cardList) {
		this.cardList = cardList;
	}

	/**
	 * It is default constructor.
	 */
	public GamePlayer() {
		CardView cardView = new CardView();
		this.addObserver(cardView);
	}

	/**
	 * Get the Player name.
	 *
	 * @return Name of the player
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Set the Player name.
	 *
	 * @param name To set the name of the Player
	 */
	public void setPlayerName(String name) {
		this.playerName = name;
	}

	/**
	 * Get the list of the countries assigned to player.
	 *
	 * @return Countries List of countries of player .
	 */
	public ArrayList<Country> getPlayerCountries() {
		return playerCountries;
	}

	/**
	 * Set the list of the countries to player.
	 *
	 * @param countries To set the list of the countries to player.
	 */
	public void setPlayerCountries(ArrayList<Country> countries) {
		this.playerCountries = countries;
	}

	/**
	 * Get the army count of the player.
	 *
	 * @return Army count
	 */
	public int getNoOfArmies() {
		return noOfArmies;
	}

	/**
	 * Set the army count of the player.
	 *
	 * @param count To set the army count of the player
	 */
	public void setNoOfArmies(int count) {
		this.noOfArmies = count;
	}

	/**
	 * This method is used to add the country to the player's countries list.
	 *
	 * @param country The country that needs to be added.
	 */
	public void addCountry(Country country) {
		this.playerCountries.add(country);
	}

	/**
	 * This method gets the current player
	 * 
	 * @return Current player
	 */
	public GamePlayer getCurrentPlayer() {
		return current;
	}

	/**
	 * This method sets the current player
	 * 
	 * @param current Current player
	 */
	public void setCurrentPlayer(GamePlayer current) {
		this.current = current;
		setChanged();
		notifyObservers();
	}

	@Override
	public String toString() {
		return "Player [PlayerName=" + playerName + ", Armies=" + noOfArmies + ", Countries=" + playerCountries + "]";

	}

}