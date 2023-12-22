package pt.ipleiria.estg.dei.ei.dae.projeto.entities.types;

public enum UserType {
    PRODUCT_MANUFACTER("Product Manufacter"),
    LOGISTICS_OPERATOR("Logistics Operator"),
    FINAL_COSTUMER("Final Costumer");
    private final String userType;

    UserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }
}