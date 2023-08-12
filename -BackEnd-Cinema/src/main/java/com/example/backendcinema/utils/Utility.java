package com.example.backendcinema.utils;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utility {

    public static <T> void filter(List<T> list, String filterBy, Object filterValue) {
        System.out.println(filterBy+", "+filterValue);
        if (filterBy != null && filterValue != null && !filterBy.isEmpty() && !filterValue.toString().isEmpty()) {
            list.removeIf(obj -> {
                try {
                    Field field = obj.getClass().getDeclaredField(filterBy);
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    field.setAccessible(false);

                    if (value instanceof Integer && isNumber(filterValue)) {
                        return !value.equals(Integer.valueOf(filterValue.toString()));
                    } else if (value instanceof Date && isDate(filterValue)) {
                        return !value.equals(Date.valueOf(filterValue.toString()));
                    }
                    return !value.equals(filterValue);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    return false;
                }
            });
        }
    }

    private static boolean isDate(Object filterValue) {
        try {
            Date date = Date.valueOf(filterValue.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isNumber(Object filterValue) {
        try {
            Integer number = Integer.valueOf(filterValue.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static <T> void sort(List<T> list, String sortBy, String sortValue) {
        if (sortBy != null && sortValue != null && !sortBy.isEmpty() && !sortValue.isEmpty()) {
            list.sort((o1, o2) -> {
                try {
                    Field field1 = o1.getClass().getDeclaredField(sortBy);
                    Field field2 = o2.getClass().getDeclaredField(sortBy);

                    field1.setAccessible(true);
                    field2.setAccessible(true);

                    Object value1 = field1.get(o1);
                    Object value2 = field1.get(o2);

                    field1.setAccessible(true);
                    field2.setAccessible(true);

                    if (value1 instanceof Integer && value2 instanceof Integer) {
                        return ((Integer) value1).compareTo((Integer) value2);
                    } else if (value1 instanceof String && value2 instanceof String) {
                        return ((String) value1).compareTo((String) value2);
                    } else if (value1 instanceof Date && value2 instanceof Date) {
                        return ((Date) value1).compareTo((Date) value2);
                    }

                    return 1;
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });

            if (sortValue.equals("desc")) Collections.reverse(list);
        }
    }

    public static <T> void filterAndSort(List<T> cashiers, String sortBy, String sortValue, String filterBy, String filterValue) {
        filter(cashiers, filterBy, filterValue);
        sort(cashiers, sortBy, sortValue);
    }

    public static <T> boolean atLeastOneNullField(T object) {
        List<Field> fields = Arrays.stream(object.getClass().getDeclaredFields()).toList();
        fields.forEach(field -> field.setAccessible(true));
        for (Field field: fields) {
            try {
                if (!field.getName().equals("id") && field.get(object) == null) return true;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public static <T> boolean allFieldsAreNull(T object) {
        List<Field> fields = Arrays.stream(object.getClass().getDeclaredFields()).toList();
        fields.forEach(field -> field.setAccessible(true));
        for (Field field: fields) {
            try {
                if (!field.getName().equals("id") && field.get(object) != null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
