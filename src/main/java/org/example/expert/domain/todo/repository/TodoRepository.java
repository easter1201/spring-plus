package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom{

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @Query("SELECT t from Todo t WHERE t.weather = :weather AND t.modifiedAt BETWEEN :startDate AND :endDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findByAllConditions(@Param("weather")String weather, @Param("startDate")LocalDateTime startDate, @Param("endDate")LocalDateTime endDate, Pageable pageable);

    @Query("SELECT t FROM Todo t WHERE t.weather = :weather AND t.modifiedAt >= :startDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findByWeatherAndStart(@Param("weather")String weather, @Param("startDate")LocalDateTime startDate, Pageable pageable);

    @Query("SELECT t FROM Todo t WHERE t.weather = :weather ORDER BY t.modifiedAt DESC")
    Page<Todo> findByWeather(@Param("weather")String weather, Pageable pageable);

    @Query("SELECT t FROM Todo t WHERE t.weather = :weather AND t.modifiedAt <= :endDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findByWeatherAndEnd(@Param("weather")String weather, @Param("endDate")LocalDateTime endDate, Pageable pageable);

    @Query("SELECT t FROM Todo t WHERE t.modifiedAt BETWEEN :startDate AND :endDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findByDates(@Param("startDate")LocalDateTime startDate, @Param("endDate")LocalDateTime endDate, Pageable pageable);

    @Query("SELECT t FROM Todo t WHERE t.modifiedAt >= :startDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findByStart(@Param("startDate")LocalDateTime startDate, Pageable pageable);

    @Query("SELECT t FROM Todo t WHERE t.modifiedAt <= :endDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findByEnd(@Param("endDate")LocalDateTime endDate, Pageable pageable);
}
