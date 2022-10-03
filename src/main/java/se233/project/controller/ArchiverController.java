package se233.project.controller;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.progress.ProgressMonitor;
import java.io.*;
import java.util.List;

public class ArchiverController {

    public static void compressToZip(List<String> fileDirectories, String directory, String password, Label label, ProgressBar progressBar) throws IOException, InterruptedException {
        if (fileDirectories.isEmpty()) {
            throw new IllegalStateException();
        }

        boolean encrypt = !password.isEmpty();
        ZipFile zipFile;
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(CompressionMethod.DEFLATE);
        zipParameters.setCompressionLevel(CompressionLevel.NORMAL);
        if (encrypt) {
            zipParameters.setEncryptFiles(true);
            zipParameters.setEncryptionMethod(EncryptionMethod.AES);
            zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
            zipFile = new ZipFile(directory, password.toCharArray());
        }
        else {
            zipFile = new ZipFile(directory);
        }
        ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
        zipFile.setRunInThread(true);
        for (String file: fileDirectories) {
            zipFile.addFile(file, zipParameters);
            while (!progressMonitor.getState().equals(ProgressMonitor.State.READY)) {
                label.setText(progressMonitor.getPercentDone()+"%");
                progressBar.setProgress(progressMonitor.getPercentDone());
            }
        }
        zipFile.close();
    }
    public static void compressToTargz(List<String> fileDirectories, String directory, String password, Label label, ProgressBar progressBar) throws IOException, InterruptedException {
        if (fileDirectories.isEmpty()) {
            throw new IllegalStateException();
        }
        boolean encrypt = !password.isEmpty();
        ZipFile targzFile;
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(CompressionMethod.DEFLATE);
        zipParameters.setCompressionLevel(CompressionLevel.NORMAL);
        if (encrypt) {
            zipParameters.setEncryptFiles(true);
            zipParameters.setEncryptionMethod(EncryptionMethod.AES);
            zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
            targzFile = new ZipFile(directory, password.toCharArray());
        } else {
            targzFile = new ZipFile(directory);
        }
        ProgressMonitor progressMonitor = targzFile.getProgressMonitor();
        targzFile.setRunInThread(true);
        for (String file : fileDirectories) {
            targzFile.addFile(file, zipParameters);
            while (!progressMonitor.getState().equals(ProgressMonitor.State.READY)) {
                label.setText(progressMonitor.getPercentDone() + "%");
                progressBar.setProgress(progressMonitor.getPercentDone());
            }
        }
        targzFile.close();
    }
}