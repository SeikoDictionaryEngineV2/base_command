package io.github.seikodictionaryenginev2.base.command.global;

import io.github.seikodictionaryenginev2.base.entity.DictionaryFile;
import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 获取设置的键列表
 * @Author kagg886
 * @Date 2023/11/2
 */
public class SettingKey extends Function {
    public SettingKey(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {

        try {
            Field f = DictionaryFile.class.getDeclaredField("settings");
            f.setAccessible(true);
            return new ArrayList<>(((Map<String, String>) f.get(runtime.getFile())).keySet());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
