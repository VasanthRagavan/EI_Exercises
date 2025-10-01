public enum Priority {
    HIGH, MEDIUM, LOW;

    public static Priority fromString(String s) {
        if (s == null) return MEDIUM;
        switch (s.trim().toLowerCase()) {
            case "high": return HIGH;
            case "low": return LOW;
            default: return MEDIUM;
        }
    }
}
