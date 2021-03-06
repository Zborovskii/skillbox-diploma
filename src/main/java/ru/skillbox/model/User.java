package ru.skillbox.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(of = {"id"})
@ToString(callSuper = true, of = {"name"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_moderator")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isModerator;

    @Column(name = "reg_time")
    private LocalDateTime regTime;

    private String name;
    private String email;
    private String password;
    private String code;

    @Column(length = 65535, columnDefinition = "TEXT")
    @Type(type = "text")
    private String photo;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<PostVote> votes = new ArrayList<>();
}
