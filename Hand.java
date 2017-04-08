
import java.util.ArrayList;

/**
 * <p>A Hand of Uno cards, held by a particular player. A Hand object is
 * responsible for playing a Card (<i>i.e.</i>, actually choosing a card to
 * play) when the player's turn comes up. To do this, it implements the
 * strategy pattern by which this choice can be delegated to an arbitrary
 * implementer of the UnoPlayer class.</p>
 * @since 1.0
 * 某个玩家手中的一副uno牌
 * 一副牌对象负责玩一局，即选择一手牌来玩
 * 它实现一个策略类型，通过这个选择能够被授权给任一一个实现了unoplayer的类
 */
public class Hand {

    private ArrayList<Card> cards;//玩家手中的牌
    private UnoPlayer player;//当前玩家
    private String playerName;//当前玩家的姓名

    /**
     * Instantiate a Hand object to be played by the UnoPlayer class, and
     * the player name, passed as arguments. This implements a strategy
     * pattern whereby the constructor accepts various strategies that
     * implement the UnoPlayer interface.
     * 例示一手被某个unoplayer类来玩的牌对象，和这个玩家的姓名，作为一个参数传递
     * 它实现了一种策略类型，即通过这个构造函数接受各种各样的实现了unoplayer接口的策略（它实现了unoplayer的接口）
     */
    public Hand(String unoPlayerClassName, String playerName) {
        try {
            player = (UnoPlayer)
                Class.forName(unoPlayerClassName).newInstance();//为player实例话，获得一个player实现了unoplayer接口的对象
        }
        catch (Exception e) {
            System.out.println("Problem with " + unoPlayerClassName + ".");
            e.printStackTrace();
            System.exit(1);
        }
        this.playerName = playerName;
        cards = new ArrayList<Card>();
    }

    /**
     * Add (draw) a card to the hand.
     * 从牌堆中获得一张牌
     */
    void addCard(Card c) {
        cards.add(c);
    }

    /**
     * Return the number of cards in the hand.
     * 返回自己牌中的牌的数量
     */
    public int size() {
        return cards.size();
    }

    /**
     * It's your turn: play a card. When this method is called, the Hand
     * object choose a Card from the Hand based on the strategy that is
     * controlling it (<i>i.e.</i>, whose code was passed to the Hand
     * constructor.) If the player cannot legally play any of his/her
     * cards, null is returned.
     * @return The Card object to be played (which has been removed from
     * this Hand as a side effect), or null if no such Card can be played.
     * 轮到你打出一张牌了。当这个方法被调用的时候，这个Hand对象从自己手中的牌中选择一张牌
     * 用这种策略，即控制它，即某个人的代码被传递给Hand的构造函数
     * 如果当前玩家不能出牌，null将会被返回。
     * 将会返回一张牌（这张牌已经从当前自己手中的牌打出作为一个次要的效果）
     * 或者null如果没有牌被打出
     */
    //我们是从Game中调用hand.play(this)方法进入的，将game作为参数进入里面，目的是返回一张牌
    Card play(Game game) {
        int playedCard;//将要打出的牌，game.getupCard()获得桌子中间的牌是什么，然后获得calledColor的颜色如果为wild，再获得游戏的状态
        playedCard = player.play(cards, game.getUpCard(), game.calledColor,
            game.getGameState());//调用我将会写的play（，，，，）
        if (playedCard == -1) {
            return null;
        }
        else {
            Card toPlay = cards.remove(playedCard);
            return toPlay;
        }
    }

    /**
     * Designed to be called in response to a wild card having been played
     * on the previous call to this object's play() method. This method
     * will choose one of the four colors based on the strategy controlling
     * it (<i>i.e.</i>, the class whose code was passed to the Hand
     * constructor.)
     * @return A Color value, <i>not</i> Color.NONE.
     * 设计当被调用的时候负责一张wild牌，被打出在目前的调用中对于对象的play（）方法
     * 这个方法将会依照策略控制它，将会选择四种颜色之一
     * 即这个类的整个代码将会被传递给Hand的构造函数
     */
    UnoPlayer.Color callColor(Game game) {
        return player.callColor(cards);
    }

    /**
     * Return true only if this Hand has no cards, which should trigger a
     * winning condition.
     * 返回true当且仅当本玩家手中没有牌的时候，这时将会触发赢了的条件
     */
    public boolean isEmpty() {
        return cards.size() == 0;
    }

    /**
     * Return a string rendering of this Hand. See Card::toString() for
     * notes about how individual cards are rendered.
     * 返回玩家手中的牌，以字符串的形式
     * 看card：：toString（） 作为笔记关于如何个人的牌被返回
     */
    public String toString() {
        String retval = "";
        for (int i=0; i<cards.size(); i++) {
            retval += cards.get(i);
            if (i<cards.size()-1) {
                retval += ",";
            }
        }
        return retval;
    }

    /**
     * Return the forfeit value of this Hand, as it now stands (in other
     * words, the sum of all the forfeit values of cards still possessed.)
     * 返回玩家手中牌的值的和
     */
    public int countCards() {
        int total = 0;
        for (int i=0; i<cards.size(); i++) {
            total += cards.get(i).forfeitCost();
        }
        return total;
    }

    /**
     * Return the name of the contestant.
     * 返回玩家的姓名
     */
    public String getPlayerName() {
        return playerName;
    }
}
