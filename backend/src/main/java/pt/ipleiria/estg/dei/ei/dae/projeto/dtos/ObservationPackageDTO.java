package pt.ipleiria.estg.dei.ei.dae.projeto.dtos;

import java.util.List;

public class ObservationPackageDTO {
    long packageCode;
    String packageType;
    List<ObservationDetailDTO> observationDetailDTO;
    public ObservationPackageDTO(){

    }
    public ObservationPackageDTO(long packageCode, String packageType, List<ObservationDetailDTO> observationDetailDTO) {
        this.packageCode = packageCode;
        this.packageType = packageType;
        this.observationDetailDTO = observationDetailDTO;
    }

    public long getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(long packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public List<ObservationDetailDTO> getObservationDetailDTO() {
        return observationDetailDTO;
    }

    public void setObservationDetailDTO(List<ObservationDetailDTO> observationDetailDTO) {
        this.observationDetailDTO = observationDetailDTO;
    }
}
