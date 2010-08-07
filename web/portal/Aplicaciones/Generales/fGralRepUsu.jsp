<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="GeneralesUsuarioReporte">
	
	<Portal:PaginaNombre titulo="Reporte" subtitulo="Catálogo de Usuarios"/>
	<Portal:Forma tipo='url' funcion='GeneralesUsuarioReporte' url="ProcesaReporte?Funcion=GeneralesUsuarioReporte&TipoReporte=Pdf" agregaentorno="false">
	
		<Portal:FormaSeparador nombre="Filtros"/>
			
			<Portal:FormaElemento etiqueta='Grupo de Empresa' control='selector' controlnombre='CveGpoEmpresa' controlvalor='' editarinicializado='true' obligatorio='false' campoclave="CVE_GPO_EMPRESA" campodescripcion="CVE_GPO_EMPRESA" datosselector='${requestScope.ListaGEmpresa}' />
			<Portal:FormaElemento etiqueta='Clave de usuario' control='Texto' controlnombre='UsuarioCve' controlvalor='' controllongitud='30' controllongitudmax='256' editarinicializado='false' obligatorio='false' />
			<Portal:FormaElemento etiqueta='Localidad' control='Texto' controlnombre='Localidad' controlvalor='' controllongitud='30' controllongitudmax='256' editarinicializado='false' obligatorio='false' />			
			<Portal:FormaElemento etiqueta='Alias' control='Texto' controlnombre='Alias' controlvalor='' controllongitud='30' controllongitudmax='256' editarinicializado='false' obligatorio='false' /> 
					
		<Portal:FormaBotones>
			<input type="button" name="Imprimir" value="Aceptar" onclick="javascript:fImprimeReporte();" >
		</Portal:FormaBotones>
		
	</Portal:Forma>

	<script>
     		function fImprimeReporte(){
				url = '/portal/ProcesaReporte?Funcion=GeneralesUsuarioReporte&TipoReporte=Pdf&CveGpoEmpresa='+document.frmRegistro.CveGpoEmpresa.value+'&UsuarioCve='+document.frmRegistro.UsuarioCve.value+'&Localidad='+document.frmRegistro.Localidad.value+'&Alias='+document.frmRegistro.Alias.value;
				MM_openBrWindow(url,'ReporteUsuarios','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
			}
	</script>
	
</Portal:Pagina>