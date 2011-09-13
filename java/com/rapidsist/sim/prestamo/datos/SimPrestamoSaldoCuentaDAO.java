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
					"P.ID_PRESTAMO, \n"+
					"TO_CHAR(A.SDO_EFECTIVO,'999,999,999.99') SALDO_CUENTA, \n"+
					"NVL(A.SDO_EFECTIVO,0) SDO_EFECTIVO, \n"+
					"A.F_FOTO FECHA \n"+
					"FROM \n"+
					"PFIN_SALDO A, \n"+
					"SIM_PRESTAMO P \n"+
					"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				    "AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				    "AND P.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
					"AND A.CVE_GPO_EMPRESA  = P.CVE_GPO_EMPRESA \n"+
					"AND A.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					"AND A.ID_CUENTA = P.ID_CUENTA_REFERENCIA \n"+
					"AND A.CVE_DIVISA = 'MXP' \n"+
					"ORDER BY FECHA DESC \n";
			
			System.out.println("saldo de la cuenta individual¨****************************************"+sSql);
			
		}else if (parametros.getDefCampo("CONSULTA").equals("SALDO_GRUPAL")) {
	
			sSql =  "SELECT \n"+
			      "PG.ID_PRESTAMO_GRUPO, \n"+
			      "TO_CHAR(SUM(NVL(A.SDO_EFECTIVO,0)),'999,999,999.99') SALDO_CUENTA, \n"+
			      "SUM(NVL(A.SDO_EFECTIVO,0)) SDO_EFECTIVO, \n"+
			      "MAX (A.F_FOTO) FECHA FROM \n"+
			      "(SELECT CVE_GPO_EMPRESA, CVE_EMPRESA, ID_CUENTA, F_FOTO, CVE_DIVISA, SDO_EFECTIVO FROM PFIN_SALDO) A, \n"+
			      "SIM_PRESTAMO_GPO_DET PG, \n"+
			      "SIM_PRESTAMO P \n"+
			      "WHERE PG.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			      "AND PG.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			      "AND PG.ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO_GRUPO") + "' \n"+
			      "AND P.CVE_GPO_EMPRESA  = PG.CVE_GPO_EMPRESA  \n"+
			      "AND P.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
			      "AND P.ID_PRESTAMO = PG.ID_PRESTAMO \n"+
			      "AND A.CVE_GPO_EMPRESA  = P.CVE_GPO_EMPRESA  \n"+
			      "AND A.CVE_EMPRESA = P.CVE_EMPRESA \n"+
			      "AND A.ID_CUENTA = P.ID_CUENTA_REFERENCIA \n"+
			      "AND A.CVE_DIVISA = 'MXP' \n"+
			      "GROUP BY PG.ID_PRESTAMO_GRUPO \n";
			
			System.out.println("saldo de la cuenta grupal¨****************************************"+sSql);
			
		}
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
}