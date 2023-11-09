package io.github.seikodictionaryenginev2.base.command.string;


import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.util.List;
import java.util.Locale;

/**
 * @projectName: Seiko
 * @package: com.kagg886.seiko.dic.entity.func.uninterrupted
 * @className: StringUtil
 * @author: kagg886
 * @description: $转小写 变量名 存入变量(可选)$
 * @date: 2023/2/18 18:48
 * @version: 1.0
 */
public class LowerCase extends Function {
    public LowerCase(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        return ((String) args.get(0)).toLowerCase(Locale.ROOT);
    }
}