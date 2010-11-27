/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import javax.servlet.jsp.tagext.VariableInfo;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.TagData;

/**
 * Coloca en la página JSP las variables usuario, permiso, urlEstilo y menu.
 */
public class TagPaginaExt extends TagExtraInfo {

	public TagPaginaExt() {
	}

	/**
	 * Método estandar de la interfaz de TagLibs que coloca en la página JSP variables
	 * generadas dentro del TagLib.
	 * @param data Conjunto de datos a colocar en la JSP.
	 * @return Conjunto de variables que se pueden utilizar dentro de una JSP con los
	 * datos generados dentro del TagLib.
	 */
	public VariableInfo[] getVariableInfo(TagData data){
		return new VariableInfo[]{
			new VariableInfo("usuario","com.rapidsist.portal.configuracion.Usuario", true, VariableInfo.AT_END),
			new VariableInfo("permiso","com.rapidsist.portal.configuracion.Permisos", true, VariableInfo.AT_END),
			new VariableInfo("urlEstilo","java.lang.String", true, VariableInfo.AT_END),
			new VariableInfo("Menu","java.lang.String", true, VariableInfo.AT_END)
		};
	}
}