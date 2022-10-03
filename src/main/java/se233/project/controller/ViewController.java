package se233.project.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import net.lingala.zip4j.exception.ZipException;
import se233.project.Launcher;
import se233.project.model.FileDirectories;
import se233.project.model.FileExtension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ViewController {
    private FileDirectories fileDirectories = new FileDirectories();
    private ArrayList<String> fileArrayList = new ArrayList<>();
    @FXML
    private Pane dropPane;
    @FXML
    private MenuBar menuBar;
    @FXML
    private ChoiceBox<FileExtension> extensionChoiceBox = new ChoiceBox<FileExtension>();
    @FXML
    private ListView<String> listView;
    @FXML
    private Label progressLabel;
    @FXML
    private Button browseButton, addButton, deleteButton, archiveButton, clearButton, extractButton;
    @FXML private MenuItem exitProgram;
    @FXML
    private TextField directoryTextField, fileNameTextField, passwordField;
    @FXML
    private ProgressBar progressBar;
    @FXML
    public void initialize() {

        extensionChoiceBox.setItems(FXCollections.observableList(Arrays.asList(FileExtension.ZIP,FileExtension.TARGZ)));
        extensionChoiceBox.setValue(FileExtension.ZIP);
        directoryTextField.setText("D:\\953233\\ADV_Programming\\Project");

        dropPane.setOnDragOver(dragEvent -> {
            Dragboard dragboard = dragEvent.getDragboard();
            if (dragboard.hasFiles()) {
                dragEvent.acceptTransferModes(TransferMode.COPY);
            } else {
                dragEvent.consume();
            }
        });

        dropPane.setOnDragDropped(dragEvent -> {
            Dragboard dragboard = dragEvent.getDragboard();
            boolean success = false;
            if (dragboard.hasFiles()) {
                success = true;
                int total_files = dragboard.getFiles().size();
                for (int i = 0; i<total_files; i++) {
                    File file = dragboard.getFiles().get(i);
                    if (!fileArrayList.contains(file.getPath()))
                        fileArrayList.add(file.getPath());
                }
            }
            fileDirectories.setAndUpdate(fileArrayList);
            listView.setItems(fileDirectories.getObservableFileList());
        });

        addButton.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select files");
            try {
                fileArrayList.addAll(fileChooser.showOpenMultipleDialog(Launcher.stage.getOwner())
                        .stream()
                        .map(File::getAbsolutePath)
                        .toList());
                fileDirectories.setAndUpdate(fileArrayList);
            } catch (NullPointerException e) {
                System.out.println("Add Button: No directory chosen");
            }
            listView.setItems(fileDirectories.getObservableFileList());
        });

        deleteButton.setOnAction(actionEvent -> {
            fileArrayList.remove(listView.getSelectionModel().getSelectedIndex());
            fileDirectories.setAndUpdate(fileArrayList);
            listView.setItems(fileDirectories.getObservableFileList());
        });

        browseButton.setOnAction(actionEvent -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select a directory");
            directoryTextField.setText(directoryChooser.showDialog(Launcher.stage.getOwner()).getAbsolutePath());
        });

        clearButton.setOnAction(actionEvent -> {
            fileArrayList = new ArrayList<>();
            fileDirectories.setAndUpdate(fileArrayList);
            listView.setItems(fileDirectories.getObservableFileList());
        });

        archiveButton.setOnAction(actionEvent -> {
            if (directoryTextField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notice");
                alert.setHeaderText("You haven't selected a file destination.");
                alert.showAndWait();
                return;
            }
            if (fileNameTextField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notice");
                alert.setHeaderText("You haven't set the file name.");
                alert.showAndWait();
                return;
            }
            try {
                if (extensionChoiceBox.getValue().equals(FileExtension.ZIP)) {
                    ArchiverController.compressToZip(fileDirectories.getFileList(),
                            directoryTextField.getText() + "\\" + fileNameTextField.getText() + ".zip",
                            passwordField.getText(),
                            progressLabel,
                            progressBar);
                }
                else {
                    ArchiverController.compressToTargz(fileDirectories.getFileList(),
                            directoryTextField.getText() + "\\" + fileNameTextField.getText()+".tar.gz", passwordField.getText(), progressLabel, progressBar);

                }
            } catch (IllegalStateException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notice");
                alert.setHeaderText("No file");
                alert.showAndWait();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        extractButton.setOnAction(actionEvent -> {
            if (!fileDirectories.isExtractable()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notice");
                alert.setHeaderText("Invalid file extension");
                alert.showAndWait();
                return;
            } else {
                System.out.println("Can");
            }
            fileDirectories.getFileList().forEach(s -> {
                try {
                    if (s.contains(".zip"))
                        ExtractorController.extractZip(s, directoryTextField.getText(), passwordField.getText());
                    else
                        ExtractorController.extractTargz(s, directoryTextField.getText(), passwordField.getText());;
                    if (s.contains(".tar.gz"))
                        ExtractorController.extractTargz(s, directoryTextField.getText(), passwordField.getText());
                    else
                        ExtractorController.extractTargz(s, directoryTextField.getText(), passwordField.getText());;
                } catch (ZipException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        exitProgram.setOnAction(actionEvent -> {
            Launcher.stage.close();
        });
    }
}
