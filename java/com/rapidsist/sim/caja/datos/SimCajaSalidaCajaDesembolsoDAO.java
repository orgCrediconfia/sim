/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para mostrar movimientos de Caja.
 */
 
public class SimCajaSalidaCajaDesembolsoDAO extends Conexion2 implements OperacionAlta, OperacionConsultaTabla, OperacionConsultaRegistro {

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
				"T.ID_PRESTAMO_GRUPO, \n"+
				"PG.CVE_PRESTAMO_GRUPO, \n"+
				"TO_CHAR(SUM(M.IMP_NETO),'999,999,999.99') MONTO, \n"+
				"T.ID_TRANSACCION_GRUPO, \n"+
				"T.ID_GRUPO, \n"+
				"G.NOM_GRUPO, \n"+
				"T.ID_PRODUCTO, \n"+
				"P.NOM_PRODUCTO, \n"+
				"T.NUM_CICLO, \n"+
				"T.ID_SUCURSAL, \n"+
				"T.ID_CAJA, \n"+
				"T.FECHA_TRANSACCION \n"+
				"FROM \n"+
				"PFIN_MOVIMIENTO M, \n"+
				"SIM_CAJA_TRANSACCION T, \n"+
				"SIM_PRESTAMO_GRUPO PG, \n"+
				"SIM_GRUPO G, \n"+
				"SIM_PRODUCTO P \n"+
				"WHERE M.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND M.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND T.CVE_GPO_EMPRESA = M.CVE_GPO_EMPRESA \n"+
				"AND T.CVE_EMPRESA = M.CVE_EMPRESA \n"+
				"AND T.ID_TRANSACCION_GRUPO = M.ID_GRUPO \n"+
				"AND T.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
				"AND T.ID_CAJA = '" + (String)parametros.getDefCampo("ID_CAJA") + "' \n"+
				"AND T.CVE_MOVIMIENTO_CAJA = 'INGCAJDES' \n"+ 
				"AND PG.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
				"AND PG.CVE_EMPRESA = T.CVE_EMPRESA \n"+
				"AND PG.ID_PRESTAMO_GRUPO = T.ID_PRESTAMO_GRUPO \n"+
				"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
				"AND G.ID_GRUPO = PG.ID_GRUPO \n"+
				"AND P.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
				"AND P.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
				"AND P.ID_PRODUCTO = PG.ID_PRODUCTO \n"+
				"GROUP BY T.CVE_GPO_EMPRESA, T.CVE_EMPRESA, T.ID_PRESTAMO_GRUPO, PG.CVE_PRESTAMO_GRUPO, T.ID_TRANSACCION_GRUPO, T.ID_GRUPO, G.NOM_GRUPO, T.ID_PRODUCTO, P.NOM_PRODUCTO, T.NUM_CICLO, T.ID_SUCURSAL, T.ID_CAJA, T.FECHA_TRANSACCION \n";

		if (parametros.getDefCampo("CVE_PRESTAMO_GRUPO") != null) {
			sSql = sSql + "AND PG.CVE_PRESTAMO_GRUPO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO_GRUPO") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_GRUPO") != null) {
			sSql = sSql + "AND G.NOM_GRUPO = '" + (String) parametros.getDefCampo("NOM_GRUPO") + "' \n";
		}
		
		sSql = sSql + "ORDER BY T.ID_PRESTAMO_GRUPO \n";
		
		System.out.println("solo los que no han salido"+sSql);
		
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
		sSql = " SELECT \n"+
			   " T.CVE_GPO_EMPRESA, \n"+
			   " T.CVE_EMPRESA, \n"+
			   " T.MONTO IMPORTE, \n" + 
			   " TO_CHAR(T.MONTO,'999,999,999.99') MONTO \n" + 
			   " FROM SIM_CAJA_TRANSACCION T \n"+
			   " WHERE T.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			   " AND T.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND T.ID_TRANSACCION_GRUPO = '" + (String)parametros.getDefCampo("ID_TRANSACCION_GRUPO") + "' \n"+
			   " AND T.CVE_MOVIMIENTO_CAJA = 'INGCAJDES' \n";
		ejecutaSql();
		return this.getConsultaRegistro();
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
		
		sMonto = (String)registro.getDefCampo("IMPORTE");
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
				"ID_PRESTAMO_GRUPO, \n" +
				"ID_SUCURSAL, \n" +
				"ID_CAJA, \n" +
				"ID_CLIENTE, \n" +
				"ID_GRUPO, \n" +
				"ID_PRODUCTO, \n" +
				"NUM_CICLO, \n" +
				"CVE_MOVIMIENTO_CAJA, \n" +
				"MONTO, \n" +
				"FECHA_TRANSACCION, \n" +
				"CVE_USUARIO_CAJERO, \n" +
				"ID_TRANSACCION_GRUPO, \n" +
				"USUARIO_RECIBE) \n" +
		        "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdTransaccion + ", \n "+
				"'" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
				"'" + (String)registro.getDefCampo("USUARIO_RECIBE") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
				"'SALCAJDES', \n" +
				"-'" + (String)registro.getDefCampo("IMPORTE") + "', \n" +
				"SYSDATE, \n" +
				"'" + (String)registro.getDefCampo("CVE_USUARIO_CAJERO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_TRANSACCION_GRUPO") + "', \n" +
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