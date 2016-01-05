package org.securityfilter.authenticator;

import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.securityfilter.realm.SimplePrincipal;
import org.securityfilter.realm.SimpleSecurityRealmBase;

import com.anacleto.base.Configuration;
import com.anacleto.base.Logging;
import com.anacleto.view.UserBean;


/*
 * Store active users.
 * Used to know if a user already logged in from an other ip
 */
public class IPBasedAuthenticator extends SimpleSecurityRealmBase{
	
	private HashMap activePrincipals = new HashMap();
	private static long sessionTimeout = 100;

	private PasswordFileHandler handler;
	
	private Logger logger = Logging.getUserEventsLogger();
	
	private static String ADMINROLE = "anacleto_admin";
	//private static String USERROLE = "anacleto_user";
	
	private static final String LoginErrorUSER = "loginerror.userpassword";
	private static final String LoginErrorIP   = "loginerror.ip";
	
	private String loginError = null;
	
	public IPBasedAuthenticator() {
		handler = Configuration.getAuthController();
	}
	
	public Principal authenticate(String username, String password, String remoteIP) {
        try {
			login(username, password, remoteIP);
			return new SimplePrincipal(username);
		} catch (LoginException e) {
			return null;
		}
	   }
	/**
	 * Login a principal
	 * @param principal
	 * @param remoteAddr
	 * @return
	 * @throws LoginException 
	 */
	synchronized public void login(String username, String password,
		String remoteAddr) throws LoginException{
		try {
			validateUser(username, password.toCharArray());
		} catch (LoginException e) {
			loginError = LoginErrorUSER;
			logger.warn("Unsuccessful login. User:" + username + " from:" + remoteAddr
					+ "root cause:" + e );			
			throw e;
		}
		clearInactive();
		
		//check if ip control should be run
		UserBean u = handler.getUserForName(username);
		if (!u.isIpFilterEnabled())
			return ;
		
		SessionInfo sessionInfo = (SessionInfo)activePrincipals.get(username);
		if (sessionInfo != null){
			//has an active session: check remote addresses
			if (!sessionInfo.remoteAddr.equals(remoteAddr)){
				logger.warn("User:" + username + " tried to log in from:" + remoteAddr + 
						" while logged in from:" + sessionInfo.remoteAddr);
				loginError = LoginErrorIP;
				throw new LoginException("User already logged in from an other address");
			}
		} else {
			SessionInfo newSessionInfo = new SessionInfo(remoteAddr,
					System.currentTimeMillis() + sessionTimeout * 1000);
			activePrincipals.put(username, newSessionInfo);
		}
		
	}

	/**
	 * Logout a principal
	 * @param principal
	 */
	synchronized public void logout(String username){
		activePrincipals.remove(username);
	}
	
	/**
	 * Clears timeoutted sessions 
	 *
	 */
	synchronized private void clearInactive(){
		long now = System.currentTimeMillis();
		List principalsToRemove = new LinkedList();
		
		Iterator it = activePrincipals.keySet().iterator();
		while (it.hasNext()) {
			String username = (String) it.next();
			SessionInfo session = (SessionInfo)activePrincipals.get(username);
			if (session.sessionTimeoutAt < now)
				principalsToRemove.add(username);
		}
		
		Iterator it2 = principalsToRemove.iterator();
		while (it2.hasNext()) {
			String username = (String) it2.next();
			activePrincipals.remove(username);
		}
	}

	public boolean booleanAuthenticate(String username, String password, String remoteIP) {
		try {
			login(username, password, remoteIP);
			return true;
		} catch (LoginException e) {
			return false;
		}
	}

	public boolean isUserInRole(String username, String rolename) {
		UserBean u = handler.getUserForName(username);
		if (rolename.equals(ADMINROLE) && !u.isAdmin())
			return false;
		else 
			return true;
	}

	public static void setSessionTimeout(long sessionTimeout) {
		IPBasedAuthenticator.sessionTimeout = sessionTimeout;
	}
	

	protected void validateUser(String username, char password[]) throws LoginException
	{
		UserBean u = handler.getUserForName(username);
		if (u == null )
		   throw new AccountExpiredException("Unknown user");
		
		//check if user has the wright status
		if (!u.isEnabled())
			throw new AccountExpiredException("User disabled");
		
		char pwd[] = password;
		/*
		try {
			pwd = Utils.cryptPassword(password);
		} catch (Exception e) {
			throw new LoginException("Error encoding password (" + e.getMessage() + ")");
		}
		*/
		char[] savedPasswd = u.getPassword().toCharArray();
		int c;
		for (c = 0; c < pwd.length && c < savedPasswd.length; c++)
			if (pwd[c] != savedPasswd[c])
			   break;
		if (c != pwd.length || c != savedPasswd.length)
		   throw new FailedLoginException("Bad password");
		
	}

	/**
	 * @return Returns the loginError.
	 */
	public String getLoginError() {
		return loginError;
	}

	/**
	 * @param loginError The loginError to set.
	 */
	public void setLoginError(String loginError) {
		this.loginError = loginError;
	}	
	
	
}

final class SessionInfo {
	long sessionTimeoutAt;
	String remoteAddr;
	
	public SessionInfo(String addr, long at) {
		remoteAddr = addr;
		sessionTimeoutAt = at;
	}
}


