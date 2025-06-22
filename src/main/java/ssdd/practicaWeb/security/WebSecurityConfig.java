package ssdd.practicaWeb.security;

import jakarta.servlet.http.HttpServletRequest;
import ssdd.practicaWeb.security.jwt.JwtRequestFilter;
import ssdd.practicaWeb.security.jwt.UnauthorizedHandlerJwt;
import ssdd.practicaWeb.service.NutritionService;
import ssdd.practicaWeb.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAuthority;
import static org.springframework.security.authorization.AuthorizationManagers.allOf;
import static org.springframework.security.authorization.AuthorizationManagers.not;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	RepositoryUserDetailsService userDetailsService;

	@Autowired
	TrainingService trainingService;

	@Autowired
	NutritionService nutritionService;


	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Expose AuthenticationManager as a Bean to be used in other services
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}



	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http
				.securityMatcher("/api/**")
				.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

		http
				.authorizeHttpRequests(authorize -> authorize

						// AUTH ENDPOINTS
						.requestMatchers(HttpMethod.POST, "/api/v1/auth/login", "/api/v1/auth/refresh",
								"/api/v1/auth/logout")
						.permitAll()

						// PRIVATE ENDPOINTS

						// For User
						.requestMatchers(HttpMethod.GET, "/api/users/all").hasAnyRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
						.requestMatchers(HttpMethod.GET, "/api/users/*/image").hasRole("USER")
						.requestMatchers(HttpMethod.PUT, "/api/users/*").hasAnyRole("USER", "ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/users/*/image").hasAnyRole("USER", "ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/users/*").hasAnyRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/users/*/image").hasRole("ADMIN")

						// PUBLIC ENDPOINTS
						.requestMatchers(HttpMethod.POST, "/api/users/new").permitAll()

						// For Training
						.requestMatchers(HttpMethod.POST, "/api/trainings/").hasAnyRole("USER")
						.requestMatchers(HttpMethod.PUT, "/api/trainings/*").hasAnyRole("USER")
						.requestMatchers(HttpMethod.DELETE, "/api/trainings/**").hasRole("ADMIN")

						.requestMatchers(HttpMethod.POST, "/api/trainings/subscribed/*")
						.access((authentication, context) ->
								authentication.get().getAuthorities().stream()
										.anyMatch(a -> a.getAuthority().equals("ROLE_USER")) &&
										authentication.get().getAuthorities().stream()
												.noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
										? new AuthorizationDecision(true)
										: new AuthorizationDecision(false)
						)
						.requestMatchers(HttpMethod.POST, "/api/trainings/unsubscribed/*")
						.access((authentication, context) ->
								authentication.get().getAuthorities().stream()
										.anyMatch(a -> a.getAuthority().equals("ROLE_USER")) &&
										authentication.get().getAuthorities().stream()
												.noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
										? new AuthorizationDecision(true)
										: new AuthorizationDecision(false)
						)

						// For Nutrition
						.requestMatchers(HttpMethod.POST, "/api/nutritions/").hasAnyRole("USER")
						.requestMatchers(HttpMethod.PUT, "/api/nutritions/*").hasAnyRole("USER")
						.requestMatchers(HttpMethod.DELETE, "/api/nutritions/**").hasRole("ADMIN")

						.requestMatchers(HttpMethod.POST, "/api/nutritions/subscribed/*")
						.access((authentication, context) ->
								authentication.get().getAuthorities().stream()
										.anyMatch(a -> a.getAuthority().equals("ROLE_USER")) &&
										authentication.get().getAuthorities().stream()
												.noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
										? new AuthorizationDecision(true)
										: new AuthorizationDecision(false)
						)
						.requestMatchers(HttpMethod.POST, "/api/nutritions/unsubscribed/*")
						.access((authentication, context) ->
								authentication.get().getAuthorities().stream()
										.anyMatch(a -> a.getAuthority().equals("ROLE_USER")) &&
										authentication.get().getAuthorities().stream()
												.noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
										? new AuthorizationDecision(true)
										: new AuthorizationDecision(false)
						)

						// PUBLIC ENDPOINTS
						.anyRequest().permitAll());

		// Disable Form login Authentication
		http.formLogin(formLogin -> formLogin.disable());

		// Disable CSRF protection (it is difficult to implement in REST APIs)
		http.csrf(csrf -> csrf.disable());

		// Disable Basic Authentication
		http.httpBasic(httpBasic -> httpBasic.disable());

		// Stateless session
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http
				.authorizeHttpRequests(authorize -> authorize
						// PUBLIC PAGES
						.requestMatchers("/").permitAll()
						.requestMatchers("/images/**", "/css/**", "/js/**").permitAll() // Acceso a los recursos
																						// estáticos
						.requestMatchers("/index").permitAll()
						.requestMatchers("/login").permitAll()
						.requestMatchers("/register").permitAll()
						.requestMatchers("/error").permitAll()
						.requestMatchers("/trainings").permitAll()
						.requestMatchers("/trainings/*").permitAll()
						.requestMatchers("/nutritions").permitAll()
						.requestMatchers("/nutritions/*").permitAll()
						.requestMatchers("/v3/api-docs*/**").permitAll()
						.requestMatchers("/swagger-ui.html").permitAll()
						.requestMatchers("/swagger-ui/**").permitAll()

						// PRIVATE PAGES
						.requestMatchers("/account/**").access(allOf(
								hasAuthority("ROLE_USER"),
								not(hasAuthority("ROLE_ADMIN"))
						))
						.requestMatchers("/nutritions/createNutrition").hasAnyRole("ADMIN", "USER")
						.requestMatchers("/trainings/createTraining").hasAnyRole("ADMIN", "USER")
						.requestMatchers("/admin/**").hasAnyRole("ADMIN")

						.requestMatchers("listFoods/editFood/*").hasAnyRole("ADMIN")
						.requestMatchers("listFoods/deleteFood/*").hasAnyRole("ADMIN")
						.requestMatchers("listFoods/createFood/*").hasAnyRole("ADMIN")
						// SECURITY FOR EDIT PAGES
						// EDIT TRAINING
						.requestMatchers(HttpMethod.GET, "/trainings/editTraining/{trainingId}")
						.access((authentication, context) -> {
							Long trainingId = Long.valueOf(context.getVariables().get("trainingId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									trainingService.isOwner(trainingId, authentication.get())
											? new AuthorizationDecision(true)
											: new AuthorizationDecision(false);
						})
						.requestMatchers(HttpMethod.POST, "/trainings/editTraining/{trainingId}")
						.access((authentication, context) -> {
							Long trainingId = Long.valueOf(context.getVariables().get("trainingId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									trainingService.isOwner(trainingId, authentication.get())
											? new AuthorizationDecision(true)
											: new AuthorizationDecision(false);
						})
						// EDIT NUTRITION
						.requestMatchers(HttpMethod.GET, "/nutritions/editNutrition/{nutritionId}")
						.access((authentication, context) -> {
							Long nutritionId = Long.valueOf(context.getVariables().get("nutritionId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									nutritionService.isOwner(nutritionId, authentication.get())
											? new AuthorizationDecision(true)
											: new AuthorizationDecision(false);
						})
						.requestMatchers(HttpMethod.POST, "/nutritions/editNutrition/{nutritionId}")
						.access((authentication, context) -> {
							Long nutritionId = Long.valueOf(context.getVariables().get("nutritionId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									nutritionService.isOwner(nutritionId, authentication.get())
											? new AuthorizationDecision(true)
											: new AuthorizationDecision(false);
						})

						//EDIT NUTRITION ABOUT FOOD
						.requestMatchers(HttpMethod.GET, "/listFoods/{nutritionId}")
						.access((authentication, context) -> {
							Long nutritionId = Long.valueOf(context.getVariables().get("nutritionId"));

							boolean isAdmin = authentication.get().getAuthorities().stream()
									.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

							boolean isOwner = nutritionService.isOwner(nutritionId, authentication.get());

							return new AuthorizationDecision(isAdmin || isOwner);
						})

						.requestMatchers(HttpMethod.GET, "/listFoods/detailsFood/{foodId}")
						.access((authentication, context) -> {
							HttpServletRequest request = context.getRequest();
							String param = request.getParameter("nutritionId");

							if (param == null) return new AuthorizationDecision(false);

							Long nutritionId = Long.parseLong(param);

							boolean isAdmin = authentication.get().getAuthorities().stream()
									.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

							boolean isOwner = nutritionService.isOwner(nutritionId, authentication.get());

							return new AuthorizationDecision(isAdmin || isOwner);
						})

						.requestMatchers(HttpMethod.GET, "/listFoods/deleteFoodList/{nutritionId}/{foodId}")
						.access((authentication, context) -> {
							Long nutritionId = Long.valueOf(context.getVariables().get("nutritionId"));

							boolean isAdmin = authentication.get().getAuthorities().stream()
									.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

							boolean isOwner = nutritionService.isOwner(nutritionId, authentication.get());

							return new AuthorizationDecision(isAdmin || isOwner);
						})

						.requestMatchers(HttpMethod.GET, "/listFoods/addFood/{nutritionId}/{foodId}")
						.access((authentication, context) -> {
							Long nutritionId = Long.valueOf(context.getVariables().get("nutritionId"));

							boolean isAdmin = authentication.get().getAuthorities().stream()
									.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

							boolean isOwner = nutritionService.isOwner(nutritionId, authentication.get());

							return new AuthorizationDecision(isAdmin || isOwner);
						})

						//SECURITY FOR DELETE PAGES
						//DELETE TRAINING
						.requestMatchers(HttpMethod.GET, "/trainings/delete/{trainingId}")
						.access((authentication, context) -> {
							Long trainingId = Long.valueOf(context.getVariables().get("trainingId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									trainingService.isOwner(trainingId, authentication.get())
									? new AuthorizationDecision(true)
									: new AuthorizationDecision(false);
						})
						.requestMatchers(HttpMethod.POST, "/trainings/delete/{trainingId}")
						.access((authentication, context) -> {
							Long trainingId = Long.valueOf(context.getVariables().get("trainingId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									trainingService.isOwner(trainingId, authentication.get())
									? new AuthorizationDecision(true)
									: new AuthorizationDecision(false);
						})
						// DELETE NUTRITION
						.requestMatchers(HttpMethod.GET, "/nutritions/delete/{nutritionId}")
						.access((authentication, context) -> {
							Long nutritionId = Long.valueOf(context.getVariables().get("nutritionId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									nutritionService.isOwner(nutritionId, authentication.get())
									? new AuthorizationDecision(true)
									: new AuthorizationDecision(false);
						})
						.requestMatchers(HttpMethod.POST, "/nutritions/delete/{nutritionId}")
						.access((authentication, context) -> {
							Long nutritionId = Long.valueOf(context.getVariables().get("nutritionId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									nutritionService.isOwner(nutritionId, authentication.get())
									? new AuthorizationDecision(true)
									: new AuthorizationDecision(false);
						})

						//PRIVATE PAGES FOR ONLY USER (NOT ADMIN)
						//TRAINING
						.requestMatchers("/trainings/subscribe/*").access(allOf(
								hasAuthority("ROLE_USER"),
								not(hasAuthority("ROLE_ADMIN"))
						))
						.requestMatchers("/trainings/unsubscribe/*").access(allOf(
								hasAuthority("ROLE_USER"),
								not(hasAuthority("ROLE_ADMIN"))
						))
						.requestMatchers("/myTrainings").access(allOf(
								hasAuthority("ROLE_USER"),
								not(hasAuthority("ROLE_ADMIN"))
						))

						//NUTRITION
						.requestMatchers("/nutritions/subscribe/*").access(allOf(
								hasAuthority("ROLE_USER"),
								not(hasAuthority("ROLE_ADMIN"))
						))
						.requestMatchers("/nutritions/unsubscribe/*").access(allOf(
								hasAuthority("ROLE_USER"),
								not(hasAuthority("ROLE_ADMIN"))
						))
						.requestMatchers("/myNutritions").access(allOf(
								hasAuthority("ROLE_USER"),
								not(hasAuthority("ROLE_ADMIN"))
						))


						//FOR FOOD MENU
						.requestMatchers(HttpMethod.POST, "/ListFoods/**")
						.access((authentication, context) -> {
							HttpServletRequest request = context.getRequest();
							String param = request.getParameter("nutritionId");

							if (param == null) return new AuthorizationDecision(false);

							Long nutritionId = Long.parseLong(param);

							boolean isAdmin = authentication.get().getAuthorities().stream()
									.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

							boolean isOwner = nutritionService.isOwner(nutritionId, authentication.get());

							return new AuthorizationDecision(isAdmin || isOwner);
						})
				);

		http.formLogin(formLogin -> formLogin
				.loginPage("/login")
				.failureUrl("/login?error=true")
				.defaultSuccessUrl("/index", true)
				.permitAll())

				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.permitAll());

		return http.build();
	}
}
