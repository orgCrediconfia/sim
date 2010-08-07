<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimGaranteDepositario">
	<Portal:PaginaNombre titulo="Garante Depositario" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimGaranteDepositario' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdPersona' controllongitud='20' controllongitudmax='10' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomCompleto' controllongitud='80' controllongitudmax='256' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='RFC' control='Texto' controlnombre='Rfc' controllongitud='30' controllongitudmax='100' editarinicializado='true'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta de Garantes Depositarios" botontipo="url" url='/ProcesaCatalogo?Funcion=SimGaranteDepositario&OperacionCatalogo=IN&Filtro=Alta'>
		<Portal:TablaListaTitulos> 
		    <Portal:Columna tipovalor='texto' ancho='25' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre'/>	
			<Portal:Columna tipovalor='texto' ancho='100%' valor='RFC'/>		
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='25' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_PERSONA"]}' funcion='SimGaranteDepositario' operacion='CR' parametros='IdPersona=${registro.campos["ID_PERSONA"]}' parametrosregreso='\'${registro.campos["ID_PERSONA"]}\', \'${registro.campos["NOM_COMPLETO"]}\''/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["RFC"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>		
	</Portal:TablaLista>	
	
	<script>
		function RegresaDatos(IdPersona, NomCompleto){
			var padre = window.opener;
			
			padre.document.frmRegistro.IdPersonaGarante.value  = IdPersona;
			padre.document.getElementById('NomCompletoGarante').innerHTML = NomCompleto;
			
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
	</script>
	
</Portal:Pagina>
