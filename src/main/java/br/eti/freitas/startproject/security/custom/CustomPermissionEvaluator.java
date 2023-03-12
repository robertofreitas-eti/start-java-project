package br.eti.freitas.startproject.security.custom;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class CustomPermissionEvaluator implements PermissionEvaluator {

	private static final Logger LOG = LoggerFactory.getLogger(CustomPermissionEvaluator.class);

	@Override
	public boolean hasPermission( Authentication authentication
								, Object targetDomainObject
								, Object permission) {
		LOG.info("hasPermission targetDomainObject: {} authentication: {} - permission: {}", targetDomainObject, authentication, permission);
		if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) {
			LOG.info("- return false");
			return false;
		}
		return hasPrivilege(authentication, targetDomainObject.toString().toLowerCase(), permission.toString().toLowerCase());

	}

	@Override
	public boolean hasPermission( Authentication authentication
			                    , Serializable targetId
			                    , String targetType
			                    , Object permission) {
		LOG.info("hasPermission targetType: {} targetId: {} authentication: {} - permission: {}", targetType, targetId, authentication, permission);
		if ((authentication == null) || (targetType == null) || !(permission instanceof String)) {
			LOG.info("- return false");
			return false;
		}
		return hasPrivilege(authentication, targetType.toLowerCase(), permission.toString().toLowerCase());
	}

	private boolean hasPrivilege(Authentication authentication, String targetType, String permission) {
		LOG.info("hasPrivilege targetType: {} authentication: {} - permission: {}", targetType, authentication, permission);

		for (final GrantedAuthority grantedAuth : authentication.getAuthorities()) {

			System.out.println("grantedAuth1: [" + grantedAuth);
			System.out.println("targetType: [" + targetType.trim() + "]");
			System.out.println("permission: [" + permission + "]");
			System.out.println(" > " + grantedAuth.getAuthority().startsWith(targetType));
			System.out.println(" > " + grantedAuth.getAuthority().contains(permission));
			if (grantedAuth.getAuthority().trim().startsWith(targetType) && grantedAuth.getAuthority().contains(permission)) {
				LOG.info("- hasPrivilege: return true");
				return true;
			}
		}
		LOG.info("- hasPrivilege: return false");
		return false;
	}

}