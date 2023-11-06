package io.github.seikodictionaryenginev2.base.command;

import io.github.seikodictionaryenginev2.base.command.bean.Read;
import io.github.seikodictionaryenginev2.base.command.bean.WriteKV;
import io.github.seikodictionaryenginev2.base.command.bean.WriteObject;
import io.github.seikodictionaryenginev2.base.command.collection.CheckExist;
import io.github.seikodictionaryenginev2.base.command.collection.Clone;
import io.github.seikodictionaryenginev2.base.command.collection.Length;
import io.github.seikodictionaryenginev2.base.command.func.CallMethod;
import io.github.seikodictionaryenginev2.base.command.func.CallMethodCrossDictionary;
import io.github.seikodictionaryenginev2.base.command.global.SettingGet;
import io.github.seikodictionaryenginev2.base.command.global.SettingKey;
import io.github.seikodictionaryenginev2.base.entity.DictionaryFile;
import io.github.seikodictionaryenginev2.base.entity.code.DictionaryCommandMatcher;
import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.util.HashMap;

public class Registrator {
    public static final HashMap<String, Class<? extends Function>> list = new HashMap<>() {{
        //集合有关类
        put("集合克隆", Clone.class); //$集合克隆 {对象}$ -> 集合 or null
        put("集合长", Length.class); //$集合长 {对象}$ -> 集合 or null
        put("集合检验", CheckExist.class); //$集合检验 {集合对象} {检验对象}$ -> 布尔值

        //变量类
        put("取设置", SettingGet.class); //$设置 键 默认值(可选)$ -> 字符串
        put("设置键", SettingKey.class); //$设置键$ -> 集合(列表)

        put("调用", CallMethod.class); //$调用 函数名 {传参}$ -> 不返回

        put("跨词库调用", CallMethodCrossDictionary.class); //$跨词库调用 词库名 函数名 {传参}$


        put("读", Read.class); //$读 文件路径 参数名(可选) 默认值(可选)$
        put("写", WriteKV.class); //$写 文件路径 键 值(可选)$
        put("写对象", WriteObject.class); //$写 文件路径 {覆写对象}$
    }};

    public static void inject() {
        Function.globalManager.putAll(list);

        DictionaryCommandMatcher.domainQuoteNew.put("函数", new Class[]{
                BasicRuntime.class,
                DictionaryFile.class
        });
    }
}
