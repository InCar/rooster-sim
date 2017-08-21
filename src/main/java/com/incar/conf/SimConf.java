package com.incar.conf;/**
 * Created by fanbeibei on 2017/8/21.
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Fan Beibei
 * @Description: 描述
 * @date 2017/8/21 9:42
 */
@Configuration
@ImportResource(locations={"classpath:*.xml"})
public class SimConf {
    private String serverAddr;
    private String serverPort;

    @Value("server.address")
    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }
    @Value("server.port")
    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public String getServerPort() {
        return serverPort;
    }
}
