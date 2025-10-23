package com.sendika.bookstore.repo;

import com.sendika.bookstore.model.Snap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnapRepository extends JpaRepository<Snap, Long> {
}

