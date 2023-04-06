package com.ocbc_rpp.rest.util;

import jakarta.persistence.Persistence;

public class LazyFieldsFilter {
    @Override
    public boolean equals(Object obj){
        return !Persistence.getPersistenceUtil().isLoaded(obj);
    }
}
