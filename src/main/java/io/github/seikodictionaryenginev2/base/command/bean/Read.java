package io.github.seikodictionaryenginev2.base.command.bean;

import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.entity.code.func.type.ArgumentLimiter;
import io.github.seikodictionaryenginev2.base.env.DictionaryEnvironment;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;
import io.github.seikodictionaryenginev2.base.util.storage.JSONObjectStorage;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * @Description 读取一个文件
 * @Author kagg886
 * @Date 2023/11/6
 */
public class Read extends Function implements ArgumentLimiter {
    public Read(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        String path = runtime.getFile().getFather().getName();

        File target = DictionaryEnvironment.getInstance().getDicData().resolve(
                path.substring(0,path.lastIndexOf("."))
        ).resolve(args.get(0).toString()).toFile();

        JSONObjectStorage storage = JSONObjectStorage.obtain(target.getAbsolutePath());

        if (args.size() == 1) {
            return storage;
        }

        String key = args.get(1).toString();
        String def = args.size() == 3 ? String.valueOf(args.get(2).toString()) : "null";
        return Optional.ofNullable(storage.get(key)).orElse(def);
    }

    @Override
    public int getArgumentLength() {
        return 3;
    }
}
