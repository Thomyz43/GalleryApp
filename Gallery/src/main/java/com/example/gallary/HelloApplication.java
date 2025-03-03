package com.example.gallary;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {

    private GridPane gridPane;
    private BorderPane borderPane;
    private List<String> imagePaths;
    private int currentIndex;

    @Override
    public void start(Stage primaryStage) {
        imagePaths = new ArrayList<>();
        addImageToList("/image/image.JPG");
        addImageToList("/image/image1.JPG");
        addImageToList("/image/image2.JPG");
        addImageToList("/image/image3.JPG");
        addImageToList("/image/image4.JPG");
        addImageToList("/image/image5.JPG");
        addImageToList("/image/image6.JPG");
        addImageToList("/image/image7.JPG");

        currentIndex = 0;

        borderPane = new BorderPane();
        gridPane = createThumbnailGrid();
        borderPane.setCenter(gridPane);

        Scene scene = new Scene(borderPane, 600, 400);
        scene.getStylesheets().add("style.css");

        primaryStage.setTitle("Image Gallery");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addImageToList(String imagePath) {
        imagePaths.add(imagePath);
    }

    private GridPane createThumbnailGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        for (int i = 0; i < imagePaths.size(); i++) {
            String imagePath = imagePaths.get(i);
            final int index = i;
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().add("thumbnail-container");

            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            ImageView thumbnail = new ImageView(image);
            thumbnail.setFitWidth(100);
            thumbnail.setFitHeight(100);
            thumbnail.setPreserveRatio(true);
            thumbnail.getStyleClass().add("thumbnail-image");

            // Hover effect - Scale & Rotate
            ScaleTransition hoverScale = new ScaleTransition(Duration.millis(200), thumbnail);
            hoverScale.setFromX(1);
            hoverScale.setFromY(1);
            hoverScale.setToX(1.1);
            hoverScale.setToY(1.1);

            RotateTransition hoverRotate = new RotateTransition(Duration.millis(200), thumbnail);
            hoverRotate.setFromAngle(0);
            hoverRotate.setToAngle(10);

            ScaleTransition hoverExitScale = new ScaleTransition(Duration.millis(200), thumbnail);
            hoverExitScale.setFromX(1.1);
            hoverExitScale.setFromY(1.1);
            hoverExitScale.setToX(1);
            hoverExitScale.setToY(1);

            RotateTransition hoverExitRotate = new RotateTransition(Duration.millis(200), thumbnail);
            hoverExitRotate.setFromAngle(10);
            hoverExitRotate.setToAngle(0);

            stackPane.setOnMouseEntered(e -> {
                hoverScale.playFromStart();
                hoverRotate.playFromStart();
            });

            stackPane.setOnMouseExited(e -> {
                hoverExitScale.playFromStart();
                hoverExitRotate.playFromStart();
            });

            stackPane.setOnMouseClicked(e -> showFullImage(index));
            stackPane.getChildren().add(thumbnail);
            grid.add(stackPane, i % 4, i / 4);
        }
        return grid;
    }

    private void showFullImage(int index) {
        currentIndex = index;
        Image image = new Image(getClass().getResource(imagePaths.get(index)).toExternalForm());
        ImageView fullImageView = new ImageView(image);
        fullImageView.setFitWidth(300);
        fullImageView.setFitHeight(300);
        fullImageView.getStyleClass().add("full-image-view");

        // Full Image Fade-in Animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), fullImageView);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Full Image Scale-in Animation
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(500), fullImageView);
        scaleIn.setFromX(0.8);
        scaleIn.setFromY(0.8);
        scaleIn.setToX(1);
        scaleIn.setToY(1);
        scaleIn.play();

        // Hover Effect on Full Image
        ScaleTransition fullImageHoverScale = new ScaleTransition(Duration.millis(200), fullImageView);
        fullImageHoverScale.setFromX(1);
        fullImageHoverScale.setFromY(1);
        fullImageHoverScale.setToX(1.05);
        fullImageHoverScale.setToY(1.05);

        ScaleTransition fullImageHoverExitScale = new ScaleTransition(Duration.millis(200), fullImageView);
        fullImageHoverExitScale.setFromX(1.05);
        fullImageHoverExitScale.setFromY(1.05);
        fullImageHoverExitScale.setToX(1);
        fullImageHoverExitScale.setToY(1);

        fullImageView.setOnMouseEntered(e -> fullImageHoverScale.playFromStart());
        fullImageView.setOnMouseExited(e -> fullImageHoverExitScale.playFromStart());

        Button prevButton = new Button("Previous");
        prevButton.getStyleClass().add("nav-button");
        prevButton.setOnAction(e -> showFullImage((currentIndex - 1 + imagePaths.size()) % imagePaths.size()));

        Button nextButton = new Button("Next");
        nextButton.getStyleClass().add("nav-button");
        nextButton.setOnAction(e -> showFullImage((currentIndex + 1) % imagePaths.size()));

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> backToThumbnailView());

        BorderPane fullImagePane = new BorderPane();
        fullImagePane.setCenter(fullImageView);
        fullImagePane.setBottom(backButton);

        BorderPane buttonPanel = new BorderPane();
        buttonPanel.setLeft(prevButton);
        buttonPanel.setCenter(nextButton);
        buttonPanel.setRight(backButton);

        borderPane.setTop(buttonPanel);
        borderPane.setCenter(fullImageView);
    }

    private void backToThumbnailView() {
        borderPane.setCenter(gridPane);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
