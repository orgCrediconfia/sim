/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.catalogos.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Administra los accesos a la base de datos para el catálogo regional.
 */
 
public class SimCatalogoRegionalDAO extends Conexion2 implements OperacionConsultaTabla, OperacionConsultaRegistro, OperacionAlta, OperacionModificacion, OperacionBaja {

	/**
	 * Obtiene un conjunto de registros en base al filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{
		//PREPARA LA CONSULTA QUE VA A HACER EN LA BD, PARA TRAER LOS REGISTROS DE BANCOS
		
		sSql =  " SELECT \n"+
				" R.CVE_GPO_EMPRESA, \n"+
				" R.CVE_EMPRESA, \n"+
				" R.ID_REGIONAL, \n"+
				" R.NOM_REGIONAL, \n"+
				" R.ID_DIRECCION, \n"+
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
			" SIM_CAT_REGIONAL R, \n"+
			" RS_GRAL_DOMICILIO D \n"+
			" WHERE R.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND R.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND D.CVE_GPO_EMPRESA (+)= R.CVE_GPO_EMPRESA \n"+
			" AND D.CVE_EMPRESA (+)= R.CVE_EMPRESA \n"+
			" AND D.ID_DOMICILIO (+)= R.ID_DIRECCION \n";
		
		if (parametros.getDefCampo("ID_REGIONAL") != null) {
			sSql = sSql + " AND R.ID_REGIONAL = '" + (String) parametros.getDefCampo("ID_REGIONAL") + "' \n";
		}
		
		if (parametros.getDefCampo("NOM_REGIONAL") != null) {
			sSql = sSql + " AND UPPER(R.NOM_REGIONAL) LIKE'%" + ((String) parametros.getDefCampo("NOM_REGIONAL")).toUpperCase()  + "%' \n";
		}
		
		sSql = sSql + " ORDER BY R.ID_REGIONAL \n";
		
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
		sSql =  " SELECT \n"+
				" R.CVE_GPO_EMPRESA, \n"+
				" R.CVE_EMPRESA, \n"+
				" R.ID_REGIONAL, \n"+
				" R.NOM_REGIONAL, \n"+
				" R.ID_DIRECCION, \n"+
				" R.CVE_USUARIO_GERENTE, \n"+
				" R.CVE_USUARIO_COORDINADOR, \n"+
				" PUG.NOM_COMPLETO NOMBRE_GERENTE, \n"+ 
				" PUC.NOM_COMPLETO NOMBRE_COORDINADOR, \n"+ 
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
			" SIM_CAT_REGIONAL R, \n"+
			" RS_GRAL_DOMICILIO D, \n"+
			" RS_GRAL_USUARIO UG, \n"+
			" RS_GRAL_PERSONA PUG, \n"+
			" RS_GRAL_USUARIO UC, \n"+
			" RS_GRAL_PERSONA PUC \n"+
			" WHERE R.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			" AND R.CVE_EMPRESA = '" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND R.ID_REGIONAL = '" + (String)parametros.getDefCampo("ID_REGIONAL") + "' \n"+
			" AND D.CVE_GPO_EMPRESA (+)= R.CVE_GPO_EMPRESA \n"+
			" AND D.CVE_EMPRESA (+)= R.CVE_EMPRESA \n"+
			" AND D.ID_DOMICILIO (+)= R.ID_DIRECCION \n"+
			" AND UG.CVE_GPO_EMPRESA (+)= R.CVE_GPO_EMPRESA \n"+
			" AND UG.CVE_EMPRESA (+)= R.CVE_EMPRESA \n"+
			" AND UG.CVE_USUARIO (+)= R.CVE_USUARIO_GERENTE \n"+
			" AND PUG.CVE_GPO_EMPRESA (+)= UG.CVE_GPO_EMPRESA \n"+
			" AND PUG.CVE_EMPRESA (+)= UG.CVE_EMPRESA \n"+
			" AND PUG.ID_PERSONA (+)= UG.ID_PERSONA \n"+
			" AND UC.CVE_GPO_EMPRESA (+)= R.CVE_GPO_EMPRESA \n"+
			" AND UC.CVE_EMPRESA (+)= R.CVE_EMPRESA \n"+
			" AND UC.CVE_USUARIO (+)= R.CVE_USUARIO_COORDINADOR \n"+
			" AND PUC.CVE_GPO_EMPRESA (+)= UC.CVE_GPO_EMPRESA \n"+
			" AND PUC.CVE_EMPRESA (+)= UC.CVE_EMPRESA \n"+
			" AND PUC.ID_PERSONA (+)= UC.ID_PERSONA \n";
			
		ejecutaSql();
		return this.getConsultaRegistro();
	}
	
	/**
	 * Inserta un registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecuciï¿½n de este mï¿½todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		resultadoCatalogo.Resultado = new Registro();
		
		String sIdDomicilio = "";
		String sIdRegional = "";
		
		sSql = "SELECT NOM_REGIONAL FROM SIM_CAT_REGIONAL WHERE NOM_REGIONAL = '" + (String)registro.getDefCampo("NOM_REGIONAL") + "' \n" ;
		ejecutaSql();
		if (rs.next()){
			resultadoCatalogo.mensaje.setClave("EXISTE_REGIONAL");
		}else{
		
		
		//OBTENEMOS EL SEQUENCE
		sSql = "SELECT SQ01_RS_GRAL_DOMICILIO.nextval as ID_DOMICILIO FROM DUAL";
			ejecutaSql();
			if (rs.next()){
				sIdDomicilio = rs.getString("ID_DOMICILIO");
			}

			//INSERTA EL REGISTRO DE DOMICILIO PARA PODER OBTENER EL ID_DOMICILIO DEL SEQUENCE
			sSql = "INSERT INTO RS_GRAL_DOMICILIO ( "+
				   "CVE_GPO_EMPRESA, \n" +
				   "CVE_EMPRESA, \n" +
				   "ID_DOMICILIO, \n" +
				   "CALLE, \n" +
				   "NUMERO_INT, \n" +
				   "NUMERO_EXT, \n" +
				   "CODIGO_POSTAL, \n" +
				   "ID_REFER_POST, \n" +
				   "NOM_ASENTAMIENTO, \n" +
				   "TIPO_ASENTAMIENTO, \n" +
				   "NOM_DELEGACION, \n" +
				   "NOM_CIUDAD, \n" +
				   "NOM_ESTADO, \n" +
				   "IDENTIFICADOR, \n" +
				   "CVE_TIPO_IDENTIFICADOR) \n" +
				" VALUES (" +
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				" " + sIdDomicilio +", \n" +
				"'" + (String)registro.getDefCampo("CALLE") + "', \n" +
				"'" + (String)registro.getDefCampo("NUMERO_INT") + "', \n" +
				"'" + (String)registro.getDefCampo("NUMERO_EXT") + "', \n" +
				"'" + (String)registro.getDefCampo("CODIGO_POSTAL") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_REFER_POST") + "', \n" +
				"'" + (String)registro.getDefCampo("NOM_ASENTAMIENTO") + "', \n" +
				"'" + (String)registro.getDefCampo("TIPO_ASENTAMIENTO") + "', \n" +				
				"'" + (String)registro.getDefCampo("NOM_DELEGACION") + "', \n" +
				"'" + (String)registro.getDefCampo("NOM_CIUDAD") + "', \n" +
				"'" + (String)registro.getDefCampo("NOM_ESTADO") + "', \n" +
				"'" + (String)registro.getDefCampo("IDENTIFICADOR") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_TIPO_IDENTIFICADOR") + "') \n" ;

		//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		resultadoCatalogo.Resultado.addDefCampo("ID_DOMICILIO", sIdDomicilio);
		
		//SE OBTIENE EL SEQUENCE
					   
		sSql = "SELECT SQ01_SIM_CAT_REGIONAL.nextval as ID_REGIONAL FROM DUAL";
		ejecutaSql();
		if (rs.next()){
		sIdRegional = rs.getString("ID_REGIONAL");
		}
		
		
			sSql =  "INSERT INTO SIM_CAT_REGIONAL ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_REGIONAL, \n" +
				"NOM_REGIONAL, \n" +
				"ID_DIRECCION, \n" +
				"CVE_USUARIO_GERENTE, \n" +
				"CVE_USUARIO_COORDINADOR) \n" +
		           	"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				sIdRegional + ", \n "+
				"'" + (String)registro.getDefCampo("NOM_REGIONAL") + "', \n" +
				" " + sIdDomicilio +", \n" +
				"'" + (String)registro.getDefCampo("CVE_USUARIO_GERENTE") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_USUARIO_COORDINADOR") + "') \n" ;

			//VERIFICA SI NO SE DIO DE ALTA EL REGISTRO
			if (ejecutaUpdate() == 0){
				resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
			}
			resultadoCatalogo.Resultado.addDefCampo("ID_REGIONAL", sIdRegional);
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
			" WHERE ID_DOMICILIO = '" + (String)registro.getDefCampo("ID_DOMICILIO") + "' \n" +
			" AND CVE_GPO_EMPRESA = '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_EMPRESA = '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n" ;
			
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		sSql = " UPDATE SIM_CAT_REGIONAL SET "+
			   " NOM_REGIONAL  			='" + (String)registro.getDefCampo("NOM_REGIONAL")  + "', \n" +
			   " CVE_USUARIO_GERENTE    		='" + (String)registro.getDefCampo("CVE_USUARIO_GERENTE")  + "', \n" +
			   " CVE_USUARIO_COORDINADOR    	='" + (String)registro.getDefCampo("CVE_USUARIO_COORDINADOR")  + "' \n" +
			   " WHERE ID_REGIONAL    	='" + (String)registro.getDefCampo("ID_REGIONAL") + "' \n" +
			   " AND CVE_EMPRESA   		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			   " AND CVE_GPO_EMPRESA   	='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		return resultadoCatalogo;
	}
	
	/**
	 * Borra un registro.
	 * @param registro Llave primaria.
	 * @return Objeto que contiene el resultado de la ejecuciï¿½n de este mï¿½todo.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		//BORRA LA FUNCION
		sSql =  "DELETE FROM SIM_CAT_REGIONAL " +
			" WHERE ID_REGIONAL			='" + (String)registro.getDefCampo("ID_REGIONAL") + "' \n" +
			" AND CVE_EMPRESA			='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";

		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}
}