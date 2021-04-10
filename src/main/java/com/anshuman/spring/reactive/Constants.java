package com.anshuman.spring.reactive;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class Constants {
    public enum Endpoint {
        byMake("/car/make/"),
        byMakeVar("/car/make/{make}"),
        byId("/car/id/"),
        byIdVar("/car/id/{id}"),
        all("/cars"),
        save("/car");

        @Getter
        final String path;

        Endpoint(String path){
            this.path = path;
        };
    }
}
