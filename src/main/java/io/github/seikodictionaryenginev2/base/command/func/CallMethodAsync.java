package io.github.seikodictionaryenginev2.base.command.func;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.annotation.JSONField;
import io.github.seikodictionaryenginev2.base.entity.DictionaryFile;
import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.exception.DictionaryOnRunningException;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Description 不阻塞的调用本词库内的函数
 * @Author kagg886
 * @Date 2023/11/7
 */
public class CallMethodAsync extends Function {
    public CallMethodAsync(int line, String code) {
        super(line, code);
    }

    @SuppressWarnings("all")
    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        DictionaryFile file = runtime.getFile();
        Options op = JSON.parseObject(args.get(0).toString(), Options.class);

        if (op.command == null) {
            throw new DictionaryOnRunningException("command和delay为必选项!");
        }

        BasicRuntime<?, ?, ?> origin = runtime;
        try {
            Constructor<? extends BasicRuntime> con = runtime.getClass().getConstructor(DictionaryFile.class, runtime.getEvent().getClass());
            runtime = con.newInstance(file, runtime.getEvent()); //复制一个空Runtime
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        FunctionRuntime runtime1 = new FunctionRuntime(file, runtime, true); //异步执行的函数

        runtime1.getRuntimeObject().putAll(op.args != null ? op.args : origin.getRuntimeObject());

        BasicRuntime<?, ?, ?> finalRuntime = runtime;
        CompletableFuture.runAsync(() -> {
            runtime1.invoke(op.command);
            //若回调函数!=null，执行回调函数
            if (op.call != null) {
                FunctionRuntime runtime2 = new FunctionRuntime(file, finalRuntime, true);
                runtime2.getRuntimeObject().putAll(runtime1.getRuntimeObject());
                runtime2.invoke(op.call);
            }
        });
        return null;
    }

    @SuppressWarnings("all")
    private static class FunctionRuntime<Event, Contact, MessageCache> extends CallMethod.FunctionRuntime<Event, Contact, MessageCache> {

        public FunctionRuntime(DictionaryFile file, BasicRuntime basicRuntime, boolean prevented) {
            super(file, basicRuntime, prevented);
        }

        @Override
        protected void initObject(String command, BasicRuntime<Event, Contact, MessageCache> eventContactMessageCacheBasicRuntime) {
            getRuntimeObject().put("缓冲区", initMessageCache());
        }

        @Override
        protected MessageCache initMessageCache() {
            try {
                Method m = getOrigin().getClass().getDeclaredMethod("initMessageCache");
                m.setAccessible(true);
                return ((MessageCache) m.invoke(getOrigin()));
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

        @Override
        public void clearMessage() {
            getOrigin().clearMessage();
        }

        @Override
        protected void appendMessage(String str) {
            try {
                Method m = getOrigin().getClass().getDeclaredMethod("appendMessage", String.class);
                m.setAccessible(true);
                m.invoke(getOrigin(),str);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
    }

    private static class Options {

        @JSONField(deserializeFeatures = JSONReader.Feature.ErrorOnNoneSerializable)
        private String command;

        @JSONField(deserializeFeatures = JSONReader.Feature.NullOnError)
        private String call;

        public JSONObject getArgs() {
            return args;
        }

        public void setArgs(JSONObject args) {
            this.args = args;
        }

        @JSONField(deserializeFeatures = JSONReader.Feature.NullOnError)
        private JSONObject args;

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public String getCall() {
            return call;
        }

        public void setCall(String call) {
            this.call = call;
        }

    }
}
