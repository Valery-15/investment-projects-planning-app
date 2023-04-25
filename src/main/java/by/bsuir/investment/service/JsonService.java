package by.bsuir.investment.service;

import by.bsuir.investment.domain.Indicators;
import by.bsuir.investment.dto.ResourcesNeedProjectInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.swing.text.FlowView;
import java.util.List;
import java.util.Map;

@Service
public class JsonService {
    public List<Indicators> parseIndicatorsJson(String indicatorsJson) {
        List<Indicators> indicators = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            indicators = mapper.readValue(indicatorsJson, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return indicators;
    }

    public Map<Integer, Float> parseCashFlowsJson(String cashFlowsJson) {
        Map<Integer, Float> cashFlows = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            cashFlows = mapper.readValue(cashFlowsJson, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return cashFlows;
    }

    public List<String> parseProjectsCodesJson(String projectsCodesJson) {
        List<String> projectsCodes = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            projectsCodes = mapper.readValue(projectsCodesJson, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return projectsCodes;
    }

    public String stringifyResourcesNeedProjectsInfo(List<ResourcesNeedProjectInfo> projectsInfo){
        ObjectMapper mapper = new ObjectMapper();
        String projectsInfoJson = null;
        try {
            projectsInfoJson = mapper.writeValueAsString(projectsInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return projectsInfoJson;
    }

    public List<ResourcesNeedProjectInfo> parseResourcesNeedProjectsInfoJson(String projectsInfoJson){
        ObjectMapper mapper = new ObjectMapper();
        List<ResourcesNeedProjectInfo> projectsInfo = null;
        try {
            projectsInfo = mapper.readValue(projectsInfoJson, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return projectsInfo;
    }
}
