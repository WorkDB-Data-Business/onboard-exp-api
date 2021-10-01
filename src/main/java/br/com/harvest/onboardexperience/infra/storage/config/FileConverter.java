package br.com.harvest.onboardexperience.infra.storage.config;

import br.com.harvest.onboardexperience.infra.storage.entities.HarvestFile;
import org.springframework.core.convert.converter.Converter;

public class FileConverter implements Converter<HarvestFile, String> {

    @Override
    public String convert(HarvestFile source) {
        return source.getContentPath();
    }

}
