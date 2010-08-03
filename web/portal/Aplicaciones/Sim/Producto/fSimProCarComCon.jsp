<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimProductoCargoComision">
	<Portal:PaginaNombre titulo="Cat&aacute;logo de cargos y comisiones" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimProductoCargoComision' operacion='CT' filtro='Todos' parametros='IdProducto=${param.IdProducto}'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdCargoComision' controllongitud='5' controllongitudmax='5'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomCargoComision' controllongitud='30' controllongitudmax='50'/>
	</Portal:Forma>
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/Aplicaciones/Sim/CatalogosGenerales/fSimCarComReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Beneficiarios'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_ACCESORIO"]}' funcion='SimCatalogoCargoComision' operacion='CR'parametros='IdCargoComision=${registro.campos["ID_ACCESORIO"]}' parametrosregreso='\'${registro.campos["ID_ACCESORIO"]}\',\'${registro.campos["NOM_ACCESORIO"]}\''/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_ACCESORIO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["BENEFICIARIO"]}'/>
			</Portal:TablaListaRenglon>	
		</c:forEach>
	</Portal:TablaLista>
	
	<script>
		function RegresaDatos(IdCargoComision,NomCargoComision){
			var padre = window.opener;
			padre.document.frmRegistro.IdCargoComision.value = IdCargoComision;
			padre.document.getElementById('NomCargoComision').innerHTML = NomCargoComision;
			
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
	</script>
	
</Portal:Pagina>
