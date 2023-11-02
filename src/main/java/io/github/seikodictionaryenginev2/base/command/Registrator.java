package io.github.seikodictionaryenginev2.base.command;

import io.github.seikodictionaryenginev2.base.command.collection.CheckExist;
import io.github.seikodictionaryenginev2.base.command.collection.Clone;
import io.github.seikodictionaryenginev2.base.command.collection.Length;
import io.github.seikodictionaryenginev2.base.command.global.SettingGet;
import io.github.seikodictionaryenginev2.base.command.global.SettingKey;
import io.github.seikodictionaryenginev2.base.entity.code.func.Function;

import java.util.HashMap;

public class Registrator {
    public static final HashMap<String, Class<? extends Function>> list = new HashMap<>() {{
        //集合有关类
        put("集合克隆", Clone.class); //$集合克隆 %对象%$ -> 集合 or null
        put("集合长", Length.class); //$集合长 %对象%$ -> 集合 or null
        put("集合检验", CheckExist.class); //$集合检验 %集合对象% %检验对象%$ -> 布尔值

        //变量类
        put("取设置", SettingGet.class); //$设置 键 默认值(可选)$ -> 字符串
        put("设置键", SettingKey.class); //$设置键$ -> 集合(列表)
    }};
}
