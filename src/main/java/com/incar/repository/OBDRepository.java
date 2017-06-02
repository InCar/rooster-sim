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

    @Query(value = "select o.obdCode from t_obd_history o where (concat(?1)  is null or o.obdCode in (?1) ) group by o.obdCode ",nativeQuery = true)
    List<String> findAllDifferentCodes(List<String> obdCodes);

    @Query(value = "select o.content from t_obd_history o " +
            "where o.obdCode = ?1 " +
            " and (?2 is null or o.receiveDate >= " +
            "date_sub((SELECT MAX(ot.receiveDate) from t_obd_history ot where ot.obdCode = ?1),interval ?2 day)) ORDER BY receiveDate",nativeQuery = true)
    List<String> findAllAndTimeByCodes(String obdCode ,Integer intervalDays);
}
