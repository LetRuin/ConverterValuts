package com.example.converter;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import java.io.IOException;

import java.io.StringReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.*;

public class ParseXML {
    public List<Valute> listOfValute = new ArrayList<Valute>();
    public String data = "";

    public void parseXML (String xml){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            Document document = builder.parse(is);

            Element elementNode = document.getDocumentElement();
            data = elementNode.getAttribute("Date");
            NodeList nodeList = elementNode.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++)
            {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element elem = (Element) node;
                    int numCode = 0;
                    if (elem.getElementsByTagName("NumCode").item(0).getChildNodes().item(0) != null)
                    {
                        String temp = elem.getElementsByTagName("NumCode").item(0).getChildNodes().item(0).getTextContent();
                        numCode = Integer.parseInt(temp);
                    }

                    String charCode = "";
                    if (elem.getElementsByTagName("CharCode").item(0).getChildNodes().item(0) != null)
                    {
                        charCode = elem.getElementsByTagName("CharCode").item(0).getChildNodes().item(0).getNodeValue();
                    }

                    int nominal = 0;
                    if (elem.getElementsByTagName("Nominal").item(0).getChildNodes().item(0) != null)
                    {
                        String temp = elem.getElementsByTagName("Nominal").item(0).getChildNodes().item(0).getTextContent();
                        nominal = Integer.parseInt(temp);
                    }

                    String name = "";
                    if (elem.getElementsByTagName("Name").item(0).getChildNodes().item(0) != null)
                    {
                        name = elem.getElementsByTagName("Name").item(0).getChildNodes().item(0).getTextContent();
                    }

                    double value = 0;
                    NumberFormat nf = NumberFormat.getInstance();
                    if (elem.getElementsByTagName("Value").item(0).getChildNodes().item(0) != null)
                    {
                        String temp = elem.getElementsByTagName("Value").item(0).getChildNodes().item(0).getTextContent();
                        value = nf.parse(temp).doubleValue();
                    }

                    double vunitRate = 0;
                    if (elem.getElementsByTagName("VunitRate").item(0).getChildNodes().item(0) != null)
                    {
                        String temp = elem.getElementsByTagName("VunitRate").item(0).getChildNodes().item(0).getTextContent();
                        vunitRate = nf.parse(temp).doubleValue();
                    }
                    listOfValute.add(new Valute(numCode, charCode, nominal, name, value, vunitRate));
                }
            }
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
