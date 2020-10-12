package example.vbc.bind.propertyeditor.manager;

import java.beans.PropertyEditorManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PropertyEditorManagerMain {

    public static void main(String[] args) {

        registerPropertyEditor(Hello.class, HelloEditor.class);

        addEditorSearchPath("example.vbc.bind.propertyeditor");
    }


    /**
     *  注册 propertyEditor 到 propertyEditorManager 后就可以被 JavaBeans 搜索到
     */
    public static void registerPropertyEditor(Class<?> targetType, Class<?> editorClass){
        PropertyEditorManager.registerEditor(targetType, editorClass);

        String result = PropertyEditorManager.findEditor(targetType).getClass() == editorClass ? "good" : "error";

        System.out.println(result);
    }

    /**
     *  propertyEditorManager 设置 propertyEditor 的搜索地址
     */
    public static void addEditorSearchPath(String... path){
        final String[] searchPath = PropertyEditorManager.getEditorSearchPath();

        System.out.println("before: " + Arrays.toString(searchPath));

        Set<String> newPath = new HashSet<>(Arrays.asList(searchPath));
        Collections.addAll(newPath, path);

        PropertyEditorManager.setEditorSearchPath(newPath.toArray(new String[newPath.size()]));

        System.out.println("after: " + Arrays.toString(PropertyEditorManager.getEditorSearchPath()));

    }
}
