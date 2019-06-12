class Audience {

    private int homeTeamFans;

    private int visitingTeamFans;

    Audience(int homeTeamFans, int visitingTeamFans) {
        this.homeTeamFans = homeTeamFans;
        this.visitingTeamFans = visitingTeamFans;
    }

    /**
     * Soccer fans would cry and cheer for their teams
     */
    void cheerRandomly() {
        if (Tools.nextInt(21) == 5) {
            Tools.playAudio("cheer.wav");
            if (Tools.DEBUG)
                System.out.println(String.format("%d home team supporters are cheering and %d visiting team " +
                    "supporters shouting.", homeTeamFans, visitingTeamFans));
        }
    }
}
