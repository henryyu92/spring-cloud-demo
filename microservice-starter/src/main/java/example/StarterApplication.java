package example;

import example.actuator.monitor.Monitor;
import example.actuator.monitor.MonitoredController;
import example.actuator.monitor.QpsCollector;
import example.container.bean.Student;
import example.container.bean.StudentFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StarterApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(StarterApplication.class, args);

        Monitor bean = context.getBean("monitoredController", Monitor.class);

        for (int i = 0; i < 10; i++){
            bean.test();
        }

        QpsCollector.statistic();


        Student bean1 = context.getBean(Student.class);
        System.out.println(bean1);

        Object bean2 = context.getBean("studentFactoryBean");
        System.out.println(bean2);

        Object bean3 = context.getBean("studentFactoryBean");
        System.out.println(bean3);

        Object bean4 = context.getBean("&studentFactoryBean");
        System.out.println(bean4);

    }
}
