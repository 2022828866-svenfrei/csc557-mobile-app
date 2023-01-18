package my.edu.uitm.friendmanagement.models;

public enum Gender {
    UNKNOWN("Unknown", 0),
    MALE("Male", 1),
    FEMALE("Female", 2);

    private String friendlyName;
    private int index;

    private Gender(String friendlyName, int index) {
        this.friendlyName = friendlyName;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return friendlyName;
    }

    public static Gender parse(String v) {
        switch (v) {
            case "Male":
                return MALE;
            case "Female":
                return FEMALE;
            default:
                return UNKNOWN;
        }
    }
}
