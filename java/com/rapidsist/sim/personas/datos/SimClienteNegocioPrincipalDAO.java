/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.personas.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro; 
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para la consula del negocio principal del cliente.
 */
 
public class SimClienteNegocioPrincipalDAO extends Conexion2 implements OperacionConsultaRegistro {

	/**
	 * Obtiene un registro tomando como base la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
	
		sSql =  " SELECT \n"+
			" NVL(( \n"+
			" SELECT \n"+
			" B_PRINCIPAL \n"+
			" FROM SIM_CLIENTE_NEGOCIO \n" +
			" WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
			" AND ID_PERSONA  = '" + (String) parametros.getDefCampo("ID_PERSONA") + "' \n" +
			" AND B_PRINCIPAL = 'V'),'F') B_PRINCIPAL \n" +
			" FROM DUAL \n" ;	
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}