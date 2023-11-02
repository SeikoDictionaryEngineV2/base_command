package io.github.seikodictionaryenginev2.base.command.collection;

import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.exception.DictionaryOnRunningException;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.util.List;
import java.util.Map;

/**
 * @Description 检查列表值是否存在，检查键值对键是否存在。传入非集合会抛错
 * @Author kagg886
 * @Date 2023/11/2
 */
public class CheckExist extends Function {
    public CheckExist(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        Object a = args.get(0);

        if (a instanceof Map<?,?>) {
            return ((Map<?, ?>) a).containsKey(((Map<?, ?>) a).get(1));
        }

        if (a instanceof List<?>) {
            return ((List<?>) a).contains(((List<?>) a).get(1));
        }
        throw new DictionaryOnRunningException("第一个参数不是集合对象!");
    }
}
