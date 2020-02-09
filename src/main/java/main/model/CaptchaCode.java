package main.model;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "captcha_codes")
public class CaptchaCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @UpdateTimestamp
    @Column(name = "time")
    private Date time;

    @NotNull
    private byte code;

    @NotNull
    @Column(name = "secret_code")
    private byte secretCode;

}
