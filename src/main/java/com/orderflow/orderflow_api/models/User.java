package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="users", uniqueConstraints = {
        @UniqueConstraint(columnNames="username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Size(max=25)
    @Column(name = "username")
    private String username;

    @NotBlank
    @Email
    @Size(max=50)
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(max=25)
    @Column(name = "password")
    private String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
