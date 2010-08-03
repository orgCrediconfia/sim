/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.publicaciones.datos.PublicacionDAO;
import com.rapidsist.portal.configuracion.PortalSL;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.SortedMap;
import org.jdom.*;
import org.jdom.input.*;
import java.util.*;
import java.io.*;

/**
 * Esta clase se encarga de identar secciones de publicaciones
 */
public class ConstruyeSeccion2 {

	private	LinkedList listaSeccion = new LinkedList();
	private Registro registroSeccion = new Registro();
	private int iNivelSeccion = 0;
	private PublicacionDAO publicacionDAO = new PublicacionDAO();
	private String sNomAplicacionWeb = "";
	private String sHtml_expandible = "";

	private String sIdNivelAcceso = "0";
	private String sCvePortal = "";
	private String sCveGpoEmpresa = "";
	private String sCvePerfilPub = "";
	private String sUsuarioFirmado = "";

	/**
	 * Obtiene los elementos padre de la consulta e invoca al método que construye
	 * recursivamente el menú de opciones a partir de los elementos padre.
	 * @param conexion Se pasa la clase Conexion2 para raliza accesos a la base de datos
	 * @param parametros Conjunto de datos que son necesarios: sCveGpoEmpresa,  sCvePortal, sNomAplicacionWeb
	 * @return listaSeccion Lista con los elementos indentados y listos para poder mostrarse
	 */
	public String getSeccion(Conexion2 conexion, Registro parametros){
		boolean bConectaMetodo = false;
		//INICIALIZA EL SECCION
		iNivelSeccion = 0;
		PortalSL portal = null;
		//SE COLOCAN EN PROPIEDADES DE LA CLASE ESTOS VALORES
		this.sCveGpoEmpresa = (String)parametros.getDefCampo("CVE_GPO_EMPRESA");
		this.sCvePortal = (String)parametros.getDefCampo("CVE_PORTAL");
		this.sIdNivelAcceso = (String)parametros.getDefCampo("ID_NIVEL_ACCESO");
		this.sCvePerfilPub = (String)parametros.getDefCampo("CVE_PERFIL_PUB");
		this.sUsuarioFirmado = (String)parametros.getDefCampo("USUARIO_FIRMADO");
		try{
			publicacionDAO.setConexion(conexion.getConexion());
			//EL USUARIO_FIRMADO Y PUBLICA_CONTENIDOS SON DATOS QUE SE UTILIZAN PARA DETERMINAR QUE SECCIONES PUEDE VER Y QUE TENGAN PUBLICACIONES VIGENTES PARA PODER MOSTRAR
			boolean bUsuarioFirmado = parametros.getDefCampo("USUARIO_FIRMADO")!=null ? parametros.getDefCampo("USUARIO_FIRMADO").equals("SI") ? true : false : false;
			boolean bSoloPublicaciones = parametros.getDefCampo("PUBLICA_CONTENIDOS")!=null ? parametros.getDefCampo("PUBLICA_CONTENIDOS").equals("SI") ? true : false : false;
			//SE UTILIZA EL NIVEL DE ACCESO Y EL PERFIL DE PUBLICACION PARA DETERMINAR LAS SECCIONES QUE PUEDE VER EL USUARIO QUE SE FIRMA
			String sIdNivelAcceso = parametros.getDefCampo("ID_NIVEL_ACCESO")!=null ? (String)parametros.getDefCampo("ID_NIVEL_ACCESO") : "";
			String sCvePerfilPub = parametros.getDefCampo("CVE_PERFIL_PUB")!=null ? (String)parametros.getDefCampo("CVE_PERFIL_PUB") : "" ;
			//SE UTILIZA SOLO PARA DETERMINAR LA RUTA DE UNA SECCION EN PARTICULAR
			boolean bSoloRutaSeccion = parametros.getDefCampo("DETERMINA_RUTA_SECCION")!=null ? parametros.getDefCampo("DETERMINA_RUTA_SECCION").equals("SI") ? true : false : false;
			String sCveSeccion = (String)parametros.getDefCampo("CVE_SECCION");
			TreeMap listaOpciones = null;
			//OBTENEMOS LAS OPCIONES DEL SECCION PARA EL PRIMER NIVEL
			listaOpciones = publicacionDAO.getOpcionSeccion((String)parametros.getDefCampo("CVE_GPO_EMPRESA"), (String)parametros.getDefCampo("CVE_PORTAL"), (String)parametros.getDefCampo("CVE_USUARIO"), " is null ", bUsuarioFirmado, bSoloPublicaciones, sIdNivelAcceso, sCvePerfilPub, bSoloRutaSeccion, sCveSeccion);
			//VERIFICA SI ENCONTRO OPCIONES DEL SECCION
			if (listaOpciones != null){
				construyeSeccion(listaOpciones, (String)parametros.getDefCampo("CVE_GPO_EMPRESA"), (String)parametros.getDefCampo("CVE_PORTAL"), (String)parametros.getDefCampo("CVE_USUARIO"), iNivelSeccion, bUsuarioFirmado, bSoloPublicaciones, sIdNivelAcceso, sCvePerfilPub, bSoloRutaSeccion, sCveSeccion);
			}
			//FINALIZA LA CONSTRUCCION DEL SECCION
			publicacionDAO.cierraConexion();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//sHtml_expandible = "<ul>\n"+sHtml_expandible+"</ul><div id='FinMenu' style='position:absolute;'></div> \n";
		sHtml_expandible = "\n"+sHtml_expandible+"<div id='FinMenu' style='position:absolute;'></div> \n";
		return sHtml_expandible;
	}

	/**
	 * Obtiene de una lista todas las  sub-opciones que dependen, esto se
	 * realiza recursivamente
	 * @param listaOpciones Lista base sobre la que itera "n" veces hasta llegar al último elemento.
	 * @param sCveGpoEmpresa Clave del grupo de la empresa que se consulta
	 * @param sCvePortal Clave del portal que se consulta
	 * @param iNivelActual Nivel de profundidad en las sub-opciones
	 * @param bUsuarioFirmado "F" Muestra publicaciones públicas, "V" Muestra publicaciones privadas y públicas
	 * @param bSoloPublicaciones "V" Muestra las secciones en donde existen publicaciones vigentes que el usuario puede ver
	 * @param sIdNivelAcceso Indica el nivel de acceso del usuario
	 * @param sCvePerfilPub Clave del perfil de publicaciones del usuario
	 * @param bSoloRutaSeccion Indica que sólo se determine la ruta de una sección.
	 * @param sCveSeccion Parámetro que auxilia a bSoloRutaSeccion para poder
	 * determinar la ruta de una sección que se busca.
	 */
	private void construyeSeccion(TreeMap listaOpciones, String sCveGpoEmpresa, String sCvePortal, String sCveUsuario, int iNivelActual, boolean bUsuarioFirmado, boolean bSoloPublicaciones, String sIdNivelAcceso, String sCvePerfilPub, boolean bSoloRutaSeccion, String sCveSeccion){

		String sHref_1 = "";
		int iNuevoNivel = iNivelActual;
		//RECORRE LA LISTA DE OPCIONES ENCONTRADAS. PARA CADA OPCION SE BUSCARA SI TIENE A SU VEZ
		//OTRAS SUBOPCIONES ASIGNADAS
		SortedMap listaOrdenada = listaOpciones;
		Iterator lista = listaOrdenada.keySet().iterator();
		while (lista.hasNext()){
			String sLlaveOpcion = (String)lista.next();
			Registro opcion = (Registro)listaOpciones.get(sLlaveOpcion);
			//OBTIENE LAS SUBOPCIONES
			TreeMap listaSubOpciones = publicacionDAO.getOpcionSeccion(sCveGpoEmpresa, sCvePortal, sCveUsuario, " = '" + opcion.getDefCampo("CVE_SECCION") + "'", bUsuarioFirmado, bSoloPublicaciones, sIdNivelAcceso, sCvePerfilPub, bSoloRutaSeccion, sCveSeccion);
			//VERIFICA SI ENCONTRO SUBOPCIONES
			if ( listaSubOpciones != null ){
				//CREA UNA OPCION DE SECCION TIPO SUBSECCION
				iNivelSeccion ++;
				//SE PINTA EL SUBSECCION DE MANERA DISTINTA SI ESTA EN EL NIVEL SUPERIOR (0) O SI
				//FORMA PARTE DE ALGUN SECCION LATERAL

				//INICIALIZA EL REGISTRO
				registroSeccion = new Registro();
				//SE AGREGAN DATOS AL REGISTRO
				registroSeccion = this.AlmacenaDatos(opcion);
				//SE OBTIENE LA CADEBA DE LAS PUBLICACIONES A LA VARIABLE sPublicaciones
				String sPublicaciones = this.getPublicaciones(registroSeccion);
				//SI LA CADENA DE LA VARIABLE bPublicaciones VIENE EN BLANCO SE DICE QUE NO HAY PUBLICACIONES
				boolean bPublicaciones = sPublicaciones.equals("") ? false : true;
				if (opcion.getDefCampo("B_BLOQUEADO").equals("F")){
					//SE FORMA LA URL Y SE LE INDICA SI LA SECCIÓN TIENE PUBLICACIONES O NO
					sHref_1 = "<a href= \"javascript:\" onClick=\"AbreVentanaPublicaciones('/portal/MuestraPublicacion?CvePortal="+(String)opcion.getDefCampo("CVE_PORTAL")+"&CveSeccion="+(String)opcion.getDefCampo("CVE_SECCION")+"&MostrarPublicaciones=true','winDatosSolicitudPublicaciones','status=yes,scrollbars=yes,resizable=yes,width=700,height=400','"+bPublicaciones+"')\" title=\"" + opcion.getDefCampo("NOM_SECCION") + "\">"+opcion.getDefCampo("NOM_SECCION")+"</a> ";
					sHtml_expandible = sHtml_expandible + "<li id=\"foldheader\">"+sHref_1+/*"---"+opcion.getDefCampo("NOM_SECCION")+"</li> \n" +*/
					" <ul id=\"foldinglist\" style=\"display:none\" style=&{head};> \n";
					//sHtml_expandible = sHtml_expandible /* + sPublicaciones //ESTO PINTA LOS DOCUMENTOS QUE DEPENDEN DEL MENU*/;
					//SE VUELVE A LLAMAR A ESTA FUNCION (RECURSIVIDAD) PARA CONSTRUIR EL SUBSECCION
					construyeSeccion(listaSubOpciones, sCveGpoEmpresa, sCvePortal, sCveUsuario, iNivelSeccion, bUsuarioFirmado, bSoloPublicaciones, sIdNivelAcceso, sCvePerfilPub, bSoloRutaSeccion, sCveSeccion);
					sHtml_expandible = sHtml_expandible + "</ul> \n";
					//sHtml_expandible = sHtml_expandible + " \n";
				}
			}
			else{
				//INICIALIZA EL REGISTRO
				registroSeccion = new Registro();
				//SE AGREGAN DATOS AL REGISTRO
				registroSeccion = this.AlmacenaDatos(opcion);
				//SE OBTIENE LA CADEBA DE LAS PUBLICACIONES A LA VARIABLE sPublicaciones
				String sPublicaciones = this.getPublicaciones(registroSeccion);
				//SI LA CADENA DE LA VARIABLE bPublicaciones VIENE EN BLANCO SE DICE QUE NO HAY PUBLICACIONES
				boolean bPublicaciones = sPublicaciones.equals("") ? false : true;
				if (opcion.getDefCampo("B_BLOQUEADO").equals("F")){
					//SE FORMA LA URL Y SE LE INDICA SI LA SECCIÓN TIENE PUBLICACIONES O NO
					sHref_1 = "<a href= \"javascript:\" onClick=\"AbreVentanaPublicaciones('/portal/MuestraPublicacion?CvePortal="+(String)opcion.getDefCampo("CVE_PORTAL")+"&CveSeccion="+(String)opcion.getDefCampo("CVE_SECCION")+"&NomSeccion="+(String)opcion.getDefCampo("NOM_SECCION")+"&MostrarPublicaciones=true','winDatosSolicitudPublicaciones','status=yes,scrollbars=yes,resizable=yes,width=700,height=400','"+bPublicaciones+"')\" title=\"" + opcion.getDefCampo("NOM_SECCION") + "\">"+opcion.getDefCampo("NOM_SECCION")+"</a> ";
					sHtml_expandible = sHtml_expandible + "<li id=\"foldheader\">"+sHref_1+/*"---"+opcion.getDefCampo("NOM_SECCION")+"</li> \n" +*/
						"<ul id=\"foldinglist\" style=\"display:none\" style=&{head};> \n";
						//sHtml_expandible = sHtml_expandible /* + sPublicaciones //ESTO PINTA LOS DOCUMENTOS QUE DEPENDEN DEL MENU*/;
						sHtml_expandible = sHtml_expandible + "</ul> \n";
						//sHtml_expandible = sHtml_expandible + " \n";
				}
			}
		}
	}

	/**
	 * Este método obtiene las publicaciones de la clase PublicacionDAO
	 * y convierte en Cadena las publicaciones
	 * @param parametros Objeto Registro que se envía a la clase PublicacionDAO
	 * para realizar la consulta
	 * @return Registro con los valores del XML.
	 */
	public String getPublicaciones(Registro parametros) {
		String sPublicaciones = "";
		String sHref = "";
		try {
			parametros.addDefCampo("CVE_GPO_EMPRESA", this.sCveGpoEmpresa);
			parametros.addDefCampo("CVE_PORTAL", this.sCvePortal);
			parametros.addDefCampo("ID_NIVEL_ACCESO", this.sIdNivelAcceso);
			parametros.addDefCampo("CVE_PERFIL_PUB", this.sCvePerfilPub);
			parametros.addDefCampo("USUARIO_FIRMADO", this.sUsuarioFirmado);
			LinkedList listaPublicaciones = publicacionDAO.getPublicaciones(parametros);
			if (listaPublicaciones != null){
				Iterator ilistaPublicaciones = listaPublicaciones.iterator();
				Registro publicacion = new Registro();
				while (ilistaPublicaciones.hasNext()){
					publicacion = (Registro)ilistaPublicaciones.next();
					sHref = "<a href=/portal/MuestraPublicacion?CvePortal="+(String)publicacion.getDefCampo("CVE_PORTAL")+"&CveSeccion="+(String)publicacion.getDefCampo("CVE_SECCION")+"&IdPublicacion="+(String)publicacion.getDefCampo("ID_PUBLICACION")+"' \" title=\"" + publicacion.getDefCampo("DESC_PUBLICACION") + "\">"+(String)publicacion.getDefCampo("NOM_PUBLICACION")+"</a> ";
					sPublicaciones = sPublicaciones + "<li>" + sHref + "</li> \n";
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return sPublicaciones;
	}

	/**
	 * Este método se encarga de almacenar los valores del registro que
	 * vienen en formato xml, en un objeto registro sin importar el número
	 * de columnas que tenga el xml.
	 * @param opcion Objeto Registro del que se obtiene la cadena xml
	 * @return Registro con los valores del XML.
	 */
	public Registro AlmacenaDatos(Registro opcion) {
		Registro registro = new Registro();
		try {
			ByteArrayInputStream io = new ByteArrayInputStream(opcion.getXml().getBytes());
			InputStreamReader isr = new InputStreamReader(io, "iso-8859-1");
			SAXBuilder builder = new SAXBuilder();
			Document docCampos = builder.build(isr);
			// SE OBTIENE LA LISTA DE REGISTROS
			List listaNombresDeColumnas = docCampos.getRootElement().getChildren();
			Iterator ilistaNombresDeColumnas = listaNombresDeColumnas.iterator();
			while (ilistaNombresDeColumnas.hasNext()) {
				Element eValorCampo = (Element) ilistaNombresDeColumnas.next();
				registro.addDefCampo(eValorCampo.getName(), eValorCampo.getText());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
		}
		return registro;
	}
}
