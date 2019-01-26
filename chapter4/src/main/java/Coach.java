/*
* This is a personal academic project. Dear PVS-Studio, please check it.
* PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
*/
import java.awt.*;

class Coach extends Person {

    private int champions;

    Coach(Name name, int age, int champions) {
        super(name, age);
        this.champions = champions;
    }

    @Override
    public void draw(Graphics g) {
        Location location = this.getLocation();
        g.fill3DRect(location.x, location.y, Constants.ROLE_SIZE, Constants.ROLE_SIZE, true);
        g.setColor(Color.WHITE);
        g.drawString(this.getName().getFirst().substring(0, 1),
            location.x + Constants.ROLE_SIZE / 2, location.y + Constants.ROLE_SIZE / 2);
    }

    @Override
    public String toString() {
        return String.format("%s, %d years old, %d champions won.", this.getName(), this.getAge(), this.champions);
    }
}
