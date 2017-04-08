
/**
 * <p>A Scoreboard is a simple container for player names and their current
 * scores. It can do the obvious things like increment someone's score,
 * find the score for a particular player, and determine the winner at the
 * end of the game.</p>
 * 一个记分板是一个简单的容器记录玩家的姓名和他们目前的分数
 * 它可以增加某人的分数，查找某人的分数和在游戏结束时决定赢家
 * @since 1.0
 */
public class Scoreboard {

    private String playerList[];//玩家的姓名数组
    private int scores[];//各个玩家的分数记录

    /**
     * Instantiate a new Scoreboard object, given an array of player names.
     * 例示一个记分对象，提供一系列玩家的姓名
     */
    //构造函数
    public Scoreboard(String playerList[]) {
        scores = new int[playerList.length];
        for (int i=0; i<playerList.length; i++) {
            scores[i] = 0;
        }
        this.playerList = playerList;
    }

    /**
     * Award points to a particular player.
     * @param player The zero-based player number who just won a game.
     * @param points The number of points to award.
     * 奖励分数给某个玩家
     * 参数player 某个刚赢了一局的玩家的编号
     * poins  奖励的分数
     */
    public void addToScore(int player, int points) {
        scores[player] += points; 
    }

    /**
     * Obtain the score of a particular player.
     * @param player The zero-based player number whose score is desired.
     * 获得某个选手的分数
     * player 希望获得某个玩家的代号
     */
    public int getScore(int player) {
        return scores[player];
    }

    /**
     * Render the Scoreboard as a string for display during game play.
     * 在游戏中，将记分板作为一个字符串返回展示
     */
    public String toString() {
        String retval = "";
        for (int i=0; i<scores.length; i++) {
            retval += "Player #" + i + ": " + scores[i] + "\n";
        }
        return retval;
    }

    /**
     * Return the list of player names.
     * 返回所有玩家的姓名？
     */
    public String[] getPlayerList() {
        return playerList;
    }

    /**
     * Return the number of players in the game.
     * 返回玩家的个数
     */
    public int getNumPlayers() {
        return playerList.length; 
    }

    /**
     * Return the zero-based number of the player who has won the game,
     * <i>presuming someone has.</i> (This method should only be called
     * once the end of the entire match has been detected by some other
     * means, and returns the number of the player with the highest score.)
     * 返回赢这场游戏的玩家的编号
     * 假定某人已经赢了
     * 这个方法只应该被调用一旦在整场比赛中有人赢了，并且返回最高分数的玩家编号
     */
    public int getWinner() {
        int winner = 0;
        int topScore = scores[0];
    
        for (int i=1; i<scores.length; i++) {
            if (scores[i] > topScore) {
                topScore = scores[i];
                winner = i;
            }
        }
        return winner;
    }
}
