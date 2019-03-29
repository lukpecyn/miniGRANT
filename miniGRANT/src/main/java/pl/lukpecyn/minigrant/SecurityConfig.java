 package pl.lukpecyn.minigrant;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public AuthenticationSuccessHandler successHandler() {
	    SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
	    handler.setUseReferer(false);
	    return handler;
	}	

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.info(">>> configure(HttpSecurity http) <<<");
/*
		http
			.authorizeRequests()
				.antMatchers("/health", "/info", "/metrics", "/metrics/**")
					.permitAll();
*/
		http
		.authorizeRequests()
			.antMatchers("/register","/activation/**","/confirmation/**")
				//.authenticated();
				.permitAll();

		http
		.authorizeRequests()
			.antMatchers("/beneficiary/**","/account","/logout")
				.authenticated();

		http
		.authorizeRequests()
			.antMatchers("/admin/**")
				.hasRole("ADMIN");

		http
		.authorizeRequests()
			.antMatchers("/*")
				//.authenticated();
				.permitAll();

		http
			.formLogin()
				.loginPage("/login")

				.usernameParameter("username")
				.passwordParameter("password")
				.successHandler(successHandler());
		http
			.formLogin()
				.permitAll();
		/*
		http
			.logout()
				.permitAll();
		*/
		/*
		http
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout");
		*/
		// add this line to use H2 web console
		http
			.headers()
				.frameOptions()
					.disable();
	}
  
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
		   //.usersByUsernameQuery("SELECT username,password,enabled FROM users WHERE username=?");
		   .authoritiesByUsernameQuery("select username,authority from authorities where username=?");
		auth.jdbcAuthentication().dataSource(dataSource)
			.passwordEncoder(passwordEncoder());
		
		logger.info(">>> configureGlobal(AuthenticationManagerBuilder auth) <<<");
	}
	
}