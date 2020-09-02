package com.au.superchoice.registration.service;

import com.au.superchoice.registration.model.EmployerForMember;
import com.au.superchoice.registration.model.FundForMember;
import com.au.superchoice.registration.model.Member;
import com.au.superchoice.registration.model.MemberRegistration;
import com.au.superchoice.registration.model.TransactionDetails;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
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
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.groupingBy;


@Service
@Slf4j
@ToString
public class MemberRegistrationsService {


    private static final Pattern CSVPattern = Pattern.compile(",");


    public Map<String, List<MemberRegistration>> groupByEmployer(MultipartFile file) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("MRA  in FileProcessingService-->processGroupByEmployer ------>");
        }
        BufferedReader bufferedReader = getBufferedReader(file);
        Map<String, List<MemberRegistration>> groupedByEmployer = bufferedReader
                .lines()
                .skip(1)
                .map(line -> getMemberRegistration(CSVPattern, line))
                .collect(groupingBy(memberRegistration -> memberRegistration.getTransactionDetails().getEmployerForMember().getABN()));

        if (log.isDebugEnabled()) {
            log.debug("MRA  in ProcessFileService-->groupByEmployer ------>output " + groupedByEmployer);
        }
        return groupedByEmployer;

    }


    public Map<String, List<MemberRegistration>> groupByFund(MultipartFile file) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("MRA  in FileProcessingService-->processGroupByFund ------>");
        }
        BufferedReader bufferedReader = getBufferedReader(file);
        Map<String, List<MemberRegistration>> groupedByFund = bufferedReader
                .lines()
                .skip(1)
                .map(line -> getMemberRegistration(CSVPattern, line))
                .collect(groupingBy(memberRegistration -> memberRegistration.getTransactionDetails().getFundForMember().getIdentifier()));

        if (log.isDebugEnabled()) {
            log.debug("MRA  in ProcessFileService-->groupByFund ------>output " + groupedByFund);
        }
        return groupedByFund;

    }


    @VisibleForTesting
    private MemberRegistration getMemberRegistration(Pattern pattern, String line) {
        String[] arr = pattern.split(line);
        return new MemberRegistration(arr[0], new TransactionDetails(new EmployerForMember(arr[1], arr[2]), new FundForMember(arr[3],
                arr[4]), new Member(arr[5], arr[6], arr[7], arr[8], arr[9], arr[10], arr[11], arr[12],
                arr[13], arr[14], arr[15], arr[16], arr[17])));
    }

    @VisibleForTesting
    private BufferedReader getBufferedReader(MultipartFile file) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes());
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }


    public MultipartFile convertXMLtoCSV(MultipartFile file) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        File stylesheet = new File("sample.xsl");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        StreamSource styleSource = new StreamSource(stylesheet);
        Document xmlDocument = factory.newDocumentBuilder().parse(new InputSource(byteArrayInputStream));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        Transformer transformer = transformerFactory.newTransformer(styleSource);

        Source source = new DOMSource(xmlDocument);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Result outputTarget = new StreamResult(byteArrayOutputStream);

        transformer.transform(source, outputTarget);

        return new MockMultipartFile("file", file.getName(), "text/plain", byteArrayOutputStream.toByteArray());


    }
}
