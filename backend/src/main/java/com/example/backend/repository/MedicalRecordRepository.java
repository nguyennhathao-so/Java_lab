package com.example.backend.repository;

import com.example.backend.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, String> {
    MedicalRecord findTopByUser_UserIdOrderByCheckedDateDesc(String userId);

    @Modifying
    @Query("DELETE FROM MedicalRecord mr WHERE mr.user.userId = :userId")
    void deleteByUserId(@Param("userId") String userId);

    MedicalRecord findTopByUser_UserIdOrderByCheckedDateDescRecordIdDesc(String userId);
}
