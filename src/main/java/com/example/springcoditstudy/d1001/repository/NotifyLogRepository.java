package com.example.springcoditstudy.d1001.repository;

import com.example.springcoditstudy.d1001.entity.NotifyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyLogRepository extends JpaRepository<NotifyLog,Long> {

}
