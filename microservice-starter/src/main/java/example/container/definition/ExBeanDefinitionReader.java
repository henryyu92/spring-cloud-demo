package example.container.definition;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.Resource;

/**
 * BeanDefinition Reader load BeanDefinition from Resources
 */
public class ExBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public ExBeanDefinitionReader(BeanDefinitionRegistry registry){
        super(registry);
    }

    @Override
    public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
        return 0;
    }
}
