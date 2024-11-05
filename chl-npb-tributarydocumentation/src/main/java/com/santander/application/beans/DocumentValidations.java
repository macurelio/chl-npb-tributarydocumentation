package com.santander.application.beans;

import org.apache.camel.Exchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Result;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.santander.application.model.exception.ContextException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unchecked")
public class DocumentValidations {

    static final String FLOWS_HEADERS = "flows";
    static final String FLOWS_HEADERS_CONTEXT = "context";
    static final String HTTP_STATUS_CODE_RESPONSE = "HttpStatusCodeResponse";

    public void  bis57DocumentDataFormat(Exchange __exchange){
        
        Map<String, Object> flows = (Map<String, Object>)__exchange.getProperty("flows");
        System.out.println("ACA EL LOG QUE QUEREMOS VER LOCOOOOOOOOOOOOO-------------"+ __exchange.toString());
        //Map<String, Object> session = (Map<String, Object>) flows.get("session");
        //Map<String, Object> login = (Map<String, Object>) session.get("login");
        //Map<String, Object> requestBody = (Map<String, Object>) flows.get("request-body");
        Map<String, Object> bis57Data = (Map<String, Object>) flows.get("57bis-data");
        Map<String, Object> response = (Map<String, Object>) bis57Data.get("response");
        Map<String, Object> request = (Map<String, Object>) bis57Data.get("request");
        Map<String, Object> headers = (Map<String, Object>) response.get("headers");
        Map<String, Object> body = (Map<String, Object>) response.get("body");
        Map<String, Object> customerData = (Map<String, Object>) body.get("customerData");
        List<Map<String, Object>> bis57DataResponse = (List<Map<String, Object>>) body.get("57bis-Data");
        String issueDate = "Santiago, 01 de Noviembre del 2024";
        //Map<String, Object> bis57DataResponse = (Map<String, Object>) response.get("57bisData");

        //$headers.properties.flows.session.login.username
        //customer_data
        /*flows.put("fundManagerDescription", customerData.get("fundManagerDescription"));
        flows.put("fundManagerId", customerData.get("fundManagerId"));
        flows.put("category", customerData.get("category"));
        flows.put("address", customerData.get("adress"));
        flows.put("manager", customerData.get("manager"));
        flows.put("managerId", customerData.get("managerId"));
        //flows.put("customerId", customerData.get("customerId"));
        flows.put("managerId", customerData.get("managerId"));
        flows.put("customerName", customerData.get("customerName"));
        flows.put("customerAdress", customerData.get("customerAdress"));
        flows.put("issueDate", issueDate);
        flows.put("customerRut", login.get("username"));
        */
        //System.out.println("CLASSPATH: " +System.getProperty("java.class.path") );
        //System.out.println("ACA EL LOG QUE QUEREMOS VER LOCOOOOOOOOOOOOO-------------"+ __exchange.toString());

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // Elemento raíz
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Documents");
            doc.appendChild(rootElement);
        
            rootElement.setAttributeNode(createAttr(doc, "PublicationName", "CERT_FFMM_57BIS"));
            rootElement.setAttributeNode(createAttr(doc, "xmlns:xalan", "http://xml.apache.org/xslt"));
            rootElement.setAttributeNode(createAttr(doc, "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance"));
            rootElement.setAttributeNode(createAttr(doc, "xsi:noNamespaceSchemaLocation", "CERT_FFMM_57BIS.xsd"));
        
            Element cert = doc.createElement("CERT_FFMM_57BIS");
            rootElement.appendChild(cert);
        
            Element documentIdentifier = doc.createElement("DocumentIdentifier");
            cert.appendChild(documentIdentifier);
        
            documentIdentifier.appendChild(createElement(doc, "Empresa", "0022"));
            documentIdentifier.appendChild(createElement(doc, "Idioma", "es-CL"));
            documentIdentifier.appendChild(createElement(doc, "CanalMarco", "003"));
        
            cert.appendChild(createElement(doc, "Num_certificado", "12345467"));
            cert.appendChild(createElement(doc, "Fecha_emision", issueDate));
            cert.appendChild(createElement(doc, "Razon_social", customerData.get("fundManagerDescription").toString()));
            cert.appendChild(createElement(doc, "Numero_rut", customerData.get("fundManagerId").toString()));
            cert.appendChild(createElement(doc, "Direccion_banco", customerData.get("adress").toString()));
            cert.appendChild(createElement(doc, "Giro_actividad",  customerData.get("category").toString()));
            cert.appendChild(createElement(doc, "Nombre_cliente",  customerData.get("customerName").toString()));
            //cert.appendChild(createElement(doc, "Rut_cliente", login.get("username")));
            cert.appendChild(createElement(doc, "Rut_cliente", "17.683.826-4"));
            cert.appendChild(createElement(doc, "Direccion_cliente", customerData.get("customerAdress").toString()+" - "+customerData.get("customerCommune")));
            //cert.appendChild(createElement(doc, "Annio", requestBody.get("year").toString()));
            cert.appendChild(createElement(doc, "Annio", "2023"));
            cert.appendChild(createElement(doc, "Saldo_ahorro_con_ant_neg",  "0"));
            cert.appendChild(createElement(doc, "Saldo_ahorro_a_contar_neg", "0"));
            cert.appendChild(createElement(doc, "Inv_saldo_ahorro_negativo", "0"));
            cert.appendChild(createElement(doc, "Inv_monto_rent_positiva",   "0"));
            cert.appendChild(createElement(doc, "Inv_monto_rent_negativa",   "0"));
            cert.appendChild(createElement(doc, "Nombre_firma", customerData.get("manager").toString()));
            cert.appendChild(createElement(doc, "Rut_firma", customerData.get("managerId").toString()));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            Result destino = new StreamResult(writer);
            transformer.transform(source, destino);
            System.out.println(writer.toString());
            String xmlString = writer.toString();
            xmltoBase64(__exchange, xmlString, null);
            System.out.println("ACA ESTA EL BASE64 OUYES  ----    " + flows.get("base64XmlString"));
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

    }/*
      <Num_certificado>1</Num_certificado>

	  OK - issueDate                 <Fecha_emision>Santiago, 22 de Noviembre del 2021</Fecha_emision>
	  OK - fundManagerDescription    <Razon_social>SANTANDER ASSET MANAGEMENT S.A. ADMINISTRADORA GENERAL DE FONDOS</Razon_social> 
	  OK - fundManagerId             <Numero_rut>96.667.040-1</Numero_rut>
	  OK - address                   <Direccion_banco>BANDERA 140 P. 3 - SANTIAGO</Direccion_banco>
	  OK - category                  <Giro_actividad>ADMINISTRADORA GENERAL DE FONDOS</Giro_actividad>
	  OK - customerName              <Nombre_cliente>TEODORO RINSCHE NUÑEZ</Nombre_cliente>
	  OK - customerRut               <Rut_cliente>1.809.976-4</Rut_cliente>
	  OK - customerAdress            <Direccion_cliente>LOS RETAMOS 12 B.LAS FLORES - PIEDRA ROJA - COLINA</Direccion_cliente>
	  OK - parametro entrada yearid  <Annio>2020</Annio>

	  <Saldo_ahorro_con_ant_neg>0</Saldo_ahorro_con_ant_neg>
	  <Saldo_ahorro_a_contar_neg>2,064,477</Saldo_ahorro_a_contar_neg>
	  <Inv_saldo_ahorro_negativo>0</Inv_saldo_ahorro_negativo>
	  <Inv_monto_rent_positiva>0</Inv_monto_rent_positiva>
	  <Inv_monto_rent_negativa>0</Inv_monto_rent_negativa>

	  OK - manager                   <Nombre_firma>Daniel Ospina Arboleda</Nombre_firma>
	  OK - managerId                 <Rut_firma>21.724.053-0</Rut_firma>
    */ 

    public static Attr createAttr(Document doc, String nombreAtributo, String valor) {
        Attr atributo = doc.createAttribute(nombreAtributo);
        atributo.setValue(valor);
        return atributo;
    }

    public static Element createElement(Document doc, String nomElemento, String contenido) {
        Element elemento = doc.createElement(nomElemento);
        elemento.setTextContent(contenido);
        return elemento;
    }

    public void xmltoBase64(Exchange __exchange, String xmlString, File xmlDocument){
        Map<String, Object> flows = (Map<String, Object>)__exchange.getProperty("flows");

        if (xmlString != null && !xmlString.isEmpty()) {
            String base64XmlString = Base64.getEncoder().encodeToString(xmlString.getBytes());
            flows.put("base64XmlString", base64XmlString);
        }

        if (xmlDocument != null) {
            String base64XmlFile = convertFileToBase64(xmlDocument);
            flows.put("base64XmlString", base64XmlFile);
        }
    }

    private String convertFileToBase64(File xmlDocument) {
        try {
            byte[] fileBytes = readBytesFromFile(xmlDocument);
            return Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] readBytesFromFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        fis.read(fileBytes);
        fis.close();
        return fileBytes;
    }



}
