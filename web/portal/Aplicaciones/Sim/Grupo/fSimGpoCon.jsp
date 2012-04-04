<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimGrupo">
	<Portal:PaginaNombre titulo="Grupo" subtitulo="Consulta de datos"/>
	<Portal:Forma tipo='busqueda' funcion='SimGrupo' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdGrupo' controllongitud='5' controllongitudmax='5'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomGrupo' controllongitud='30' controllongitudmax='30'/>
		<Portal:Calendario2 etiqueta='Fecha de formaci&oacute;n' contenedor='frmRegistro' controlnombre='FechaFormacion' esfechasis='false'/>
	</Portal:Forma>
	<Portal:TablaLista tipo="alta" nombre="Consulta" botontipo="url" url="/ProcesaCatalogo?Funcion=SimGrupo&OperacionCatalogo=IN&Filtro=Alta">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Fin de formaci&oacute;n'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_GRUPO"]}' funcion='SimGrupo' operacion='CR' parametros='IdGrupo=${registro.campos["ID_GRUPO"]}&ComentarioExcepcion=NO' parametrosregreso='\'${registro.campos["ID_GRUPO"]}\', \'${registro.campos["NOM_GRUPO"]}\''/>
				</Portal:Columna>	
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_GRUPO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["FECHA_FORMACION"]}'/>								
			</Portal:TablaListaRenglon>	
		</c:forEach>
	</Portal:TablaLista>
	
	<script>
		function RegresaDatos(IdGrupo, NomGrupo){
			var padre = window.opener;
			
			padre.document.frmRegistro.IdGrupo.value  = IdGrupo;
			padre.document.getElementById('NomGrupo').innerHTML = NomGrupo;
			
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
	</script>
	
</Portal:Pagina>
