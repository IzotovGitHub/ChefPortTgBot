package ru.izotov.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import ru.izotov.enums.UserStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tgUserId;
    @CreationTimestamp
    private LocalDateTime creationDate;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private Boolean isActive;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppUser appUser = (AppUser) o;
        return id != null && Objects.equals(id, appUser.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
