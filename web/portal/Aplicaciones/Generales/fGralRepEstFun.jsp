<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="GeneralesReporteEstadisticasFunciones">
	
	<Portal:PaginaNombre titulo="Reporte" subtitulo="Estadística de acceso a funciones"/>
	<Portal:Forma tipo='url' funcion='GeneralesReporteEstadsticasFunciones' url="ProcesaReporte?Funcion=GeneralesReporteEstadisticasFunciones&TipoReporte=Pdf"agregaentorno="false">
	
		<Portal:FormaSeparador nombre="Filtros"/>
		
		<Portal:FormaElemento etiqueta='Grupo de Empresa' control='selector' controlnombre='CveGpoEmpresa' controlvalor='' editarinicializado='true' obligatorio='false' campoclave="CVE_GPO_EMPRESA" campodescripcion="CVE_GPO_EMPRESA" datosselector='${requestScope.ListaGEmpresa}' />
		<Portal:FormaElemento etiqueta='Clave de función' control='selector' controlnombre='CveFuncion' controlvalor='' editarinicializado='true' obligatorio='false' campoclave="CVE_FUNCION" campodescripcion="CVE_FUNCION" datosselector='${requestScope.ListaCFunciones}' />
		<Portal:Calendario etiqueta='Fecha inicio' contenedor='frmRegistro' controlnombre='FechaIni' controlvalor='' esfechasis='false'/>
		<Portal:Calendario etiqueta='Fecha fin' contenedor='frmRegistro' controlnombre='FechaFin' controlvalor='' esfechasis='false'/>			
		
		
		<Portal:FormaBotones>
			<input type="button" name="Imprimir" value="Aceptar" onclick="javascript:fAceptar();" >
		</Portal:FormaBotones>
		
	</Portal:Forma>

	<script>
	//VERIFICA SI SE LLENO AL MENOS UN FILTRO		
		function fAceptar(){
				if (frmRegistro.FechaIni.value == "" ||frmRegistro.FechaFin.value == "" &frmRegistro.CveGpoEmpresa.value == "null" &frmRegistro.CveFuncion.value == "null") {
					alert("Llene al menos ambas fechas");
				}
				else{
					fImprimeReporte();
				}	
			}	
	</script>
	
	<script>
	// ABRE UNA NUEVA VENTANA Y ES NECESARIO LE PASA PARÁMETROS 
     		function fImprimeReporte(){
				url = '/portal/ProcesaReporte?Funcion=GeneralesReporteEstadisticasFunciones&TipoReporte=Pdf&CveGpoEmpresa='+document.frmRegistro.CveGpoEmpresa.value+'&CveFuncion='+document.frmRegistro.CveFuncion.value+'&FechaIni='+document.frmRegistro.FechaIni.value+'&FechaFin='+document.frmRegistro.FechaFin.value;
				MM_openBrWindow(url,'ReporteEstadisticaFunciones','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
			}
	</script>

</Portal:Pagina>