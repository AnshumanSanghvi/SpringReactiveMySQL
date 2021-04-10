package com.anshuman.spring.reactive;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class Constants {
    public enum Endpoint {
        byMake("/vehicle/make/"),
        byMakeVar("/vehicle/make/{make}"),
        byId("/vehicle/id/"),
        byIdVar("/vehicle/id/{id}"),
        all("/vehicles"),
        save("/vehicle");

        @Getter
        final String path;

        Endpoint(String path){
            this.path = path;
        };
    }
}
