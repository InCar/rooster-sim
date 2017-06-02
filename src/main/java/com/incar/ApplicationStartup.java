package com.incar;

import com.incar.repository.OBDRepository;
import com.incar.util.OBDRunParameter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by zhouyongbo on 2017/6/1.
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        OBDRepository obdRepository = event.getApplicationContext().getBean(OBDRepository.class);
        new OBDRunParameter().init(obdRepository);
    }
}
