package com.au.superchoice.registration.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration(classes = {MemberRegistrationsService.class})
@RunWith(SpringRunner.class)
public class MemberRegistrationsServiceTest {
    @Autowired
    private MemberRegistrationsService processFileService;

    @Test
    public void testGroupByEmployerUsingFile() throws IOException {
        assertEquals("This test is with the actual sample.csv", 3,
                this.processFileService.groupByEmployer(new MockMultipartFile("file", new FileInputStream(new File("sample.csv")))).size());

    }


    @Test
    public void testGroupByFundWithoutFile() throws IOException {
        assertEquals("This test is without the actual file ", 0,
                this.processFileService.groupByFund(new MockMultipartFile("file",
                        new byte[]{65, 65})).size()
        );

    }

    @Test
    public void testGroupByFundUsingFile() throws IOException {
        assertEquals("This test is with the actual sample.csv", 2,
                this.processFileService.groupByFund(new MockMultipartFile("file", new FileInputStream(new File("sample.csv")))).size());

    }


    @Test
    public void testGroupByEmployerWithoutFile() throws IOException {
        assertEquals("This test is without the actual file ", 0,
                this.processFileService.groupByEmployer(new MockMultipartFile("file",
                        new byte[]{65, 65})).size()
        );

    }

    @Test
    public void testXmlToCSV() throws IOException, SAXException, TransformerException, ParserConfigurationException {
        File stylesheet = new File("sample.xsl");
        File xmlSource = new File("sample.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlSource);
        StreamSource styleSource = new StreamSource(stylesheet);
        Transformer transformer = TransformerFactory.newInstance().newTransformer(styleSource);
        Source source = new DOMSource(document);
        Result outputTarget = new StreamResult(new File("ccc.csv"));
        transformer.transform(source, outputTarget);
        assertNotNull(outputTarget);

    }


}

