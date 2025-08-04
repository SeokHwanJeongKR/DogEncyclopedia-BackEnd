package com.est.mungpe.pedia.repository;

import com.est.mungpe.pedia.domain.Pedia;
import com.est.mungpe.post.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PediaRepository extends JpaRepository<Pedia, Long> {

    List<Pedia> findTop12ByOrderByUpdatedAtDesc();

    Page<Pedia> findAll(Pageable pageable);


}
