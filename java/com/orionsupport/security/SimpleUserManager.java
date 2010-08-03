package com.orionsupport.security;

import com.evermind.security.AbstractUserManager;
import com.evermind.security.User;
import com.evermind.security.Group;

/**
 * Utility to create simple read only UserManagers very quickly.
 *
 * @version 1.0
 *
 * @see com.evermind.security.UserManager
 * @see com.evermind.security.User
 */
public abstract class SimpleUserManager extends AbstractUserManager {

	/**
	 * Determine if the given username is a valid user on the system.
	 */
	protected abstract boolean userExists( String username );

	/**
	 * Check the supplied password for a user is valid.
	 */
	protected abstract boolean checkPassword( String username, String password );

	/**
	 * Determine if a user is a member of a particular group.
	 */
	protected abstract boolean inGroup( String username, String groupname );

	public User getUser( String username ) {
		System.out.println("Método getUser");
		if ( username != null && userExists( username ) ) {
			return new UserWrapper( username );
		}
		else {
			return getParent().getUser( username );
		}
	}

	public Group getGroup(String groupName) {
		System.out.println("Método Group");
		if (groupName == null){
			return null;
		}
		try{
			Group group = createGroup(groupName);
			return group;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Inner class used to return an Orion friendly User wrapper around
	 * our methods.
	 */
	class UserWrapper implements User {

		private String name;

		UserWrapper( String name ) {
			System.out.println("Método UserWrapper");
			this.name = name;
		}

		public String getName() {
			System.out.println("Método getName");
			return name;
		}

		public boolean authenticate( String password ) {
			System.out.println("Método authenticate");
			if ( password == null ) {
				return false;
			}
			else {
				boolean   result = checkPassword( name, password );
				return result;
				
			}
		}

		
		public boolean isMemberOf( Group group ) {
			System.out.println("Método isMemberOf");
			return inGroup( name, group.getName() );
		}


		// The following methods are not implemented (because they don't need to be).

		public java.util.Set getGroups() { return null; }
		public java.lang.String getDescription() { return null; }
		public void setDescription(java.lang.String description) {}
		public java.util.Locale getLocale() { return null; }
		public void setLocale(java.util.Locale locale) {}
		public boolean hasPermission(java.security.Permission permission) { return false; }
		public void setPassword(java.lang.String password) {}
		public java.lang.String getPassword() { return null; }
		public java.math.BigInteger getCertificateSerial() { return null; }
		public java.lang.String getCertificateIssuerDN() { return null; }
		public void setCertificate(java.lang.String issuerDN, java.math.BigInteger serial) throws java.lang.UnsupportedOperationException {}
		public void setCertificate(java.security.cert.X509Certificate cert) {}
		public void addToGroup(com.evermind.security.Group group) throws java.lang.UnsupportedOperationException {}
		public void removeFromGroup(com.evermind.security.Group group) throws java.lang.UnsupportedOperationException {}

	}

}
