<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimGrupoCoordinador">
	<Portal:PaginaNombre titulo="Coordinador" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimGrupoCoordinador' operacion='CT' filtro='Todos' parametros='IdGrupo=${param.IdGrupo}'>
		<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdPersona' controllongitud='20' controllongitudmax='20'/>
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomCompleto' controllongitud='35' controllongitudmax='35'/>
	</Portal:Forma>
	
	<Portal:TablaForma nombre="Consulta" funcion="SimGrupoCoordinador" operacion="AL" parametros='IdPersona=${param.IdPersona}'>
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaCoordinador}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_PERSONA"]}' funcion='SimGrupoCoordinador' operacion='CR' parametros='IdPersona=${registro.campos["ID_PERSONA"]}' parametrosregreso='\'${registro.campos["ID_PERSONA"]}\', \'${registro.campos["NOM_COMPLETO"]}\''/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_COMPLETO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
		<Portal:FormaBotones>
		</Portal:FormaBotones>				
	</Portal:TablaForma>
	
	<script>
		function RegresaDatos(IdPersona, NomCompleto){
			var padre = window.opener;
			padre.document.frmRegistro.IdCoordinador.value = IdPersona;
			padre.document.getElementById('NomCoordinador').innerHTML = NomCompleto;
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
		frmRegistro.NomCompleto.focus();
	</script>	
</Portal:Pagina>
