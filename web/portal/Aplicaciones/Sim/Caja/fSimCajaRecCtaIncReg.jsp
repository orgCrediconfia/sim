<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimCajaRecuperacionCuentaIncobrable">
	<Portal:PaginaNombre titulo="Recuperación de Cuenta Incobrable" subtitulo=""/>
	<Portal:Forma tipo='catalogo' funcion='SimCajaRecuperacionCuentaIncobrable' parametros='CveNombre=${param.CveNombre}&IdCaja=${param.IdCaja}&AplicaA=${param.AplicaA}&IdPrestamo=${param.IdPrestamo}&IdTransaccion=${param.IdTransaccion}&IdProducto=${param.IdProducto}&NumCiclo=${param.NumCiclo}'>
		<Portal:FormaSeparador nombre="Recuperación de Cuenta Incobrable"/>
		<Portal:FormaElemento etiqueta='Clave del préstamo' control='etiqueta-controloculto' controlnombre='CvePrestamo' controlvalor='${requestScope.registro.campos["CVE_PRESTAMO"]}' />
		<Portal:FormaElemento etiqueta='Nombre' control='etiqueta-controloculto' controlnombre='Nombre' controlvalor='${requestScope.registro.campos["NOMBRE"]}' />
		<Portal:FormaElemento etiqueta='Monto' control='Texto' controlnombre='Importe' controlvalor='' controllongitud='6' controllongitudmax='10'/>
		<input type="hidden" name="IdCaja" value='<c:out value='${param.IdCaja}'/>' />
		<input type="hidden" name="IdPrestamo" value='<c:out value='${param.IdPrestamo}'/>' />
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
		</Portal:FormaBotones>
	</Portal:Forma>	
</Portal:Pagina>