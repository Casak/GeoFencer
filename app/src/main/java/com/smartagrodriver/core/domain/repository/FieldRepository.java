package com.smartagrodriver.core.domain.repository;

import com.smartagrodriver.core.domain.model.Field;

public interface FieldRepository {
    Field createField();

    Field getField(Integer id);

    boolean addField(Field field);

    boolean updateField(Field field);

    int[] getAllFieldIds();

    boolean deleteField(Integer id);
}