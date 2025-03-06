package com.example.accounting_service.services;

import com.example.accounting_service.dto.UserDto;
import com.example.accounting_service.entities.User;
import com.example.accounting_service.entities.UserRole;
import com.example.accounting_service.exceptions.UserNotFoundException;
import com.example.accounting_service.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get().mapToDto();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        GrantedAuthority authority = new SimpleGrantedAuthority(UserRole.fromValue(user.getRole()).name());
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authority)
                .accountLocked(false)
                .build();
    }

    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(User::mapToDto).collect(Collectors.toList());
    }

    public void createUser(UserDto userDto) {
        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setName(userDto.getName());
        newUser.setSurname(userDto.getSurname());
        newUser.setAccount(userDto.getAccount());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
    }

    public void deleteUserById(Integer userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public boolean isUserEmailAlreadyUsed(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
