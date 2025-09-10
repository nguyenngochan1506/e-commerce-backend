package dev.edu.ngochandev.userservice.repository;

import dev.edu.ngochandev.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    @Query("""
            SELECT u FROM UserEntity u 
                     WHERE u.username = :identifier 
                        OR u.email = :identifier
                        OR u.phoneNumber = :identifier""")
    Optional<UserEntity> findByIdentifier(@Param("identifier") String identifier);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
