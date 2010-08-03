<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimUsuarioEmpresa">
	<Portal:PaginaNombre titulo="Usuarios" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimUsuarioEmpresa' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='ClaveUsuario' controllongitud='20' controllongitudmax='256'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomCompleto' controllongitud='20' controllongitudmax='256'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta de Usuarios" botontipo="url" url='/ProcesaCatalogo?Funcion=SimUsuarioEmpresa&OperacionCatalogo=IN&Filtro=Alta'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Puesto'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_USUARIO"]}' funcion='SimUsuarioEmpresa' operacion='CR' parametros='CveUsuario=${registro.campos["CVE_USUARIO"]}&IdPersona=${registro.campos["ID_PERSONA"]}' parametrosregreso='\'${registro.campos["CVE_USUARIO"]}\', \'${registro.campos["NOM_COMPLETO"]}\''/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_PUESTO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
	
	<script>
		function RegresaDatos(CveUsuario,NomCompleto){
			var padre = window.opener;
			padre.document.frmRegistro.CveUsuario.value = CveUsuario;
			padre.document.getElementById('NomCompleto').innerHTML = NomCompleto;
			
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
	</script>

</Portal:Pagina>
