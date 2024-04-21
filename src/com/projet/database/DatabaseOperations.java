package com.projet.database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import org.mindrot.jbcrypt.BCrypt;

import com.projet.main.Dashboard;
import com.projet.metier.Employee;
import com.projet.metier.Project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DatabaseOperations {
	// JDBC connection parameters (replace with your database details)
	private static String JDBC_URL = "jdbc:mysql://localhost:3306/auth";
	private static String DB_USER = "root";
	private static String DB_PASSWORD = "randomR@@T1050";
	
	public static boolean authenticateUser(TextField usernameField, PasswordField passwordField) {
		// Implement your authentication logic using JDBC and BCrypt for password
		// hashing
		String username = usernameField.getText();
		String enteredPassword = passwordField.getText();

		try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
			String query = "SELECT * FROM users WHERE username = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, username);
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						String storedHashedPassword = resultSet.getString("hashed_password"); // Replace with your column name

						// Compare the hashed passwords using BCrypt
						if (BCrypt.checkpw(enteredPassword, storedHashedPassword)) {
							return true;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception appropriately
		}
		return false;
	}

	private static String hashPassword(String password) {
		// Use BCrypt to hash the password
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public static void insertUser(String username, String password) {
		try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
			String sql = "INSERT INTO users (username, hashed_password) VALUES (?, ?)";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setString(1, username);
				statement.setString(2, hashPassword(password));
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static ObservableList<Employee> getData() {
	    ObservableList<Employee> employees = FXCollections.observableArrayList();

	    try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
	    	String query = "SELECT e.*, l.name AS location_name, d.name AS department_name, p.name AS position_name " +
	                "FROM employee e " +
	                "JOIN location l ON e.location_id = l.id " +
	                "JOIN department d ON e.department_id = d.id " +
	                "JOIN positions p ON e.position_id = p.id";
	    	
	        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                while (resultSet.next()) {
	                    int id = resultSet.getInt("id");
	                    String firstName = resultSet.getString("first_name");
	                    String lastName = resultSet.getString("last_name");
	                    String position = resultSet.getString("position_name");
	                    String phoneNumber = resultSet.getString("phone_number");
	                    String email = resultSet.getString("email");
	                    int monthlySalary = resultSet.getInt("monthly_salary");
	                    LocalDate recruitmentDate = resultSet.getDate("recruitment_date").toLocalDate();
	                    int contractDuration = resultSet.getInt("contract_duration");
	                    String location = resultSet.getString("location_name");  // Assuming the column name in the locations table is location_name
	                    String department = resultSet.getString("department_name");  // Assuming the column name in the departments table is department_name

	                    Employee employee = new Employee(id, firstName, lastName, position, department, email,
	                            phoneNumber, location, monthlySalary, recruitmentDate, contractDuration);

	                    employees.add(employee);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Handle the exception appropriately
	    }

	    return employees;
	}
	
	public static void saveData(TableView<Employee> table) {
	    // Get the modified data from the TableView
	    ObservableList<Employee> modifiedData = table.getItems();

	    try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
	        // Clear existing data in the employee table (you may want to handle this differently based on your requirements)
	        clearEmployeeTable(connection);

	        // Insert the modified data into the employee table
	        insertEmployees(connection, modifiedData);

	    } catch (SQLException e) {
	        e.printStackTrace(); // Handle the exception appropriately
	        // Add additional logging or error handling if needed
	    }

	    // Update the global variable with the modified data
	    Dashboard.data.setAll(modifiedData);
	    table.refresh();
	}

	private static void clearEmployeeTable(Connection connection) throws SQLException {
	    try (Statement clearStatement = connection.createStatement()) {
	        clearStatement.executeUpdate("DELETE FROM employee");
	    }
	}

	private static void insertEmployees(Connection connection, ObservableList<Employee> employees) throws SQLException {
	    try (PreparedStatement insertStatement = connection.prepareStatement(
	            "INSERT INTO employee (id, first_name, last_name, position_id, department_id, phone_number, " +
	                    "email, monthly_salary, recruitment_date, location_id, contract_duration) " +
	                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

	        for (Employee employee : employees) {
	            // Get position_id from positions table based on name
	            int positionId = getPositionId(connection, employee.getPosition());
	            // Get department_id from department table based on name
	            int departmentId = getDepartmentId(connection, employee.getDepartment());
	            // Get location_id from location table based on name
	            int locationId = getLocationId(connection, employee.getLocation());

	            insertStatement.setInt(1, employee.getId());
	            insertStatement.setString(2, employee.getFirstName());
	            insertStatement.setString(3, employee.getLastName());
	            insertStatement.setInt(4, positionId);
	            insertStatement.setInt(5, departmentId);
	            insertStatement.setString(6, employee.getPhoneNumber());
	            insertStatement.setString(7, employee.getEmail());
	            insertStatement.setInt(8, employee.getMonthlySalary());
	            insertStatement.setDate(9, Date.valueOf(employee.getRecruitmentDate()));
	            insertStatement.setInt(10, locationId);
	            insertStatement.setInt(11, employee.getContractDuration());

	            insertStatement.addBatch();  // Add batch for efficient execution
	        }

	        // Execute batch insert
	        insertStatement.executeBatch();
	    }
	}

	// Method to get position_id from positions table based on name
	private static int getPositionId(Connection connection, String positionName) throws SQLException {
	    String query = "SELECT id FROM positions WHERE name = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setString(1, positionName);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) {
	                return resultSet.getInt("id");
	            }
	        }
	    }
	    throw new SQLException("Position not found: " + positionName);
	}

	// Method to get department_id from department table based on name
	private static int getDepartmentId(Connection connection, String departmentName) throws SQLException {
	    String query = "SELECT id FROM department WHERE name = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setString(1, departmentName);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) {
	                return resultSet.getInt("id");
	            }
	        }
	    }
	    throw new SQLException("Department not found: " + departmentName);
	}

	// Method to get location_id from location table based on name
	private static int getLocationId(Connection connection, String locationName) throws SQLException {
	    String query = "SELECT id FROM location WHERE name = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setString(1, locationName);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) {
	                return resultSet.getInt("id");
	            }
	        }
	    }
	    throw new SQLException("Location not found: " + locationName);
	}
	
	
	public static ObservableList<Project> getProjects() {
	    ObservableList<Project> projects = FXCollections.observableArrayList();

	    try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
	        String query = "SELECT * FROM projects";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                while (resultSet.next()) {
	                    String projectName = resultSet.getString("name");
	                    String projectStatus = resultSet.getString("status");
	                    String projectWorkers = resultSet.getString("workers");
	                    int projectDuration = resultSet.getInt("duration_months");

	                    Project project = new Project(projectName, projectStatus, projectWorkers, projectDuration);
	                    projects.add(project);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Handle the exception appropriately
	    }

	    return projects;
	}

	public static void saveProjects(TableView<Project> table) {
	    // Get the modified data from the TableView
	    ObservableList<Project> modifiedData = table.getItems();

	    try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
	        // Clear existing data in the projects table (you may want to handle this differently based on your requirements)
	        clearProjectsTable(connection);

	        // Insert the modified data into the projects table
	        insertProjects(connection, modifiedData);

	    } catch (SQLException e) {
	        e.printStackTrace(); // Handle the exception appropriately
	        // Add additional logging or error handling if needed
	    }

	    // Update the global variable with the modified data
	    Dashboard.projectsData.setAll(modifiedData);
	    table.refresh();
	}

	private static void clearProjectsTable(Connection connection) throws SQLException {
	    try (Statement clearStatement = connection.createStatement()) {
	        clearStatement.executeUpdate("DELETE FROM projects");
	    }
	}

	private static void insertProjects(Connection connection, ObservableList<Project> projects) throws SQLException {
	    try (PreparedStatement insertStatement = connection.prepareStatement(
	            "INSERT INTO projects (name, status, workers, duration_months) VALUES (?, ?, ?, ?)")) {

	        for (Project project : projects) {
	            insertStatement.setString(1, project.getName());
	            insertStatement.setString(2, project.getStatus());
	            insertStatement.setString(3, project.getWorkers());
	            insertStatement.setInt(4, project.getDurationInMonths());

	            insertStatement.addBatch();  // Add batch for efficient execution
	        }

	        // Execute batch insert
	        insertStatement.executeBatch();
	    }
	}
}