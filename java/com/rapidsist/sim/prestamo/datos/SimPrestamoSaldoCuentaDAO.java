/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para obtener la consulta de las cuentas de los clientes individuales y grupales.
 */
 
public class SimPrestamoSaldoCuentaDAO extends Conexion2 implements OperacionConsultaRegistro  {

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		
		if (parametros.getDefCampo("CONSULTA").equals("SALDO_INDIVIDUAL")) {

			sSql =  "SELECT \n"+
					"TO_CHAR(sdo_efectivo,'999,999,999.99') SALDO_CUENTA \n"+
					"from pfin_saldo \n"+
					"where id_cuenta = \n"+
					"(select id_cuenta_referencia \n"+
					"from sim_prestamo \n"+
					"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "') \n";
				
		}else if (parametros.getDefCampo("CONSULTA").equals("SALDO_GRUPAL")) {
			sSql =  "SELECT \n"+
					"pg.id_prestamo_grupo, TO_CHAR(sum(s.sdo_efectivo),'999,999,999.99') SALDO_CUENTA \n"+
					"from sim_prestamo_gpo_det pg, \n"+
					"sim_prestamo p, \n"+
					"pfin_saldo s \n"+
					"where pg.cve_gpo_empresa =  p.cve_gpo_empresa \n"+
					"and pg.cve_empresa =  p.cve_empresa \n"+
					"and pg.id_prestamo = p.id_prestamo \n"+
					"and pg.id_prestamo_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
					"and s.cve_gpo_empresa = p.cve_gpo_empresa \n"+
					"and s.cve_empresa = p.cve_empresa \n"+
					"and s.id_cuenta = p.id_cuenta_referencia group by pg.id_prestamo_grupo \n";
			
		}
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
}