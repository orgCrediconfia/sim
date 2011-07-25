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
 
public class SimCajaIngresoCajaDesembolsarDAO extends Conexion2 implements OperacionAlta, OperacionConsultaTabla {

	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql = "SELECT \n"+
				"GD.ID_PRESTAMO_GRUPO, \n"+ 
				"PG.CVE_PRESTAMO_GRUPO, \n"+
				"'GRUPAL' APLICA_A, \n"+
				"'' NOM_COMPLETO, \n"+
				"G.ID_GRUPO, \n"+
				"G.NOM_GRUPO, \n"+
				"PG.ID_PRODUCTO, \n"+
				"PRO.NOM_PRODUCTO, \n"+
				"PG.NUM_CICLO, \n"+
				"M.F_APLICACION, \n"+
				"M.ID_GRUPO ID_TRANSACCION_GRUPO, \n"+
			    "SUM(NVL(ROUND(M.IMP_NETO,2),0)) MONTO, \n"+
			    "TO_CHAR(SUM(NVL(ROUND(M.IMP_NETO,2),0)),'999,999,999.99') IMPORTE \n"+
				"FROM \n"+
				"SIM_PRESTAMO_GPO_DET GD, \n"+ 
				"SIM_PRESTAMO_GRUPO PG, \n"+
				"SIM_GRUPO G, \n"+
				"SIM_PRESTAMO P, \n"+
				"SIM_PRODUCTO PRO, \n"+
				"PFIN_MOVIMIENTO M \n"+
				"WHERE GD.CVE_GPO_EMPRESA = 'SIM' \n"+ 
				"AND GD.CVE_EMPRESA = 'CREDICONFIA' \n"+
				"AND PG.CVE_GPO_EMPRESA = GD.CVE_GPO_EMPRESA \n"+  
				"AND PG.CVE_EMPRESA = GD.CVE_EMPRESA \n"+
				"AND PG.ID_PRESTAMO_GRUPO = GD.ID_PRESTAMO_GRUPO \n"+  
				"AND PG.ID_ETAPA_PRESTAMO != '8' \n"+
				"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
				"AND G.ID_GRUPO = PG.ID_GRUPO \n"+
				"AND G.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
				"AND P.CVE_GPO_EMPRESA = GD.CVE_GPO_EMPRESA \n"+  
				"AND P.CVE_EMPRESA = GD.CVE_EMPRESA \n"+
				"AND P.ID_PRESTAMO = GD.ID_PRESTAMO \n"+
				"AND PRO.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+ 
				"AND PRO.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND PRO.ID_PRODUCTO = P.ID_PRODUCTO \n"+
				"AND M.CVE_GPO_EMPRESA = GD.CVE_GPO_EMPRESA \n"+  
				"AND M.CVE_EMPRESA = GD.CVE_EMPRESA \n"+
				"AND M.ID_PRESTAMO = GD.ID_PRESTAMO \n"+
				"AND M.CVE_OPERACION = 'CRPAGOPRES' \n";
				
		if (parametros.getDefCampo("CVE_PRESTAMO_GRUPO") != null) {
			sSql = sSql + "AND PG.CVE_PRESTAMO_GRUPO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO_GRUPO") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_GRUPO") != null) {
			sSql = sSql + "AND G.NOM_GRUPO = '" + (String) parametros.getDefCampo("NOM_GRUPO") + "' \n";
		}
		
		sSql = sSql + "GROUP BY GD.ID_PRESTAMO_GRUPO, PG.CVE_PRESTAMO_GRUPO, 'GRUPAL', '', G.ID_GRUPO, G.NOM_GRUPO, PG.ID_PRODUCTO, PRO.NOM_PRODUCTO, PG.NUM_CICLO, M.F_APLICACION, M.ID_GRUPO \n";
		
		sSql = sSql + "MINUS \n"+
			"SELECT \n"+
			"C.ID_PRESTAMO, \n"+ 
			"PG.CVE_PRESTAMO_GRUPO, \n"+
			"'GRUPAL' APLICA_A, \n"+
			"'' NOM_COMPLETO, \n"+
			"G.ID_GRUPO, \n"+
			"G.NOM_GRUPO, \n"+
			"PG.ID_PRODUCTO, \n"+
			"PRO.NOM_PRODUCTO, \n"+
			"PG.NUM_CICLO, \n"+
			"C.FECHA_APLICACION, \n"+
			"ID_TRANSACCION_GRUPO, \n"+
			"NVL(ROUND(-C.MONTO,2),0) MONTO,  \n"+
			"TO_CHAR(NVL(ROUND(-C.MONTO,2),0),'999,999,999.99') IMPORTE \n"+
			"FROM \n"+
			"SIM_CAJA_TRANSACCION C, \n"+
			"SIM_PRESTAMO_GRUPO PG, \n"+
			"SIM_GRUPO G, \n"+
			"SIM_PRODUCTO PRO \n"+
			"WHERE C.CVE_GPO_EMPRESA = 'SIM' \n"+ 
			"AND C.CVE_EMPRESA = 'CREDICONFIA' \n"+
			"AND C.CVE_MOVIMIENTO_CAJA = 'CANPAGO' \n"+
			"AND PG.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+  
			"AND PG.CVE_EMPRESA = C.CVE_EMPRESA \n"+
			"AND PG.ID_PRESTAMO_GRUPO = C.ID_PRESTAMO \n"+  
			"AND PG.ID_ETAPA_PRESTAMO != '8' \n"+
			"AND G.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+
			"AND G.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
			"AND G.ID_GRUPO = PG.ID_GRUPO \n"+
			"AND G.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
			"AND PRO.CVE_GPO_EMPRESA = PG.CVE_GPO_EMPRESA \n"+ 
			"AND PRO.CVE_EMPRESA = PG.CVE_EMPRESA \n"+
			"AND PRO.ID_PRODUCTO = PG.ID_PRODUCTO \n";
		
		if (parametros.getDefCampo("CVE_PRESTAMO_GRUPO") != null) {
			sSql = sSql + "AND PG.CVE_PRESTAMO_GRUPO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO_GRUPO") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_GRUPO") != null) {
			sSql = sSql + "AND G.NOM_GRUPO = '" + (String) parametros.getDefCampo("NOM_GRUPO") + "' \n";
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
		
		resultadoCatalogo.Resultado = new Registro();
		
		String sIdTransaccion = "";
		int iIdTransaccion = 0;
		String sMontoCaja = "";
		float fMontoCaja = 0;
		String sMonto = "";
		float fMonto = 0;
		
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
				"ID_CLIENTE, \n" +
				"ID_GRUPO, \n" +
				"ID_PRODUCTO, \n" +
				"NUM_CICLO, \n" +
				"CVE_MOVIMIENTO_CAJA, \n" +
				"MONTO, \n" +
				"FECHA_TRANSACCION, \n" +
				"CVE_USUARIO_CAJERO, \n" +
				"USUARIO_ENTREGA, \n" +
				"ID_TRANSACCION_GRUPO, \n" +
				"ID_PRESTAMO_GRUPO) \n" +
		        "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdTransaccion + ", \n "+
				"'" + (String)registro.getDefCampo("ID_SUCURSAL") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
				"'" + (String)registro.getDefCampo("USUARIO_ENTREGA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_GRUPO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_MOVIMIENTO_CAJA") + "', \n" +
				"'" + (String)registro.getDefCampo("MONTO") + "', \n" +
				"SYSDATE, \n" +
				"'" + (String)registro.getDefCampo("CVE_USUARIO_CAJERO") + "', \n" +
				"'" + (String)registro.getDefCampo("USUARIO_ENTREGA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_TRANSACCION_GRUPO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRESTAMO_GRUPO") + "') \n" ;
		
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		resultadoCatalogo.Resultado.addDefCampo("ID_TRANSACCION", sIdTransaccion);
		
		return resultadoCatalogo;
		
	}
}