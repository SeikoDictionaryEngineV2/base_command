package io.github.seikodictionaryenginev2.base.command.string;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.entity.code.func.type.ArgumentLimiter;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description 正则匹配
 * @Author kagg886
 * @Date 2023/11/9
 */
public class Regex extends Function implements ArgumentLimiter {
    public Regex(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        Pattern p = Pattern.compile(((String) args.get(1)));
        Matcher m = p.matcher(((String) args.get(0)));

        JSONArray array = new JSONArray();
        while (m.find()) {
            array.add(m.group());
        }

        m.reset();

        JSONArray array1 = new JSONArray();
        while (m.find()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                array1.add(m.group(i));
            }
        }
        return new JSONObject() {{
           put("result",array);
           put("group",array1);
        }};
    }

    @Override
    public int getArgumentLength() {
        return 2;
    }
}
