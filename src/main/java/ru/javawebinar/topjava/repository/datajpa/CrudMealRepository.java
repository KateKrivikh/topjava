package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer>{
    Meal findByIdAndUserId(int id, int userId);

    @Transactional
    int deleteByIdAndUserId(int id, int userId);

    List<Meal> getAllByUserIdOrderByDateTimeDesc(int userId);

    List<Meal> getAllByUserIdAndDateTimeGreaterThanEqualAndDateTimeLessThanOrderByDateTimeDesc(int userId, LocalDateTime startDate, LocalDateTime endDate);
}
