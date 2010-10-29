/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import com.rapidsist.comun.util.Fecha2;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpServletRequest;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;

/**
 * Construye un campo para captura de fechas y despliega un calendario auxiliar.
 *
 *<p><br><b>Forma de Uso:</b></p>
 *
 * <p>Incluir el manejo de ventanas:</p>
 * <blockquote>
 *   <p><font size="2">&lt;script src='/com/monex/comun/bibliotecas/bManejoVentana.js'&gt;&lt;/script&gt;</font></p>
 * </blockquote>
 *
 * <p>Dada la siguiente referencia:<br>
 * </p>
 * <blockquote>
 *   <p> <font size="2">&lt;%@ taglib uri=&quot;Aplicacion&quot; prefix=&quot;Monex&quot;%&gt;
 *       </font></p>
 * </blockquote>
 * <p>Dentro de una forma se inserta el c&oacute;digo:</p>
 * <blockquote>
 *   <p><font size="2">&lt;Monex:FrmCalendario contenedor=<i><b>nombre_forma</b></i>
 *    nombre=<i><b>nombre_campo_fecha</b></i> fecha=<i><b>valor_fecha</b></i>/&gt;</font></p>
 *    <p>d&oacute;nde</p>
 * <blockquote>
 *   <p><i><b><font size="2">nombre_forma</font></b></i> es el nombre de la forma
 *       en la cual se construye el campo de fecha,</p>
 *   <p><i><b><font size="2">nombre_campo_fecha</font></b></i> es el nombre con
 *      el que se crea el campo fecha, si no se proporciona se crea con el nombre
 * <b><font size="2">'calendario'</font>,</b></p>
 * <p><i><b><font size="2">valor_fecha</font></b></i> es el valor de la fecha
 *  en formato dd/mm/aaaa, no es obligatorio.</p>
 *  </blockquote>
 *  </blockquote>
 *  <p>&nbsp;</p>
 *  <p><b>Ejemplo:</b></p>
 *  <blockquote>
 *    <p><font size="2">&lt;Monex:FrmCalendario contenedor='frmBusqueda' nombre='FInicProgBus'
 *    fecha='&lt;%=((request.getParameter(&quot;FInicProg&quot;))==null ? &quot;&quot;
 *    : request.getParameter(&quot;FInicProg&quot;))%&gt;'/&gt;</font></p>
 */
public class TagCalendario2 extends TagSupport {
		String  sContenedor ="";
		String  sControlNombre ="calendario2";
		String  sControlValor="";
		String  sEtiqueta = "";
		String  sFecha = "";
		String  sNombre = "calendario2";
		String  sObligatorio ="false";
		boolean sEsFechaMayorSis = false;
		boolean sEsFechaSis = false;
		boolean sTerminarenglon= true;
		
		public void setContenedor(String sContenedor){
			this.sContenedor = sContenedor;
		}
		public void setControlnombre(String sControlNombre){
			this.sControlNombre = sControlNombre;
		}
		public void setControlvalor(String sControlValor){
			this.sControlValor = sControlValor;
		}
		public void setEtiqueta(String sEtiqueta){
			this.sEtiqueta = sEtiqueta;
		}
		public void setFecha(String sFecha){
			this.sFecha = sFecha;
		}
		public void setNombre(String sNombre){
			this.sNombre = sNombre;
		}
		public void setObligatorio(String sObligatorio){
			this.sObligatorio = sObligatorio;
		}
		public void setEsfechamayorsis(boolean sEsFechaMayorSis){
			this.sEsFechaMayorSis = sEsFechaMayorSis;
		}
		public void setEsfechasis(boolean sEsFechaSis){
			this.sEsFechaSis = sEsFechaSis;
		}
		public void setTerminarenglon(boolean sTerminarenglon){
			this.sTerminarenglon = sTerminarenglon;
		}

		/**
		 *
	     * @throws JspException Si hubo algún error durante la ejecución del TagLib.
		 */
		public int doStartTag() throws JspException {

		String Salida = "";
		int iNumRenglonesForma= ((Integer)pageContext.getAttribute("NumRenglonesForma")).intValue();
		pageContext.setAttribute("NumRenglonesForma", new Integer(iNumRenglonesForma++));

		if (!this.sControlValor.equals("")) {
			String sParamControlValor = (String) ExpressionUtil.evalNotNull("out", "controlvalor", sControlValor + "x", Object.class, this, pageContext);
			this.sControlValor = sParamControlValor.substring(0, sParamControlValor.length() - 1);
		}
		if( this.sControlValor.equals("") && this.sEsFechaSis){
			Calendar gcFecSis = Calendar.getInstance();
			java.text.SimpleDateFormat fmter = new java.text.SimpleDateFormat ("dd/MM/yyyy");
			this.sControlValor = fmter.format(gcFecSis.getTime());
		}

		try {
			String sRutaContexto = ( (HttpServletRequest) pageContext.getRequest()).getContextPath();
			JspWriter out = pageContext.getOut();

			out.println("\t\t\t\t\t\t\t\t\t\t <script src='" + sRutaContexto + "/comun/lib/bCalendar.js'></script>");
			out.println("\t\t\t\t\t\t\t\t\t\t <script src='" + sRutaContexto + "/comun/lib/bFechaForma.js'></script>");
			out.println("\t\t\t\t\t\t\t\t\t\t <script language=\"javascript\" src='" + sRutaContexto + "/comun/lib/calendario2/config/xc2_default.js'></script>");
			out.println("\t\t\t\t\t\t\t\t\t\t <script language=\"javascript\" src='" + sRutaContexto + "/comun/lib/calendario2/script/xc2_window.js'></script>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t<tr>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t <th>" + sEtiqueta + "</th>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t <td id='"+ sControlNombre +"'>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t <input class='fondocaptura' maxlength='15' name='"+ sControlNombre +"' ");
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t onBlur='ValidaFecha(this)' ");
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t onKeyPress='if ( (event.keyCode < 47) || (event.keyCode > 57 ) ) event.returnValue = false;' ");
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t size=15 value='" + this.sControlValor + "'>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t <img border=0 src=\"/portal/comun/lib/calendario2/img/arrowdate_blue.gif\" onClick=\"javascript:showCalendar('',document.forms[0]." + sControlNombre + ",null,'','Calendario',-80,16)\">");
			
			if(sObligatorio.equals("true")){
			   out.println("\t\t\t\t\t\t\t\t\t\t\t\t<script>fAgregaCampo('" + sControlNombre + "', '" + sEtiqueta + "');</script>");
			}
			out.println("\t\t\t\t\t\t\t\t\t\t\t</td> ");
			
			if( this.sTerminarenglon){
				out.println("\t\t\t\t\t\t\t\t\t\t</tr>");
			}
			
			out.println("		<script languaje=\"javascript\">");
			out.println("          //*******************************************************************");
			out.println("          //VALIDA EL FORMATO DE LA FECHA");
			out.println("          //*******************************************************************");
			out.println("          function ValidaFecha(objName) {");
			out.println("            checkdate(objName);");
			//VERIFICA SI LA FECHA NO PUEDE SER MAYOR A LA DEL SISTEMA
			if( this.sEsFechaMayorSis){
				out.println("            fFechaControlMayor();");
			}
			out.println("          }");
			
			//VERIFICA SI LA FECHA NO PUEDE SER MAYOR A LA DEL SISTEMA
			if( this.sEsFechaMayorSis){
				//SE OBTIENE LA FECHA DEL SISTEMA
				Fecha2 fecha = new Fecha2();
				java.util.Date dFechaSistema = new Date(); 
				String sFechaSistema = fecha.formatoSimple(dFechaSistema);
				
				int iLongitudFechaSistema = sFechaSistema.length();
				//VERIFICA SI LA LONGITUD ES IGUAL A NUEVE
				if (iLongitudFechaSistema == 9){
					sFechaSistema = "0"+sFechaSistema;
				}
			
				out.println("          function fFechaControlMayor() {");
				out.println("            var fechaSistema = '"+sFechaSistema+"';");
				out.println("            var fechaControl = "+sContenedor+"."+sControlNombre+".value;");
				out.println("            var bRes = false; ");
				out.println("            var sDia0 = fechaControl.substr(0, 2);"); 
				out.println("            var sMes0 = fechaControl.substr(3, 2);"); 
				out.println("            var sAno0 = fechaControl.substr(6, 4);"); 
				out.println("            var sDia1 = fechaSistema.substr(0, 2);"); 
				out.println("            var sMes1 = fechaSistema.substr(3, 2);"); 
				out.println("            var sAno1 = fechaSistema.substr(6, 4);"); 
				out.println("            if (sAno0 > sAno1)"); 
				out.println("				bRes = true;"); 
				out.println("            else { ");
				out.println("            	if (sAno0 == sAno1){"); 
				out.println("            		if (sMes0 > sMes1)"); 
				out.println("						bRes = true;"); 
				out.println("            		else { ");
				out.println("            			if (sMes0 == sMes1)"); 
				out.println("            				if (sDia0 > sDia1)"); 
				out.println("								bRes = true;"); 
				out.println("            		} ");
				out.println("            	} ");
				out.println("            } ");
				out.println("            if (bRes == true){");
				out.println("            	alert('La fecha '+fechaControl+' no debe ser mayor a la fecha del sistema.');");
				out.println("            	"+sContenedor+"."+sControlNombre+".value='';");
				out.println("            	"+sContenedor+"."+sControlNombre+".focus();");
				out.println("            }");
				out.println("          }");
			}
			
			out.println("		</script>");
			
		}

		catch ( IOException e){
			e.printStackTrace();
		}

		return(TagSupport.SKIP_BODY);

	}

		public int doEndTag(){
				return (TagSupport.EVAL_PAGE);
		}
}