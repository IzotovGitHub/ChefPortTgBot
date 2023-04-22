package ru.izotov.entity;

import lombok.*;
import org.hibernate.Hibernate;
import ru.izotov.enums.UserCodeStatus;

import javax.persistence.*;
import java.util.Objects;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_code",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "code_app_user_id_unique",
                        columnNames = {"code", "app_user_id"}
                )
        })
public class UserCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "app_user_id", nullable = false, updatable = false)
    private AppUser appUser;
    @Column(nullable = false)
    private String code;
    @Enumerated(EnumType.STRING)
    private UserCodeStatus status;


    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserCode userCode = (UserCode) o;
        return id != null
                && Objects.equals(id, userCode.id)
                && Objects.equals(appUser, userCode.appUser)
                && Objects.equals(code, userCode.code)
                && Objects.equals(status, userCode.status);
    }
}
