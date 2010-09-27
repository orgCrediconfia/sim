<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<Portal:Pagina funcion="SimUsuarioSucursal">

	<Portal:PaginaNombre titulo="Sucursales" subtitulo="Consulta de datos"/>
	
	<Portal:TablaForma nombre="Consulta de Sucursales" funcion="SimUsuarioSucursal" operacion="AL" parametros='CveUsuario=${param.CveUsuario}&IdPersona=${param.IdPersona}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Seleccione'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Sucursal'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='' control='checkbox' controlnombre='SucursalAlta${registro.campos["ID_SUCURSAL"]}'>
					<input type="hidden" name="IdSucursal" value='<c:out value='${registro.campos["ID_SUCURSAL"]}'/>'>		
				</Portal:Columna>			
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["ID_SUCURSAL"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_SUCURSAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
			<Portal:Boton tipo='submit' etiqueta='Alta' />
		</Portal:FormaBotones>				
	</Portal:TablaForma>
</Portal:Pagina>	