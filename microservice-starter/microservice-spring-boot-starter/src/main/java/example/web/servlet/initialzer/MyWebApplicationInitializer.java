package example.web.servlet.initialzer;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 *
 * In a Servlet 3.0+ environment, you have the option of configuring the Servlet container
 * programmatically as an alternative or in combination with a web.xml file
 *
 * Java configuration registers and initializes the DispatcherServlet
 */
public class MyWebApplicationInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        // WebApplicationContext
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
//        ac.register(AppConfig.class);
        ContextLoaderListener contextLoaderListener = new ContextLoaderListener(ac);
        servletContext.addListener(contextLoaderListener);
        ac.refresh();

        servletContext.setInitParameter("", "");

        // Create and register the DispatcherServlet
        DispatcherServlet servlet = new DispatcherServlet(ac);

        ServletRegistration.Dynamic registration = servletContext.addServlet("app", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }
}
