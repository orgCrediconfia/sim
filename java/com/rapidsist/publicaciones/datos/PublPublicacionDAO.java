/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.publicaciones.datos;

import com.rapidsist.portal.catalogos.OperacionAlta; 
import com.rapidsist.portal.catalogos.OperacionBaja; 
import com.rapidsist.portal.catalogos.OperacionModificacion; 
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.comun.util.Fecha2;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el cat�logo de publicaciones.
 */
public class PublPublicacionDAO extends Conexion2 implements OperacionAlta, OperacionBaja,  OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base a un filtro de b�squeda.
	 * @param parametros Par�metros que se le env�an a la consulta para obtener el conjunto
	 * de registros deseados. 
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		boolean bNomPublicacion = false;
		LinkedList lista = new LinkedList();
		String sFiltro = (String) parametros.getDefCampo("Filtro");

			sSql = " SELECT \n" +
				" PP.CVE_GPO_EMPRESA, \n" +
				" PP.CVE_PORTAL, \n" +
				" PP.CVE_SECCION, \n" +
				" PP.ID_PUBLICACION, \n" +
				" PP.ID_NIVEL_ACCESO, \n" +
				" PP.NOM_PUBLICACION, \n" +
				" PP.TX_COMENTARIO, \n" +
				" PP.DESC_PUBLICACION, \n" +
				" PP.URL_PUBLICACION, \n" +
				" PP.URL_IMAGEN, \n" +
				" PP.B_AUTORIZADO, \n" +
				" PP.F_INI_VIGENCIA, \n" +
				" PP.F_FIN_VIGENCIA, \n" +
				" PP.PASSWORD, \n" +
				" PP.CONTADOR, \n" +
				" PP.CVE_USUARIO, \n" +
				" PP.B_PRINCIPAL, \n" +
				" PP.ID_PRIORIDAD \n" +
				" FROM \n" +
				" RS_PUB_PUBLICACION PP \n" +
				" WHERE \n"+
				" PP.CVE_GPO_EMPRESA = '" + (String) parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND PP.CVE_PORTAL ='" + (String) parametros.getDefCampo("CVE_PORTAL") + "' \n"+
				" AND PP.CONTROLADOR IS NULL \n"+
				" AND PP.VIDEO IS NULL \n";

			if (parametros.getDefCampo("CVE_SECCION") != null) {
				sSql = sSql + " AND PP.CVE_SECCION ='" + (String) parametros.getDefCampo("CVE_SECCION") + "' \n";
				bNomPublicacion = true;
			}
			if (parametros.getDefCampo("F_INI_VIGENCIA") != null) {
				sSql = sSql + " AND PP.F_INI_VIGENCIA >= TO_DATE('" +  (String) parametros.getDefCampo("F_INI_VIGENCIA") + ":00-00-00','dd/mm/yyyy:hh24-mi-ss') \n";
				bNomPublicacion = true;
			}
			if (parametros.getDefCampo("F_FIN_VIGENCIA") != null) {
				sSql = sSql + " AND PP.F_FIN_VIGENCIA >= TO_DATE('" +  (String) parametros.getDefCampo("F_FIN_VIGENCIA") + ":00-00-00','dd/mm/yyyy:hh24-mi-ss') \n";
				bNomPublicacion = true;
			}
			if (parametros.getDefCampo("ID_NIVEL_ACCESO") != null) {
				sSql = sSql + " AND PP.ID_NIVEL_ACCESO ='" + (String) parametros.getDefCampo("ID_NIVEL_ACCESO") + "' \n";
				bNomPublicacion = true;
			}
			if (parametros.getDefCampo("NOM_PUBLICACION") != null) {
				sSql = sSql + " AND PP.NOM_PUBLICACION LIKE'%" + (String) parametros.getDefCampo("NOM_PUBLICACION") + "%' \n";
				bNomPublicacion = true;
			}
			sSql = sSql + " ORDER BY PP.ID_PUBLICACION ";
		ejecutaSql();
		while (rs.next()){
			Registro registro= new Registro();
			registro.addDefCampo("CVE_GPO_EMPRESA",rs.getString("CVE_GPO_EMPRESA"));
			registro.addDefCampo("CVE_PORTAL",rs.getString("CVE_PORTAL"));
			registro.addDefCampo("CVE_SECCION",rs.getString("CVE_SECCION"));
			registro.addDefCampo("ID_PUBLICACION",rs.getString("ID_PUBLICACION"));
			registro.addDefCampo("ID_NIVEL_ACCESO",rs.getString("ID_NIVEL_ACCESO"));
			registro.addDefCampo("NOM_PUBLICACION",rs.getString("NOM_PUBLICACION"));
			registro.addDefCampo("TX_COMENTARIO",rs.getString("TX_COMENTARIO"));
			registro.addDefCampo("DESC_PUBLICACION",rs.getString("DESC_PUBLICACION"));
			registro.addDefCampo("URL_PUBLICACION",rs.getString("URL_PUBLICACION"));
			registro.addDefCampo("URL_IMAGEN",rs.getString("URL_IMAGEN"));
			registro.addDefCampo("ID_PRIORIDAD",rs.getString("ID_PRIORIDAD"));
			if (rs.getString("B_AUTORIZADO").equals("V")){
				registro.addDefCampo("B_AUTORIZADO","SI");
			}
			else{
				registro.addDefCampo("B_AUTORIZADO","NO");
			}
			//registro.addDefCampo("B_AUTORIZADO",rs.getString("B_AUTORIZADO"));
			registro.addDefCampo("F_INI_VIGENCIA",Fecha2.formatoSimple(rs.getDate("F_INI_VIGENCIA")));
			registro.addDefCampo("F_FIN_VIGENCIA",Fecha2.formatoSimple(rs.getDate("F_FIN_VIGENCIA")));
			registro.addDefCampo("PASSWORD",rs.getString("PASSWORD"));
			registro.addDefCampo("CONTADOR",rs.getString("CONTADOR"));
			registro.addDefCampo("CVE_USUARIO",rs.getString("CVE_USUARIO"));
			registro.addDefCampo("B_PRINCIPAL",rs.getString("B_PRINCIPAL"));
			lista.add(registro);
		}
		return lista;
	}
	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Par�metros que se le env�an a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		Registro registro= new Registro();
		sSql = " SELECT \n" +
			" PP.CVE_GPO_EMPRESA, \n" +
			" PP.CVE_PORTAL, \n" +
			" PP.CVE_SECCION, \n" +
			" PP.ID_PUBLICACION, \n" +
			" PP.ID_NIVEL_ACCESO, \n" +
			" PP.NOM_PUBLICACION, \n" +
			" PP.TX_COMENTARIO, \n" +
			" PP.DESC_PUBLICACION, \n" +
			" PP.URL_PUBLICACION, \n" +
			" PP.URL_IMAGEN, \n" +
			" PP.B_AUTORIZADO, \n" +
			" TO_CHAR(PP.F_INI_VIGENCIA,'DD/MM/YYYY') F_INI_VIGENCIA, \n" +
			" TO_CHAR(PP.F_FIN_VIGENCIA,'DD/MM/YYYY') F_FIN_VIGENCIA, \n" +
			" PP.PASSWORD, \n" +
			" PP.CONTADOR, \n" +
			" PP.CVE_USUARIO, \n" +
			" PP.B_PRINCIPAL, \n" +
			" PP.ID_PRIORIDAD, \n" +
			" PP.CONTROLADOR \n" +
			" FROM \n" +
			" RS_PUB_PUBLICACION PP \n" +
			" WHERE \n"+
			" PP.CVE_GPO_EMPRESA ='" + (String) parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND PP.CVE_PORTAL = '" + (String) parametros.getDefCampo("CVE_PORTAL") + "' \n"+
				" AND PP.ID_PUBLICACION = '" + (String) parametros.getDefCampo("ID_PUBLICACION") + "' \n";
		ejecutaSql();
		if (rs.next()){
			registro.addDefCampo("CVE_GPO_EMPRESA",rs.getString("CVE_GPO_EMPRESA"));
			registro.addDefCampo("CVE_PORTAL",rs.getString("CVE_PORTAL"));
			registro.addDefCampo("CVE_SECCION",rs.getString("CVE_SECCION"));
			registro.addDefCampo("ID_PUBLICACION",rs.getString("ID_PUBLICACION"));
			registro.addDefCampo("ID_NIVEL_ACCESO",rs.getString("ID_NIVEL_ACCESO"));
			registro.addDefCampo("NOM_PUBLICACION",rs.getString("NOM_PUBLICACION"));
			registro.addDefCampo("TX_COMENTARIO",rs.getString("TX_COMENTARIO"));
			registro.addDefCampo("DESC_PUBLICACION",rs.getString("DESC_PUBLICACION"));
			registro.addDefCampo("URL_PUBLICACION",rs.getString("URL_PUBLICACION"));
			registro.addDefCampo("URL_IMAGEN",rs.getString("URL_IMAGEN"));
			registro.addDefCampo("ID_PRIORIDAD",rs.getString("ID_PRIORIDAD"));
			if (rs.getString("B_AUTORIZADO").equals("V")){
				registro.addDefCampo("B_AUTORIZADO","SI");
			}
			else{
				registro.addDefCampo("B_AUTORIZADO","NO");
			}
			registro.addDefCampo("F_INI_VIGENCIA",rs.getString("F_INI_VIGENCIA"));
			registro.addDefCampo("F_FIN_VIGENCIA",rs.getString("F_FIN_VIGENCIA"));
			registro.addDefCampo("PASSWORD",rs.getString("PASSWORD")== null ? "": rs.getString("PASSWORD"));
			registro.addDefCampo("CONFIRME_PASSWORD",rs.getString("PASSWORD")== null ? "": rs.getString("PASSWORD"));
			registro.addDefCampo("CONTADOR",rs.getString("CONTADOR"));
			registro.addDefCampo("CVE_USUARIO",rs.getString("CVE_USUARIO"));
			registro.addDefCampo("B_PRINCIPAL",rs.getString("B_PRINCIPAL"));
			registro.addDefCampo("CONTROLADOR",rs.getString("CONTROLADOR"));
		}
		return registro;
	}

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuci�n de este m�todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		registro.addDefCampo("URL_PUBLICACION",registro.getDefCampo("URL_PUBLICACION")== null? "": registro.getDefCampo("URL_PUBLICACION"));
		registro.addDefCampo("URL_IMAGEN",registro.getDefCampo("URL_IMAGEN")== null? "": registro.getDefCampo("URL_IMAGEN"));
		registro.addDefCampo("PASSWORD",registro.getDefCampo("PASSWORD")== null? "": registro.getDefCampo("PASSWORD"));
		//VERIFICA SI EL PERFIL LE PERMITE AUTORIZAR DE INMEDIATO O SI EL USUARIO PERTENECE AL 
		//GRUPO RESPONSABLE ASOCIADO A LA SECCION
			sSql =  " SELECT PUB_SEC.CVE_SECCION \n"+
			  " FROM   RS_PUB_SECCION PUB_SEC, \n"+
			  "        RS_PUB_GPO_RESP_SECCION GPO_RES_SEC \n"+
			  " WHERE PUB_SEC.CVE_GPO_EMPRESA = '"+(String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n"+
			  " AND   PUB_SEC.CVE_PORTAL = '"+(String)registro.getDefCampo("CVE_PORTAL")+"' \n"+
			  " AND   PUB_SEC.CVE_SECCION = '"+(String)registro.getDefCampo("CVE_SECCION")+"' \n"+
			  " AND GPO_RES_SEC.CVE_GPO_EMPRESA = PUB_SEC.CVE_GPO_EMPRESA \n"+
			  " AND GPO_RES_SEC.CVE_PORTAL = PUB_SEC.CVE_PORTAL \n"+
			  " AND GPO_RES_SEC.CVE_GPO_RESP = PUB_SEC.CVE_GPO_RESP \n"+
			  " AND GPO_RES_SEC.CVE_USUARIO ='"+(String)registro.getDefCampo("CVE_USUARIO")+"' \n";
		//SI ENCUENTRA ALGUN REGISTRO EL CAMPO B_AUTORIZADO ES VERDADERO
		//DE OTRA FORMA ES FALSO, ES DECIR, VERDADERO INDICA QUE LA PUBLICACION
		//SE PUEDE AUTORIZAR Y FALSO INDICA QUE LA PUBLICACION NO LA PUEDE AUTORIZAR
		//EL USUARIO FIRMADO
		ejecutaSql();
		if (rs.next()){
			registro.addDefCampo("B_AUTORIZADO", "V");
		}else{
			registro.addDefCampo("B_AUTORIZADO", "F");
		}
		//OBTENEMOS EL SEQUENCE
		sSql = "SELECT SQ01_RS_PUB_PUBLICACION.nextval as ID_PUBLICACION FROM DUAL";
		ejecutaSql();
		String sIdPublicacion = "";
		if (rs.next()){
			sIdPublicacion = rs.getString("ID_PUBLICACION");
		}
		//INSERTA EL REGISTRO
		sSql = "INSERT INTO RS_PUB_PUBLICACION ( \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_PORTAL, \n" +
					"CVE_SECCION, \n" +
					"ID_PUBLICACION, \n" +
					"ID_NIVEL_ACCESO, \n" +
					"NOM_PUBLICACION, \n" +
					"TX_COMENTARIO, \n" +
					"DESC_PUBLICACION, \n" +
					"URL_PUBLICACION, \n" +
					"URL_IMAGEN, \n" +
					"B_AUTORIZADO, \n" +
					"F_INI_VIGENCIA, \n" +
					"F_FIN_VIGENCIA, \n" +
					"CONTADOR, \n" +
					"CVE_USUARIO, \n" +
					"B_PRINCIPAL,  \n"+
					"ID_PRIORIDAD,  \n" +
					"SECCION_ANTERIOR )  \n" +
				"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_PORTAL") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_SECCION")  + "', \n" +
					sIdPublicacion + ", \n" +
					"'" + (String)registro.getDefCampo("ID_NIVEL_ACCESO")  + "', \n" +
					"'" + (String)registro.getDefCampo("NOM_PUBLICACION")  + "', \n" +
					"'" + (String)registro.getDefCampo("TX_COMENTARIO")  + "', \n" +
					"'" + (String)registro.getDefCampo("DESC_PUBLICACION")  + "', \n" +
					"'" + (String)registro.getDefCampo("URL_PUBLICACION")  + "', \n" +
					"'" + (String)registro.getDefCampo("URL_IMAGEN")  + "', \n" +
					"'" + (String)registro.getDefCampo("B_AUTORIZADO")  + "', \n" +
					"" +(String)registro.getDefCampo("F_INI_VIGENCIA")+",\n"+
					"" +(String)registro.getDefCampo("F_FIN_VIGENCIA")+",\n"+
					"'0', \n" +
					"'" + (String)registro.getDefCampo("CVE_USUARIO")  + "', \n" +
					"'', \n"+
					"'" + (String)registro.getDefCampo("ID_PRIORIDAD")  + "',  \n" +
					"'" + (String)registro.getDefCampo("CVE_SECCION")  + "') \n" ;
			
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		else {
			resultadoCatalogo.Resultado = new Registro();
			resultadoCatalogo.Resultado.addDefCampo("SEQUENCE", sIdPublicacion);
			//SI SE AGREGO CORRECTAMENTE EN LA TABLA ENTONCES SE DEBE DETERMINAR LA RUTA
			//DONDE SE VA A ALMACENAR LA PUBLICACI�N.
			try{
				//SE INSTANCIA LA CLASE QUE CONSTRUYE LAS CARPETAS
				PubAdministracionCarpetas admoncarpetas = new PubAdministracionCarpetas();
				//OBTIENE LA RUTA DE LA PUBLICACION EN BASE A LA SECCI�N QUE PERTENECE
				String sRutaPublicacion = admoncarpetas.getRuta(registro, this);
				//ALMACENA EN EL OBJETO resultadoCatalogo.Resultado EL VALOR DE LA RUTA
				resultadoCatalogo.mensaje.setDescripcion(sRutaPublicacion);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return resultadoCatalogo;
	}

	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecuci�n de este m�todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		registro.addDefCampo("URL_PUBLICACION",registro.getDefCampo("URL_PUBLICACION")== null? "PUBLICACION": registro.getDefCampo("URL_PUBLICACION"));
		registro.addDefCampo("URL_IMAGEN",registro.getDefCampo("URL_IMAGEN")== null? "IMAGEN": registro.getDefCampo("URL_IMAGEN"));
		registro.addDefCampo("PASSWORD",registro.getDefCampo("PASSWORD")== null? "": registro.getDefCampo("PASSWORD"));
				
		//VERIFICA SI EL PERFIL LE PERMITE AUTORIZAR DE INMEDIATO
		sSql =" SELECT \n"+
			  " PS.CVE_SECCION \n"+
			  " FROM \n"+
			  " RS_CONF_USUARIO_PORTAL PU, RS_PUB_PERFIL PP, RS_PUB_PERFIL_SECCION PS \n"+
			  " WHERE \n"+
			  " PU.CVE_USUARIO ='"+(String)registro.getDefCampo("CVE_USUARIO")+"' \n"+
			  " AND PS.CVE_SECCION ='"+(String)registro.getDefCampo("CVE_SECCION")+"' \n"+
			  " AND PU.CVE_GPO_EMPRESA ='"+(String)registro.getDefCampo("CVE_GPO_EMPRESA")+"' \n"+
			  " AND PU.CVE_PORTAL ='"+(String)registro.getDefCampo("CVE_PORTAL")+"' \n"+
			  " AND PU.CVE_GPO_EMPRESA = PP.CVE_GPO_EMPRESA \n"+
			  " AND PP.CVE_GPO_EMPRESA = PS.CVE_GPO_EMPRESA \n"+
			  " AND PU.CVE_PERFIL_PUB = PP.CVE_PERFIL_PUB \n"+
			  " AND PP.CVE_PERFIL_PUB = PS.CVE_PERFIL_PUB \n"+
			  " AND PU.CVE_PORTAL = PP.CVE_PORTAL";
		
		ejecutaSql();
		if (rs.next()){
			registro.addDefCampo("B_AUTORIZADO", "V");
		}
		else{
			registro.addDefCampo("B_AUTORIZADO", "F");
		}
		
		if(registro.getDefCampo("OPERACION_CATALOGO").equals("BA") ){
			
			sSql = " DELETE FROM RS_PUB_PUBLICACION "+
						" WHERE \n"+
						" CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						" AND CVE_PORTAL = '" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n" +
						" AND ID_PUBLICACION = '" + (String)registro.getDefCampo("ID_PUBLICACION") + "' \n";
												
		}
		else{
			//Es una modificaci�n
			sSql = "SELECT CVE_SECCION " +
			   "FROM RS_PUB_PUBLICACION " +
			   "WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_PORTAL = '" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n" +
			   " AND ID_PUBLICACION = '" + (String)registro.getDefCampo("ID_PUBLICACION") + "' \n";
			ejecutaSql();
			
				if (rs.next()){
					String sCveSeccionAnterior = rs.getString("CVE_SECCION");	
					registro.addDefCampo("SECCION_ANTERIOR",sCveSeccionAnterior);
			
					sSql =  "UPDATE RS_PUB_PUBLICACION SET \n"+
							"CVE_SECCION = '" + (String)registro.getDefCampo("CVE_SECCION") + "',\n" +
							"ID_NIVEL_ACCESO ='" + (String)registro.getDefCampo("ID_NIVEL_ACCESO") + "', \n" +
							"NOM_PUBLICACION ='" + (String)registro.getDefCampo("NOM_PUBLICACION") + "', \n" +
							"TX_COMENTARIO ='" + (String)registro.getDefCampo("TX_COMENTARIO") + "', \n" +
							"DESC_PUBLICACION ='" + (String)registro.getDefCampo("DESC_PUBLICACION") + "', \n" +
							"URL_PUBLICACION ='" + (String)registro.getDefCampo("URL_PUBLICACION") + "', \n" +
							"URL_IMAGEN ='" + (String)registro.getDefCampo("URL_IMAGEN") + "', \n" +
							"B_AUTORIZADO ='" + (String)registro.getDefCampo("B_AUTORIZADO") + "', \n" +
							"F_INI_VIGENCIA ="+ (String)registro.getDefCampo("F_INI_VIGENCIA") + ", \n" +
							"F_FIN_VIGENCIA ="+ (String)registro.getDefCampo("F_FIN_VIGENCIA") + ", \n" +
							"CVE_USUARIO ='" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
							"B_PRINCIPAL ='', \n" +
							"ID_PRIORIDAD = '" + (String)registro.getDefCampo("ID_PRIORIDAD") + "', \n" +
							"SECCION_ANTERIOR ='" + sCveSeccionAnterior + "' \n" +
							"WHERE \n"+
							"CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							"AND CVE_PORTAL = '" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n" +
							"AND ID_PUBLICACION = '" + (String)registro.getDefCampo("ID_PUBLICACION") + "' \n";
				}
		}
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		else {
			//SI SE AGREGO CORRECTAMENTE EN LA TABLA ENTONCES SE DEBE DETERMINAR LA RUTA
			//DONDE SE VA A ALMACENAR LA PUBLICACI�N.
			try{
				
				if(registro.getDefCampo("OPERACION_CATALOGO").equals("MO") ){
					
					sSql =  "SELECT SECCION_ANTERIOR " +
							"FROM RS_PUB_PUBLICACION " +
							"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							" AND CVE_PORTAL = '" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n" +
							" AND ID_PUBLICACION = '" + (String)registro.getDefCampo("ID_PUBLICACION") + "' \n";
					ejecutaSql();
				
				
					if (rs.next()){
						String sCveSeccion = rs.getString("SECCION_ANTERIOR");	
						registro.addDefCampo("CVE_SECCION",sCveSeccion);
				
						//SE INSTANCIA LA CLASE QUE CONSTRUYE LAS CARPETAS
						PubAdministracionCarpetas admoncarpetas = new PubAdministracionCarpetas();
						//OBTIENE LA RUTA DE LA PUBLICACION EN BASE A LA SECCI�N QUE PERTENECE
						String sRutaPublicacion = admoncarpetas.getRuta(registro, this);
						//ALMACENA EN EL OBJETO resultadoCatalogo.Resultado EL VALOR DE LA RUTA
						resultadoCatalogo.mensaje.setDescripcion(sRutaPublicacion);
					}
				}
				
				if(registro.getDefCampo("OPERACION_CATALOGO").equals("BA") ){
					PubAdministracionCarpetas admoncarpetas = new PubAdministracionCarpetas();
					String sRutaPublicacion = admoncarpetas.getRuta(registro, this);
					resultadoCatalogo.mensaje.setDescripcion(sRutaPublicacion);
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return resultadoCatalogo;
	}

	/**
	 * Borra un registro.
	 * @param registro Llave primaria.
	 * @return Objeto que contiene el resultado de la ejecuci�n de este m�todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		//BORRA EL REGISTRO
		sSql = "DELETE FROM RS_PUB_PUBLICACION " +
			" WHERE ID_PUBLICACION='" + (String) registro.getDefCampo("ID_PUBLICACION") + "' \n" +
			" AND CVE_GPO_EMPRESA='" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_PORTAL='" + (String) registro.getDefCampo("CVE_PORTAL") + "' \n"+
			" AND CVE_SECCION='" + (String) registro.getDefCampo("CVE_SECCION") + "' \n";
		
		///VERIFICA LA TRANSACCION
		if (ejecutaUpdate() == 0) {
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
}
