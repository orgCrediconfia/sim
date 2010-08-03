/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.util.*;

/**
 * Administra los accesos a la base de datos para el EJB de Publicaciones.
 */
public class PublicacionDAO	extends Conexion2 {

	/**
	 * Obtiene un conjunto de registros en base a un filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException {
		sSql = ( (parametros.getDefCampo("CONDICION") != null) ? (String) parametros.getDefCampo("CONDICION") : "");
		ejecutaSql();
		return this.getConsultaLista();
	}

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException {
		sSql = "SELECT * FROM RS_CONF_APLICACION " +
			"WHERE  CVE_APLICACION='" + (String) parametros.getDefCampo("CVE_APLICACION") + "'";
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException {
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		sSql = "INSERT INTO RS_CONF_APLICACION( \n" +
			"CVE_APLICACION, \n" +
			"NOM_APLICACION, \n" +
			"TX_DESC_APLICACION, \n" +
			"URL_APLICACION, \n" +
			"B_BLOQUEADO) \n" +
			"VALUES ( \n" +
			"'" + (String) registro.getDefCampo("CVE_APLICACION") + "',\n" +
			"'" + (String) registro.getDefCampo("NOM_APLICACION") + "',\n" +
			"'" + (String) registro.getDefCampo("TX_DESC_APLICACION") + "',\n" +
			"'" + (String) registro.getDefCampo("URL_APLICACION") + "',\n" +
			"'" + (String) registro.getDefCampo("B_BLOQUEADO") + "')\n";
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0) {
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}

	/**
	 * Incrementa el contador en uno de la publicación solicitada.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public boolean IncrementaContadorPublicacion(Registro registro) throws SQLException {
		boolean bExito = false;
		//CONSULTA LA PUBLICACION SOLICITADA
		sSql = "SELECT * FROM RS_PUB_PUBLICACION " +
			"WHERE  CVE_GPO_EMPRESA ='" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_PORTAL ='" + (String) registro.getDefCampo("CVE_PORTAL") + "' \n"+
			" AND CVE_SECCION ='" + (String) registro.getDefCampo("CVE_SECCION") + "' \n"+
			" AND ID_PUBLICACION ='" + (String) registro.getDefCampo("ID_PUBLICACION") + "' \n";
		ejecutaSql();
		//OBTIENE EL REGISTRO DE LA CONSULTA
		Registro registroContador = (Registro)getConsultaRegistro();
		String sContador = (String)registroContador.getDefCampo("CONTADOR");
		//OBTIENE EL CONTADOR ORIGINAL Y LE SUMA UNO
		int iContador = (Integer.parseInt(sContador)) + 1;
		//ACTUALIZA EL CONTADOR POR EL NUEVO VALOR
		sSql = "UPDATE RS_PUB_PUBLICACION SET \n" +
			"CONTADOR = " + String.valueOf(iContador) + " \n"+
			"WHERE  CVE_GPO_EMPRESA ='" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND CVE_PORTAL ='" + (String) registro.getDefCampo("CVE_PORTAL") + "' \n"+
			" AND CVE_SECCION ='" + (String) registro.getDefCampo("CVE_SECCION") + "' \n"+
			" AND ID_PUBLICACION ='" + (String) registro.getDefCampo("ID_PUBLICACION") + "' \n";
		//VERIFICA SI NO SE MODIFICO EL REGISTRO
		if (ejecutaUpdate() == 0) {
			bExito = false;
		}
		else {
			bExito = true;
		}
		return bExito;
	}

	/**
	 * Borra un registro.
	 * @param registro Llave primaria.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException {
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		//VERIFICA SI LA FUNCION EXISTE EN LA TABLA RS_CONF_MENU
		sSql = "SELECT CVE_APLICACION FROM RS_CONF_APLICACION_FUNCION \n" +
			"WHERE CVE_APLICACION= '" + (String) registro.getDefCampo("CVE_APLICACION") + "' \n";
		ejecutaSql();
		if (rs.next()) {
			resultadoCatalogo.mensaje.setClave("NO_BORRAR_REGISTRO_RELACIONADO");
		}
		else {
			//BORRA LA FUNCION
			sSql = "DELETE FROM RS_CONF_APLICACION " +
				" WHERE CVE_APLICACION='" + (String) registro.getDefCampo("CVE_APLICACION") + "' \n";

			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0) {
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
		}
		return resultadoCatalogo;
	}

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getPropiedadesUsuarioPublicaciones(Registro parametros) throws SQLException {
		sSql = "SELECT * FROM RS_CONF_USUARIO_PORTAL " +
			"WHERE CVE_GPO_EMPRESA = '" + (String) parametros.getDefCampo("CVE_GPO_EMPRESA") + "' " +
			" AND CVE_PORTAL = '" + (String) parametros.getDefCampo("CVE_PORTAL") + "' " +
			" AND CVE_USUARIO = '" + (String) parametros.getDefCampo("CVE_USUARIO") + "' ";
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Se obtienen las opciones que forman parte de un menú.
	 * @param sCveGpoEmpresa Clave del grupo de empresa
	 * @param sCvePortal Clave del portal
	 * @param sCveOpcionPadre Clave del a seccion padre
	 * @param bUsuarioFirmado true Indica si el usuario esta firmado y false indica que no esta firmado
	 * @param bSoloPublicaciones true indica que muestre publicaciones
	 * @param sIdNivelAcceso Clave del nivel de acceso
	 * @param sCvePerfilPub Clave del perfil del usuario
	 * @param bSoloRutaSeccion Indica que sólo obtenga la ruta de la seccion
	 * @param sCveSeccion Clave de la seccion
	 * @return mapaOpciones TreeMap
	 */
	public TreeMap getOpcionSeccion(String sCveGpoEmpresa, String sCvePortal, String sCveUsuario, String sCveOpcionPadre, boolean bUsuarioFirmado, boolean bSoloPublicaciones, String sIdNivelAcceso, String sCvePerfilPub, boolean bSoloRutaSeccion, String sCveSeccion) {
		boolean bInicializa = true;
		TreeMap mapaOpciones = null;
		try {
			//DETERMINA QUE CONSULTA EJECUTAR
			//MUESTRA SECCIONES PRIVADAS Y PUBLICAS QUE TIENEN DOCUMENTOS PUBLICADOS
			if (bUsuarioFirmado && bSoloPublicaciones) {
				sSql = " SELECT DISTINCT PS.* \n" +
				" 	 FROM RS_PUB_SECCION PS \n" +
				" 	 WHERE CVE_GPO_EMPRESA = '" + sCveGpoEmpresa + "' \n" +
				" 	 AND CVE_PORTAL = '" + sCvePortal + "' \n" +
				"	 AND CVE_SECCION_PADRE " + sCveOpcionPadre + " \n" +
				"    AND CVE_SECCION != 'Unidad' \n" +
				"    AND CVE_SECCION != 'Evangelistas' \n" +
				"	 START WITH CVE_SECCION IN ( \n" +
				" 								SELECT PP.CVE_SECCION \n" +
				" 								FROM RS_PUB_PUBLICACION PP, \n" +
				" 								RS_PUB_PERFIL_SECCION PS \n" +
				" 								WHERE PP.CVE_GPO_EMPRESA = '" + sCveGpoEmpresa + "' \n" +
				"								AND PS.CVE_GPO_EMPRESA = PP.CVE_GPO_EMPRESA \n" +
				"								AND PS.CVE_PORTAL = PP.CVE_PORTAL \n" +
				"								AND PS.CVE_SECCION = PP.CVE_SECCION \n" +
				"								AND PS.CVE_PERFIL_PUB = '" + sCvePerfilPub + "' \n" +
				" 								AND PP.CVE_PORTAL = '" + sCvePortal + "' \n" +
				" 								AND PP.B_AUTORIZADO = 'V' \n" +
				" 								AND SYSDATE BETWEEN PP.F_INI_VIGENCIA AND PP.F_FIN_VIGENCIA  \n" +
				" 								AND PP.ID_NIVEL_ACCESO <= 0 \n" +
				" 						UNION \n " +
				" 							    SELECT CVE_SECCION \n" +
				"								FROM RS_PUB_PERFIL_SECCION \n" +
				"								WHERE CVE_GPO_EMPRESA = '" + sCveGpoEmpresa + "' \n" +
				"								AND CVE_PORTAL = '" + sCvePortal + "' \n" +
				" 								AND CVE_PERFIL_PUB = '" + sCvePerfilPub + "' \n" +
				" 								AND CVE_SECCION IN (  \n" +
				" 													SELECT CVE_SECCION  \n" +
				"													FROM RS_PUB_PUBLICACION \n" +
				"													WHERE CVE_GPO_EMPRESA = '" + sCveGpoEmpresa + "' \n" +
				"													AND CVE_PORTAL = '" + sCvePortal + "'  \n" +
				" 													AND B_AUTORIZADO = 'V' \n" +
				" 													AND SYSDATE BETWEEN F_INI_VIGENCIA AND F_FIN_VIGENCIA  \n" +
				"													AND ID_NIVEL_ACCESO <= " + sIdNivelAcceso + " \n" +
				" 													) \n" +
				" 								) \n" +
				" CONNECT BY PRIOR CVE_SECCION_PADRE = CVE_SECCION \n" +
				" ORDER BY CVE_GPO_EMPRESA, CVE_PORTAL, CVE_SECCION, NUM_ORDEN \n";
			}
			else {
				//MUESTRA SOLO SECCIONES PUBLICAS QUE TIENEN DOCUMENTOS PUBLICADOS
				if (!bUsuarioFirmado && bSoloPublicaciones) {
					sSql = " SELECT DISTINCT * \n" +
				       "FROM RS_PUB_SECCION PS \n" +
					   "WHERE CVE_GPO_EMPRESA = '" + sCveGpoEmpresa + "' \n" +
					   "         AND CVE_PORTAL ='" + sCvePortal + "' \n" +
					   "         AND CVE_SECCION_PADRE " + sCveOpcionPadre + " \n" +
					   "    AND CVE_SECCION != 'Unidad' \n" +
					   "    AND CVE_SECCION != 'Evangelistas' \n" +
					   "    AND CVE_SECCION != 'Video' \n" +
					   "         START WITH CVE_SECCION in ( \n" +
					   "                                       SELECT PP.CVE_SECCION \n" +
					   "                                       FROM RS_PUB_PUBLICACION PP, \n" +
					   "                                            RS_PUB_PERFIL_SECCION PS \n" +
					   "                                       WHERE PP.CVE_GPO_EMPRESA = '" + sCveGpoEmpresa + "' \n" +
					   "                                            AND PS.CVE_GPO_EMPRESA = PP.CVE_GPO_EMPRESA \n" +
					   "                                            AND PS.CVE_PORTAL = PP.CVE_PORTAL \n" +
					   "                                            AND PS.CVE_SECCION = PP.CVE_SECCION \n" +
					   "                                            AND PS.CVE_PERFIL_PUB = '" + sCvePerfilPub + "' \n" +
					   "                                            AND PP.CVE_PORTAL = '" + sCvePortal + "' \n" +
					   "                                            AND PP.B_AUTORIZADO = 'V' \n" +
					   "                                            AND SYSDATE BETWEEN PP.F_INI_VIGENCIA AND PP.F_FIN_VIGENCIA \n" +
					   "                                            AND PP.ID_NIVEL_ACCESO <= 0 \n" +
					   "                                    ) \n" +
					   "CONNECT BY CVE_SECCION_PADRE = CVE_SECCION \n" +
					   "ORDER BY CVE_GPO_EMPRESA, CVE_PORTAL, CVE_SECCION, NUM_ORDEN \n" ;
					   System.out.println("ya lo cambie a ver que tal"+sSql);
				}
				else {
					if (bSoloRutaSeccion) {
						//BUSCA SOLO LA RUTA DE UNA SECCION
						sSql = "SELECT DISTINCT PS.* \n" +
							"FROM RS_PUB_SECCION PS \n" +
							"WHERE CVE_GPO_EMPRESA = '" + sCveGpoEmpresa + "' \n" +
							"     AND CVE_SECCION_PADRE " + sCveOpcionPadre + " \n" +
							" 	  AND CVE_PORTAL = '" + sCvePortal + "' \n" +
							"						START WITH CVE_SECCION = '" + sCveSeccion + "' " +
							"						CONNECT BY PRIOR CVE_SECCION_PADRE = CVE_SECCION \n" +
							"						ORDER BY CVE_GPO_EMPRESA, CVE_PORTAL, CVE_SECCION, NUM_ORDEN \n";
					}
				
					else {
						//BUSCA TODAS LAS SECCIONES SIN IMPORTAR SI HAY DOCUMENTOS PUBLICADOS
						//SE OBTIENEN LAS OPCIONES DE UN MENU
						sSql = "SELECT CVE_PERFIL_PUB " +
						   "FROM RS_CONF_USUARIO_PORTAL " +
						   "WHERE CVE_GPO_EMPRESA = '" + sCveGpoEmpresa + "' \n" +
						   " AND CVE_PORTAL = '" + sCvePortal + "' \n" +
						   " AND CVE_USUARIO = '" + sCveUsuario + "' \n" ;
						ejecutaSql();
					
						if (rs.next()){
							String sPerfilUsuarioPublicacion = rs.getString("CVE_PERFIL_PUB");	
					
					
							sSql= " SELECT SEC.CVE_SECCION, \n" +
						"		   SEC.CVE_SECCION_PADRE, \n" +
						"		   SEC.CVE_GPO_RESP, \n" +
						"		   SEC.NOM_SECCION, \n" +
						"		   B_BLOQUEADO, \n" +
						"		   SEC.NUM_ORDEN, \n" +
						"		   PS.CVE_PERFIL_PUB \n" +
						"	FROM   RS_PUB_SECCION SEC, \n" +
						"		   RS_PUB_PERFIL_SECCION PS \n" +
						"	WHERE  SEC.CVE_GPO_EMPRESA = '" + sCveGpoEmpresa + "' \n" +
						"	AND PS.CVE_GPO_EMPRESA = SEC.CVE_GPO_EMPRESA \n" +
						"	AND SEC.CVE_PORTAL = '" + sCvePortal + "' \n" +
						"	AND PS.CVE_PORTAL = SEC.CVE_PORTAL \n" +
						"	AND PS.CVE_SECCION = SEC.CVE_SECCION \n" +
						"	AND SEC.CVE_SECCION_PADRE " + sCveOpcionPadre + " \n" +
						"	AND PS.CVE_PERFIL_PUB = '" + sPerfilUsuarioPublicacion + "' \n" +
						"	ORDER BY SEC.NUM_ORDEN \n" ;
						}
						
					}
				}
			}
			ejecutaSql();
			while (rs.next()) {
				//VERIFICA SI ES NECESARIO INICIALIZAR LA LISTA
				if (bInicializa) {
					bInicializa = false;
					mapaOpciones = new TreeMap();
				}
				Registro registroOpcion = new Registro();
				registroOpcion.addDefCampo("CVE_SECCION", rs.getString("CVE_SECCION"));
				registroOpcion.addDefCampo("CVE_SECCION_PADRE", rs.getString("CVE_SECCION_PADRE"));
				registroOpcion.addDefCampo("CVE_GPO_RESP", rs.getString("CVE_GPO_RESP"));
				registroOpcion.addDefCampo("NOM_SECCION", rs.getString("NOM_SECCION"));
				registroOpcion.addDefCampo("NUM_ORDEN", rs.getString("NUM_ORDEN"));
				registroOpcion.addDefCampo("B_BLOQUEADO", rs.getString("B_BLOQUEADO"));
				mapaOpciones.put(rs.getString("NUM_ORDEN"), registroOpcion);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return mapaOpciones;
	}

	/**
	 * Se obtienen las opciones que forman parte de un menú.
	 * @param sCveGpoEmpresa Clave del grupo de empresa
	 * @param sCvePortal Clave del portal
	 * @param sCveOpcionPadre Clave del a seccion padre
	 * @param bUsuarioFirmado true Indica si el usuario esta firmado y false indica que no esta firmado
	 * @param bSoloPublicaciones true indica que muestre publicaciones
	 * @param sIdNivelAcceso Clave del nivel de acceso
	 * @param sCvePerfilPub Clave del perfil del usuario
	 * @param bSoloRutaSeccion Indica que sólo obtenga la ruta de la seccion
	 * @param sCveSeccion Clave de la seccion
	 * @return mapaOpciones TreeMap
	 */
	public TreeMap getOpcionSeccionEspecial(String sCveGpoEmpresa, String sCvePortal, String sCveOpcionPadre, boolean bUsuarioFirmado, boolean bSoloPublicaciones, String sIdNivelAcceso, String sCvePerfilPub, boolean bSoloRutaSeccion, String sCveSeccion) {
		boolean bInicializa = true;
		TreeMap mapaOpciones = null;
		int i = 0;
		int k = 0;
		try {
			//DETERMINA QUE CONSULTA EJECUTAR
			//MUESTRA SECCIONES PRIVADAS Y PUBLICAS QUE TIENEN DOCUMENTOS PUBLICADOS
			sSql ="SELECT...  AQUI VA EL QRY";
			ejecutaSql();
			//OBTIENE EL NUMERO DE CAMPOS QUE TIENE CADA REGISTRO
			i = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				//VERIFICA SI ES NECESARIO INICIALIZAR LA LISTA
				if (bInicializa) {
					bInicializa = false;
					mapaOpciones = new TreeMap();
				}
				Registro registroOpcion = new Registro();
				for (int j=1; j<=i; j++){
					registroOpcion.addDefCampo(rs.getMetaData().getColumnName(j), rs.getString(rs.getMetaData().getColumnName(j)));
				}
				k = k+1;
				mapaOpciones.put(String.valueOf(k), registroOpcion);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
		}
		return mapaOpciones;
	}

	/**
	 * Obtiene un registro con los datos de la publicacion que se busca y
	 * validando que sus permisos de acceso a esta publicacion
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getPublicacion(Registro parametros) throws SQLException {
			sSql = "SELECT PUB_PUB.CVE_GPO_EMPRESA, \n "+
				"   PUB_PUB.CVE_PORTAL, \n "+
				"   PUB_PUB.ID_PUBLICACION, \n "+
				"   PUB_PUB.CVE_SECCION, \n "+
				"   PUB_PUB.ID_NIVEL_ACCESO, \n "+
				"   PUB_PUB.NOM_PUBLICACION, \n "+
				"   PUB_PUB.DESC_PUBLICACION, \n "+
				"   PUB_PUB.URL_PUBLICACION, \n "+
				"   PUB_PUB.URL_IMAGEN, \n "+
				"   PUB_PUB.B_AUTORIZADO, \n "+
				"   PUB_PUB.F_INI_VIGENCIA, \n "+
				"   PUB_PUB.F_FIN_VIGENCIA, \n "+
				"   PUB_PUB.PASSWORD, \n "+
				"   PUB_PUB.CONTADOR, \n "+
				"   PUB_PUB.B_PRINCIPAL, \n "+
				"   PUB_PUB.CONTROLADOR, \n "+
				"   PUB_PUB.STREAM \n "+
				" FROM RS_CONF_USUARIO_PORTAL USU_POR, \n "+
				"   RS_PUB_PUBLICACION PUB_PUB \n "+
				" WHERE  \n "+
				"   USU_POR.CVE_GPO_EMPRESA = '"+ (String)parametros.getDefCampo("CVE_GPO_EMPRESA") +"' \n "+
				"   AND USU_POR.CVE_USUARIO = '"+ (String)parametros.getDefCampo("CVE_USUARIO") +"' \n "+
				"   AND USU_POR.CVE_PORTAL = '"+ (String)parametros.getDefCampo("CVE_PORTAL") +"' \n "+
				"   AND USU_POR.ID_NIVEL_ACCESO >= PUB_PUB.ID_NIVEL_ACCESO \n " +
				"   AND USU_POR.CVE_GPO_EMPRESA = PUB_PUB.CVE_GPO_EMPRESA \n " +
				"   AND USU_POR.CVE_PORTAL = PUB_PUB.CVE_PORTAL \n " +
				"   AND PUB_PUB.ID_PUBLICACION = '" + (String) parametros.getDefCampo("ID_PUBLICACION") + "' ";
		ejecutaSql();
		
		return this.getConsultaRegistro();
	}

	/**
	 * Obtiene un conjunto de registros con las publicaciones que puede ver un usuario.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getPublicaciones(Registro parametros) throws SQLException {
		if (parametros.getDefCampo("USUARIO_FIRMADO").equals("SI")){
			sSql = " SELECT DISTINCT * \n"+
				" FROM RS_PUB_PUBLICACION \n"+
				" WHERE CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"      AND CVE_PORTAL = '" + parametros.getDefCampo("CVE_PORTAL") + "' \n"+
				"      AND ID_NIVEL_ACCESO <= " + parametros.getDefCampo("ID_NIVEL_ACCESO") + " \n"+
				"      AND B_AUTORIZADO = 'V' \n"+
				"      AND SYSDATE BETWEEN F_INI_VIGENCIA AND F_FIN_VIGENCIA \n"+
				"      AND CVE_SECCION = '"+parametros.getDefCampo("CVE_SECCION")+"' \n"+
				"      AND CVE_SECCION IN (\n"+
				"						 SELECT CVE_SECCION \n"+
				"							 FROM RS_PUB_PERFIL_SECCION \n"+
				"							 WHERE CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"								   AND CVE_PORTAL = '" + parametros.getDefCampo("CVE_PORTAL") + "'  \n"+
				"								   AND CVE_PERFIL_PUB = '" + parametros.getDefCampo("CVE_PERFIL_PUB") + "' \n"+
				"                         UNION \n"+
				"                         SELECT CVE_SECCION \n"+
				"                         FROM RS_PUB_PUBLICACION \n"+
				"                         WHERE CVE_GPO_EMPRESA='"+ parametros.getDefCampo("CVE_GPO_EMPRESA") +"' \n"+
				"                               AND CVE_PORTAL = '" + parametros.getDefCampo("CVE_PORTAL") + "' \n"+
				"                               AND ID_NIVEL_ACCESO = 0 \n"+
				"                               AND SYSDATE BETWEEN F_INI_VIGENCIA AND F_FIN_VIGENCIA \n"+
				"                               AND B_AUTORIZADO = 'V' \n"+
				"							) \n"+
				" ORDER BY ID_PRIORIDAD, ID_PUBLICACION DESC \n";
		} else {
			sSql = " SELECT DISTINCT * \n"+
				" FROM RS_PUB_PUBLICACION \n"+
				" WHERE CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"      AND CVE_PORTAL = '" + parametros.getDefCampo("CVE_PORTAL") + "' \n"+
				"      AND ID_NIVEL_ACCESO <= 0 \n"+
				"      AND B_AUTORIZADO = 'V' \n"+
				"      AND SYSDATE BETWEEN F_INI_VIGENCIA AND F_FIN_VIGENCIA \n"+
				"      AND CVE_SECCION = '"+parametros.getDefCampo("CVE_SECCION")+"' \n"+
				" ORDER BY ID_PRIORIDAD, ID_PUBLICACION DESC \n";
		}
		ejecutaSql();
		return this.getConsultaLista();
	}
}