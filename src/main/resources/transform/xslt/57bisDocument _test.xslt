<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0" 
	xmlns:xalan="http://xml.apache.org/xslt">

  <xsl:output indent="yes"/>

  <xsl:param name="headers"/>
  <xsl:param name="flows" select="$headers/@flows"/>
  <xsl:param name="fundManager" select="$flows/@fundManager"/>
   

  <xsl:template match="/">
        <Documents PublicationName="CERT_FFMM_57BIS" xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="CERT_FFMM_57BIS.xsd">
            <xsl:call-template name="Documents"/>
        </Documents>
    </xsl:template>

    <xsl:template name="Documents">
        <CERT_FFMM_57BIS>
	        <DocumentIdentifier>
	        	<Empresa>0022</Empresa>
	        	<Idioma>es-CL</Idioma>
	        	<CanalMarco>003</CanalMarco>
	        </DocumentIdentifier>
	        <Num_certificado>1</Num_certificado>
	        <Razon_social>
                <xsl:value-of select="$fundManager"/>
            </Razon_social>
	        
            </CERT_FFMM_57BIS>
    </xsl:template>
</xsl:stylesheet>