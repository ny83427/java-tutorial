public class Field {
    /**
     * Name of the soccer field/stadium, for example:
     * San Siro, Nou Camp, Bernabeu, Old Trafford
     */
    private final String name;

    /**
     * length of the soccer field in meters
     */
    private final int length;

    /**
     * width of the soccer field in meters
     */
    private final int width;

    private final Location middle;

    public Field(String name, int length, int width) {
        this.name = name;
        this.length = length;
        this.width = width;
        this.middle = new Location(length / 2, width / 2);
    }

    /**
     * middle point, where the game begins or refresh after any goal
     */
    Location middle() {
        return this.middle;
    }
}
