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
}
