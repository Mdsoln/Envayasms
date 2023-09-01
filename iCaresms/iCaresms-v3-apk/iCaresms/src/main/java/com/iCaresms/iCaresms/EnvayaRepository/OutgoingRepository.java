package com.iCaresms.iCaresms.EnvayaRepository;

import com.iCaresms.iCaresms.EnvayaSMS.OutgoinngSMS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutgoingRepository extends JpaRepository<OutgoinngSMS,Long> {
    List<OutgoinngSMS> findByStatus(String status);
}
