package io.github.seikodictionaryenginev2.base.command.global;

import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.util.List;
import java.util.Optional;

/**
 * @Description 获取全局设置，默认值不选默认为null
 * @Author kagg886
 * @Date 2023/11/2
 */
public class SettingGet extends Function {
    public SettingGet(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        String key = args.get(0).toString();
        String def = Optional.ofNullable(args.get(1)).orElse("null").toString();
        return Optional.ofNullable(runtime.getFile().getSetting(key)).orElse(def);
    }
}
