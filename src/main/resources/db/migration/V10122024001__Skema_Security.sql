create table c_security_permission
(
    id               varchar(36)                               not null,
    created_at       TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    updated_at       TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) NOT NULL,
    permission_label varchar(100)                              not null,
    permission_value varchar(100)                              not null,
    primary key (id)
);

create table c_security_role
(
    id          varchar(36)                               not null,
    created_at  TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) NOT NULL,
    description varchar(100),
    name        varchar(100)                               not null,
    primary key (id)
);

create table c_security_role_permission
(
    id_role       VARCHAR(36) not null,
    id_permission VARCHAR(36) not null,
    primary key (id_role, id_permission)
);

create table c_security_user
(
    id           varchar(36)                               not null,
    created_at   TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    updated_at   TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) NOT NULL,
    active       boolean,
    name         varchar(100),
    username     varchar(100)                              not null,
    id_role      varchar(36)                               not null,
    primary key (id)
);

create table c_security_user_password
(
    id_user       varchar(36)                               not null,
    created_at    TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL,
    updated_at    TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) NOT NULL,
    user_password varchar(255)                              not null,
    primary key (id_user)
);


insert into c_security_permission (id, created_at, updated_at, permission_label,permission_value) VALUES ('P000','2023-08-09 00:00:00', '2023-08-09 00:00:00','SUPER_ADMIN','super_admin');

insert into c_security_role (id, created_at, updated_at, description,name) VALUES ('R000','2023-08-09 00:00:00', '2023-08-09 00:00:00','Default Super Admin Role','Role Super Admin');

insert into c_security_role_permission (id_role, id_permission) VALUES ('R000','P000');

insert into c_security_user (id, created_at, updated_at, active,name,username,id_role) VALUES
    ('super_admin','2023-08-09 00:00:00', '2023-08-09 00:00:00',true,'Super Admin','superadmin','R000');

insert into c_security_user_password (id_user, created_at, updated_at, user_password) VALUES
    ('super_admin','2023-08-09 00:00:00', '2023-08-09 00:00:00','$2y$10$6xVr5ZxD4E01D8Ib.Bie.edKCH2WWa.pGsTBGm/tW19/ElHBLSTaS');
