/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.caja.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para la entrega de prestamos individuales.
 */
 
public class SimCajaDesembolsoGrupalTransaccionDAO extends Conexion2 implements OperacionConsultaTabla {
	
	/**
	 * Obtiene un conjunto de registros en base a el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS QUE COINCIDAN CON LAS CONDICIONES
		
		sSql =  "SELECT DISTINCT \n"+
				"T.CVE_GPO_EMPRESA, \n"+
				"T.CVE_EMPRESA, \n"+
				"T.ID_TRANSACCION_GRUPO, \n"+
				"T.ID_GRUPO, \n"+
				"T.ID_PRODUCTO, \n"+
				"G.NOM_GRUPO, \n"+
				"T.NUM_CICLO, \n"+
				"P.NOM_PRODUCTO, \n"+
				"TO_CHAR(A.MONTO_AUTORIZADO + B.CARGO_INICIAL,'999,999,999.99') MONTO_PRESTADO, \n"+
				"T.ID_SUCURSAL, \n"+
				"T.ID_CAJA \n"+
				"FROM SIM_CAJA_TRANSACCION T, \n"+
				"SIM_PRODUCTO P, \n"+
				"(SELECT  \n"+
				"CVE_GPO_EMPRESA, \n"+
				"CVE_EMPRESA, \n"+
				"ID_PRESTAMO_GRUPO, \n"+
				"SUM(MONTO_AUTORIZADO) MONTO_AUTORIZADO \n"+
				"FROM SIM_PRESTAMO_GPO_DET \n"+
				"WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"GROUP BY CVE_GPO_EMPRESA, \n"+
				"CVE_EMPRESA, \n"+
				"ID_PRESTAMO_GRUPO) A, \n"+
				"(SELECT \n"+
				"P.CVE_GPO_EMPRESA, \n"+
				"P.CVE_EMPRESA, \n"+
				"P.ID_PRESTAMO_GRUPO, \n"+
				"SUM(C.CARGO_INICIAL) CARGO_INICIAL \n"+
				"FROM SIM_PRESTAMO_GPO_DET P, \n"+ 
				"SIM_PRESTAMO_GPO_CARGO C \n"+
				"WHERE P.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND P.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND C.CVE_GPO_EMPRESA = P.CVE_GPO_EMPRESA \n"+
				"AND C.CVE_EMPRESA = P.CVE_EMPRESA \n"+
				"AND C.ID_PRESTAMO_GRUPO = P.ID_PRESTAMO_GRUPO \n"+
				"AND C.ID_FORMA_APLICACION = 1 \n"+
				"GROUP BY  \n"+
				"P.CVE_GPO_EMPRESA, \n"+
				"P.CVE_EMPRESA, \n"+
				"P.ID_PRESTAMO_GRUPO)B, \n"+
				"SIM_GRUPO G, \n"+
				"SIM_PRESTAMO_GRUPO PG, \n"+
				"SIM_USUARIO_ACCESO_SUCURSAL US \n"+
				"WHERE T.CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND T.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND T.CVE_MOVIMIENTO_CAJA = 'DESGPO' \n"+
				"AND T.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
				"AND P.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
				"AND P.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
				"AND P.ID_PRODUCTO (+)= T.ID_PRODUCTO \n"+
				"AND G.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
				"AND G.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
				"AND G.ID_GRUPO (+)= T.ID_GRUPO \n"+
				"AND A.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
				"AND A.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
				"AND A.ID_PRESTAMO_GRUPO (+)= T.ID_PRESTAMO_GRUPO \n"+
				"AND B.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
				"AND B.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
				"AND B.ID_PRESTAMO_GRUPO (+)= T.ID_PRESTAMO_GRUPO \n"+
				"AND PG.CVE_GPO_EMPRESA (+)= T.CVE_GPO_EMPRESA \n"+
				"AND PG.CVE_EMPRESA (+)= T.CVE_EMPRESA \n"+
				"AND PG.ID_PRESTAMO_GRUPO (+)= T.ID_PRESTAMO_GRUPO \n"+
				"AND US.CVE_GPO_EMPRESA = T.CVE_GPO_EMPRESA \n"+
	            "AND US.CVE_EMPRESA = T.CVE_EMPRESA \n"+
	            "AND US.ID_SUCURSAL = T.ID_SUCURSAL \n"+
	            "AND US.CVE_USUARIO = '" + (String)parametros.getDefCampo("CVE_USUARIO") + "' \n";
		
		if (parametros.getDefCampo("CVE_PRESTAMO") != null) {
			sSql = sSql + "AND PG.CVE_PRESTAMO_GRUPO = '" + (String) parametros.getDefCampo("CVE_PRESTAMO") + "' \n";
		}
		if (parametros.getDefCampo("NOM_GRUPO") != null) {
			sSql = sSql + " AND UPPER(G.NOM_GRUPO) LIKE '%" + ((String) parametros.getDefCampo("NOM_GRUPO")).toUpperCase() + "%' \n";
		}
		
		ejecutaSql();
		return getConsultaLista();
	}
}