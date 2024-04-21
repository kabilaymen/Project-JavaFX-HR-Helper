package com.projet.main;

import static com.projet.main.Login.*;

import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {
	public static BorderPane root;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("HR Management Application");
		
		// Create the login scene
		showLoginScene(primaryStage);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}