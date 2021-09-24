package br.com.harvest.onboardexperience.infra.storage.config;

import br.com.harvest.onboardexperience.infra.storage.entities.File;
import org.springframework.core.convert.converter.Converter;

public class FileConverter implements Converter<File, String> {

    @Override
    public String convert(File source) {
        return source.getContentPath();
    }

}
