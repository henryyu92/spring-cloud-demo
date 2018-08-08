package org.mooc.cloud;

import org.apache.kafka.common.protocol.types.Field;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@SpringBootApplication
@EnableBinding(Sink.class)
public class MicroserviceStreamApplication {

	/**
	 * Spring Cloud Stream is a framework for building message-driven microservice applications
	 * It provides opinionated configuration of middleware from several vendors,
	 * introducing the concepts of persistent publish-subscribe semantics, consumer groups, and partitions.
	 */

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceStreamApplication.class, args);
	}

	/**
	 * It tries to automatically convert incoming message payloads to type Person
	 *
	 */
	@StreamListener(Sink.INPUT)
	public void handle(Person person){
		System.out.println("Received: " + person);
	}

	public static class Person{
		private String name;
		public String getName(){
			return name;
		}
		public void setName(String name){
			this.name = name;
		}
		public String toString(){
			return this.name;
		}
	}

}
