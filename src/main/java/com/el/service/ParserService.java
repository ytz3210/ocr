package com.el.service;

import com.el.exception.UserException;
import com.el.parse.*;
import com.el.util.FileUtils;
import com.el.util.HttpUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class ParserService {
    @Resource
    RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(ParserService.class);

    public static Map<String, TextParser> map = new HashMap<String, TextParser>();

    static {
        map.put("APL", new APLParser());
        map.put("MSC", new MSCParser());
        map.put("MAERSK", new MaerskParser());
        map.put("ONE", new ONEParser());
        map.put("YM", new YMParser());
        map.put("HAMBURG", new HamburgParser());
        map.put("HAPAG", new HapagParser());
        map.put("COSCO_S", new COSCO_SParser());
        map.put("COSCO_W", new COSCO_WParser());
        map.put("EMC", new EMCParser());
        map.put("COSU", new COSUParser());
        map.put("JSZC", new JSZCParser());
    }

    /**
     * 解析单证
     * @param fileName
     * @param in
     * @return parser.getContext()
     * @author Yangtz
     * @date 2019/8/12
     */
    public Map<String, Object> parseText(String fileName, InputStream in) throws UserException, IOException {
        String text = fileExtractText(fileName, in);
        String company = getCompany(text);
        TextParser parser = map.get(company);
        try {
            parser.parse(text);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new UserException("单证解析错误，请检查是否符合格式要求！");
        }

        return parser.getContent();
    }

    /**
     * 查看此文件应该进行哪种方式的解析
     * @param text
     * @return company
     * @author Yangtz
     * @date 2019/8/12
     */
    private String getCompany(String text) throws UserException {
        String company = null;
        if (text.contains("APLU")) {
            company = "APL";
        } else if (text.toUpperCase().contains("MEDITERRANEAN")) {
            company = "MSC";
        } else if (text.toUpperCase().contains("MAERSK")) {
            company = "MAERSK";
        } else if (text.toUpperCase().contains("HAPAG")) {
            company = "HAPAG";
        } else if (text.toUpperCase().contains("HAMBURG")) {
            company = "HAMBURG";
        } else if (text.toUpperCase().contains("EVERGREEN")) { // EMC
            company = "EMC";
        } else if (text.toUpperCase().contains("航线") && text.contains("堆场")) {  //
            company = "YM";
        } else if (text.toUpperCase().contains("ONE")) {  //
            company = "ONE";
        } else if (text.contains("中远海运")) {
            if (text.contains("中远海运集运")) {
                company = "COSU";
            } else {
                company = "COSCO_S";
            }
        } else if (text.contains("江苏众诚国际物流有限公司")) {
            company = "JSZC";
        } else {
            throw new UserException("单证不匹配，请重新选择或确认");
        }
        //else必须是异常
        return company;
    }

    /**
     * 查看此文件属于那种文件类型，并进行相应文件类型的内容解析
     * @param filePath
     * @param in
     * @return text
     * @author Yangtz
     * @date 2019/8/12
     */
    private String fileExtractText(String filePath, InputStream in) throws IOException {
        String text = "";
        try {
            if (filePath.toLowerCase().endsWith("doc")) {
                WordExtractor wordExtractor = new WordExtractor(in);
                text = wordExtractor.getText();
                wordExtractor.close();
            } else if (filePath.toLowerCase().endsWith("docx")) {
                POIXMLTextExtractor extractor = new XWPFWordExtractor(new XWPFDocument(in));
                text = extractor.getText();
                extractor.close();
            } else if (filePath.toLowerCase().endsWith("pdf")) {
                PDDocument pdDocument = PDDocument.load(in);
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setSortByPosition(true);
                text = stripper.getText(pdDocument);
                pdDocument.close();
            }
            String suffix = filePath.substring(filePath.indexOf(".") + 1);

            if ("jpg, jpeg, png, bpm".contains(suffix.toLowerCase())) {
                text = parseImg(in);
            }
        } catch (IOException e) {
            throw new IOException("文件解析异常，请重新选择文件或稍后重试");
        } finally {
            FileUtils.close(in, null);
        }
        return text;
    }

    /**
     * Baidu ocr 文字识别
     *
     * @param in
     * @return
     */
    private String parseImg(InputStream in) {
        String access_token = HttpUtils.getAuth();
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic?access_token=" + access_token;
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileUtils.copy(in, out);
        String img = Base64.getEncoder().encodeToString(out.toByteArray());
        map.add("image", img);
        map.add("detect_direction", "true");
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, header);
        String resp = restTemplate.postForObject(url, httpEntity, String.class);
        return resp;
    }

}
