package com.myth.xml;

import com.myth.time.GetRunTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;


/**
 * Created by Myth on 2017/2/3
 * 使用 jdom 读取xml文件
 * @TODO 解析xml文档
 */
public class DOMParser {

    private DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

    public Document parse(String filePath) {
        Document document = null;
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            InputStream is = this.getClass().getResourceAsStream(filePath);
            document = builder.parse(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    public static void main(String[] args) throws Exception{
        GetRunTime time = GetRunTime.INSTANCE;
        time.startCount();
        DOMParser parser = new DOMParser();
        Document document = parser.parse("/books.xml");
        //get root element
        Element rootElement = document.getDocumentElement();

        //traverse child elements
        NodeList nodes = rootElement.getChildNodes();
        for (int i=0; i < nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) node;
                //threads child element
            }
        }

        NodeList nodeList = rootElement.getElementsByTagName("book");
        if(nodeList != null) {
            for (int i = 0 ; i < nodeList.getLength(); i++) {
                Element element = (Element)nodeList.item(i);
                String id = element.getAttribute("id");
                NodeList list = element.getElementsByTagName("title");
                if(list!=null){
                    for (int j = 0 ; j < list.getLength(); j++) {
                        Element elements = (Element)list.item(j);
                        String d = elements.getNodeValue();
                        System.out.println(elements.getTagName()+"---"+d);
                    }
                }
                System.out.println(id);
            }
        }
        time.endCount("读取XML成功");

    }

}