<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="HerramientasConfiguracionPortalDefault">
	<Portal:PaginaNombre titulo="Portales default" subtitulo="Alta"/>
	
	<Portal:Forma tipo='catalogo' funcion='HerramientasConfiguracionPortalDefault' operacion='IN'>
		<Portal:FormaSeparador nombre="Paso 1: seleccione el grupo de empresas"/>
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
			<Portal:Url tipo='catalogo-ventana' url='/ProcesaCatalogo?Funcion=HerramientasConfiguracionGrupoEmpresas&OperacionCatalogo=CT&Filtro=Todos' nombreliga='Grupo de empresas' nomventana='VentanaCp'/>
		</Portal:FormaElemento>
		<Portal:FormaElemento etiqueta='Clave' control='etiqueta-controlreferencia' controlnombre='CveGpoEmpresa' controlvalor='' />
		<Portal:FormaElemento etiqueta='Nombre' control='etiqueta-controlreferencia' controlnombre='NomGpoEmpresa' controlvalor='' />
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
		</Portal:FormaBotones>
		<input type="hidden" name="CveGpoEmpresa" value='' />			
	</Portal:Forma>
	<script>
		fAgregaCampo('CveGpoEmpresa', 'Grupo de empresa');
	</script>
</Portal:Pagina>