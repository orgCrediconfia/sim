/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para mostrar movimientos de Caja.
 */
 
public class SimCajaConsultaPagarCreditoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro {
	
	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT \n"+
				"C.CVE_GPO_EMPRESA, \n"+
				"C.CVE_EMPRESA, \n"+
				"C.ID_PRESTAMO, \n"+
				"C.CVE_PRESTAMO, \n"+
				"C.NOMBRE, \n"+
				"C.APLICA_A, \n"+
				"C.ID_ETAPA_PRESTAMO \n"+
				"FROM V_CREDITO C, \n"+
				"     SIM_CAT_ETAPA_PRESTAMO E, \n"+
				"     SIM_USUARIO_SUCURSAL US \n"+
				"WHERE C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
				"AND C.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n" +
				"AND C.APLICA_A = '" + (String)parametros.getDefCampo("APLICA_A") + "' \n" +
				"AND E.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
				"AND E.CVE_EMPRESA = C.CVE_EMPRESA \n"+
				"AND E.ID_ETAPA_PRESTAMO = C.ID_ETAPA_PRESTAMO \n"+
				"AND E.B_ENTREGADO = 'V' \n" +
				"AND US.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
	            "AND US.CVE_EMPRESA = C.CVE_EMPRESA \n"+
	            "AND US.ID_SUCURSAL = C.ID_SUCURSAL \n"+
	            "AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n";
		
						
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + "AND C.CVE_PRESTAMO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
		}
		if (parametros.getDefCampo("NOMBRE") != null) {
			sSql = sSql + "AND C.NOMBRE LIKE '%" + (String) parametros.getDefCampo("NOMBRE") + "%' \n";
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
		sSql =  "SELECT \n"+
				"C.CVE_GPO_EMPRESA, \n"+
				"C.CVE_EMPRESA, \n"+
				"C.ID_PRESTAMO, \n"+
				"C.CVE_PRESTAMO, \n"+
				"C.NOMBRE, \n"+
				"C.ID_PRODUCTO, \n"+
				"C.NOM_PRODUCTO, \n"+
				"C.NUM_CICLO, \n"+
				"C.APLICA_A, \n"+
				"C.ID_ETAPA_PRESTAMO \n"+
				"FROM V_CREDITO C, \n"+
				"     SIM_CAT_ETAPA_PRESTAMO E \n"+
				"WHERE C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				"AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
				"AND C.ID_PRESTAMO = '" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n" +
				"AND C.APLICA_A = '" + (String)parametros.getDefCampo("APLICA_A") + "' \n" +
				"AND E.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
				"AND E.CVE_EMPRESA = C.CVE_EMPRESA \n"+
				"AND E.ID_ETAPA_PRESTAMO = C.ID_ETAPA_PRESTAMO \n"+
				"AND E.B_ENTREGADO = 'V' \n" ;
		
		System.out.println("que jaislllllllllllllllll"+sSql);
				
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}