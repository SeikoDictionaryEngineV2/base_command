package io.github.seikodictionaryenginev2.base.command.string;

import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.util.List;

/**
 * @projectName: Seiko
 * @package: com.kagg886.seiko.dic.entity.func.uninterrupted
 * @className: StringUtil
 * @author: kagg886
 * @description: 将原文中的关键词替换成另外一个字符串
 * @date: 2023/2/18 18:17
 * @version: 1.0
 */
public class Replace extends Function {

    public Replace(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?,?,?> runtime, List<Object> args) {
        String dx = ((String) args.get(0));
        String from = ((String) args.get(1));
        String to = ((String) args.get(2));
        return dx.replace(from,to);
    }
}
