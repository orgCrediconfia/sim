<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimCajaMovimientoCuenta">
	<Portal:PaginaNombre titulo="<%=request.getParameter("NomMovimientoCaja")%>" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCajaMovimientoCuenta' parametros='IdCaja=${param.IdCaja}&Nombre=${param.Nombre}&CveNombre=${param.CveNombre}&CveMovimientoCaja=${param.CveMovimientoCaja}&NomMovimientoCaja=${param.NomMovimientoCaja}&IdPrestamo=${param.IdPrestamo}&AplicaA=${param.AplicaA}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Cliente' control='etiqueta-controlreferencia' controlnombre='IdPersona' controlvalor='${param.Nombre}' controllongitud='45' controllongitudmax='60' editarinicializado='false' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Monto' control='Texto' controlnombre='Monto' controlvalor='' controllongitud='6' controllongitudmax='10'/>
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>