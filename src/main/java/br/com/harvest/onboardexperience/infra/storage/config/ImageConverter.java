package br.com.harvest.onboardexperience.infra.storage.config;

import br.com.harvest.onboardexperience.infra.storage.entities.Asset;
import org.springframework.core.convert.converter.Converter;

public class ImageConverter implements Converter<Asset, String > {

    @Override
    public String convert(Asset source) {
        return source.getContentPath();
    }
}
