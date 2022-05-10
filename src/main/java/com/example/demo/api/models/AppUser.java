package com.example.demo.api.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table
@Data
public class AppUser {
    @Id
    @SequenceGenerator(
            name = "appuser_sequence",
            sequenceName = "appuser_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "appuser_sequence"
    )
    private Integer id;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;
    private LocalDate birthDay;
    private Integer isDisabled;
}
