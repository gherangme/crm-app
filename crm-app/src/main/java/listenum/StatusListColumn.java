package listenum;

public enum StatusListColumn {

    UNDO(1),
    DOING(2),
    DONE(3);

    private int value;

    StatusListColumn(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
