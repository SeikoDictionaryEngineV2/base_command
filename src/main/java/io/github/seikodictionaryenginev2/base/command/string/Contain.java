package io.github.seikodictionaryenginev2.base.command.string;

import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.util.List;

/**
 * @projectName: Seiko
 * @package: com.kagg886.seiko.dic.entity.func.uninterrupted
 * @className: StringUtil
 * @author: kagg886
 * @description: 检测字符串内是否存在子字符串
 * @date: 2023/2/18 18:44
 * @version: 1.0
 */
public class Contain extends Function {

    public Contain(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        String dx = ((String) args.get(0));
        String regex = ((String) args.get(1));
        return String.valueOf(dx.contains(regex)); //和其他伪代码方法兼容
    }
}