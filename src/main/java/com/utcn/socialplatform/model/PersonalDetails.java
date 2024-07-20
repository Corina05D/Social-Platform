package com.utcn.socialplatform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="PersonalDetails")
public class PersonalDetails {
    @Id
    @PrimaryKeyJoinColumn
    private String email;
    private String bio;
    private String home;
    private String birthplace;
    private String study;
    private String work;

    @OneToOne
    @JoinColumn(name = "profile_picture_id", referencedColumnName = "id")
    private Photo profilePicture;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    private User user;
}