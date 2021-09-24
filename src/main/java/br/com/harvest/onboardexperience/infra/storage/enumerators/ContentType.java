package br.com.harvest.onboardexperience.infra.storage.enumerators;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum ContentType {

    IMAGE("0", "Image"),
    VIDEO ("1", "Video"),
    MUSIC("2", "Music"),
    OTHER("3", "Other");

    private String code;
    private String description;

    ContentType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ContentType getByCodeOrDescription(String value){
        return Objects.isNull(getByCode(value)) ? getByDescription(value) : getByCode(value);
    }

    public static ContentType getByCode(String code){
        for(ContentType contentType : ContentType.values()){
            if(contentType.getCode().equalsIgnoreCase(code)){
                return contentType;
            }
        }
        return null;
    }

    public static ContentType getByDescription(String description){
        for(ContentType contentType : ContentType.values()){
            if(contentType.getDescription().equalsIgnoreCase(description)){
                return contentType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.code + " - " + this.description;
    }
}
