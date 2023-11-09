package io.github.seikodictionaryenginev2.base.command.bean;

import com.alibaba.fastjson2.JSONArray;
import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.env.DictionaryEnvironment;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;
import io.github.seikodictionaryenginev2.base.util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * @Description 读取二进制文件 文件不存在时返回空集合
 * @Author kagg886
 * @Date 2023/11/9
 */
public class ReadBinary extends Function {
    public ReadBinary(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        String path = runtime.getFile().getFather().getName();

        File target = DictionaryEnvironment.getInstance().getDicData().resolve(
                path.substring(0, path.lastIndexOf("."))
        ).resolve(args.get(0).toString()).toFile();

        try {
            return JSONArray.parseArray(Arrays.toString(IOUtil.loadByteFromStream(Files.newInputStream(target.toPath()))));
        } catch (IOException e) {
            return new JSONArray();
        }
    }
}
