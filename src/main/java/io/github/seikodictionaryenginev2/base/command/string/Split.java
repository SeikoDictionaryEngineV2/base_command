package io.github.seikodictionaryenginev2.base.command.string;

import com.alibaba.fastjson2.JSONArray;
import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @projectName: Seiko
 * @package: com.kagg886.seiko.dic.entity.func.uninterrupted
 * @className: StringUtil
 * @author: kagg886
 * @description: 以正则表达式分割文字为子数组
 * @date: 2023/2/18 18:23
 * @version: 1.0
 */
public class Split extends Function {

    public Split(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?,?,?> runtime, List<Object> args) {
        String dx = ((String) args.get(0));
        String split = ((String) args.get(1));
        return new JSONArray(Stream.of(dx.split(split)).collect(Collectors.toList()));
    }
}
