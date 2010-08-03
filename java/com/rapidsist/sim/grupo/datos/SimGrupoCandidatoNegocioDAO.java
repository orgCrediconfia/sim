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
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * Administra los accesos a la base de datos de los candidatos al grupo
 */
 
public class SimGrupoCandidatoNegocioDAO extends Conexion2 implements OperacionAlta {

	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		String sIdCandidato = "";
		
		sSql =  "SELECT ID_CANDIDATO \n" +
			"FROM SIM_GRUPO_CANDIDATO  \n"+
			"WHERE \n"+
			"CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" +
			"AND ID_GRUPO = '" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" ;
		ejecutaSql();			
							
		while (rs.next()){				
			sIdCandidato = rs.getString("ID_CANDIDATO");
								
			sSql =  "INSERT INTO SIM_GRUPO_INTEGRANTE ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_GRUPO, \n"+
				"ID_INTEGRANTE) \n"+
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
				"'" + sIdCandidato + "') \n";
									
			PreparedStatement ps2 = this.conn.prepareStatement(sSql);
			ps2.execute();
			ps2.close();	
		}
		
		
		sSql =  " DELETE FROM SIM_GRUPO_CANDIDATO "+
			" WHERE ID_GRUPO		='" + (String)registro.getDefCampo("ID_GRUPO") + "' \n" +
			" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
				
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
			
		return resultadoCatalogo;
	}
}