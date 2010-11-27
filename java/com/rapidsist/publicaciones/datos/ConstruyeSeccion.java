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
public class ConstruyeSeccion {

	private	LinkedList listaSeccion = new LinkedList();
	private Registro registroSeccion = new Registro();
	private int iNivelSeccion = 0;
	private PublicacionDAO publicacionDAO = new PublicacionDAO();
	private String sNomAplicacionWeb = "";

	/**
	 * Obtiene los elementos padre de la consulta e invoca al método que construye
	 * recursivamente el menú de opciones a partir de los elementos padre.
	 * @param conexion Se pasa la clase Conexion2 para raliza accesos a la base de datos
	 * @param parametros Conjunto de datos que son necesarios: sCveGpoEmpresa,  sCvePortal, sNomAplicacionWeb
	 * @return listaSeccion Lista con los elementos indentados y listos para poder mostrarse
	 */
	public LinkedList getSeccion(Conexion2 conexion, Registro parametros){
		boolean bConectaMetodo = false;
		//INICIALIZA EL SECCION
		iNivelSeccion = 0;
		PortalSL portal = null;
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
		return listaSeccion;
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
		int iNuevoNivel = iNivelActual;
		String sUrl = "";
		String sTabulador = "&nbsp;&nbsp;&nbsp;";
		String sSeccionIdentada = "";
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
				sSeccionIdentada = "";
				for (int i=1; i <= iNivelActual; i++){
					sSeccionIdentada += sTabulador;
				}
				//INICIALIZA EL REGISTRO
				registroSeccion = new Registro();
				//SE AGREGAN DATOS AL REGISTRO
				registroSeccion = this.AlmacenaDatos(opcion, sSeccionIdentada);
				//SE AGREGA EL REGISTRO A LA LISTA
				listaSeccion.add(registroSeccion);
				//SE VUELVE A LLAMAR A ESTA FUNCION (RECURSIVIDAD) PARA CONSTRUIR EL SUBSECCION
				construyeSeccion(listaSubOpciones, sCveGpoEmpresa, sCvePortal, sCveUsuario, iNivelSeccion, bUsuarioFirmado, bSoloPublicaciones, sIdNivelAcceso, sCvePerfilPub, bSoloRutaSeccion, sCveSeccion);
			}
			else{

				//CREA UNA OPCION SIMPLE TIPO LINK
				if (opcion.getDefCampo("URL_FUNCION") != null){
					sUrl = sNomAplicacionWeb + (String)opcion.getDefCampo("URL_FUNCION");
				}
				else{
					sUrl = "#";
				}
				sSeccionIdentada = "";
				for (int i=1; i <= iNivelActual; i++){
					sSeccionIdentada += sTabulador;
				}
				//INICIALIZA EL REGISTRO
				registroSeccion = new Registro();
				//SE AGREGAN DATOS AL REGISTRO
				registroSeccion = this.AlmacenaDatos(opcion, sSeccionIdentada);
				//SE AGREGA EL REGISTRO A LA LISTA
				listaSeccion.add(registroSeccion);
			}
		}
	}

	/**
	 * Este método se encarga de almacenar los valores del registro que
	 * vienen en formato xml, en un objeto registro sin importar el número
	 * de columnas que tenga el xml.
	 * @param opcion Objeto Registro del que se obtiene la cadena xml
	 * @param sSeccionIdentada Cadena con los tabuladores correspondientes a cada sección
	 * @return Registro con los valores del XML.
	 */
	public Registro AlmacenaDatos(Registro opcion, String sSeccionIdentada) {
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
				if (eValorCampo.getName().equals("NOM_SECCION")) {
					registro.addDefCampo(eValorCampo.getName(), sSeccionIdentada + eValorCampo.getText());
					registro.addDefCampo("NOM_SECCION_SIN_IDENTAR", eValorCampo.getText());
				}
				else {
					registro.addDefCampo(eValorCampo.getName(), eValorCampo.getText());
				}
				registro.addDefCampo("IDENTACION", sSeccionIdentada);
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