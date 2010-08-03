/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para el catálogo de sucursales.
 */
 
public class SimCatalogoSucursalTribunalesDAO extends Conexion2 implements OperacionConsultaRegistro, OperacionModificacion {

	/**
	 * Obtiene un registro en base a una llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql =  " SELECT \n"+
				" S.CVE_GPO_EMPRESA, \n"+
				" S.CVE_EMPRESA, \n"+
				" S.ID_SUCURSAL, \n"+
				" S.NOM_SUCURSAL, \n"+
				" S.ID_DIRECCION, \n"+
				" S.ID_DIRECCION_TRIBUNAL, \n"+
				" S.CVE_USUARIO_GERENTE, \n"+
				" S.CVE_USUARIO_COORDINADOR, \n"+
				" S.TASA_IVA, \n"+
				" D.ID_DOMICILIO, \n"+
				" D.IDENTIFICADOR, \n"+
				" D.CVE_TIPO_IDENTIFICADOR, \n"+
				" D.CALLE, \n"+
				" D.NUMERO_INT, \n"+
				" D.NUMERO_EXT, \n"+
				" D.CODIGO_POSTAL, \n"+
				" D.ID_REFER_POST, \n"+
				" D.NOM_ASENTAMIENTO, \n"+
				" D.NOM_DELEGACION, \n"+
				" D.NOM_CIUDAD, \n"+
				" D.NOM_ESTADO \n"+
			" FROM \n"+
			" SIM_CAT_SUCURSAL S, \n"+
			" RS_GRAL_DOMICILIO D \n"+
			" WHERE S.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND S.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND S.ID_SUCURSAL = '" + (String)parametros.getDefCampo("ID_SUCURSAL") + "' \n"+
			" AND D.CVE_GPO_EMPRESA (+)= S.CVE_GPO_EMPRESA \n"+
			" AND D.CVE_EMPRESA (+)= S.CVE_EMPRESA \n"+
			" AND D.ID_DOMICILIO (+)= S.ID_DIRECCION_TRIBUNAL \n";
			
			   
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
	/**
	 * Modifica un registro.
	 * @param registro Campos del registro a modificar.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo modificacion(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		
		//ACTUALIZA EL DOMICILIO
		sSql =  " UPDATE RS_GRAL_DOMICILIO SET "+
			" CODIGO_POSTAL ='" + (String)registro.getDefCampo("CODIGO_POSTAL") + "', \n" +
			" ID_REFER_POST ='" + (String)registro.getDefCampo("ID_REFER_POST") + "', \n" +
			" CALLE ='" + (String)registro.getDefCampo("CALLE") + "', \n" +
			" NUMERO_INT='" + (String)registro.getDefCampo("NUMERO_INT") + "', \n" +
			" NUMERO_EXT='" + (String)registro.getDefCampo("NUMERO_EXT") + "', \n" +
			" NOM_ASENTAMIENTO ='" + (String)registro.getDefCampo("NOM_ASENTAMIENTO") + "', \n" +
			" TIPO_ASENTAMIENTO ='" + (String)registro.getDefCampo("TIPO_ASENTAMIENTO") + "', \n" +
			" NOM_DELEGACION ='" + (String)registro.getDefCampo("NOM_DELEGACION") + "', \n" +
   			" NOM_CIUDAD ='" + (String)registro.getDefCampo("NOM_CIUDAD") + "', \n" +
  			" NOM_ESTADO ='" + (String)registro.getDefCampo("NOM_ESTADO") + "' \n" +
			" WHERE ID_DOMICILIO = '" + (String)registro.getDefCampo("ID_DOMICILIO_TRIBUNAL") + "' \n" +
			" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" ;
		
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		return resultadoCatalogo;
	}
}