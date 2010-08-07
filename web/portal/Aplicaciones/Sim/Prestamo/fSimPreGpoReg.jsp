<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<Portal:Pagina funcion="SimPrestamoGrupal">
	<Portal:PaginaNombre titulo="Pr&eacute;stamo Grupal" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimPrestamoGrupal'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		<c:if test='${(requestScope.registro != null)}'>
			<Portal:FormaElemento etiqueta='Clave' control='Texto' controlnombre='IdPrestamo' controlvalor='${requestScope.registro.campos["ID_PRESTAMO"]}' editarinicializado='false'/>
		</c:if>
		
		<Portal:FormaElemento etiqueta='' control='etiqueta' controlvalor=''>		
		    	<a id="AsignarGrupo" href="javascript:fAsignarGrupo();">Asignar Grupo</a>			
		</Portal:FormaElemento>
		
		<Portal:FormaElemento etiqueta='Grupo' control='etiqueta-controlreferencia' controlnombre='NomGrupo' controlvalor='${requestScope.registro.campos["NOM_GRUPO"]}' editarinicializado='false'/>
		<input type="hidden" name="IdGrupo" value='<c:out value='${requestScope.registro.campos["ID_GRUPO"]}'/>' />
		
		<c:if test='${(requestScope.registro != null)}'>
			<Portal:FormaElemento etiqueta='Sucursal' control='selector' controlnombre='IdSucursal' controlvalor='${requestScope.registro.campos["ID_SUCURSAL"]}' editarinicializado='true' obligatorio='true' campoclave="ID_SUCURSAL" campodescripcion="NOM_SUCURSAL" datosselector='${requestScope.ListaSucursal}'/>
		</c:if>
		
		<c:if test='${(requestScope.registro == null)}'>
			<Portal:FormaElemento etiqueta='Sucursal' control='etiqueta-controlreferencia' controlnombre='NomSucursal' controlvalor='${requestScope.registro.campos["NOM_SUCURSAL"]}' />
			<input type="hidden" name="IdSucursal" value='<c:out value='${requestScope.registro.campos["ID_SUCURSAL"]}'/>' />
		</c:if>
		
		<Portal:FormaElemento etiqueta='Asesor crédito' control='etiqueta-controlreferencia' controlnombre='NomAsesorCredito' controlvalor='${requestScope.registro.campos["NOMBRE_ASESOR"]}' />
		<input type="hidden" name="IdAsesorCredito" value='<c:out value='${requestScope.registro.campos["CVE_ASESOR_CREDITO"]}'/>' />
		
		<Portal:FormaElemento etiqueta='Producto' control='selector' controlnombre='IdProducto' controlvalor='${requestScope.registro.campos["ID_PRODUCTO"]}' editarinicializado='true' obligatorio='false' campoclave="ID_PRODUCTO" campodescripcion="NOM_PRODUCTO" datosselector='${requestScope.ListaProducto}'/>
		
		<Portal:FormaBotones>
			<input type='button' name='Aceptar' value='Aceptar' onClick='fAceptar()'/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>	
			
	<script>
		function fAceptar(){
			if (document.frmRegistro.IdProducto.value == "null"){
				alert("Debe asignar un producto");
			}else if (document.frmRegistro.IdGrupo.value == ""){
				alert("Debe asignar un grupo");
			}else{			
				document.frmRegistro.action="ProcesaCatalogo?Funcion=SimPrestamoGrupal&OperacionCatalogo=AL&IdGrupo="+document.frmRegistro.IdGrupo.value+"&IdProducto="+document.frmRegistro.IdProducto.value;
				document.frmRegistro.submit();
			}
		}
		
		function fAsignarGrupo(){
			MM_openBrWindow('/portal/ProcesaCatalogo?Funcion=SimPrestamoAsignaGrupo&OperacionCatalogo=IN&Filtro=Inicio&Ventana=Si','Acepta','status=yes,scrollbars=yes,resizable=yes,width=700,height=500');
		}
		
	</script>
</Portal:Pagina>	