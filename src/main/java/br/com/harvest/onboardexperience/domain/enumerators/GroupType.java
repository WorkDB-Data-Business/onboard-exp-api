package br.com.harvest.onboardexperience.domain.enumerators;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum GroupType {

    USER("0", "User"), COMPANY_ROLE ("1", "Company Role");

    private String code;
    private String description;

    GroupType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static GroupType getByCodeOrDescription(String value){
        return Objects.isNull(getByCode(value)) ? getByDescription(value) : getByCode(value);
    }

    public static GroupType getByCode(String code){
        for(GroupType groupType : GroupType.values()){
            if(groupType.getCode().equalsIgnoreCase(code)){
                return groupType;
            }
        }
        return null;
    }

    public static GroupType getByDescription(String description){
        for(GroupType groupType : GroupType.values()){
            if(groupType.getDescription().equalsIgnoreCase(description)){
                return groupType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.code + " - " + this.description;
    }
}
