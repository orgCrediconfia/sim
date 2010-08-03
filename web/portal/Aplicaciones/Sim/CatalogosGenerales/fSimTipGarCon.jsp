<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimCatalogoTipoGarantia">
	
	<Portal:PaginaNombre titulo="Cat&aacute;logo de Tipo de Garant&iacute;a" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimCatalogoTipoGarantia' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdTipoGarantia' controllongitud='22' controllongitudmax='20'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomTipoGarantia' controllongitud='22' controllongitudmax='20'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/Aplicaciones/Sim/CatalogosGenerales/fSimTipGarReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='200' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>	
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='200' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_TIPO_GARANTIA"]}' funcion='SimCatalogoTipoGarantia' operacion='CR'parametros='IdTipoGarantia=${registro.campos["ID_TIPO_GARANTIA"]}' parametrosregreso='\'${registro.campos["ID_TIPO_GARANTIA"]}\',\'${registro.campos["NOM_TIPO_GARANTIA"]}\''/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_TIPO_GARANTIA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
	
	<script>
		function RegresaDatos(IdTipoGarantia,NomTipoGarantia){
			var padre = window.opener;
			padre.document.frmRegistro.IdTipoGarantia.value = IdTipoGarantia;
			padre.document.getElementById('NomTipoGarantia').innerHTML = NomTipoGarantia;
			
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
	</script>
	
</Portal:Pagina>
