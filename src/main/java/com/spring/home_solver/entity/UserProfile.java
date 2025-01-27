package com.spring.home_solver.entity;

import com.spring.home_solver.enumulation.Gender;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String profileImage;

    private String alias;

    private String introduction;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User user;

    public UserProfile(String firstName, String lastName, LocalDate birthDate, Gender gender,
                       String profileImage, String alias, String introduction) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.profileImage = profileImage;
        this.alias = alias;
        this.introduction = introduction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
