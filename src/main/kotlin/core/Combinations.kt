package core
/**
 * Scoring hand and return the answer who have a more strength hand
 */
class Combinations {
    fun scoreHand(player: Play, common: MutableList<Card>): Int {
// list of cards for all cards (5 on table and 2 in hand)
        val comb = mutableListOf<Card>()//arrayOf<Card>() //card list with all cards
        comb.add(player.cards[0])
        comb.add(player.cards[1])
        comb.add(common[0])
        comb.add(common[1])
        comb.add(common[2])
        comb.add(common[3])
        comb.add(common[4])
        comb.sortByDescending { it.rank } //sort the list of cards from value high to low

        val isRoyalFlush = checkRoyalFlush(comb)
        val isStraightFlush = checkStraightFlush(comb)
        val isFourOfAKind = checkFourOfAKind(comb)
        val isFullHouse = checkFullHouse(comb)
        val isFlush = checkFlush(comb)
        val isStraight = checkStraight(comb)
        val isThreeOfAKind = checkThreeOfAKind(comb)
        val isTwoPair = checkTwoPair(comb)
        val isOnePair = checkOnePair(comb)

        if (isRoyalFlush != 0) //if the player is a royal flush...
            return isRoyalFlush //return that return value
        //this continues for all hands, in order of how good the hand is
        if (isStraightFlush != 0)
            return isStraightFlush
        if (isFourOfAKind != 0)
            return isFourOfAKind
        if (isFullHouse != 0)
            return isFullHouse
        if (isFlush != 0)
            return isFlush
        if (isStraight != 0)
            return isStraight
        if (isThreeOfAKind != 0)
            return isThreeOfAKind
        if (isTwoPair != 0)
            return isTwoPair
        return if (isOnePair != 0)
            isOnePair
        else comb[0].rank.value
//if the player is nothing in their hand, return their best card
    }

    /**
     * checks to see if there is a royal flush in the hand
     */
    private fun checkRoyalFlush(comb: MutableList<Card>): Int {
        //if the highest 5 return a royal straight and are all the same suit
        if (comb[0].rank.value == 14 && comb[1].rank.value == 13 && comb[2].rank.value == 12 && comb[3].rank.value == 11 && comb[4].rank.value == 10
                && comb[0].suit == comb[1].suit && comb[1].suit == comb[2].suit && comb[2].suit == comb[3].suit && comb[3].suit == comb[4].suit)
            return 180 + comb[0].rank.value
        //if the middle 5 return a royal straight and are all the same suit
        if (comb[1].rank.value == 14 && comb[2].rank.value == 13 && comb[3].rank.value == 12 && comb[4].rank.value == 11 && comb[5].rank.value == 10
                && comb[1].suit == comb[2].suit && comb[2].suit == comb[3].suit && comb[3].suit == comb[4].suit && comb[4].suit == comb[5].suit)
            return 180 + comb[1].rank.value
        //if the lowest 5 return a royal straight and are all the same suit
        return if (comb[2].rank.value == 14 && comb[3].rank.value == 13 && comb[4].rank.value == 12 && comb[5].rank.value == 11 && comb[6].rank.value == 10
                && comb[2].suit == comb[3].suit && comb[3].suit == comb[4].suit && comb[4].suit == comb[5].suit && comb[5].suit == comb[6].suit)
            180 + comb[2].rank.value
        else 0
    }

    /**
     * checks to see if the hand is a straight flush
     */
    private fun checkStraightFlush(comb: MutableList<Card>): Int {
        //if the highest 5 return a straight and are all the same suit
        if (comb[0].rank.value == comb[1].rank.value + 1 && comb[1].rank.value == comb[2].rank.value + 1 && comb[2].rank.value == comb[3].rank.value + 1 && comb[3].rank.value == comb[4].rank.value + 1
                && comb[0].suit == comb[1].suit && comb[1].suit == comb[2].suit && comb[2].suit == comb[3].suit && comb[3].suit == comb[4].suit)
            return 160 + comb[0].rank.value
        //if the middle 5 return a straight and are all the same suit
        if (comb[1].rank.value == comb[2].rank.value + 1 && comb[2].rank.value == comb[3].rank.value + 1 && comb[3].rank.value == comb[4].rank.value + 1 && comb[4].rank.value == comb[5].rank.value + 1
                && comb[1].suit == comb[2].suit && comb[2].suit == comb[3].suit && comb[3].suit == comb[4].suit && comb[4].suit == comb[5].suit)
            return 160 + comb[1].rank.value
        //if the lowest 5 return a straight and are all the same suit
        return if (comb[2].rank.value == comb[3].rank.value + 1 && comb[3].rank.value == comb[4].rank.value + 1 && comb[4].rank.value == comb[5].rank.value + 1 && comb[5].rank.value == comb[6].rank.value + 1
                && comb[2].suit == comb[3].suit && comb[3].suit == comb[4].suit && comb[4].suit == comb[5].suit && comb[5].suit == comb[6].suit)
            160 + comb[2].rank.value
        else 0
    }

    /**
     * checks to see if the hand is four of a kind
     */
    private fun checkFourOfAKind(comb: MutableList<Card>): Int {
        //if the highest four cards are the same value
        if (comb[0].rank.value == comb[1].rank.value && comb[1].rank.value == comb[2].rank.value && comb[2].rank.value == comb[3].rank.value)
            return 140 + comb[0].rank.value
        //if next four are same value..
        if (comb[1].rank.value == comb[2].rank.value && comb[2].rank.value == comb[3].rank.value && comb[3].rank.value == comb[4].rank.value)
            return 140 + comb[1].rank.value
        if (comb[2].rank.value == comb[3].rank.value && comb[3].rank.value == comb[4].rank.value && comb[4].rank.value == comb[5].rank.value)
            return 140 + comb[2].rank.value
        return if (comb[3].rank.value == comb[4].rank.value && comb[4].rank.value == comb[5].rank.value && comb[5].rank.value == comb[6].rank.value)
            140 + comb[3].rank.value
        else 0
    }

    /**
     * checks to see if there is a full house in a hand
     */
    private fun checkFullHouse(comb: MutableList<Card>): Int {
        var third = checkThreeOfAKind(comb) //return the value from a three of a kind check
        if (third == 0) //if we ain't have three of a kind, there is no full house
            return 0
        else { //if there is three of a kind, check for two of a kind of different value from the three of a kind
            third -= 60 //change this value to be the value of the card used in the three of a kind
            if (comb[0].rank.value == comb[1].rank.value && comb[0].rank.value != third)
                return 120 + third //if we have a pair that is not the three of a kind, return the full house constant plus the value of the three of a kind card
            if (comb[1].rank.value == comb[2].rank.value && comb[1].rank.value != third)
                return 120 + third
            if (comb[2].rank.value == comb[3].rank.value && comb[2].rank.value != third)
                return 120 + third
            if (comb[3].rank.value == comb[4].rank.value && comb[3].rank.value != third)
                return 120 + third
            if (comb[4].rank.value == comb[5].rank.value && comb[4].rank.value != third)
                return 120 + third
            if (comb[5].rank.value == comb[6].rank.value && comb[5].rank.value != third)
                return 120 + third
        }
        return 0
    }

    /**
     * checks to see if there is a flush in the hand
     */
    private fun checkFlush(comb: MutableList<Card>): Int {
        var numHeart = 0
        var numSpade = 0
        var numDiamond = 0
        var numClub = 0
        //variables for holding num of each suit
        //add values to the variables
        for (i in comb.indices) {
            if (comb[i].suit.imageName == "S") {
                numSpade++
            }
            if (comb[i].suit.imageName == "H") {
                numHeart++
            }
            if (comb[i].suit.imageName == "C") {
                numClub++
            }
            if (comb[i].suit.imageName == "D") {
                numDiamond++
            }
        }
        if (numDiamond > 4) { //if the flush is caused by diamonds
            if (comb[0].suit.imageName == "D") { //if the first card is highest in the flush
                return 100 + comb[0].rank.value
            }
            if (comb[1].suit.imageName == "D") { //if second card is highest in the flush
                return 100 + comb[1].rank.value
            }
            if (comb[2].suit.imageName == "D") { //if the third card is highest in the flush
                return 100 + comb[2].rank.value
            }
        }
        if (numClub > 4) { //if the flush is caused by clubs
            if (comb[0].suit.imageName == "C") { //if the first card is highest in the flush
                return 100 + comb[0].rank.value
            }
            if (comb[1].suit.imageName == "C") { //if the second card is highest in the flush
                return 100 + comb[1].rank.value
            }
            if (comb[2].suit.imageName == "C") { //if the third card is highest in the flush
                return 100 + comb[2].rank.value
            }
        }
        if (numHeart > 4) { //if the flush is caused by hearts
            if (comb[0].suit.imageName == "H") { //if the first card is highest in the flush
                return 100 + comb[0].rank.value
            }
            if (comb[1].suit.imageName == "H") { //if the second card is highest in the flush
                return 100 + comb[1].rank.value
            }
            if (comb[2].suit.imageName == "H") { //if the third card is highest in the flush
                return 100 + comb[2].rank.value
            }
        }
        if (numSpade > 4) { //if the flush is caused by spades
            if (comb[0].suit.imageName == "S") { //if the first card is highest in the flush
                return 100 + comb[0].rank.value
            }
            if (comb[1].suit.imageName == "S") { //if the second card is highest in the flush
                return 100 + comb[1].rank.value
            }
            if (comb[2].suit.imageName == "S") { //if the third card is highest in the flush
                return 100 + comb[2].rank.value
            }
        }
        return 0
    }

    /**
     * checks to see if there is a straight in the hand
     */
    private fun checkStraight(comb: MutableList<Card>): Int {
        //if the highest 5 return a straight
        if (comb[0].rank.value == comb[1].rank.value + 1 && comb[1].rank.value == comb[2].rank.value + 1 &&
                comb[2].rank.value == comb[3].rank.value + 1 && comb[3].rank.value == comb[4].rank.value + 1) {
            return 80 + comb[0].rank.value
        }
        //if the middle 5 return a straight
        if (comb[1].rank.value == comb[2].rank.value + 1 && comb[2].rank.value == comb[3].rank.value + 1 &&
                comb[3].rank.value == comb[4].rank.value + 1 && comb[4].rank.value == comb[5].rank.value + 1) {
            return 80 + comb[1].rank.value
        }
        //if the lowest 5 return a straight
        return if (comb[2].rank.value == comb[3].rank.value + 1 && comb[3].rank.value == comb[4].rank.value + 1 &&
                comb[4].rank.value == comb[5].rank.value + 1 && comb[5].rank.value == comb[6].rank.value + 1) {
            80 + comb[2].rank.value
        } else 0
    }

    /**
     * checks to see if there is three of a kind in the hand
     */
    private fun checkThreeOfAKind(comb: MutableList<Card>): Int {
        //if the first three are the same value...
        if (comb[0].rank.value == comb[1].rank.value && comb[1].rank.value == comb[2].rank.value) {
            return 60 + comb[0].rank.value
        }
        if (comb[1].rank.value == comb[2].rank.value && comb[2].rank.value == comb[3].rank.value) {
            return 60 + comb[1].rank.value
        }
        if (comb[2].rank.value == comb[3].rank.value && comb[3].rank.value == comb[4].rank.value) {
            return 60 + comb[2].rank.value
        }
        if (comb[3].rank.value == comb[4].rank.value && comb[4].rank.value == comb[5].rank.value) {
            return 60 + comb[3].rank.value
        }
        return if (comb[4].rank.value == comb[5].rank.value && comb[5].rank.value == comb[6].rank.value) {
            60 + comb[4].rank.value
        } else 0
    }

    /**
     * a method to see if there is two pair in the hand
     */
    private fun checkTwoPair(comb: MutableList<Card>): Int {
        var second = checkOnePair(comb) //check to see if there is a single pair
        if (second == 0) { //if no pair exists
            return 0 //return 0
        } else { //if there is a pair
            second -= 20 //second is now the value of the pair
            if (comb[0].rank.value == comb[1].rank.value && comb[0].rank.value != second) {
                return 40 + second //if we have a pair that is not the first pair, return the two pair constant plus the value of the highest pair card
            }
            if (comb[1].rank.value == comb[2].rank.value && comb[1].rank.value != second) {
                return 40 + second
            }
            if (comb[2].rank.value == comb[3].rank.value && comb[2].rank.value != second) {
                return 40 + second
            }
            if (comb[3].rank.value == comb[4].rank.value && comb[3].rank.value != second) {
                return 40 + second
            }
            if (comb[4].rank.value == comb[5].rank.value && comb[4].rank.value != second) {
                return 40 + second
            }
            if (comb[5].rank.value == comb[6].rank.value && comb[5].rank.value != second) {
                return 40 + second
            }
        }
        return 0
    }

    /**
     * a method to check if there is a single pair in the hand
     */
    private fun checkOnePair(comb: MutableList<Card>): Int {
        //checks to see if the first two cards are a pair...
        if (comb[0].rank.value == comb[1].rank.value)
            return 20 + comb[0].rank.value

        if (comb[1].rank.value == comb[2].rank.value)
            return 20 + comb[1].rank.value

        if (comb[2].rank.value == comb[3].rank.value)
            return 20 + comb[2].rank.value

        if (comb[3].rank.value == comb[4].rank.value)
            return 20 + comb[3].rank.value

        if (comb[4].rank.value == comb[5].rank.value)
            return 20 + comb[4].rank.value

        return if (comb[5].rank.value == comb[6].rank.value)
            20 + comb[5].rank.value
        else 0
    }
}
