import javax.swing.*;
import java.awt.*;

class GameBoard extends JPanel {

    private enum Status {
        INIT, START, PAUSE, RESET, OVER
    }

    private Field field;
    private Team homeTeam;
    private Team visitingTeam;
    private Ball ball;
    private Referee majorReferee;
    private Referee[] linesmen;
    private Audience audience;
    private Status status;
    private Score score;

    void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    void setVisitingTeam(Team visitingTeam) {
        this.visitingTeam = visitingTeam;
    }

    void setField(String stadiumName) {
        this.field = new Field(stadiumName, Constants.FIELD_WIDTH, Constants.FIELD_HEIGHT);
    }

    void applyDefaultSettings() {
        this.setField("San Siro");
        this.setHomeTeam(Team.fromJSON("real-madrid.json"));
        this.setVisitingTeam(Team.fromJSON("barcelona.json"));
        this.initialize().initLocations();
    }

    Score getScore() {
        return score;
    }

    GameBoard initialize() {
        this.score = new Score(this.homeTeam.getClubName(), this.visitingTeam.getClubName());
        this.ball = new Ball(5, Color.WHITE, this.field.middle());

        this.majorReferee = new Referee(Referee.Role.MAJOR, new Name("Pierluigi", "Collina"), 46);
        this.linesmen = new Referee[] {
            new Referee(Referee.Role.LINESMAN, new Name("Tom", "Yang"), 11),
            new Referee(Referee.Role.LINESMAN, new Name("Qianmu", "Yang"), 11)
        };
        if (Tools.DEBUG)
            System.out.println(String.format("There are %d linesmen assisting %s.", linesmen.length, majorReferee));

        this.audience = new Audience(38000, 24000);
        this.status = Status.INIT;
        return this;
    }

    void initLocations() {
        this.homeTeam.initLocations(Constants.FIELD_WIDTH, Constants.FIELD_HEIGHT, true);
        this.visitingTeam.initLocations(Constants.FIELD_WIDTH, Constants.FIELD_HEIGHT, false);
        this.ball.setLocation(this.field.middle());
        this.majorReferee.setLocation(new Location(Constants.FIELD_WIDTH / 2 + Constants.ROLE_SIZE / 2,
            Constants.FIELD_HEIGHT / 2 - Constants.ROLE_SIZE / 2));
    }

    boolean isRunning() {
        return this.status == Status.START || this.status == Status.PAUSE;
    }

    void start() {
        this.status = Status.START;
        this.majorReferee.whistle();
        this.score = new Score(this.homeTeam.getClubName(), this.visitingTeam.getClubName());
    }

    void reset() {
        this.status = Status.RESET;
        this.initLocations();
        this.repaint();
    }

    private void restartAfterGoal() {
        reset();
        Tools.sleepSilently(1500);

        this.status = Status.START;
        this.majorReferee.whistle();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics g2d = g.create();
        if (status == null || status == Status.INIT) {
            g2d.setFont(new Font(g2d.getFont().getName(), Font.BOLD, 48));
            g2d.setColor(new Color(63, 122, 77));
            g2d.drawString("Welcome to the Most Boring Soccer Game!", 35, 90);
            g2d.drawImage(new ImageIcon("images/champions.jpg").getImage(), 13, 144, null);
        } else {
            this.field.draw(g2d);
            this.homeTeam.draw(g2d);
            this.visitingTeam.draw(g2d);
            this.ball.draw(g2d);
            this.majorReferee.draw(g2d);
        }
    }

    /**
     * Trigger event randomly as the simplest simulation of a soccer game
     */
    void triggerEvent() {
        this.homeTeam.play(ball);
        this.visitingTeam.play(ball);
        this.majorReferee.actRandomly();
        this.assertRules();
        this.audience.cheerRandomly();
        if (score.isTimeout()) {
            this.status = Status.OVER;
            this.majorReferee.whistle();
        }
    }

    private void assertRules() {
        if (ball.checkScore(score)) {
            System.out.println("GOOOOOOOAL! " + score.abbrev());
            this.restartAfterGoal();
        } else if (ball.isOutOfField()) {
            System.out.println("Ball is out of field and game will pause!");
            ball.getLocation().handleOutOfBound();
        }
    }

}
