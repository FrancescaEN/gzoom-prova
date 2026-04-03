package it.mapsgroup.gzoom;


import com.fasterxml.jackson.databind.ObjectMapper;
import it.mapsgroup.gzoom.mybatis.dao.ContactMechDao;
import it.mapsgroup.gzoom.mybatis.dao.PartyContentDao;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginDao;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginHistoryDao;
import it.mapsgroup.gzoom.mybatis.dao.UserLoginPasswordHistoryDao;
import it.mapsgroup.gzoom.mybatis.dao.UserPreferenceDaoMB;
import it.mapsgroup.gzoom.ofbiz.client.OfBizClientConfig;
import it.mapsgroup.gzoom.ofbiz.client.impl.AuthenticationOfBizClientImpl;
import it.mapsgroup.gzoom.ofbiz.client.impl.VersionOfBizClientImpl;
import it.mapsgroup.gzoom.ofbiz.service.ChangePasswordServiceOfBiz;
import it.mapsgroup.gzoom.ofbiz.service.LoginServiceOfBiz;
import it.mapsgroup.gzoom.ofbiz.service.VersionServiceOfBiz;
import it.mapsgroup.gzoom.security.*;
import it.mapsgroup.gzoom.security.service.EmailServiceCheckUser;
import it.mapsgroup.gzoom.security.service.KeycloakAdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import java.net.URL;
import java.util.List;

/**
 * @author Andrea Fossi.
 */


@EnableWebSecurity
@Configuration
public class GZoomWebConfig {

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PermitsStorage permitsStorage;

    @Autowired
    private LoginServiceOfBiz loginService;

    @Autowired
    private UserLoginDao userLoginDao;

    @Autowired
    private EmailServiceCheckUser emailService;

    @Autowired
    private  ChangePasswordServiceOfBiz changeService;

    @Autowired
    private  UserLoginHistoryDao userLoginHistoryDao;

    @Autowired
    private HashingStrategy hashingStrategy;

    @Autowired
    private  ContactMechDao contactMechDao;

    @Autowired
    private  PartyContentDao partyContentDao;

    @Autowired
    private UserPreferenceDaoMB userPreferenceDao;

    @Autowired
    private UserLoginPasswordHistoryDao userLoginPasswordHistoryDao;

    @Autowired
    private it.mapsgroup.gzoom.service.Configuration config;

    @Autowired
    private KeycloakAdminClient keycloakAdminClient;

    @Value("${keycloak.login.enable:false}")
    private String securityModeKeycloakLoginEnable;

    @Bean
    public AuthenticationEntryPoint http403ForbiddenEntryPoint() {
        return new Http403ForbiddenEntryPoint();
    }

    @Bean
    public LoginServiceOfBiz loginServiceOfBiz(OfBizClientConfig ofBizClientConfig) {
        return new LoginServiceOfBiz(new AuthenticationOfBizClientImpl(new OfBizClientConfig() {
            @Override
            public URL getServerXmlRpcUrl() {
                return ofBizClientConfig.getServerXmlRpcUrl();
            }
        }));
    }

    @Bean
    public VersionServiceOfBiz versionServiceOfBiz(OfBizClientConfig ofBizClientConfig) {
        return new VersionServiceOfBiz(new VersionOfBizClientImpl(new OfBizClientConfig() {
            @Override
            public URL getServerXmlRpcUrl() {
                return ofBizClientConfig.getServerXmlRpcUrl();
            }
        }));
    }


    @Bean
    public HttpFirewall allowUrlEncodePercentHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedPercent(true);

        return firewall;
    }

    @Bean
    public ChangePasswordServiceOfBiz changePasswordServiceOfBiz(OfBizClientConfig ofBizClientConfig) {
        return new ChangePasswordServiceOfBiz(new AuthenticationOfBizClientImpl(new OfBizClientConfig() {
            @Override
            public URL getServerXmlRpcUrl() {
                return ofBizClientConfig.getServerXmlRpcUrl();
            }
        }));
    }

    @Autowired
    @Qualifier("jwtTokenAuthenticationProvider")
    JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;

    @Autowired
    @Qualifier("jwtOfBizLoginAuthenticationProvider")
    JwtOfBizLoginAuthenticationProvider jwtOfBizLoginAuthenticationProvider;

    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> authenticationProviders) {
        return new ProviderManager(authenticationProviders);
    }


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        // auth.authenticationProvider(jwtDbLoginAuthenticationProvider);
//        //  auth.authenticationProvider(jwtADLoginAuthenticationProvider);
//        auth.authenticationProvider(jwtOfBizLoginAuthenticationProvider);
//        auth.authenticationProvider(jwtTokenAuthenticationProvider);
//    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        //@formatter:off
//        super.configure(web);
//        web.ignoring().antMatchers("/profile/i18n");
//        web.httpFirewall(allowUrlEncodePercentHttpFirewall());
//    }

    @Bean
    WebSecurityCustomizer ignoringCustomizer() {
    // not needed anymore: https://github.com/spring-projects/spring-security/issues/10938
        return (web) -> web.ignoring().requestMatchers("/profile/i18n");
    }
    
    @Bean
    WebSecurityCustomizer fireWall() {
        return (web) -> web.httpFirewall(allowUrlEncodePercentHttpFirewall());
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());
        http.httpBasic((httpBasic) -> httpBasic.disable());
        http.logout((logout) -> logout.disable());
        http.exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(restAuthenticationEntryPoint));
        http.sessionManagement((sessionManagement)-> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(HttpMethod.POST, "/logout", "/login", "/check-user", "/reset-password", "/api/getToken", "/api/getExternalKey","/api/doLogout", "/twoFAuthEnable", "/verifyOtp","verify-user").permitAll()
        );
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET,"/profile/mail-host", "/profile/isLdap","/profile/enableChangePassword", "/profile/i18n/*", "/reminder-period", "/reminder-expiry", "/user-preference-na/VISUAL_THEME", "/user-preference-na/*", "/node/configuration/*", "/node/version/*", "/node/logo/*/*", "/party/partiesExposed","/api/getLoginMethod", "/api/keycloak-config","/api/getOneLogin-LoginUrl","/api/getOneLogin-LogoutUrl", "/note-data/pref-value/*").permitAll());
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(HttpMethod.OPTIONS, "/api/getToken").permitAll());
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers("/**").authenticated());

        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(authenticationManager, restAuthenticationEntryPoint, objectMapper);
        JwtLogoutFilter jwtLogoutFilter = new JwtLogoutFilter(restAuthenticationEntryPoint, permitsStorage, objectMapper, loginService, userLoginDao, emailService, changeService, hashingStrategy, userLoginHistoryDao, contactMechDao, securityModeKeycloakLoginEnable, keycloakAdminClient);

        if (securityModeKeycloakLoginEnable.equalsIgnoreCase("true")){
            // Flusso Keycloak -> passa dal filtro legacy
            // http.authenticationProvider(jwtTokenAuthenticationProvider); // il provider legacy
            //http.addFilterBefore(jwtTokenFilter, BasicAuthenticationFilter.class); // usa lo stesso filtro
            http.addFilterBefore(jwtLogoutFilter, LogoutFilter.class);

            http.addFilterBefore(new QueryParamBearerTokenFilter(), BearerTokenAuthenticationFilter.class);

            http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
               .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        }
        else{
            JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(authenticationManager, objectMapper, config, userPreferenceDao);
            http.addFilterBefore(jwtLoginFilter, BasicAuthenticationFilter.class);

            //JwtLogoutFilter jwtLogoutFilter = new JwtLogoutFilter(restAuthenticationEntryPoint, permitsStorage, objectMapper, loginService, userLoginDao, emailService, changeService, hashingStrategy, userLoginHistoryDao, contactMechDao );
            http.addFilterBefore(jwtLogoutFilter, LogoutFilter.class);

            JwtChangePasswordFilter jwtChangePasswordFilter = new JwtChangePasswordFilter(restAuthenticationEntryPoint, objectMapper, userLoginDao, emailService, userLoginHistoryDao, contactMechDao, partyContentDao, userLoginPasswordHistoryDao );
            http.addFilterBefore(jwtChangePasswordFilter, LogoutFilter.class);

            JwtTwoFAuthFilter jwtTwoFAuthFilter = new JwtTwoFAuthFilter(restAuthenticationEntryPoint, objectMapper, userLoginDao, emailService, userLoginHistoryDao, contactMechDao, partyContentDao , userPreferenceDao);
            http.addFilterBefore(jwtTwoFAuthFilter, BasicAuthenticationFilter.class);

            RequestMatcher profile = PathPatternRequestMatcher.withDefaults().matcher("/profile/i18n/*");
            RequestMatcher mailHost = PathPatternRequestMatcher.withDefaults().matcher("/profile/mail-host");
            RequestMatcher ldap = PathPatternRequestMatcher.withDefaults().matcher("/profile/isLdap");
            RequestMatcher enableChangePassword = PathPatternRequestMatcher.withDefaults().matcher("/profile/enableChangePassword");
            RequestMatcher logout = PathPatternRequestMatcher.withDefaults().matcher("/logout");
            RequestMatcher login = PathPatternRequestMatcher.withDefaults().matcher("/login");
            RequestMatcher checkUser = PathPatternRequestMatcher.withDefaults().matcher("/check-user");
            RequestMatcher verifyOtp = PathPatternRequestMatcher.withDefaults().matcher("/verifyOtp");
            RequestMatcher verifyuser = PathPatternRequestMatcher.withDefaults().matcher("/verify-user");
            RequestMatcher twoFAuthEnable = PathPatternRequestMatcher.withDefaults().matcher("/twoFAuthEnable");
            RequestMatcher resetPassword = PathPatternRequestMatcher.withDefaults().matcher("/reset-password");
            RequestMatcher partiesExposed = PathPatternRequestMatcher.withDefaults().matcher("/party/partiesExposed");
            RequestMatcher reminderPeriod = PathPatternRequestMatcher.withDefaults().matcher("/reminder-period"); //TODO
            RequestMatcher reminderExipry = PathPatternRequestMatcher.withDefaults().matcher("/reminder-expiry"); //TODO
            RequestMatcher userPreferenceNA = PathPatternRequestMatcher.withDefaults().matcher("/user-preference-na/VISUAL_THEME");
            RequestMatcher userPreference = PathPatternRequestMatcher.withDefaults().matcher("/user-preference-na/*");
            RequestMatcher configuration = PathPatternRequestMatcher.withDefaults().matcher("/node/configuration/*");
            RequestMatcher version = PathPatternRequestMatcher.withDefaults().matcher("/node/version/*");
            RequestMatcher logo = PathPatternRequestMatcher.withDefaults().matcher("/node/logo/*/*");
            RequestMatcher noteInfo = PathPatternRequestMatcher.withDefaults().matcher("/note-data/pref-value/*");
            RequestMatcher ignoredRequests = new OrRequestMatcher(profile,enableChangePassword, noteInfo, mailHost,ldap, logout, login, checkUser, verifyuser, resetPassword, reminderPeriod, reminderExipry, userPreferenceNA, userPreference, configuration, version, logo, partiesExposed, verifyOtp, twoFAuthEnable);

            http.securityMatcher("/**").addFilterAfter(new DelegateRequestMatchingFilter(ignoredRequests, jwtTokenFilter), JwtLogoutFilter.class);
            http.authenticationProvider(jwtOfBizLoginAuthenticationProvider);
        }
        http.authenticationProvider(jwtTokenAuthenticationProvider);

        return http.build();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.httpBasic().disable();
//        http.logout().disable();
//        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.authorizeRequests().antMatchers(HttpMethod.POST, "/logout", "/login", "/check-user", "/reset-password", "/api/getToken","/api/doLogout", "/twoFAuthEnable", "/verifyOtp","verify-user").permitAll();
//        http.authorizeRequests().antMatchers(HttpMethod.GET,"/profile/mail-host", "/profile/isLdap","/profile/enableChangePassword", "/profile/i18n/*", "/reminder-period", "/reminder-expiry", "/user-preference-na/VISUAL_THEME", "/user-preference-na/*", "/node/configuration/*", "/node/version/*", "/node/logo/*/*", "/party/partiesExposed","/api/getLoginMethod","/api/getOneLogin-LoginUrl","/api/getOneLogin-LogoutUrl", "/note-data/pref-value/*").permitAll();
//        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/api/getToken").permitAll();
//        http.authorizeRequests().antMatchers("/**").authenticated();
//
//        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(authenticationManager, objectMapper, config, userPreferenceDao);
//        http.addFilterBefore(jwtLoginFilter, BasicAuthenticationFilter.class);
//
//        JwtLogoutFilter jwtLogoutFilter = new JwtLogoutFilter(restAuthenticationEntryPoint, permitsStorage, objectMapper, loginService, userLoginDao, emailService, changeService, hashingStrategy, userLoginHistoryDao, contactMechDao );
//        http.addFilterBefore(jwtLogoutFilter, LogoutFilter.class);
//
//        JwtChangePasswordFilter jwtChangePasswordFilter = new JwtChangePasswordFilter(restAuthenticationEntryPoint, objectMapper, userLoginDao, emailService, userLoginHistoryDao, contactMechDao, partyContentDao, userLoginPasswordHistoryDao );
//        http.addFilterBefore(jwtChangePasswordFilter, LogoutFilter.class);
//
//        JwtTwoFAuthFilter jwtTwoFAuthFilter = new JwtTwoFAuthFilter(restAuthenticationEntryPoint, objectMapper, userLoginDao, emailService, userLoginHistoryDao, contactMechDao, partyContentDao , userPreferenceDao);
//        http.addFilterBefore(jwtTwoFAuthFilter, BasicAuthenticationFilter.class);
//
//        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(authenticationManager, restAuthenticationEntryPoint, objectMapper);
//        RequestMatcher profile = new AntPathRequestMatcher("/profile/i18n/*");
//        RequestMatcher mailHost = new AntPathRequestMatcher("/profile/mail-host");
//        RequestMatcher ldap = new AntPathRequestMatcher("/profile/isLdap");
//        RequestMatcher enableChangePassword = new AntPathRequestMatcher("/profile/enableChangePassword");
//        RequestMatcher logout = new AntPathRequestMatcher("/logout");
//        RequestMatcher login = new AntPathRequestMatcher("/login");
//        RequestMatcher checkUser = new AntPathRequestMatcher("/check-user");
//        RequestMatcher verifyOtp = new AntPathRequestMatcher("/verifyOtp");
//        RequestMatcher verifyuser = new AntPathRequestMatcher("/verify-user");
//        RequestMatcher twoFAuthEnable = new AntPathRequestMatcher("/twoFAuthEnable");
//        RequestMatcher resetPassword = new AntPathRequestMatcher("/reset-password");
//        RequestMatcher partiesExposed = new AntPathRequestMatcher("/party/partiesExposed");
//        RequestMatcher reminderPeriod = new AntPathRequestMatcher("/reminder-period"); //TODO
//        RequestMatcher reminderExipry = new AntPathRequestMatcher("/reminder-expiry"); //TODO
//        RequestMatcher userPreferenceNA = new AntPathRequestMatcher("/user-preference-na/VISUAL_THEME");
//        RequestMatcher userPreference = new AntPathRequestMatcher("/user-preference-na/*");
//        RequestMatcher configuration = new AntPathRequestMatcher("/node/configuration/*");
//        RequestMatcher version = new AntPathRequestMatcher("/node/version/*");
//        RequestMatcher logo = new AntPathRequestMatcher("/node/logo/*/*");
//        RequestMatcher noteInfo = new AntPathRequestMatcher("/note-data/pref-value/*");
//        RequestMatcher ignoredRequests = new OrRequestMatcher(profile,enableChangePassword, noteInfo, mailHost,ldap, logout, login, checkUser, verifyuser, resetPassword, reminderPeriod, reminderExipry, userPreferenceNA, userPreference, configuration, version, logo, partiesExposed, verifyOtp, twoFAuthEnable);
//        http.antMatcher("/**").addFilterAfter(new DelegateRequestMatchingFilter(ignoredRequests, jwtTokenFilter), JwtLogoutFilter.class);
//    }
}
