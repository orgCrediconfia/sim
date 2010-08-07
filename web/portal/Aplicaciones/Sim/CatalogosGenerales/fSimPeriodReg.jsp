<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoPeriodicidad">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Periodicidad" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoPeriodicidad'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdPeriodicidad' controlvalor='${param.IdPeriodicidad}' controllongitud='5' controllongitudmax='5' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomPeriodicidad' controlvalor='${requestScope.registro.campos["NOM_PERIODICIDAD"]}' controllongitud='25' controllongitudmax='20' />
		<Portal:FormaElemento etiqueta='Cantidad de pagos' control='Texto' controlnombre='CantidadPagos' controlvalor='${requestScope.registro.campos["CANTIDAD_PAGOS"]}' controllongitud='3' controllongitudmax='3' validadato='numerico' />
		<Portal:FormaElemento etiqueta='D&iacute;as' control='Texto' controlnombre='Dias' controlvalor='${requestScope.registro.campos["DIAS"]}' controllongitud='4' controllongitudmax='4' validadato='numerico'/>
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
	
</Portal:Pagina>	