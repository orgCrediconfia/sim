/**
 * Sistema de administración de portales.
 *
 */

package com.rapidsist.portal.cliente;

import com.rapidsist.portal.configuracion.Permisos;
import com.rapidsist.portal.configuracion.Usuario;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Iterator;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.comun.util.Fecha2;
import com.rapidsist.sim.catalogos.datos.SimCatalogoRegionalDAO;
import com.rapidsist.sim.catalogos.datos.SimRegionalSucursalDAO;
import com.rapidsist.sim.usuarios.datos.UsuarioAsesorSucursalDAO;

/**
 * Construye los combos padre-hijo de modelo y marca.
 * <br><br>
 * Parámetros:
 * <ul>
 *	<li>
 * etiqueta.- Etiqueta que se coloca del lado izquierdo del control html.
 *  </li>
 *	<li>
 * control.- Tipo de control html construido por este tag.<br>
 * Posibles valores: Texto (Control INPUT), TextoArea (Control TEXTAREA), SelectorLogico (Control
 * SELECT con valores lógicos: Falso -F- y Verdadero -V-), SelectorAfirmativo (Control
 * SELECT con valores lógicos: Si -V- y No -F-), selector (muestra en un objeto html SELECT un
 * LinkedList), etiqueta (muestra el contenido de controlvalor como simple texto html),
 * etiqueta-campooculto (muestra el contenido de controlvalor como simple texto html y agrega
 * un control hidden utilizando los parametros controlnombre y controlvalor).
 *  </li>
 *	<li>
 * controlvalor.- Valor que se le asigna por default al control html.
 *  </li>
 *	<li>
 * campoclave.- Nombre del campo clave para el control selector.
 *  </li>
 *	<li>
 * campodescripcion.- Nombre del campo descripción para el control selector.
 *  </li>
 *	<li>
 * datosselector.- Lista de opciones para el control tipo selector.
 *  </li>
 * </ul>
 */
public class TagSelectorCadena extends TagSupport {

	String sEtiqueta1 ="";
	String sEtiqueta2 ="";
	String sEtiqueta3 ="";
	String sControl="";
	String sCveacto ="";
	String sControlValor="";
	String sCampoClave="";
	String sCampoDescripcion="";
	String sDatosSelector="";
	String sLongitudSelector="1";
	
    /**
	 * @param sEtiqueta1 Nombre de la primer etiqueta
	 */
	public void setEtiqueta1(String sEtiqueta1){
		this.sEtiqueta1 = sEtiqueta1;
	}
	
	/**
	 * @param sEtiqueta2 Nombre de la segunda etiqueta
	 */
	public void setEtiqueta2(String sEtiqueta2){
		this.sEtiqueta2 = sEtiqueta2;
	}
	
	/**
	 * @param sEtiqueta3 Nombre de la tercera etiqueta
	 */
	public void setEtiqueta3(String sEtiqueta3){
		this.sEtiqueta3 = sEtiqueta3;
	}

	/**
	 * @param sControl Nombre del control
	 */
	public void setControl(String sControl){
		this.sControl = sControl;
	}

	public void setCveacto(String sCveacto){
		this.sCveacto = sCveacto;
	}
	
	public void setControlvalor(String sControlValor){
		this.sControlValor = sControlValor;
	}

	
	
	public void setCampoclave(String sCampoClave){
		this.sCampoClave = sCampoClave;
	}

	public void setCampodescripcion(String sCampoDescripcion){
		this.sCampoDescripcion = sCampoDescripcion;
	}

	public void setDatosselector(String sDatosSelector){
		this.sDatosSelector = sDatosSelector;
	}

	/**
	 * Método estándar de la interfaz de TagLibs, que es disparado por el contenedor
	 * de servlets al comenzar la ejecución del TagLib.
	 * @return Mensaje al contenedor de servlets que indica como manejar el flujo
	 * de ejecución del TagLib y el contenido que pueda existir dentro del tag.
	 * @throws JspException Si hubo algún error durante la ejecución del TagLib.
	 */
	public int doStartTag() throws JspException {
		int iSalida = TagSupport.EVAL_BODY_INCLUDE;
		ListIterator listaSelector =  null;
		
		try{
			JspWriter out = pageContext.getOut();
			out.println();
			
			out.println("<script language='javascript' src=\'/portal/comun/lib/chainedselects.js'></script>");
			out.println("<script languaje=\"javascript\">");
			out.println("         function checkLists(list, order, instance, value) {");
			out.println("            if(list.name == \"Regional\"){");
			out.println("            	window.document.frmRegistro.IdRegional.value = value; ");
			out.println("            }else if(list.name == \"Sucursal\"){");
			out.println("            	window.document.frmRegistro.IdSucursal.value = value; ");
			out.println("            }else if(list.name == \"Asesor\"){");
			out.println("            	window.document.frmRegistro.CveUsuario.value = value; ");
			out.println("            }");
			out.println("         }");
			out.println("</script>");
			
			out.println("		<script>");
			out.println("		addListGroup(\"vehicles\", \"makers\");");
			out.println("		addOption(\"makers\", \"\", \"\");");
			
			SimCatalogoRegionalDAO regionalDAO = new SimCatalogoRegionalDAO();
			regionalDAO.abreConexion("java:comp/env/jdbc/PortalDs");
			
			Registro parametros = new Registro();
			
			parametros.addDefCampo("CVE_GPO_EMPRESA", "SIM");
			parametros.addDefCampo("CVE_EMPRESA", "CREDICONFIA");
			
			LinkedList listaRegional = regionalDAO.getRegistros(parametros);
			Iterator ilistaRegional = listaRegional.iterator();
			
			while (ilistaRegional.hasNext()){
				Registro registro = (Registro) ilistaRegional.next();
				String sClave = (String) registro.getDefCampo(sCampoClave);
				System.out.println("sClaveRegional"+sClave);
				out.println("		addList(\"makers\", \"" + (String) registro.getDefCampo(sCampoDescripcion) + "\", \"" + (String) registro.getDefCampo(sCampoDescripcion) + "\", \"" + (String) registro.getDefCampo(sCampoDescripcion) + "-list\");");
				
				parametros.addDefCampo("ID_REGIONAL", sClave);
				
				SimRegionalSucursalDAO sucursalDAO = new SimRegionalSucursalDAO();
				sucursalDAO.abreConexion("java:comp/env/jdbc/PortalDs");
				
				LinkedList listaSucursal = sucursalDAO.getRegistros(parametros);
				Iterator ilistaSucursal = listaSucursal.iterator();
				
				out.println("		addOption(\"" + (String) registro.getDefCampo(sCampoDescripcion) + "-list\", \"\", \"\");");
			
				while (ilistaSucursal.hasNext()){
					Registro registrohijo = new Registro();
					registrohijo = (Registro)ilistaSucursal.next();
					String sClaveSucursal = (String) registrohijo.getDefCampo("ID_SUCURSAL");
					System.out.println("sClaveSucursal"+sClaveSucursal);
					out.println("		addList(\"" + (String) registro.getDefCampo(sCampoDescripcion) + "-list\", \"" + (String)registrohijo.getDefCampo("NOM_SUCURSAL") + "\", \"" + (String)registrohijo.getDefCampo("NOM_SUCURSAL") + "\", \"" + (String)registrohijo.getDefCampo("NOM_SUCURSAL") + "-sucursal\");");
					
					parametros.addDefCampo("ID_SUCURSAL", sClaveSucursal);
					
					UsuarioAsesorSucursalDAO asesorDAO = new UsuarioAsesorSucursalDAO();
					asesorDAO.abreConexion("java:comp/env/jdbc/PortalDs");
					
					LinkedList listaAsesor = asesorDAO.getRegistros(parametros);
					Iterator ilistaAsesor = listaAsesor.iterator();
					
					out.println("		addOption(\"" + (String) registrohijo.getDefCampo("NOM_SUCURSAL") + "-sucursal\", \"\", \"\");");
					
					while (ilistaAsesor.hasNext()){
						Registro registronieto = new Registro();
						registronieto = (Registro)ilistaAsesor.next();
						String sClaveNieto = (String) registronieto.getDefCampo("ID_PERSONA");
						System.out.println("sClaveNieto"+sClaveNieto);
						out.println("		addList(\"" + (String) registronieto.getDefCampo("NOM_SUCURSAL") + "-sucursal\", \"" + (String)registronieto.getDefCampo("NOM_COMPLETO") + "\", \"" + (String)registronieto.getDefCampo("NOM_COMPLETO") + "\");");
					}
					
					if ( asesorDAO.isConectado()){
						asesorDAO.cierraConexion();
					}
					
				}
				
				
				if ( sucursalDAO.isConectado()){
					sucursalDAO.cierraConexion();
				}
			}
			
			if ( regionalDAO.isConectado()){
				regionalDAO.cierraConexion();
			}
			
			out.println("		</script>");
			
			out.println("\t\t\t\t\t\t\t\t\t\t<tr>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t<th>" + sEtiqueta1 + "</th>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t<td>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t<select name='IdRegional' style=\"width:150px\">");
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t</select>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t</td>");
			out.println("\t\t\t\t\t\t\t\t\t\t</tr>");
			
			if (!sControlValor.equals("")){
				out.println("\t\t\t\t\t\t\t\t\t\t<tr>");
				out.println("\t\t\t\t\t\t\t\t\t\t\t<th>" + sEtiqueta2 + "</th>");
				out.println("\t\t\t\t\t\t\t\t\t\t\t<td>");
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t<input type='text' name='Regional' value='" + sControlValor + "' >");	
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t<select name='IdSucursal' style=\"width:200px\">");
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t</select>");	
				out.println("\t\t\t\t\t\t\t\t\t\t\t</td>");
				out.println("\t\t\t\t\t\t\t\t\t\t</tr>");
				
			}else {
				out.println("\t\t\t\t\t\t\t\t\t\t<tr>");
				out.println("\t\t\t\t\t\t\t\t\t\t\t<th>" + sEtiqueta2 + "</th>");
				out.println("\t\t\t\t\t\t\t\t\t\t\t<td>");
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t<select name='IdSucursal' style=\"width:200px\">");
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t</select>");
				out.println("\t\t\t\t\t\t\t\t\t\t\t</td>");
				out.println("\t\t\t\t\t\t\t\t\t\t</tr>");
				
			}
			
			out.println("\t\t\t\t\t\t\t\t\t\t<tr>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t<th>" + sEtiqueta3 + "</th>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t<td>");
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t<select name='CveUsuario' style=\"width:285px\">");
			out.println("\t\t\t\t\t\t\t\t\t\t\t\t</select>");
			
			
			
			out.println("<script type=\"text/javascript\">");
			out.println("initListGroup('vehicles', window.document.forms[0].IdRegional, window.document.forms[0].IdSucursal, window.document.forms[0].CveUsuario, checkLists);");
			out.println("</script>");
			
		}
		catch(Exception e){
			e.printStackTrace();
		
		}
		
		return(iSalida);
		
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
			out.println("\t\t\t\t\t\t\t\t\t\t\t</td>");
			out.println("\t\t\t\t\t\t\t\t\t\t</tr>");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return TagSupport.EVAL_PAGE;
	}
}