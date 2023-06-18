package my.home.legacyapp.repository;

import my.home.legacyapp.entity.BusinessEntity;
import my.home.legacyapp.entity.BusinessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BusinessRepository extends JpaRepository<BusinessEntity, Long> {
    @Query("select b from BusinessEntity b where b.createdAt between ?1 and ?2 order by b.createdAt")
    Page<BusinessEntity> findByCreatedAtBetween(LocalDateTime createdAtStart, LocalDateTime createdAtEnd, Pageable pageable);

    @Query("select b from BusinessEntity b where b.type in ?1")
    Page<BusinessEntity> findByTypeIn(Collection<BusinessType> types, Pageable pageable);
}
