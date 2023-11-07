package io.github.seikodictionaryenginev2.base.command.func;

import io.github.seikodictionaryenginev2.base.entity.DictionaryFile;
import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @Description 调用本词库内的函数
 * @Author kagg886
 * @Date 2023/11/5
 */
public class CallMethod extends Function {
    public CallMethod(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        DictionaryFile file = runtime.getFile();

        boolean isolate = args.size() > 1;
        var runtime1 = new FunctionRuntime(file, runtime, isolate);

        if (isolate) {
            runtime1.getRuntimeObject().putAll((Map<String, Object>) args.get(1));
        }

        runtime1.invoke(args.get(0).toString());
        return isolate ? runtime1.getRuntimeObject() : null;
    }


    public static class FunctionRuntime<Event, Contact, MessageCache> extends BasicRuntime<BasicRuntime<Event, Contact, MessageCache>, Contact, MessageCache> {

        private final BasicRuntime<Event, Contact, MessageCache> origin;

        private final boolean prevented;

        public FunctionRuntime(DictionaryFile file, BasicRuntime<Event, Contact, MessageCache> eventContactMessageCacheBasicRuntime, boolean prevented) {
            super(file, eventContactMessageCacheBasicRuntime);
            this.origin = eventContactMessageCacheBasicRuntime;
            this.prevented = prevented;
        }


        protected BasicRuntime<Event, Contact, MessageCache> getOrigin() {
            return origin;
        }

        @Override
        protected Contact initContact(BasicRuntime<Event, Contact, MessageCache> EVENT) {
            return EVENT.getContact();
        }

        @Override
        protected MessageCache initMessageCache() {
            return origin.getMessageCache();
        }

        @Override
        protected void clearMessage0(MessageCache messageCache) {
            try {
                Method method = origin.getClass().getDeclaredMethod("clearMessage0", Object.class);
                method.setAccessible(true);
                method.invoke(origin, messageCache);
            } catch (Exception ignored) {
            }
        }

        @Override
        protected void appendMessage(String str) {
            try {
                Method method = origin.getClass().getDeclaredMethod("appendMessage", String.class);
                method.setAccessible(true);
                method.invoke(origin, str);
            } catch (Exception ignored) {
            }
        }

        @Override
        public MessageCache getMessageCache() {
            return origin.getMessageCache();
        }

        @Override
        protected void initObject(String command, BasicRuntime<Event, Contact, MessageCache> eventContactMessageCacheBasicRuntime) {
            //不需要添加obj
        }

        @Override
        public Stack<String> getExceptionStacks() {
            return origin.getExceptionStacks();
        }

        @Override
        public DictionaryFile getFile() {
            return origin.getFile();
        }

        @Override
        public Contact getContact() {
            return origin.getContact();
        }

        @Override
        public void setContact(Contact contact) {
            origin.setContact(contact);
        }

        @Override
        public void clearMessage() {
            //函数内无需sendMessage
//            origin.clearMessage();
        }

        @Override
        public HashMap<String, Object> getRuntimeObject() {
            return origin.getRuntimeObject();
        }
    }

}
