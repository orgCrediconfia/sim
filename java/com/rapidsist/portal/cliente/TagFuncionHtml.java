/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import javax.rmi.PortableRemoteObject;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.portal.catalogos.CatalogoSLHome;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.comun.bd.Registro;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;

/**
 * Construye un elemento html dentro de una JSP.
 * <br><br>
 * Parámetros:
 * <ul>
 *	<li>
 * cvehtml.- Parametro que puede manipularse para mostrar código html
 *  </li>
 *	<li>
 */
public class TagFuncionHtml extends TagSupport {
	
	String scvehtml = "";
	
	public void setCvehtml (String sCveHtml) {
		this.scvehtml = sCveHtml;
	}

	/**
	 * Método estándar de la interfaz de TagLibs, que es disparado por el contenedor
	 * de servlets al comenzar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como manejar el flujo
	 * de ejecución del TagLib y el contenido que pueda existir dentro del tag.
	 * @throws JspException Si hubo algún error durante la ejecución del TagLib.
	 */
	public int doStartTag() throws JspException {
		try{
			JspWriter out = pageContext.getOut();

			//OBTIENE EL CONTEXTO DEL SERVIDOR DE APLICACIONES
			Context context = new InitialContext();

			//INICIALIZA EL EJB DE CATALOGOS
			Object referenciaCatalogo = context.lookup("java:comp/env/ejb/CatalogoSL");
			CatalogoSLHome catalogoHome = (CatalogoSLHome)PortableRemoteObject.narrow(referenciaCatalogo, CatalogoSLHome.class);
			CatalogoSL catalogoSL = catalogoHome.create();
			
			//INICIALIZA EL OBJETO PARAMETROS
			Registro parametros = new Registro();
			
			//OBTIENE EL USUARIO
			Usuario usuario = (Usuario)pageContext.getSession().getAttribute("Usuario");
			parametros.addDefCampo("CVE_GPO_EMPRESA",usuario.sCveGpoEmpresa);
			parametros.addDefCampo("CVE_PORTAL",usuario.sCvePortal);
			
			//OBTIENE EL CODIGO HTML
			scvehtml = (String) ExpressionUtil.evalNotNull("out", "scvehtml", scvehtml, Object.class, this, pageContext);
			parametros.addDefCampo("CVE_HTML", this.scvehtml);
			
			//SE RECUPERA EL CODIGO HTML QUE SE VA A MOSTRAR EN LA JSP
			Registro registroCodigoHtml = catalogoSL.getRegistro("HerramientasConfiguracionFuncionHtml", parametros);
	
			//RECUPERA EL CODIGO HTML DEL REGISTRO CODIGOHTML Y LO GUARDA EN EL STRING SCODIGOHTML
			String sCodigoHtml = (String) registroCodigoHtml.getDefCampo("CODIGO_HTML");
			
			if(registroCodigoHtml.getDefCampo("CODIGO_HTML")!= null && !registroCodigoHtml.getDefCampo("CODIGO_HTML").equals("null")){
				out.print(sCodigoHtml);
			}

		}//Try END
		
		catch(Exception e){
			e.printStackTrace();
		
		}
		return javax.servlet.jsp.tagext.TagSupport.SKIP_BODY;
	}

	/**
	 * Método estándar de la interfaz de TagLibs, el cual es disparado por el contenedor
	 * de servlets al finalizar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como debe evaluar el resto
	 * de una página JSP una vez terminado el TagLib.
	 */
	public int doEndTag() {
		return javax.servlet.jsp.tagext.TagSupport.EVAL_PAGE;
	}
}