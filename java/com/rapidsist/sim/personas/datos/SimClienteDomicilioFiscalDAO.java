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
 * Administra los accesos a la base de datos para la direcciones del cliente.
 */
 
public class SimClienteDomicilioFiscalDAO extends Conexion2 implements OperacionConsultaRegistro {

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
			" DOMICILIO_FISCAL \n"+
			" FROM RS_GRAL_DOMICILIO \n" +
			" WHERE CVE_GPO_EMPRESA = '" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n" +
			" AND IDENTIFICADOR  = '" + (String) parametros.getDefCampo("IDENTIFICADOR") + "' \n" +
			" AND CVE_TIPO_IDENTIFICADOR = 'CLIENTE' \n"+
			" AND DOMICILIO_FISCAL = 'V'),'F') DOMICILIO_FISCAL \n" +
			" FROM DUAL \n" ;
		
		ejecutaSql();
		return this.getConsultaRegistro();
	}
}