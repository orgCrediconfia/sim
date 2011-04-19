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
 * Administra los accesos a la base de datos para consultar el día anterior valido a la fecha del medio.
 */
 
public class SimPrestamoDiaAnteriorValidoDAO extends Conexion2 implements OperacionConsultaRegistro {
	

	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		
		sSql = "SELECT TO_DATE(F_LIQUIDACION,'DD-MM-YYYY') AS F_MEDIO_ANTERIOR \n"+
		       "FROM PFIN_DIA_LIQUIDACION \n"+
		       "WHERE CVE_GPO_EMPRESA  = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
		       "AND CVE_EMPRESA      = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
		       "AND CVE_LIQUIDACION  = 'AYER' \n"+   
		       "AND F_INFORMACION    = (SELECT F_MEDIO \n"+
		       "		 				FROM PFIN_PARAMETRO \n"+
		       "		 				WHERE CVE_GPO_EMPRESA  = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
		       "		 				AND CVE_EMPRESA      = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "') \n";
						
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}