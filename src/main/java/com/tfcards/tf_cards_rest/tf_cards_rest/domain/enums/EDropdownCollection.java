package com.tfcards.tf_cards_rest.tf_cards_rest.domain.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum EDropdownCollection {
    API_LANG, API_VERSION;

    public enum Lang {
        ES, EN, PT, FR;

        public static boolean contains(String value) {
            try {
                Lang.valueOf(value);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    }

    public enum ApiVersion {
        V1, V2, V3;
    }

    public static List<Class<?>> getAllEnums() {
        return Arrays.asList(EDropdownCollection.class.getClasses()).stream()
                .filter(Class::isEnum)
                .collect(Collectors.toList());

    }

}
