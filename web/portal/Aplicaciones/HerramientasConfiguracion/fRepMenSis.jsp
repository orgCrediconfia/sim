<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="HerramientasConfiguracionMensajeReporte">
	
	<Portal:PaginaNombre titulo="Reporte" subtitulo="Mensajes del sistema"/>
	<Portal:Forma tipo='url' funcion='HerramientasConfiguracionMensajeReporte' url="ProcesaReporte?Funcion=HerramientasConfiguracionMensajeReporte&TipoReporte=Pdf" agregaentorno="false">
	
		<Portal:FormaSeparador nombre="Filtros"/>
		
		<Portal:FormaElemento etiqueta='Clave de mensaje' control='Texto' controlnombre='CveMensaje' controlvalor='' controllongitud='30' controllongitudmax='256' editarinicializado='false' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Tipo de mensaje' control='Texto' controlnombre='Tipo' controlvalor='' controllongitud='30' controllongitudmax='256' editarinicializado='false' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Texto del mensaje' control='Texto' controlnombre='Mensaje' controlvalor='' controllongitud='30' controllongitudmax='256' editarinicializado='false' obligatorio='false' />
		
		<Portal:FormaBotones>
			<input type="button" name="Imprimir" value="Aceptar" onclick="javascript:fImprimeReporte();" >
		</Portal:FormaBotones>
		
	</Portal:Forma>

	<script>
     		function fImprimeReporte(){
				url = '/portal/ProcesaReporte?Funcion=HerramientasConfiguracionMensajeReporte&TipoReporte=Pdf&CveMensaje='+document.frmRegistro.CveMensaje.value+'&Tipo='+document.frmRegistro.Tipo.value+'&Mensaje='+document.frmRegistro.Mensaje.value;
				MM_openBrWindow(url,'ReporteMensajesSistema','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
			}
	</script>
	
</Portal:Pagina>

