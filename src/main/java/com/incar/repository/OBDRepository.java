package com.incar.repository;

import com.incar.entity.ObdHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhouyongbo on 2017/5/31.
 */
@Repository
public interface OBDRepository extends JpaRepository<ObdHistory, Integer> {

    @Query(value = "select o.* from t_obd_history o " +
            "where o.obdCode = ?1 " +
            " and (?2 is null or o.receiveDate >= " +
            "date_sub((SELECT MAX(ot.receiveDate) from t_obd_history ot where ot.obdCode = ?1),interval ?2 day)) ORDER BY receiveDate",nativeQuery = true)
    List<ObdHistory> findAllAndTime(String obdCode ,Integer intervalDays);

//    void findAllDifferentCodes();
}
