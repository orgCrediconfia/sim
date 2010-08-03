<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoMovimientoExtraordinario">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Movimientos Extraordinarios" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimCatalogoMovimientoExtraordinario' parametros='IdMovimientoExtra=${param.IdMovimientoExtra}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controloculto' controlnombre='IdMovimientoExtra' controlvalor='${param.IdMovimientoExtra}' controllongitud='5' controllongitudmax='5' editarinicializado='false' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomMovimientoExtra' controlvalor='${requestScope.registro.campos["NOM_MOVIMIENTO_EXTRA"]}' controllongitud='80' controllongitudmax='100' />
		<Portal:FormaElemento etiqueta='Tipo de movimiento' control='selector' controlnombre='IdTipoMovimiento' controlvalor='${requestScope.registro.campos["ID_TIPO_MOVIMIENTO"]}' editarinicializado='true' obligatorio='false' campoclave="ID_TIPO_MOVIMIENTO" campodescripcion="NOM_TIPO_MOVIMIENTO" datosselector='${requestScope.ListaTipoMovmiento}'/>	
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
</Portal:Pagina>	