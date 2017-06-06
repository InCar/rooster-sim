package com.incar.device;

import com.incar.repository.OBDRepository;
import com.incar.util.ApplicationVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by zhouyongbo on 2017/6/6.
 */
@Component
public class DataService implements ApplicationListener<ContextRefreshedEvent> {

    private static OBDRepository obdRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        obdRepository = event.getApplicationContext().getBean(OBDRepository.class);
    }

    /**
     * 获取数据源
     * @param codes
     * @return
     */
    public List<String> getData(String codes){
        if (ApplicationVariable.getDataType() == 1){
           return obdRepository.findAllAndTimeByCodes(codes,ApplicationVariable.getDays());
        }else {
            List<String> allTimeSlot = obdRepository.findAllTimeSlot(codes, ApplicationVariable.getStartTime(), ApplicationVariable.getEndTime());
            return allTimeSlot;
        }
    }


}
