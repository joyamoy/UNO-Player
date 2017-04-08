import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class netid_UnoPlayer implements UnoPlayer {

	/**
	 * play - This method is called when it's your turn and you need to choose
	 * what card to play. 这个方法被调用当轮到你的时候并且你需要选取一张牌来出
	 * 
	 * The hand parameter tells you what's in your hand. You can call
	 * getColor(), getRank(), and getNumber() on each of the cards it contains
	 * to see what it is. The color will be the color of the card, or
	 * "Color.NONE" if the card is a wild card. The rank will be "Rank.NUMBER"
	 * for all numbered cards, and another value (e.g., "Rank.SKIP,"
	 * "Rank.REVERSE," etc.) for special cards. The value of a card's "number"
	 * only has meaning if it is a number card. (Otherwise, it will be -1.) hand
	 * 参数告诉你你手中有什么牌，你可以调用getColor（），getRank（），getNumber（）在
	 * 每一张牌中。这个颜色将会返回这张牌的颜色，或者none如果它是一张wild牌 the rank 将会是rank.number
	 * 对于所有被编号的牌，和另外一个值，如skip，reverse等等对于特殊牌 一张牌的值仅当它是数字牌时才有意义，否则返回-1，即为特殊牌时返回-1
	 * 
	 * The upCard parameter works the same way, and tells you what the up card
	 * (in the middle of the table) is. the upCard参数同理，将会告诉你the up card
	 * 是什么（即现在桌子中间的是什么牌？）
	 * 
	 * The calledColor parameter only has meaning if the up card is a wild, and
	 * tells you what color the player who played that wild card called. 仅当the
	 * up card是wild时the calledcolor 参数才有意义并且告诉你上家决定的颜色是什么
	 * 
	 * Finally, the state parameter is a GameState object on which you can
	 * invoke methods if you choose to access certain detailed information about
	 * the game (like who is currently ahead, what colors each player has
	 * recently called, etc.) 最后，这个state参数是一个GameState对象，你可以调用方法如果你选择去获得某种详细的
	 * 信息关于这个游戏（像谁在前面，每个玩家最近调用的颜色是什么）
	 * 
	 * You must return a value from this method indicating which card you wish
	 * to play. If you return a number 0 or greater, that means you want to play
	 * the card at that index. If you return -1, that means that you cannot play
	 * any of your cards (none of them are legal plays) in which case you will
	 * be forced to draw a card (this will happen automatically for you.)
	 * 你必须返回一个值在这个方法中，暗指哪张牌你想出。如果你返回0或者更大的，那意味着你想出的那张牌的
	 * 下标。如果你返回-1，那就意味着你不能出任何牌（你手中没有一张牌是合理的） 在这种情况下你将会被强迫抽一张牌（它将会自动给你一张牌）
	 */
	public int play(List<Card> hand, Card upCard, Color calledColor, GameState state) {
		int size = hand.size();// 牌的总张数
		int return_index = -1;
		ArrayList<Card> properCard = new ArrayList<Card>();
		// 不用在乎calledColor的值，先找到符合要求的所有牌
		for (int i = 0; i < size; i++) {
			if (hand.get(i).canPlayOn(upCard, calledColor)) {
				// 找到符合要求的牌，加入properCard
				properCard.add(hand.get(i));
				return_index = i;//return_index为最后一张满足的下标
			}
			
		}
	
		//如果一张都没有找到，那么返回-1
		if (properCard.size() == 0) {
			return -1;
		} 
		//现在表明有可以出的牌，那么我们如何出呢？不是给了GameState的状态吗？我们判断当前玩家的next的当前的牌的数目
		int[] numCardsInHandsOfUpcomingPlayers = state.getNumCardsInHandsOfUpcomingPlayers();
		//当前玩家的数目
		
		//以及下家的下家的数目  以及当前玩家的上家的牌的数目来出牌
		//思想是尽量不要让牌少的人出牌，不然输得可能性就要大些了 反过来，就是尽量让牌多的人出牌
		//主要是考虑有特殊牌的情况下
		//主要考虑2个位置，当前玩家的后一家，后后一家
		//然后分为3种情况
		
		//第1种 如果都为1 ，如果有reverse出reverse
		if( numCardsInHandsOfUpcomingPlayers[0] == 1 
				&& numCardsInHandsOfUpcomingPlayers[0] == numCardsInHandsOfUpcomingPlayers[1]) {
			for (int i = 0; i < properCard.size(); i++) {
				if (properCard.get(i).getRank() == Rank.REVERSE) {
					// 此时返回REVERSE的下表
					for (int j = 0; j < hand.size(); j++) {
						if (hand.get(j).getRank() == Rank.REVERSE) {
							return j;
						}
					}
				}
			}
		}
		//第2种  下家牌最多，直接出一张不是特殊牌的牌
		if(numCardsInHandsOfUpcomingPlayers[0] >= numCardsInHandsOfUpcomingPlayers[1]) {
			for (int i = 0; i < properCard.size(); i++) {
				if (properCard.get(i).getRank() == Rank.NUMBER) {//找到符合牌中的是number的牌
					// 此时返回NUMBER的下标
					for (int j = 0; j < hand.size(); j++) {
						if (hand.get(j) == properCard.get(i)) {
							return j;
						}
					}
				}
			}
		} else {
			//第三种，下下家牌最多
			//如果有skip，draw2,wild4就出
			//当有wild4的时候就先出wild4
			for (int i = 0; i < properCard.size(); i++) {
				if (properCard.get(i).getRank() == Rank.WILD_D4) {
					// 此时返回wild-d4的下表
					for (int j = 0; j < hand.size(); j++) {
						if (hand.get(j).getRank() == Rank.WILD_D4) {
							return j;
						}
					}
				}
			}
			// 再判断是否有DRAW_TWO，如果有，则返回DRAW_TWO
			for (int i = 0; i < properCard.size(); i++) {
				if (properCard.get(i).getRank() == Rank.DRAW_TWO) {
					// 此时返回DRAW_TWO的下表
					for (int j = 0; j < hand.size(); j++) {
						if (hand.get(j).getRank() == Rank.DRAW_TWO) {
							return j;
						}
					}
				}
			}
			// 如果之前的都没有，再判断是否有SKIP
			for (int i = 0; i < properCard.size(); i++) {
				if (properCard.get(i).getRank() == Rank.SKIP) {
					// 此时返回SKIP的下标
					for (int j = 0; j < hand.size(); j++) {
						if (hand.get(j).getRank() == Rank.SKIP) {
							return j;
						}
					}
				}
			}
		}

		// 如果以上的判断完了，还没有符合的，那么就返回return_index
		return return_index;

	}

	/**
	 * callColor - This method will be called when you have just played a wild
	 * card, and is your way of specifying which color you want to change it to.
	 * 这个方法将会被调用当你打出一张wild牌，你会确定那种颜色你想出
	 * 
	 * You must return a valid Color value from this method. You must not return
	 * the value Color.NONE under any circumstances. 你必须返回一个有效的颜色值从这个方法中
	 * 
	 */
	public Color callColor(List<Card> hand) {
		// 首先，我手里有一些牌，我要返回的牌的颜色前应该分析我手中有那些牌，特殊的牌不考虑（为什么？）
		// 然后计算每种颜色的牌的张数，返回最多的牌的颜色
		int size = hand.size();// 创建变量size，计算总的张数
		int red_num = 0, yellow_num = 0, green_num = 0, blue_num = 0;
		for (int i = 0; i < size; i++) {
			if (hand.get(i).getColor() == UnoPlayer.Color.RED) {
				red_num++;
			}
			if (hand.get(i).getColor() == UnoPlayer.Color.YELLOW) {
				yellow_num++;
			}
			if (hand.get(i).getColor() == UnoPlayer.Color.GREEN) {
				green_num++;
			}
			if (hand.get(i).getColor() == UnoPlayer.Color.BLUE) {
				blue_num++;
			}

		}
		int[] arr_color = new int[4];
		arr_color[0] = red_num;
		arr_color[1] = yellow_num;
		arr_color[2] = green_num;
		arr_color[3] = blue_num;

		Arrays.sort(arr_color);//排序，最后一个为个数最多
		UnoPlayer.Color return_color;//定义返回的颜色变量
		if (arr_color[3] != 0) {//如果每种颜色都为0，则跳出if判断

			if (arr_color[3] == red_num) {
				return_color = UnoPlayer.Color.RED;
			}
			if (arr_color[3] == yellow_num) {
				return_color = UnoPlayer.Color.YELLOW;
			}
			if (arr_color[3] == green_num) {
				return_color = UnoPlayer.Color.GREEN;
			}
			if (arr_color[3] == blue_num) {
				return_color = UnoPlayer.Color.BLUE;
			}
		
		}
		return_color = UnoPlayer.Color.RED;//则任意选一种，我选的为红色
		return return_color;
	}

}
