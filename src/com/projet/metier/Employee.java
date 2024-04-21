package com.projet.metier;

import java.time.LocalDate;

import javafx.beans.property.*;

public class Employee {
	private final Integer id;
	private final String firstName;
	private final String lastName;
	private final String position;
	private final String phoneNumber;
	private final String department;
	private final String email;
	private final String location;
	private final Integer monthlySalary;
	private final LocalDate recruitmentDate;
	private final Integer contractDuration; // Assuming this represents the number of years

	// Use Property instances during initialization for non-string fields
	private final IntegerProperty idProperty = new SimpleIntegerProperty();
	private final StringProperty firstNameProperty = new SimpleStringProperty();
	private final StringProperty lastNameProperty = new SimpleStringProperty();
	private final StringProperty positionProperty = new SimpleStringProperty();
	private final StringProperty phoneNumberProperty = new SimpleStringProperty();
	private final StringProperty departmentProperty = new SimpleStringProperty();
	private final StringProperty emailProperty = new SimpleStringProperty();
	private final StringProperty locationProperty = new SimpleStringProperty();
	private final SimpleIntegerProperty monthlySalaryProperty = new SimpleIntegerProperty();
	private final ObjectProperty<LocalDate> recruitmentDateProperty = new SimpleObjectProperty<>();
	private final IntegerProperty contractDurationProperty = new SimpleIntegerProperty();

	public Employee(Integer id, String firstName, String lastName, String position, String department, String email,
			String phoneNumber, String location, Integer monthlySalary, LocalDate recruitmentDate,
			Integer contractDuration) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.position = position;
		this.phoneNumber = phoneNumber;
		this.department = department;
		this.email = email;
		this.location = location;
		this.monthlySalary = monthlySalary;
		this.recruitmentDate = recruitmentDate;
		this.contractDuration = contractDuration;

		// Set initial values for IntegerProperty and ObjectProperty instances
		idProperty.set(id);
		firstNameProperty.set(firstName);
		lastNameProperty.set(lastName);
		positionProperty.set(position);
		phoneNumberProperty.set(phoneNumber);
		departmentProperty.set(department);
		emailProperty.set(email);
		locationProperty.set(location);
		monthlySalaryProperty.set(monthlySalary);
		recruitmentDateProperty.set(recruitmentDate);
		contractDurationProperty.set(contractDuration);
	}

	public Integer getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPosition() {
		return position;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getDepartment() {
		return department;
	}

	public String getEmail() {
		return email;
	}

	public String getLocation() {
		return location;
	}

	public Integer getMonthlySalary() {
		return monthlySalary;
	}

	public LocalDate getRecruitmentDate() {
		return recruitmentDate;
	}

	public Integer getContractDuration() {
		return contractDuration;
	}

	public IntegerProperty getIdProperty() {
		return idProperty;
	}

	public StringProperty getFirstNameProperty() {
		return firstNameProperty;
	}

	public StringProperty getLastNameProperty() {
		return lastNameProperty;
	}

	public StringProperty getPositionProperty() {
		return positionProperty;
	}

	public StringProperty getPhoneNumberProperty() {
		return phoneNumberProperty;
	}

	public StringProperty getDepartmentProperty() {
		return departmentProperty;
	}

	public StringProperty getEmailProperty() {
		return emailProperty;
	}

	public StringProperty getLocationProperty() {
		return locationProperty;
	}

	public SimpleIntegerProperty getMonthlySalaryProperty() {
		return monthlySalaryProperty;
	}

	public ObjectProperty<LocalDate> getRecruitmentDateProperty() {
		return recruitmentDateProperty;
	}

	public IntegerProperty getContractDurationProperty() {
		return contractDurationProperty;
	}

}
