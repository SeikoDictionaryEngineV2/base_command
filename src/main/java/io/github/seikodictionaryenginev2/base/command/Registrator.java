package io.github.seikodictionaryenginev2.base.command;

import io.github.seikodictionaryenginev2.base.command.bean.ReadBinary;
import io.github.seikodictionaryenginev2.base.command.bean.ReadObject;
import io.github.seikodictionaryenginev2.base.command.bean.WriteBinary;
import io.github.seikodictionaryenginev2.base.command.bean.WriteObject;
import io.github.seikodictionaryenginev2.base.command.collection.CheckExist;
import io.github.seikodictionaryenginev2.base.command.collection.Clone;
import io.github.seikodictionaryenginev2.base.command.collection.Length;
import io.github.seikodictionaryenginev2.base.command.func.CallMethod;
import io.github.seikodictionaryenginev2.base.command.func.CallMethodAsync;
import io.github.seikodictionaryenginev2.base.command.func.CallMethodCrossDictionary;
import io.github.seikodictionaryenginev2.base.command.func.Delay;
import io.github.seikodictionaryenginev2.base.command.global.SettingGet;
import io.github.seikodictionaryenginev2.base.command.global.SettingKey;
import io.github.seikodictionaryenginev2.base.command.spawn.RandomNumber;
import io.github.seikodictionaryenginev2.base.command.spawn.RandomUUID;
import io.github.seikodictionaryenginev2.base.command.string.*;
import io.github.seikodictionaryenginev2.base.command.web.HTTP;
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

        //调用类
        put("调用", CallMethod.class); //$调用 函数名 {传参(可选)}$ -> 不返回 or 集合
        put("跨词库调用", CallMethodCrossDictionary.class); //$跨词库调用 词库名 函数名 {传参(可选)}$ -> 不返回 or 集合
        put("异步调用", CallMethodAsync.class); //$异步调用 {调用结构}$ -> 不返回
        put("延时", Delay.class); //$延时 毫秒$ -> 不返回

        //读写类
        put("读对象", ReadObject.class); //$读对象 文件路径 默认值 自动保存(可选)$ -> 集合
        put("写对象", WriteObject.class); //$写对象 文件路径 {覆写对象}(可选)$ ->不返回
        put("读字节", ReadBinary.class); //$读字节 文件路径$ -> 集合
        put("写字节", WriteBinary.class); //$写字节 文件路径 [集合]$ -> 不返回

        //生成类
        put("随机数", RandomNumber.class); //$随机数$ $随机数 {最大值}$ $随机数 {最小值} {最大值}$ ->数字
        put("UUID", RandomUUID.class); //$UUID$ -> 字符串

        //网络类
        put("HTTP", HTTP.class); //$HTTP {选项}$ ->集合

        //字符串类
        put("字符串创建", NewString.class); //$字符串创建 集合(字节数组) 编码(可选)$ -> 字符串
        put("字符串分割", Split.class); //$字符串分割 字符串 正则表达式$ ->集合
        put("转大写", UpperCase.class); //$转大写 字符串$ ->字符串
        put("转小写", LowerCase.class); //$转小写 字符串$ ->字符串
        put("关键词检测", Contain.class); //$关键词检测 字符串 子关键词$ ->true/false
        put("替换", Replace.class); //$替换 待替换字符串 原字符串 替换字符串$ ->字符串
        put("正则",Regex.class); //$正则 字符串 表达式$->匹配到的字符串的集合
    }};

    public static void inject() {
        Function.globalManager.putAll(list);

        DictionaryCommandMatcher.domainQuoteNew.put("函数", new Class[]{
                BasicRuntime.class,
                DictionaryFile.class
        });
    }
}
