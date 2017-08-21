package com.incar;

import com.incar.conf.SimConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by zhouyongbo on 2017/5/31.
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private SimConf conf;

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }



    @Override
    public void run(String... args) throws Exception {
        System.out.println(conf);




    }


}
