package org.sahabat.pulau.entity.security;

import jakarta.persistence.*;
import lombok.*;
import org.sahabat.pulau.entity.BaseEntity;


/**
 *
 * @author anggi
 */
@Entity
@Table(name = "c_security_user_password")
@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class UserPassword extends BaseEntity {

    private static final long serialVersionUID = -7064711860149422914L;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_user", nullable = false, columnDefinition = "varchar(36)")
    private User user;

    @Column(nullable = false, name = "user_password")
    private String password;
}
