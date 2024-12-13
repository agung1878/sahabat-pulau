package org.sahabat.pulau.entity.security;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.sahabat.pulau.entity.BaseEntity;

@Entity
@Table(name = "c_security_role")
@Data
public class Role extends BaseEntity {

    private static final long serialVersionUID = 8483344987127880471L;

    @Size(max = 100)
    @NotEmpty(message = "Nama role tidak boleh kosong")
    @Column(name = "name", nullable = false, length = 15, unique = true)
    private String name;

    @Size(max = 100)
    @Column(name = "description", length = 100)
    private String description;

    @ManyToMany
    @OrderBy("permissionValue asc")
    @JoinTable(
            name="c_security_role_permission",
            joinColumns=@JoinColumn(name="id_role", nullable=false, columnDefinition = "VARCHAR(36)"),
            inverseJoinColumns=@JoinColumn(name="id_permission", nullable=false, columnDefinition = "VARCHAR(36)")
    )
    private Set<Permission> permissions = new HashSet<Permission>();

    public boolean hasPermission(String id){
        for (Permission p:permissions) {
            if(p.getId().equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    public List<String> getPermissionList(){
        List<String> listPerms = new ArrayList<>();
        for (Permission permission: this.permissions) {
            listPerms.add(permission.getId());
        }
        return listPerms;
    }

}
