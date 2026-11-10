package com.inframanager.equipment;

public class Equipment {
    private Long id;
    private String name;
    private String manufacturer;
    private String model;
    private String ipAddress;

    public Equipment(String name, String manufacturer, String model) {
        validateName(name);
        validateManufacturer(manufacturer);
        validateModel(model);
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Le nom de l'équipement est obligatoire");
        }
    }

    private void validateManufacturer(String manufacturer) {
        if (manufacturer == null || manufacturer.isBlank()) {
            throw new IllegalArgumentException("le nom du fabricant est obligatoire");
        }
    }

    private void validateModel(String model) {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("le nom du modèle est obligatoire");
        }
    }

    private boolean validateIpAddress(String ipAddress) {
        if (ipAddress == null) {
            return true;
        }

        String[] parties = ipAddress.split("\\.");

        if (parties.length != 4) {
            return false;
        }

        for (String partie : parties) {
            try {
                int nombre = Integer.parseInt(partie);

                if (nombre < 0 || nombre > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }

    public void setName(String newName) {
        validateName(newName);
        this.name = newName;
    }

    public void setManufacturer(String newManufacturer) {
        validateManufacturer(newManufacturer);
        this.manufacturer = newManufacturer;
    }

    public void setModel(String newModel) {
        validateModel(newModel);
        this.model = newModel;
    }

    public void setIpAddress(String newIpAddress) {
        if (!validateIpAddress(newIpAddress)) {
            throw new IllegalArgumentException("Adresse IP invalide");
        }
        this.ipAddress = newIpAddress;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}