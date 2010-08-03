package com.orionsupport.security;

import com.orionsupport.security.SimpleUserManager;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.catalogos.CatalogoSLHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import com.rapidsist.comun.bd.Registro;

/**
 * Clase utilizada para la autentificación de usuarios.
 */

public class MyUserManager extends SimpleUserManager {

	protected boolean userExists( String username ) {
		return true;
	}

	protected boolean checkPassword( String username, String password ) {
		
		boolean bPassword = false;		
		try{
			Registro parametros = new Registro();
			//OBTIENE EL CONTEXTO DEL SERVIDOR DE APLICACIONES
			Context context = new InitialContext();
			//INICIALIZA EL EJB DE CATALOGOS
			Object referenciaCatalogo = context.lookup("java:comp/env/ejb/CatalogoSL");
			CatalogoSLHome catalogoHome = (CatalogoSLHome)PortableRemoteObject.narrow(referenciaCatalogo, CatalogoSLHome.class);
			CatalogoSL catalogoSL = catalogoHome.create();
			
			parametros.addDefCampo("FILTRO","UsuarioAutentificacion");
			parametros.addDefCampo("CVE_USUARIO_AUTENTIFICACION",username);
			parametros.addDefCampo("PASSWORD_AUTENTIFICACION",password);
			//OBTIENE EL USUARIO
			Registro registroUsuario = catalogoSL.getRegistro("GeneralesUsuario", parametros);
			//VERIFICA SI EXISTE EL USUARIO
			if (registroUsuario == null){
				System.out.println("AUTENTIFICACION INCORRECTA");
				return bPassword;
			}
			else{
				System.out.println("AUTENTIFICACION CORRECTA");
				bPassword = true;
			}
		}
		catch(Exception e){
		}
		return bPassword;
	}
	
	protected boolean inGroup( String username, String groupname ) {
		return true;
	}
}
