import java.awt.*;

class Coach implements Drawable {

    private Name name;

    private int age;

    private int champions;

    private Location location;

    Coach(Name name, int age, int champions) {
        this.name = name;
        this.age = age;
        this.champions = champions;
    }

    Location getLocation() {
        return location;
    }

    void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void draw(Graphics g) {
        g.fill3DRect(location.x, location.y, Constants.ROLE_SIZE, Constants.ROLE_SIZE, true);
        g.setColor(Color.WHITE);
        g.drawString(this.name.getFirst().substring(0, 1),
            location.x + Constants.ROLE_SIZE / 2, location.y + Constants.ROLE_SIZE / 2);
    }
}
