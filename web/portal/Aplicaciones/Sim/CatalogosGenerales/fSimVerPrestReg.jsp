<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoVerificacionPrestamo">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de verificaci&oacute;n del pr&eacute;stamo" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoVerificacionPrestamo'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdVerificacionPrestamo' controlvalor='${param.IdVerificacionPrestamo}' controllongitud='2' controllongitudmax='2' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre de verificaci&oacute;n del pr&eacute;stamo' control='Texto' controlnombre='NomVerificacionPrestamo' controlvalor='${requestScope.registro.campos["NOM_VERIFICACION_PRESTAMO"]}' controllongitud='30' controllongitudmax='50' />	
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>	