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
 
public class SimCajaCancelacionPagoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta {
	
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
				"T.CVE_GPO_EMPRESA, \n"+
				"T.CVE_EMPRESA, \n"+
				"T.ID_TRANSACCION, \n"+
				"T.ID_SUCURSAL, \n"+
				"T.ID_CAJA, \n"+
				"T.CVE_MOVIMIENTO_CAJA, \n"+
				"T.MONTO, \n"+
				"T.FECHA_CANCELACION, \n"+
				"T.CVE_USUARIO_CAJERO, \n"+
				"T.ID_PRESTAMO, \n"+
				"T.ID_PRESTAMO_GRUPO, \n"+
				"T.ID_GRUPO, \n"+
				"G.NOM_GRUPO, \n"+
				"T.ID_PRODUCTO, \n"+
				"O.NOM_PRODUCTO, \n"+
				"T.NUM_CICLO, \n"+
				"T.ID_CLIENTE, \n"+
				"P.NOM_COMPLETO \n"+
				"FROM \n"+
				"SIM_CAJA_TRANSACCION T, \n"+
				"RS_GRAL_PERSONA P, \n"+
				"SIM_GRUPO G, \n"+
				"SIM_PRODUCTO O \n"+
				"WHERE T.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND T.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND (T.CVE_MOVIMIENTO_CAJA = 'PAGOIND' OR T.CVE_MOVIMIENTO_CAJA = 'PAGOGPO') \n"+
				"AND T.ID_CAJA = '" + (String)parametros.getDefCampo("ID_CAJA") + "' \n"+
				"AND T.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
				//"AND T.FECHA_CANCELACION IS NULL \n"+
				"AND P.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
				"AND P.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
				"AND P.ID_PERSONA (+)= T.ID_CLIENTE \n"+
				"AND G.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
				"AND G.ID_GRUPO (+)= T.ID_GRUPO \n"+
				"AND O.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
				"AND O.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
				"AND O.ID_PRODUCTO (+)= T.ID_PRODUCTO \n"+
				"AND T.FECHA_TRANSACCION =   (SELECT  F_MEDIO \n"+
				"					        FROM    PFIN_PARAMETRO \n"+
				"					        WHERE   CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"					            AND CVE_EMPRESA     = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"					            AND CVE_MEDIO       = 'SYSTEM') \n";
				//"AND FECHA_TRANSACCION = SYSDATE \n";
				
		if (parametros.getDefCampo("ID_PRESTAMO") != null) {
			sSql = sSql + "AND ID_PRESTAMO = '" + (String) parametros.getDefCampo("ID_PRESTAMO") + "' \n";
		}
		if (parametros.getDefCampo("ID_PRODUCTO") != null) {
			sSql = sSql + "AND ID_PRODUCTO = '" + (String) parametros.getDefCampo("ID_PRODUCTO") + "' \n";
		}
		if (parametros.getDefCampo("NUM_CICLO") != null) {
			sSql = sSql + "AND NUM_CICLO = '" + (String) parametros.getDefCampo("NUM_CICLO") + "' \n";
		}
		if (parametros.getDefCampo("ID_GRUPO") != null) {
			sSql = sSql + "AND ID_GRUPO = '" + (String) parametros.getDefCampo("ID_GRUPO") + "' \n";
		}
		
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
		
		sSql =  "SELECT \n"+
				"CVE_GPO_EMPRESA, \n"+
				"CVE_EMPRESA, \n"+
				"ID_TRANSACCION, \n"+
				"ID_SUCURSAL, \n"+
				"ID_CAJA, \n"+
				"CVE_MOVIMIENTO_CAJA, \n"+
				"MONTO, \n"+
				"FECHA_TRANSACCION, \n"+
				"CVE_USUARIO_CAJERO, \n"+
				"ID_PRESTAMO, \n"+
				"ID_PRESTAMO_GRUPO, \n"+
				"ID_GRUPO, \n"+
				"ID_CLIENTE, \n"+
				"ID_PRODUCTO, \n"+
				"NUM_CICLO \n"+
				"FROM \n"+
				"SIM_CAJA_TRANSACCION \n"+
				"WHERE CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND ID_TRANSACCION = '" + (String)registro.getDefCampo("ID_TRANSACCION") + "' \n"+
				"AND CVE_MOVIMIENTO_CAJA = '" + (String)registro.getDefCampo("CVE_MOVIMIENTO_PAGO") + "' \n"+
				//"AND ID_CAJA = '" + (String)registro.getDefCampo("ID_CAJA") + "' \n"+
				//"AND ID_SUCURSAL = '" + (String)registro.getDefCampo("ID_SUCURSAL") + "' \n"+
				"AND FECHA_CANCELACION IS NULL \n";
		ejecutaSql();
		if (rs.next()){
			registro.addDefCampo("ID_PRESTAMO",rs.getString("ID_PRESTAMO") == null ? "" : rs.getString("ID_PRESTAMO"));
			registro.addDefCampo("ID_PRESTAMO_GRUPO",rs.getString("ID_PRESTAMO_GRUPO") == null ? "" : rs.getString("ID_PRESTAMO_GRUPO"));
			registro.addDefCampo("ID_PRODUCTO",rs.getString("ID_PRODUCTO"));
			registro.addDefCampo("NUM_CICLO",rs.getString("NUM_CICLO"));
			registro.addDefCampo("ID_CLIENTE",rs.getString("ID_CLIENTE") == null ? "" : rs.getString("ID_CLIENTE"));
			registro.addDefCampo("ID_GRUPO",rs.getString("ID_GRUPO") == null ? "" : rs.getString("ID_GRUPO"));
			registro.addDefCampo("ID_CAJA",rs.getString("ID_CAJA") == null ? "" : rs.getString("ID_CAJA"));
			registro.addDefCampo("ID_SUCURSAL",rs.getString("ID_SUCURSAL") == null ? "" : rs.getString("ID_SUCURSAL"));
			registro.addDefCampo("MONTO",rs.getString("MONTO") == null ? "" : rs.getString("MONTO"));
		}
		
		String sIdTransaccion = "";
		int iIdTransaccion = 0;
		
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
				"ID_PRESTAMO, \n"+
				"ID_PRESTAMO_GRUPO, \n"+
				"ID_GRUPO, \n"+
				"ID_CLIENTE, \n"+
				"ID_PRODUCTO, \n"+
				"NUM_CICLO, \n"+
				"MONTO, \n" +
				"CVE_USUARIO_CAJERO, \n" +
				"FECHA_TRANSACCION, \n" +
				"FECHA_CANCELACION) \n" +
		        "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdTransaccion + ", \n "+
				"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
				"'CANPAGO', \n" +
				"'" + (String)registro.getDefCampo("ID_PRESTAMO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_CLIENTE") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
				"-'" + (String)registro.getDefCampo("MONTO") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_USUARIO") + "', \n" +
				"SYSDATE, \n" +
				"SYSDATE) \n" ;
		
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
	
		sSql = "UPDATE SIM_CAJA_TRANSACCION SET \n" +
				"FECHA_CANCELACION = SYSDATE \n"+
				"WHERE  CVE_GPO_EMPRESA ='" + (String) registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND CVE_EMPRESA ='" + (String) registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND ID_TRANSACCION ='" + (String) registro.getDefCampo("ID_TRANSACCION") + "' \n"+
				" AND CVE_MOVIMIENTO_CAJA ='" + (String) registro.getDefCampo("CVE_MOVIMIENTO_PAGO") + "' \n";
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		
		return resultadoCatalogo;
	}
}