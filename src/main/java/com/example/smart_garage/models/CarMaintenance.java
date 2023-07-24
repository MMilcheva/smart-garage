package com.example.smart_garage.models;


import jakarta.persistence.*;

@Entity
@Table(name = "car_maintenances")
public class CarMaintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_maintenance_id")
    private Long carMaintenanceId;

    @Column(name = "car_maintenance_name")
    private String carMaintenanceName;

    @Column(name = "isArchived")
    private Boolean isArchived = false;

    public CarMaintenance() {
    }

    public Long getCarMaintenanceId() {
        return carMaintenanceId;
    }

    public void setCarMaintenanceId(Long carMaintenanceId) {
        this.carMaintenanceId = carMaintenanceId;
    }

    public String getCarMaintenanceName() {
        return carMaintenanceName;
    }

    public void setCarMaintenanceName(String carMaintenanceName) {
        this.carMaintenanceName = carMaintenanceName;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }
}
