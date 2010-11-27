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
 * Construye dos campos para captura de un rango de fechas y despliega su respectivo calendario auxiliar.
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
public class TagCalendarioRango extends TagSupport {
		String  sContenedor1 ="";
		String  sControlNombre1 ="calendario1";
		String  sControlValor1="";
		String  sEtiqueta1 = "";
		String  sFecha1 = "";
		String  sNombre1 = "calendario1";
		String  sObligatorio1 ="false";
		boolean sEsFechaMayorSis1 = false;
		boolean sEsFechaSis1 = false;
		boolean sTerminarenglon1= true;
		String  sContenedor2 ="";
		String  sControlNombre2 ="calendario2";
		String  sControlValor2="";
		String  sEtiqueta2 = "";
		String  sFecha2 = "";
		String  sNombre2 = "calendario2";
		String  sObligatorio2 ="false";
		boolean sEsFechaMayorSis2 = false;
		boolean sEsFechaSis2 = false;
		boolean sTerminarenglon2= true;
		
		public void setContenedor1(String sContenedor1){
			this.sContenedor1 = sContenedor1;
		}
		public void setControlnombre1(String sControlNombre1){
			this.sControlNombre1 = sControlNombre1;
		}
		public void setControlvalor1(String sControlValor1){
			this.sControlValor1 = sControlValor1;
		}
		public void setEtiqueta1(String sEtiqueta1){
			this.sEtiqueta1 = sEtiqueta1;
		}
		public void setFecha1(String sFecha1){
			this.sFecha1 = sFecha1;
		}
		public void setNombre1(String sNombre1){
			this.sNombre1 = sNombre1;
		}
		public void setObligatorio1(String sObligatorio1){
			this.sObligatorio1 = sObligatorio1;
		}
		public void setEsfechamayorsis1(boolean sEsFechaMayorSis1){
			this.sEsFechaMayorSis1 = sEsFechaMayorSis1;
		}
		public void setEsfechasis1(boolean sEsFechaSis1){
			this.sEsFechaSis1 = sEsFechaSis1;
		}
		public void setTerminarenglon1(boolean sTerminarenglon1){
			this.sTerminarenglon1 = sTerminarenglon1;
		}

		public void setContenedor2(String sContenedor2){
			this.sContenedor2 = sContenedor2;
		}
		public void setControlnombre2(String sControlNombre2){
			this.sControlNombre2 = sControlNombre2;
		}
		public void setControlvalor2(String sControlValor2){
			this.sControlValor2 = sControlValor2;
		}
		public void setEtiqueta2(String sEtiqueta2){
			this.sEtiqueta2 = sEtiqueta2;
		}
		public void setFecha2(String sFecha2){
			this.sFecha2 = sFecha2;
		}
		public void setNombre2(String sNombre2){
			this.sNombre2 = sNombre2;
		}
		public void setObligatorio2(String sObligatorio2){
			this.sObligatorio2 = sObligatorio2;
		}
		public void setEsfechamayorsis2(boolean sEsFechaMayorSis2){
			this.sEsFechaMayorSis2 = sEsFechaMayorSis2;
		}
		public void setEsfechasis2(boolean sEsFechaSis2){
			this.sEsFechaSis2 = sEsFechaSis2;
		}
		public void setTerminarenglon2(boolean sTerminarenglon2){
			this.sTerminarenglon2 = sTerminarenglon2;
		}
		
		
		/**
		 *
	     * @throws JspException Si hubo algún error durante la ejecución del TagLib.
		 */
		public int doStartTag() throws JspException {

		String Salida = "";
		int iNumRenglonesForma= ((Integer)pageContext.getAttribute("NumRenglonesForma")).intValue();
		pageContext.setAttribute("NumRenglonesForma", new Integer(iNumRenglonesForma++));

		if (!this.sControlValor1.equals("")) {
			String sParamControlValor1 = (String) ExpressionUtil.evalNotNull("out", "controlvalor", sControlValor1 + "x", Object.class, this, pageContext);
			this.sControlValor1 = sParamControlValor1.substring(0, sParamControlValor1.length() - 1);
		}
		if( this.sControlValor1.equals("") && this.sEsFechaSis1){
			Calendar gcFecSis = Calendar.getInstance();
			java.text.SimpleDateFormat fmter = new java.text.SimpleDateFormat ("dd/MM/yyyy");
			this.sControlValor1 = fmter.format(gcFecSis.getTime());
		}

		if (!this.sControlValor2.equals("")) {
			String sParamControlValor2 = (String) ExpressionUtil.evalNotNull("out", "controlvalor", sControlValor2 + "x", Object.class, this, pageContext);
			this.sControlValor2 = sParamControlValor2.substring(0, sParamControlValor2.length() - 1);
		}
		if( this.sControlValor2.equals("") && this.sEsFechaSis2){
			Calendar gcFecSis = Calendar.getInstance();
			java.text.SimpleDateFormat fmter = new java.text.SimpleDateFormat ("dd/MM/yyyy");
			this.sControlValor2 = fmter.format(gcFecSis.getTime());
		}
		
		try {
			String sRutaContexto = ( (HttpServletRequest) pageContext.getRequest()).getContextPath();
			JspWriter out = pageContext.getOut();

			out.println("\t\t\t\t\t\t\t\t\t\t <script src='" + sRutaContexto + "/comun/lib/bCalendar.js'></script>");
			out.println("\t\t\t\t\t\t\t\t\t\t <script src='" + sRutaContexto + "/comun/lib/bFechaForma.js'></script>");
			out.println("\t\t\t\t\t\t\t\t\t\t <script language=\"javascript\" src='" + sRutaContexto + "/comun/lib/calendario2/config/xc2_default.js'></script>");
			out.println("\t\t\t\t\t\t\t\t\t\t <script language=\"javascript\" src='" + sRutaContexto + "/comun/lib/calendario2/script/xc2_window.js'></script>");
			
			out.println("<script language=\"javascript\">");
			out.println("function beforeSetDateValue(ref_field, target_field, date) {");
			out.println("  if (date!=\"\") {");
			out.println("    var startDate=document.forms[0][\"" + sControlNombre1 + "\"];");
			out.println("    var endDate=document.forms[0][\"" + sControlNombre2 + "\"];");

			out.println("    if (target_field==endDate &&");
			out.println("        checkDate(getDateValue(startDate))==0 &&");
			out.println("        compareDates(getDateValue(startDate), date)>0) {");
			out.println("      date=getDateValue(endDate);");
			out.println("      alert(\"La " + sEtiqueta1 + " no debe ser mayor a la " + sEtiqueta2 + ", pruebe otra vez.\");");
			out.println("    }");
			out.println("  }");

			out.println("  return date;");
			out.println("}");

			out.println("function afterSetDateValue(ref_field, target_field, date) {");
			out.println("  if (date!=\"\") {");
			out.println("    var startDate=document.forms[0][\"" + sControlNombre1 + "\"];");
			out.println("    var endDate=document.forms[0][\"" + sControlNombre2 + "\"];");

			out.println("    if (target_field==startDate &&");
			out.println("        checkDate(getDateValue(endDate))==0 &&");
			out.println("        compareDates(date, getDateValue(endDate))>0) {");
			out.println("      setDateValue(endDate, date);");
			out.println("      alert(\"La " + sEtiqueta2 + " no puede ser menor a la " + sEtiqueta1 + ". Se tomara el valor de la " + sEtiqueta1 + " para ambas.\");");
			out.println("    }");
			out.println("  }");
			out.println("}");

			out.println("function checkForm() {");
			out.println("    var startDate=document.forms[0][\"" + sControlNombre1 + "\"];");
			out.println("    var endDate=document.forms[0][\"" + sControlNombre2 + "\"];");
			out.println("    if (compareDates(getDateValue(startDate), getDateValue(endDate))>0) {");
			out.println("       alert(\"La " + sEtiqueta1 + " debe ser siempre menor a la " + sEtiqueta2 + ", pruebe otra vez\");");
			out.println("    }");
			out.println("}");
						
			out.println("</script>");
			
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t<tr>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t <th>" + sEtiqueta1 + "</th>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t <td id='"+ sControlNombre1 +"'>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t <input class='fondocaptura' maxlength='15' name='"+ sControlNombre1 +"' ");
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t onBlur='ValidaFecha(this); checkForm()' ");
			//out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t onBlur='checkForm()' ");
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t onKeyPress='if ( (event.keyCode < 47) || (event.keyCode > 57 ) ) event.returnValue = false;' ");
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t size=15 value='" + this.sControlValor1 + "'>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t <img border=0 src=\"/portal/comun/lib/calendario2/img/arrowdate_blue.gif\" onClick=\"javascript:showCalendar('',document.forms[0]." + sControlNombre1 + ",null,'','Calendario',-80,16)\">");
			
			if(sObligatorio1.equals("true")){
			   out.println("\t\t\t\t\t\t\t\t\t\t\t\t<script>fAgregaCampo('" + sControlNombre1 + "', '" + sEtiqueta1 + "');</script>");
			}
			out.println("\t\t\t\t\t\t\t\t\t\t\t</td> ");
			
			if( this.sTerminarenglon1){
				out.println("\t\t\t\t\t\t\t\t\t\t</tr>");
			}
			
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t<tr>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t <th>" + sEtiqueta2 + "</th>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t <td id='"+ sControlNombre2 +"'>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t <input class='fondocaptura' maxlength='15' name='"+ sControlNombre2 +"' ");
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t onBlur='ValidaFecha(this); checkForm()' ");
			//out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t onBlur='checkForm()' ");
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t onKeyPress='if ( (event.keyCode < 47) || (event.keyCode > 57 ) ) event.returnValue = false;' ");
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t size=15 value='" + this.sControlValor2 + "'>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t <img border=0 src=\"/portal/comun/lib/calendario2/img/arrowdate_blue.gif\" onClick=\"javascript:showCalendar('',document.forms[0]." + sControlNombre2 + ",null,'','Calendario',-80,16)\">");
			
			if(sObligatorio2.equals("true")){
			   out.println("\t\t\t\t\t\t\t\t\t\t\t\t<script>fAgregaCampo('" + sControlNombre2 + "', '" + sEtiqueta2 + "');</script>");
			}
			out.println("\t\t\t\t\t\t\t\t\t\t\t</td> ");
			
			if( this.sTerminarenglon2){
				out.println("\t\t\t\t\t\t\t\t\t\t</tr>");
			}
			
			out.println("		<script languaje=\"javascript\">");
			out.println("          //*******************************************************************");
			out.println("          //VALIDA EL FORMATO DE LA FECHA 1");
			out.println("          //*******************************************************************");
			out.println("          function ValidaFecha(objName) {");
			out.println("            checkdate(objName);");
			//VERIFICA SI LA FECHA NO PUEDE SER MAYOR A LA DEL SISTEMA
			if( this.sEsFechaMayorSis1){
				out.println("            fFechaControlMayor1();");
			}
			out.println("          }");
			
			//VERIFICA SI LA FECHA NO PUEDE SER MAYOR A LA DEL SISTEMA
			if( this.sEsFechaMayorSis1){
				//SE OBTIENE LA FECHA DEL SISTEMA
				Fecha2 fecha = new Fecha2();
				java.util.Date dFechaSistema = new Date(); 
				String sFechaSistema = fecha.formatoSimple(dFechaSistema);
				
				int iLongitudFechaSistema = sFechaSistema.length();
				//VERIFICA SI LA LONGITUD ES IGUAL A NUEVE
				if (iLongitudFechaSistema == 9){
					sFechaSistema = "0"+sFechaSistema;
				}
			
				out.println("          function fFechaControlMayor1() {");
				out.println("            var fechaSistema = '"+sFechaSistema+"';");
				out.println("            var fechaControl = "+sContenedor1+"."+sControlNombre1+".value;");
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
				out.println("            	"+sContenedor1+"."+sControlNombre1+".value='';");
				out.println("            	"+sContenedor1+"."+sControlNombre1+".focus();");
				out.println("            }");
				out.println("          }");
			}
			
			out.println("		</script>");
			
			out.println("		<script languaje=\"javascript\">");
			out.println("          //*******************************************************************");
			out.println("          //VALIDA EL FORMATO DE LA FECHA 2");
			out.println("          //*******************************************************************");
			out.println("          function ValidaFecha(objName) {");
			out.println("            checkdate(objName);");
			//VERIFICA SI LA FECHA NO PUEDE SER MAYOR A LA DEL SISTEMA
			if( this.sEsFechaMayorSis2){
				out.println("            fFechaControlMayor2();");
			}
			out.println("          }");
			
			//VERIFICA SI LA FECHA NO PUEDE SER MAYOR A LA DEL SISTEMA
			if( this.sEsFechaMayorSis2){
				//SE OBTIENE LA FECHA DEL SISTEMA
				Fecha2 fecha = new Fecha2();
				java.util.Date dFechaSistema = new Date(); 
				String sFechaSistema = fecha.formatoSimple(dFechaSistema);
				
				int iLongitudFechaSistema = sFechaSistema.length();
				//VERIFICA SI LA LONGITUD ES IGUAL A NUEVE
				if (iLongitudFechaSistema == 9){
					sFechaSistema = "0"+sFechaSistema;
				}
			
				out.println("          function fFechaControlMayor2() {");
				out.println("            var fechaSistema = '"+sFechaSistema+"';");
				out.println("            var fechaControl = "+sContenedor2+"."+sControlNombre2+".value;");
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
				out.println("            	"+sContenedor2+"."+sControlNombre2+".value='';");
				out.println("            	"+sContenedor2+"."+sControlNombre2+".focus();");
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