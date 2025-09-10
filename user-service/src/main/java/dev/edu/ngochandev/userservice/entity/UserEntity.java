package dev.edu.ngochandev.userservice.entity;

import dev.edu.ngochandev.sharedkernel.common.BaseEntity;
import dev.edu.ngochandev.userservice.common.Gender;
import dev.edu.ngochandev.userservice.common.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_users", schema = "user_schema")
@Getter
@Setter
public class UserEntity extends BaseEntity {
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "avatar", length = 1000)
    private String avatar;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Override
    public void prePersist() {
        super.prePersist();
        if (status == null) {
            status = UserStatus.INACTIVE;
        }
        if(gender == null) {
            gender = Gender.UNKNOWN;
        }
    }
}
