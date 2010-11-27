/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;

/**
 * Administra los accesos a la base de datos para:
 * 1. Obtener el siguiente ciclo del crédito grupal.
 */
 
public class SimPrestamoCicloSiguienteDAO extends Conexion2 implements OperacionConsultaRegistro {

	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		
		String sCiclo = "";
		
		sSql =  "SELECT \n"+
				"NVL(MAX (NUM_CICLO) + 1,0) NUMERO_CICLO \n"+
				"FROM ( \n"+
				"SELECT \n"+
				"G.ID_PRESTAMO_GRUPO, \n"+
				"G.NUM_CICLO \n"+
				"FROM SIM_PRESTAMO_GRUPO G, \n"+
				"     SIM_CAT_ETAPA_PRESTAMO E \n"+
				"WHERE G.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND G.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND G.ID_GRUPO = '" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
				"AND G.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
				"AND E.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+ 
				"AND E.CVE_EMPRESA = G.CVE_EMPRESA \n"+ 
				"AND E.ID_ETAPA_PRESTAMO = G.ID_ETAPA_PRESTAMO \n"+ 
				"AND E.B_CANCELADO != 'V' \n"+
				") \n";
		
		ejecutaSql();
		
		if (rs.next()){
			
			sCiclo = rs.getString("NUMERO_CICLO");
			
			if (sCiclo.equals("0")){
			
			sSql =  "SELECT \n"+
					"MAX (NUM_CICLO) + 1 NUMERO_CICLO \n"+
					"FROM ( \n"+
					"SELECT \n"+
					"NUM_CICLO \n"+
					"FROM SIM_PRESTAMO_EXCEPCION_CICLO \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND ID_GRUPO = '" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
					"AND ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
					") \n";
			
			}else {
				sSql =  "SELECT \n"+
						"MAX (NUM_CICLO) + 1 NUMERO_CICLO \n"+
						"FROM ( \n"+
						"SELECT \n"+
						"G.ID_PRESTAMO_GRUPO, \n"+
						"G.NUM_CICLO \n"+
						"FROM SIM_PRESTAMO_GRUPO G, \n"+
						"     SIM_CAT_ETAPA_PRESTAMO E \n"+
						"WHERE G.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						"AND G.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
						"AND G.ID_GRUPO = '" + (String)parametros.getDefCampo("ID_GRUPO") + "' \n"+
						"AND G.ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
						"AND E.CVE_GPO_EMPRESA = G.CVE_GPO_EMPRESA \n"+ 
						"AND E.CVE_EMPRESA = G.CVE_EMPRESA \n"+ 
						"AND E.ID_ETAPA_PRESTAMO = G.ID_ETAPA_PRESTAMO \n"+ 
						"AND E.B_CANCELADO != 'V' \n"+
						") \n";
			}
		}
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}