package io.github.seikodictionaryenginev2.base.command.bean;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import io.github.seikodictionaryenginev2.base.Util;
import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.env.DictionaryEnvironment;
import io.github.seikodictionaryenginev2.base.exception.DictionaryOnRunningException;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;
import io.github.seikodictionaryenginev2.base.util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

/**
 * @Description
 * @Author kagg886
 * @Date 2023/11/9
 */
public class WriteBinary extends Function {
    public WriteBinary(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        String path = runtime.getFile().getFather().getName();

        File target = DictionaryEnvironment.getInstance().getDicData().resolve(
                path.substring(0, path.lastIndexOf("."))
        ).resolve(args.get(0).toString()).toFile();

        if (args.size() == 1) {
            target.delete();
            return null;
        }

        try {
            IOUtil.writeByteToFile(target.getAbsolutePath(), Util.json2byte(((JSONArray) args.get(1))));
        } catch (IOException e) {
            throw new DictionaryOnRunningException("写入二进制失败");
        }
        return null;
    }
}
