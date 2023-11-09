package io.github.seikodictionaryenginev2.base.command.spawn;

import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.util.List;
import java.util.UUID;

/**
 * @Description 随机一个UUID
 * @Author kagg886
 * @Date 2023/11/9
 */
public class RandomUUID extends Function {
    public RandomUUID(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }
}
