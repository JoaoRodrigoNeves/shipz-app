package pt.ipleiria.estg.dei.ei.dae.projeto.entities.types;

public enum PackageType {
    PRIMARY("Primário"),
    SECONDARY("Secundário"),
    TERTIARY("Terciário"),
    TRANSPORT("Transporte");
    private final String packageType;

    PackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getPackageType() {
        return packageType;
    }

    public static PackageType fromString(String text) {
        for (PackageType packageType : PackageType.values()) {
            if (packageType.packageType.equalsIgnoreCase(text)) {
                return packageType;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
