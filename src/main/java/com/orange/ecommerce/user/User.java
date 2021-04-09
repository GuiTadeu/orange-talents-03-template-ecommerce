package com.orange.ecommerce.user;

import com.orange.ecommerce.config.security.role.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "USER_APPLICATION")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 6)
    private String encryptPassword;

    @NotNull
    @PastOrPresent
    private LocalDate createdAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "authority")
    private Role role;

    public User() {}

    public User(@Email @NotBlank String email, @NotBlank @Size(min = 6) String plainPassword) {
        this.email = email.toLowerCase();
        this.encryptPassword = new BCryptPasswordEncoder().encode(plainPassword);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getEncryptPassword() {
        return encryptPassword;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(this.role);
    }

    @Override
    public String getPassword() {
        return this.encryptPassword;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
