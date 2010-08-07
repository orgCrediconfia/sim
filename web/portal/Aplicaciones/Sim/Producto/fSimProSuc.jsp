<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<Portal:Pagina funcion="SimProductoSucursal">

	<Portal:PaginaNombre titulo="Sucursales" subtitulo="Consulta de datos"/>
	
	<Portal:TablaForma nombre="Consulta de sucursales" funcion="SimProductoSucursal" operacion="AL" parametros='IdProducto=${param.IdProducto}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaSucursal}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='FuncionAlta${registro.campos["ID_SUCURSAL"]}' />		
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_SUCURSAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_SUCURSAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
	
</Portal:Pagina>	