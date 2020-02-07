package br.com.jdt.jdtspringboot.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.jdt.jdtspringboot.service.UsuarioService;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UsuarioService usuarioService;
	
	/**
	 * Configura as solicitações de acesso por HTTP
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() // Desativa as configurações padão de memória
			.authorizeRequests() // Restringir acessos
				.antMatchers(HttpMethod.GET, "/").permitAll() // qualquer usuario acessa a pagina inicial
				.anyRequest().authenticated()
			.and().formLogin().permitAll() // permite qualquer usuário
			.loginPage("/login")
				.defaultSuccessUrl("/pessoas/home")
				.failureUrl("/login?error=true")
			.and().logout()
				.logoutSuccessUrl("/")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));// mapeia url de logout
	}
	

	/**
	 * Cria autenticação do usuário com banco de dados ou em memória
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Autenticacao com banco de dados
		auth.userDetailsService(this.usuarioService).passwordEncoder(new BCryptPasswordEncoder());
		
		// Autenticacao em memória
		/*
		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
			.withUser("admin")
			.password("$2a$10$53ES7T8EIW2jt4SRri4byelEjT8U.w5BqANRKqaCxcupkatD0Z6Fa")
			.roles("ADMIN");
		*/
	}
	
	/**
	 * Ignora URL específicas
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/materialize/**");
	}

}
