/**
 * Sistema de administración de portales.
 *
 */

package com.rapidsist.portal.cliente;

import com.rapidsist.portal.configuracion.Usuario;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Iterator;
import com.rapidsist.comun.bd.Registro;


/**
 * Construye un elemento html dentro de una forma de captura.
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
 * controlnombre.- Nombre del control html, con el cual se podrá manipularlo a través de código
 * javascript.
 *  </li>
 *	<li>
 * controlvalor.- Valor que se le asigna por default al control html.
 *  </li>
 *	<li>
 * controllongitud.- Tamaño en la página html que se le asigna al control. Para el control INPUT
 * se le asigna al atributo SIZE, al control TEXTAREA se le asigna al atributo COLS.
 *  </li>
 *	<li>
 * controllongitudmax.- Maxima cantidad de caracteres que se pueden introducir dentro del control
 * html. Para el control INPUT se le asigna al atributo MAXLENGTH, al control TEXTAREA se le
 * asigna al atributo ROWS.
 *  </li>
 *	<li>
 * editarinicializado.- Verifica si el control html puede editarse si es inicializado con un
 * valor.<br>
 * Posibles valores: true (si el control puede editarse aun estando inicializado con algún valor),
 * false (no mostrará ningún control en la página, tan solo el valor del control como simple
 * texto).
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
public class TagFormaElemento extends TagSupport {

	String sEtiqueta ="";
	String sControl="";
	String sControlNombre="";
	String sControlValor="";
	String sControlLongitud="";
	String sControlLongitudMaxima="";
	String sControlTextAreaMaxLong="";
	String sEditarInicializado="true";
	String sObligatorio="false";
	String sCampoClave="";
	String sCampoDescripcion="";
	String sDatosSelector="";
	String sLongitudSelector="1";
	String sEvento = "";
	String sSoloMayusculas ="";
	String sFuncionSoloMayusculas ="";
	boolean bSoloMayusculas = false;
	String sValidaDato ="";   
	String sFuncionValidaDato =""; 
 
    /**
	 * @param sEtiqueta Nombre de la etiqueta
	 */
	public void setEtiqueta(String sEtiqueta){
		this.sEtiqueta = sEtiqueta;
	}

	/**
	 * @param sControl Nombre del control
	 */
	public void setControl(String sControl){
		this.sControl = sControl;
	}


	public void setControlnombre(String sControlNombre){
		this.sControlNombre = sControlNombre;
	}

	public void setControlvalor(String sControlValor){
		this.sControlValor = sControlValor;
	}

	public void setControllongitud(String sControlLongitud){
		this.sControlLongitud = sControlLongitud;
	}

	public void setControllongitudmax(String sControlLongitudMaxima){
		this.sControlLongitudMaxima = sControlLongitudMaxima;
	}

	public void setControltexarealongmax(String sControlTextAreaMaxLong){
		this.sControlTextAreaMaxLong = sControlTextAreaMaxLong;
	}


	public void setEditarinicializado(String sEditarInicializado){
		this.sEditarInicializado = sEditarInicializado;
	}

	public void setObligatorio(String sObligatorio){
		this.sObligatorio = sObligatorio;
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
	public void setLongitudselector(String sLongitudSelector){
		this.sLongitudSelector = sLongitudSelector;
	}
	public void setEvento(String sEvento){
		this.sEvento = sEvento;
	}
	
	public void setSolomayusculas(String sSoloMayusculas){
		this.sSoloMayusculas = sSoloMayusculas;
	}
	
	public void setValidadato(String sValidaDato){
		this.sValidaDato = sValidaDato;
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
		LinkedList listaSelectorLogico = new LinkedList();
		ListIterator listaSelector =  null;
		
		//REEMPLAZA LAS COMILLAS POR EL VALOR DE &quot; PARA QUE PUEDA SER LEIDO POR LA JSP
		//sControlValor = sControlValor.replaceAll("\"","&quot;");
		
		try{
		
			// RECUPERA EL VALOR DE  EL TIPO DE LETRA DE EL OBJETO USUARIO
			if(pageContext.getSession().getAttribute("Usuario") != null){
				Usuario usuario = (Usuario)pageContext.getSession().getAttribute("Usuario");
				bSoloMayusculas = usuario.bSoloMayusculas;
			}
						
			// VERIFICA SI LA FUNCION onkeyup SE AÑADIRÁ AL ELEMENTO			
			if(bSoloMayusculas && ( sSoloMayusculas.equals("") ||  sSoloMayusculas.equals("true"))){
				 sFuncionSoloMayusculas = " onchange=\"fOnKeyUp();\" ";
			}
			else{
				 if(sSoloMayusculas.equals("true")){
					sFuncionSoloMayusculas = " onchange=\"fOnKeyUp();\" ";
				}
			}
			
			// VERIFICA SI LA FUNCION onkeydown SE AÑADIRÁ AL ELEMENTO PARA VALIDAR LOS DATOS QUE SE INGRESAN (LUC)			
			if(sValidaDato.equals("cantidades")){
					sFuncionValidaDato = " onkeydown=\"CantidadesMonetarias();\" ";
			}
			if(sValidaDato.equals("numerico")){
				sFuncionValidaDato = " onkeydown=\"SoloNumeros();\" ";
			}
			if(sValidaDato.equals("alfabetico")){
					sFuncionValidaDato = " onkeydown=\"SoloLetras();\" ";
			}
			if(sValidaDato.equals("alfabeticonumerico")){
					sFuncionValidaDato = " onkeydown=\"SoloNumerosLetras();\" ";
			}
			
			//ACTUALIZA EL CONTADOR DE RENGLONES DENTRO DE LA FORMA
			int iNumRenglonesForma= ((Integer)pageContext.getAttribute("NumRenglonesForma")).intValue();

			String sOperacionCatalogo = pageContext.getRequest().getParameter("OperacionCatalogo");
			pageContext.setAttribute("NumRenglonesForma", new Integer(iNumRenglonesForma++));

			JspWriter out = pageContext.getOut();
			out.println();

			if (!sDatosSelector.equals("")){
				listaSelector = (ListIterator) ExpressionUtil.evalNotNull("out", "controllista", sDatosSelector, Object.class, this, pageContext);
			}

			if (!sControlValor.equals("")){
				String sParamControlValor = (String) ExpressionUtil.evalNotNull("out", "controlvalor", sControlValor + "x", Object.class, this, pageContext);
				sControlValor = sParamControlValor.substring(0, sParamControlValor.length() - 1);
			}

			if (!sEvento.equals("")){
				String sParamEvento = (String) ExpressionUtil.evalNotNull("out", "evento", sEvento + "x", Object.class, this, pageContext);
				sEvento = sParamEvento.substring(0, sParamEvento.length() - 1);
			}

			if(sControl.equals("etiqueta-controlreferencia")) {
				
				out.println("\t\t\t\t\t\t\t\t\t\t<tr>");
				out.println("\t\t\t\t\t\t\t\t\t\t\t<th>" + sEtiqueta + "</th>");
				out.println("\t\t\t\t\t\t\t\t\t\t\t<td id='" + sControlNombre + "'>" + sControlValor);
			}
			else{
				if(!sControl.equals("variableoculta")) {
					// SI SE TRATA DE UN CONTROL HORIZONTAL NO PONE EL SALTO DE LÍNEA
					if (!sControl.equals("selector-horizontal") && 
						!sControl.equals("Texto-horizontal") && 
					    !sControl.equals("etiqueta-horizontal") && 
						!sControl.equals("etiqueta-horizontal-titulo")&& 
						!sControl.equals("Texto-horizontal-titulo")&& 
						!sControl.equals("variableoculta-horizontal")&& 
						!sControl.equals("selector-horizontal-titulo")){
						out.println("\t\t\t\t\t\t\t\t\t\t<tr>");
						out.println("\t\t\t\t\t\t\t\t\t\t\t<th>" + sEtiqueta + "</th>");
						out.println("\t\t\t\t\t\t\t\t\t\t\t<td>");
					}
					else if(sControl.equals("etiqueta-horizontal-titulo")||sControl.equals("Texto-horizontal-titulo")||sControl.equals("selector-horizontal-titulo")){
						out.println("\t\t\t\t\t\t\t\t\t\t\t<th>" + sEtiqueta + "</th>");
						out.println("\t\t\t\t\t\t\t\t\t\t\t<td>");
					}
					else if(!sControl.equals("variableoculta-horizontal")){
						out.println("\t\t\t\t\t\t\t\t\t\t\t<td>");
					}
				}
			}

			if(sControl.equals("etiqueta") || sControl.equals("etiqueta-horizontal") || sControl.equals("etiqueta-horizontal-titulo") ) {
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t" + sControlValor);
			}
			else if(sControl.equals("etiqueta-controloculto")) {
				out.print("\t\t\t\t\t\t\t\t\t\t\t\t" + sControlValor);
				out.println("<input type='hidden' name='" + sControlNombre + "' value=\"" + sControlValor + "\" >" );
			}
			else if(sControl.equals("variableoculta")) {
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t<input type='hidden' name='" + sControlNombre + "' value=\"" + sControlValor + "\">" );
				if (sObligatorio.equals("true")){
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t<script>fAgregaCampo('" + sControlNombre + "', '" + sEtiqueta + "');</script>");
				}
			}
			else if(sControl.equals("variableoculta-horizontal")) {
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t<input type='hidden' name='" + sControlNombre + "' value=\"" + sControlValor + "\">" );
			}
			else if(sControl.equals("TextoArea")) {
				if(!sControlTextAreaMaxLong.equals("")){
				    sControlTextAreaMaxLong = " onpropertychange=\"limitChars("+ sControlTextAreaMaxLong +")\" " ;
				}
					out.print("\t\t\t\t\t\t\t\t\t\t\t\t<textarea name='" + sControlNombre + "' cols='" + sControlLongitud + "' rows='" + sControlLongitudMaxima + "'  " + sControlTextAreaMaxLong +"  " + sFuncionSoloMayusculas + sFuncionValidaDato + " >" + sControlValor + "</textarea>");//LUC
					if (sObligatorio.equals("true")){

						out.println("\t\t\t\t\t\t\t\t\t\t\t\t<script>fAgregaCampo('" + sControlNombre + "', '" + sEtiqueta + "');</script>");
					}
			}
			
			else if(sControl.equals("SelectorLogico")) {
			
				if (sObligatorio.equals("false")){
					Registro registroSelectorLogico0 = new Registro();
					registroSelectorLogico0.addDefCampo("Clave", "");
					registroSelectorLogico0.addDefCampo("Nombre","");
					listaSelectorLogico.add(registroSelectorLogico0);
				}
				
				Registro registroSelectorLogico1 = new Registro();
				registroSelectorLogico1.addDefCampo("Clave", "F");
				registroSelectorLogico1.addDefCampo("Nombre","Falso");
				listaSelectorLogico.add(registroSelectorLogico1);
				Registro registroSelectorLogico2 = new Registro();
				registroSelectorLogico2.addDefCampo("Clave", "V");
				registroSelectorLogico2.addDefCampo("Nombre","Verdadero");
				listaSelectorLogico.add(registroSelectorLogico2);

				String sSelected="";
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t<select name='" + sControlNombre + "' class='CapturaTexto'>");
				Iterator lista = listaSelectorLogico.iterator();
				while (lista.hasNext()){
					Registro registro = (Registro)lista.next();
					String sClave = (String)registro.getDefCampo("Clave");
					if (sClave.equals(sControlValor)){
						sSelected = " selected ";
					}
					else{
						sSelected = " ";
					}
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t<option value='" + sClave + "'" + sSelected  + ">" + (String)registro.getDefCampo("Nombre") + "</option>");
				}
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t</select>");
			}

			else if(sControl.equals("SelectorAfirmativo")) {
					Registro registroSelectorLogico1 = new Registro();
					registroSelectorLogico1.addDefCampo("Clave", "F");
					registroSelectorLogico1.addDefCampo("Nombre","No");
					listaSelectorLogico.add(registroSelectorLogico1);
					Registro registroSelectorLogico2 = new Registro();
					registroSelectorLogico2.addDefCampo("Clave", "V");
					registroSelectorLogico2.addDefCampo("Nombre","Si");
					listaSelectorLogico.add(registroSelectorLogico2);

					String sSelected="";
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t<select name='" + sControlNombre + "' class='CapturaTexto'>");
					Iterator lista = listaSelectorLogico.iterator();
					while (lista.hasNext()){
						Registro registro = (Registro)lista.next();
						String sClave = (String)registro.getDefCampo("Clave");
						if (sClave.equals(sControlValor)){
							sSelected = " selected ";
						}
						else{
							sSelected = " ";
						}
						out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t<option value='" + sClave + "'" + sSelected  + ">" + (String)registro.getDefCampo("Nombre") + "</option>");
					}
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t</select>");
			}




			
			else if(sControl.equals("SelectorActivo")) {
					Registro registroSelectorLogico1 = new Registro();
					registroSelectorLogico1.addDefCampo("Clave", "AC");
					registroSelectorLogico1.addDefCampo("Nombre","Activo");
					listaSelectorLogico.add(registroSelectorLogico1);
					Registro registroSelectorLogico2 = new Registro();
					registroSelectorLogico2.addDefCampo("Clave", "IN");
					registroSelectorLogico2.addDefCampo("Nombre","Inactivo");
					listaSelectorLogico.add(registroSelectorLogico2);

					String sSelected="";
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t<select name='" + sControlNombre + "' class='CapturaTexto'>");
					Iterator lista = listaSelectorLogico.iterator();
					while (lista.hasNext()){
						Registro registro = (Registro)lista.next();
						String sClave = (String)registro.getDefCampo("Clave");
						if (sClave.equals(sControlValor)){
							sSelected = " selected ";
						}
						else{
							sSelected = " ";
						}
						out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t<option value='" + sClave + "'" + sSelected  + ">" + (String)registro.getDefCampo("Nombre") + "</option>");
					}
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t</select>");
			}
            
			else if(sControl.equals("SelectorTipoOO")) {
				Registro registroSelectorLogico1 = new Registro();
				registroSelectorLogico1.addDefCampo("Clave", "PE");
				registroSelectorLogico1.addDefCampo("Nombre","Permanente");
				listaSelectorLogico.add(registroSelectorLogico1);
				Registro registroSelectorLogico2 = new Registro();
				registroSelectorLogico2.addDefCampo("Clave", "TE");
				registroSelectorLogico2.addDefCampo("Nombre","Temporal");
				listaSelectorLogico.add(registroSelectorLogico2);
				Registro registroSelectorLogico3 = new Registro();
				registroSelectorLogico3.addDefCampo("Clave", "OT");
				registroSelectorLogico3.addDefCampo("Nombre","Otras");
				listaSelectorLogico.add(registroSelectorLogico3);

				String sSelected="";
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t<select name='" + sControlNombre + "' class='CapturaTexto'>");
				Iterator lista = listaSelectorLogico.iterator();
				while (lista.hasNext()){
					Registro registro = (Registro)lista.next();
					String sClave = (String)registro.getDefCampo("Clave");
					if (sClave.equals(sControlValor)){
						sSelected = " selected ";
					}
					else{
						sSelected = " ";
					}
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t<option value='" + sClave + "'" + sSelected  + ">" + (String)registro.getDefCampo("Nombre") + "</option>");
				}
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t</select>");
		}

			
			else if(sControl.equals("SelectorAfecta")) {
				Registro registroSelectorLogico1 = new Registro();
				registroSelectorLogico1.addDefCampo("Clave", "D");
				registroSelectorLogico1.addDefCampo("Nombre","Decrementa");
				listaSelectorLogico.add(registroSelectorLogico1);
				Registro registroSelectorLogico2 = new Registro();
				registroSelectorLogico2.addDefCampo("Clave", "I");
				registroSelectorLogico2.addDefCampo("Nombre","Incrementa");
				listaSelectorLogico.add(registroSelectorLogico2);
				Registro registroSelectorLogico3 = new Registro();
				registroSelectorLogico3.addDefCampo("Clave", "N");
				registroSelectorLogico3.addDefCampo("Nombre","No Afecta");
				listaSelectorLogico.add(registroSelectorLogico3);

				String sSelected="";
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t<select name='" + sControlNombre + "' class='CapturaTexto'>");
				Iterator lista = listaSelectorLogico.iterator();
				while (lista.hasNext()){
					Registro registro = (Registro)lista.next();
					String sClave = (String)registro.getDefCampo("Clave");
					if (sClave.equals(sControlValor)){
						sSelected = " selected ";
					}
					else{
						sSelected = " ";
					}
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t<option value='" + sClave + "'" + sSelected  + ">" + (String)registro.getDefCampo("Nombre") + "</option>");
				}
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t</select>");
		}

			
			else if(sControl.equals("SelectorGenero")) {
				Registro registroSelectorLogico1 = new Registro();
				registroSelectorLogico1.addDefCampo("Clave", "M");
				registroSelectorLogico1.addDefCampo("Nombre","Masculino");
				listaSelectorLogico.add(registroSelectorLogico1);
				Registro registroSelectorLogico2 = new Registro();
				registroSelectorLogico2.addDefCampo("Clave", "F");
				registroSelectorLogico2.addDefCampo("Nombre","Femenino");
				listaSelectorLogico.add(registroSelectorLogico2);

				String sSelected="";
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t<select name='" + sControlNombre + "' class='CapturaTexto'>");
				Iterator lista = listaSelectorLogico.iterator();
				while (lista.hasNext()){
					Registro registro = (Registro)lista.next();
					String sClave = (String)registro.getDefCampo("Clave");
					if (sClave.equals(sControlValor)){
						sSelected = " selected ";
					}
					else{
						sSelected = " ";
					}
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t<option value='" + sClave + "'" + sSelected  + ">" + (String)registro.getDefCampo("Nombre") + "</option>");
				}
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t</select>");
			}

			else if(sControl.equals("SelectorNacionalidad")) {
				Registro registroSelectorLogico1 = new Registro();
				registroSelectorLogico1.addDefCampo("Clave", "Mex");
				registroSelectorLogico1.addDefCampo("Nombre","Mexicana");
				listaSelectorLogico.add(registroSelectorLogico1);
				Registro registroSelectorLogico2 = new Registro();
				registroSelectorLogico2.addDefCampo("Clave", "Ext");
				registroSelectorLogico2.addDefCampo("Nombre","Extranjera");
				listaSelectorLogico.add(registroSelectorLogico2);

				String sSelected="";
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t<select name='" + sControlNombre + "' class='CapturaTexto'>");
				Iterator lista = listaSelectorLogico.iterator();
				while (lista.hasNext()){
					Registro registro = (Registro)lista.next();
					String sClave = (String)registro.getDefCampo("Clave");
					if (sClave.equals(sControlValor)){
						sSelected = " selected ";
					}
					else{
						sSelected = " ";
					}
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t<option value='" + sClave + "'" + sSelected  + ">" + (String)registro.getDefCampo("Nombre") + "</option>");
				}
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t</select>");
			}

			else if(sControl.equals("SelectorEstadoCivil")) {
				Registro registroSelectorLogico1 = new Registro();
				registroSelectorLogico1.addDefCampo("Clave", "Casado");
				registroSelectorLogico1.addDefCampo("Nombre","Casado");
				listaSelectorLogico.add(registroSelectorLogico1);
				Registro registroSelectorLogico2 = new Registro();
				registroSelectorLogico2.addDefCampo("Clave", "Soltero");
				registroSelectorLogico2.addDefCampo("Nombre","Soltero");
				listaSelectorLogico.add(registroSelectorLogico2);
				Registro registroSelectorLogico3 = new Registro();
				registroSelectorLogico3.addDefCampo("Clave", "Divorciado");
				registroSelectorLogico3.addDefCampo("Nombre","Divorciado");
				listaSelectorLogico.add(registroSelectorLogico3);
				Registro registroSelectorLogico4 = new Registro();
				registroSelectorLogico4.addDefCampo("Clave", "Viudo");
				registroSelectorLogico4.addDefCampo("Nombre","Viudo");
				listaSelectorLogico.add(registroSelectorLogico4);
				Registro registroSelectorLogico5 = new Registro();
				registroSelectorLogico5.addDefCampo("Clave", "Union Libre");
				registroSelectorLogico5.addDefCampo("Nombre","Union Libre");
				listaSelectorLogico.add(registroSelectorLogico5);

				String sSelected="";
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t<select name='" + sControlNombre + "' class='CapturaTexto'>");
				Iterator lista = listaSelectorLogico.iterator();
				while (lista.hasNext()){
					Registro registro = (Registro)lista.next();
					String sClave = (String)registro.getDefCampo("Clave");
					if (sClave.equals(sControlValor)){
						sSelected = " selected ";
					}
					else{
						sSelected = " ";
					}
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t<option value='" + sClave + "'" + sSelected  + ">" + (String)registro.getDefCampo("Nombre") + "</option>");
				}
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t</select>");
			}
			
			else if(sControl.equals("SelectorGrupoSanguineo")) {
				Registro registroSelectorLogico1 = new Registro();
				registroSelectorLogico1.addDefCampo("Clave", "A+");
				registroSelectorLogico1.addDefCampo("Nombre","A RH Positivo");
				listaSelectorLogico.add(registroSelectorLogico1);
				
				Registro registroSelectorLogico2 = new Registro();
				registroSelectorLogico2.addDefCampo("Clave", "B+");
				registroSelectorLogico2.addDefCampo("Nombre","B RH Positivo");
				listaSelectorLogico.add(registroSelectorLogico2);
				
				Registro registroSelectorLogico3 = new Registro();
				registroSelectorLogico3.addDefCampo("Clave", "AB+");
				registroSelectorLogico3.addDefCampo("Nombre","AB RH Positivo");
				listaSelectorLogico.add(registroSelectorLogico3);
				
				Registro registroSelectorLogico4 = new Registro();
				registroSelectorLogico4.addDefCampo("Clave", "O+");
				registroSelectorLogico4.addDefCampo("Nombre","O RH Positivo");
				listaSelectorLogico.add(registroSelectorLogico4);
				
				Registro registroSelectorLogico5 = new Registro();
				registroSelectorLogico5.addDefCampo("Clave", "A-");
				registroSelectorLogico5.addDefCampo("Nombre","A RH Negativo");
				listaSelectorLogico.add(registroSelectorLogico5);
				
				Registro registroSelectorLogico6 = new Registro();
				registroSelectorLogico6.addDefCampo("Clave", "B-");
				registroSelectorLogico6.addDefCampo("Nombre","B RH Negativo");
				listaSelectorLogico.add(registroSelectorLogico6);

				Registro registroSelectorLogico7 = new Registro();
				registroSelectorLogico7.addDefCampo("Clave", "AB-");
				registroSelectorLogico7.addDefCampo("Nombre","AB RH Negativo");
				listaSelectorLogico.add(registroSelectorLogico7);

				Registro registroSelectorLogico8 = new Registro();
				registroSelectorLogico8.addDefCampo("Clave", "O-");
				registroSelectorLogico8.addDefCampo("Nombre","O RH Negativo");
				listaSelectorLogico.add(registroSelectorLogico8);
				
				String sSelected="";
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t<select name='" + sControlNombre + "' class='CapturaTexto'>");
				Iterator lista = listaSelectorLogico.iterator();
				while (lista.hasNext()){
					Registro registro = (Registro)lista.next();
					String sClave = (String)registro.getDefCampo("Clave");
					if (sClave.equals(sControlValor)){
						sSelected = " selected ";
					}
					else{
						sSelected = " ";
					}
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t<option value='" + sClave + "'" + sSelected  + ">" + (String)registro.getDefCampo("Nombre") + "</option>");
				}
				out.println("\t\t\t\t\t\t\t\t\t\t\t\t</select>");
			}
			
			//CONTROL SELECTOR
			//else if (this.sEditarInicializado.equals("false") && !sControlValor.equals("") && sControl.equals("selector")){
			else if (sControl.equals("selector") || sControl.equals("selector-horizontal") || sControl.equals("selector-horizontal-titulo")){
				//VERIFICA SI NO SE DEBE EDITAR EL CONTROL CUANDO ESTA INICIALIZADO
				if (this.sEditarInicializado.equals("false") && !sControlValor.equals("")){ 
					while (listaSelector.hasNext()) {
						Registro registro = (Registro) listaSelector.next();
						String sClave = (String) registro.getDefCampo(sCampoClave);
						if (sClave.equals(sControlValor)) {
							out.print("\t\t\t\t\t\t\t\t\t\t\t\t" + (String) registro.getDefCampo(sCampoDescripcion));
							out.println("<input type='hidden' name='" + sControlNombre + "' value=\"" + sControlValor + "\"/>");
						}
					}
					// REGRESA EL LISTITERATOR A LA POSICIÓN INICIAL PARA PODER SER REUTILIZADO
					while (listaSelector.hasPrevious()){
						listaSelector.previous();
					}					
				}
				else{
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t<select name='" + sControlNombre + "' size='"+ sLongitudSelector +"' " + sEvento + " >");
					//SI EL SELECTOR NO ES OBLIGATORIO ENTONCES AGREGA UNA OPCION EN NULO
					if (this.sObligatorio.equals("false")) {
						out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t<option value='null'></option>");
					}
					String sSelected = ""; 					
					while (listaSelector.hasNext()) {
						Registro registro = (Registro) listaSelector.next();
						String sClave = (String) registro.getDefCampo(sCampoClave);
						if (sClave.equals(sControlValor)) {
							sSelected = " selected ";
						}
						else {
							sSelected = " ";
						}
						out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t<option value='" + sClave + "' " + sSelected + " >" + (String) registro.getDefCampo(sCampoDescripcion) + "</option>");
					}
					// REGRESA EL LISTITERATOR A LA POSICIÓN INICIAL PARA PODER SER REUTILIZADO
					while (listaSelector.hasPrevious()){
						listaSelector.previous();
					}
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t</select>");
				}
			}
			//CONTROL TEXTO
			else if (sControl.equals("Texto") || sControl.equals("Texto-horizontal") || sControl.equals("Texto-horizontal-titulo")){
				//VERIFICA SI NO SE DEBE EDITAR EL CONTROL
				if(this.sEditarInicializado.equals("false") && !sControlValor.equals("") && !sOperacionCatalogo.equals("AL") && !sOperacionCatalogo.equals("IN")){
					out.print("\t\t\t\t\t\t\t\t\t\t\t\t" + sControlValor);
					out.println("<input type='hidden' name='" + sControlNombre + "' value=\"" + sControlValor + "\" />");
				}
				else{
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t<input type='text' name='" + sControlNombre + "' size='" + sControlLongitud + "' maxlength='" + sControlLongitudMaxima + "' value=\"" + sControlValor + "\" " + sEvento + "  " + sFuncionSoloMayusculas + sFuncionValidaDato +" >");//LUC
					if (sObligatorio.equals("true")) {
						out.println("\t\t\t\t\t\t\t\t\t\t\t\t<script>fAgregaCampo('" + sControlNombre + "', '" + sEtiqueta + "');</script>");
					}
				}
			}
			
			else if (sControl.equals("File")) {
				//VERIFICA SI NO SE DEBE EDITAR EL CONTROL
				if(this.sEditarInicializado.equals("false") && !sControlValor.equals("") && !sOperacionCatalogo.equals("AL") && !sOperacionCatalogo.equals("IN")){
					out.print("\t\t\t\t\t\t\t\t\t\t\t\t" + sControlValor);
					out.println("<input type='hidden' name='" + sControlNombre + "' value=\"" + sControlValor + "\"/>");
				}
				else{
					out.println("\t\t\t\t\t\t\t\t\t\t\t\t<input type=FILE name='" + sControlNombre + "' size='" + sControlLongitud + "' maxlength='" + sControlLongitudMaxima + "' value='" + sControlValor + "' " + sEvento + " >");
					if (sObligatorio.equals("true")) {
						out.println("\t\t\t\t\t\t\t\t\t\t\t\t<script>fAgregaCampo('" + sControlNombre + "', '" + sEtiqueta + "');</script>");
					}
				}
			}
			else if (sControl.equals("checkbox")){
				out.print("\t\t\t\t\t\t\t\t\t<input type='checkbox' name='" + sControlNombre + "'" + (sControlValor.equals("V") ? " checked " : "" )+ " />");
			}
			
			sFuncionSoloMayusculas = "";
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
			// SI SE TRATA DE UN CONTROL HORIZONTAL NO PONE EL SALTO DE LÍNEA
			if (!sControl.equals("selector-horizontal") && 
			    !sControl.equals("Texto-horizontal")&&
			    !sControl.equals("Texto-horizontal-titulo")&&
			    !sControl.equals("etiqueta-horizontal")&& 
				!sControl.equals("etiqueta-horizontal-titulo")&& 
				!sControl.equals("variableoculta-horizontal")&& 
				!sControl.equals("selector-horizontal-titulo")){
				out.println("\t\t\t\t\t\t\t\t\t\t</tr>");
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return TagSupport.EVAL_PAGE;
	}

}