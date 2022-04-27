package by.bsuir.rpodmp1;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

public class UserInfo {

    private final String username;
    private long highestScore;
    private final List<GameInfo> userGames;

    public UserInfo(String name, List<GameInfo> games) {
        username = name;
        userGames = games;
        highestScore = findHighestScore();
    }

    public String getUsername() {
        return username;
    }

    public long getHighestScore() {
        return highestScore;
    }

    public List<GameInfo> getUserGames() {
        return userGames;
    }

    public GameInfo getGameAt(int index) {
        return userGames.get(index);
    }

    public void AddNewGame(GameInfo gameInfo) {
        userGames.add(gameInfo);
    }

    private long findHighestScore() {
        if (userGames.isEmpty()) return 0;

        long cScore = -1;
        for (GameInfo game : userGames) {
            if (game.score > cScore) {
                cScore = game.score;
            }
        }

        return cScore;
    }

    static class GameInfo {
        Date date;
        long score;

        public GameInfo(Date date, long score) {
            this.date = date;
            this.score = score;
        }

        public Date getDate() {
            return date;
        }

        public long getScore() {
            return score;
        }

        @NonNull
        @Override
        public String toString() {
            String[] twoComponents = Database.dateFormat.format(date).split(" ");

            return twoComponents[0] + "\n" +
                    twoComponents[1] + "\n" +
                    score;
        }
    }
}
