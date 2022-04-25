package com.stockService.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stockService.command.api.events.StockEventsHandler;
import com.stockService.model.UserDto;


@Component
public class ReqFilter extends OncePerRequestFilter {
	private static RestTemplate restTemplate = new RestTemplate();
	
	private static final Logger logger = LoggerFactory.getLogger(ReqFilter.class);
	
	@Value("${googleUserInfoUrl}")
	private String googleUserInfoUrl;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String authorization = request.getHeader("Authorization");
		
		String token = null;
		
		
		if(null != authorization && authorization.startsWith("Bearer")) {
			
			token = authorization.substring(7);
		}
		
		if (null != token && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			String url = googleUserInfoUrl+"?access_token="+token;

			HttpEntity<Void> entity = new HttpEntity<>(null);
			
			

			UserDto userDto = restTemplate
					.exchange(url, HttpMethod.GET, entity,UserDto.class).getBody();
			
			logger.info("Fetched user details from Google");
			
			UserDetails userDetails = new User(userDto.getEmail(),userDto.getSub(),new ArrayList<>());
				UsernamePasswordAuthenticationToken credAuthToken 
									= new UsernamePasswordAuthenticationToken (userDetails,
											null,userDetails.getAuthorities());
				credAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(credAuthToken);
		}				
			
		filterChain.doFilter(request, response);
	}

}
