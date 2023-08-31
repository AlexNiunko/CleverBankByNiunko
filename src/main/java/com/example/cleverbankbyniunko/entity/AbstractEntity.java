package com.example.cleverbankbyniunko.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractEntity {

    private int id;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
