package org.sahabat.pulau.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Profile("!secure")
@Service @Slf4j
public class PlainFileService implements FileService {
    
    public void store(String folder,String prefix, String name, byte[] fileContent) {
        try {
            Path destination = Paths.get(folder, prefix, name);
            log.debug("Storing {} with prefix {} at {}", name, prefix, destination.toAbsolutePath());
            Files.createDirectories(Paths.get(folder, prefix));
            Files.write(destination, fileContent);
        } catch (Exception err) {
            log.error(err.getMessage(), err);
        }
    }

    public byte[] load(String folder,String prefix, String name) {
        try {
            Path destination = Paths.get(folder, prefix, name);
            log.debug("Loading {} with prefix {} from {}", name, prefix, destination.toAbsolutePath());
            return Files.readAllBytes(destination);
        }catch (NoSuchFileException | FileNotFoundException fileNotFoundException){
            log.info("FILE NOT FOUND {}{}{}",folder, prefix, name);
        } catch (Exception err) {
            log.error(err.getMessage(), err);
        }
        return null;
    }

    public FileSystemResource loadAsFileSystemResource(String folder,String prefix, String name) {
        try {
            Path destination = Paths.get(folder, prefix, name);
            log.debug("Loading {} with prefix {} from {}", name, prefix, destination.toAbsolutePath());
            return new FileSystemResource(destination);
        } catch (Exception err) {
            log.error(err.getMessage(), err);
        }
        return null;
    }
}
