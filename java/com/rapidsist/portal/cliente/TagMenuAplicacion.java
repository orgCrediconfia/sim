/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.rapidsist.portal.configuracion.PortalSLHome;
import com.rapidsist.portal.configuracion.PortalSL;
import javax.naming.Context;
import javax.naming.InitialContext;
import com.rapidsist.portal.configuracion.Usuario;
import javax.rmi.PortableRemoteObject;

/**
 * Imprime el componente de menú de aplicación en una página.
 */
public class TagMenuAplicacion extends TagSupport {

	/**
	 * Método estandar de la interfaz de TagLibs, que es disparado por el contenedor
	 * de servlets al comenzar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como manejar el flujo
	 * de ejecución del TagLib y el contenido que pueda existir dentro del tag.
	 * @throws JspException Si hubo algún error durante la ejecución del TagLib.
	 */
	public int doStartTag() throws JspException {
		try{
			//OBTIENE EL CONTEXTO DEL SERVIDOR DE APLICACIONES
			Context context = new InitialContext();
			Object referencia = context.lookup("java:comp/env/ejb/PortalSL");			
			PortalSLHome portalHome = (PortalSLHome)PortableRemoteObject.narrow(referencia, PortalSLHome.class);			
			PortalSL configuracion = portalHome.create();

			JspWriter out = pageContext.getOut();
			//OBTENEMOS LOS DATOS DE LA SESION DEL USUARIO
			Usuario usuario=(Usuario)pageContext.getSession().getAttribute("Usuario");

			String sMenu = configuracion.getMenuAplicacion(usuario.sAplicacionActual, usuario.sCvePerfilActual, usuario.sNomAplicacionWeb);
			out.println(sMenu);

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return(TagSupport.SKIP_BODY);
	}
}