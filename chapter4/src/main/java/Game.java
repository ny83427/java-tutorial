import java.awt.*;

public class Game {

    private Field field;
    private Team homeTeam;
    private Team visitingTeam;
    private Ball ball;
    private Referee majorReferee;
    private Referee[] linesmen;
    private Audience audience;

    void start() {

    }

    public static void main(String[] args) {
        Game game = new Game();

        game.field = new Field("San Siro", 105, 68);
        game.homeTeam = Team.fromJSON("real-madrid.json");
        game.visitingTeam = Team.fromJSON("barcelona.json");
        game.ball = new Ball(5, Color.WHITE, game.field.middle());
        game.majorReferee = new Referee();
        game.linesmen = new Referee[]{new Referee(), new Referee()};
        game.audience = new Audience(36000, 28000);

        game.start();
    }

}
