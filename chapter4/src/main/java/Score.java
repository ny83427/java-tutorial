import java.util.concurrent.TimeUnit;

class Score {
    /**
     * Duration of a whole match in game: {@value} minutes
     */
    private static final int WHOLE_MATCH_GAME_TIME = 3;

    private String homeTeamName;

    private String visitingTeamName;

    private int homeTeam;

    private int visitingTeam;

    private long startTime;

    Score(String homeTeamName, String visitingTeamName) {
        this.homeTeamName = homeTeamName;
        this.visitingTeamName = visitingTeamName;
        this.homeTeam = this.visitingTeam = 0;
        this.startTime = System.currentTimeMillis();
    }

    boolean isTimeout() {
        return System.currentTimeMillis() - startTime >= TimeUnit.MINUTES.toMillis(WHOLE_MATCH_GAME_TIME);
    }

    void updateHomeTeam() {
        homeTeam++;
    }

    void updateVisitingTeam() {
        visitingTeam++;
    }

    private String timeElapsed() {
        long time = (System.currentTimeMillis() - startTime) * 90 / WHOLE_MATCH_GAME_TIME;
        int minute = (int) (time / 60000);
        int seconds = (int) ((time - minute * 60000) / 1000);
        return String.format("%02d:%02d", minute, seconds);
    }

    String abbrev() {
        return String.format("%s %s %d : %s %d", timeElapsed(), homeTeamName, homeTeam, visitingTeamName, visitingTeam);
    }
}
