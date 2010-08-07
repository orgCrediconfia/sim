<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoSucursal">
	<Portal:PaginaNombre titulo="Catálogo de Sucursales" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoSucursal' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Id Sucursal' control='Texto' controlnombre='IdSucursal' controllongitud='20' controllongitudmax='10' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomSucursal'  controllongitud='30' controllongitudmax='30' editarinicializado='true'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta cat&aacute;logo Sucursal" botontipo="url" url="/Aplicaciones/Sim/CatalogosGenerales/fSimSucursalReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='80' valor='Id Sucursal'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='80' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_SUCURSAL"]}' funcion='SimCatalogoSucursal' operacion='CR' parametros='IdSucursal=${registro.campos["ID_SUCURSAL"]}&IdDomicilio=${registro.campos["ID_DOMICILIO"]}&IdDomicilioTribunal=${registro.campos["ID_DIRECCION_TRIBUNAL"]}'/>
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_SUCURSAL"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
</Portal:Pagina>
