package com.trishul.util;


public enum LicenceType {
    LEARNING_LL("Learning Licence (LL)", "Learning", "badge-info"),
    PERMANENT_DL("Permanent Driving Licence (DL)", "DL", "badge-success"),
    RENEWAL("Licence Renewal", "Renewal", "badge-warning"),
    DUPLICATE("Duplicate Licence", "Duplicate", "badge-danger"),
    INTERNATIONAL("International Driving Permit", "International", "badge-primary"),
    ADD_REMOVE("Licence Add/Remove Classes", "Add/Remove", "badge-secondary");

    private final String displayName;
    private final String shortName;
    private final String badgeClass;

    LicenceType(String displayName, String shortName, String badgeClass) {
        this.displayName = displayName;
        this.shortName = shortName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    // Helper method to get enum from display name
    public static LicenceType fromDisplayName(String displayName) {
        for (LicenceType type : values()) {
            if (type.getDisplayName().equals(displayName)) {
                return type;
            }
        }
        return null;
    }

    // Get all display names for dropdown
    public static String[] getAllDisplayNames() {
        LicenceType[] types = values();
        String[] names = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            names[i] = types[i].getDisplayName();
        }
        return names;
    }
}