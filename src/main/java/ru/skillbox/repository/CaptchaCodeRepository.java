package ru.skillbox.repository;

import java.time.LocalDateTime;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.model.CaptchaCode;

@Repository
@Transactional
public interface CaptchaCodeRepository extends CrudRepository<CaptchaCode, Long> {
    @Modifying
    void deleteByTime(LocalDateTime localDateTime);

    CaptchaCode findBySecretCode(String secretCode);
}
