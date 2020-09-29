package example.vbc.bind.wrapper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

public interface BeansListener {

    class BeanWrapperPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            System.out.printf("propertyName: %s, old value: %s, new value: %s%n",
                    evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }

    class BeanWrapperVetoableChangeListener implements VetoableChangeListener {

        @Override
        public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
            System.out.printf("propertyName: %s, old value: %s, new value: %s%n",
                    evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }
}
