package io.github.seikodictionaryenginev2.base.command.func;

import io.github.seikodictionaryenginev2.base.entity.DictionaryFile;
import io.github.seikodictionaryenginev2.base.entity.DictionaryProject;
import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.exception.DictionaryOnRunningException;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.util.List;
import java.util.Map;

/**
 * @Description 跨词库调用函数
 * @Author kagg886
 * @Date 2023/11/6
 */
public class CallMethodCrossDictionary extends Function {
    public CallMethodCrossDictionary(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        DictionaryProject project = runtime.getFile().getFather();
        if (project.isSimpleDictionary()) {
            throw new DictionaryOnRunningException("非多词库文件禁止调用此方法");
        }

        DictionaryFile f = project
                .getSubFile()
                .stream()
                .filter((v) -> v.getName().equals(args.get(0).toString()))
                .findFirst().orElseThrow(() -> new DictionaryOnRunningException("找不到词库文件:" + args.get(0).toString()));

        boolean isolate = args.size() > 2;
        var runtime1 = new CallMethod.FunctionRuntime(f, runtime, isolate);

        if (isolate) {
            runtime1.getRuntimeObject().putAll((Map<String, Object>) args.get(2));
        }

        runtime1.invoke(args.get(1).toString());
        return isolate ? runtime1.getRuntimeObject() : null;
    }
}
