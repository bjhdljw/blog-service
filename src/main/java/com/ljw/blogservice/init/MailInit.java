package com.ljw.blogservice.init;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class MailInit {

    private static Template template;

    static {
        try {
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
            String templatePath = MailInit.class.getResource("/").getPath() + "templates/";
            configuration.setDirectoryForTemplateLoading(new File(templatePath));
            configuration.setDefaultEncoding("UTF-8");
            configuration.setLogTemplateExceptions(false);
            configuration.setWrapUncheckedExceptions(true);
            template = configuration.getTemplate("mail.ftl");
        } catch (Exception e) {
            log.info("解析ftl模板失败：{}", e.getMessage());
        }
    }

    public static Template getTemplate() {
        return template;
    }

}
