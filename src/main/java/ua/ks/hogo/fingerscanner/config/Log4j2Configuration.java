//package ua.ks.hogo.fingerscanner.config;
//
//import org.apache.logging.log4j.Level;
//import org.apache.logging.log4j.core.Filter;
//import org.apache.logging.log4j.core.LoggerContext;
//import org.apache.logging.log4j.core.appender.ConsoleAppender;
//import org.apache.logging.log4j.core.config.Configuration;
//import org.apache.logging.log4j.core.config.ConfigurationFactory;
//import org.apache.logging.log4j.core.config.ConfigurationSource;
//import org.apache.logging.log4j.core.config.Order;
//import org.apache.logging.log4j.core.config.builder.api.*;
//import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
//import org.apache.logging.log4j.core.config.plugins.Plugin;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.AutoConfigureBefore;
//import org.springframework.stereotype.Component;
//
//import java.net.URI;
//
//
//@Plugin(name = "CustomConfigurationFactory", category = ConfigurationFactory.CATEGORY)
//@Order(50)
//public class Log4j2Configuration extends ConfigurationFactory {
//
//    public Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
//        builder.setConfigurationName(name);
//        builder.setStatusLevel(Level.DEBUG);
//        builder.add(builder.newFilter("ThresholdFilter", Filter.Result.ACCEPT, Filter.Result.NEUTRAL)
//                        .addAttribute("level", Level.DEBUG));
//        /* Console appender configuration */
//        AppenderComponentBuilder consoleAppender = builder.newAppender("Stdout", "Console")
//                        .addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
//        consoleAppender
//                .add(builder.newLayout("PatternLayout")
//                .addAttribute("pattern", "%d %p %c{1.} [%t] %m %ex%n"));
////        appenderBuilder.add(builder.newFilter("MarkerFilter", Filter.Result.DENY,
////                Filter.Result.NEUTRAL).addAttribute("marker", "FLOW"));
//
//        AppenderComponentBuilder fileErrorAppender = builder.newAppender("err_log", "File")
//                        .addAttribute("fileName", "error");
//
//        AppenderComponentBuilder httpErrorAppender = builder.newAppender("http_log", "Http")
//                .addAttribute("url", "http://" + "/api/scanner/log")
//                .addAttribute("method", "PUT")
//                .addAttribute("x-java-runtime", "$${java:runtime}")
//                .addAttribute("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmaW5nZXJzY2FubmVyXzRiNjgiLCJleHAiOjE2NjI0NzcyMTh9.F5STlr63Vt1R6IvYLcSpz05T8K8JCNjtx77NWh-b6z6HHw6R2tAwspYskVVUC40_OP32OpCAc5yRArulYyefLQ")
//                .addAttribute("Content-Type", "application/json");
//        LayoutComponentBuilder httpJsonLayout = builder.newLayout("HttpLayout")
//                .addAttribute("properties", true);
//        ComponentBuilder<KeyValuePairComponentBuilder> macKeyValuePair = builder.newKeyValuePair("mac", "02:42:e8:65:3b:3c");
//        httpJsonLayout.addComponent(macKeyValuePair);
//
//        builder.add(consoleAppender)
//                .add(fileErrorAppender)
//                .add(httpErrorAppender);
//
//        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.DEBUG)
//                .add(builder.newAppenderRef("Stdout").addAttribute("level", Level.DEBUG))
//                .add(builder.newAppenderRef("err_log").addAttribute("level", Level.ERROR))
//                .add(builder.newAppenderRef("http_log").addAttribute("level", Level.ERROR));
//
//
////        builder.add(builder.newLogger("org.apache.logging.log4j", Level.DEBUG).
////                add(builder.newAppenderRef("Stdout")).
////                addAttribute("additivity", false));
//        builder.add(rootLogger);
//        return builder.build();
//    }
//
//    @Override
//    protected String[] getSupportedTypes() {
//        return new String[]{"*"};
//    }
//
//    @Override
//    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
//        return getConfiguration(loggerContext, source.toString(), null);
//    }
//
//    @Override
//    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
//        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
//        return createConfiguration(name, builder);
//    }
//}
