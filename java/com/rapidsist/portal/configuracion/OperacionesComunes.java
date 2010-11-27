/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */
 
package com.rapidsist.portal.configuracion;

import java.sql.SQLException;
import com.rapidsist.comun.bd.Registro;
import javax.naming.*;

public class OperacionesComunes {
	
	/**
	 * Obtiene la función.
	 * @param sIdFuncion Cve de la función.
	 */
	public com.rapidsist.portal.configuracion.Funcion getFuncion(String sIdFuncion){
		com.rapidsist.portal.configuracion.Funcion funcion = null;
		ConfiguracionPortalDAO configuracionDAO = new ConfiguracionPortalDAO();

		try{
			Context context = new InitialContext();
			try {
				//BUSCA EL OBJETO CON LA CONFIGURACION DEL PORTAL Y EL USUARIO EN EL JNDI
				funcion = (com.rapidsist.portal.configuracion.Funcion) context.lookup("Funcion_" + sIdFuncion);
			}
			catch (NameNotFoundException e) {
				//ABRE CONEXION A BASE DE DATOS
				configuracionDAO.abreConexion("java:comp/env/jdbc/PortalDs");

				//VERFICA SI LA FUNCION NO ESTA BLOQUEADA
				Registro funcionDatos = configuracionDAO.getFuncion(sIdFuncion);
				if (funcionDatos != null) {
					funcion = new Funcion();
					funcion.setClaseCon( (String) funcionDatos.getDefCampo("NOM_CLASE_CON"));
					funcion.setClaseDao( (String) funcionDatos.getDefCampo("NOM_CLASE_DAO"));
					funcion.setNombre( (String) funcionDatos.getDefCampo("NOM_FUNCION"));
					funcion.setNomClaseReporte( (String) funcionDatos.getDefCampo("NOM_CLASE_REPORTE"));
					funcion.setLogUsuario( (String) funcionDatos.getDefCampo("B_LOG_USUARIO"));
					funcion.setCveTabla( (String) funcionDatos.getDefCampo("CVE_TABLA"));
					funcion.setListaOperaciones(configuracionDAO.getFuncionOperaciones(sIdFuncion));
					funcion.setBRastreaCodigo((String) funcionDatos.getDefCampo("B_RASTREA_CODIGO"));
					funcion.setBBloaqueado((String) funcionDatos.getDefCampo("B_BLOQUEADO"));
					funcion.setBWebservice((String) funcionDatos.getDefCampo("B_WEBSERVICE"));
					funcion.setBBitacora((String) funcionDatos.getDefCampo("B_BITACORA"));
					funcion.setBPaginacion((String) funcionDatos.getDefCampo("B_PAGINACION"));
					try {
						//ALMACENA LA FUNCION EN EL JNDI
						context.rebind("Funcion_" + sIdFuncion, funcion);
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
				} // funcionDatos != null
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if ( configuracionDAO != null){
				if (configuracionDAO.isConectado()) {
					configuracionDAO.cierraConexion();
				}
			}
		}
		return funcion;
	}
	
	/**
	 * Obtiene el mensaje.
	 * @param sClaveMensaje Clave del mensaje.
	 * @return mensaje.
	 */
	public Mensaje getMensaje(String sClaveMensaje) {
		Mensaje mensaje = null;

		ConfiguracionPortalDAO configuracionDAO = new ConfiguracionPortalDAO();

		try{
			//ABRE CONEXION A BASE DE DATOS
			configuracionDAO.abreConexion("java:comp/env/jdbc/PortalDs");

			//VERFICA SI LA FUNCION NO ESTA BLOQUEADA
			Registro mensajeDatos = configuracionDAO.getMensaje(sClaveMensaje);
			if (mensajeDatos != null){
				mensaje = new Mensaje();
				mensaje.setClave((String)mensajeDatos.getDefCampo("CVE_MENSAJE"));
				mensaje.setDescripcion((String)mensajeDatos.getDefCampo("TX_MENSAJE"));
				mensaje.setTipo((String)mensajeDatos.getDefCampo("TIPO_MENSAJE"));
			} // funcionDatos != null
		}
		catch (SQLException ex) {
			System.out.println("Se genero una SQLException en getMensaje en PortalSLBean: " + ex.getMessage());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally{
			//VERIFICA SI HAY UNA CONEXION ABIERTA A LA BASE DE DATOS
			if ( configuracionDAO.isConectado()){
				configuracionDAO.cierraConexion();
			}
		}
		return mensaje;
	}
}
