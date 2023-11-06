package io.github.seikodictionaryenginev2.base.command.bean;

import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.entity.code.func.type.ArgumentLimiter;
import io.github.seikodictionaryenginev2.base.env.DictionaryEnvironment;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;
import io.github.seikodictionaryenginev2.base.util.storage.JSONObjectStorage;

import java.io.File;
import java.util.List;

/**
 * @Description 写入键值对到文件中
 * @Author kagg886
 * @Date 2023/11/6
 */
public class WriteKV extends Function implements ArgumentLimiter {
    public WriteKV(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        String path = runtime.getFile().getFather().getName();

        File target = DictionaryEnvironment.getInstance().getDicData().resolve(
                path.substring(0,path.lastIndexOf("."))
        ).resolve(args.get(0).toString()).toFile();

        JSONObjectStorage storage = JSONObjectStorage.obtain(target.getAbsolutePath());
        if (args.size() >= 3) {
            storage.put(args.get(1).toString(),args.get(2));
        } else {
            storage.remove(args.get(1).toString());
        }
        storage.save();

        return null;
    }

    @Override
    public int getArgumentLength() {
        return 3;
    }
}
