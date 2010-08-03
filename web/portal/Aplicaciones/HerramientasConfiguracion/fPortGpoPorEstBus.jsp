<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionPortalDefault">

	<Portal:PaginaNombre titulo="Portales y estilos disponibles" subtitulo="Selecci&oacute;n de datos"/>

	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionPortalDefault' operacion='CT' filtro='FuncionesDisponibles'>
		<Portal:FormaSeparador nombre="Filtro de b&uacute;squeda"/>
		<Portal:FormaElemento etiqueta='Clave grupo empresa' control='etiqueta' controlvalor='${param.CveGpoEmpresa}'/>
		<Portal:FormaBotones>
		</Portal:FormaBotones>		
	</Portal:Forma>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta de portales con sus estilos correspondientes" botontipo="url" url="/">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='50%' valor='Clave portal'/>
			<Portal:Columna tipovalor='texto' ancho='50%' valor='Clave estilo'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaPortalEstilo}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='50%' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["CVE_PORTAL"]}' funcion='' operacion='CR' parametros='' parametrosregreso='\'${registro.campos["CVE_PORTAL"]}\', \'${registro.campos["CVE_ESTILO"]}\''/>
				</Portal:Columna>				
				<Portal:Columna tipovalor='texto' ancho='50%' valor='${registro.campos["CVE_ESTILO"]}'/>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
	<script languaje="javascript">
		function RegresaDatos(CvePortal, CveEstilo){
			var padre = window.opener;
			padre.document.frmRegistro.CvePortal.value = CvePortal;
			padre.document.frmRegistro.CveEstilo.value = CveEstilo;			
			padre.document.getElementById('CvePortal').innerHTML = CvePortal;
			padre.document.getElementById('CveEstilo').innerHTML = CveEstilo;
			
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
	</script>	

</Portal:Pagina>
