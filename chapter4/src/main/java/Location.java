/**
 * Location in the soccer field, such as each player, the ball, the referee and linesmen
 */
class Location {
    int x;

    int y;

    Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void move(int offsetX, int offsetY) {
        this.move(offsetX, offsetY, true);
    }

    void move(int offsetX, int offsetY, boolean checkBound) {
        this.x += offsetX;
        this.y += offsetY;

        if (checkBound)
            this.handleOutOfBound();
    }

    void handleOutOfBound() {
        if (this.x <= 0 || this.x >= Constants.FIELD_WIDTH - Constants.ROLE_SIZE)
            x = Tools.nextInt(Constants.FIELD_WIDTH);

        if (this.y <= 0 || this.y >= Constants.FIELD_HEIGHT - Constants.ROLE_SIZE)
            y = Math.abs(Tools.nextInt(Constants.FIELD_HEIGHT) - Constants.ROLE_SIZE);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
