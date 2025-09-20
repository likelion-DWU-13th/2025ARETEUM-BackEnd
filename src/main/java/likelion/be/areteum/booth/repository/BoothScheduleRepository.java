package likelion.be.areteum.booth.repository;

import likelion.be.areteum.booth.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BoothScheduleRepository extends JpaRepository<BoothSchedule, Integer> {

    List<BoothSchedule> findByBoothIdOrderByEventDateAscStartTimeAsc(Integer boothId);

    List<BoothSchedule> findByBoothIdAndEventDateOrderByStartTimeAsc(Integer boothId, LocalDate date);

    // DIFF/삭제용
    List<BoothSchedule> findByBoothId(Integer boothId);

    @Query(value = """
        select s from BoothSchedule s
        where s.eventDate = :date
          and (:category is null or s.booth.category = :category)
          and (:sub is null or s.booth.subCategory = :sub)
          and (:q is null or lower(s.booth.name) like lower(concat('%', :q, '%')))
        """,
            countQuery = """
        select count(s) from BoothSchedule s
        where s.eventDate = :date
          and (:category is null or s.booth.category = :category)
          and (:sub is null or s.booth.subCategory = :sub)
          and (:q is null or lower(s.booth.name) like lower(concat('%', :q, '%')))
        """)
    Page<BoothSchedule> filterPaged(@Param("date") LocalDate date,
                                    @Param("category") Category category,
                                    @Param("sub") SubCategory sub,
                                    @Param("q") String q,
                                    Pageable pageable);

    //페이지네이션 없는 버전
    @Query("""
        select s from BoothSchedule s
        where s.eventDate = :date
          and (:category is null or s.booth.category = :category)
          and (:q is null or lower(s.booth.name) like lower(concat('%', :q, '%')))
        order by s.booth.name asc
        """)
    List<BoothSchedule> filterAll(@Param("date") LocalDate date,
                                  @Param("category") Category category,
                                  @Param("q") String q);

    boolean existsByBoothIdAndEventDateAndStartTimeAndEndTime(
            Integer boothId, java.time.LocalDate d, java.time.LocalTime st, java.time.LocalTime et);

    void deleteByBoothId(Integer boothId);
}