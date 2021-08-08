package com.personal.ad.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.netflix.discovery.converters.Auto;
import com.personal.ad.mysql.listener.AggregationListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Component
public class BinlogClient {
    private BinaryLogClient client;

    private final BinlogConfig config;
    private final AggregationListener listener;

    @Autowired
    public BinlogClient(BinlogConfig config, AggregationListener listener) {
        this.config = config;
        this.listener = listener;
    }

    @PostConstruct
    public void connect(){
        new Thread(()->{
            client = new BinaryLogClient(
                    config.getHost(),
                    config.getPort(),
                    config.getUsername(),
                    config.getPassword()
            );
            if(!StringUtils.isEmpty(config.getBinlogName()) && !config.getPosition().equals(-1L)){
                client.setBinlogFilename(config.getBinlogName());
                client.setBinlogPosition(config.getPosition());
            }
            client.registerEventListener(listener);
            try{
                log.info("Connecting to mysql server");
                client.connect();
                log.info("Connecting to mysql server done");
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }).start();
    }

    private void close(){
        try{
            client.disconnect();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
