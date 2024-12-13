package org.sahabat.pulau.services;

import org.springframework.core.io.FileSystemResource;

public interface FileService {


    public static String getFileExtension(String filename) {
        Integer extensionSeparatorPosition = filename.lastIndexOf('.');
        return extensionSeparatorPosition != -1 ? filename.substring(extensionSeparatorPosition) : "";
    }

    void store(String folder,String prefix, String name, byte[] fileContent);
    byte[] load(String folder,String prefix, String name);
    FileSystemResource loadAsFileSystemResource(String folder,String prefix, String name);
}
