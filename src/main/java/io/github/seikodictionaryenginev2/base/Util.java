package io.github.seikodictionaryenginev2.base;

import com.alibaba.fastjson2.JSONArray;

import java.text.MessageFormat;

/**
 * @Description
 * @Author kagg886
 * @Date 2023/11/9
 */
public class Util {
    public static byte[] json2byte(JSONArray arr) {
        byte[] byt = new byte[arr.size()];
        for (int i = 0; i < byt.length; i++) {
            byt[i] = arr.getByte(i);
        }
        return byt;
    }

    public static String buildBraceTemplate(String format, Object... args) {
        StringBuilder builder = new StringBuilder();
        String[] p = format.split("\\{\\}");
        if (p.length - 1 != args.length) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < p.length; i++) {
            builder.append(p[i]);
            builder.append(args[i].toString());
        }
        return builder.toString();
    }
}
