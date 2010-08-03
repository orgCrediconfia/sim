<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionGrupoEmpresas">

	<Portal:PaginaNombre titulo="Grupo de empresas" subtitulo="Consulta de datos"/>
	
	<Portal:TablaLista tipo="alta" nombre="Consulta cat&aacute;logo de grupo de empresas" botontipo="url" url="/Aplicaciones/HerramientasConfiguracion/fPortGpoReg.jsp?OperacionCatalogo=AL">
		<Portal:TablaListaTitulos>
			<Portal:Columna tipovalor='texto' ancho='100' valor='Clave'/>
			<Portal:Columna tipovalor='texto' ancho='100%' valor='Nombre'/>
		</Portal:TablaListaTitulos>
		<c:forEach var="registro" items="${requestScope.ListaBusqueda}">		
			<Portal:TablaListaRenglon>
				<Portal:Columna tipovalor='texto' ancho='100' valor='${registro.campos["CVE_GPO_EMPRESA"]}'/>
				<Portal:Columna tipovalor='texto' ancho='100%' valor=''>
					<Portal:Url tipo='catalogo' nombreliga='${registro.campos["NOM_GPO_EMPRESA"]}' funcion='HerramientasConfiguracionGrupoEmpresas' operacion='CR' parametros='CveGpoEmpresa=${registro.campos["CVE_GPO_EMPRESA"]}' parametrosregreso='\'${registro.campos["CVE_GPO_EMPRESA"]}\', \'${registro.campos["NOM_GPO_EMPRESA"]}\''/>
				</Portal:Columna>
			</Portal:TablaListaRenglon>
		</c:forEach>
	</Portal:TablaLista>
	<script languaje="javascript">
		function RegresaDatos(CveGpoEmpresa, NomGpoEmpresa){
			var padre = window.opener;
			padre.document.frmRegistro.CveGpoEmpresa.value = CveGpoEmpresa;
			padre.document.getElementById('CveGpoEmpresa').innerHTML = CveGpoEmpresa;
			padre.document.getElementById('NomGpoEmpresa').innerHTML = NomGpoEmpresa;
			
			//CIERRA ESTA VENTANA Y REGRESA EL CONTROL A LA VENTANA PADRE
			window.close();
		}
	</script>	
</Portal:Pagina>
