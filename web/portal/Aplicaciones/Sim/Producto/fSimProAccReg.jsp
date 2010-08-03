<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimProductoAccesorio">
	<Portal:PaginaNombre titulo="Accesorios" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimProductoAccesorio' parametros='IdProducto=${param.IdProducto}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='Orden de los accesorios' control='Texto' controlnombre='Orden' controlvalor='${requestScope.registro.campos["ORDEN"]}' controllongitud='3' controllongitudmax='3' editarinicializado='true' obligatorio='true'/>
		<Portal:FormaElemento etiqueta='Accesorios' control='selector' controlnombre='IdAccesorio' controlvalor='${requestScope.registro.campos["ID_ACCESORIO"]}' editarinicializado='true' obligatorio='false' campoclave="ID_ACCESORIO" campodescripcion="NOM_ACCESORIO" datosselector='${requestScope.ListaAccesorio}'/>	
		
		<Portal:FormaBotones>
			<c:if test='${(requestScope.registro == null)}'>
				<Portal:FormaBotonAltaModificacion/>
			</c:if>
			<c:if test='${(requestScope.registro != null)}'>
				<input type="button" name="Aceptar" value="Aceptar" onClick='history.go(-1)'>
			</c:if>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
		
	</Portal:Forma>
</Portal:Pagina>	