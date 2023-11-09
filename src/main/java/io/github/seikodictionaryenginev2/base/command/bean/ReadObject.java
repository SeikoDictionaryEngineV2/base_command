package io.github.seikodictionaryenginev2.base.command.bean;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.entity.code.func.type.ArgumentLimiter;
import io.github.seikodictionaryenginev2.base.env.DictionaryEnvironment;
import io.github.seikodictionaryenginev2.base.exception.DictionaryOnRunningException;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;
import io.github.seikodictionaryenginev2.base.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

/**
 * @Description 读入一个JSON文件，并转换成集合对象
 * @Author kagg886
 * @Date 2023/11/6
 */
public class ReadObject extends Function implements ArgumentLimiter {
    public ReadObject(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        String path = runtime.getFile().getFather().getName();

        File target = DictionaryEnvironment.getInstance().getDicData().resolve(
                path.substring(0, path.lastIndexOf("."))
        ).resolve(args.get(0).toString()).toFile();

        Object listOrKV;
        try {
            listOrKV = JSON.parse(IOUtil.loadByteFromStream(new FileInputStream(target)));

            if (!(listOrKV instanceof JSONObject || listOrKV instanceof JSONArray)) {
                throw new IllegalArgumentException("文件内容不是集合格式！");
            }
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                listOrKV = JSON.parse(args.get(1).toString());
                if (!(listOrKV instanceof JSONObject || listOrKV instanceof JSONArray)) {
                    throw new DictionaryOnRunningException("默认值必须符合JSON格式!");
                }
            } else {
                throw new DictionaryOnRunningException("文件读取失败:" + e.getClass().getName() + "->" + e.getMessage());
            }
        }

        boolean isReactive = args.size() >= 3 && Boolean.parseBoolean(args.get(2).toString());
        if (!isReactive) {
            return listOrKV;
        }
        if (listOrKV instanceof JSONObject) {
            Object finalListOrKV = listOrKV;
            return Proxy.newProxyInstance(JSONObject.class.getClassLoader(), new Class[]{Map.class}, (o, method, objects) -> {
                Object rtn = method.invoke(finalListOrKV, objects);
                if (method.getName().equals("put")) {
                    IOUtil.writeStringToFile(target.getAbsolutePath(), finalListOrKV.toString());
                }
                return rtn;
            });
        }

        Object finalListOrKV = listOrKV;
        return Proxy.newProxyInstance(JSONObject.class.getClassLoader(), new Class[]{List.class}, (o, method, objects) -> {
            Object rtn = method.invoke(finalListOrKV, objects);
            if (method.getName().equals("add")) {
                IOUtil.writeStringToFile(target.getAbsolutePath(), finalListOrKV.toString());
            }
            return rtn;
        });
    }

    @Override
    public int getArgumentLength() {
        return 3;
    }
}
