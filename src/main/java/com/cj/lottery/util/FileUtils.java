package com.cj.lottery.util;

import cn.felord.payment.PayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Slf4j
public class FileUtils {


    public static String getFileAbsolutePath(String classPath) {
        try {
            return (new ClassPathResource(classPath)).getFile().getAbsolutePath();
        } catch (IOException var3) {
            log.error("ali pay cert path is not exist ,{}", var3.getMessage());
            throw new PayException("ali pay cert path is not exist");
        }
    }

}
