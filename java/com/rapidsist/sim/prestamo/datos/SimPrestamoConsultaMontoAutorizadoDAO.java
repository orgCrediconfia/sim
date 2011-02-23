/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.prestamo.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.configuracion.Usuario;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ListIterator;
import java.util.Iterator;

/**
 * Administra los accesos a la base de datos para obtener los montos autorizados de un préstamo.
 */
 
public class SimPrestamoConsultaMontoAutorizadoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro  {
	

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
	
		if (parametros.getDefCampo("TOTAL").equals("NO")) {
		
			sSql =  "SELECT \n"+
			   "M.CVE_GPO_EMPRESA, \n" +
			   "M.CVE_EMPRESA, \n" +
			   "M.ID_PRESTAMO_GRUPO, \n"+
			   "M.ID_PRESTAMO, \n"+
			   "M.ID_INTEGRANTE ID_CLIENTE, \n"+
			   "PER.NOM_COMPLETO, \n"+
			   "M.MONTO_SOLICITADO, \n"+
			   "TO_CHAR(M.MONTO_AUTORIZADO,'999,999,999.99') MONTO_AUTORIZADO \n"+
			" FROM SIM_PRESTAMO_GPO_DET M, \n"+
			"		RS_GRAL_PERSONA PER \n"+
			" WHERE M.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND M.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND M.ID_PRESTAMO_GRUPO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			" AND PER.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA \n"+
			" AND PER.CVE_EMPRESA = M.CVE_EMPRESA \n"+
			" AND PER.ID_PERSONA = M.ID_INTEGRANTE \n"+
			" AND M.ID_ETAPA_PRESTAMO != '31' \n";
			
		}else if (parametros.getDefCampo("TOTAL").equals("SI")) {
		
			sSql =  "SELECT \n"+
			"TO_CHAR(C.MONTO_AUTORIZADO + C.CARGO_INICIAL,'999,999,999.99') TOTAL \n"+
			"FROM V_CREDITO C \n"+
			"WHERE C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND C.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
			"AND C.APLICA_A = 'GRUPO' \n"+
			"AND C.ID_ETAPA_PRESTAMO != '31' \n";
			
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
		
		if (parametros.getDefCampo("CONSULTA").equals("CARGO_INICIAL")) {
			sSql =  "SELECT \n"+
					"P.CVE_GPO_EMPRESA, \n"+
					"P.CVE_EMPRESA, \n"+
					"P.ID_PRESTAMO_GRUPO, \n"+
					"TO_CHAR(SUM(C.CARGO_INICIAL),'999,999,999.99') CARGO_INICIAL \n"+
					"FROM SIM_PRESTAMO_GPO_DET P, \n"+
					"SIM_PRESTAMO_GPO_CARGO C \n"+
					"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND P.ID_PRESTAMO_GRUPO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
					"AND C.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
					"AND C.CVE_EMPRESA = P.CVE_EMPRESA \n"+
					"AND C.ID_PRESTAMO_GRUPO = P.ID_PRESTAMO_GRUPO \n"+
					"AND C.ID_FORMA_APLICACION = 1 \n"+
					"GROUP BY  \n"+
					"P.CVE_GPO_EMPRESA, \n"+
					"P.CVE_EMPRESA, \n"+
					"P.ID_PRESTAMO_GRUPO \n";
		}else if (parametros.getDefCampo("CONSULTA").equals("CAJA")) {
			sSql =  "SELECT \n"+
					"SC.CVE_GPO_EMPRESA, \n"+
					"SC.CVE_EMPRESA, \n"+
					"SC.ID_SUCURSAL, \n"+
					"S.NOM_SUCURSAL, \n"+
					"'CAJA '||SC.ID_CAJA CAJA \n"+
					"FROM SIM_SUCURSAL_CAJA SC, \n"+
					"     SIM_CAT_SUCURSAL S \n"+
					"WHERE SC.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
					"AND SC.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
					"AND SC.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
					"AND SC.ID_CAJA = '" + (String)parametros.getDefCampo("ID_CAJA") + "' \n"+
					"AND S.CVE_GPO_EMPRESA = SC.CVE_GPO_EMPRESA \n"+
					"AND S.CVE_EMPRESA = SC.CVE_EMPRESA \n"+
					"AND S.ID_SUCURSAL = SC.ID_SUCURSAL \n";
			
		}
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
}