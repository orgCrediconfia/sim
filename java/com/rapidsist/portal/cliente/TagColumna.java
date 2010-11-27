/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;

/**
 * Imprime una columna de una tabla. Si este tag se incluye dentro del tag TablaListaTitulos
 * entonces pintará la columna como título (th). Si el tag se encuentra dentro del tag
 * TablaListaRenglon entonces pinta una columna con td.
 * <br><br>
 * Parametros:
 * <ul>
 *	<li>
 * tipovalor.- Define el tipo de dato que aloja la columna.<br>
 * Posibles valores: texto, numero, fecha, moneda.
 *  </li>
 *	<li>
 * ancho.- Ancho de la columna (este valor se le pasa directamente al atributo width del
 * tag td o th
 *  </li>
 *	<li>
 * ajustelinea.- Define si la columna realiza un ajuste de linea.<br>
 * Posibles valores: true, false (aplica al tag tr o td el atributo nowrap).<br>
 * Valor por default: false.
 *  </li>
 *	<li>
 * valor.- Valor que se le asigna a la columna.
 *  </li>
 *	<li>
 * control.- Tipo de control html construido por este tag.<br>
 * Posibles valores: checkbox.
 *  </li>
 *	<li>
 * controlnombre.- Nombre del control html, con el cual se podrá manipularlo a través de código
 * javascript.
 *  </li>
 * </ul>
 */
public class TagColumna extends TagSupport {
	String sTipo = "";
	String sTipoValor = "";
	String sAncho = "";
	String sAjusteLinea = "false";
	String sValor = "";
	String sControl="";
	String sControlNombre="";
	String sBgColor="";

	public void setTipo(String sTipo){
		this.sTipo = sTipo;
	}

	public void setTipovalor(String sTipovalor){
		this.sTipoValor = sTipovalor;
	}

	public void setAncho(String sAncho){
		this.sAncho = sAncho;
	}

	public void setAjustelinea(String sAjusteLinea){
		this.sAjusteLinea = sAjusteLinea;
	}

	public void setValor(String sValor){
		this.sValor = sValor;
	}

	public void setControl(String sControl){
		this.sControl = sControl;
	}

	public void setControlnombre(String sControlNombre){
		this.sControlNombre = sControlNombre;
	}
	
	public void setBgcolor(String sBgColor){
		this.sBgColor = sBgColor;
	}

	/**
	 * Método estandar de la interfaz de TagLibs, que es disparado por el contenedor
	 * de servlets al comenzar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como manejar el flujo
	 * de ejecución del TagLib y el contenido que pueda existir dentro del tag.
	 * @throws JspException Si hubo algún error durante la ejecución del TagLib.
	 */
	public int doStartTag() throws JspException {
		try{

			if (!sValor.equals("")){
				String valor = (String) ExpressionUtil.evalNotNull("out", "valor", sValor + "x", Object.class, this, pageContext);
				sValor = valor.substring(0, valor.length() - 1);
			}

			if (!sControl.equals("")){
				String control = (String) ExpressionUtil.evalNotNull("out", "control", sControl + "x", Object.class, this, pageContext);
				sControl = control.substring(0, control.length() - 1);
			}

			if (!sControlNombre.equals("")){
				String controlNombre = (String) ExpressionUtil.evalNotNull("out", "controlNombre", sControlNombre + "x", Object.class, this, pageContext);
				sControlNombre = controlNombre.substring(0, controlNombre.length() - 1);
			}
			
			if (!sBgColor.equals("")){
				String BgColor = (String) ExpressionUtil.evalNotNull("out", "bgColor", sBgColor + "x", Object.class, this, pageContext);
				sBgColor = BgColor.substring(0, BgColor.length() - 1);
			}
		

			//OBTIENE EL TAG PADRE PARA IDENTIFICA SI IMPRIME LA COLUMNA COMO UN TITULO O
			//COMO UN CAMPO
			String sTagPadre = this.getParent().getClass().getName();
			if (sTagPadre.equals("com.rapidsist.portal.cliente.TagTablaListaTitulos")){
				sTipo = "titulo";
			}
			else{
				sTipo = "columna";
			}

			JspWriter out = pageContext.getOut();
			out.println();

			String sAlineacion = "left";
			if (sTipoValor.equals("numero") || sTipoValor.equals("moneda")) {
				sAlineacion = "right";
			}
			String sNoWrap = " nowrap ";
			if (sAjusteLinea.equals("true")) {
				sNoWrap = "";
			}
			
				
			// SI SE SOLICITA UN COLOR DE BACKGROUND PARA EL <TD> SE AGREGA UN ID AL <TD>
			// ESTE ID SE DEBE AGREGAR A LA HOJA DE ESTILOS PARA QUE LO PUEDA RECONOCER
			if (!sBgColor.equals("")){
				sBgColor = " id=\""+ sBgColor + "\" ";
			}

			//VERIFICA SI SE DEBE PINTAR UN CONTROL HTML EN LA PAGINA
			if (!sControl.equals("")){
				if (sControl.equals("checkbox")){
					out.print("\t\t\t\t\t\t\t\t\t<td width='" + sAncho + "' align='" + sAlineacion + "' " + sNoWrap + " ><input type='checkbox' name='" + sControlNombre + "'" + (sValor.equals("V") ? " checked " : "" )+ " />");
				}else if (sControl.equals("Texto")){
					out.print("\t\t\t\t\t\t\t\t\t<td width='" + sAncho + "' align='" + sAlineacion + "' " + sNoWrap + " ><input type='checkbox' name='" + sControlNombre + "'" + (sValor.equals("V") ? " checked " : "" )+ " />");
				}
			}
			else{
				//VERIFICA SI EL TIPO ES TITULO
				if (sTipo.equals("titulo")) {
					out.print("\t\t\t\t\t\t\t\t\t<th width='" + sAncho + "' align='" + sAlineacion + "' " + sNoWrap + " >" + sValor);
				}
				else {
					out.print("\t\t\t\t\t\t\t\t\t<td " + sBgColor + " width='" + sAncho + "' align='" + sAlineacion + "' " + sNoWrap + " >" + sValor);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return(TagSupport.EVAL_BODY_INCLUDE);
	}

	/**
	 * Método estándar de la interfaz de TagLibs, el cual es disparado por el contenedor
	 * de servlets al finalizar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como debe evaluar el resto
	 * de una página JSP una vez terminado el TagLib.
	 */
	public int doEndTag(){
		try {
			JspWriter out = pageContext.getOut();
			out.println();

			if (sTipo.equals("titulo")){
				out.println("\t\t\t\t\t\t\t\t\t</th>");
			}
			else{
				out.println("\t\t\t\t\t\t\t\t\t</td>");
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return TagSupport.EVAL_PAGE;
	}
}