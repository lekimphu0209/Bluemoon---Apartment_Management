package com.qlcc.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // username cu, van con trong schema (col `name`)
    @Column(name = "name")
    private String name;

    // Legacy col `password` van con trong DB; cot chuan moi la password_hash.
    // Ta map field `password` -> `password` (legacy, van doc duoc bcrypt cu)
    // va `passwordHash` -> `password_hash` (canonical). setPassword() ghi ca 2.
    @Column(name = "password")
    private String password;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "role")
    private String role;

    @Column(name = "activation")
    private boolean activation = true;

    @Column(name = "resident_id")
    private Long residentId;

    @Column(name = "date_created")
    private String dateCreated;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    /**
     * Ghi password vao ca 2 cot (legacy `password` + canonical `password_hash`)
     * de giu consistent trong giai doan chuyen tiep.
     */
    public void setPassword(String password) {
        this.password = password;
        this.passwordHash = password;
    }

    // ---- UserDetails ----
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return this.activation; }
}
