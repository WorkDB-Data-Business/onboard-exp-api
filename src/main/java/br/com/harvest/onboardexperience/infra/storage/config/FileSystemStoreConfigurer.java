package br.com.harvest.onboardexperience.infra.storage.config;

import org.springframework.content.fs.config.FilesystemStoreConfigurer;
import org.springframework.core.convert.converter.ConverterRegistry;

public class FileSystemStoreConfigurer implements FilesystemStoreConfigurer {

    @Override
    public void configureFilesystemStoreConverters(ConverterRegistry converterRegistry) {
        converterRegistry.addConverter(new FileConverter());
        converterRegistry.addConverter(new ImageConverter());
    }
}
