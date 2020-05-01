package com.black.config;

import com.google.common.base.Charsets;
import org.springframework.http.codec.json.Jackson2CodecSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;

@Component
public class CharsetConfig {
    // 添加到 SpringBootApplication 启动类中
    static {
        // utf8的兜底方案，webfilter 在 resultHandler 之前，出现错误时会删掉 content-type，所以异常链路需要这边兜底
        addCharsetToJsonContentType();
    }
    public static void addCharsetToJsonContentType() {
        Field defaultMimeTypeField = null;
        try {
            defaultMimeTypeField = Jackson2CodecSupport.class.getDeclaredField("DEFAULT_MIME_TYPES");
            defaultMimeTypeField.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(defaultMimeTypeField, defaultMimeTypeField.getModifiers() &~ Modifier.FINAL);
            defaultMimeTypeField.set(null,
                    Collections.unmodifiableList(
                            Arrays.asList(
                                    new MimeType("application", "json", Charsets.UTF_8),
                                    new MimeType("application", "*+json", Charsets.UTF_8))));
            defaultMimeTypeField.setAccessible(false);
            modifiersField.setInt(defaultMimeTypeField, defaultMimeTypeField.getModifiers() | Modifier.FINAL);
            System.out.println("Add Charset To Jackson");
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
