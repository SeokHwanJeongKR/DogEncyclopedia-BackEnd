package com.est.mungpe.pedia.repository;

import com.est.mungpe.pedia.domain.PediaEditRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PediaEditRequestRepository extends JpaRepository<PediaEditRequest, Long> {

    Page<PediaEditRequest> findByCompletedFalse(Pageable pageable);

}
