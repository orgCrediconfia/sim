/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
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
 
public class SimCajaTranspasoBancoDAO extends Conexion2 implements OperacionAlta, OperacionConsultaTabla {

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
				"T.CVE_GPO_EMPRESA, \n" +
				"T.CVE_EMPRESA, \n" +
				"T.ID_TRANSACCION, \n" +
				"T.CVE_MOVIMIENTO_CAJA, \n" +
				"T.ID_SUCURSAL, \n" +
				"T.ID_CAJA, \n" +
				"T.FUENTE_DESTINO, \n" +
				"B.NOM_BANCO, \n" +
				"T.MONTO, \n" +
				"T.FECHA_REGISTRO, \n" +
				"T.FECHA_TRANSACCION, \n" +
				"T.CVE_USUARIO_CAJERO \n" +
				"FROM SIM_CAJA_TRANSACCION T, \n" +
				"SIM_CAT_BANCO B \n" +
				"WHERE T.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND T.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND T.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
				"AND T.ID_CAJA = '" + (String)parametros.getDefCampo("ID_CAJA") + "' \n"+
				"AND T.CVE_MOVIMIENTO_CAJA = 'TRABAN' \n"+
				"AND B.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
				"AND B.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
				"AND B.CVE_BANCO (+)= T.FUENTE_DESTINO \n";
				
		
		if (parametros.getDefCampo("FECHA") != null) {
			sSql = sSql + " AND TO_CHAR(T.FECHA_TRANSACCION,'DD/MM/YYYY') = '" + (String) parametros.getDefCampo("FECHA") + "' \n";
		}
		if (parametros.getDefCampo("FUENTE_DESTINO") != null) {
			sSql = sSql + " AND T.FUENTE_DESTINO = '" + (String) parametros.getDefCampo("FUENTE_DESTINO") + "' \n";
		}
		if (parametros.getDefCampo("MONTO") != null) {
			sSql = sSql + " AND -T.MONTO = '" + (String) parametros.getDefCampo("MONTO") + "' \n";
		}
		
		sSql = sSql + "ORDER BY T.ID_TRANSACCION \n";
		
		ejecutaSql();
		return getConsultaLista();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sIdTransaccion = "";
		int iIdTransaccion = 0;
		String sMontoCaja = "";
		float fMontoCaja = 0;
		String sMonto = "";
		float fMonto = 0;
		
		sSql =  "SELECT \n" +
				"SUM(MONTO) MONTO_CAJA \n" +
				"FROM \n" +
				"SIM_CAJA_TRANSACCION \n" +
				"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND ID_CAJA = '" + (String)registro.getDefCampo("ID_CAJA") + "' \n"+
				"AND ID_SUCURSAL = '" + (String)registro.getDefCampo("ID_SUCURSAL") + "' \n";
		ejecutaSql();
		if (rs.next()){
			sMontoCaja = rs.getString("MONTO_CAJA");
			
			if (sMontoCaja == null){
				sMontoCaja = "0";
			}
			
			fMontoCaja = Float.parseFloat(sMontoCaja.trim());
		}
		
		sMonto = (String)registro.getDefCampo("MONTO");
		fMonto = Float.parseFloat(sMonto.trim());
		
		if (fMontoCaja < fMonto){
			resultadoCatalogo.mensaje.setClave("FONDO_INSUFICIENTE");
		}else {
		//OBTENEMOS EL SEQUENCE
		sSql =  "SELECT \n" +
				"CVE_GPO_EMPRESA, \n" +
				"MAX(ID_TRANSACCION) ID_TRANSACCION \n" +
				"FROM \n" +
				"SIM_CAJA_TRANSACCION \n" +
				"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND CVE_MOVIMIENTO_CAJA = '" + (String)registro.getDefCampo("CVE_MOVIMIENTO_CAJA") + "' \n"+
				"GROUP BY CVE_GPO_EMPRESA \n";
		ejecutaSql();
			
		if (rs.next()){
		
			sIdTransaccion = rs.getString("ID_TRANSACCION");
			iIdTransaccion=Integer.parseInt(sIdTransaccion.trim());
			iIdTransaccion ++;
			sIdTransaccion= String.valueOf(iIdTransaccion);
		}else {
		
			sIdTransaccion = "1";
		}
		
		sSql =  "INSERT INTO SIM_CAJA_TRANSACCION ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_TRANSACCION, \n" +
				"ID_SUCURSAL, \n" +
				"ID_CAJA, \n" +
				"CVE_MOVIMIENTO_CAJA, \n" +
				"MONTO, \n" +
				"FECHA_TRANSACCION, \n" +
				"CVE_USUARIO_CAJERO, \n" +
				"FUENTE_DESTINO, \n" +
				"USUARIO_RECIBE) \n" +
		        "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdTransaccion + ", \n "+
				"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_MOVIMIENTO_CAJA") + "', \n" +
				"-'" + (String)registro.getDefCampo("MONTO") + "', \n" +
				"SYSDATE, \n" +
				"'" + (String)registro.getDefCampo("CVE_USUARIO_CAJERO") + "', \n" +
				"'" + (String)registro.getDefCampo("FUENTE_DESTINO") + "', \n" +
				"'" + (String)registro.getDefCampo("USUARIO_RECIBE") + "') \n" ;
		
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		resultadoCatalogo.Resultado.addDefCampo("ID_TRANSACCION", sIdTransaccion);
		}
		return resultadoCatalogo;
	}
}