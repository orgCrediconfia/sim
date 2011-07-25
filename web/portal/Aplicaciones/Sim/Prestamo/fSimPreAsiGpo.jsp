<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoAsignaGrupo">
	<Portal:PaginaNombre titulo="Grupos" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimPrestamoAsignaGrupo' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave del Grupo' control='Texto' controlnombre='IdGrupo' controllongitud='5' controllongitudmax='10' editarinicializado='true' validadato='cantidades'/>
		<Portal:FormaElemento etiqueta='Nombre del Grupo' control='Texto' controlnombre='NomGrupo' controllongitud='30' controllongitudmax='100' editarinicializado='true'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="consulta" nombre="Consulta de grupos">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave del Grupo'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre del Cliente'/>	
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Asesor'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_GRUPO"]}' funcion='SimPrestamoAsignaGrupo' operacion='CR' parametros='IdGrupo=${registro.campos["ID_GRUPO"]}' parametrosregreso='\'${registro.campos["ID_GRUPO"]}\', \'${registro.campos["NOM_GRUPO"]}\', \'${registro.campos["CVE_ASESOR_CREDITO"]}\', \'${registro.campos["NOM_ASESOR_CREDITO"]}\', \'${registro.campos["ID_SUCURSAL"]}\', \'${registro.campos["NOM_SUCURSAL"]}\''/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_GRUPO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_ASESOR_CREDITO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>		
	</Portal:TablaLista>
	
	<script>
		function RegresaDatos(IdGrupo, NomGrupo, CveAsesorCredito, NomAsesorCredito, IdSucursal, NomSucursal){
			var padre = window.opener;
			
			padre.document.frmRegistro.IdGrupo.value  = IdGrupo;
			padre.document.getElementById('NomGrupo').innerHTML = NomGrupo;
			padre.document.frmRegistro.IdAsesorCredito.value  = CveAsesorCredito;
			padre.document.getElementById('NomAsesorCredito').innerHTML = NomAsesorCredito;
			padre.document.frmRegistro.IdSucursal.value  = IdSucursal;
			padre.document.getElementById('NomSucursal').innerHTML = NomSucursal;
			
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
	</script>
		
</Portal:Pagina>
