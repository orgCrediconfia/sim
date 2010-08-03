/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2008 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.sim.producto.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Administra los accesos a la base de datos para los ciclos de un producto.
 */
 
public class SimProductoCicloDAO extends Conexion2 implements OperacionConsultaTabla, OperacionAlta,  OperacionConsultaRegistro, OperacionModificacion, OperacionBaja  {

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
				"C.CVE_GPO_EMPRESA, \n" +
				"C.CVE_EMPRESA, \n" +
				"C.NUM_CICLO, \n"+
				"C.ID_PRODUCTO, \n"+
				"C.PLAZO, \n" +
				"C.TIPO_TASA, \n" +
				"C.VALOR_TASA, \n" +
				//"DECODE(C.VALOR_TASA,NULL,(SELECT VALOR FROM SIM_CAT_TASA_REFER_DETALLE WHERE ID_TASA_REFERENCIA = '17' 	AND FECHA_PUBLICACION = (SELECT MAX(FECHA_PUBLICACION) FECHA_TASA_REFERENCIA FROM (SELECT FECHA_PUBLICACION FROM SIM_CAT_TASA_REFER_DETALLE WHERE ID_TASA_REFERENCIA = '17' ))),C.VALOR_TASA) VALOR_TASA, \n"+
				//"DECODE(C.VALOR_TASA,NULL,(SELECT VALOR FROM SIM_CAT_TASA_REFER_DETALLE WHERE ID_TASA_REFERENCIA = (SELECT ID_TASA_REFERENCIA FROM SIM_PRODUCTO_CICLO \n"+
				//"WHERE ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' AND ID_TASA_REFERENCIA IS NOT NULL) AND FECHA_PUBLICACION = (SELECT MAX(FECHA_PUBLICACION) FECHA_TASA_REFERENCIA FROM (SELECT FECHA_PUBLICACION FROM SIM_CAT_TASA_REFER_DETALLE WHERE ID_TASA_REFERENCIA = (SELECT ID_TASA_REFERENCIA FROM SIM_PRODUCTO_CICLO WHERE ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' AND ID_TASA_REFERENCIA IS NOT NULL) AND FECHA_PUBLICACION < SYSDATE))),C.VALOR_TASA) VALOR_TASA, \n" +
				
				"C.ID_PERIODICIDAD_TASA, \n" +
				//"C.VALOR_TASA, \n" +
				"C.ID_TASA_REFERENCIA, \n"+
				"TO_CHAR(C.MONTO_MAXIMO,'999,999,999.99') MONTO_MAXIMO, \n"+
				"C.PORC_FLUJO_CAJA, \n" +
				"C.ID_TIPO_RECARGO, \n" +
				"A.NOM_ACCESORIO, \n" +
				"C.TIPO_TASA_RECARGO, \n" +
				"C.ID_PERIODICIDAD_TASA_RECARGO, \n" +
				"C.TASA_RECARGO, \n"+
				"C.ID_TASA_REFERENCIA_RECARGO, \n" +
				"C.FACTOR_TASA_RECARGO, \n"+
				"C.MONTO_FIJO_PERIODO  \n" +
				" FROM SIM_PRODUCTO_CICLO C, \n"+
				"      SIM_CAT_ACCESORIO A \n"+
				" WHERE C.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND C.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND C.ID_PRODUCTO ='" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
				" AND A.CVE_GPO_EMPRESA (+)= C.CVE_GPO_EMPRESA \n"+
				" AND A.CVE_EMPRESA (+)= C.CVE_EMPRESA \n"+
				" AND A.ID_ACCESORIO (+)= C.ID_TIPO_RECARGO \n"+
				" ORDER BY C.NUM_CICLO \n";
				
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
		sSql = "SELECT \n"+
				"PC.CVE_GPO_EMPRESA, \n" +
				"PC.CVE_EMPRESA, \n" +
				"PC.NUM_CICLO, \n"+
				"PC.ID_PRODUCTO, \n"+
				"PC.ID_FORMA_DISTRIBUCION, \n"+
				"PC.PLAZO, \n" +
				"PC.TIPO_TASA, \n"+
				
				"DECODE(PC.ID_PERIODICIDAD_TASA,NULL,(SELECT ID_PERIODICIDAD \n" +
				"									FROM SIM_CAT_TASA_REFERENCIA \n" +
				"									WHERE ID_TASA_REFERENCIA = (SELECT ID_TASA_REFERENCIA \n" +
				"																FROM SIM_PRODUCTO_CICLO \n" +
				"																WHERE ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n" +
						"														AND NUM_CICLO = '" + (String)parametros.getDefCampo("NUM_CICLO") + "')),PC.ID_PERIODICIDAD_TASA) ID_PERIODICIDAD_TASA, \n" +
				"DECODE(PC.VALOR_TASA,NULL,(SELECT VALOR  \n" +
				"						FROM SIM_CAT_TASA_REFER_DETALLE \n" +
				"						WHERE ID_TASA_REFERENCIA = (SELECT ID_TASA_REFERENCIA \n" +
				"													FROM SIM_PRODUCTO_CICLO \n" +
				"													WHERE ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n" +
						"											AND NUM_CICLO = '" + (String)parametros.getDefCampo("NUM_CICLO") + "') \n" +
								"		AND FECHA_PUBLICACION = (SELECT MAX(FECHA_PUBLICACION) FECHA_TASA_REFERENCIA \n" +
								"								FROM (SELECT FECHA_PUBLICACION \n" +
								"								FROM SIM_CAT_TASA_REFER_DETALLE \n" +
								"								WHERE ID_TASA_REFERENCIA = (SELECT ID_TASA_REFERENCIA \n" +
								"															FROM SIM_PRODUCTO_CICLO \n" +
								"															WHERE ID_PRODUCTO = '" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n" +
										"													AND NUM_CICLO = '" + (String)parametros.getDefCampo("NUM_CICLO") + "') \n" +
												"				AND FECHA_PUBLICACION < SYSDATE))),PC.VALOR_TASA) VALOR_TASA, \n" +
				
				"PT.NOM_PERIODICIDAD NOM_PERIODICIDAD_TASA, \n" +
				"PC.ID_TASA_REFERENCIA, \n"+
				"TR.NOM_TASA_REFERENCIA, \n"+
				"PC.MONTO_MAXIMO, \n" +
				"PC.PORC_FLUJO_CAJA, \n" +
				"PC.ID_TIPO_RECARGO, \n" +
				"A.ID_ACCESORIO, \n" +
				"A.NOM_ACCESORIO, \n" +
				"DECODE(PC.TIPO_TASA_RECARGO,'1','Fija Independiente','2','Dependiente de la Tasa de crédito') NOM_TIPO_TASA_RECARGO, \n" +
				"PC.TIPO_TASA_RECARGO, \n" +
				"PC.ID_PERIODICIDAD_TASA_RECARGO, \n" +
				"PTR.NOM_PERIODICIDAD NOM_PERIODICIDAD_TASA_RECARGO, \n" +
				"PC.TASA_RECARGO, \n"+
				"PC.ID_TASA_REFERENCIA_RECARGO, \n" +
				"TRR.NOM_TASA_REFERENCIA NOM_TASA_REFERENCIA_RECARGO, \n" +
				"PC.FACTOR_TASA_RECARGO, \n"+
				"PC.MONTO_FIJO_PERIODO,  \n" +
				"P.ID_PERIODICIDAD, \n" +
				"PE.NOM_PERIODICIDAD  \n" +
				" FROM SIM_PRODUCTO_CICLO PC, \n"+
				" SIM_CAT_ACCESORIO A, \n"+
				" SIM_PRODUCTO P, \n"+
				" SIM_CAT_PERIODICIDAD PE, \n"+
				" SIM_CAT_PERIODICIDAD PTR, \n"+
				" SIM_CAT_TASA_REFERENCIA TRR, \n"+
				" SIM_CAT_PERIODICIDAD PT, \n"+
				" SIM_CAT_TASA_REFERENCIA TR \n"+
				" WHERE PC.CVE_GPO_EMPRESA ='" + (String)parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				" AND PC.CVE_EMPRESA ='" + (String)parametros.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND PC.ID_PRODUCTO ='" + (String)parametros.getDefCampo("ID_PRODUCTO") + "' \n"+
				" AND PC.NUM_CICLO ='" + (String)parametros.getDefCampo("NUM_CICLO") + "' \n"+
				" AND A.CVE_GPO_EMPRESA (+)= PC.CVE_GPO_EMPRESA \n"+
				" AND A.CVE_EMPRESA (+)= PC.CVE_EMPRESA \n"+
				" AND A.ID_ACCESORIO (+)= PC.ID_TIPO_RECARGO \n"+
				" AND P.CVE_GPO_EMPRESA (+)= PC.CVE_GPO_EMPRESA \n"+
				" AND P.CVE_EMPRESA (+)= PC.CVE_EMPRESA \n"+
				" AND P.ID_PRODUCTO (+)= PC.ID_PRODUCTO \n"+
				" AND PE.CVE_GPO_EMPRESA (+)= P.CVE_GPO_EMPRESA \n"+
				" AND PE.CVE_EMPRESA (+)= P.CVE_EMPRESA \n"+
				" AND PE.ID_PERIODICIDAD (+)= P.ID_PERIODICIDAD \n"+
				" AND PTR.CVE_GPO_EMPRESA (+)= PC.CVE_GPO_EMPRESA \n"+
				" AND PTR.CVE_EMPRESA (+)= PC.CVE_EMPRESA \n"+
				" AND PTR.ID_PERIODICIDAD (+)= PC.ID_PERIODICIDAD_TASA_RECARGO \n"+
				" AND TRR.CVE_GPO_EMPRESA (+)= PC.CVE_GPO_EMPRESA \n"+
				" AND TRR.CVE_EMPRESA (+)= PC.CVE_EMPRESA \n"+
				" AND TRR.ID_TASA_REFERENCIA (+)= PC.ID_TASA_REFERENCIA_RECARGO \n"+
				" AND PT.CVE_GPO_EMPRESA (+)= PC.CVE_GPO_EMPRESA \n"+
				" AND PT.CVE_EMPRESA (+)= PC.CVE_EMPRESA \n"+
				" AND PT.ID_PERIODICIDAD (+)= PC.ID_PERIODICIDAD_TASA \n"+
				" AND TR.CVE_GPO_EMPRESA (+)= PC.CVE_GPO_EMPRESA \n"+
				" AND TR.CVE_EMPRESA (+)= PC.CVE_EMPRESA \n"+
				" AND TR.ID_TASA_REFERENCIA (+)= PC.ID_TASA_REFERENCIA \n";
		
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
		
		int iNumCiclo = 0;
		String sNumCiclo = "";
		String sIdCargoComision = "";
		
		sSql =  "SELECT \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PRODUCTO, \n"+
				"MAX(NUM_CICLO) NUM_CICLO \n"+
			"FROM \n"+
				"SIM_PRODUCTO_CICLO \n"+
				"WHERE ID_PRODUCTO = '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
			"GROUP BY \n"+
			"CVE_GPO_EMPRESA, \n"+
			"CVE_EMPRESA, \n"+
			"ID_PRODUCTO \n";
			
		ejecutaSql();
		if (rs.next()){
			sNumCiclo = rs.getString("NUM_CICLO");
			iNumCiclo=Integer.parseInt(sNumCiclo.trim());
			iNumCiclo ++;
			sNumCiclo= String.valueOf(iNumCiclo);
			registro.addDefCampo("NUM_CICLO",sNumCiclo);
			
			
			sSql =  "INSERT INTO SIM_PRODUCTO_CICLO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"NUM_CICLO, \n"+
				"ID_PRODUCTO, \n"+
				"ID_FORMA_DISTRIBUCION, \n"+
				"PLAZO, \n" +
				"TIPO_TASA, \n"+
				"ID_PERIODICIDAD_TASA, \n" +
				"VALOR_TASA, \n" +
				"ID_TASA_REFERENCIA, \n"+
				"MONTO_MAXIMO, \n" +
				"PORC_FLUJO_CAJA, \n" +
			  	"ID_TIPO_RECARGO, \n" +
				"TIPO_TASA_RECARGO, \n" +
				"ID_PERIODICIDAD_TASA_RECARGO, \n" +
				"TASA_RECARGO, \n"+
				"ID_TASA_REFERENCIA_RECARGO, \n" +
				
				"FACTOR_TASA_RECARGO, \n"+
				"MONTO_FIJO_PERIODO ) \n" +
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_FORMA_DISTRIBUCION") + "', \n" +
				"'" + (String)registro.getDefCampo("PLAZO") + "', \n" +
				"'" + (String)registro.getDefCampo("TIPO_TASA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA") + "', \n" +
				"'" + (String)registro.getDefCampo("VALOR_TASA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_TASA_REFERENCIA") + "', \n" +
				"'" + (String)registro.getDefCampo("MONTO_MAXIMO") + "', \n" +
				"'" + (String)registro.getDefCampo("PORC_FLUJO_CAJA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_TIPO_RECARGO") + "', \n" +
				"'" + (String)registro.getDefCampo("TIPO_TASA_RECARGO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA_RECARGO") + "', \n" +
				"'" + (String)registro.getDefCampo("TASA_RECARGO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_TASA_REFERENCIA_RECARGO") + "', \n" +
				
				"'" + (String)registro.getDefCampo("FACTOR_TASA_RECARGO") + "', \n" +
				"'" + (String)registro.getDefCampo("MONTO_FIJO_PERIODO") + "') \n";
		} else {
			registro.addDefCampo("NUM_CICLO","1");
			
			sSql =  "INSERT INTO SIM_PRODUCTO_CICLO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"NUM_CICLO, \n"+
				"ID_PRODUCTO, \n"+
				"ID_FORMA_DISTRIBUCION, \n"+
				"PLAZO, \n" +
				"TIPO_TASA, \n"+
				"ID_PERIODICIDAD_TASA, \n" +
				"VALOR_TASA, \n" +
				"ID_TASA_REFERENCIA, \n"+
				"MONTO_MAXIMO, \n" +
				"PORC_FLUJO_CAJA, \n" +
			  	"ID_TIPO_RECARGO, \n" +
				"TIPO_TASA_RECARGO, \n" +
				"ID_PERIODICIDAD_TASA_RECARGO, \n" +
				"TASA_RECARGO, \n"+
				"ID_TASA_REFERENCIA_RECARGO, \n" +
				
				"FACTOR_TASA_RECARGO, \n"+
				"MONTO_FIJO_PERIODO ) \n" +
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_FORMA_DISTRIBUCION") + "', \n" +
				"'" + (String)registro.getDefCampo("PLAZO") + "', \n" +
				"'" + (String)registro.getDefCampo("TIPO_TASA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA") + "', \n" +
				"'" + (String)registro.getDefCampo("VALOR_TASA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_TASA_REFERENCIA") + "', \n" +
				"'" + (String)registro.getDefCampo("MONTO_MAXIMO") + "', \n" +
				"'" + (String)registro.getDefCampo("PORC_FLUJO_CAJA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_TIPO_RECARGO") + "', \n" +
				"'" + (String)registro.getDefCampo("TIPO_TASA_RECARGO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA_RECARGO") + "', \n" +
				"'" + (String)registro.getDefCampo("TASA_RECARGO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_TASA_REFERENCIA_RECARGO") + "', \n" +
				"'" + (String)registro.getDefCampo("FACTOR_TASA_RECARGO") + "', \n" +
				"'" + (String)registro.getDefCampo("MONTO_FIJO_PERIODO") + "') \n" ;
		
		}		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		sSql =  "INSERT INTO SIM_PRODUCTO_CICLO_ACCESORIO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PRODUCTO, \n"+
				"NUM_CICLO, \n" +
				"ID_ACCESORIO) \n" +
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_TIPO_RECARGO") + "') \n" ;
				
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		sSql =  "INSERT INTO SIM_PRODUCTO_CICLO_ACCESORIO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PRODUCTO, \n"+
				"NUM_CICLO, \n" +
				"ID_ACCESORIO) \n" +
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
				"'1') \n" ;
				
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		sSql =  "INSERT INTO SIM_PRODUCTO_CICLO_ACCESORIO ( \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PRODUCTO, \n"+
				"NUM_CICLO, \n" +
				"ID_ACCESORIO) \n" +
				"VALUES ( \n"+
				"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
				"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
				"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
				"'2') \n" ;
				
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		sSql =  "SELECT  \n"+
				"C.CVE_GPO_EMPRESA, \n"+
				"C.CVE_EMPRESA, \n"+
				"C.ID_PRODUCTO, \n"+
				"C.ID_CARGO_COMISION, \n"+
				"A.B_ACCESORIO \n"+
				"FROM SIM_PRODUCTO_CARGO_COMISION C, \n"+
				"SIM_CAT_ACCESORIO A \n"+
			"WHERE C.CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
			"AND C.CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
			"AND C.ID_PRODUCTO ='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
			"AND A.CVE_GPO_EMPRESA = C.CVE_GPO_EMPRESA \n"+
			"AND A.CVE_EMPRESA = C.CVE_EMPRESA \n"+
			"AND A.ID_ACCESORIO = C.ID_CARGO_COMISION \n"+
			"AND A.B_ACCESORIO = 'V' \n";
			
		
		ejecutaSql();		
		while (rs.next()){
			
			sIdCargoComision = rs.getString("ID_CARGO_COMISION");
			
			sSql =  "SELECT  \n"+
				"CVE_GPO_EMPRESA, \n" +
				"CVE_EMPRESA, \n" +
				"ID_PRODUCTO, \n"+
				"NUM_CICLO, \n"+
				"ID_ACCESORIO \n" +
				"FROM SIM_PRODUCTO_CICLO_ACCESORIO \n" +
				"WHERE CVE_GPO_EMPRESA ='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
				"AND CVE_EMPRESA ='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				"AND ID_PRODUCTO ='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n"+
				"AND NUM_CICLO ='" + (String)registro.getDefCampo("NUM_CICLO") + "' \n"+
				"AND ID_ACCESORIO ='" + sIdCargoComision + "' \n";
			
			PreparedStatement ps1 = this.conn.prepareStatement(sSql);
			ps1.execute();
			ResultSet rs1 = ps1.getResultSet();

			if (!rs1.next()){
				sSql =  "INSERT INTO SIM_PRODUCTO_CICLO_ACCESORIO ( \n"+
					"CVE_GPO_EMPRESA, \n" +
					"CVE_EMPRESA, \n" +
					"ID_PRODUCTO, \n"+
					"NUM_CICLO, \n" +
					"ID_ACCESORIO) \n" +
					"VALUES ( \n"+
					"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("CVE_EMPRESA") + "', \n" +
					"'" + (String)registro.getDefCampo("ID_PRODUCTO") + "', \n" +
					"'" + (String)registro.getDefCampo("NUM_CICLO") + "', \n" +
					sIdCargoComision + ") \n" ;
					
				PreparedStatement ps2 = this.conn.prepareStatement(sSql);
				ps2.execute();
				ps2.close();
			}
			rs1.close();
			ps1.close();	
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
		ResultadoCatalogo resultadoCatalogo = new 	ResultadoCatalogo();
		sSql =  " UPDATE SIM_PRODUCTO_CICLO SET "+
			" ID_FORMA_DISTRIBUCION     	= '" + (String)registro.getDefCampo("ID_FORMA_DISTRIBUCION")  + "', \n" +
			" PLAZO     			= '" + (String)registro.getDefCampo("PLAZO")  + "', \n" +
			" TIPO_TASA                     = '" + (String)registro.getDefCampo("TIPO_TASA")  + "', \n" +
			" ID_PERIODICIDAD_TASA    	= '" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA")  + "', \n" +
			" VALOR_TASA     		= '" + (String)registro.getDefCampo("VALOR_TASA")  + "', \n" +
			" ID_TASA_REFERENCIA            = '" + (String)registro.getDefCampo("ID_TASA_REFERENCIA")  + "', \n" +
			" MONTO_MAXIMO     		= '" + (String)registro.getDefCampo("MONTO_MAXIMO")  + "', \n" +
			" PORC_FLUJO_CAJA    		= '" + (String)registro.getDefCampo("PORC_FLUJO_CAJA")  + "', \n" +
			" ID_TIPO_RECARGO 		= '" + (String)registro.getDefCampo("ID_TIPO_RECARGO")  + "', \n" +
			" TIPO_TASA_RECARGO 		= '" + (String)registro.getDefCampo("TIPO_TASA_RECARGO")  + "', \n" +
			" ID_PERIODICIDAD_TASA_RECARGO 	= '" + (String)registro.getDefCampo("ID_PERIODICIDAD_TASA_RECARGO")  + "', \n" +
			" TASA_RECARGO 			= '" + (String)registro.getDefCampo("TASA_RECARGO")  + "', \n" +
			" ID_TASA_REFERENCIA_RECARGO 	= '" + (String)registro.getDefCampo("ID_TASA_REFERENCIA_RECARGO")  + "', \n" +  
		    " FACTOR_TASA_RECARGO 		= '" + (String)registro.getDefCampo("FACTOR_TASA_RECARGO")  + "', \n" +
			" MONTO_FIJO_PERIODO 		= '" + (String)registro.getDefCampo("MONTO_FIJO_PERIODO")  + "' \n" +
			" WHERE ID_PRODUCTO      	= '" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
			" AND NUM_CICLO   		= '" + (String)registro.getDefCampo("NUM_CICLO") + "' \n" +
			" AND CVE_GPO_EMPRESA   	= '" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
			" AND CVE_EMPRESA   		= '" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n";
			   
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		return resultadoCatalogo;
	}
	
	/**
	 * Borra un registro.
	 * @param registro Llave primaria.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo baja(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo = new ResultadoCatalogo();
		//BORRA EL REGISTRO
		
		sSql = "DELETE FROM SIM_PRODUCTO_CICLO_ACCESORIO" +
				" WHERE ID_PRODUCTO		='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
				" AND NUM_CICLO			='" + (String)registro.getDefCampo("NUM_CICLO") + "' \n"+
				" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}
		
		sSql = "DELETE FROM SIM_PRODUCTO_CICLO" +
				" WHERE ID_PRODUCTO		='" + (String)registro.getDefCampo("ID_PRODUCTO") + "' \n" +
				" AND NUM_CICLO			='" + (String)registro.getDefCampo("NUM_CICLO") + "' \n"+
				" AND CVE_EMPRESA		='" + (String)registro.getDefCampo("CVE_EMPRESA") + "' \n"+
				" AND CVE_GPO_EMPRESA		='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n";
		
		//VERIFICA SI DIO DE ALTA EL REGISTRO
		if (ejecutaUpdate() == 0){
			resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
		}

		return resultadoCatalogo;
	}

}