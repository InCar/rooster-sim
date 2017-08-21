package com.incar.sim.dao;/**
 * Created by fanbeibei on 2017/8/21.
 */

import com.incar.sim.entity.ObdHistory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Fan Beibei
 * @Description: 描述
 * @date 2017/8/21 10:13
 */
@Component
public class ObdHistoryDao extends BaseDao {


    /**
     * 查询obd历史数据
     *
     * @param obdCode
     * @param startTime
     * @param endTime
     * @return
     */
    public List<ObdHistory> queryObdHistoryList(String obdCode, Date startTime, Date endTime) {

        String sql = "select " + getEntityColumnSql(ObdHistory.class, "t") +
                " from t_obd_history t where t.obdCode=? and t.receiveDate > ? and t.receiveDate < ?";
        List<ObdHistory> list = getJdbcTemplate().query(sql,
                new Object[]{obdCode, startTime, endTime},
                new EntityMapper(ObdHistory.class));
        if (null != list && list.size() > 0) {
            return list;
        }

        return null;
    }

}
