class Score {
    private String homeTeamName;

    private String visitingTeamName;

    private int homeTeam;

    private int visitingTeam;

    private long startTime;

    private boolean timeout;

    Score(String homeTeamName, String visitingTeamName) {
        this.homeTeamName = homeTeamName;
        this.visitingTeamName = visitingTeamName;
        this.homeTeam = this.visitingTeam = 0;
        this.startTime = System.currentTimeMillis();
    }

    boolean isTimeout() {
        return timeout;
    }

    void updateHomeTeam() {
        homeTeam++;
    }

    void updateVisitingTeam() {
        visitingTeam++;
    }

    private String timeElapsed() {
        // 3 mins in game maps to 90 mins in real-life
        long time = (System.currentTimeMillis() - startTime) * 30;
        int minute = (int) (time / 60000);
        int seconds = (int) ((time - minute * 60000) / 1000);
        timeout = minute >= 90;
        return String.format("%02d:%02d", minute, seconds);
    }

    String abbrev() {
        return String.format("%s %s %d : %s %d", timeElapsed(), homeTeamName, homeTeam, visitingTeamName, visitingTeam);
    }
}
