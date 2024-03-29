package io.githubseikodictionaryenginev2.test;

import com.sun.net.httpserver.HttpServer;
import io.github.seikodictionaryenginev2.base.command.Registrator;
import io.github.seikodictionaryenginev2.base.entity.DictionaryFile;
import io.github.seikodictionaryenginev2.base.entity.DictionaryProject;
import io.github.seikodictionaryenginev2.base.entity.code.DictionaryCommandMatcher;
import io.github.seikodictionaryenginev2.base.env.DictionaryEnvironment;
import io.github.seikodictionaryenginev2.base.session.BasicRuntime;
import io.github.seikodictionaryenginev2.base.util.IOUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author kagg886
 * @Date 2023/11/5
 */
public class TestDictionaryCode {

    public static DictionaryEnvironment env;

    @BeforeAll
    public static void initDictionary() {
        env = initEnv();
        Registrator.inject();
        DictionaryCommandMatcher.domainQuoteNew.put("控制台", new Class[]{String.class});

        env.getDicList().refresh();
    }

    @Test
    public void arrayTest() {
        invoke("集合函数测试");
    }

    @Test
    public void settingTest() {
        invoke("设置函数测试");
    }

    @Test
    public void callTest() {
        invoke("调用测试");
    }

    @Test
    public void crossDicTest() {
        invoke("跨词库调用测试");
    }


    @Test
    public void httpTest() throws IOException, InterruptedException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
        server.createContext("/test", httpExchange -> {
            String builder = "服务端接收到新请求:" +
                    "\nmethod:" + httpExchange.getRequestMethod() +
                    "\nHeader:\n   " + httpExchange.getRequestHeaders().entrySet().stream().map((entry) -> entry.getKey() + ":" + entry.getValue() + "\n   ").collect(Collectors.joining()) +
                    "\nbody:" + IOUtil.loadStringFromStream(httpExchange.getRequestBody());
            System.out.println(builder);

            String response = "hello world";
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });
        server.start();
        invoke("http测试");
    }

    @Test
    public void strTest() {
        invoke("字符串测试");
    }

    @Test
    public void regexTest() {
        Matcher m = Pattern.compile("点歌 (.*)").matcher("点歌 111");
        int groups = 0;
        while (m.find()) {
            int i;
            if (groups == 0) {
                for (i = 1; i <= m.groupCount(); i++) {
                    System.out.println(m.group(i));
                }
            }
            groups++;
        }
    }

    @Test
    public void asyncTest() throws InterruptedException {
        invoke("异步测试");
        Thread.sleep(5000);
    }

    @Test
    public void spawnTest() throws InterruptedException {
        invoke("生成测试");
    }


    @Test
    public void reactiveTest() {
        invoke("读写对象测试");
    }

    private static DictionaryEnvironment initEnv() {
        DictionaryEnvironment environment = DictionaryEnvironment.getInstance();

        Path base = new File("mock").toPath();
        if (!base.toFile().isDirectory()) {
            base.toFile().mkdirs();
        }

        //设置mock\dic为伪代码文件的路径。伪代码的扫码等均在此进行。
        environment.setDicRoot(base.resolve("dic").toFile());
        //设置mock\data为伪代码文件产生的文件默认仓储的路径。
        environment.setDicData(base.resolve("data").toAbsolutePath());

        //设置dic的启停配置文件。此文件用于管理词库工程启用停止状态
        environment.setDicConfigPoint(base.resolve("config.json").toFile().getAbsolutePath());


        return environment;
    }


    private static void invoke(String comm) {
        for (DictionaryProject project : DictionaryEnvironment.getInstance().getDicList()) {
            TestRuntime runtime = new TestRuntime(project.getIndexFile(), "");
            runtime.invoke(comm);
        }
    }


    //继承BasicRuntime，三个泛型分别为：事件class，联系人class，消息缓冲区class
    public static class TestRuntime extends BasicRuntime<String, String, StringBuilder> {

        public TestRuntime(DictionaryFile file, String s) {
            super(file, s);
        }

        //在初始化时调用，此方法负责从事件中抽出联系人。
        @Override
        protected String initContact(String EVENT) {
            return EVENT;
        }

        //初始化消息缓冲区
        @Override
        protected StringBuilder initMessageCache() {
            return new StringBuilder();
        }

        //清空缓冲区前会调用这个方法，在此之前请检查缓冲区是否为null
        @Override
        protected void clearMessage0(StringBuilder stringBuilder) {
            if (!stringBuilder.isEmpty()) {
                System.out.println(stringBuilder);
            }
        }

        //向缓冲区添加格式化的字符串。
        //若要向缓冲区添加别的元素请使用自定义方法。
        @Override
        protected void appendMessage(String code) {
            getMessageCache().append(code);
        }
    }
}
