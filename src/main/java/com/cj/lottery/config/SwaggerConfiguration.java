package com.cj.lottery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;

@Configuration
@EnableSwagger2 //开启Swagger2
public class SwaggerConfiguration {

    //配置 Swagger的Docket的Bean实例
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.ant("/api/cj/simple/activity/**"))// 设置过滤的 mapperHandler 路径，满足条件则扫描api；/hello/** 包含/hello
                .build()
                .globalOperationParameters(Collections.singletonList(
                        new ParameterBuilder()
                                .name("token")
                                .description("token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")

                                .build()
                ));
    }

    //配置Swagger 信息apiInfo
    private ApiInfo apiInfo() {
        //作者信息
        Contact DEFAULT_CONTACT = new springfox.documentation.service.Contact("刘艳群", "http://www.baidu.com/", "1163866926@qq.com");
        return new ApiInfo("抽奖接口API",
                "抽奖API！",
                "1.0",
                "http://www.baidu.com/",
                DEFAULT_CONTACT,
                "license@liuyanqun",
                null,
                new ArrayList<>()
        );
    }
}
