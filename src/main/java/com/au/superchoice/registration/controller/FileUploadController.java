package com.au.superchoice.registration.controller;

import com.au.superchoice.registration.model.MemberRegistration;
import com.au.superchoice.registration.service.ProcessFileService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@NoArgsConstructor
@RequestMapping("/member")
public class FileUploadController {

    private static final String GROUP_BY_ERROR = "MRA Could not process  choose option groupBy employer or fund:  you have chosen File Name  ${fileName} ! and groupBy ${groupBy}";
    private static final String EXCEPTION = "MRA Could not upload the file:  ${fileName} !  ${exception}";


    @Autowired
    ProcessFileService processFileService;

    @PostMapping(path = "/registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<MemberRegistration>>> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("groupBy") String groupBy, Model model ) {
        log.info("MRA  in FileUploadController-->uploadFile ------>" + file.getOriginalFilename());

        try {
             if(file.getOriginalFilename().endsWith(".xml")){
               file=processFileService.XmlToCSV(file);
            }
            if ("employer".equalsIgnoreCase(groupBy)) {
                return ResponseEntity.ok(processFileService.groupByEmployer(file));
            } else if ("fund".equalsIgnoreCase(groupBy)) {
                return ResponseEntity.ok(processFileService.groupByFund(file));
            } else {
                return  ResponseEntity.badRequest().body(setError(file, groupBy, model));

            }
        } catch (Exception exception) {
            log.error("exception", exception);
            return  ResponseEntity.badRequest().body(setException(file,model,exception));
        }
    }

    private Map<String, List<MemberRegistration>> setError(MultipartFile file, String groupBy, Model model) {
      String replace1 = StringUtils.replace(GROUP_BY_ERROR, "${fileName}", file.getOriginalFilename());
      String replace2 = StringUtils.replace(replace1, "${groupBy}", groupBy == null || groupBy.isEmpty() ? "is blank or empty please enter the value employer or fund" : groupBy);
        model.addAttribute("message", replace2);
        model.addAttribute("status", false);
        Map<String, List<MemberRegistration>> map = new HashMap<>();
        map.put(model.toString(), Arrays.asList());
        return map;
    }

    private Map<String, List<MemberRegistration>> setException(MultipartFile file, Model model, Exception exception) {
       String replace1 = StringUtils.replace(EXCEPTION, "${fileName}", file.getOriginalFilename());
        String replace2 = StringUtils.replace(replace1, "${exception}", exception.getMessage());
        model.addAttribute("message", replace2);
        model.addAttribute("status", false);
        Map<String, List<MemberRegistration>> map = new HashMap<>();
        map.put(model.toString(), Arrays.asList());
        return map;
    }


}
