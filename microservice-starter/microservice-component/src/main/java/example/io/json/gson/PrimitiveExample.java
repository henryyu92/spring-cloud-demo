package example.io.json.gson;

import com.google.gson.Gson;

import java.lang.reflect.Field;

public class PrimitiveExample {

    public static void serialization() {

        Gson gson = new Gson();
        System.out.println(gson.toJson(1));


    }

    private String name;

    public static void main(String[] args) {
        Field[] fields = PrimitiveExample.class.getDeclaredFields();
        for (Field field : fields){
            System.out.println(field.getClass());
            System.out.println(field.getGenericType());
        }
    }

}
