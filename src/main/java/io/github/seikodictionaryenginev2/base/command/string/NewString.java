package io.github.seikodictionaryenginev2.base.command.string;

import com.alibaba.fastjson2.JSONArray;
import io.github.seikodictionaryenginev2.base.Util;
import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @Description 根据字节集合创建字符串
 * @Author kagg886
 * @Date 2023/11/9
 */
public class NewString extends Function {
    public NewString(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        byte[] byt = Util.json2byte(((JSONArray) args.get(0)));
        return new String(byt, Charset.forName(args.size() >= 2 ? args.get(1).toString() : "UTF-8"));
    }
}
