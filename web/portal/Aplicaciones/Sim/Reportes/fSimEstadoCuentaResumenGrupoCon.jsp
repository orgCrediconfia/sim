<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 

<Portal:Pagina funcion="SimEstadoCuentaResumenGrupo">

	<Portal:PaginaNombre titulo="Reporte de estado de cuenta grupal resumido" subtitulo="Consulta del reporte"/>
	
	<Portal:Forma tipo='url' funcion='SimEstadoCuentaResumenGrupo' url="ProcesaReporte?Funcion=SimEstadoCuentaResumenGrupo&TipoReporte=Xls" agregaentorno="false">
		<Portal:FormaSeparador nombre="Filtros"/>
		
	          <Portal:FormaElemento etiqueta='Clave del préstamo' control='Texto' controlnombre='CvePrestamoGrupo' controllongitud='20' controllongitudmax='10' editarinicializado='true'/>
              <Portal:FormaElemento etiqueta='Nombre del grupo' control='Texto' controlnombre='NomGrupo' controllongitud='30' controllongitudmax='100' editarinicializado='true'/>
 
		<Portal:FormaBotones>
                      <input type="button" name="Imprimir" value="Reporte en Excel" onClick="javascript:fReporteXls();">
        </Portal:FormaBotones>
		
	</Portal:Forma>
	
	

	 <script>
      function fReporteXls(){
      
              if (document.frmRegistro.CvePrestamoGrupo.value == ""&& document.frmRegistro.NomGrupo.value=="" ){
      		    alert ("La clave de préstamo grupal o el nombre del grupo son obligatorios");
      		  }else{
              
              url = '/portal/ProcesaReporte?Funcion=SimEstadoCuentaResumenGrupo&TipoReporte=Xls&CvePrestamoGrupo='+document.frmRegistro.CvePrestamoGrupo.value+'&NomGrupo='+document.frmRegistro.NomGrupo.value;
              MM_openBrWindow(url,'Reporte','status=yes,scrollbars=yes,resizable=yes,width=700,height=400');
             }
      }
     </script>      
     
</Portal:Pagina>     