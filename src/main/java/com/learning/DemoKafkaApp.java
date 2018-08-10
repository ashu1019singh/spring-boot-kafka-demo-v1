package com.learning;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class DemoKafkaApp implements CommandLineRunner {

	public static Logger logger = LoggerFactory.getLogger(DemoKafkaApp.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoKafkaApp.class, args).close();
	}

	@Autowired
	private KafkaTemplate<String, String> template;

	private final CountDownLatch latch = new CountDownLatch(3);

	@Override
	public void run(String... args) throws Exception {
		this.template.send("test1", "foo1");
		this.template.send("test1", "foo2");
		this.template.send("test1", "foo3");
		latch.await(60, TimeUnit.SECONDS);
		logger.info("All Received");
	}

	@KafkaListener(topics = "test1")
	public void listen(ConsumerRecord<?, ?> cr) throws Exception {
		logger.info(cr.toString());
		latch.countDown();
	}
}
