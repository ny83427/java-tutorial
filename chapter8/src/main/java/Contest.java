class Contest {

    private String id;

    Contest(String id) {
        this.id = id;
    }

    @Override public String toString() {
        return String.format("Contest={id: %s}", id);
    }
}
