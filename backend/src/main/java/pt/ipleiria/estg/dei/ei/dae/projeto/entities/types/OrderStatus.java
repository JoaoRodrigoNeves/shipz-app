package pt.ipleiria.estg.dei.ei.dae.projeto.entities.types;
public enum OrderStatus {
    STATUS_0("Estado Inicial"),
    STATUS_1("Em Processamento"),
    STATUS_2("Enviada"),
    STATUS_3("Recebida");
    private final String orderStatus;

    OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public static OrderStatus fromString(String text) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.orderStatus.equalsIgnoreCase(text)) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
