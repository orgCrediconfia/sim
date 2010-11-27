/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.comite.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para comites.
 */
 
public class SimComitePrestamoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		if (parametros.getDefCampo("CONSULTA").equals("TODOS")) {
			sSql =  "(SELECT \n"+
					   "P.CVE_GPO_EMPRESA, \n" +
					   "P.CVE_EMPRESA, \n" +
					   "P.ID_PRESTAMO, \n"+
					   "P.ID_CLIENTE CLAVE, \n"+
					   "PE.NOM_COMPLETO NOMBRE, \n"+
					   "P.CVE_PRESTAMO \n"+
					" FROM SIM_PRESTAMO P," +
					" RS_GRAL_PERSONA PE, \n"+
					" SIM_CAT_ETAPA_PRESTAMO E \n"+
					" WHERE P.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					" AND P.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND E.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+ 
					"AND E.CVE_EMPRESA = P.CVE_EMPRESA \n"+ 
					"AND E.ID_ETAPA_PRESTAMO = P.ID_ETAPA_PRESTAMO \n"+ 
					"AND E.B_AUTORIZAR_COMITE = 'V' \n"+ 
					" AND PE.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
					" AND PE.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
					" AND PE.ID_PERSONA (+)= P.ID_CLIENTE \n"+
					" AND ID_GRUPO IS NULL \n"+
					" UNION \n"+
					 "SELECT \n"+
					   "P.CVE_GPO_EMPRESA, \n" +
					   "P.CVE_EMPRESA, \n" +
					   "P.ID_PRESTAMO_GRUPO, \n"+
					   "P.ID_GRUPO CLAVE, \n"+
					   "G.NOM_GRUPO NOMBRE, \n"+
					   "P.CVE_PRESTAMO_GRUPO CVE_PRESTAMO \n"+
					" FROM SIM_PRESTAMO_GRUPO P," +
					" SIM_GRUPO G, \n"+
					" SIM_CAT_ETAPA_PRESTAMO E \n"+
					" WHERE P.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					" AND P.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND E.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+ 
					"AND E.CVE_EMPRESA = P.CVE_EMPRESA \n"+ 
					"AND E.ID_ETAPA_PRESTAMO = P.ID_ETAPA_PRESTAMO \n"+ 
					"AND E.B_AUTORIZAR_COMITE = 'V' \n"+ 
					" AND G.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
					" AND G.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
					" AND G.ID_GRUPO (+)= P.ID_GRUPO) \n"+
					" MINUS \n"+
					 "(SELECT DISTINCT \n"+
					    "CP.CVE_GPO_EMPRESA, \n"+
					    "CP.CVE_EMPRESA, \n"+
						"CP.ID_PRESTAMO_GRUPO ID_PRESTAMO, \n"+
						"GPO.ID_GRUPO CLAVE, \n"+
						"GPO.NOM_GRUPO NOMBRE, \n"+
						"G.CVE_PRESTAMO_GRUPO CVE_PRESTAMO \n"+
						"FROM SIM_COMITE_PRESTAMO CP, \n"+
						"SIM_PRESTAMO_GRUPO G, \n"+
						"SIM_GRUPO GPO \n"+
						"WHERE CP.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CP.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND CP.ID_COMITE = '" + (String)parametros.getDefCampo("ID_COMITE") + "' \n"+
						"AND CP.ID_PRESTAMO_GRUPO IS NOT NULL \n"+
						"AND G.CVE_GPO_EMPRESA = CP.CVE_GPO_EMPRESA \n"+
						"AND G.CVE_EMPRESA = CP.CVE_EMPRESA \n"+
						"AND G.ID_PRESTAMO_GRUPO = CP.ID_PRESTAMO_GRUPO \n"+
						"AND GPO.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+
						"AND GPO.CVE_EMPRESA = G.CVE_EMPRESA \n"+
						"AND GPO.ID_GRUPO = G.ID_GRUPO \n"+
						"UNION \n"+
						"SELECT \n"+
						"CP.CVE_GPO_EMPRESA, \n"+
					    "CP.CVE_EMPRESA, \n"+
						"CP.ID_PRESTAMO, \n"+
						"PER.ID_PERSONA CLAVE, \n"+
						"PER.NOM_COMPLETO NOMBRE, \n"+
						"P.CVE_PRESTAMO \n"+
						"FROM SIM_COMITE_PRESTAMO CP, \n"+
						"SIM_PRESTAMO P, \n"+
						"RS_GRAL_PERSONA PER \n"+
						"WHERE CP.CVE_GPO_EMPRESA= '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND CP.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND CP.ID_COMITE = '" + (String)parametros.getDefCampo("ID_COMITE") + "' \n"+
						"AND CP.ID_PRESTAMO_GRUPO IS NULL \n"+
						"AND P.CVE_GPO_EMPRESA = CP.CVE_GPO_EMPRESA \n"+
						"AND P.CVE_EMPRESA = CP.CVE_EMPRESA \n"+
						"AND P.ID_PRESTAMO = CP.ID_PRESTAMO \n"+
						"AND PER.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+ 
						"AND PER.CVE_EMPRESA = P.CVE_EMPRESA \n"+
						"AND PER.ID_PERSONA = P.ID_CLIENTE) \n";
		
		}else if (parametros.getDefCampo("CONSULTA").equals("ASIGNADOS_GRUPO")) {
			sSql =  "SELECT DISTINCT \n"+
					"CP.ID_PRESTAMO_GRUPO ID_PRESTAMO, \n"+
					"G.CVE_PRESTAMO_GRUPO CVE_PRESTAMO, \n"+
					"CP.ID_COMITE, \n"+
					"GPO.NOM_GRUPO NOMBRE \n"+
					"FROM SIM_COMITE_PRESTAMO CP, \n"+
					"SIM_PRESTAMO_GRUPO G, \n"+
					"SIM_GRUPO GPO \n"+
					"WHERE CP.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CP.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND CP.ID_COMITE = '" + (String)parametros.getDefCampo("ID_COMITE") + "' \n"+
					"AND CP.ID_PRESTAMO_GRUPO IS NOT NULL \n"+
					"AND G.CVE_GPO_EMPRESA = CP.CVE_GPO_EMPRESA \n"+
					"AND G.CVE_EMPRESA = CP.CVE_EMPRESA \n"+
					"AND G.ID_PRESTAMO_GRUPO = CP.ID_PRESTAMO_GRUPO \n"+
					"AND GPO.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+
					"AND GPO.CVE_EMPRESA = G.CVE_EMPRESA \n"+
					"AND GPO.ID_GRUPO = G.ID_GRUPO \n";
			
		}else if (parametros.getDefCampo("CONSULTA").equals("ASIGNADOS_INDIVIDUAL")) {
			sSql =  "SELECT DISTINCT \n"+
			"CP.ID_PRESTAMO, \n"+
			"P.CVE_PRESTAMO, \n"+
			"CP.ID_COMITE, \n"+
			"PER.NOM_COMPLETO NOMBRE, \n"+
			"'INDIVIDUAL'  \n"+
			"FROM SIM_COMITE_PRESTAMO CP, \n"+
			"SIM_PRESTAMO P, \n"+
			"RS_GRAL_PERSONA PER \n"+
			"WHERE CP.CVE_GPO_EMPRESA= '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND CP.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND CP.ID_COMITE = '" + (String)parametros.getDefCampo("ID_COMITE") + "' \n"+
			"AND CP.ID_PRESTAMO_GRUPO IS NULL \n"+
			"AND P.CVE_GPO_EMPRESA = CP.CVE_GPO_EMPRESA \n"+
			"AND P.CVE_EMPRESA = CP.CVE_EMPRESA \n"+
			"AND P.ID_PRESTAMO = CP.ID_PRESTAMO \n"+
			"AND PER.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+ 
			"AND PER.CVE_EMPRESA = P.CVE_EMPRESA \n"+
			"AND PER.ID_PERSONA = P.ID_CLIENTE \n";
}
		
		ejecutaSql();
		return getConsultaLista();
	}
	
	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  "SELECT \n"+
			   "P.CVE_GPO_EMPRESA, \n" +
			   "P.CVE_EMPRESA, \n" +
			   "P.ID_PRESTAMO, \n"+
			   "P.APLICA_A, \n"+
			   "P.ID_PRODUCTO, \n"+
			   "P.NUM_CICLO, \n"+
			   "P.FECHA_SOLICITUD, \n"+
			   "P.FECHA_ENTREGA, \n"+
			   "P.ID_ETAPA_PRESTAMO, \n"+
			   "P.FECHA_REAL, \n"+
			   "P.DIA_SEMANA_PAGO, \n"+
			   "E.NOM_ESTATUS_PRESTAMO ESTATUS \n"+
			" FROM SIM_PRESTAMO P, \n"+
			"      SIM_CAT_ETAPA_PRESTAMO E \n"+
			" WHERE P.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND P.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND P.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND E.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+			
			" AND E.CVE_EMPRESA = P.CVE_EMPRESA \n"+
			" AND E.ID_ETAPA_PRESTAMO = P.ID_ETAPA_PRESTAMO \n";
			
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		if (registro.getDefCampo("PRESTAMO").equals("GRUPAL")){
		
			sSql =  "SELECT \n"+
					"ID_PRESTAMO \n"+
					"FROM SIM_PRESTAMO_GPO_DET \n"+
					"WHERE CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND ID_PRESTAMO_GRUPO ='" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n";
			PreparedStatement ps1 = this.conn.prepareStatement(sSql);
			ps1.execute();
			ResultSet rs1 = ps1.getResultSet();
			while (rs1.next()){
				registro.addDefCampo("ID_PRESTAMO",rs1.getString("ID_PRESTAMO")== null ? "": rs1.getString("ID_PRESTAMO"));
				
				sSql =  "INSERT INTO SIM_COMITE_PRESTAMO ( \n"+
							"CVE_GPO_EMPRESA, \n" +
							"CVE_EMPRESA, \n" +
							"ID_COMITE, \n"+
							"ID_PRESTAMO, \n"+
							"ID_PRESTAMO_GRUPO ) \n"+
						"VALUES ( \n"+
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_COMITE") + "', \n" +
							"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n"+
							"'" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "') \n";
							
					//VERIFICA SI DIO DE ALTA EL REGISTRO
					if (ejecutaUpdate() == 0){
						resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
					}
					
				sSql = " UPDATE SIM_PRESTAMO SET "+
					   " ID_FOLIO_ACTA     		= '" + (String)registro.getDefCampo("ID_FOLIO_ACTA")  + "', \n" +
					   " ID_COMITE     		= '" + (String)registro.getDefCampo("ID_COMITE")  + "' \n" +
					   " WHERE ID_PRESTAMO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
					   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
				//VERIFICA SI DIO DE ALTA EL REGISTRO
				if (ejecutaUpdate() == 0){
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}	
					
			}
		}else if (registro.getDefCampo("PRESTAMO").equals("INDIVIDUAL")){
		
			sSql =  "INSERT INTO SIM_COMITE_PRESTAMO ( \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_COMITE, \n"+
					"ID_PRESTAMO) \n"+
				"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_COMITE") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "') \n";
					
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			
			sSql = " UPDATE SIM_PRESTAMO SET "+
				   " ID_FOLIO_ACTA     		= '" + (String)registro.getDefCampo("ID_FOLIO_ACTA")  + "', \n" +
				   " ID_COMITE     		= '" + (String)registro.getDefCampo("ID_COMITE")  + "' \n" +
				   " WHERE ID_PRESTAMO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
				   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			//VERIFICA SI DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
		}
		
		return resultadoCatalogo;
	}
	
	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new 	ResultadoCatalogo();
		sSql = " UPDATE SIM_COMITE SET "+
			   " ID_SUCURSAL     		= '" + (String)registro.getDefCampo("ID_SUCURSAL")  + "', \n" +
			   " NOM_COMITE     		= '" + (String)registro.getDefCampo("NOM_COMITE")  + "', \n" +
			   " FECHA                      = TO_DATE('" + (String)registro.getDefCampo("FECHA")  + "','DD/MM/YYYY') \n" +
			   " WHERE ID_COMITE      	= '" + (String)registro.getDefCampo("ID_COMITE") + "' \n" +
			   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
	
	/**
	 * Borra un registro.
	 * @param registro Llave primaria.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		//BORRA EL REGISTRO
		sSql = "DELETE FROM SIM_COMITE" +
				" WHERE ID_COMITE		='" + (String)registro.getDefCampo("ID_COMITE") + "' \n" +
				" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}

}