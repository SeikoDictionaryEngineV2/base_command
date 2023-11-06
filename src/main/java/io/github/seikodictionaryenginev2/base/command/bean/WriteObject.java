package io.github.seikodictionaryenginev2.base.command.bean;

import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.env.DictionaryEnvironment;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;
import io.github.seikodictionaryenginev2.base.util.storage.JSONObjectStorage;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Description 向文件写入内容
 * @Author kagg886
 * @Date 2023/11/6
 */
public class WriteObject extends Function {

    public WriteObject(int line, String code) {
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
            JSONObjectStorage.destroy(target.getAbsolutePath());
            target.delete();
            return null;
        }
        
        storage.clear();
        storage.putAll(((Map) args.get(1)));

        storage.save();

        return null;
    }
}
