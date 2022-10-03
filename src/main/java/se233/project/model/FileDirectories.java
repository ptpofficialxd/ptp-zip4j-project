package se233.project.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

public class FileDirectories {
    private List<String> fileDirectories = new ArrayList<>();
    private ObservableList<String> observableFileList;
    private boolean isExtractable;

    public FileDirectories() {}

    /*
        Set fileDirectories and update observableFileList to current value of fileDirectories
        @param fileDirectories - List of files
     */
    public void setAndUpdate(List<String> fileList) {
        setFileList(fileList);
        observableFileList = FXCollections.observableList(fileList.stream().toList());
    }

    public void setFileList(List<String> fileList) {
        this.fileDirectories = fileList;
    }

    public List<String> getFileList() {
        return fileDirectories;
    }

    public ObservableList<String> getObservableFileList() {
        return observableFileList;
    }

    public boolean isExtractable() {
        isExtractable = true;
        fileDirectories.forEach(s -> {
                    if (!s.contains(".zip") && !s.contains(".tar.gz")) {
                        this.isExtractable = false;
                    }
        });
        return this.isExtractable;
    }
}
