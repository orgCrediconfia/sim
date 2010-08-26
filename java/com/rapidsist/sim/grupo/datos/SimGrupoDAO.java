/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.grupo.datos;

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
 * Administra los accesos a la base de datos para el grupo.
 */
 
public class SimGrupoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		
		//Consulta los grupos existentes.
		sSql =  " SELECT \n"+
				" G.CVE_GPO_EMPRESA, \n" +
				" G.CVE_EMPRESA, \n" +
				" G.ID_GRUPO, \n"+
				" G.NOM_GRUPO, \n" +
				" G.FECHA_FORMACION \n" +
				" FROM SIM_GRUPO G, \n"+
				" SIM_USUARIO_ACCESO_SUCURSAL US \n"+
				" WHERE G.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND G.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND US.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+
	            " AND US.CVE_EMPRESA = G.CVE_EMPRESA \n"+
	            " AND US.ID_SUCURSAL = G.ID_SUCURSAL \n"+
	            " AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n"+
				" AND G.FECHA_BAJA_LOGICA IS NULL  \n";
				
		if (parametros.getDefCampo("ID_GRUPO") != null) {
			sSql = sSql + " AND G.ID_GRUPO = '" + (String) parametros.getDefCampo("ID_GRUPO") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_GRUPO") != null) {
			sSql = sSql + " AND UPPER(G.NOM_GRUPO) LIKE'%" + ((String) parametros.getDefCampo("NOM_GRUPO")).toUpperCase()  + "%' \n";
		}
		
		if (parametros.getDefCampo("FECHA_FORMACION") != null) {
			sSql = sSql + " AND G.FECHA_FORMACION = TO_DATE('" + (String) parametros.getDefCampo("FECHA_FORMACION") + "','DD/MM/YYYY') \n";
		}
		
		sSql = sSql + "ORDER BY G.ID_GRUPO \n";
		
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
	
		//Consulta los datos del grupo.
		sSql = " SELECT \n"+
			   " G.CVE_GPO_EMPRESA, \n" +
			   " G.CVE_EMPRESA, \n" +
			   " G.ID_GRUPO, \n"+
			   " G.NOM_GRUPO, \n" +
			   " G.FECHA_FORMACION, \n" +
			   " G.ID_SUCURSAL, \n"+
			   " G.ID_COORDINADOR, \n" +
			   " NC.NOM_COMPLETO NOMBRE_COORDINADOR \n" +
			   " FROM SIM_GRUPO G, \n"+
			   "      RS_GRAL_PERSONA NC \n"+
			   " WHERE G.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND G.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND G.ID_GRUPO = '" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
			   " AND G.CVE_GPO_EMPRESA = NC.CVE_GPO_EMPRESA (+) \n"+
			   " AND G.CVE_EMPRESA = NC.CVE_EMPRESA (+) \n"+
			   " AND G.ID_COORDINADOR = NC.ID_PERSONA (+) \n";
			 
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
		resultadoCatalogo.Resultado = new Registro();
		
		String sIdGrupo = "";
		
		sSql = "SELECT SQ01_SIM_GRUPO.nextval AS ID_GRUPO FROM DUAL";
		
		ejecutaSql();
		if (rs.next()){
			sIdGrupo = rs.getString("ID_GRUPO");
		}
		
		//Da de alta el grupo.
		sSql =  "INSERT INTO SIM_GRUPO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_GRUPO, \n"+
				"NOM_GRUPO, \n" +
				"FECHA_FORMACION, \n" +
				"ID_SUCURSAL) \n"+		
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdGrupo + ", \n" +
				"'" + (String)registro.getDefCampo("NOM_GRUPO") + "', \n" +
				"TO_DATE('" + (String)registro.getDefCampo("FECHA_FORMACION") + "','DD/MM/YYYY HH24:MI:SS'), \n" +
				"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "') \n" ;
				
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		resultadoCatalogo.Resultado.addDefCampo("ID_GRUPO", sIdGrupo);
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
		resultadoCatalogo.Resultado = new Registro();
		
		//Actualiza los datos generales del grupo.
		sSql = " UPDATE SIM_GRUPO SET "+
			   " NOM_GRUPO     		= '" + (String)registro.getDefCampo("NOM_GRUPO")  + "', \n" +
			   " FECHA_FORMACION    	= TO_DATE('" + (String)registro.getDefCampo("FECHA_FORMACION")  + "','DD/MM/YYYY'), \n" +
			   " ID_SUCURSAL    		= '" + (String)registro.getDefCampo("ID_SUCURSAL")  + "', \n" +
			   " ID_COORDINADOR     	= '" + (String)registro.getDefCampo("ID_COORDINADOR")  + "' \n" +
			   " WHERE ID_GRUPO      	= '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
			   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		//Obtiene los préstamos grupales que tenga o a tenido ese grupo.
		sSql =  " SELECT \n" +
				" CVE_PRESTAMO_GRUPO, \n" +
				" ID_PRESTAMO_GRUPO \n" +
				" FROM SIM_PRESTAMO_GRUPO \n" +
				" WHERE ID_GRUPO      	= '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
				" AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
		
		PreparedStatement ps1 = this.conn.prepareStatement(sSql);
		ps1.execute();
		ResultSet rs1 = ps1.getResultSet();
		
		if (rs1.next()){
			registro.addDefCampo("CVE_PRESTAMO_GRUPO",rs1.getString("CVE_PRESTAMO_GRUPO"));
			registro.addDefCampo("ID_PRESTAMO_GRUPO",rs1.getString("ID_PRESTAMO_GRUPO"));
			//Obtiene los prestamos individuales.
			sSql =  " SELECT \n" +
					" ID_PRESTAMO \n" +
					" FROM SIM_PRESTAMO_GPO_DET \n" +
					" WHERE ID_PRESTAMO_GRUPO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n" +
					" AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
					" AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
		
			PreparedStatement ps2 = this.conn.prepareStatement(sSql);
			ps2.execute();
			ResultSet rs2 = ps2.getResultSet();
			
			while (rs2.next()){
				registro.addDefCampo("ID_PRESTAMO",rs2.getString("ID_PRESTAMO"));
				//Verfica si el grupo cambio la sucursal, en tal caso actualiza el crédito individual.
				sSql =  " SELECT \n" +
						" ID_PRESTAMO \n" +
						" FROM SIM_PRESTAMO \n" +
						" WHERE ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
						" AND ID_SUCURSAL   	= '" + (String)registro.getDefCampo("ID_SUCURSAL") + "' \n" +
						" AND CVE_GPO_EMPRESA   = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
						" AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
				
				PreparedStatement ps3 = this.conn.prepareStatement(sSql);
				ps3.execute();
				ResultSet rs3 = ps3.getResultSet();
				if (!rs3.next()){
					
					sSql =  " UPDATE SIM_PRESTAMO SET \n" +
							" ID_SUCURSAL = '" + (String)registro.getDefCampo("ID_SUCURSAL") + "' \n" +
							" WHERE ID_PRESTAMO     = '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
							" AND CVE_GPO_EMPRESA   = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							" AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
					
					PreparedStatement ps4 = this.conn.prepareStatement(sSql);
					ps4.execute();
					ResultSet rs4 = ps4.getResultSet();
				}
			}
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
		
		//Da de baja el grupo.
		sSql = " UPDATE SIM_GRUPO SET "+
		 	   " FECHA_BAJA_LOGICA     	= SYSDATE \n" +
		 	   " WHERE ID_GRUPO     ='" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
			   " AND CVE_GPO_EMPRESA    ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND CVE_EMPRESA        ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}

}