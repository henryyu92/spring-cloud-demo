package example.resource;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Resource Loader load resources from pointed location
 */
public class ExtResourceLoader implements ResourceLoader {
    @Override
    public Resource getResource(String location) {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }
}
