package com.DvFabricio.PicPaySimplificado.domain.user;

import com.DvFabricio.PicPaySimplificado.dtos.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name= "Users")
@Table(name= "Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String document;

    @Column(unique = true)
    private String email;

    private String password;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(UserDTO userDto) {
        this.firstName = userDto.firstName();
        this.lastName = userDto.lastName();
        this.document = userDto.document();
        this.email = userDto.email();
        this.balance = userDto.balance();
        this.password = userDto.password();
        this.userType = userDto.userType();
    }
}
