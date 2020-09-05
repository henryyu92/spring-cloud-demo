package example.vbc.validation.message;

import org.springframework.util.StringUtils;
import org.springframework.validation.MessageCodesResolver;

import java.util.Collections;
import java.util.StringJoiner;

/**
 * MessageCodeResolver 将校验失败的错误码注册用于 MessageSource 输出错误信息，默认的实现是 DefaultMessageCodesResolver
 */
public class CustomMessageCodeResolver implements MessageCodesResolver {

    public static final String CODE_SEPARATOR = ":";

    @Override
    public String[] resolveMessageCodes(String errorCode, String objectName) {
        return StringUtils.toStringArray(Collections.singleton(errorCode + ":" + objectName));
    }

    @Override
    public String[] resolveMessageCodes(String errorCode, String objectName, String field, Class<?> fieldType) {

        StringJoiner joiner = new StringJoiner(CODE_SEPARATOR);
        joiner.add(errorCode).add(objectName).add(field);
        if (fieldType != null){
            joiner.add(fieldType.getName());
        }

        return new String[]{joiner.toString()};
    }
}
