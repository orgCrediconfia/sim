/**
 * Sistema de administración de portales.
 *
 * Copyright (c) 2003 Rapidisist S.A de C.V. Todos los derechos reservados
 */

package com.rapidsist.portal.cliente;

import com.rapidsist.portal.catalogos.CatalogoSL;
import com.rapidsist.comun.bd.Registro;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.naming.Context;
import java.rmi.RemoteException;

/**
 * Interface que estandariza la forma en que se ejecutan los servicios
 * de alta, baja o modificación de datos para las clases CON. Las operacion soportadas
 * por este método son: alta de datos (CON_ALTA), baja de un
 * registro (CON_BAJA) o modificación (CON_MODIFICACION).
 */
public interface CatalogoControlActualizaIN extends CatalogoControl{

	/**
	 * Activa los servicios de alta, baja o modificación de datos para las clases CON.
	 * @param registro Parámetros por default para la clase CON.
	 * @param request Objeto que provee de información al servlet sobre el request del cliente. El
	 * contenedor de servlets crea un objeto HttpServletRequest y lo envía como un parámetro a este método.
	 * @param response Objeto que asiste al servlet en enviar una respuesta HTTP al cliente. El
	 * contenedor  de servlets crea un objeto HttpServletResponse y lo envía como parámetro a este método.
	 * @param config Objeto ServletConfig.
	 * @param catalogoSL Interfase remota para el EJB de catálogos.
	 * @param contexto Objeto Context del servidor de aplicaciones.
	 * @param iTipoOperacion Tipo de operación a realizar sobre la clase CON.
	 * @return Resultado del alta, baja o modificación de datos y la página a donde se redirecciona el control.
	 * @throws RemoteException Si hubo algún error en el EJB de catálogos.
	 * @throws java.lang.Exception Si hubo algún error en la clase controladora.
	 */
	RegistroControl actualiza(Registro registro, HttpServletRequest request, HttpServletResponse response, ServletConfig config, CatalogoSL catalogoSL, Context contexto, int iTipoOperacion) throws RemoteException, Exception;
}