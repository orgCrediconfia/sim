<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="GeneralesReporteEstadisticas">
	
	<Portal:PaginaNombre titulo="Reporte" subtitulo="Estadísticas de uso del sistema"/>
	<Portal:Forma tipo='url' funcion='GeneralesReporteEstadisticas' url="ProcesaReporte?Funcion=GeneralesReporteEstadisticas&TipoReporte=Pdf"agregaentorno="false">
	
		<Portal:FormaSeparador nombre="Filtros"/>
		
		
		<Portal:FormaElemento etiqueta='Grupo de Empresa' control='selector' controlnombre='CveGpoEmpresa' controlvalor='' editarinicializado='true' obligatorio='false' campoclave="CVE_GPO_EMPRESA" campodescripcion="CVE_GPO_EMPRESA" datosselector='${requestScope.ListaGEmpresa}' />
		<Portal:Calendario etiqueta='Fecha inicio' contenedor='frmRegistro' controlnombre='FechaIni' controlvalor='' esfechasis='false'/>
		<Portal:Calendario etiqueta='Fecha fin' contenedor='frmRegistro' controlnombre='FechaFin' controlvalor='' esfechasis='false'/>			
		<Portal:FormaElemento etiqueta='Usuario' control='Texto' controlnombre='CveUsuario' controlvalor='${requestScope.registro.campos["NOM_PERSONA"]}' controllongitud='21' controllongitudmax='21' editarinicializado='true' obligatorio='false'/>		
		
		<Portal:FormaBotones>
			<input type="button" name="Imprimir" value="Aceptar" onclick="javascript:fAceptar();" >
		</Portal:FormaBotones>
		
	</Portal:Forma>

	<script>
	//VERIFICA SI SE LLENO AL MENOS UN FILTRO
		function fAceptar(){
			if (frmRegistro.CveUsuario.value == "" && frmRegistro.FechaIni.value == "" && frmRegistro.FechaFin.value == "" && frmRegistro.CveGpoEmpresa.value == "null") {
					alert("Llene al menos un filtro");					
				}
				else{
					fImprimeReporte();
				}	
			}	
	</script>
	
	<script>
	// ABRE UNA NUEVA VENTANA Y ES NECESARIO LE PASA PARÁMETROS
     		function fImprimeReporte(){
				url = '/portal/ProcesaReporte?Funcion=GeneralesReporteEstadisticas&TipoReporte=Pdf&CveGpoEmpresa='+document.frmRegistro.CveGpoEmpresa.value+'&FechaIni='+document.frmRegistro.FechaIni.value+'&FechaFin='+document.frmRegistro.FechaFin.value+'&CveUsuario='+document.frmRegistro.CveUsuario.value;
				MM_openBrWindow(url,'ReporteEstadisticas','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
			}
	</script>
	
		

</Portal:Pagina>
