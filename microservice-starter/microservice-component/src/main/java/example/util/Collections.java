package example.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Collections {


    public static <T, R> List<T> collectionToList(Collection<R> source, Function<T, R> converter){

        return null;
    }

    public static <T, R> Set<T> collectionToSet(Collection<R> source, Function<T, R> converter){
        return null;
    }

    // https://blog.csdn.net/fzy629442466/article/details/84629422
    public static void main(String[] args) {

        class Person{
            String name;

            public Person(String name){
                this.name = name;
            }

            public String getName(){
                return this.name;
            }
        }

        final Map<String, Long> collect = Stream.of("hello", "world").collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        System.out.println(collect);


        final Map<String, Long> collect1 = Stream.of(new Person("hello"), new Person("world"))
                .collect(Collectors.groupingBy(Person::getName, Collectors.mapping(person -> 1, Collectors.counting())));
        System.out.println(collect1);


        final Map<String, Integer> collect2 = Stream.of(new Person("hello"), new Person("world"))
                .collect(Collectors.groupingBy(Person::getName, Collectors.reducing(0, (Person p) -> 1, Integer::sum)));
        System.out.println(collect2);
    }



}
