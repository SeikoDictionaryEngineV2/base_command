package io.github.seikodictionaryenginev2.base.command.collection;

import com.alibaba.fastjson2.JSON;
import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.entity.code.func.type.ArgumentLimiter;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.util.List;

public class ToCollection extends Function implements ArgumentLimiter {

    public ToCollection(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        return JSON.parse(args.get(0).toString());
    }

    @Override
    public int getArgumentLength() {
        return 1;
    }
}
