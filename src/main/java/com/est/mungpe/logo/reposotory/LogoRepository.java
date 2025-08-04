package com.est.mungpe.logo.reposotory;

import com.est.mungpe.logo.domain.Logo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogoRepository extends JpaRepository<Logo, Long> {
}
