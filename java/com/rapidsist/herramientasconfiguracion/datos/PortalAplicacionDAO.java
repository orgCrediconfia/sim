/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.herramientasconfiguracion.datos;

import com.rapidsist.comun.bd.Conexion2;
import com.rapidsist.comun.bd.Registro;
import com.rapidsist.portal.catalogos.OperacionAlta;
import com.rapidsist.portal.catalogos.OperacionBaja;
import com.rapidsist.portal.catalogos.OperacionModificacion;
import com.rapidsist.portal.catalogos.OperacionConsultaRegistro;
import com.rapidsist.portal.catalogos.OperacionConsultaTabla;
import com.rapidsist.portal.catalogos.ResultadoCatalogo;
import java.util.LinkedList;
import java.util.Iterator;
import java.sql.SQLException;

/**
 * Administra los accesos a la base de datos para el catálogo de portal - aplicaciones.
 */
public class PortalAplicacionDAO extends Conexion2 implements OperacionAlta, OperacionBaja, OperacionModificacion, OperacionConsultaRegistro, OperacionConsultaTabla  {

	/**
	 * Obtiene un conjunto de registros tomando como base el filtro de búsqueda.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el conjunto
	 * de registros deseados.
	 * @return Lista de registros.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public LinkedList getRegistros(Registro parametros) throws SQLException{

		//OBTENEMOS EL FILTRO DE BUSQUEDA
		String sFiltro = (String)parametros.getDefCampo("Filtro");

		if (sFiltro.equals("AplicacionesDisponibles")){
			sSql = "SELECT   \n" +
				" AE.CVE_GPO_EMPRESA,  \n" +
				" AE.CVE_GPO_EMPRESA,  \n" +
				" AE.CVE_APLICACION,  \n" +
				" AE.B_BLOQUEADO,  \n" +
				" A.NOM_APLICACION,  \n" +
				" A.TX_DESC_APLICACION  \n" +
				" FROM RS_CONF_APL_GPO_EMPRESA AE, RS_CONF_APLICACION A  \n" +
				" WHERE AE.CVE_GPO_EMPRESA =  '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND AE.CVE_APLICACION = A.CVE_APLICACION  \n" +
				" AND AE.CVE_APLICACION NOT IN  \n" +
				" (SELECT  \n" +
				"  AP.CVE_APLICACION  \n" +
				"  FROM RS_CONF_PORTAL_APL AP  \n" +
				"  WHERE AP.CVE_GPO_EMPRESA =  '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND AP.CVE_PORTAL ='" + parametros.getDefCampo("CVE_PORTAL") + "')\n";
		}else{
			sSql = "SELECT " +
				" AP.CVE_GPO_EMPRESA, \n" +
				" AP.CVE_APLICACION, \n" +
				" DECODE(AP.TIPO_LETRA, 'MA','V','F','F') TIPO_LETRA, \n" +
				" A.NOM_APLICACION, \n" +
				" A.TX_DESC_APLICACION \n" +
				" FROM RS_CONF_PORTAL_APL AP, RS_CONF_APLICACION A \n" +
				" WHERE AP.CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
				" AND AP.CVE_PORTAL = '" + parametros.getDefCampo("CVE_PORTAL") + "'\n" +
				" AND AP.CVE_APLICACION = A.CVE_APLICACION \n";
		}
		ejecutaSql();
		return this.getConsultaLista();
	}

	/**
	 * Obtiene un registro en base a la llave primaria.
	 * @param parametros Parámetros que se le envían a la consulta para obtener el registro
	 * deseado.
	 * @return Los campos del registro.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public Registro getRegistro(Registro parametros) throws SQLException{
		sSql = "SELECT "+
				 " AP.CVE_GPO_EMPRESA, \n"+
				 " AP.CVE_APLICACION, \n"+
				 " AP.TIPO_LETRA, \n"+
				 " A.NOM_APLICACION, \n" +
				 " A.TX_DESC_APLICACION \n"+
			   " FROM RS_CONF_PORTAL_APL AP, RS_CONF_APLICACION A \n"+
			   " WHERE AP.CVE_GPO_EMPRESA = '" + parametros.getDefCampo("CVE_GPO_EMPRESA") + "'\n" +
				 " AND AP.CVE_PORTAL = '" + parametros.getDefCampo("CVE_PORTAL") + "'\n" +
				 " AND AP.CVE_APLICACION = '" + parametros.getDefCampo("CVE_APLICACION") + "'\n" +
				 " AND AP.CVE_APLICACION = A.CVE_APLICACION \n";
		ejecutaSql();
		return this.getConsultaRegistro();
	}

	/**
	 * Inserta un nuevo registro.
	 * @param registro Campos del nuevo registro.
	 * @return Objeto que contiene el resultado de la ejecución de este método.
	 * @throws SQLException Si se genera un error al accesar la base de datos.
	 */
	public ResultadoCatalogo alta(Registro registro) throws SQLException{
		ResultadoCatalogo resultadoCatalogo= new ResultadoCatalogo();

		String sCveAplicacion = "";
		LinkedList listaAplicaciones = (LinkedList)registro.getDefCampo("ListaAplicaciones");
		System.out.println("la lista listaAplicaciones: "+listaAplicaciones);
		if (listaAplicaciones != null){
			
			Iterator lista = listaAplicaciones.iterator();
			System.out.println("que es lista: "+lista);
			while (lista.hasNext()){
				System.out.println("lista.hasNext(): "+lista.hasNext());
				sCveAplicacion = (String)lista.next();
				System.out.println("sCveAplicacion: "+sCveAplicacion);
				sSql = "INSERT INTO RS_CONF_PORTAL_APL( \n"+
							   "CVE_GPO_EMPRESA, \n"+
							   "CVE_APLICACION, \n"+
							   "CVE_PORTAL, \n "+
							   "TIPO_LETRA) \n"+
						"VALUES ( \n"+
							"'" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "',\n" +
							"'" + sCveAplicacion + "',\n" +
							"'" + (String)registro.getDefCampo("CVE_PORTAL") + "', "+
							"'DE')" ;  
				try{
					ejecutaUpdate();
				}
				catch (Exception e){
				}
			}
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
		
		String sCveFuncion = "";
		LinkedList listaAplicaciones = (LinkedList)registro.getDefCampo("ListaAplicaciones");
		
		if (listaAplicaciones != null){
			Iterator lista = listaAplicaciones.iterator();
			while (lista.hasNext()){
				Registro registroTipoLetra = (Registro)lista.next();
				sSql = "UPDATE RS_CONF_PORTAL_APL SET \n"+
						   "TIPO_LETRA  = '" + (String)registroTipoLetra.getDefCampo("TIPO_LETRA") + "' \n" +
						"WHERE CVE_GPO_EMPRESA = '" + (String)registroTipoLetra.getDefCampo("CVE_GPO_EMPRESA") + "' \n" +
							" AND CVE_APLICACION = '" + (String)registroTipoLetra.getDefCampo("CVE_APLICACION") + "' \n"+
							" AND CVE_PORTAL = '" + (String)registroTipoLetra.getDefCampo("CVE_PORTAL") + "' \n";
				//VERIFICA SI NO SE ACTUALIZO EL REGISTRO
				
				if (ejecutaUpdate() == 0) {
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
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

		String sCveAplicacion = "";
		LinkedList listaAplicaciones = (LinkedList)registro.getDefCampo("ListaAplicaciones");
		if (listaAplicaciones != null){
			Iterator lista = listaAplicaciones.iterator();
			while (lista.hasNext()){
				sCveAplicacion = (String)lista.next();
				sSql = "DELETE FROM RS_CONF_PORTAL_APL " +
						" WHERE CVE_GPO_EMPRESA='" + (String)registro.getDefCampo("CVE_GPO_EMPRESA") + "' \n"+
						  " AND CVE_APLICACION='" + sCveAplicacion + "' \n"+
						  " AND CVE_PORTAL='" + (String)registro.getDefCampo("CVE_PORTAL") + "' \n";
				//VERIFICA SI NO SE ACTUALIZO EL REGISTRO
				if (ejecutaUpdate() == 0) {
					resultadoCatalogo.mensaje.setClave("CATALOGO_NO_OPERACION");
				}
			}
		}
		return resultadoCatalogo;
	}
}
