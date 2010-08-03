<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="GeneralesPersonaEmpresa">
	<Portal:PaginaNombre titulo="Personas" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='GeneralesPersonaEmpresa' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Nombre Completo' control='Texto' controlnombre='NomCompleto' controlvalor='${param.NomCompleto}' controllongitud='80' controllongitudmax='256' editarinicializado='true' obligatorio='true'>
			
		</Portal:FormaElemento>
		<Portal:FormaElemento etiqueta='RFC' control='Texto' controlnombre='Rfc' controlvalor='${param.Rfc}' controllongitud='30' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Personalidad fiscal' control='selector' controlnombre='PersonaFisica' controlvalor='${param.PersonaFisica}' editarinicializado='true' obligatorio='false' campoclave="Clave" campodescripcion="Descripcion" datosselector='${requestScope.ListaPersonalidad}'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta personas" botontipo="url" url='/ProcesaCatalogo?Funcion=GeneralesPersonaEmpresa&OperacionCatalogo=IN&Filtro=Alta&CveGpoEmpresa=${param.CveGpoEmpresa}'>
		<Portal:TablaListaTitulos> 
		    <Portal:Columna tipovalor='texto' ancho='10%' valor='Id Persona'/>
			<Portal:Columna tipovalor='texto' ancho='70%' valor='Nombre Completo'/>	
			<Portal:Columna tipovalor='texto' ancho='30%' valor='RFC'/>		
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='10%' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_PERSONA"]}' funcion='GeneralesPersonaEmpresa' operacion='CR' parametros='IdPersona=${registro.campos["ID_PERSONA"]}&CveGpoEmpresa=${registro.campos["CVE_GPO_EMPRESA"]}' parametrosregreso='\'${registro.campos["ID_PERSONA"]}\', \'${registro.campos["CVE_EMPRESA"]}\', \'${registro.campos["NOM_COMPLETO"]}\''/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='70%' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='30%' valor='${registro.campos["RFC"]}'/>
				
			</Portal:TablaListaRenglon>
		</c:forEach>		
	</Portal:TablaLista>
	
	<script>
		function RegresaDatos(IdPersona, CveEmpresa, NomCompleto){
			var padre = window.opener;
			padre.document.frmRegistro.IdPersona.value = IdPersona;
			padre.document.getElementById('NomCompleto').innerHTML = NomCompleto;
			padre.document.frmRegistro.CveEmpresa.value = CveEmpresa;
			padre.document.getElementById('CveEmpresa').innerHTML = CveEmpresa;
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
		frmRegistro.NomCompleto.focus();
	</script>	
</Portal:Pagina>
