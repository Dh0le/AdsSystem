package com.personal.ad.sender.kafka;

import com.alibaba.fastjson.JSON;
import com.personal.ad.mysql.dto.MySqlRowData;
import com.personal.ad.sender.ISender;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Optional;

@Component("kafkaSender")
public class KafkaSender implements ISender {
    @Value("${adconf.kafka.topic}")
    private String topic;

    private KafkaTemplate<String,String> template;
    @Autowired
    public KafkaSender(KafkaTemplate<String, String> template) {
        this.template = template;
    }


    @Override
    public void sender(MySqlRowData rowData) {
        template.send(topic, JSON.toJSONString(rowData));

    }

    @KafkaListener(topics = {"ad-search-mysql-data"},groupId = "ad-search")
    public void processMqlRowData(ConsumerRecord<?,?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if(kafkaMessage.isPresent()){
            Object message = kafkaMessage.get();
            MySqlRowData rowData = JSON.parseObject(message.toString(),MySqlRowData.class);
            System.out.println("kafka mysql row data:"+ JSON.toJSONString(rowData));
        }

    }
}
