package br.com.harvest.onboardexperience.infra.storage.config;

import org.springframework.content.fs.config.FilesystemStoreConfigurer;
import org.springframework.content.fs.io.FileSystemResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StoreConfig {

    private final Path tempDir = Paths.get(System.getProperty("user.dir")).resolve("storage");

    @Bean
    public FilesystemStoreConfigurer configurer() {
        return new FileSystemStoreConfigurer();
    }

    @Bean
    public File filesystemRoot() {
        try {
            if(!Files.exists(tempDir)){
                Files.createDirectory(tempDir).toFile();
            }
            return tempDir.toFile();
        } catch (IOException ioe) {}
        return null;
    }

    @Bean
    public FileSystemResourceLoader fileSystemResourceLoader() {
        return new FileSystemResourceLoader(filesystemRoot().getAbsolutePath());
    }
}
