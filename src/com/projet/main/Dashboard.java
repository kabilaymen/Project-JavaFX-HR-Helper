package com.projet.main;

import static com.projet.main.Main.root;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.projet.main.Login.*;

import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import com.projet.database.DatabaseOperations;
import com.projet.metier.Employee;
import com.projet.metier.Project;
import com.projet.utilities.DatePickerTableCell;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class Dashboard {
	public static Scene dashboardScene;
	private static VBox contentArea;
	public static ObservableList<Employee> data = DatabaseOperations.getData();
	public static ObservableList<Project> projectsData = DatabaseOperations.getProjects();

	public static void showDashboardScene(Stage primaryStage) {
		// Add code to transition to the dashboard or other application functionality.
		root = new BorderPane();
		root.setId("root");

		// Create the header
		HBox header = createHeader();
		root.setTop(header);

		// Create the sidebar
		VBox sidebar = createSidebar();
		root.setLeft(sidebar);

		Button logoutButton = new Button("Log Out");
		logoutButton.getStyleClass().add("logout-button"); // Add a CSS class for styling
		logoutButton.setOnAction(event -> showLogoutConfirmation(primaryStage));
		logoutButton.setMaxWidth(Double.MAX_VALUE);

		// Center the button using alignment and spacing
		VBox centeringContainer = new VBox(logoutButton);
		centeringContainer.setAlignment(Pos.BOTTOM_CENTER);
		VBox.setVgrow(centeringContainer, Priority.ALWAYS);
		sidebar.getChildren().addAll(centeringContainer);

		// Create the main content area with an employee table
		contentArea = createContentArea();

		// Add initial content (a label in this example)
		Label initialLabel = new Label("Welcome to the Dashboard!");
		initialLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
		contentArea.getChildren().add(initialLabel);

		root.setCenter(contentArea);

		dashboardScene = new Scene(root, 1000, 600);
		dashboardScene.getStylesheets().add(new Main().getClass().getResource("/style.css").toExternalForm());

		primaryStage.setScene(dashboardScene);
		primaryStage.centerOnScreen();
	}

	private static HBox createHeader() {
		HBox header = new HBox();
		header.setPadding(new Insets(15));
		header.setAlignment(Pos.CENTER);
		header.setId("header");

		Label companyName = new Label("Dashboard");
		companyName.setTextFill(Color.WHITE);

		header.getChildren().add(companyName);
		return header;
	}

	private static VBox createSidebar() {
		VBox sidebar = new VBox();
		sidebar.setId("sidebar");

		// Create buttons for various functionalities
		Button viewEmployeesButton = createMenuButton("View Employees", "View Employees Content");
		Button manageDepartmentsButton = createMenuButton("Manage Departments", "Manage Departments Content");
		Button analyzeDataButton = createMenuButton("Analyze Data", "Analyze Data Content");
		Button settingsButton = createMenuButton("Projects", "Project List Content");

		sidebar.getChildren().addAll(viewEmployeesButton, manageDepartmentsButton, analyzeDataButton, settingsButton);
		return sidebar;
	}

	private static Button createMenuButton(String text, String content) {
		Button button = new Button(text);
		button.getStyleClass().add("menu-button");
		button.setMaxWidth(Double.MAX_VALUE);

		// Add button actions or scene changes as needed
		button.setOnAction(event -> showContent(content));

		return button;
	}

	private static VBox createContentArea() {
		VBox contentArea = new VBox(20);
		contentArea.setPadding(new Insets(20));
		contentArea.setAlignment(Pos.CENTER);
		contentArea.setId("content-area");

		/*
		 * // Create employee table table = createEmployeeTable();
		 * contentArea.getChildren().add(table);
		 */

		return contentArea;
	}

	@SuppressWarnings("unchecked")
	private static TableView<Employee> createEmployeeTable() {
		data = DatabaseOperations.getData();
		TableView<Employee> table = new TableView<>();

		TableColumn<Employee, Integer> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());

		TableColumn<Employee, String> firstNameColumn = new TableColumn<>("First Name");
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().getFirstNameProperty());

		TableColumn<Employee, String> lastNameColumn = new TableColumn<>("Last Name");
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().getLastNameProperty());

		TableColumn<Employee, String> positionColumn = new TableColumn<>("Position");
		positionColumn.setCellValueFactory(cellData -> cellData.getValue().getPositionProperty());

		TableColumn<Employee, String> departmentColumn = new TableColumn<>("Department");
		departmentColumn.setCellValueFactory(cellData -> cellData.getValue().getDepartmentProperty());

		TableColumn<Employee, String> emailColumn = new TableColumn<>("Email");
		emailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());

		TableColumn<Employee, String> phoneNumberColumn = new TableColumn<>("Phone Number");
		phoneNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getPhoneNumberProperty());

		TableColumn<Employee, String> locationColumn = new TableColumn<>("Location");
		locationColumn.setCellValueFactory(cellData -> cellData.getValue().getLocationProperty());

		// Additional columns for monthlySalary, recruitmentDate, and contractDuration
		TableColumn<Employee, Integer> monthlySalaryColumn = new TableColumn<>("Monthly Salary");
		monthlySalaryColumn.setCellValueFactory(cellData -> cellData.getValue().getMonthlySalaryProperty().asObject());

		TableColumn<Employee, LocalDate> recruitmentDateColumn = new TableColumn<>("Recruitment Date");
		recruitmentDateColumn.setCellValueFactory(cellData -> cellData.getValue().getRecruitmentDateProperty());

		TableColumn<Employee, Integer> contractDurationColumn = new TableColumn<>("Contract Duration");
		contractDurationColumn
				.setCellValueFactory(cellData -> cellData.getValue().getContractDurationProperty().asObject());

		table.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, positionColumn, departmentColumn,
				emailColumn, phoneNumberColumn, locationColumn, monthlySalaryColumn, recruitmentDateColumn,
				contractDurationColumn);

		table.setItems(data);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		return table;
	}

	private static void showContent(String content) {
		Node selectedContent = getFunctionality(content);
		if (selectedContent != null) {
			root.setCenter(selectedContent);
		} else {
			System.out.println("Functionality not implemented yet.");
		}
	}

	private static Node getFunctionality(String functionality) {
		switch (functionality) {
		case "View Employees Content":
			return createViewEmployeesContent();
		case "Manage Departments Content":
			return createEditEmployeesContent();
		case "Analyze Data Content":
			return createAnalyzeDataContent();
		case "Project List Content":
			return createProjectListContent();
		default:
			return null;
		}
	}

	private static Node createViewEmployeesContent() {
		TableView<Employee> table = createEmployeeTable();

		// Create a label, choice box, and text field for filtering employees
		Label filterLabel = new Label("Filter by:");
		ChoiceBox<String> filterChoiceBox = new ChoiceBox<>();
		filterChoiceBox.getItems().addAll("First Name", "Last Name", "Position", "Department", "Email", "Phone Number",
				"Location");
		filterChoiceBox.setValue("First Name"); // Set default value

		TextField filterField = new TextField();
		filterField.setPromptText("Enter filter value");

		// Create a FilteredList to apply the filter to the employee data
		FilteredList<Employee> filteredData = new FilteredList<>(table.getItems());

		// Create a SortedList to apply sorting to the filtered data
		SortedList<Employee> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(table.comparatorProperty());

		// Bind the text property of the filter field to update the filter predicate
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(employee -> {
				if (newValue == null || newValue.trim().isEmpty()) {
					return true; // Show all employees if the filter is empty
				}

				String lowerCaseFilter = newValue.toLowerCase();
				String selectedField = filterChoiceBox.getValue().toLowerCase();

				switch (selectedField) {
				case "first name":
					return employee.getFirstName().toLowerCase().contains(lowerCaseFilter);
				case "last name":
					return employee.getLastName().toLowerCase().contains(lowerCaseFilter);
				case "position":
					return employee.getPosition().toLowerCase().contains(lowerCaseFilter);
				case "department":
					return employee.getDepartment().toLowerCase().contains(lowerCaseFilter);
				case "email":
					return employee.getEmail().toLowerCase().contains(lowerCaseFilter);
				case "phone number":
					return employee.getPhoneNumber().toLowerCase().contains(lowerCaseFilter);
				case "location":
					return employee.getLocation().toLowerCase().contains(lowerCaseFilter);
				default:
					return false; // Unknown field
				}
			});
		});

		// Set the filtered data to the sorted data, and then set the sorted data to the
		// employee table
		table.setItems(sortedData);

		// Add the label, choice box, and filter field to the content area
		contentArea.getChildren().clear();
		contentArea.getChildren().addAll(filterLabel, filterChoiceBox, filterField, table);

		return contentArea;
	}

	public static Node createEditEmployeesContent() {
		TableView<Employee> table = createEmployeeTableForEditing();

		// Create a label, choice box, and text field for filtering employees
		Label filterLabel = new Label("Filter by:");
		ChoiceBox<String> filterChoiceBox = new ChoiceBox<>();
		filterChoiceBox.getItems().addAll("First Name", "Last Name", "Position", "Department", "Email", "Phone Number",
				"Location");
		filterChoiceBox.setValue("First Name"); // Set default value

		TextField filterField = new TextField();
		filterField.setPromptText("Enter filter value");

		// Create a FilteredList to apply the filter to the employee data
		FilteredList<Employee> filteredData = new FilteredList<>(table.getItems());

		// Create a SortedList to apply sorting to the filtered data
		SortedList<Employee> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(table.comparatorProperty());

		// Bind the text property of the filter field to update the filter predicate
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(employee -> {
				if (newValue == null || newValue.trim().isEmpty()) {
					return true; // Show all employees if the filter is empty
				}

				String lowerCaseFilter = newValue.toLowerCase();
				String selectedField = filterChoiceBox.getValue().toLowerCase();

				switch (selectedField) {
				case "first name":
					return employee.getFirstName().toLowerCase().contains(lowerCaseFilter);
				case "last name":
					return employee.getLastName().toLowerCase().contains(lowerCaseFilter);
				case "position":
					return employee.getPosition().toLowerCase().contains(lowerCaseFilter);
				case "department":
					return employee.getDepartment().toLowerCase().contains(lowerCaseFilter);
				case "email":
					return employee.getEmail().toLowerCase().contains(lowerCaseFilter);
				case "phone number":
					return employee.getPhoneNumber().toLowerCase().contains(lowerCaseFilter);
				case "location":
					return employee.getLocation().toLowerCase().contains(lowerCaseFilter);
				default:
					return false; // Unknown field
				}
			});
		});

		// Set the filtered data to the sorted data, and then set the sorted data to the
		// employee table
		table.setItems(sortedData);

		// Button for adding new entries
		Button addEntryButton = new Button("Add New Entry");
		addEntryButton.setOnAction(event -> addNewEntry());

		// Button for deleting selected entries
		Button deleteSelectedButton = new Button("Delete Selected");
		deleteSelectedButton.setOnAction(event -> deleteSelectedEntries(table));

		// Button for saving data
		Button saveDataButton = new Button("Save Data");
		saveDataButton.setOnAction(event -> DatabaseOperations.saveData(table));

		// Arrange "Add New Entry," "Delete Selected," and "Save Data" buttons
		// horizontally
		HBox buttonsBox = new HBox(10);
		buttonsBox.getChildren().addAll(addEntryButton, deleteSelectedButton, saveDataButton);

		// Center the buttons horizontally
		HBox centerBox = new HBox(buttonsBox);
		centerBox.setAlignment(Pos.CENTER);

		// Add the label, choice box, filter field, centered buttons, and employee table
		// to the content area
		contentArea.getChildren().clear();
		contentArea.getChildren().addAll(filterLabel, filterChoiceBox, filterField, centerBox, table);

		return contentArea;
	}

	private static void addNewEntry() {
		// Logic for adding a new entry
		Employee newEmployee = new Employee(data.size() + 1, "New", "Employee", "", "", "", "", "", 0, LocalDate.now(),
				0);
		data.add(newEmployee); // Add the new employee to the data collection
	}

	private static void deleteSelectedEntries(TableView<Employee> table) {
		// Logic for deleting selected entries
		ObservableList<Employee> selectedEmployees = table.getSelectionModel().getSelectedItems();
		data.removeAll(selectedEmployees); // Remove selected employees from the data collection
	}

	@SuppressWarnings("unchecked")
	private static TableView<Employee> createEmployeeTableForEditing() {
		data = DatabaseOperations.getData();
		TableView<Employee> table = new TableView<>();

		TableColumn<Employee, Integer> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());

		TableColumn<Employee, String> firstNameColumn = new TableColumn<>("First Name");
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().getFirstNameProperty());

		TableColumn<Employee, String> lastNameColumn = new TableColumn<>("Last Name");
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().getLastNameProperty());

		TableColumn<Employee, String> positionColumn = new TableColumn<>("Position");
		positionColumn.setCellValueFactory(cellData -> cellData.getValue().getPositionProperty());

		TableColumn<Employee, String> departmentColumn = new TableColumn<>("Department");
		departmentColumn.setCellValueFactory(cellData -> cellData.getValue().getDepartmentProperty());

		TableColumn<Employee, String> emailColumn = new TableColumn<>("Email");
		emailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmailProperty());

		TableColumn<Employee, String> phoneNumberColumn = new TableColumn<>("Phone Number");
		phoneNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getPhoneNumberProperty());

		TableColumn<Employee, String> locationColumn = new TableColumn<>("Location");
		locationColumn.setCellValueFactory(cellData -> cellData.getValue().getLocationProperty());

		// Additional columns for monthlySalary, recruitmentDate, and contractDuration
		TableColumn<Employee, Integer> monthlySalaryColumn = new TableColumn<>("Monthly Salary");
		monthlySalaryColumn.setCellValueFactory(cellData -> cellData.getValue().getMonthlySalaryProperty().asObject());

		TableColumn<Employee, LocalDate> recruitmentDateColumn = new TableColumn<>("Recruitment Date");
		recruitmentDateColumn.setCellValueFactory(cellData -> cellData.getValue().getRecruitmentDateProperty());

		TableColumn<Employee, Integer> contractDurationColumn = new TableColumn<>("Contract Duration");
		contractDurationColumn
				.setCellValueFactory(cellData -> cellData.getValue().getContractDurationProperty().asObject());

		table.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, positionColumn, departmentColumn,
				emailColumn, phoneNumberColumn, locationColumn, monthlySalaryColumn, recruitmentDateColumn,
				contractDurationColumn);

		// Set up cell factories for editable columns
		setEditableColumnFactory(table, firstNameColumn,
				TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
		setEditableColumnFactory(table, lastNameColumn,
				TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
		setEditableColumnFactory(table, positionColumn,
				TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
		setEditableColumnFactory(table, departmentColumn,
				TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
		setEditableColumnFactory(table, emailColumn, TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
		setEditableColumnFactory(table, phoneNumberColumn,
				TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
		setEditableColumnFactory(table, locationColumn,
				TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
		setEditableColumnFactory(table, monthlySalaryColumn,
				TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		setEditableColumnFactory(table, recruitmentDateColumn,
				cell -> new DatePickerTableCell<>(new LocalDateStringConverter()));
		setEditableColumnFactory(table, contractDurationColumn,
				TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

		table.setItems(data);

		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setEditable(true); // Add this line

		return table;
	}

	@SuppressWarnings("unchecked")
	private static <S, T> void setEditableColumnFactory(TableView<Employee> table, TableColumn<S, T> column,
			Callback<TableColumn<S, T>, TableCell<S, T>> cellFactory) {
		column.setCellFactory(cellFactory);

		column.setOnEditCommit((CellEditEvent<S, T> t) -> {
			S rowValue = t.getRowValue();
			TableColumn<S, T> editedColumn = t.getTableColumn();

			// Assuming you have a property in your data model corresponding to the edited
			// column
			if (editedColumn instanceof TableColumn<?, ?>) {
				TableColumn<S, T> typedColumn = (TableColumn<S, T>) editedColumn;
				ObservableValue<T> cellObservableValue = typedColumn.getCellObservableValue(rowValue);

				// Check if the property is a WritableValue (e.g., SimpleObjectProperty)
				if (cellObservableValue instanceof WritableValue<?>) {
					((WritableValue<T>) cellObservableValue).setValue(t.getNewValue());

					// Create a new Employee object with updated values
					Employee modified = createEmployeeAfterUpdate(rowValue, typedColumn, t.getNewValue());

					// Update the ObservableList with the modified Employee
					ObservableList<Employee> modifiableList = FXCollections.observableArrayList(table.getItems());
					modifiableList.set(table.getSelectionModel().getSelectedIndex(), modified);
					table.setItems(modifiableList);

					// Refresh the TableView to reflect the changes
					table.refresh();

				} else {
					System.err.println("Cannot set value. ObservableValue is not a WritableValue.");
				}
			}
		});
	}

	// Helper method to create a new Employee object after cell update
	private static <S, T> Employee createEmployeeAfterUpdate(S rowValue, TableColumn<S, T> editedColumn, T newValue) {
		// Assuming S is Employee type
		Employee original = (Employee) rowValue;

		// Use reflection to set the updated value for the edited attribute
		try {
			Field field = Employee.class.getDeclaredField(getAttributeNameFromColumn(editedColumn, false));
			field.setAccessible(true);
			field.set(original, newValue);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return original;
	}

	private static <S, T> String getAttributeNameFromColumn(TableColumn<S, T> column, boolean forProjects) {
		String columnName = column.getText().trim();
		if (forProjects) {

			if (column.getText().toLowerCase().equals("duration (months)"))
				return "durationInMonths";

			if (column.getText().toLowerCase().equals("project name"))
				return "name";

			return column.getText().toLowerCase();
		}

		// Split the column name by spaces
		String[] words = columnName.split(" ");

		// If there's only one word, convert it to lowercase
		if (words.length == 1) {
			return words[0].toLowerCase();
		}

		// Convert the first word to lowercase
		StringBuilder attributeName = new StringBuilder(words[0].toLowerCase());

		// Capitalize the first letter of the second word
		attributeName.append(Character.toUpperCase(words[1].charAt(0)));

		// Append the rest of the second word in lowercase
		attributeName.append(words[1].substring(1).toLowerCase());

		// If there are more words, capitalize the first letter of each subsequent word
		for (int i = 2; i < words.length; i++) {
			attributeName.append(Character.toUpperCase(words[i].charAt(0)));
			attributeName.append(words[i].substring(1).toLowerCase());
		}

		return attributeName.toString();
	}

	public static Node createAnalyzeDataContent() {
		// Create a VBox to hold the data analysis UI elements vertically
		VBox analyzeDataContent = new VBox(20);
		analyzeDataContent.setAlignment(Pos.CENTER);
		analyzeDataContent.setPadding(new Insets(20));

		// Create an HBox to hold the pie charts horizontally
		HBox pieChartsHBox = new HBox(20);
		pieChartsHBox.setAlignment(Pos.CENTER);

		// Add a pie chart for visualizing department distribution
		PieChart departmentPieChart = createPieChart("Departments Distribution", "department");

		// Add a pie chart for visualizing position distribution
		PieChart positionPieChart = createPieChart("Position Distribution", "position");

		// Add a pie chart for visualizing location distribution
		PieChart locationPieChart = createPieChart("Location Distribution", "location");

		// Set the orientation of PieCharts to horizontal
		departmentPieChart.setClockwise(false);
		positionPieChart.setClockwise(false);
		locationPieChart.setClockwise(false);

		// Add the pie charts to the HBox
		pieChartsHBox.getChildren().addAll(departmentPieChart, positionPieChart, locationPieChart);

		// Add the pie charts HBox to the main VBox
		analyzeDataContent.getChildren().add(pieChartsHBox);

		// Create a bar chart for total salaries per department
		BarChart<String, Number> salaryBarChart = createDepartmentSalaryBarChart();

		// Set max width for the bar chart
		salaryBarChart.setMaxWidth(340);

		// Add the bar chart to the main VBox
		analyzeDataContent.getChildren().add(salaryBarChart);

		return analyzeDataContent;
	}

	private static BarChart<String, Number> createDepartmentSalaryBarChart() {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();

		BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
		barChart.setTitle("Total Salaries per Department");

		// Map to store total salaries per department
		Map<String, Double> totalSalariesPerDepartment = new HashMap<>();

		// Calculate total salaries per department
		for (Employee employee : data) {
			String department = employee.getDepartment();
			double monthlySalary = employee.getMonthlySalary();
			totalSalariesPerDepartment.put(department,
					totalSalariesPerDepartment.getOrDefault(department, 0.0) + monthlySalary);
		}

		// Create a series for the bar chart
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Total Salaries");

		// Add data to the series
		totalSalariesPerDepartment.forEach((department, totalSalary) -> {
			series.getData().add(new XYChart.Data<>(department, totalSalary));
		});

		// Add the series to the chart
		barChart.getData().add(series);

		return barChart;
	}

	private static PieChart createPieChart(String title, String attribute) {
		// Calculate data distribution from the provided employee data
		ObservableList<PieChart.Data> pieChartData = calculateDataDistributionObservable(attribute);

		// Create the pie chart
		PieChart pieChart = new PieChart(pieChartData);
		pieChart.setTitle(title);

		return pieChart;
	}

	private static ObservableList<PieChart.Data> calculateDataDistributionObservable(String attribute) {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

		if (data != null && !data.isEmpty()) {
			// Use a map to store attribute counts
			Map<String, Integer> attributeCounts = new HashMap<>();

			// Count the occurrences of each attribute
			for (Employee employee : data) {
				String attributeValue = getAttributeValue(employee, attribute);

				// Use a unique key for each combination of attribute name and value
				String key = attribute + "_" + attributeValue;
				attributeCounts.put(key, attributeCounts.getOrDefault(key, 0) + 1);
			}

			// Convert the map entries to PieChart.Data
			attributeCounts.forEach((key, count) -> {
				String[] parts = key.split("_");
				String attributeValue = parts[1];

				PieChart.Data data = new PieChart.Data(attributeValue, count);
				pieChartData.add(data);
			});
		}

		return pieChartData;
	}

	// Helper method to get attribute value based on attribute name
	private static String getAttributeValue(Employee employee, String attribute) {
		switch (attribute) {
		case "department":
			return employee.getDepartment();
		case "position":
			return employee.getPosition();
		case "location":
			return employee.getLocation();
		// Add more cases for other attributes as needed
		default:
			throw new IllegalArgumentException("Invalid attribute: " + attribute);
		}
	}

	public static Node createProjectListContent() {
		projectsData = DatabaseOperations.getProjects();

		// Create a VBox to hold the settings UI elements
		VBox content = new VBox(20);
		content.setAlignment(Pos.CENTER);
		content.setPadding(new Insets(20));

		// Create a label, choice box, and text field for filtering projects
		Label filterLabel = new Label("Filter by:");
		ChoiceBox<String> filterChoiceBox = new ChoiceBox<>();
		filterChoiceBox.getItems().addAll("Name", "Status", "Workers");
		filterChoiceBox.setValue("Name"); // Set default value

		TextField filterField = new TextField();
		filterField.setPromptText("Enter filter value");

		// Create a FilteredList to apply the filter to the project data
		FilteredList<Project> filteredData = new FilteredList<>(projectsData);

		// Create a SortedList to apply sorting to the filtered data
		SortedList<Project> sortedData = new SortedList<>(filteredData);
		TableView<Project> table = Dashboard.createProjectTable();
		sortedData.comparatorProperty().bind(table.comparatorProperty());

		// Bind the text property of the filter field to update the filter predicate
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(project -> {
				if (newValue == null || newValue.trim().isEmpty()) {
					return true; // Show all projects if the filter is empty
				}

				String lowerCaseFilter = newValue.toLowerCase();
				String selectedField = filterChoiceBox.getValue().toLowerCase();

				switch (selectedField) {
				case "name":
					return project.getName().toLowerCase().contains(lowerCaseFilter);
				case "status":
					return project.getStatus().toLowerCase().contains(lowerCaseFilter);
				case "workers":
					return project.getWorkers().toLowerCase().contains(lowerCaseFilter);
				default:
					return false; // Unknown field
				}
			});
		});

		// Set the filtered data to the sorted data, and then set the sorted data to the
		// project table
		table.setItems(sortedData);

		// Button for adding new entries
		Button addEntryButton = new Button("Add New Project");
		addEntryButton.setOnAction(event -> addNewProject());

		// Button for deleting selected entries
		Button deleteSelectedButton = new Button("Delete Selected Project");
		deleteSelectedButton.setOnAction(event -> deleteSelectedProjects(table));

		// Button for saving data
		Button saveDataButton = new Button("Save Project Data");
		saveDataButton.setOnAction(event -> DatabaseOperations.saveProjects(table));

		// Arrange "Add New Entry," "Delete Selected," and "Save Data" buttons
		// horizontally
		HBox buttonsBox = new HBox(10);
		buttonsBox.getChildren().addAll(addEntryButton, deleteSelectedButton, saveDataButton);

		// Center the buttons horizontally
		HBox centerBox = new HBox(buttonsBox);
		centerBox.setAlignment(Pos.CENTER);

		// Add the label, choice box, filter field, centered buttons, and project table
		// to the content area
		content.getChildren().clear();
		content.getChildren().addAll(filterLabel, filterChoiceBox, filterField, centerBox, table);

		return content;
	}

	private static void addNewProject() {
		// Logic for adding a new project
		Project newProject = new Project("New Project", "Ongoing", "John, Alice", 2);
		projectsData.add(newProject); // Add the new project to the data collection
	}

	private static void deleteSelectedProjects(TableView<Project> table) {
		// Logic for deleting selected projects
		ObservableList<Project> selectedProjects = table.getSelectionModel().getSelectedItems();
		projectsData.removeAll(selectedProjects); // Remove selected projects from the data collection
	}

	private static TableView<Project> createProjectTable() {
		ObservableList<Project> projects = DatabaseOperations.getProjects(); // Assuming a method like getData for
																				// projects

		TableView<Project> table = new TableView<>();

		// Define TableColumn instances for Project
		TableColumn<Project, String> nameColumn = new TableColumn<>("Project Name");
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

		TableColumn<Project, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

		TableColumn<Project, String> workersColumn = new TableColumn<>("Workers");
		workersColumn.setCellValueFactory(cellData -> cellData.getValue().workersProperty());

		TableColumn<Project, Integer> durationColumn = new TableColumn<>("Duration (Months)");
		durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationInMonthsProperty().asObject());

		// Add columns to the table
		table.getColumns().addAll(nameColumn, statusColumn, workersColumn, durationColumn);

		// Set up cell factories for editable columns
		setProjectEditableColumnFactory(table, nameColumn, TextFieldTableCell.forTableColumn());
		setProjectEditableColumnFactory(table, statusColumn, TextFieldTableCell.forTableColumn());
		setProjectEditableColumnFactory(table, workersColumn, TextFieldTableCell.forTableColumn());
		setProjectEditableColumnFactory(table, durationColumn,
				TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

		// Set data and properties for the table
		table.setItems(projects);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setEditable(true);

		return table;
	}

	private static <S, T> void setProjectEditableColumnFactory(TableView<Project> table, TableColumn<S, T> column,
			Callback<TableColumn<S, T>, TableCell<S, T>> cellFactory) {
		column.setCellFactory(cellFactory);

		column.setOnEditCommit((CellEditEvent<S, T> t) -> {
			S rowValue = t.getRowValue();
			TableColumn<S, T> editedColumn = t.getTableColumn();

			if (editedColumn instanceof TableColumn<?, ?>) {
				TableColumn<S, T> typedColumn = (TableColumn<S, T>) editedColumn;
				ObservableValue<T> cellObservableValue = typedColumn.getCellObservableValue(rowValue);

				if (cellObservableValue instanceof WritableValue<?>) {
					((WritableValue<T>) cellObservableValue).setValue(t.getNewValue());

					// Assuming a method like createProjectAfterUpdate for creating updated Project
					// object
					Project modifiedProject = createProjectAfterUpdate(rowValue, typedColumn, t.getNewValue());

					// Update the ObservableList with the modified Project
					ObservableList<Project> modifiableList = FXCollections.observableArrayList(table.getItems());
					modifiableList.set(table.getSelectionModel().getSelectedIndex(), modifiedProject);
					table.setItems(modifiableList);
				} else {
					System.err.println("Cannot set value. ObservableValue is not a WritableValue.");
				}
			}
		});
	}

//Helper method to create a new Project object after cell update
	private static <S, T> Project createProjectAfterUpdate(S rowValue, TableColumn<S, T> editedColumn, T newValue) {
		// Assuming S is Project type
		Project original = (Project) rowValue;

		// Use reflection to set the updated value for the edited attribute
		try {
			Field field = Project.class.getDeclaredField(getAttributeNameFromColumn(editedColumn, true));
			field.setAccessible(true);
			field.set(original, newValue);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return original;
	}
}
