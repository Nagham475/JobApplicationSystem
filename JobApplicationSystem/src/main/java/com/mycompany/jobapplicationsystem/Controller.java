package com.mycompany.jobapplicationsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Random;

public class Controller {

    // ===== FXML Fields =====
    @FXML private VBox rootPane;
    @FXML private TextField nameField, addressField, phoneField;
    @FXML private RadioButton singleRadio, marriedRadio, divorceRadio;
    @FXML private ToggleGroup maritalGroup;
    @FXML private DatePicker birthdatePicker;
    @FXML private ListView<String> availableSkillsList, selectedSkillsList;
    @FXML private TextArea coverLetterArea;
    @FXML private Slider fontSizeSlider;
    @FXML private ImageView imageView;
    @FXML private ColorPicker colorPicker;

    // Track if image was selected
    private boolean imageSelected = false;

    // Skills lists
    private ObservableList<String> availableSkills = FXCollections.observableArrayList(
            "Java", "JavaFX", "Python", "SQL", "HTML/CSS",
            "JavaScript", "C++", "Data Analysis", "Machine Learning", "Networking"
    );
    private ObservableList<String> selectedSkills = FXCollections.observableArrayList();

    // Current font settings
    private String currentFontFamily = "Arial";
    private int    currentFontSize   = 14;
    private String currentFontStyle  = "normal";

    // ===== initialize =====
    @FXML
    public void initialize() {

        // Setup skill lists
        availableSkillsList.setItems(availableSkills);
        selectedSkillsList.setItems(selectedSkills);

        // Slider → cover letter font size (real-time)
        fontSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            coverLetterArea.setStyle("-fx-font-size: " + newVal.intValue() + "px;");
        });

        // ColorPicker → background color (real-time)
        colorPicker.setOnAction(e -> applyBackgroundColor(colorPicker.getValue()));

        // DatePicker → disable dates after Jan 1 2010
        birthdatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate maxDate = LocalDate.of(2010, 1, 1);
                if (date.isAfter(maxDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
    }

    // ===== Background color helper =====
    private void applyBackgroundColor(Color color) {
        String hex = String.format("#%02X%02X%02X",
                (int)(color.getRed()   * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue()  * 255));
        rootPane.setStyle("-fx-background-color: " + hex + "; -fx-padding: 15;");
    }

    // ===== Apply font to whole scene =====
    private void applyFont() {
        String bold   = currentFontStyle.equals("bold")   ? "bold"   : "normal";
        String italic = currentFontStyle.equals("italic")  ? "italic" : "normal";
        rootPane.setStyle(rootPane.getStyle()
                + " -fx-font-family: '" + currentFontFamily + "';"
                + " -fx-font-size: " + currentFontSize + "px;"
                + " -fx-font-weight: " + bold + ";"
                + " -fx-font-style: " + italic + ";");
    }

    // ===== MENU: File =====
    @FXML void handleExit() {
        System.exit(0);
    }

    // ===== MENU: Appearance → Font Family =====
    @FXML void setFontArial()   { currentFontFamily = "Arial";           applyFont(); }
    @FXML void setFontTimes()   { currentFontFamily = "Times New Roman"; applyFont(); }
    @FXML void setFontCourier() { currentFontFamily = "Courier New";     applyFont(); }

    // ===== MENU: Appearance → Font Size =====
    @FXML void setFontSmall()  { currentFontSize = 12; applyFont(); }
    @FXML void setFontMedium() { currentFontSize = 16; applyFont(); }
    @FXML void setFontLarge()  { currentFontSize = 20; applyFont(); }

    // ===== MENU: Appearance → Font Style =====
    @FXML void setFontStyleNormal() { currentFontStyle = "normal"; applyFont(); }
    @FXML void setFontStyleBold()   { currentFontStyle = "bold";   applyFont(); }
    @FXML void setFontStyleItalic() { currentFontStyle = "italic"; applyFont(); }

    // ===== MENU: Help → About =====
    @FXML void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Job Application System");
        alert.setContentText(
                "Application Name: Job Application System\n\n" +
                "Purpose: Allows applicants to submit job applications\n" +
                "through a user-friendly desktop interface.\n\n" +
                "Developer: [Naghm  Qanou']\n" +
                "Course: Programming III Lab - CSCI 2108\n" +
                "University: The Islamic University of Gaza"
        );
        alert.showAndWait();
    }

    // ===== Upload Image =====
    @FXML void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
        if (file != null) {
            imageView.setImage(new Image(file.toURI().toString()));
            imageSelected = true;
        }
    }

    // ===== Skills Transfer =====
    @FXML void moveSkillRight() {
        String selected = availableSkillsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            availableSkills.remove(selected);
            selectedSkills.add(selected);
        }
    }

    @FXML void moveSkillLeft() {
        String selected = selectedSkillsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedSkills.remove(selected);
            availableSkills.add(selected);
        }
    }

    // ===== Upload Cover Letter =====
    @FXML void uploadCoverLetter() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Cover Letter");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File file = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
        if (file != null) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                coverLetterArea.setText(content);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to read the cover letter file.");
            }
        }
    }

    // ===== Save Application =====
    @FXML void saveApplication() {

        // --- Collect data ---
        String name        = nameField.getText().trim();
        String address     = addressField.getText().trim();
        String phone       = phoneField.getText().trim();
        String coverLetter = coverLetterArea.getText().trim();
        LocalDate birthdate = birthdatePicker.getValue();
        RadioButton selectedMarital = (RadioButton) maritalGroup.getSelectedToggle();

        // --- Validate using Validator class ---
        if (!Validator.isValidName(name)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Full Name is required and must be at least 3 characters.");
            return;
        }
        if (!Validator.isValidAddress(address)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Address must not be empty.");
            return;
        }
        if (!Validator.isValidPhone(phone)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Phone number must contain digits only and be at least 7 digits.");
            return;
        }
        if (selectedMarital == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a marital status.");
            return;
        }
        if (birthdate == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a birthdate.");
            return;
        }
        if (!Validator.hasAtLeastOneSkill(selectedSkills.size())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Please select at least one skill.");
            return;
        }
        if (!Validator.hasCoverLetter(coverLetter)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Cover letter must not be empty. Please upload or type your cover letter.");
            return;
        }
        if (!imageSelected) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Please upload a personal image.");
            return;
        }

        // --- Save to file ---
        try {
            int randomNum = new Random().nextInt(9000) + 1000;
            String fileName = name.replaceAll("\\s+", "_") + "_" + randomNum + ".txt";
            File file = new File(fileName);
            PrintWriter writer = new PrintWriter(file);

            writer.println("========================================");
            writer.println("         JOB APPLICATION FORM          ");
            writer.println("========================================");
            writer.println("Full Name    : " + name);
            writer.println("Address      : " + address);
            writer.println("Phone        : " + phone);
            writer.println("Marital Status: " + selectedMarital.getText());
            writer.println("Birthdate    : " + birthdate);
            writer.println("Skills       : " + String.join(", ", selectedSkills));
            writer.println();
            writer.println("--- Cover Letter ---");
            writer.println(coverLetter);
            writer.println("========================================");
            writer.close();

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Application saved successfully!\nFile: " + fileName);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while saving the file.");
        }
    }

    // ===== Helper: show alert =====
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}