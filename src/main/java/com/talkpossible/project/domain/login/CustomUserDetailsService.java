package com.talkpossible.project.domain.login;

import com.talkpossible.project.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.talkpossible.project.global.exception.CustomErrorCode.DOCTOR_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return doctorRepository.findByEmail(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new CustomException(DOCTOR_NOT_FOUND));
    }

}
