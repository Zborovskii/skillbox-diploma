package ru.skillbox.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.enums.GlobalSettings;
import ru.skillbox.model.GlobalSetting;

@Repository
public interface GlobalSettingsRepository extends CrudRepository<GlobalSetting, Integer> {

    Optional<GlobalSetting> findByCode(GlobalSettings.Code code);
}
