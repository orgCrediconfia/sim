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
 * Administra los accesos a la base de datos para hacer desembolsos de Caja.
 */
 
public class SimCajaDesembolsoDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta {
	
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
				"C.ID_SUCURSAL||'-'||C.ID_CAJA ID_CAJA, \n"+
				"S.NOM_SUCURSAL ||' - '|| 'CAJA ' || C.ID_CAJA NOM_CAJA \n"+
				"FROM SIM_SUCURSAL_CAJA C, \n"+
				"SIM_CAT_SUCURSAL S \n"+
				"WHERE C.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND C.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND S.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
				"AND S.CVE_EMPRESA = C.CVE_EMPRESA \n"+
				"AND S.ID_SUCURSAL = C.ID_SUCURSAL \n"+
				"ORDER BY NOM_CAJA \n";
				
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
		
		String sIdTransaccion = "";
		
		//OBTENEMOS EL SEQUENCE
		sSql = "SELECT SQ01_SIM_CAJA_TRANSACCION.nextval as ID_TRANSACCION FROM DUAL";
		ejecutaSql();
			
		if (rs.next()){
			sIdTransaccion = rs.getString("ID_TRANSACCION");
		}
		
		sSql =  "INSERT INTO SIM_CAJA_TRANSACCION ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_TRANSACCION, \n" +
				"ID_CAJA, \n" +
				"CVE_MOVIMIENTO_CAJA, \n" +
				"MONTO, \n" +
				"FECHA_TRANSACCION, \n" +
				"FECHA_TRANSACCION_SYSTEM, \n" +
				"CVE_USUARIO_CAJERO) \n" +
		        "VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdTransaccion + ", \n "+
				"'" + (String)registro.getDefCampo("ID_CAJA") + "', \n" +
				"'1', \n" +
				"'" + (String)registro.getDefCampo("MONTO") + "', \n" +
				//"TO_DATE('" + (String)registro.getDefCampo("FECHA_TRANSACCION") + "','DD/MM/YYYY'), \n" +
				"SYSDATE, \n" +
				"SYSDATE, \n" +
				"'" + (String)registro.getDefCampo("CVE_USUARIO_CAJERO") + "') \n" ;
		
		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		return resultadoCatalogo;
	}
}