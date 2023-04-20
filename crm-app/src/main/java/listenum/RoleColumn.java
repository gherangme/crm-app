package listenum;

public enum RoleColumn {
    ID("id"),
    NAME("name"),
    DESCRIPTION("description");

    private String value;

    RoleColumn(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
