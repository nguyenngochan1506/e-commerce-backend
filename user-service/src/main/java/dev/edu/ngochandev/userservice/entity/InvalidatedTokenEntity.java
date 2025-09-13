package dev.edu.ngochandev.userservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "tbl_invalidated_tokens", schema = "user_schema")
public class InvalidatedTokenEntity {
    @Id
    private String jti;
    private Date expiration;
}
