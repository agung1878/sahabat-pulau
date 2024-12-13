package org.sahabat.pulau.entity.security;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.sahabat.pulau.entity.BaseEntity;

@Entity
@Data
@Table(name = "c_security_user")
public class User extends BaseEntity {

    @Size(max = 100)
    @NotEmpty(message = "Username tidak boleh dikosongkan")
    @Column(nullable = false, unique = true)
    private String username;

    @Size(max = 255)
    @NotEmpty(message = "Name tidak boleh dikosongkan")
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 100)
    @NotEmpty(message = "Email tidak boleh dikosongkan")
    @Column(nullable = false, unique = true)
    private String email;

    @Size(max = 100)
    @NotEmpty(message = "Phone tidak boleh dikosongkan")
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    private Boolean active = Boolean.FALSE;

    @JsonIgnore
    @OneToOne(mappedBy = "user", optional = true, orphanRemoval = true)
    @Cascade({CascadeType.ALL})
    private UserPassword userPassword;

    @Transient @JsonIgnore
    private String password;

    @NotNull(message = "Role tidak boleh dikosongkan")
    @ManyToOne
    @JoinColumn(name = "id_role", nullable = false)
    private Role role;


}
