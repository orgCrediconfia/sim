/**
 * Sistema de administraci�n de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para obtenr el identificador del pr�stamo en base a su clave.
 */
 
public class SimGiroPrestamoDAO extends Conexion2 implements OperacionConsultaRegistro {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de b�squeda.
	 * @param parametros Par�metros que se le env�an a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
	
		sSql =  "SELECT \n"+
		  "CC.NOM_CLASE AS GIRO\n"+
		"FROM SIM_PRESTAMO SP,\n"+
		  "SIM_CLIENTE_NEGOCIO CN,\n"+
		  "SIM_CAT_CLASE CC\n"+
		"WHERE SP.CVE_GPO_EMPRESA  = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
		  "AND SP.CVE_EMPRESA      = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
		  "AND SP.ID_PRESTAMO      = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
		  "AND CN.CVE_GPO_EMPRESA  = SP.CVE_GPO_EMPRESA\n"+
		  "AND CN.CVE_EMPRESA      = SP.CVE_EMPRESA\n"+
		  "AND CN.ID_PERSONA       = SP.ID_CLIENTE\n"+
		  "AND CN.B_PRINCIPAL      = 'V'\n"+
		  "AND CC.CVE_GPO_EMPRESA  = CN.CVE_GPO_EMPRESA\n"+
		  "AND CC.CVE_EMPRESA      = CN.CVE_EMPRESA\n"+
		  "AND CC.CVE_CLASE        = CN.CVE_CLASE\n";

		
		System.out.println("Muestra consulta: " + sSql);
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	

}