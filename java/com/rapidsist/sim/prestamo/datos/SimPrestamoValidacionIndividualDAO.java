/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para validar que exista un garante, al menos un obligado y una garantia en los préstamos individuales.
 */
 
public class SimPrestamoValidacionIndividualDAO extends Conexion2 implements OperacionConsultaRegistro {
	
	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  " SELECT \n"+
				"	P.CVE_GPO_EMPRESA, \n"+
				"	P.CVE_EMPRESA, \n"+
				"	P.ID_PRESTAMO, \n"+
				"	PO.ID_PRODUCTO, \n"+
				"	G.ID_PERSONA GARANTE, \n"+
				"	O.ID_PERSONA OBLIGADO, \n"+
				"	GA.ID_GARANTIA GARANTIA \n"+
				"	FROM SIM_PRESTAMO P, \n"+
				"	SIM_PRODUCTO PO, \n"+
				"	SIM_PRESTAMO_PARTICIPANTE G, \n"+
				"	SIM_PRESTAMO_PARTICIPANTE O, \n"+
				"	SIM_PRESTAMO_GARANTIA GA \n"+
				"	WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"	AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
				"	AND P.ID_GRUPO IS NULL \n"+
				"	AND P.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n" +
				"	AND PO.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"	AND PO.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"	AND PO.ID_PRODUCTO = P.ID_PRODUCTO \n"+
				"	AND PO.ID_PRODUCTO != '11' \n"+
				"	AND G.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"	AND G.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"	AND G.ID_PRESTAMO (+)= P.ID_PRESTAMO \n"+
				"	AND G.CVE_TIPO_PERSONA (+)= 'GARANTE' \n"+
				"	AND O.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"	AND O.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				"	AND O.ID_PRESTAMO (+)= P.ID_PRESTAMO \n"+
				"	AND O.CVE_TIPO_PERSONA LIKE 'OBLIGADO' \n"+
				"	AND GA.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				"	AND GA.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+ 
				"	AND GA.ID_PRESTAMO (+)= P.ID_PRESTAMO \n";
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}