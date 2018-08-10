package com.hankkin.reading.greendao;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by danny.jiang on 17/6/20.
 */

public class StringConverter implements PropertyConverter<List<String>, String>{

    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        else {
            List<String> list = Arrays.asList(databaseValue.split(","));
            return list;
        }
    }

    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        if(entityProperty==null){
            return null;
        }
        else{
            StringBuilder sb= new StringBuilder();
            for(String link:entityProperty){
                sb.append(link);
                sb.append(",");
            }
            return sb.toString();
        }
    }
}