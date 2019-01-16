import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class Player extends Person {
    private enum Role {
        FORWARD, MIDFIELDER, DEFENDER, GOALKEEPER
    }

    private enum Speed {
        NORMAL(40), FAST(60), SUPERFAST(80);

        private int value;

        Speed(int value) {
            this.value = value;
        }
    }

    private int number;

    private Role role;

    private int height;

    private Speed speed = Speed.NORMAL;

    private Player(Name name, int number, Role role, int age, int height) {
        super(name, age);
        this.number = number;
        this.role = role;
        this.height = height;
    }

    Player(Name name, int number, String role, int age, int height) {
        this(name, number, Role.valueOf(role), age, height);
    }

    static void initLocation(int width, int height, boolean leftSide, Player[] mainPlayers, Player[] benchPlayers) {
        Map<Role, Integer> counts = new HashMap<>();
        Map<Role, Integer> indexes = new HashMap<>();
        Arrays.stream(mainPlayers).forEach(e -> {
            counts.put(e.role, counts.getOrDefault(e.role, 0) + 1);
            indexes.put(e.role, 0);
        });

        for (Player player : mainPlayers) {
            Integer total = counts.get(player.role);
            Integer index = indexes.get(player.role);
            player.initLocation(width, height, index++, total, leftSide);
            indexes.put(player.role, index);
        }

        int index = 0;
        for (Player player : benchPlayers) {
            if (player != null) {
                int x = index++ * 80;
                player.setLocation(new Location(leftSide ? x : (width - x - 40), height));
            }
        }
    }

    private void initLocation(int width, int height, int index, int total, boolean leftSide) {
        switch (role) {
            case GOALKEEPER:
                int yg = height / 2 - Constants.ROLE_SIZE / 2;
                this.setLocation(leftSide ? new Location(0, yg) : new Location(width - Constants.ROLE_SIZE, yg));
                break;
            case DEFENDER:
                int yd = 60 + (height - 120 - Constants.ROLE_SIZE) * index / (total - 1);
                this.setLocation(leftSide ? new Location(width / 6, yd) : new Location(width - width / 6 - Constants.ROLE_SIZE, yd));
                break;
            case MIDFIELDER:
                int ym = 80 + (height - 160 - Constants.ROLE_SIZE) * index / (total - 1);
                this.setLocation(leftSide ? new Location(width / 3, ym) : new Location(width - width / 3 - Constants.ROLE_SIZE, ym));
                break;
            case FORWARD:
                int yf = 180 + (height - 360 - Constants.ROLE_SIZE) * index / (total - 1);
                this.setLocation(leftSide ? new Location(width / 2 - 60, yf) : new Location(width / 2 + 20, yf));
                break;
        }
    }

    void shoot(Ball ball) {
        Tools.playAudio("kick-" + (1 + new Random().nextInt(2)) + ".wav");

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s shoots the ball and it moves from %s to ",
            this.getName(), ball.getLocation()));

        ball.getLocation().move(-80 + new Random().nextInt(101), -60 + new Random().nextInt(81), false);

        sb.append(ball.getLocation().toString());
        if (Tools.DEBUG)
            System.out.println(sb);
    }

    void pass(Ball ball) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s passes the ball and it moves from %s to ",
            this.getName(), ball.getLocation()));

        ball.getLocation().move(-60 + new Random().nextInt(81), -48 + new Random().nextInt(69), false);

        sb.append(ball.getLocation().toString());
        if (Tools.DEBUG)
            System.out.println(sb);
    }

    void run() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s runs with %s speed from %s to ", this.getName(), this.speed, this.getLocation()));

        // restrict goal keeper, or it will be a total joke
        if (this.role == Role.GOALKEEPER) {
            int delta = new Random().nextInt(165 - Constants.ROLE_SIZE / 2);
            Location location = this.getLocation();
            this.setLocation(new Location(location.x < Constants.FIELD_WIDTH / 2 ? delta : (Constants.FIELD_WIDTH - delta),
                Constants.FIELD_HEIGHT / 2 + (new Random().nextBoolean() ? new Random().nextInt(40) : -new Random().nextInt(40))));
        } else {
            this.getLocation().move(-20 + new Random().nextInt(this.speed.value),
                -15 + new Random().nextInt(this.speed.value));
        }

        sb.append(this.getLocation());
        if (Tools.DEBUG)
            System.out.println(sb);
    }

    @Override
    public void draw(Graphics g) {
        Color color = g.getColor();

        Location location = this.getLocation();
        int delta = this.height > 185 ? 4 : 0;
        if (this.role == Role.GOALKEEPER) {
            g.fill3DRect(location.x, location.y, Constants.ROLE_SIZE, Constants.ROLE_SIZE / 2, true);
            g.setColor(Color.YELLOW);
            g.fill3DRect(location.x, location.y + Constants.ROLE_SIZE / 2, Constants.ROLE_SIZE, Constants.ROLE_SIZE / 2 + delta, true);
        } else {
            g.fill3DRect(location.x, location.y, Constants.ROLE_SIZE, Constants.ROLE_SIZE + delta, true);
        }

        g.setColor(color == Color.WHITE ? Color.BLACK : Color.WHITE);
        g.drawString(this.getName().getFirst().substring(0, 1),
            location.x + Constants.ROLE_SIZE / 2, location.y + 15);
        g.drawString(String.valueOf(this.number),
            location.x + Constants.ROLE_SIZE / 2 - (number > 10 ? 4 : 0), location.y + 34);
        g.setColor(color);
    }

    public static void main(String[] args) {
        Player ronaldo = new Player(new Name("Cristiano", "Ronaldo "), 7, Role.FORWARD, 33, 185);
        ronaldo.speed = Speed.SUPERFAST;
        Ball ball = new Ball(5, Color.WHITE, new Location(53, 34));
        ronaldo.shoot(ball);
        Tools.sleepSilently(2000);

        Player muller = new Player(new Name("Thomas", "Muller "), 14, Role.MIDFIELDER, 24, 186);
        muller.speed = Speed.FAST;
        muller.pass(ball);
    }
}
