package example.vbc.bind.propertyeditor;

import example.vbc.bind.propertyeditor.manager.Hello;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.time.LocalDate;
import java.util.Arrays;

public class PropertyEditorMain {

    public static void main(String[] args) {

        propertyEditorManager();
    }


    public static void propertyEditorManager(){
        String[] searchPath = PropertyEditorManager.getEditorSearchPath();
        Arrays.stream(searchPath).forEach(System.out::println);

        PropertyEditorManager.registerEditor(LocalDate.class, CustomLocalDateEditor.class);
        PropertyEditor editor2 = PropertyEditorManager.findEditor(LocalDate.class);
        System.out.println(editor2);

        PropertyEditor editor = PropertyEditorManager.findEditor(Boolean.class);
        System.out.println(editor);

        PropertyEditorManager.setEditorSearchPath(new String[]{"example.vbc.bind.propertyeditor"});
        PropertyEditor editor1 = PropertyEditorManager.findEditor(Hello.class);
        System.out.println(editor1);
    }
}
