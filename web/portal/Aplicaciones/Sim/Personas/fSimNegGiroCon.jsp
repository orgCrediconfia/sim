<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimNegocioGiro">
	<Portal:PaginaNombre titulo="Giro" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimNegocioGiro' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Giro' control='Texto' controlnombre='NomSinonimo' controllongitud='150' controllongitudmax='40' editarinicializado='true'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="consulta" nombre="Consulta de giros">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='25' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='150' valor='Sin&oacute;nimo'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre del Giro'/>	
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='25' valor='${registro.campos["CVE_CLASE"]}'/>
				<Portal:Columna tipovalor='texto' ancho='150' valor='${registro.campos["NOM_SINONIMO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NOM_CLASE"]}' funcion='SimNegocioGiro' operacion='CR' parametros='CveClase=${registro.campos["CVE_CLASE"]}&NomClase=${registro.campos["NOM_CLASE"]}' parametrosregreso='\'${registro.campos["CVE_CLASE"]}\', \'${registro.campos["NOM_CLASE"]}\''/>
				</Portal:Columna>
			</Portal:TablaListaRenglon>
		</c:forEach>		
	</Portal:TablaLista>
	
	<script>
		function RegresaDatos(CveClase, NomClase){
			var padre = window.opener;
			
			padre.document.frmRegistro.CveClase.value  = CveClase;
			padre.document.getElementById('NomClase').innerHTML = NomClase;
			
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
	</script>
		
</Portal:Pagina>
