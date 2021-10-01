package br.com.harvest.onboardexperience.infra.storage.config;

import br.com.harvest.onboardexperience.infra.storage.entities.Image;
import org.springframework.core.convert.converter.Converter;

public class ImageConverter implements Converter<Image, String > {

    @Override
    public String convert(Image source) {
        return source.getContentPath();
    }
}
