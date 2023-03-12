package br.eti.freitas.startproject.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.eti.freitas.startproject.security.constant.AuthorizationConstant;
import br.eti.freitas.startproject.security.service.impl.SecurityUserDetailsServiceImpl;
import br.eti.freitas.startproject.security.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class SecurityRequestFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(SecurityRequestFilter.class);

	@Autowired
	private SecurityUserDetailsServiceImpl securityUserDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		try {

			String jwt = request.getHeader(AuthorizationConstant.AUTHENTICATION_HEADER);

			if (jwt != null && jwt.startsWith(AuthorizationConstant.TOKEN_PREFIX)) {

				jwt = jwt.replace(AuthorizationConstant.TOKEN_PREFIX, "");
				String username = jwtUtil.extractUsername(jwt);

				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

					UserDetails userDetails = securityUserDetailsService.loadUserByUsername(username);

					if (jwtUtil.validateToken(jwt, userDetails)) {

						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtUtil
								.getAuthenticationToken(jwt, SecurityContextHolder.getContext().getAuthentication(),
										userDetails);

						usernamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
						request.setAttribute(AuthorizationConstant.USER, username);
					}
				}
			}
		} catch (SignatureException e) {
			LOG.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			LOG.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			LOG.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			LOG.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			LOG.error("JWT claims string is empty: {}", e.getMessage());
		} finally {
			chain.doFilter(request, response);
		}
	}

}
