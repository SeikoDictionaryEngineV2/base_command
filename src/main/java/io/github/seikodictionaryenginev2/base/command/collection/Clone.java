package io.github.seikodictionaryenginev2.base.command.collection;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 复制一个集合对象
 * 成功返回集合，不成功返回null
 * @Author kagg886
 * @Date 2023-11-2 21:37
 */
public class Clone extends Function {
    public Clone(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        Object obj = args.get(0);

        if (obj instanceof List<?>) {
            return new JSONArray(((List<?>) obj));
        }

        if (obj instanceof Map<?,?>) {
            return new JSONObject(((Map<?, ?>) obj));
        }
        return "null";
    }
}
