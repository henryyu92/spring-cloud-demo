package org.mooc.cloud;

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

	/**
	 * Sink: Identifies the contract for the message consumer by providing the destination from which the message is consumed.
	 * Source: Identifies the contract for the message producer by providing the destination to which the produced message is sent.
	 * Processor: Encapsulates both the sink and the source contracts by exposing two destinations that allow consumption and production of messages.
	 */

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
