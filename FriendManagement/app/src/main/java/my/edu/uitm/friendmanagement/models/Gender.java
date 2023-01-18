package my.edu.uitm.friendmanagement.models;

public enum Gender {
    UNKNOWN("Unknown"),
    MALE("Male"),
    FEMALE("Female");

    private String friendlyName;

    private Gender(String friendlyName) {
        this.friendlyName = friendlyName;
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
