/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.OperacionAlta; 
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;


/**
 * Administra los accesos a la base de datos para los productos asigandos a los préstamos.
 */
 
public class SimPrestamoActividadRequisitoHistoricoDAO extends Conexion2 implements OperacionAlta {

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		sSql = "SELECT SQ01_SIM_PRESTAMO_ACT_REQ_HIST.nextval AS ID_HISTORICO FROM DUAL";
		
		PreparedStatement ps6 = this.conn.prepareStatement(sSql);
		ps6.execute();
		ResultSet rs6 = ps6.getResultSet();	
		if (rs6.next()){
			registro.addDefCampo("ID_HISTORICO",rs6.getString("ID_HISTORICO"));
			
			sSql = "INSERT INTO SIM_PRESTAMO_ETAPA_HISTORICO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_HISTORICO, \n" +
				"ID_PRESTAMO, \n" +
				"ID_ACTIVIDAD_REQUISITO, \n" +
				"ID_ETAPA_PRESTAMO, \n"+
				"ORDEN_ETAPA, \n"+
				"ESTATUS) \n" +
				" VALUES (" +
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_HISTORICO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_ACTIVIDAD_REQUISITO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_ETAPA_PRESTAMO") + "', \n" +
				"'" + (String)registro.getDefCampo("ORDEN_ETAPA") + "', \n" +
				"'Registrada') \n" ;
				
			PreparedStatement ps7 = this.conn.prepareStatement(sSql);
			ps7.execute();
			ResultSet rs7 = ps7.getResultSet();
		}
		
	
		return resultadoCatalogo;
	}
}