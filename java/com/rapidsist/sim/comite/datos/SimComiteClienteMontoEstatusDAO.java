/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.comite.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;  
import com.rapidsist.portal.catalogos.OperacionModificacion; 
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para los montos por cliente del préstamo.
 */
 
public class SimComiteClienteMontoEstatusDAO extends Conexion2 implements OperacionAlta, OperacionModificacion, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros en base ael filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//OBTENEMOS EL FILTRO DE BUSQUEDA
		
		sSql =  "SELECT \n"+
				   "MC.CVE_GPO_EMPRESA, \n" +
				   "MC.CVE_EMPRESA, \n" +
				   "MC.ID_PRESTAMO, \n"+
				   "MC.ID_CLIENTE, \n"+
				   "P.NOM_COMPLETO, \n"+
				   "MC.MONTO_SOLICITADO, \n"+
				   "MC.MONTO_AUTORIZADO, \n"+
				   "CP.ESTATUS \n"+
				" FROM SIM_CLIENTE_MONTO MC, \n"+
				"      RS_GRAL_PERSONA P, \n"+
				"      SIM_COMITE_PRESTAMO CP \n"+
				" WHERE MC.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND MC.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND MC.ID_PRESTAMO ='" + (String)parametros.getDefCampo("ID_PRESTAMO") + "' \n"+
				" AND P.CVE_GPO_EMPRESA = MC.CVE_GPO_EMPRESA \n"+
				" AND P.CVE_EMPRESA = MC.CVE_EMPRESA \n"+
				" AND P.ID_PERSONA = MC.ID_CLIENTE \n"+
				" AND CP.CVE_GPO_EMPRESA = MC.CVE_GPO_EMPRESA \n"+
				" AND CP.CVE_EMPRESA = MC.CVE_EMPRESA \n"+
				" AND CP.ID_PRESTAMO = MC.ID_PRESTAMO \n";
				
		ejecutaSql();
		return this.getConsultaLista();
	}

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();

		sSql = " UPDATE SIM_CLIENTE_MONTO SET "+
			   " MONTO_AUTORIZADO     	= '" + (String)registro.getDefCampo("MONTO_AUTORIZADO")  + "' \n" +
			   " WHERE ID_PRESTAMO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
			   " AND ID_CLIENTE   		= '" + (String)registro.getDefCampo("ID_CLIENTE") + "' \n" +
			   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		sSql = " UPDATE SIM_COMITE_PRESTAMO SET "+
			   " ESTATUS     		= '" + (String)registro.getDefCampo("ESTATUS")  + "' \n" +
			   " WHERE ID_PRESTAMO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
			   " AND ID_CLIENTE   		= '" + (String)registro.getDefCampo("ID_CLIENTE") + "' \n" +
			   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		return resultadoCatalogo;
	}

	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		sSql = " UPDATE SIM_CLIENTE_MONTO SET "+
			   " MONTO_AUTORIZADO     	= '" + (String)registro.getDefCampo("MONTO_AUTORIZADO")  + "' \n" +
			   " WHERE ID_PRESTAMO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
			   " AND ID_CLIENTE   		= '" + (String)registro.getDefCampo("ID_CLIENTE") + "' \n" +
			   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		sSql = " UPDATE SIM_COMITE_PRESTAMO SET "+
			   " ESTATUS     		= '" + (String)registro.getDefCampo("ESTATUS")  + "' \n" +
			   " WHERE ID_PRESTAMO      	= '" + (String)registro.getDefCampo("ID_PRESTAMO") + "' \n" +
			   " AND ID_CLIENTE   		= '" + (String)registro.getDefCampo("ID_CLIENTE") + "' \n" +
			   " AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			   " AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
	
		return resultadoCatalogo;
	}

}