<%@ taglib uri="Portal" prefix="Portal" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %> 
<Portal:Pagina funcion="SimClienteIntegranteUEF">

	<Portal:PaginaNombre titulo="Integrantes de la UEF" subtitulo="Modificaci&oacute;n de datos" subtituloalta="Alta de datos"/>
	<Portal:Forma tipo='catalogo' funcion='SimClienteIntegranteUEF' parametros='IdPersona=${param.IdPersona}&IdIntegrante=${param.IdIntegrante}&IdExConyuge=${param.IdExConyuge}'>
		<Portal:FormaSeparador nombre="Datos generales"/>
		
		<Portal:FormaElemento etiqueta='Apellido Paterno' control='Texto' controlnombre='ApPaterno' controlvalor='${requestScope.registro.campos["AP_PATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Apellido Materno' control='Texto' controlnombre='ApMaterno' controlvalor='${requestScope.registro.campos["AP_MATERNO"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Primer Nombre' control='Texto' controlnombre='Nombre1' controlvalor='${requestScope.registro.campos["NOMBRE_1"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Segundo Nombre' control='Texto' controlnombre='Nombre2' controlvalor='${requestScope.registro.campos["NOMBRE_2"]}' controllongitud='40' controllongitudmax='40' editarinicializado='true' obligatorio='false' />
		<Portal:FormaElemento etiqueta='Nombre Completo' control='Texto' controlnombre='NomCompleto' controlvalor='${requestScope.registro.campos["NOM_COMPLETO"]}' controllongitud='60' controllongitudmax='256' editarinicializado='true' obligatorio='true' />
		<Portal:FormaElemento etiqueta='Parentesco' control='selector' controlnombre='IdParentesco' controlvalor='${requestScope.registro.campos["ID_PARENTESCO"]}' editarinicializado='true' obligatorio='true' campoclave="ID_PARENTESCO" campodescripcion="NOM_PARENTESCO" datosselector='${requestScope.ListaParentesco}'/>			
		
		<Portal:FormaBotones>
			<Portal:FormaBotonAltaModificacion/>
			<Portal:FormaBotonBaja/>
		</Portal:FormaBotones>
	</Portal:Forma>
	
	<script>
		document.frmRegistro.ApPaterno.onblur = fLlenaNombre;
		document.frmRegistro.ApMaterno.onblur = fLlenaNombre;
		document.frmRegistro.Nombre1.onblur = fLlenaNombre;
		document.frmRegistro.Nombre2.onblur = fLlenaNombre;
		function fLlenaNombre(){
			sNomCompleto = document.frmRegistro.Nombre1.value +' '+ document.frmRegistro.Nombre2.value +' '+ document.frmRegistro.ApPaterno.value +' '+ document.frmRegistro.ApMaterno.value;
			document.frmRegistro.NomCompleto.value = sNomCompleto;
		}
	</script>
	
</Portal:Pagina>	
