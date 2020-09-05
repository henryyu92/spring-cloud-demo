package example.util;

import java.util.StringJoiner;

/**
 * String 工具类，参考 go strings 包
 */
public final class Strings {

    private Strings(){}

    public static boolean hasLength(String str){
        return str != null && str.length() > 0;
    }

    public static boolean hasPrefix(String str, String prefix){
        return false;
    }

    public static String join(String sep, String... str){
        if (str == null || str.length <= 0){
            return "";
        }
        if (str.length == 1){
            return str[0];
        }
        StringBuilder builder = new StringBuilder(str[0]);
        for (int i = 1; i < str.length; i++){
            builder.append(sep).append(str[i]);
        }
        return builder.toString();
    }


    public static String toDelimitedString(String sep, String... elements){
        StringJoiner joiner = new StringJoiner(sep);
        for (String element : elements) {
            if (hasLength(element)) {
                joiner.add(element);
            }
        }
        return joiner.toString();
    }

}
