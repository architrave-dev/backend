package com.architrave.portfolio.domain.model.enumType;

import com.architrave.portfolio.global.exception.custom.NoMatchEnumException;

public enum CareerType {
    EDU, //Education
    PRZ, //Prize
    PRS, //Press
    RSD, //Residency
    S_EXH,  //solo Exhibition
    G_EXH,  //group Exhibition
    RPS, //Represent
    TCH, //Teach
    PBL, //Publication
    COL; //Collections

    public static CareerType fromString(String value) {
        for (CareerType type : CareerType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new NoMatchEnumException("Invalid CareerType: " + value);
    }
}
