public enum Tags {
    FIRST_MATRIX(0),
    FIRST_ROWS_NUM(1),
    SECOND_MATRIX(2),
    RESULT(3);

    private final int value;

    Tags(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}