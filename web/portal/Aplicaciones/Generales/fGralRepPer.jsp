<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="GeneralesPersonaReporte">
	
	<Portal:PaginaNombre titulo="Reporte" subtitulo="Catálogo de personas"/>
	<Portal:Forma tipo='url' funcion='GeneralesPersonaReporte' url="ProcesaReporte?Funcion=GeneralesPersonaReporte&TipoReporte=Pdf" agregaentorno="false">
	
		<Portal:FormaSeparador nombre="Filtros"/>
		
		<Portal:FormaElemento etiqueta='Grupo de Empresa' control='selector' controlnombre='CveGpoEmpresa' controlvalor='' editarinicializado='true' obligatorio='false' campoclave="CVE_GPO_EMPRESA" campodescripcion="CVE_GPO_EMPRESA" datosselector='${requestScope.ListaGEmpresa}' />
		<Portal:FormaElemento etiqueta='Nombre' control='Texto' controlnombre='NomCompleto' controlvalor='' controllongitud='80' controllongitudmax='256' editarinicializado='true' obligatorio='false' />			
		<Portal:FormaElemento etiqueta='Personalidad fiscal' control='selector' controlnombre='PersonaFisica' controlvalor='' editarinicializado='false' obligatorio='false' campoclave="Clave" campodescripcion="Descripcion" datosselector='${requestScope.ListaPersonalidad}' />
		<Portal:FormaElemento etiqueta='RFC' control='Texto' controlnombre='Rfc' controlvalor='' controllongitud='30' controllongitudmax='100' editarinicializado='true' obligatorio='false' />
		
		<Portal:FormaBotones>
			<input type="button" name="Imprimir" value="Aceptar" onclick="javascript:fImprimeReporte();" >
		</Portal:FormaBotones>
		
	</Portal:Forma>

	<script>
	// ABRE UNA NUEVA VENTANA Y ES NECESARIO LE PASA PARÁMETROS
     		function fImprimeReporte(){
				url = '/portal/ProcesaReporte?Funcion=GeneralesPersonaReporte&TipoReporte=Pdf&CveGpoEmpresa='+document.frmRegistro.CveGpoEmpresa.value+'&Rfc='+document.frmRegistro.Rfc.value+'&PersonaFisica='+document.frmRegistro.PersonaFisica.value+'&NomCompleto='+document.frmRegistro.NomCompleto.value;
				MM_openBrWindow(url,'ReportePersonas','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
			}
	</script>
	
</Portal:Pagina>
