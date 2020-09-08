package example.vbc.validation.beanvalidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    /**
     * java bean validator 可以直接注入
     */
    @Autowired
    private javax.validation.Validator javaValidator;

    /**
     * 可以直接注入 java bean validatorFactory
     */
    @Autowired
    private javax.validation.ValidatorFactory javaValidatorFactory;

    /**
     * 也可以注入 spring validator
     */
    @Autowired
    private org.springframework.validation.Validator springValidator;
}
