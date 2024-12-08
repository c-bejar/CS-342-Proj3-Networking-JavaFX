import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MyTest {
	static Dealer d1;
	static Deck deck;

	@BeforeEach
	void setup() {
		d1 = new Dealer();
		deck = new Deck();
	}

	// Deck + Dealer Tests ===================================================================

	@Test
	void constructorTestOne() {
		int size = deck.size();
		assertEquals(52, size);
	}

	@Test
	void constructorTestTwo() {
		int size = d1.theDeck.size();
		assertEquals(52, size);
	}

	@Test
	void constructorTestThree() {
		int size = d1.dealersHand.size();
		assertEquals(0, size);
	}

	@Test
	void dealerHandUpdating() {
		d1.dealersHand = d1.dealHand();
		assertEquals(3, d1.dealersHand.size());
	}

	@Test
	void shuffleTest() {
		Deck oldDeck = new Deck();
		for(Card card : deck) {
			oldDeck.add(card);
		}
		deck.shuffleDeck();
		assertNotEquals(deck, oldDeck);
	}

	@Test
	void dealHandTestOne() {
		d1.dealersHand = d1.dealHand();
		assertTrue(d1.dealersHand.size() == 3 && d1.theDeck.size() == 49);
	}

	@Test
	void dealHandTestTwo() {
		d1.dealersHand = d1.dealHand();
		d1.dealersHand = d1.dealHand();
		d1.dealersHand = d1.dealHand();
		assertTrue(d1.dealersHand.size() == 3 && d1.theDeck.size() == 43);
	}

	@Test
	void resetDeckTest() {
		d1.dealersHand = d1.dealHand();
		d1.dealersHand = d1.dealHand();
		d1.dealersHand = d1.dealHand();
		d1.theDeck.newDeck();
		assertEquals(52, d1.theDeck.size());
	}

	// It's possible this test could fail because we are only testing if values aren't all in same place preshuffle
	// Extremely tiny chance
	@Test
	void deckShuffledOne() {
		ArrayList<Card> values = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			for(int j = 2; j <= 14; j++) {
				values.add(new Card('X', j));
			}
		}

		boolean allMatch = true;
		int matches = 0;
		for(int i = 0; i < deck.size(); i++) {
			if(values.get(i).value == deck.get(i).value) {
				matches++;
			}
		}
		if(matches == deck.size()) {
			allMatch = false;
		}
		assertTrue(allMatch);
	}

	// It's possible this test could fail because we are only testing if suits aren't all in same place preshuffle
	// More likely than last test, but still unlikely
	@Test
	void deckShuffledTwo() {
		ArrayList<Card> suits = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			for(int j = 2; j <= 14; j++) {
				switch(i) {
					case 0:
						suits.add(new Card('C', 0));
						break;
					case 1:
						suits.add(new Card('H', 0));
						break;
					case 2:
						suits.add(new Card('S', 0));
						break;
					case 3:
						suits.add(new Card('D', 0));
						break;
				}
			}
		}

		boolean allMatch = true;
		int matches = 0;
		for(int i = 0; i < deck.size(); i++) {
			if(suits.get(i).suit == deck.get(i).suit) {
				matches++;
			}
		}
		if(matches == deck.size()) {
			allMatch = false;
		}
		assertTrue(allMatch);
	}

	// ThreeCardLogic Tests ==================================================================

	@Test
	void CorrectHandTestOne() {
		Card one = new Card('C', 8);
		Card two = new Card('C', 9);
		Card three = new Card('C', 10);
		d1.dealersHand = new ArrayList<>();
		d1.dealersHand.add(one);
		d1.dealersHand.add(two);
		d1.dealersHand.add(three);
		int score = ThreeCardLogic.evalHand(d1.dealersHand);
		assertEquals(1, score);
	}

	@Test
	void CorrectHandTestTwo() {
		Card one = new Card('C', 12);
		Card two = new Card('H', 12);
		Card three = new Card('D', 12);
		d1.dealersHand = new ArrayList<>();
		d1.dealersHand.add(one);
		d1.dealersHand.add(two);
		d1.dealersHand.add(three);
		int score = ThreeCardLogic.evalHand(d1.dealersHand);
		assertEquals(2, score);
	}

	@Test
	void CorrectHandTestThree() {
		Card one = new Card('D', 6);
		Card two = new Card('C', 7);
		Card three = new Card('D', 8);
		d1.dealersHand = new ArrayList<>();
		d1.dealersHand.add(one);
		d1.dealersHand.add(two);
		d1.dealersHand.add(three);
		int score = ThreeCardLogic.evalHand(d1.dealersHand);
		assertEquals(3, score);
	}

	@Test
	void CorrectHandTestFour() {
		Card one = new Card('D', 7);
		Card two = new Card('D', 9);
		Card three = new Card('D', 13);
		d1.dealersHand = new ArrayList<>();
		d1.dealersHand.add(one);
		d1.dealersHand.add(two);
		d1.dealersHand.add(three);
		int score = ThreeCardLogic.evalHand(d1.dealersHand);
		assertEquals(4, score);
	}

	@Test
	void CorrectHandTestFive() {
		Card one = new Card('D', 9);
		Card two = new Card('C', 13);
		Card three = new Card('H', 13);
		d1.dealersHand = new ArrayList<>();
		d1.dealersHand.add(one);
		d1.dealersHand.add(two);
		d1.dealersHand.add(three);
		int score = ThreeCardLogic.evalHand(d1.dealersHand);
		assertEquals(5, score);
	}

	@Test
	void CorrectHandTestSix() {
		Card one = new Card('D', 2);
		Card two = new Card('C', 8);
		Card three = new Card('H', 6);
		d1.dealersHand = new ArrayList<>();
		d1.dealersHand.add(one);
		d1.dealersHand.add(two);
		d1.dealersHand.add(three);
		int score = ThreeCardLogic.evalHand(d1.dealersHand);
		assertEquals(0, score);
	}

	@Test
	void PPWinningsTestOne() {
		Card one = new Card('C', 8);
		Card two = new Card('C', 9);
		Card three = new Card('C', 10);
		d1.dealersHand = new ArrayList<>();
		d1.dealersHand.add(one);
		d1.dealersHand.add(two);
		d1.dealersHand.add(three);
		int winnings = ThreeCardLogic.evalPPWinnings(d1.dealersHand, 10);
		assertEquals(400, winnings);
	}

	@Test
	void PPWinningsTestTwo() {
		Card one = new Card('C', 12);
		Card two = new Card('H', 12);
		Card three = new Card('D', 12);
		d1.dealersHand = new ArrayList<>();
		d1.dealersHand.add(one);
		d1.dealersHand.add(two);
		d1.dealersHand.add(three);
		int winnings = ThreeCardLogic.evalPPWinnings(d1.dealersHand, 10);
		assertEquals(300, winnings);
	}

	@Test
	void PPWinningsTestThree() {
		Card one = new Card('D', 6);
		Card two = new Card('C', 7);
		Card three = new Card('D', 8);
		d1.dealersHand = new ArrayList<>();
		d1.dealersHand.add(one);
		d1.dealersHand.add(two);
		d1.dealersHand.add(three);
		int winnings = ThreeCardLogic.evalPPWinnings(d1.dealersHand, 10);
		assertEquals(60, winnings);
	}

	@Test
	void PPWinningsTestFour() {
		Card one = new Card('D', 7);
		Card two = new Card('D', 9);
		Card three = new Card('D', 13);
		d1.dealersHand = new ArrayList<>();
		d1.dealersHand.add(one);
		d1.dealersHand.add(two);
		d1.dealersHand.add(three);
		int winnings = ThreeCardLogic.evalPPWinnings(d1.dealersHand, 10);
		assertEquals(30, winnings);
	}

	@Test
	void PPWinningsTestFive() {
		Card one = new Card('D', 9);
		Card two = new Card('C', 13);
		Card three = new Card('H', 13);
		d1.dealersHand = new ArrayList<>();
		d1.dealersHand.add(one);
		d1.dealersHand.add(two);
		d1.dealersHand.add(three);
		int winnings = ThreeCardLogic.evalPPWinnings(d1.dealersHand, 10);
		assertEquals(10, winnings);
	}

	@Test
	void PPWinningsTestSix() {
		Card one = new Card('D', 2);
		Card two = new Card('C', 8);
		Card three = new Card('H', 6);
		d1.dealersHand = new ArrayList<>();
		d1.dealersHand.add(one);
		d1.dealersHand.add(two);
		d1.dealersHand.add(three);
		int winnings = ThreeCardLogic.evalPPWinnings(d1.dealersHand, 10);
		assertEquals(0, winnings);
	}

	@Test
	void compareHandsTestOne() {
		ArrayList<Card> dealer = new ArrayList<>();
		ArrayList<Card> player = new ArrayList<>();

		dealer.add(new Card('C', 8));
		dealer.add(new Card('C', 9));
		dealer.add(new Card('C', 10));

		player.add(new Card('H', 8));
		player.add(new Card('H', 9));
		player.add(new Card('H', 10));
		int score = ThreeCardLogic.compareHands(dealer, player);
		assertEquals(0, score);
	}

	@Test
	void compareHandsTestTwo() {
		ArrayList<Card> dealer = new ArrayList<>();
		ArrayList<Card> player = new ArrayList<>();

		dealer.add(new Card('C', 2));
		dealer.add(new Card('D', 7));
		dealer.add(new Card('H', 14));

		player.add(new Card('H', 2));
		player.add(new Card('S', 4));
		player.add(new Card('C', 8));
		int score = ThreeCardLogic.compareHands(dealer, player);
		assertEquals(1, score);
	}

	@Test
	void compareHandsTestThree() {
		ArrayList<Card> dealer = new ArrayList<>();
		ArrayList<Card> player = new ArrayList<>();

		dealer.add(new Card('H', 13));
		dealer.add(new Card('C', 13));
		dealer.add(new Card('D', 9));

		player.add(new Card('D', 11));
		player.add(new Card('S', 11));
		player.add(new Card('S', 4));
		int score = ThreeCardLogic.compareHands(dealer, player);
		assertEquals(1, score);
	}

	@Test
	void compareHandsTestFour() {
		ArrayList<Card> dealer = new ArrayList<>();
		ArrayList<Card> player = new ArrayList<>();

		dealer.add(new Card('C', 10));
		dealer.add(new Card('C', 10));
		dealer.add(new Card('C', 10));

		player.add(new Card('H', 14));
		player.add(new Card('H', 14));
		player.add(new Card('H', 14));
		int score = ThreeCardLogic.compareHands(dealer, player);
		assertEquals(2, score);
	}

	@Test
	void compareHandsTestFive() {
		ArrayList<Card> dealer = new ArrayList<>();
		ArrayList<Card> player = new ArrayList<>();

		dealer.add(new Card('S', 5));
		dealer.add(new Card('S', 6));
		dealer.add(new Card('H', 7));

		player.add(new Card('D', 6));
		player.add(new Card('C', 7));
		player.add(new Card('D', 8));
		int score = ThreeCardLogic.compareHands(dealer, player);
		assertEquals(2, score);
	}

	@Test
	void compareHandsTestSix() {
		ArrayList<Card> dealer = new ArrayList<>();
		ArrayList<Card> player = new ArrayList<>();

		dealer.add(new Card('H', 14));
		dealer.add(new Card('S', 14));
		dealer.add(new Card('D', 14));

		player.add(new Card('C', 8));
		player.add(new Card('C', 9));
		player.add(new Card('C', 10));

		int score = ThreeCardLogic.compareHands(dealer, player);
		assertEquals(2, score);
	}

	@Test
	void compareHandsTestSeven() {
		ArrayList<Card> dealer = new ArrayList<>();
		ArrayList<Card> player = new ArrayList<>();

		dealer.add(new Card('H', 13));
		dealer.add(new Card('S', 13));
		dealer.add(new Card('D', 13));

		player.add(new Card('C', 2));
		player.add(new Card('H', 7));
		player.add(new Card('D', 14));

		int score = ThreeCardLogic.compareHands(dealer, player);
		assertEquals(1, score);
	}

	@Test
	void compareHandsTestEight() {
		ArrayList<Card> dealer = new ArrayList<>();
		ArrayList<Card> player = new ArrayList<>();

		dealer.add(new Card('C', 2));
		dealer.add(new Card('H', 7));
		dealer.add(new Card('D', 14));

		player.add(new Card('H', 13));
		player.add(new Card('S', 13));
		player.add(new Card('D', 13));

		int score = ThreeCardLogic.compareHands(dealer, player);
		assertEquals(2, score);
	}
}