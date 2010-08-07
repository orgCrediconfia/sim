<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionTabla">
	<Portal:PaginaNombre titulo="Tablas del Sistema" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='HerramientasConfiguracionTabla' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='CveTabla' controlvalor='${param.CveTabla}' controllongitud='30' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomTabla' controlvalor='${param.NomTabla}' controllongitud='30' controllongitudmax='256' editarinicializado='true' obligatorio='false' />	
	    <Portal:FormaElemento etiqueta='Descripción' control='Texto' controlnombre='DescTabla' controlvalor='${param.DescTabla}' controllongitud='30' controllongitudmax='256' editarinicializado='true' obligatorio='false' />
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta cat&aacute;logo de tablas del sistema" botontipo="url" url="/Aplicaciones/HerramientasConfiguracion/fTablTabReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Nombre en BD'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Descripci&oacute;n'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_TABLA"]}' funcion='HerramientasConfiguracionTabla' operacion='CR' parametros='CveTabla=${registro.campos["CVE_TABLA"]}' parametrosregreso='\'${registro.campos["CVE_TABLA"]}\', \'${registro.campos["NOM_TABLA"]}\'' />
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["NOM_TABLA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["DESC_TABLA"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
	<script>
		function RegresaDatos(CveTabla,  NomTabla){
			var padre = window.opener;
			padre.document.frmRegistro.CveTabla.value = CveTabla;
			padre.document.getElementById('CveTabla').innerHTML = CveTabla;
			padre.document.getElementById('NomTabla').innerHTML = NomTabla;
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
	</script>		
	
</Portal:Pagina>
