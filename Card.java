
/**
 * <p>A Card in an Uno deck. Each Card knows its particular type, which is
 * comprised of a 3-tuple (color, rank, number). Not all of these values
 * are relevant for every particular type of card, however; for instance,
 * wild cards have no color (getColor() will return Color.NONE) or number
 * (getNumber() will return -1).</p>
 * <p>A Card knows its forfeit cost (<i>i.e.</i>, how many points it counts
 * against a loser who gets stuck with it) and how it should act during
 * game play (whether it permits the player to change the color, what
 * effect it has on the game state, etc.)</p>
 * 一张牌在uno桌面上。每张牌有它自己特别的类型，由3个属性组成（颜色，排列，数字）
 * 并不是所有的值都与每一种特别类型的牌相关
 * 例如，wild 牌没有颜色（调用getColor（）的时候将会返回Color.None）
 * 调用getNUmber（）时返回-1
 * 
 * 一张牌知道它自己失去的价值，即它值好多点
 * 
 * @since 1.0
 */
public class Card {

    /**
     * For terminals that support it, setting PRINT_IN_COLOR to true will
     * annotate toString() calls with ANSI color codes. This is known to
     * work on Ubuntu Linux, and known not to work in DOS terminal and
     * NetBeans.
     * 对于终端支持它，设置Print_in_color 为true 将会注释toString（）调用用ANSi颜色代码
     */
    public static final boolean PRINT_IN_COLOR = false;

    private UnoPlayer.Color color;
    private UnoPlayer.Rank rank;
    private int number;

    /**
     * Constructor for non-number cards (skips, wilds, etc.)
     * 非数值牌的构造函数
     */
    public Card(UnoPlayer.Color color, UnoPlayer.Rank rank) {
        this.color = color;
        this.rank = rank;
        this.number = -1;
    }

    /**
     * Constructor for number cards.
     * 有值的构造函数
     */
    public Card(UnoPlayer.Color color, int number) {
        this.color = color;
        this.rank = UnoPlayer.Rank.NUMBER;
        this.number = number;
    }

    /**
     * Constructor to explicitly set entire card state.
     * 构造函数精确的设置整张牌的状态
     */
    public Card(UnoPlayer.Color color, UnoPlayer.Rank rank, int number) {
        this.color = color;
        this.rank = rank;
        this.number = number;
    }

    /**
     * Render this Card object as a string. Whether the string comes out
     * with ANSI color codes is controlled by the PRINT_IN_COLOR static
     * class variable.
     * 以字符串的形式返回本张牌
     */
    public String toString() {
        String retval = "";
        if (PRINT_IN_COLOR) {
            switch (color) {
                case RED:
                    retval += "\033[31m";
                    break;
                case YELLOW:
                    retval += "\033[33m";
                    break;
                case GREEN:
                    retval += "\033[32m";
                    break;
                case BLUE:
                    retval += "\033[34m";
                    break;
                case NONE:
                    retval += "\033[1m";
                    break;
            }
        }
        else {
            switch (color) {
                case RED:
                    retval += "R";
                    break;
                case YELLOW:
                    retval += "Y";
                    break;
                case GREEN:
                    retval += "G";
                    break;
                case BLUE:
                    retval += "B";
                    break;
                case NONE:
                    retval += "";
                    break;
            }
        }
        switch (rank) {
            case NUMBER:
                retval += number;
                break;
            case SKIP:
                retval += "S";
                break;
            case REVERSE:
                retval += "R";
                break;
            case WILD:
                retval += "W";
                break;
            case DRAW_TWO:
                retval += "+2";
                break;
            case WILD_D4:
                retval += "W4";
                break;
        }
        if (PRINT_IN_COLOR) {
            retval += "\033[37m\033[0m";
        }
        return retval;
    }

    /**
     * Returns the number of points this card will count against a player
     * who holds it in his/her hand when another player goes out.
     * 返回本张牌的值
     */
    public int forfeitCost() {
        if (rank == UnoPlayer.Rank.SKIP || rank == UnoPlayer.Rank.REVERSE ||
            rank == UnoPlayer.Rank.DRAW_TWO) {
            return 20;
        }
        if (rank == UnoPlayer.Rank.WILD || rank == UnoPlayer.Rank.WILD_D4) {
            return 50;
        }
        if (rank == UnoPlayer.Rank.NUMBER) {
            return number;
        }
        System.out.println("Illegal card!!");
        return -10000;
    }

    /**
     * Returns true only if this Card can legally be played on the up card
     * passed as an argument. The second argument is relevant only if the
     * up card is a wild.
     * @param c An "up card" upon which the current object might (or might
     * not) be a legal play.
     * @param calledColor If the up card is a wild card, this parameter
     * contains the color the player of that color called.
     * 返回true当且仅当这张牌能够合理地被打出当上一张牌传递作为一个参数的时候
     * 第二个参数仅当上一张牌是wild的时候才相关
     * 一张“up card” 当目前对象可能是或者不是一张可以打出的牌
     * calledColor 如果the up card 是一张wild牌，这个参数包含玩家将要决定的颜色
     */ 
    public boolean canPlayOn(Card c, UnoPlayer.Color calledColor) {//rank为当前card的rank
        if (rank == UnoPlayer.Rank.WILD ||
            rank == UnoPlayer.Rank.WILD_D4 ||
            color == c.color ||
            color == calledColor ||
            (rank == c.rank && rank != UnoPlayer.Rank.NUMBER) ||
            number == c.number && rank == UnoPlayer.Rank.NUMBER && c.rank == UnoPlayer.Rank.NUMBER)
        {
            return true;
        }
        return false;
    }

    /**
     * Returns true only if playing this Card object would result in the
     * player being asked for a color to call. (In the standard game, this
     * is true only for wild cards.)
     * 当为wild的时候返回true；
     */
    public boolean followedByCall() {
        return rank == UnoPlayer.Rank.WILD || rank == UnoPlayer.Rank.WILD_D4;
    }

    /**
     * This method should be called immediately after a Card is played,
     * and will trigger the effect peculiar to that card. For most cards,
     * this merely advances play to the next player. Some special cards
     * have other effects that modify the game state. Examples include a
     * Skip, which will advance <i>twice</i> (past the next player), or a
     * Draw Two, which will cause the next player to have to draw cards.
     * @param game The Game being played, whose state may be modified by
     * this card's effect.
     * @throws EmptyDeckException Thrown only in very exceptional cases
     * when a player must draw as a result of this card's effect, yet the
     * draw cannot occur because of un-shufflable deck exhaustion.
     * 在一张牌被打出去的时候这个方法应该立刻被调用并且将会触发这张牌所引起的效果。
     * 对于大多数牌，这个方法只是传递给下一个玩家。
     * 一些特殊的牌有一刻可以改变游戏状态的效果。
     * 
     */
    void performCardEffect(Game game) throws EmptyDeckException {
        switch (rank) {
            case SKIP:
                game.advanceToNextPlayer();
                game.advanceToNextPlayer();
                break;
            case REVERSE:
                game.reverseDirection();
                game.advanceToNextPlayer();
                break;
            case DRAW_TWO:
                nextPlayerDraw(game);
                nextPlayerDraw(game);
                game.advanceToNextPlayer();
                game.advanceToNextPlayer();
                break;
            case WILD_D4:
                nextPlayerDraw(game);
                nextPlayerDraw(game);
                nextPlayerDraw(game);
                nextPlayerDraw(game);
                game.advanceToNextPlayer();
                game.advanceToNextPlayer();
                break;
            default:
                game.advanceToNextPlayer();
                break;
        }
    }

    private void nextPlayerDraw(Game game) throws EmptyDeckException {
        int nextPlayer = game.getNextPlayer();
        Card drawnCard;
        try {
            drawnCard = game.deck.draw();
        }
        catch (EmptyDeckException e) {
            game.print("...deck exhausted, remixing...");
            game.deck.remix();
            drawnCard = game.deck.draw();
        }
        game.h[nextPlayer].addCard(drawnCard);
        //game.println("  Player #" + nextPlayer + " draws " + drawnCard + ".");
        game.println("  " + game.h[nextPlayer].getPlayerName() + " draws " +
            drawnCard + ".");
    }

    /**
     * Returns the color of this card, which is Color.NONE in the case of
     * wild cards.
     * 返回这张牌的颜色，当时wild时返回none
     */
    public UnoPlayer.Color getColor() {
        return color;
    }

    /**
     * Returns the rank of this card, which is Rank.NUMBER in the case of
     * number cards (calling getNumber() will retrieve the specific
     * number.)
     * 返回排列
     */
    public UnoPlayer.Rank getRank() {
        return rank;
    }

    /**
     * Returns the number of this card, which is guaranteed to be -1 for
     * non-number cards (cards of non-Rank.NUMBER rank.)
     * 返回这张牌的值，如果没有值得牌返回-1
     */
    public int getNumber() {
        return number;
    }

}
