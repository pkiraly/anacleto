package org.securityfilter.authenticator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.anacleto.base.Logging;
import com.anacleto.view.UserBean;

/**
 * Common operations with the password file:
 * get users, add user, etc...
 * @author robi
 *
 */
public class PasswordFileHandler {
	
	private Map                 users = new TreeMap();
	private long                lastModified    = 0;
	
	private String 				pwdFile;
	/*
	private static final Principal userRole = new TypedPrincipal("anacleto_user",  
			TypedPrincipal.GROUP);  

	private static final Principal adminRole = new TypedPrincipal("anacleto_admin",  
			TypedPrincipal.GROUP);  
	*/
	
	private Logger logger = Logging.getAdminLogger();
	
	public void init(String pwdFile){
		this.pwdFile = pwdFile;
		reload();
	}
	
	/**
	 * Get users with the role 'anacleto_user'
	 * @return
	 */
	public Collection getNormalUsers() {
		return getUsersForRole(false);
	}

	public Collection getAdminUsers() {
		return getUsersForRole(true);
	}

	public UserBean getUserForName(String username){
		return (UserBean)users.get(username);
	}
	
	private Collection getUsersForRole(boolean isAdmin){
		
		Collection retUsers = new LinkedList();
		Iterator it = users.keySet().iterator();
		while (it.hasNext()) {
			String currUserStr = (String) it.next();
			UserBean currUser = (UserBean)users.get(currUserStr);
			if (currUser.isAdmin() == isAdmin)
				retUsers.add(currUser);
		}
		return retUsers;
	}
	
	/**
	 * Load user data from a password file
	 * the password file has to have the format:
	 * userID:userName:active(true|false):password:role(anacleto_user|anacleto_admin)
	 * 
	 * @param f
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private synchronized void load(File f) throws FileNotFoundException, IOException{
		
		lastModified = f.lastModified();
		BufferedReader r = new BufferedReader(new FileReader(f));
		users = new TreeMap();
		String l = r.readLine();
		while (l != null)
		{
			int hash = l.indexOf('#');
			if (hash != -1) l = l.substring(0, hash);
			l = l.trim();
			if (l.length() != 0)
			{
				//StringTokenizer t = new StringTokenizer(l, ":");
				String lineToken[] = l.split(":");
				if (lineToken.length != 6){
					logger.warn("Passwordfile format error on line: " + 
							l);
					continue;
				}
					
				UserBean u = new UserBean();
				
				u.setUser(lineToken[0]);
				u.setRealName(lineToken[1]);
				u.setEmail(lineToken[2]);
				u.setEnabled((lineToken[3].equals("false")? false : true));
				u.setPassword(lineToken[4]);
				u.setAdmin((lineToken[5].equals("true")? true : false));
				
				users.put(lineToken[0], u);
			}
			l = r.readLine();
		}
		r.close();
	}
	
	/**
	 * Load user data from a password file
	 * the password file has to have the format:
	 * userID:userName:active(true|false):password:role(anacleto_user|anacleto_admin)
	 * 
	 * @param f
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws UserAlreadyExistsException 
	 */
	public void addNewUser(UserBean newUser) throws IOException, UserAlreadyExistsException{
		if (users.containsKey(newUser.getUser()))
			throw new UserAlreadyExistsException(newUser.getUser());
		
		logger.info("New user: " + newUser.getUser() + " is added");
		
		users.put(newUser.getUser(), newUser);
		saveFile();
	}
	
	/**
	 * Modify user
	 */
	public void modifyUser(UserBean modUser) {
		
		if (!users.containsKey(modUser.getUser()))
			return;
		
		logger.info("User: " + modUser.getUser() + " is modified");
		
		users.put(modUser.getUser(), modUser);
		saveFile();
			    
	}
	
	public void deleteUser(UserBean delUser) {
		
		if (!users.containsKey(delUser.getUser()))
			return;
		
		logger.info("User: " + delUser.getUser() + " is deleted");
		
		users.remove(delUser.getUser());
		saveFile();
	}
	
	
	/**
	 * Save password file
	 *
	 */
	private synchronized void saveFile() {
		
		FileOutputStream out = null;
		try{
			File f = new File(pwdFile);
			if (!f.exists())
				f.createNewFile();

			out = new FileOutputStream(f, false);
			String header = "# Anacleto password file" + 
							System.getProperty("line.separator") +
							"# userID:realname:email:enabled:password:role(s)" +
							System.getProperty("line.separator");
			out.write(header.getBytes());
			
			Iterator it = users.keySet().iterator();
			while (it.hasNext()) {
				String userStr = (String) it.next();
				UserBean currUser = (UserBean)users.get(userStr);
				
				String newLine = currUser.getUser()     + ":"
							   + currUser.getRealName() + ":" 
							   + currUser.getEmail() + ":" 
							   + (currUser.isEnabled()? "true": 
							   		"false") + ":"
				               + currUser.getPassword() + ":" 
				               + (currUser.isAdmin()? "true" : 
				            		"false")
				               + System.getProperty("line.separator");
	
				out.write(newLine.getBytes());
			}
			out.flush();
		} catch (FileNotFoundException e) {
			logger.error("Error occured during writing password file. Root cause:"
					+ e.getMessage());
		} catch (IOException e) {
			logger.error("Error occured during writing password file. Root cause:"
					+ e.getMessage());
			
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				logger.error("Error occured during writing password file. Root cause:"
						+ e.getMessage());
			}
		}
	    
	}
	
	private void reload()  {
		File f = new File(pwdFile);
		try{	
			if (f.lastModified() != lastModified)
			   load(f);
		} catch (FileNotFoundException e) {
			logger.warn("Password fie not found. Expected location:" + f);
		} catch (IOException e) {
			logger.warn("IOError occured during password file access:" + e.getMessage());;
		}
	}

}
