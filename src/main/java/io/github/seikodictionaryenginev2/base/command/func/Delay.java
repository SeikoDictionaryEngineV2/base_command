package io.github.seikodictionaryenginev2.base.command.func;

import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.entity.code.func.type.SendMessageWhenPostExecute;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.util.List;

/**
 * @Description
 * @Author kagg886
 * @Date 2023/11/7
 */
public class Delay extends Function implements SendMessageWhenPostExecute {

    public Delay(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        try {
            Thread.sleep(Long.parseLong(args.get(0).toString()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
