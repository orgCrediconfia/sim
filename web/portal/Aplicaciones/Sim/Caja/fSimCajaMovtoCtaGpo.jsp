<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimCajaMovimientoCuenta">
	<Portal:PaginaNombre titulo="<%=request.getParameter("NomMovimientoCaja")%>" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCajaMovimientoCuenta' parametros='IdCaja=${param.IdCaja}&Nombre=${param.Nombre}&CveNombre=${param.CveNombre}&CveMovimientoCaja=${param.CveMovimientoCaja}&NomMovimientoCaja=${param.NomMovimientoCaja}&IdPrestamo=${param.IdPrestamo}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Integrante del grupo que entrega' control='selector' controlnombre='NomCompleto' controlvalor='' editarinicializado='true' obligatorio='true' campoclave="ID_PERSONA" campodescripcion="NOM_COMPLETO" datosselector='${requestScope.ListaIntegrantes}'/>
		<Portal:FormaElemento etiqueta='Monto' control='Texto' controlnombre='Monto' controlvalor='' controllongitud='6' controllongitudmax='10'/>
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>