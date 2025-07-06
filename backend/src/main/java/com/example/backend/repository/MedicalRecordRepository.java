package com.example.backend.repository;

import com.example.backend.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, String> {
    MedicalRecord findTopByUser_UserIdOrderByCheckedDateDesc(String userId);

    MedicalRecord findTopByUser_UserIdOrderByCheckedDateDescRecordIdDesc(String userId);
}
