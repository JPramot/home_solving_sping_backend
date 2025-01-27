package com.spring.home_solver.entity;

import com.spring.home_solver.enumulation.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at",columnDefinition = "notnull timpstamp default 'current_timestamp'")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "is_ban")
    private Boolean isBan = false;

    @OneToOne(mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    @ToString.Exclude
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    @ToString.Exclude
    private List<Post> posts = new ArrayList<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @PrePersist
    public void prePersist() {
        if(role == null) role = Role.user;
    }

    public void addPost(Post post) {
        posts.add(post);
        post.setUser(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
