package it.mapsgroup.gzoom.email.configuration;


//import freemarker.cache.ClassTemplateLoader;
//import freemarker.cache.TemplateLoader;
//import freemarker.template.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
//import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
//import org.thymeleaf.spring5.SpringTemplateEngine;
//import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
//import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@ComponentScan(basePackages = { "it.mapsgroup.gzoom.email" })
//@PropertySource(value={"classpath:config/gzoom.properties"})
public class EmailConfiguration {

    private final String mailDebug;
    private final String mailServerHost;
    private final Integer mailServerPort;
    private final String mailServerUsername;
    private final String mailServerPassword;
    private final String mailFrom;
    private final String mailRedirectTo;
    private final String mailServerAuth;
    private final String mailServerStartTls;

    //@Value("${spring.mail.templates.path}")
    private String mailTemplatesPath;

    EmailConfiguration(Environment env) {
        this.mailDebug = env.getProperty("gzoom.mail.debug", String.class, "true");
        this.mailServerHost = env.getProperty("gzoom.mail.host", String.class);
        this.mailServerPort = env.getProperty("gzoom.mail.port", Integer.class, 25);
        this.mailServerUsername = env.getProperty("gzoom.mail.username", String.class);
        this.mailServerPassword = env.getProperty("gzoom.mail.password", String.class);
        this.mailFrom = env.getProperty("gzoom.mail.from", String.class);
        this.mailServerAuth = env.getProperty("gzoom.mail.properties.mail.smtp.auth", String.class, "false");
        this.mailServerStartTls = env.getProperty("gzoom.mail.properties.mail.smtp.starttls.enable", String.class, "false");
        this.mailRedirectTo = env.getProperty("gzoom.mail.redirectTo", String.class, null);
    }

    public String getMailFrom() { return this.mailFrom; }

    public String getMailRedirectTo() { return this.mailRedirectTo; }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailServerHost);
        mailSender.setPort(mailServerPort);
        mailSender.setUsername(mailServerUsername);
        mailSender.setPassword(mailServerPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", mailServerAuth);
        props.put("mail.smtp.starttls.enable", mailServerStartTls);
        props.put("mail.debug", mailDebug);

        return mailSender;
    }

    @Bean
    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("This is the test email template for your email:\n%s\n");
        return message;
    }

//    @Bean
//    public SpringTemplateEngine thymeleafTemplateEngine(ITemplateResolver templateResolver) {
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.setTemplateResolver(templateResolver);
//        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
//        return templateEngine;
//    }
//
//    @Bean
//    public ITemplateResolver thymeleafClassLoaderTemplateResolver() {
//        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//        templateResolver.setPrefix(mailTemplatesPath + "/");
//        templateResolver.setSuffix(".html");
//        templateResolver.setTemplateMode("HTML");
//        templateResolver.setCharacterEncoding("UTF-8");
//        return templateResolver;
//    }

//    @Bean
//    public ITemplateResolver thymeleafFilesystemTemplateResolver() {
//        FileTemplateResolver templateResolver = new FileTemplateResolver();
//        templateResolver.setPrefix(mailTemplatesPath + "/");
//        templateResolver.setSuffix(".html");
//        templateResolver.setTemplateMode("HTML");
//        templateResolver.setCharacterEncoding("UTF-8");
//        return templateResolver;
//    }

//    @Bean
//    public FreeMarkerConfigurer freemarkerClassLoaderConfig() {
//        Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
//        TemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), "/" + mailTemplatesPath);
//        configuration.setTemplateLoader(templateLoader);
//        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
//        freeMarkerConfigurer.setConfiguration(configuration);
//        return freeMarkerConfigurer;
//    }

//    @Bean
//    public FreeMarkerConfigurer freemarkerFilesystemConfig() throws IOException {
//        Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
//        TemplateLoader templateLoader = new FileTemplateLoader(new File(mailTemplatesPath));
//        configuration.setTemplateLoader(templateLoader);
//        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
//        freeMarkerConfigurer.setConfiguration(configuration);
//        return freeMarkerConfigurer;
//    }

//    @Bean
//    public ResourceBundleMessageSource emailMessageSource() {
//        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//        messageSource.setBasename("mailMessages");
//        return messageSource;
//    }

}