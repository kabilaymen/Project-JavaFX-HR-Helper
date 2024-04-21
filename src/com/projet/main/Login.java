package com.projet.main;

import static com.projet.main.Main.*;

import com.projet.database.DatabaseOperations;

import static com.projet.main.Dashboard.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Login {
	public static Scene loginScene;
	private static TextField usernameField;
	private static PasswordField passwordField;
	
	public static void showLoginScene(Stage primaryStage) {
		// Create the main layout
		root = new BorderPane();
		root.setId("root");

		// Create a VBox to hold the login UI elements
		VBox loginBox = new VBox(20);
		loginBox.setAlignment(Pos.CENTER);
		loginBox.setPadding(new Insets(20));
		loginBox.setStyle("-fx-background-color: #333;"); // Background color example

		Label loginLabel = new Label("Login to Dashboard");
		loginLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: white;"); // Title styling

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setAlignment(Pos.CENTER); // Center the GridPane within the VBox

		Label usernameLabel = new Label("Username:");
		usernameLabel.setStyle("-fx-font-size: 14; -fx-text-fill: white;"); // Label styling

		usernameField = new TextField();
		usernameField.setPromptText("Enter your username");
		usernameField.setStyle("-fx-font-size: 14; -fx-background-color: #eee; -fx-text-fill: black;"); // Input field styling

		Label passwordLabel = new Label("Password:");
		passwordLabel.setStyle("-fx-font-size: 14; -fx-text-fill: white;"); // Label styling

		passwordField = new PasswordField();
		passwordField.setPromptText("Enter your password");
		passwordField.setStyle("-fx-font-size: 14; -fx-background-color: #eee; -fx-text-fill: black;"); // Input field styling

		Button loginButton = new Button("Login");
		loginButton.setStyle("-fx-font-size: 16; -fx-background-color: #555; -fx-text-fill: white;"); // Button styling

		// Add the UI elements to the GridPane
		gridPane.addRow(0, usernameLabel, usernameField);
		gridPane.addRow(1, passwordLabel, passwordField);

		loginButton.setOnAction(event -> {
			if (DatabaseOperations.authenticateUser(usernameField, passwordField)) {
				// Authentication successful, show the dashboard or perform other actions
				System.out.println("Authentication successful. Redirecting to the dashboard...");

				showDashboardScene(primaryStage);
			} else {
				// Authentication failed, show an error message or allow another attempt
				showAlert(Alert.AlertType.ERROR, "Authentication Failed",
						"Invalid username or password. Please try again.");
			}
		});

		// Add the UI elements to the VBox
		loginBox.getChildren().addAll(loginLabel, new Separator(), gridPane, new Separator(), loginButton);

		// Set the login scene
		root.setCenter(loginBox);

		// Set up the scene
		loginScene = new Scene(root, 440, 280);
		loginScene.getStylesheets().add(new Main().getClass().getResource("/style.css").toExternalForm());

		primaryStage.setScene(loginScene);
	    primaryStage.centerOnScreen();
	}

	private static void showAlert(Alert.AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public static void showLogoutConfirmation(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log Out");
        alert.setHeaderText("Confirm Log Out");
        alert.setContentText("Are you sure you want to log out?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Perform log-out actions
                System.out.println("Logging out...");
                usernameField.setText("");
                passwordField.setText("");

                // Transition to the login scene
                primaryStage.setScene(loginScene);
                primaryStage.centerOnScreen();
            }
        });
    }
}
