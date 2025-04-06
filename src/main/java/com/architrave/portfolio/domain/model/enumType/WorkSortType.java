package com.architrave.portfolio.domain.model.enumType;

public enum WorkSortType {
    title("title"),
    prodYear("prodYear");
    private final String value;

    WorkSortType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
    // String → WorkSortType 변환
    public static WorkSortType fromString(String value) {
        for (WorkSortType type : WorkSortType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown sort type: " + value);
    }

    // 존재 여부만 확인하고 싶을 때
    public static boolean contains(String value) {
        for (WorkSortType type : WorkSortType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
