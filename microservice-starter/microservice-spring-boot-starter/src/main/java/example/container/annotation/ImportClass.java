package example.container.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class ImportClass {

    @Autowired
    private DogTest dogTest;


    public void importTest(){
        System.out.println(dogTest);
    }

    @Bean
    public DogTest dog(){
        return new DogTest();
    }
}
