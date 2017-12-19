package soa.web;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.HashMap;


@Controller
public class SearchController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @RequestMapping("/")
    public String index() {
        return "index";
    }


    @RequestMapping(value="/search")
    @ResponseBody
    public Object search(@RequestParam("q") String q) {
        String delims = "[ ]+";
        String[] tokens = q.split(delims);
        String keywords = "";
        int max = 50;
        for(int i = 0; i < tokens.length; i++){
           if(tokens[i].startsWith("max:")){
              try{
                  max = Integer.parseInt(tokens[i].substring(tokens[i].indexOf(":")+1));
                  continue;
              }catch(NumberFormatException e){}
           }
           keywords += tokens[i] + " ";
        }
        
        Map headers = new HashMap<>();
        headers.put("CamelTwitterKeywords", keywords);
        headers.put("CamelTwitterCount", max);
        
        return producerTemplate.requestBodyAndHeaders("direct:search", "", headers);
    }
}