package dev.edu.ngochandev.userservice.repository;

import dev.edu.ngochandev.userservice.entity.InvalidatedTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedTokenEntity, String> {
    boolean existsByJti(String jti);
}
