<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimProductoSucursal" precarga="SimProductoSucursal">
	<Portal:PaginaNombre titulo="Sucursales" subtitulo="Consulta de datos"/>
	<Portal:TablaForma maestrodetallefuncion="SimProductoSucursal" nombre="Sucursales" funcion="SimProductoSucursal" operacion="BA" parametros='IdProducto=${param.IdProducto}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_SUCURSAL"]}' />		
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_SUCURSAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_SUCURSAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='url' etiqueta='Alta' url='/ProcesaCatalogo?Funcion=SimProductoSucursal&OperacionCatalogo=IN&IdProducto=${param.IdProducto}'/>
			<Portal:Boton tipo='submit' etiqueta='Baja' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
</Portal:Pagina>
