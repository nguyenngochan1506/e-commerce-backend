package dev.edu.ngochandev.productservice.common;


import java.io.Serializable;

public record ProductAttributes(String key, Object value) implements Serializable {
}
