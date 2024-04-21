package com.projet.metier;

import java.util.List;

import javafx.beans.property.*;

public class Project {
    private final String name;
    private final String status;
    // ! ici nous voulons avoir tous les employés sur le projet en noms concaténés, nous savons que normalement cela doit être une liste d'instances Employee
    private final String workers;
    private final int durationInMonths;

    // Use Property instances during initialization for non-string fields
    private final StringProperty nameProperty = new SimpleStringProperty();
    private final StringProperty statusProperty = new SimpleStringProperty();
    private final StringProperty workersProperty = new SimpleStringProperty();
    private final IntegerProperty durationInMonthsProperty = new SimpleIntegerProperty();

    public Project(String name, String status, String workers, int durationInMonths) {
        this.name = name;
        this.status = status;
        this.workers = workers;
        this.durationInMonths = durationInMonths;

        // Set initial values for Property instances
        nameProperty.set(name);
        statusProperty.set(status);
        workersProperty.set(workers);
        durationInMonthsProperty.set(durationInMonths);
    }

    public String getName() {
        return name;
    }

    public StringProperty nameProperty() {
        return nameProperty;
    }

    public String getStatus() {
        return status;
    }

    public StringProperty statusProperty() {
        return statusProperty;
    }

    public String getWorkers() {
        return workers;
    }

    public StringProperty workersProperty() {
        return workersProperty;
    }

    public int getDurationInMonths() {
        return durationInMonths;
    }

    public IntegerProperty durationInMonthsProperty() {
        return durationInMonthsProperty;
    }
}