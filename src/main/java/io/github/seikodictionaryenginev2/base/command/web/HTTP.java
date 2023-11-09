package io.github.seikodictionaryenginev2.base.command.web;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.entity.code.func.type.SendMessageWhenPostExecute;
import io.github.seikodictionaryenginev2.base.exception.DictionaryOnRunningException;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;
import io.github.seikodictionaryenginev2.base.util.IOUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 访问HTTP服务器
 * @Author kagg886
 * @Date 2023/11/8
 */
public class HTTP extends Function implements SendMessageWhenPostExecute {
    public HTTP(int line, String code) {
        super(line, code);
    }

    private static final java.util.function.Function<JSONObject, Map<String, String>> converter = jsonObject -> {
        HashMap<String, String> a = new HashMap<>();
        jsonObject.forEach((key, value) -> a.put(key, value.toString()));
        return a;
    };

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        Options options = JSON.parseObject(args.get(0).toString(), Options.class, JSONReader.Feature.FieldBased);
        if (options.url == null) {
            throw new DictionaryOnRunningException("url是必选项!");
        }

        Connection conn = Jsoup.connect(options.url)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .method(options.method)
                .headers(options.header.to(converter))
                .cookies(options.cookie.to(converter));
        if (options.bodyOrData instanceof String) {
            conn.requestBody(((String) options.bodyOrData));
        }
        if (options.bodyOrData instanceof JSONObject) {
            conn.data(((JSONObject) options.bodyOrData).to(converter));
        }

        conn.timeout(options.timeout);

        Connection.Response resp;
        byte[] body;
        try {
            resp = conn.execute();
            body = IOUtil.loadByteFromStream(resp.bodyStream());
        } catch (IOException e) {
            throw new DictionaryOnRunningException("url访问失败:" + e.getMessage());
        }
        Response response = new Response();
        response.code = resp.statusCode();
        response.msg = resp.statusMessage();
        response.body = body;
        response.headers = new JSONObject(resp.headers());
        response.cookie = new JSONObject(resp.cookies());

        return JSON.parseObject(JSON.toJSONString(response, JSONWriter.Feature.FieldBased));
    }

    private static class Response {
        private int code;
        private String msg;
        private byte[] body;

        private JSONObject headers;
        private JSONObject cookie;
    }

    private static class Options {
        private String url; //url
        private Connection.Method method = Connection.Method.GET; //方法
        private JSONObject header = new JSONObject(); //请求头
        private JSONObject cookie = new JSONObject(); //cookie
        private Object bodyOrData = new JSONObject(); //body的类型为String或JSONObject
        private Integer timeout = 10000; //超时时间
    }
}
