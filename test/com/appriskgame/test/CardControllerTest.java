package com.appriskgame.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.appriskgame.controller.CardController;
import com.appriskgame.model.GamePlayer;

/**
 * This test class is to test CardController
 *
 * @author Shruthi
 *
 */

public class CardControllerTest {

	CardController cardcontroller;
	GamePlayer player;

	/**
	 * Initial setup for Map Validation Test.
	 */
	@Before
	public void initialize() {
		cardcontroller = new CardController();
		player = new GamePlayer();
		cardcontroller.setDeckOfCards();
	}

	/**
	 * This method to validate the allocation of cards to player
	 *
	 */
	@Test
	public void testCardAllocation() {
		cardcontroller.allocateCardToPlayer(player);
		assertEquals(1, player.getCardList().size());
	}

	/**
	 * Check randomly allocating cards to player
	 *
	 */
	@Test
	public void testTypeOfCardAllocation() {
		ArrayList<String> cardTypes = new ArrayList<String>();
		cardTypes.add("infantry");
		cardTypes.add("cavalry");
		cardTypes.add("artillery");
		cardcontroller.allocateCardToPlayer(player);
		boolean check = false;
		for (int i = 0; i < cardTypes.size(); i++) {

			if (player.getCardList().get(0).getType().equalsIgnoreCase(cardTypes.get(i))) {
				check = true;
			}
		}
		assertTrue(check);

	}

}