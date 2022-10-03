package se233.project.controller;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExtractorController {
    public static void extractZip(String zipDir, String extDir, String password) throws ZipException {
        File file = new File(zipDir);
        ZipFile zipFile = new ZipFile(file, password.toCharArray());
        zipFile.extractAll(extDir);
    }

    public static void extractTargz(String targzDir, String extDir, String text) throws IOException {
        File file = new File(targzDir);
        FileInputStream fileInputStream = new FileInputStream(file);
        GzipCompressorInputStream gzipCompressorInputStream = new GzipCompressorInputStream(fileInputStream);
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(gzipCompressorInputStream);
        TarArchiveEntry tarEntry;
        while ((tarEntry = tarArchiveInputStream.getNextTarEntry()) != null) {
            IOUtils.copy(tarArchiveInputStream, new FileOutputStream(new File(extDir, tarEntry.getName())));
        }
        tarArchiveInputStream.close();
        gzipCompressorInputStream.close();
        fileInputStream.close();
    }
}