package com.iCaresms.iCaresms.EnvayaRepository;

import com.iCaresms.iCaresms.EnvayaSMS.SMSMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvayaRepo extends JpaRepository<SMSMessage,Long> {

}
