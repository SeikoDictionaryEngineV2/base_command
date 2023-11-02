package io.github.seikodictionaryenginev2.base.command.collection;

import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.util.List;
import java.util.Map;

/**
 * @Description 获取集合长度
 * @Author kagg886
 * @Date 2023/11/2
 */
public class Length extends Function {

    public Length(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        Object o = args.get(0);
        if (o instanceof List<?>) {
            return ((List<?>) o).size();
        }
        if (o instanceof Map<?,?>) {
            return ((Map<?, ?>) o).size();
        }

        return "null";
    }
}
