package com.spring.home_solver.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String content;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "is_delete")
    private Boolean isDelete = false;

    @Column(name = "can_comment")
    private Boolean canComment = true;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostImage> postImages = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<AppealPost> appealPosts = new HashSet<>();

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addPostImage(PostImage postImage) {
        postImages.add(postImage);
        postImage.setPost(this);
    }

    public void removeImage(PostImage postImage) {
        postImages.remove(postImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
