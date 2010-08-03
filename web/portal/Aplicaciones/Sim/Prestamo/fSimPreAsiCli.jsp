<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimPrestamoAsignaCliente">
	<Portal:PaginaNombre titulo="Clientes" subtitulo="Consulta de datos"/>
	
	<Portal:Forma tipo='busqueda' funcion='SimPrestamoAsignaCliente' operacion='CT' filtro='Todos'>
		<Portal:FormaElemento etiqueta='Clave del Cliente' control='Texto' controlnombre='IdPersona' controllongitud='20' controllongitudmax='10' editarinicializado='true'/>
		<Portal:FormaElemento etiqueta='Nombre del Cliente' control='Texto' controlnombre='NomCompleto' controllongitud='80' controllongitudmax='256' editarinicializado='true'/>
	</Portal:Forma>
	
	<Portal:TablaLista tipo="consulta" nombre="Consulta de Clientes">
		<Portal:TablaListaTitulos> 
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave del Cliente'/>
			<Portal:Columna tipovalor='texto' ancho='250' valor='Nombre del Cliente'/>	
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Asesor'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor=''>					
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["ID_PERSONA"]}' funcion='SimPrestamoAsignaCliente' operacion='CR' parametros='IdPersona=${registro.campos["ID_PERSONA"]}' parametrosregreso='\'${registro.campos["ID_PERSONA"]}\', \'${registro.campos["NOM_COMPLETO"]}\', \'${registro.campos["CVE_ASESOR_CREDITO"]}\', \'${registro.campos["NOM_ASESOR_CREDITO"]}\', \'${registro.campos["ID_SUCURSAL"]}\', \'${registro.campos["NOM_SUCURSAL"]}\''/>
				</Portal:Columna>
				<Portal:Columna tipovalor='texto' ancho='250' valor='${registro.campos["NOM_COMPLETO"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor='${registro.campos["NOM_ASESOR_CREDITO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>		
	</Portal:TablaLista>
	
	<script>
		function RegresaDatos(IdPersona, NomCompleto, CveAsesorCredito, NomAsesorCredito, IdSucursal, NomSucursal){
			var padre = window.opener;
			
			padre.document.frmRegistro.IdPersona.value  = IdPersona;
			padre.document.getElementById('NomCompleto').innerHTML = NomCompleto;
			padre.document.frmRegistro.IdAsesorCredito.value  = CveAsesorCredito;
			padre.document.getElementById('NomAsesorCredito').innerHTML = NomAsesorCredito;
			padre.document.frmRegistro.IdSucursal.value  = IdSucursal;
			padre.document.getElementById('NomSucursal').innerHTML = NomSucursal;
			
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
	</script>
		
</Portal:Pagina>
