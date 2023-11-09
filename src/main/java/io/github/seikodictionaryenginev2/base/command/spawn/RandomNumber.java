package io.github.seikodictionaryenginev2.base.command.spawn;

import io.github.seikodictionaryenginev2.base.entity.code.func.Function;
import io.github.seikodictionaryenginev2.base.exception.DictionaryOnRunningException;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * @Description 生成随机数
 * @Author kagg886
 * @Date 2023/11/7
 */
public class RandomNumber extends Function {
    public RandomNumber(int line, String code) {
        super(line, code);
    }

    @Override
    protected Object run(BasicRuntime<?, ?, ?> runtime, List<Object> args) {
        if (args.isEmpty()) {
            return Math.random();
        }

        if (args.size() == 1) {
            try {
                int max = Integer.parseInt(args.get(0).toString());
                return new Random().nextInt(max);
            } catch (NumberFormatException e) {
                throw new DictionaryOnRunningException("非法的数字:" + args.get(0).toString());
            }
        }

        try {
            int min = Integer.parseInt(args.get(0).toString());
            int max = Integer.parseInt(args.get(1).toString());
            return new Random().nextInt(max) + min;
        } catch (NumberFormatException e) {
            throw new DictionaryOnRunningException("非法的数字:" + args.get(0).toString());
        }
    }
}
