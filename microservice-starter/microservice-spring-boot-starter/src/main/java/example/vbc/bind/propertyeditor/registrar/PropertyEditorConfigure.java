package example.vbc.bind.propertyeditor.registrar;

import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class PropertyEditorConfigure {

    @Bean
    public ExoticTypeEditor exoticTypeEditor(){
        return new ExoticTypeEditor();
    }

    @Bean
    public CustomPropertyEditorRegistrar customPropertyEditorRegistrar(){
        return new CustomPropertyEditorRegistrar();
    }

    @Bean
    public CustomEditorConfigurer customEditorConfigurer(){
        CustomEditorConfigurer editorConfigurer = new CustomEditorConfigurer();
        Map<Class<?>, Class<? extends PropertyEditor>> map = new HashMap<>();
        map.put(ExoticType.class, exoticTypeEditor().getClass());
        editorConfigurer.setCustomEditors(map);

        editorConfigurer.setPropertyEditorRegistrars(new CustomPropertyEditorRegistrar[]{customPropertyEditorRegistrar()});

        return editorConfigurer;
    }
}
