package com.talkpossible.project.domain.login;

import com.talkpossible.project.domain.chatGPT.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);
}
