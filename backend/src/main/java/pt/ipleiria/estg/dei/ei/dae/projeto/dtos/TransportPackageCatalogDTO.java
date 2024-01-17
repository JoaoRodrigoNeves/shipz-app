package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

public class TransportPackageCatalogDTO {
    long code;
    String name;
    String material;
    long volume;

    public TransportPackageCatalogDTO(long code, String name, String material, long volume) {
        this.code = code;
        this.name = name;
        this.material = material;
        this.volume = volume;
    }

    public long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }
}
